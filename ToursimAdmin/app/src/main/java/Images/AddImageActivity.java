package Images;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.toursimadmin.R;

import java.io.IOException;

import hotels.AddHotelActivity;

public class AddImageActivity extends AppCompatActivity {
    private ImageView addImage;
    private Button add;

    private ActivityResultLauncher<Intent> activityResultLauncherForImage;
    private String imagePathStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);

        addImage = findViewById(R.id.add_image_btn_toursim);
        add = findViewById(R.id.add_image_crud);

        registerActivityResultForImage();

        // add image
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tourismId = getIntent().getIntExtra("tourism_id", 0);
                ImageModel image = new ImageModel();
                image.setTourismId(Integer.toString(tourismId));
                ImageCrud crud = new ImageCrud(AddImageActivity.this);
                crud.createImage(image, imagePathStr);

            }
        });

        // select image
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(AddImageActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();

                } else {
                    requestStoragePermission();
                }
            }
        });
    }

    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            // this tells the user why this permission is needed

        } else {
            // this will request permission Manifest.permission.READ_EXTERNAL_STORAGE
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    private void pickImageFromGallery(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        activityResultLauncherForImage.launch(intent);
    }

    private void registerActivityResultForImage(){
        activityResultLauncherForImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        Intent data = result.getData();
                        if(resultCode == RESULT_OK && data != null){
                            // the user has selected an image
                            try {
                                Bitmap selectedImage = MediaStore.Images.Media.getBitmap(
                                        getContentResolver(), data.getData()
                                );
                                Uri imgUri = data.getData();

                                String[] imagePathColumn = {MediaStore.Images.Media.DATA};
                                Cursor cursor = getContentResolver().query(imgUri, imagePathColumn, null, null, null);
                                if(cursor == null)
                                    return;

                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(imagePathColumn[0]);
                                imagePathStr = cursor.getString(columnIndex);
                                cursor.close();
                                addImage.setImageBitmap(selectedImage);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == 1){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(AddImageActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                pickImageFromGallery();
            } else {
                Toast.makeText(AddImageActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
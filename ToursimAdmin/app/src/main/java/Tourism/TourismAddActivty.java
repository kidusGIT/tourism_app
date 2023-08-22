package Tourism;

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
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.toursimadmin.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

public class TourismAddActivty extends AppCompatActivity {
    private Button uploadImage, createTourism;
    private EditText desc, name;
    private ImageView uploadedImage;

    private ActivityResultLauncher<Intent> activityResultLauncherForImage;
    private String imgPath, imagePathStr;
    private Bitmap bitmap;
    private Uri imgUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourism_add_activty);

        uploadImage = findViewById(R.id.upload_image_btn);
        uploadedImage = findViewById(R.id.toursim_image_cover);
        createTourism = findViewById(R.id.create_toursim_btn);
        desc = findViewById(R.id.toursim_desc);
        name = findViewById(R.id.tourism_name);

        uploadedImage.setClipToOutline(true);

        registerActivityResultForImage();

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(TourismAddActivty.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
//                     Toast.makeText(MainActivity.this, "You have all ready granted", Toast.LENGTH_SHORT).show();
                    pickImageFromGallery();

                } else {
                    requestStoragePermission();
                }
            }
        });

        createTourism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(name.getText().toString() == null || name.getText().toString().equals("")){
                    Toast.makeText(TourismAddActivty.this, "Field Required", Toast.LENGTH_SHORT).show();
                } else {
                    TourismModel model = new TourismModel();
                    model.setDesc(desc.getText().toString());
                    model.setName(name.getText().toString());
                    TourismCrud tourismCrud = new TourismCrud(TourismAddActivty.this);
                    tourismCrud.createTourism(model, imagePathStr);

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
                                uploadedImage.setImageBitmap(selectedImage);

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
                Toast.makeText(TourismAddActivty.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                pickImageFromGallery();
            } else {
                Toast.makeText(TourismAddActivty.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Compress the image and return the string
    private String imageToString() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        byte[] imgByte = outputStream.toByteArray();
        String imgStr = "";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            imgStr = Base64.getEncoder().encodeToString(imgByte);
        }

        return imgStr;
    }
}
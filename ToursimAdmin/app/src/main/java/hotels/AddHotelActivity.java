package hotels;

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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.toursimadmin.R;

import java.io.IOException;

import Tourism.DetailActivity;

public class AddHotelActivity extends AppCompatActivity {
    private Button addTourism;
    private EditText desc, name, latitude, longtude;
    private ImageButton uploadImage;

    private ActivityResultLauncher<Intent> activityResultLauncherForImage;
    private String imagePathStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotel);

        addTourism = findViewById(R.id.create_hotel_btn);
        name = findViewById(R.id.hotel_name);
        desc = findViewById(R.id.hotel_desc);
        uploadImage = findViewById(R.id.hotel_cover_image);
        latitude = findViewById(R.id.latitude_edit);
        longtude = findViewById(R.id.longitude_edt);

        registerActivityResultForImage();

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(AddHotelActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();

                } else {
                    requestStoragePermission();
                }
            }
        });

        addTourism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tourismId = getIntent().getIntExtra("tourism_id", 0);
                if(tourismId != 0){
                    HotelModel hotelModel = new HotelModel();
                    hotelModel.setName(name.getText().toString());
                    hotelModel.setDesc(desc.getText().toString());
                    hotelModel.setToursimId(Integer.toString(tourismId));
                    hotelModel.setLongtude(longtude.getText().toString());
                    hotelModel.setLatitude(latitude.getText().toString());

                    HotelCrud hotelCrud = new HotelCrud(AddHotelActivity.this);
                    hotelCrud.createHotel(hotelModel, imagePathStr);

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
                                uploadImage.setImageBitmap(selectedImage);

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
                Toast.makeText(AddHotelActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                pickImageFromGallery();
            } else {
                Toast.makeText(AddHotelActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
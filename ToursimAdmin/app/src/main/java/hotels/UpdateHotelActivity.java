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
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.toursimadmin.ImageUrl;
import com.example.toursimadmin.R;
import com.example.toursimadmin.RetrofitConfig;

import java.io.IOException;

import Tourism.DetailActivity;
import Tourism.TourismApiCall;
import Tourism.TourismModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class UpdateHotelActivity extends AppCompatActivity {
    private Button updateTourism;
    private EditText desc, name, latitude, longtude;
    private ImageButton uploadImage;
    private String imgUrl = ImageUrl.imageUrl;

    private ActivityResultLauncher<Intent> activityResultLauncherForImage;
    private String toursimId;
    private String imagePathStr;
    private String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hotel);

        updateTourism = findViewById(R.id.create_hotel_btn_update);
        name = findViewById(R.id.hotel_name_update);
        desc = findViewById(R.id.hotel_desc_update);
        longtude = findViewById(R.id.longitude_edt_update);
        latitude = findViewById(R.id.latitude_edit_update);
        uploadImage = findViewById(R.id.hotel_cover_image_update);

        registerActivityResultForImage();

        int id = getIntent().getIntExtra("id", 0);
        setComponents(id);

        updateTourism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int id = getIntent().getIntExtra("id", 0);
                if(id != 0){
                    HotelModel hotelModel = new HotelModel();
                    hotelModel.setName(name.getText().toString());
                    hotelModel.setDesc(desc.getText().toString());
                    hotelModel.setToursimId(toursimId);
                    hotelModel.setLongtude(longtude.getText().toString());
                    hotelModel.setLatitude(latitude.getText().toString());
                    hotelModel.setImg(image);

                    HotelCrud hotelCrud = new HotelCrud(UpdateHotelActivity.this);
                    hotelCrud.updateHotel(hotelModel, imagePathStr, id);
                }
            }
        });

        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(UpdateHotelActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();

                } else {
                    requestStoragePermission();
                }
            }
        });
    }

    private void setComponents(int id){
        if(id != 0){
            Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
            HotelApiCall hotelApiCall = retrofit.create(HotelApiCall.class);
            Call<HotelModel> call = hotelApiCall.getDetailHotel(id);

            call.enqueue(new Callback<HotelModel>() {
                @Override
                public void onResponse(Call<HotelModel> call, Response<HotelModel> response) {
                    name.setText(response.body().getName());
                    desc.setText(response.body().getDesc());
                    latitude.setText(response.body().getLatitude());
                    longtude.setText(response.body().getLongtude());

                    image = response.body().getImg();

                    toursimId = response.body().getToursimId();

                    if(response.body().getImg().equals("")) {
                        Glide.with(UpdateHotelActivity.this)
                                .asBitmap()
                                .load("https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg")
                                .into(uploadImage);
                        uploadImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    } else {
                        String img = imgUrl + response.body().getImg();
                        Glide.with(UpdateHotelActivity.this)
                                .asBitmap()
                                .load(img.replace("\\", "/"))
                                .into(uploadImage);
                        uploadImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    }
                }

                @Override
                public void onFailure(Call<HotelModel> call, Throwable t) {

                }
            });
        }
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
                Toast.makeText(UpdateHotelActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                pickImageFromGallery();
            } else {
                Toast.makeText(UpdateHotelActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
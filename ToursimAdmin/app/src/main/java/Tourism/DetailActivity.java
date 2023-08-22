package Tourism;

import static android.app.PendingIntent.getActivity;

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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.toursimadmin.ImageUrl;
import com.example.toursimadmin.R;
import com.example.toursimadmin.RetrofitConfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import Images.ImageMainActivity;
import hotels.HotelActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class DetailActivity extends AppCompatActivity {
    private Button uploadImage, updateTourism, hotelList, imageList;
    private EditText desc, name;
    private ImageView uploadedImage;

    private ActivityResultLauncher<Intent> activityResultLauncherForImage;
    private String imgPath, imagePathStr;
    private Bitmap bitmap;
    private Uri imgUri;
    private String imgUrl = ImageUrl.imageUrl;
    private int tourismId = 0;
    private Handler handler;

    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        uploadImage = findViewById(R.id.upload_image_btn_detail);
        uploadedImage = findViewById(R.id.toursim_image_cover_detail);
        updateTourism = findViewById(R.id.create_toursim_btn_detail);
        desc = findViewById(R.id.toursim_desc_detail);
        name = findViewById(R.id.tourism_name_detail);
        hotelList = findViewById(R.id.hotel_list_btn);
        imageList = findViewById(R.id.image_List_btn);

        uploadedImage.setClipToOutline(true);

        int id = getIntent().getIntExtra("id", 0);
        setComponents(id);
        registerActivityResultForImage();
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(DetailActivity.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();

                } else {
                    requestStoragePermission();
                }
            }
        });

        // update toursim
        updateTourism.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TourismModel model = new TourismModel();
                model.setDesc(desc.getText().toString());
                model.setName(name.getText().toString());
                model.setCover_image(getImgPath());
                TourismCrud tourismCrud = new TourismCrud(DetailActivity.this);
                int id = getIntent().getIntExtra("id", 0);
                tourismCrud.updateTourism(model, id, imagePathStr);

            }
        });

        // hotel List
        hotelList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tourismId = getIntent().getIntExtra("id", 0);
                Intent intent = new Intent(DetailActivity.this, HotelActivity.class);
                intent.putExtra("tourismId", tourismId);
                startActivity(intent);
            }
        });

        // image List
        imageList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tourismId = getIntent().getIntExtra("id", 0);
                Intent intent = new Intent(DetailActivity.this, ImageMainActivity.class);
                intent.putExtra("tourism_id", tourismId);
                startActivity(intent);
            }
        });

    }

    // set components
    private void setComponents(int id){
       if(id != 0){
           Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
           TourismApiCall tourismApiCall = retrofit.create(TourismApiCall.class);
           Call<TourismModel> call = tourismApiCall.getTourism(id);

           call.enqueue(new Callback<TourismModel>() {
               @Override
               public void onResponse(Call<TourismModel> call, Response<TourismModel> response) {
                   name.setText(response.body().getName());
                   desc.setText(response.body().getDesc());
                   if(response.body().getCover_image().equals("")) {
                       Glide.with(DetailActivity.this)
                               .asBitmap()
                               .load("https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg")
                               .into(uploadedImage);
                   } else {
                       String img = imgUrl + response.body().getCover_image();
                       Glide.with(DetailActivity.this)
                               .asBitmap()
                               .load(img.replace("\\", "/"))
                               .into(uploadedImage);
//                        new FetchImage(img).start();


                       setImgPath(img.replace("\\", "/"));
                   }

                   setTourismId(response.body().getId());
               }

               @Override
               public void onFailure(Call<TourismModel> call, Throwable t) {

               }
           });

       }
    }

    public void setTourismId(int tourismId) {
        this.tourismId = tourismId;
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
                                imgUri = data.getData();
                                String[] imagePathColumn = {MediaStore.Images.Media.DATA};
                                Cursor cursor = getContentResolver().query(imgUri, imagePathColumn, null, null, null);
                                assert cursor != null;
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
                Toast.makeText(DetailActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
                pickImageFromGallery();
            } else {
                Toast.makeText(DetailActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    class FetchImage extends Thread {
        String url;
        public FetchImage(String url){
            this.url = url;
        }

        @Override
        public void run() {
            super.run();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        String img = imgUrl + url;
                        URL url = new URL(img.replace("\\", "/"));
                        Bitmap bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        uploadedImage.setImageBitmap(bitmap);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
package com.example.tourismapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

import Hotels.HotelModel;
import Hotels.HotelsApiCall;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HotelDetailActivity extends AppCompatActivity {
    private TextView name, desc;
    private ImageView image;
    private ImageButton location;

    private static final String imgUrl = ImageUrl.imageUrl;
    private String hotelName = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        name = findViewById(R.id.hotel_detail_name);
        desc = findViewById(R.id.hotel_detail_desc);
        image = findViewById(R.id.hotel_detail_image);
        location = findViewById(R.id.go_to_location);

        setStatusBarTransparent();
        getAllDetailHotels();

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double longitude = getIntent().getDoubleExtra("longitude", 0.0);
                double latitude = getIntent().getDoubleExtra("latitude", 0.0);

                Intent intent = new Intent(HotelDetailActivity.this, MapActivity.class);
                intent.putExtra("longitude", longitude);
                intent.putExtra("latitude", latitude);
                intent.putExtra("name", hotelName);
                HotelDetailActivity.this.startActivity(intent);
//                Toast.makeText(HotelDetailActivity.this, "context: ", Toast.LENGTH_SHORT).show();

            }
        });

    }

    private void getAllDetailHotels() {
        int id = getIntent().getIntExtra("id", 0);
        if(id != 0){
            Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
            HotelsApiCall hotelsApiCall = retrofit.create(HotelsApiCall.class);

            Call<HotelModel> call = hotelsApiCall.getDetailHotel(id);
            call.enqueue(new Callback<HotelModel>() {
                @Override
                public void onResponse(Call<HotelModel> call, Response<HotelModel> response) {
                    if(response.isSuccessful()){
                        initComponents(response.body());
                    }

                }

                @Override
                public void onFailure(Call<HotelModel> call, Throwable t) {

                }
            });
        }
    }

    private void initComponents(HotelModel body) {
        name.setText(body.getName());
        desc.setText(body.getDesc());
        hotelName = body.getName();
        String imageStr = body.getImg();
        if(imageStr.equals("")) {
            Glide.with(this)
                    .asBitmap()
                    .load("https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg")
                    .into(image);
        } else {
            String img = imgUrl + imageStr;
            Glide.with(this)
                    .asBitmap()
                    .load(img.replace("\\", "/"))
                    .into(image);
//            Log.d("Images", "onBindViewHolder: " + tourismModelList.get(position).getCover_image());

        }
    }

    private void setStatusBarTransparent(){
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }
}
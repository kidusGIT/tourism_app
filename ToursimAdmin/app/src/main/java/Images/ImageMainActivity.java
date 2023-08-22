package Images;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.toursimadmin.R;
import com.example.toursimadmin.RetrofitConfig;
import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import hotels.HotelActivity;
import hotels.HotelAdapter;
import hotels.HotelApiCall;
import hotels.HotelModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageMainActivity extends AppCompatActivity {
    private FloatingActionButton fab;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iamge_main);
        fab = findViewById(R.id.add_image);
        recyclerView = findViewById(R.id.image_list_recycle);

        displayImages();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tourismId = getIntent().getIntExtra("tourism_id", 0);
                Intent intent = new Intent(ImageMainActivity.this, AddImageActivity.class);
                intent.putExtra("tourism_id", tourismId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        displayImages();
    }

    private void displayImages(){
        int tourismId = getIntent().getIntExtra("tourism_id", 0);
        if(tourismId != 0){
            Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
            ImagesApiCall imagesApiCall = retrofit.create(ImagesApiCall.class);
            Call<List<ImageModel>> call = imagesApiCall.getAllImages(tourismId);

            call.enqueue(new Callback<List<ImageModel>>() {
                @Override
                public void onResponse(Call<List<ImageModel>> call, Response<List<ImageModel>> response) {
                    if(response.code() != 200){
                        Toast.makeText(ImageMainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setComponents(response.body());

                }

                @Override
                public void onFailure(Call<List<ImageModel>> call, Throwable t) {
                    Toast.makeText(ImageMainActivity.this, "", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void setComponents(List images){
        ImageAdapter adapter = new ImageAdapter(ImageMainActivity.this);
        adapter.setImageList(images);
        adapter.notifyDataSetChanged();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
    }
}
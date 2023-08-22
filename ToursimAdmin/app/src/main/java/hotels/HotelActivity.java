package hotels;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.toursimadmin.MainActivity;
import com.example.toursimadmin.R;
import com.example.toursimadmin.RetrofitConfig;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import Tourism.TourismAdapter;
import Tourism.TourismApiCall;
import Tourism.TourismModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HotelActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel);
        recyclerView = findViewById(R.id.hotel_recycler_view);
        fab = findViewById(R.id.add_hotels);

        int tourismId = getIntent().getIntExtra("tourismId", 0);
        getAllHotels(tourismId);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int tourismId = getIntent().getIntExtra("tourismId", 0);
                Intent intent = new Intent(HotelActivity.this, AddHotelActivity.class);
                intent.putExtra("tourism_id", tourismId);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        int tourismId = getIntent().getIntExtra("tourismId", 0);
        getAllHotels(tourismId);
    }

    private void getAllHotels(int tourismId){
        if(tourismId != 0){
            Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
            HotelApiCall hotelApiCall = retrofit.create(HotelApiCall.class);
            Call<List<HotelModel>> call = hotelApiCall.getCertainHotels(tourismId);

            call.enqueue(new Callback<List<HotelModel>>() {
                @Override
                public void onResponse(Call<List<HotelModel>> call, Response<List<HotelModel>> response) {
                    if(response.code() != 200){
                        Toast.makeText(HotelActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setAllHotels(response.body());
                }

                @Override
                public void onFailure(Call<List<HotelModel>> call, Throwable t) {

                }
            });
        }
    }

    private void setAllHotels(List hotels){
        HotelAdapter adapter = new HotelAdapter(this);
        adapter.setHotelList(hotels);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
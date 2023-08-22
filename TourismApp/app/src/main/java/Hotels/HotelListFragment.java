package Hotels;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.tourismapp.ImageUrl;
import com.example.tourismapp.MainActivity;
import com.example.tourismapp.R;
import com.example.tourismapp.RetrofitConfig;

import java.util.List;

import Toursim.TourismAdapter;
import Toursim.TourismApiCall;
import Toursim.TourismModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HotelListFragment extends Fragment {
    private RecyclerView recyclerView;
    private ImageView image;
    private TextView name;
    private LinearLayout noHotel;

    private Context context;
    private static final String imgUrl = ImageUrl.imageUrl;

    // room
    private HotelRepository hotelRepository;
    private HotelViewModel hotelViewModel;

    private static final String KEY_STATE = "state";
    private int orentationInt = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hotel_list, container, false);
        context = getActivity().getApplicationContext();

        recyclerView = (RecyclerView) view.findViewById(R.id.hotel_recycler_view);
        image = (ImageView) view.findViewById(R.id.tourism_cover_image_fragment);
        name = (TextView) view.findViewById(R.id.hotel_list_name);
        noHotel = (LinearLayout) view.findViewById(R.id.no_hotel);

        int id = getActivity().getIntent().getIntExtra("id", 0);
        hotelViewModel = new ViewModelProvider(getActivity()).get(HotelViewModel.class);
        hotelRepository = new HotelRepository(getActivity().getApplication(), id);

        setComponents();
        // Inflate the layout for this fragment
        return view;
    }

    private void setComponents(){
        int id = getActivity().getIntent().getIntExtra("id", 0);
        if(id != 0){
            Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
            HotelsApiCall hotelsApiCall = retrofit.create(HotelsApiCall.class);

            Call<List<HotelModel>> call = hotelsApiCall.getCertainHotels(id);
            call.enqueue(new Callback<List<HotelModel>>() {
                @Override
                public void onResponse(Call<List<HotelModel>> call, Response<List<HotelModel>> response) {
                    hotelRepository.delete();
                    hotelRepository.insert(response.body());
                    setRecyclerView(response.body());
                }

                @Override
                public void onFailure(Call<List<HotelModel>> call, Throwable t) {
                    setRecyclerViewOffline(id);
                }
            });

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        context = getActivity().getApplicationContext();
        setComponents();

    }

    private void setRecyclerViewOffline(int id){
        hotelViewModel.getHotelList().observe(this, new Observer<List<HotelModel>>() {
            @Override
            public void onChanged(List<HotelModel> hotelModels) {
                if(hotelModels.size() == 0){
                    noHotel.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.INVISIBLE);
                    name.setVisibility(View.INVISIBLE);
                } else {
                    String nameStr = getActivity().getIntent().getStringExtra("name");
                    String hotelName = "Hotels around " + nameStr;
                    name.setText(hotelName);

                    Toast.makeText(context, hotelModels.get(2).getName(), Toast.LENGTH_SHORT).show();

                    HotelAdapter adapter = new HotelAdapter(getActivity().getApplicationContext());
                    adapter.setHotelList(hotelModels);

                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    recyclerView.setNestedScrollingEnabled(false);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(layoutManager);
                }
            }
        });
    }

    private void setRecyclerView(List<HotelModel> hotelModels){
        String imageStr = "";
        if(getActivity().getIntent().hasExtra("image")){
            imageStr = getActivity().getIntent().getStringExtra("image");
        }
        if(imageStr.equals("")) {
            Glide.with(getActivity().getApplicationContext())
                    .asBitmap()
                    .load("https://images.pexels.com/photos/327098/pexels-photo-327098.jpeg")
                    .into(image);
        } else {
            String img = imgUrl + imageStr;
            Glide.with(context)
                    .asBitmap()
                    .load(img.replace("\\", "/"))
                    .into(image);
        }

        if(hotelModels.size() == 0){
            noHotel.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
            name.setVisibility(View.INVISIBLE);
        } else {
            String nameStr = getActivity().getIntent().getStringExtra("name");
            String hotelName = "Hotels around " + nameStr;
            name.setText(hotelName);

            HotelAdapter adapter = new HotelAdapter(getActivity().getApplicationContext());
            adapter.setHotelList(hotelModels);

//            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setAdapter(adapter);
//            recyclerView.setLayoutManager(layoutManager);
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
                recyclerView.setLayoutManager( new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            } else {
                recyclerView.setLayoutManager( new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));
            }
        }

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            setComponents();

        } else if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setComponents();
        }

    }


}
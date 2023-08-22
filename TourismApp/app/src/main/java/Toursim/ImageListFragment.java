package Toursim;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tourismapp.R;
import com.example.tourismapp.RetrofitConfig;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

import Hotels.HotelModel;
import Hotels.HotelsApiCall;
import Images.ImageApiCall;
import Images.SliderAdapter;
import Images.SliderData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageListFragment extends Fragment {
    private SliderView sliderView;
    private TextView tourismName, noImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);

        sliderView = (SliderView) view.findViewById(R.id.slider);
        tourismName = (TextView) view.findViewById(R.id.image_tourism_title);
        noImage = (TextView) view.findViewById(R.id.no_image);

        getImages();
        // Inflate the layout for this fragment
        return view;
    }

    private void getImages() {
        int id = getActivity().getIntent().getIntExtra("id", 0);

        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        ImageApiCall imageApiCall = retrofit.create(ImageApiCall.class);

        Call<List<SliderData>> call = imageApiCall.getAllImages(id);
        call.enqueue(new Callback<List<SliderData>>() {
            @Override
            public void onResponse(Call<List<SliderData>> call, Response<List<SliderData>> response) {
                setSlider(response.body());
            }

            @Override
            public void onFailure(Call<List<SliderData>> call, Throwable t) {

            }
        });
    }

    private void setSlider(List<SliderData> body) {
        if(body.size() == 0){
            tourismName.setVisibility(View.INVISIBLE);
            sliderView.setVisibility(View.INVISIBLE);
            noImage.setVisibility(View.VISIBLE);
        } else {
            String nameStr = getActivity().getIntent().getStringExtra("name");
            tourismName.setText("Images from " + nameStr);
            SliderAdapter adapter = new SliderAdapter(getActivity().getApplicationContext());
            adapter.setSliderList(body);
            sliderView.setAutoCycleDirection(SliderView.LAYOUT_DIRECTION_LTR);
            sliderView.setSliderAdapter(adapter);
            sliderView.setScrollTimeInSec(3);
            sliderView.setAutoCycle(true);
            sliderView.startAutoCycle();
        }
    }
}
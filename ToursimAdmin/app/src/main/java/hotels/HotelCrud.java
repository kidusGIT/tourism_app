package hotels;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.toursimadmin.RetrofitConfig;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Part;

public class HotelCrud {
    private Context context;

    public HotelCrud(Context context) {
        this.context = context;
    }

    private MultipartBody.Part prepareFilePart(String imgUrl){
        File file = new File(imgUrl);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imageToUpload = MultipartBody.Part.createFormData("img", file.getName(), requestBody);

        return imageToUpload;
    }

    // create Hotel
    public void createHotel(HotelModel hotelModel, String imgUri){
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        HotelApiCall hotelApiCall = retrofit.create(HotelApiCall.class);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getName());
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getDesc());
        RequestBody tourismId = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getToursimId());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getLatitude());
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getLongtude());

        Call<HotelModel> call;
        if(imgUri == null){
            call = hotelApiCall.createHotelWithoutImage(name, desc, tourismId, latitude, longitude);
        } else {
            call = hotelApiCall.createHotel(name, desc, tourismId, latitude, longitude, prepareFilePart(imgUri));
        }

        call.enqueue(new Callback<HotelModel>() {
            @Override
            public void onResponse(Call<HotelModel> call, Response<HotelModel> response) {

            }

            @Override
            public void onFailure(Call<HotelModel> call, Throwable t) {

            }
        });
    }

    // update Hotel
    public void updateHotel(HotelModel hotelModel, String imgUri,int id){
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        HotelApiCall hotelApiCall = retrofit.create(HotelApiCall.class);

        if(imgUri == null){
            updateWithoutImage(hotelModel, id);
//            Toast.makeText(context, "with out image " + hotelModel.getImg(), Toast.LENGTH_SHORT).show();
            return;
        } else {
            RequestBody name = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getName());
            RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getDesc());
            RequestBody tourismId = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getToursimId());
            RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getLatitude());
            RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getLongtude());
            Call<HotelModel> call = hotelApiCall.updateHotel(name, desc, tourismId, latitude, longitude, prepareFilePart(imgUri), id);
            call.enqueue(new Callback<HotelModel>() {
                @Override
                public void onResponse(Call<HotelModel> call, Response<HotelModel> response) {

                }

                @Override
                public void onFailure(Call<HotelModel> call, Throwable t) {

                }
            });

            Toast.makeText(context, "response: with image", Toast.LENGTH_SHORT).show();
        }


    }

    private void updateWithoutImage(HotelModel hotelModel, int id){
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        HotelApiCall hotelApiCall = retrofit.create(HotelApiCall.class);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getName());
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getDesc());
        RequestBody tourismId = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getToursimId());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getLatitude());
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getLongtude());
        RequestBody img = RequestBody.create(MediaType.parse("text/plain"), hotelModel.getImg());

        Call<HotelModel> call = hotelApiCall.updateHotelWithoutImage(name, desc, tourismId, latitude, longitude, img, id);
        call.enqueue(new Callback<HotelModel>() {
            @Override
            public void onResponse(Call<HotelModel> call, Response<HotelModel> response) {

            }

            @Override
            public void onFailure(Call<HotelModel> call, Throwable t) {

            }
        });
    }

    // delete Hotel
    public void deleteHotel(int id){
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        HotelApiCall hotelApiCall = retrofit.create(HotelApiCall.class);

        Call<HotelModel> call = hotelApiCall.deleteHotel(id);
        call.enqueue(new Callback<HotelModel>() {
            @Override
            public void onResponse(Call<HotelModel> call, Response<HotelModel> response) {

            }

            @Override
            public void onFailure(Call<HotelModel> call, Throwable t) {

            }
        });
    }

}

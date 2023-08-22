package Tourism;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.example.toursimadmin.MainActivity;
import com.example.toursimadmin.RetrofitConfig;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TourismCrud {
    private Context context;

    public TourismCrud(Context context) {
        this.context = context;
    }

    // Create Tourism
    private MultipartBody.Part prepareFilePart(String imgUrl){
        File file = new File(imgUrl);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imageToUpload = MultipartBody.Part.createFormData("cover_image", file.getName(), requestBody);

        return imageToUpload;
    }

    public void createTourism(TourismModel model, String imgUrl){
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        TourismApiCall tourismApiCall = retrofit.create(TourismApiCall.class);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), model.getName());
        RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), model.getDesc());

        Call<TourismModel> call;
        if(imgUrl == null){
            call = tourismApiCall.createTourismWithoutImage(name, desc);

        } else {
            call = tourismApiCall.createTourism(name, desc, prepareFilePart(imgUrl));
        }

        call.enqueue(new Callback<TourismModel>() {
            @Override
            public void onResponse(Call<TourismModel> call, Response<TourismModel> response) {

            }

            @Override
            public void onFailure(Call<TourismModel> call, Throwable t) {

            }
        });

    }

    // Update Tourism
    public void updateTourism(TourismModel model, int id, String imageUi){
       if(id != 0){
           Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
           TourismApiCall tourismApiCall = retrofit.create(TourismApiCall.class);

           RequestBody name = RequestBody.create(MediaType.parse("text/plain"), model.getName());
           RequestBody desc = RequestBody.create(MediaType.parse("text/plain"), model.getDesc());

           Call<TourismModel> call;

           if(imageUi == null){
              RequestBody image = RequestBody.create(MediaType.parse("text/plain"), model.getCover_image());

              call = tourismApiCall.updateTourismWithoutImage("token", name, desc, image, id);
           } else {
              call = tourismApiCall.updateTourism(name, desc, prepareFilePart(imageUi), id);
           }

//           Call<TourismModel> call = tourismApiCall.updateTourism(name, desc, prepareFilePart(imageUi), id);
           call.enqueue(new Callback<TourismModel>() {
               @Override
               public void onResponse(Call<TourismModel> call, Response<TourismModel> response) {

               }

               @Override
               public void onFailure(Call<TourismModel> call, Throwable t) {

               }
           });

       }
    }

    // Delete Tourism
    public void deleteTourism(int id){
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        TourismApiCall tourismApiCall = retrofit.create(TourismApiCall.class);

        Call<TourismModel> call = tourismApiCall.deleteTourism(id);
        call.enqueue(new Callback<TourismModel>() {
            @Override
            public void onResponse(Call<TourismModel> call, Response<TourismModel> response) {

            }

            @Override
            public void onFailure(Call<TourismModel> call, Throwable t) {

            }
        });
    }

}

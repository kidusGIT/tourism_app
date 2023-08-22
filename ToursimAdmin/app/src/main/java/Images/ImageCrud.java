package Images;

import android.content.Context;

import com.example.toursimadmin.RetrofitConfig;

import java.io.File;

import hotels.HotelApiCall;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ImageCrud {
    private Context context;

    public ImageCrud(Context context) {
        this.context = context;
    }

    private MultipartBody.Part prepareFilePart(String imgUrl){
        File file = new File(imgUrl);
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imageToUpload = MultipartBody.Part.createFormData("img_url", file.getName(), requestBody);

        return imageToUpload;
    }

    // Create
    public void createImage(ImageModel model, String imgUri){
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        ImagesApiCall imagesApiCall = retrofit.create(ImagesApiCall.class);

        RequestBody tourismId = RequestBody.create(MediaType.parse("text/plain"), model.getTourismId());

        Call<ImageModel> call = imagesApiCall.createImage(prepareFilePart(imgUri), tourismId);
        call.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {

            }

            @Override
            public void onFailure(Call<ImageModel> call, Throwable t) {

            }
        });

    }

    // delete
    public void deleteImage(int id){
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        ImagesApiCall imagesApiCall = retrofit.create(ImagesApiCall.class);

        Call<ImageModel> call = imagesApiCall.deleteImage(id);
        call.enqueue(new Callback<ImageModel>() {
            @Override
            public void onResponse(Call<ImageModel> call, Response<ImageModel> response) {

            }

            @Override
            public void onFailure(Call<ImageModel> call, Throwable t) {

            }
        });
    }
}

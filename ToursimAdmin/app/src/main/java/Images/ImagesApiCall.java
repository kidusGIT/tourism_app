package Images;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ImagesApiCall {

    // get certain Image
    @GET("image/certain/{tourismId}")
    Call<List<ImageModel>> getAllImages(@Path("tourismId") int tourismId);

    // delete Image
    @DELETE("image/{id}")
    Call<ImageModel> deleteImage(@Path("id") int id);

    // create image
    @Multipart
    @POST("image")
    Call<ImageModel> createImage(
        @Part MultipartBody.Part image,
        @Part("ToursimId") RequestBody tourismId
    );

}

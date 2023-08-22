package Tourism;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface TourismApiCall {

    // Get all Tourism
    @GET("toursim")
    Call<List<TourismModel>> getTourisms(@Header("token") String token);

    // GET Detail Tourism
    @GET("toursim/{id}")
    Call<TourismModel> getTourism(@Path("id") int id);

    // Create Tourism

    @Multipart
    @POST("toursim/create-toursim")
    Call<TourismModel> createTourism(
        @Part("name") RequestBody name,
        @Part("desc") RequestBody desc,
        @Part MultipartBody.Part image
    );

    @Multipart
    @POST("toursim/create-toursim")
    Call<TourismModel> createTourismWithoutImage(
        @Part("name") RequestBody name,
        @Part("desc") RequestBody desc
    );

    // Update Tourism
    @Multipart
    @PUT("toursim/update-toursim/{id}")
    Call<TourismModel> updateTourism(
        @Part("name") RequestBody name,
        @Part("desc") RequestBody desc,
        @Part MultipartBody.Part image,
        @Path("id") int id
    );

    @Multipart
    @PUT("toursim/update-toursim/{id}")
    Call<TourismModel> updateTourismWithoutImage(
        @Header("token") String toke,
        @Part("name") RequestBody name,
        @Part("desc") RequestBody desc,
        @Part("cover_image") RequestBody image,
        @Path("id") int id
    );

    // Delete Tourism
    @DELETE("toursim/delete-toursim/{id}")
    Call<TourismModel> deleteTourism(@Path("id") int id);

}

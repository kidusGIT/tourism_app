package hotels;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface HotelApiCall {
    // get all hotels
    @GET("")
    Call<List<HotelModel>> getHotels();

    // get detail hotel
    @GET("hotels/{id}")
    Call<HotelModel> getDetailHotel(@Path("id") int id);

    // get certain hotels
    @GET("hotels/certain/{tourism_id}")
    Call<List<HotelModel>> getCertainHotels(@Path("tourism_id") int tourismId);

    // create hotel
    // "name":"Test Three Hotel",
    // "desc":"Hotels is Test",
    // "ToursimId":3

    // "img": "",
    // "latitude": 0,
    // "longtude": 0,

//    @Part("name") RequestBody name,
//    @Part("desc") RequestBody desc,
//    @Part MultipartBody.Part image,
//    @Path("id") int id)
    @Multipart
    @POST("hotels")
    Call<HotelModel> createHotel(
        @Part("name") RequestBody name,
        @Part("desc") RequestBody desc,
        @Part("ToursimId") RequestBody tourismId,
        @Part("latitude") RequestBody latitude,
        @Part("longtude") RequestBody longitude,
        @Part MultipartBody.Part image
    );

    @Multipart
    @POST("hotels")
    Call<HotelModel> createHotelWithoutImage(
        @Part("name") RequestBody name,
        @Part("desc") RequestBody desc,
        @Part("ToursimId") RequestBody tourismId,
        @Part("latitude") RequestBody latitude,
        @Part("longtude") RequestBody longitude
    );

    // update hotels
    @Multipart
    @PUT("hotels/update-hotel/{id}")
    Call<HotelModel> updateHotel(
         @Part("name") RequestBody name,
         @Part("desc") RequestBody desc,
         @Part("ToursimId") RequestBody tourismId,
         @Part("latitude") RequestBody latitude,
         @Part("longtude") RequestBody longitude,
         @Part MultipartBody.Part image,
         @Path("id") int id
    );

    //hotels/update-hotel/6
    @Multipart
    @PUT("hotels/update-hotel/{id}")
    Call<HotelModel> updateHotelWithoutImage(
        @Part("name") RequestBody name,
        @Part("desc") RequestBody desc,
        @Part("ToursimId") RequestBody tourismId,
        @Part("latitude") RequestBody latitude,
        @Part("longtude") RequestBody longitude,
        @Part("img") RequestBody image,
        @Path("id") int id
    );

    // delete hotels
    @DELETE("hotels/delete-hotel/{id}")
    Call<HotelModel> deleteHotel(@Path("id") int id);

}

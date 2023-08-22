package Hotels;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface HotelsApiCall {

    @GET("hotels/certain/{tourism_id}")
    Call<List<HotelModel>> getCertainHotels(@Path("tourism_id") int tourismId);

    // get detail hotel
    @GET("hotels/{id}")
    Call<HotelModel> getDetailHotel(@Path("id") int id);
}

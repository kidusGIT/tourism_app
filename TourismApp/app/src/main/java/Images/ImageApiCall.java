package Images;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ImageApiCall {

    @GET("image/certain/{tourismId}")
    Call<List<SliderData>> getAllImages(@Path("tourismId") int tourismId);
}

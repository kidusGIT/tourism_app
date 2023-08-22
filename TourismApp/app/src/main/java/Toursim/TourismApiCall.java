package Toursim;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TourismApiCall {

    // Get All Tourism
    @GET("toursim/users")
    Call<List<TourismModel>> getToursims();

    // Get Detail Tourism
    @GET("toursim/{id}")
    Call<TourismModel> getTourism(@Path("id") int id);

    // Search Tourism
    @GET("toursim/search/tourism")
    Call<List<TourismModel>> searchToursims(@Query("q") String query);
}

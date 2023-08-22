package User;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface UserApiCall {

   // create a user
   @POST("auth/sign-up")
   Call<String> createUser(@Body UserModel model);

   // logging the user
   @POST("auth/sign-in")
   Call<String> loginUser(@Body UserModel model);
}

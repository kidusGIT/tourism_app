package User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.toursimadmin.MainActivity;
import com.example.toursimadmin.R;
import com.example.toursimadmin.RetrofitConfig;

import Tourism.TourismApiCall;
import Tourism.TourismModel;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity {
    private EditText username;
    private EditText password;
    private TextView signIn;
    private Button login;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = findViewById(R.id.username_login_edit);
        password = findViewById(R.id.password_login_edit);
        login = findViewById(R.id.sign_in);
        signIn = findViewById(R.id.go_to_signin);

        preferences = getSharedPreferences("SharedPrefLanguage", Context.MODE_PRIVATE);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserModel model = new UserModel();
                model.setUsername(username.getText().toString());
                model.setPassword(password.getText().toString());

                loginUser(model);
            }
        });

    }

    private void loginUser(UserModel model) {
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        UserApiCall userApiCall = retrofit.create(UserApiCall.class);

        Call<String> call = userApiCall.loginUser(model);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.isSuccessful()){
                    if(response.body() != null){
                        Log.d("TAG", "onResponse: " + response.body());

                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("token", response.body());
                        edit.apply();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                } else {
                    if(response.code() == 400){
                        Log.d("TAG", "onResponse else: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}
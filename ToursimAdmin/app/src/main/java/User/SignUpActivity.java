package User;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.toursimadmin.MainActivity;
import com.example.toursimadmin.R;
import com.example.toursimadmin.RetrofitConfig;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity {
    private EditText fullName;
    private EditText username;
    private EditText password;
    private Button signUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        fullName = findViewById(R.id.full_name_edit);
        username = findViewById(R.id.username_login_edit);
        password = findViewById(R.id.password_login_edit);
        signUp = findViewById(R.id.sign_in);

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
                UserApiCall userApiCall = retrofit.create(UserApiCall.class);
                UserModel user = new UserModel();
                user.setUsername(username.getText().toString());
                user.setFull_name(fullName.getText().toString());
                user.setPassword(password.getText().toString());


                Call<String> call = userApiCall.createUser(user);
                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if(response.isSuccessful()){
                            if(response.body() != null){
                                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
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
        });

    }
}
package com.example.toursimadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import Tourism.TourismAdapter;
import Tourism.TourismAddActivty;
import Tourism.TourismApiCall;
import Tourism.TourismModel;
import Tourism.TourismRepository;
import Tourism.TourismViewModel;
import User.LoginActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FloatingActionButton fab;

    // room
    private TourismViewModel tourismViewModel;
    private TourismRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        repository = new TourismRepository(getApplication());
        tourismViewModel = new ViewModelProvider(this).get(TourismViewModel.class);


        recyclerView = findViewById(R.id.toursim_recycle);
        fab = findViewById(R.id.add_toursim);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TourismAddActivty.class);
                startActivity(intent);
            }
        });

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("SharedPrefLanguage", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        if(token.equals("")){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);

        } else {

            getAllTousrsim(this);
        }


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getAllTousrsim(this);
    }

    public void getAllTousrsim(Context context){
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        TourismApiCall tourismApiCall = retrofit.create(TourismApiCall.class);

        SharedPreferences preferences = context.getApplicationContext().getSharedPreferences("SharedPrefLanguage", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        Call<List<TourismModel>> call = tourismApiCall.getTourisms(token);

        call.enqueue(new Callback<List<TourismModel>>() {
            @Override
            public void onResponse(Call<List<TourismModel>> call, Response<List<TourismModel>> response) {
                if(response.code() != 200){
                    Toast.makeText(context, "No Internet Connection", Toast.LENGTH_SHORT).show();
                    return;
                }
                repository.delete();
                repository.insert(response.body());
                initRecyclerView(response.body(), context);
            }

            @Override
            public void onFailure(Call<List<TourismModel>> call, Throwable t) {
//                Toast.makeText(context, "error" + t.getMessage(), Toast.LENGTH_SHORT).show();
                initRecyclerViewOffline();
                Log.d("ERROR TOursim", "onFailure: " + t.getMessage());
            }
        });
    }

    private void initRecyclerView(List<TourismModel>  tourism, Context context) {
          TourismAdapter adapter = new TourismAdapter(context);
          adapter.notifyDataSetChanged();
          adapter.setTourismModelList(tourism);

          recyclerView.setAdapter(adapter);
          Log.d("message ", "initRecyclerView: ");
          recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void initRecyclerViewOffline(){
        tourismViewModel.getAllTourism().observe(this, new Observer<List<TourismModel>>() {
            @Override
            public void onChanged(List<TourismModel> tourismModels) {
                TourismAdapter adapter = new TourismAdapter(MainActivity.this);
                adapter.notifyDataSetChanged();
                adapter.setTourismModelList(tourismModels);

                recyclerView.setAdapter(adapter);
                Log.d("message ", "initRecyclerView: ");
                recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("SharedPrefLanguage", Context.MODE_PRIVATE);
        String token = preferences.getString("token", "");
        if(token.equals("")){
            getMenuInflater().inflate(R.menu.login_option_menu, menu);

        } else {
            getMenuInflater().inflate(R.menu.logout_option_menu, menu);

        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.login_item){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else if(id == R.id.logout_item){
            SharedPreferences preferences = getApplicationContext().getSharedPreferences("SharedPrefLanguage", Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("token", "");
            edit.apply();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
}
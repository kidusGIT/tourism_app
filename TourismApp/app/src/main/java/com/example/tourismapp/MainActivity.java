package com.example.tourismapp;

import static com.google.android.material.internal.ContextUtils.getActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.IOException;
import java.util.List;

import Toursim.TourismAdapter;
import Toursim.TourismApiCall;
import Toursim.TourismModel;
import Toursim.TourismRepository;
import Toursim.TourismViewModel;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private EditText searchText;
    private ImageButton clearText;
    private ShimmerFrameLayout shimmerFrameLayout;

    // room
    private TourismViewModel tourismViewModel;
    private TourismRepository repository;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        shimmerFrameLayout = findViewById(R.id.shimmer);
        shimmerFrameLayout.startShimmer();

        repository = new TourismRepository(getApplication());
        tourismViewModel = new ViewModelProvider(this).get(TourismViewModel.class);

        clearText = findViewById(R.id.clear_search_text);
        clearText.setVisibility(View.INVISIBLE);

        searchText = findViewById(R.id.search_edit_text);
        searchText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchText(charSequence.toString());
                clearText.setVisibility(View.VISIBLE);
                shimmerFrameLayout.stopShimmer();
                if(charSequence.toString().equals("")){
                    clearText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchText.setText("");
                clearText.setVisibility(View.INVISIBLE);
            }
        });

        getAllTourism();

    }

    private void searchText(String query){
        if(query.equals("") || query == null){
           getAllTourism();
        } else {
            Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
            TourismApiCall tourismApiCall = retrofit.create(TourismApiCall.class);

            Call<List<TourismModel>> call = tourismApiCall.searchToursims(query);
            call.enqueue(new Callback<List<TourismModel>>() {
                @Override
                public void onResponse(Call<List<TourismModel>> call, Response<List<TourismModel>> response) {
                    if(response.isSuccessful()){
                        initRecycle(response.body());
                        shimmerFrameLayout.stopShimmer();
                        shimmerFrameLayout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFailure(Call<List<TourismModel>> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "onFailure: " + t.getMessage());
                }
            });
        }
    }

    private boolean isNetworkAvailable(){
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();
        if(activeNetworkInfo.getTypeName().equals("WIFI")){
            if(activeNetworkInfo.isConnected()){
                haveConnectedWifi = true;
            }
        }

        if(activeNetworkInfo.getTypeName().equals("MOBILE")){
            if(activeNetworkInfo.isConnected()){
                haveConnectedMobile = true;
            }
        }

        return haveConnectedWifi || haveConnectedMobile;
    }

    private void getAllTourism(){
        Retrofit retrofit = RetrofitConfig.getRetrofitBuilder();
        TourismApiCall tourismApiCall = retrofit.create(TourismApiCall.class);

        Call<List<TourismModel>> call = tourismApiCall.getToursims();
        call.enqueue(new Callback<List<TourismModel>>() {
            @Override
            public void onResponse(Call<List<TourismModel>> call, Response<List<TourismModel>> response) {
                if(response.isSuccessful()){
                    initRecycle(response.body());
                    repository.delete();
                    repository.insert(response.body());
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onFailure(Call<List<TourismModel>> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                initRecycleOffline();
                Log.d("TAG", "onFailure: " + t.getMessage());
            }
        });
    }
    
    // init the recycler view 
    private void initRecycle(List<TourismModel> model){

        TourismAdapter adapter = new TourismAdapter(MainActivity.this);
        adapter.setTourismList(model);

//        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        recyclerView = findViewById(R.id.recycler_tourism_list);
        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(layoutManager);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager( new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        } else {
            recyclerView.setLayoutManager( new StaggeredGridLayoutManager(4, LinearLayoutManager.VERTICAL));
        }
    }

    private void initRecycleOffline(){
        tourismViewModel.getAllTourism().observe(this, new Observer<List<TourismModel>>() {
            @Override
            public void onChanged(List<TourismModel> tourismModels) {
                if(tourismModels.size() > 0){
                    recyclerView = findViewById(R.id.recycler_tourism_list);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);

                    TourismAdapter adapter = new TourismAdapter(MainActivity.this);
                    adapter.setTourismList(tourismModels);

                    StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
                    recyclerView = findViewById(R.id.recycler_tourism_list);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(layoutManager);

                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getAllTourism();
    }

}
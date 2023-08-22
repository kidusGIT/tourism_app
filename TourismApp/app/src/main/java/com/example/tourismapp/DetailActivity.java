package com.example.tourismapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import Hotels.HotelListFragment;
import Toursim.ImageListFragment;
import Toursim.TourismDescFragment;

public class DetailActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        setStatusBarTransparent();

//        replaceFragment(new HotelListFragment());
        bottomNavigation = findViewById(R.id.bottum_navigations);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if(id == R.id.tourism_menu){
                    replaceFragment(new TourismDescFragment());
                    item.setChecked(true);
                    return true;
                } else if(id == R.id.hotel_menu){
                    replaceFragment(new HotelListFragment());
                    item.setChecked(true);
                    return true;
                } else if(id == R.id.image_menu){
                    replaceFragment(new ImageListFragment());
                    item.setChecked(true);
                    return true;
                }
                return false;
            }
        });
        Log.d("TAG", "onCreate: " + TourismDescFragment.class);
        replaceFragment(new TourismDescFragment());

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        fragmentTransaction.commit();

    }

    private void setStatusBarTransparent(){
            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
                Window window = getWindow();
                window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
    }
}























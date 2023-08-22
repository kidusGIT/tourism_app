package com.example.toursimadmin;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.content.ContentResolver;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitConfig {

    private static Retrofit retrofit = null;

    public static Retrofit getRetrofitBuilder(){
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.1.6:8800/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

}

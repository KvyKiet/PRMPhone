package com.example.fellphonef.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "http://apilayer.net/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}


//http://apilayer.net/
//Gson gson = new GsonBuilder()
//        .setDateFormat("yyyy-MM-dd HH:mm:ss")
//        .create();
//
//// Phương thức để tạo instance của ApiService
//ApiService apiService = new Retrofit.Builder()
//        .baseUrl("http://apilayer.net/")
//        .addConverterFactory(GsonConverterFactory.create(gson))
//        .build()
//        .create(ApiService.class);
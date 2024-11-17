package com.example.gullakguru.networks;

import android.content.Context;

import com.chuckerteam.chucker.api.ChuckerInterceptor;

import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {


    public static retrofit2.Retrofit getRetrofit(Context context) {

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ChuckerInterceptor(context))
                .build();

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl("https://sheets.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrofit;
    }
}

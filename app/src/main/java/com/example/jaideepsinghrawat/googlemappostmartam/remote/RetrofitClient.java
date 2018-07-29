package com.example.jaideepsinghrawat.googlemappostmartam.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static Retrofit retrofitClient=null;
    public  static  Retrofit getRetrofitClient( String baseUrl){
        if(retrofitClient==null){
            retrofitClient=new Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitClient;
    }

}

package com.example.jaideepsinghrawat.googlemappostmartam;

import com.example.jaideepsinghrawat.googlemappostmartam.remote.IGoogleApiServices;
import com.example.jaideepsinghrawat.googlemappostmartam.remote.RetrofitClient;

public class Common  {
    public static final String url="https://maps.googleapis.com/";
    public static IGoogleApiServices getGoogleApiService(){
            return RetrofitClient.getRetrofitClient(url).create(IGoogleApiServices.class);
    }

}


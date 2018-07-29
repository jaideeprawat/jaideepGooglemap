package com.example.jaideepsinghrawat.googlemappostmartam.remote;

import com.example.jaideepsinghrawat.googlemappostmartam.model.MyPojo;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface IGoogleApiServices {
    @GET
    Call<MyPojo> getDirection(@Url String url);
}

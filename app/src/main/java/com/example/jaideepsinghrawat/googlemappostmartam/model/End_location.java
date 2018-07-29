package com.example.jaideepsinghrawat.googlemappostmartam.model;

import com.google.android.gms.maps.model.LatLng;

public class End_location {
    private String lng;

    private String lat;

    public String getLng ()
    {
        return lng;
    }

    public void setLng (String lng)
    {
        this.lng = lng;
    }

    public String getLat ()
    {
        return lat;
    }

    public void setLat (String lat)
    {
        this.lat = lat;
    }
    public LatLng getCoordination() {
        return new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
    }
    @Override
    public String toString()
    {
        return "ClassPojo [lng = "+lng+", lat = "+lat+"]";
    }
}

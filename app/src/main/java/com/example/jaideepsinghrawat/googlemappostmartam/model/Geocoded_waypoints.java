package com.example.jaideepsinghrawat.googlemappostmartam.model;

class Geocoded_waypoints {
    private String place_id;

    private String geocoder_status;

    private String[] types;

    public String getPlace_id ()
    {
        return place_id;
    }

    public void setPlace_id (String place_id)
    {
        this.place_id = place_id;
    }

    public String getGeocoder_status ()
    {
        return geocoder_status;
    }

    public void setGeocoder_status (String geocoder_status)
    {
        this.geocoder_status = geocoder_status;
    }

    public String[] getTypes ()
    {
        return types;
    }

    public void setTypes (String[] types)
    {
        this.types = types;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [place_id = "+place_id+", geocoder_status = "+geocoder_status+", types = "+types+"]";
    }
}

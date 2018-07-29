package com.example.jaideepsinghrawat.googlemappostmartam;

import android.net.Uri;

class PlaceInfo {
    String address, name, id, phone_no;
    String latlng, rating;
    Uri websiteUri;
    public  PlaceInfo(){

    }
public  PlaceInfo(String address,String name,String id,String phone_no,String latlng,String rating,Uri websiteUri){
    this.address=address;
    this.name=name;
    this.id=id;
    this.phone_no=phone_no;
    this.latlng=latlng;
    this.rating=rating;
    this.websiteUri=websiteUri;
}

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public Uri getWebsiteUri() {
        return websiteUri;
    }

    public void setWebsiteUri(Uri websiteUri) {
        this.websiteUri = websiteUri;
    }
}

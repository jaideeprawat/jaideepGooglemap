package com.example.jaideepsinghrawat.googlemappostmartam;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jaideepsinghrawat.googlemappostmartam.model.Legs;
import com.example.jaideepsinghrawat.googlemappostmartam.model.MyPojo;
import com.example.jaideepsinghrawat.googlemappostmartam.model.Routes;
import com.example.jaideepsinghrawat.googlemappostmartam.model.Steps;
import com.example.jaideepsinghrawat.googlemappostmartam.remote.IGoogleApiServices;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomMapActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener {
//    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private ImageView mapimage,mylocation,infoimage;
    private static final String TAG=CustomMapActivity.class.getName();
    private static final String FINE_LOCATION_PERMISSION=Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String ACCESS_COARSE_LOCATION=Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int PERMISSION_REQUEST_CODE=1114;
    private  boolean permissionGranted=false;
    private static final int ERROR_DIALOG=1001;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private GeoDataClient mGeoDataClient;
    private GoogleApiClient mGoogleapiclient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Handler handler;
    private SupportMapFragment mapFragment;
    private PlaceInfo info;
    String originAdress;
    private static final int PLACE_PICKER_REQUEST = 12;

    /*initializing widgets on screen*/
    AutoCompleteTextView searchView;
    private PlaceAutomCompleteAdapter placeAutomCompleteAdapter;
    private  static  final LatLngBounds latLngBounds= new LatLngBounds(new LatLng(-30,-170),new LatLng(71,136));
    private Marker marker;
    IGoogleApiServices apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_map);
        searchView=findViewById(R.id.search);
        infoimage=findViewById(R.id.information);
        mylocation=findViewById(R.id.mylocation);
        mapimage=findViewById(R.id.mapsss);
        apiService = Common.getGoogleApiService();
        mylocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDeviceLocation();
            }
        });
        infoimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                for good window check google window
                if(marker!=null){
if(marker.isInfoWindowShown()){
    marker.hideInfoWindow();
}else {
    marker.showInfoWindow();
}
}
            }
        });
        mapimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                try {
                    startActivityForResult(builder.build(CustomMapActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }

            }
        });
        searchView.setOnItemClickListener(mAutoclick);
        mGoogleapiclient = new GoogleApiClient
                .Builder(this)
                .addOnConnectionFailedListener(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        if(isServiceOk()){
            if (Build.VERSION.SDK_INT >= 23) {
                // Call some material design APIs here
                getLocationPermission();

            } else {
                // Implement this feature without material design
                initializeMap();
            }
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
    }

    private void setCameraWithCoordinationBounds(Routes route) {
        LatLng southwest = route.getBounds().getSouthwest().getCoordination();
        LatLng northeast = route.getBounds().getNortheast().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    /*********initialize elements by searchview********/
//    private void initializeView(){
//        searchView.setActivated(true);
//        searchView.setQueryHint("Type your keyword here");
//        searchView.onActionViewExpanded();
//        searchView.setIconified(false);
//        searchView.clearFocus();
//
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                geoLocate();
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//
//                return false;
//            }
//        });

//    }
        private void initializeView(){

            placeAutomCompleteAdapter=new PlaceAutomCompleteAdapter(CustomMapActivity.this,mGeoDataClient,latLngBounds,null);
            searchView.setAdapter(placeAutomCompleteAdapter);
            searchView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if(i==EditorInfo.IME_ACTION_DONE
                            ||i==EditorInfo.IME_ACTION_SEARCH
                            ||keyEvent.getAction()==keyEvent.ACTION_DOWN
                            ||keyEvent.getAction()==keyEvent.KEYCODE_ENTER){
                        geoLocate();
                    }
                    return false;
                }
            });
        }



    private void geoLocate() {
        String searchString=searchView.getText().toString();
        Geocoder geocoder=new Geocoder(CustomMapActivity.this);
        List<Address> addresses=new ArrayList<>();
        try {
            addresses=geocoder.getFromLocationName(searchString,3);
        } catch (IOException e) {
            Log.e(TAG,"geocoder: IoException "+e.getMessage());
        }
        if(addresses.size()>0){
            Address address=addresses.get(0);
            Toast.makeText(CustomMapActivity.this,""+address,Toast.LENGTH_LONG).show();
            LatLng latLng=new LatLng(address.getLatitude(),address.getLongitude());
            cameraMove(latLng,DEFAULT_ZOOM,address.getAddressLine(0));

        }

    }

    private void cameraMove(LatLng latLng, int defaultZoom, String addressLine) {
        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,defaultZoom));
        MarkerOptions markerOptions=new MarkerOptions();
        markerOptions.position(latLng)
                .title(addressLine);
        mMap.addMarker(markerOptions);


//        Call<MoviesResponse> call = apiService.getTopRatedMovies(API_KEY);
//        call.enqueue(new Callback<MoviesResponse>() {
//            @Override
//            public void onResponse(Call<MoviesResponse>call, Response<MoviesResponse> response) {
//                List<Movie> movies = response.body().getResults();
//                Log.d(TAG, "Number of movies received: " + movies.size());
//            }
//
//            @Override
//            public void onFailure(Call<MoviesResponse>call, Throwable t) {
//                // Log error here since request failed
//                Log.e(TAG, t.toString());
//            }
//        });

    }



    //    for info button
    private void cameraMove(LatLng latLng, int defaultZoom, PlaceInfo info) {
        mMap.clear();
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,defaultZoom));
        MarkerOptions markerOptions=new MarkerOptions();
        if(info!=null) {
            String snippet="address: "+info.getAddress()+"\n"+
                    "phone no.: "+info.getAddress()+"\n"+
                    "website: "+info.getAddress()+"\n"+
                    "Price Rating: "+info.getAddress()+"\n";

            markerOptions.position(latLng)
                    .title(info.getName())
                    .snippet(snippet);
            marker=mMap.addMarker(markerOptions);
        }else{
            markerOptions.position(latLng);
            mMap.addMarker(markerOptions);

        }



    }

    private String getUrl(String origin,String destination,String key) {
            StringBuilder url=new StringBuilder("https://maps.googleapis.com/maps/api/directions/json");
            url.append("?origin="+origin);
                    url.append("&destination="+destination);
                            url.append("&key="+key);
                            return url.toString();


    }

    //    get the device location
    private void initializeMap(){
        mGeoDataClient = Places.getGeoDataClient(this);
//        handler=new Handler(new Handler.Callback() {
//            @Override
//            public boolean handleMessage(Message msg) {
//                if(mLastKnownLocation!=null){
//                    for(int i = 0 ; i < otherUsers.size() ; i++) {
//                        createMarker(otherUsers.get(i));
//                    }
//                }
//                return false;
//
//            }});
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

     mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.mapcustom);
    mapFragment.getMapAsync(this);
}

//    this method is used for permission in app
    private  void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                permissionGranted = true;
                initializeMap();

            } else {
                ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
            }
        }else {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

//this method is used for update the ui.
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (permissionGranted) {
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mMap.setMyLocationEnabled(true);
                mylocation.setVisibility(View.VISIBLE);
//                init();


            } else {
                mMap.setMyLocationEnabled(false);
                mylocation.setVisibility(View.GONE);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionGranted=false;
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:{
                if (grantResults.length > 0) {

                    for(int i=0;i<grantResults.length;i++){
                        if(grantResults[i]!=PackageManager.PERMISSION_GRANTED){
                            permissionGranted=false;
                            return;
                        }
                        permissionGranted=true;
                        //iniatialize our map
                        initializeMap();
                    }
                }
            }
        }
    }
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (permissionGranted) {
                Task locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = (Location) task.getResult();
                            if(mLastKnownLocation!=null){
                                LatLng exactllocation= new LatLng(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude());
                                originAdress=getAddressFromLocation(mLastKnownLocation.getLatitude(),
                                        mLastKnownLocation.getLongitude(),CustomMapActivity.this);
//                                mMap.addMarker(new MarkerOptions().position(exactllocation)
//                                        .title("ME"));
                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                                        exactllocation,13));
//                                saveMylocation(mLastKnownLocation);
//                                getotherLocation();
                                mylocation.setVisibility(View.VISIBLE);


                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                        mylocation.setVisibility(View.GONE);
                        }
                    }
                });
            }
        } catch(SecurityException e)  {
            Log.e("Exception: %s", e.getMessage());
        }
    }
    private boolean isServiceOk(){
        Log.d(TAG,"Checking to googgle service availbality");
        int available=GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(CustomMapActivity.this);
          if(available== ConnectionResult.SUCCESS){
            Log.d(TAG,"google service is woking") ;
                  return true;


              }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
                Log.d(TAG,"it can be resolved by programmer");
              Dialog dialog=GoogleApiAvailability.getInstance().getErrorDialog(CustomMapActivity.this,available,ERROR_DIALOG);
              dialog.show();
              }else{
            Log.d(TAG,"It can not be resolavable by programmer");

          }
          return false;
          }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap=googleMap;

        // Do other setup activities here too, as described elsewhere in this tutorial.

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();
        initializeView();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(CustomMapActivity.this,"connectionResult"+connectionResult.getErrorMessage(),Toast.LENGTH_LONG).show();
    }

    /************************Google place api autocomplete suggestion******************************************/
private AdapterView.OnItemClickListener mAutoclick=new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final AutocompletePrediction item= placeAutomCompleteAdapter.getItem(i);
            final String placeId=item.getPlaceId();
            PendingResult<PlaceBuffer> placeREsult=Places.GeoDataApi.getPlaceById(mGoogleapiclient,placeId);
        placeREsult.setResultCallback(mUpdatePlaceDetailCallback);
        }

    };

    private ResultCallback<PlaceBuffer> mUpdatePlaceDetailCallback=new ResultCallback<PlaceBuffer>() {
        @Override
        public void onResult(@NonNull PlaceBuffer places) {
if (!places.getStatus().isSuccess()){
    Log.d(TAG,"Onresult: place query not working"+places.getStatus().toString());
    places.release();
    return;
}
final Place place=places.get(0);
info=new PlaceInfo();
            info.setAddress(place.getAddress().toString());
            info.setName(place.getName().toString());
            info.setId(place.getId());
            info.setLatlng(place.getLatLng().toString());
            info.setPhone_no(place.getPhoneNumber().toString());
            info.setWebsiteUri(place.getWebsiteUri());
            info.setRating(String.valueOf(place.getRating()));



            String origin=originAdress;
            String destination=place.getAddress().toString();
            try {
                origin= URLEncoder.encode(origin,"utf-8");
                destination= URLEncoder.encode(destination,"utf-8");

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String key=getResources().getString(R.string.google_maps_key);
//        api result
            String url=getUrl(origin,destination,key);
            Call<MyPojo> call =apiService.getDirection(url);
            call.enqueue(new Callback<MyPojo>() {
                @Override
                public void onResponse(Call<MyPojo> call, Response<MyPojo> response) {
                    if(response.body().getStatus().equalsIgnoreCase("ok")){
                        Toast.makeText(CustomMapActivity.this,"successful",Toast.LENGTH_LONG).show();
                        Routes[] routes=response.body().getRoutes();
                        Routes route=routes[0];
                        Legs[]legs = route.getLegs();
                        Legs leg =legs[0];
                        mMap.addMarker(new MarkerOptions().position(leg.getStart_location().getCoordination()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                        mMap.addMarker(new MarkerOptions().position(leg.getEnd_location().getCoordination()));
                        ArrayList<LatLng> directionPositionList = leg.getDirectionPoint();
                        mMap.addPolyline(DirectionConverter.createPolyline(CustomMapActivity.this, directionPositionList, 5, Color.RED));
                        setCameraWithCoordinationBounds(route);


                    }else{
                        Toast.makeText(CustomMapActivity.this,"unsuccessful"+response.message(),Toast.LENGTH_LONG).show();

                    }
                    Log.e(TAG,"response"+response.toString());
                }

                @Override
                public void onFailure(Call<MyPojo> call, Throwable t) {
                    Log.e(TAG,"response"+t.getMessage().toString());

                }
            });



//            cameraMove(new LatLng(place.getViewport().getCenter().latitude,place.getViewport().getCenter().longitude),DEFAULT_ZOOM,info);
            places.release();



        }
    };
//    placepicker
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == PLACE_PICKER_REQUEST) {
        if (resultCode == RESULT_OK) {
            Place place = PlacePicker.getPlace(this, data);
            final String id=place.getId();
            PendingResult<PlaceBuffer> placeREsult=Places.GeoDataApi.getPlaceById(mGoogleapiclient,id);
            placeREsult.setResultCallback(mUpdatePlaceDetailCallback);
//            String toastMsg = String.format("Place: %s", place.getName());
//            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
        }



    }
}


    public static String getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        String result = null;
        try {
            List<Address> addressList = geocoder.getFromLocation(
                    latitude, longitude, 1);
            if (addressList != null && addressList.size() > 0) {
                Address address = addressList.get(0);
                result="";

            }
        } catch (IOException e) {
            Log.e(TAG, "Unable connect to Geocoder", e);
            result= latitude + "," + longitude;

        } finally {
            if(result.isEmpty()){
                result= latitude + "," + longitude;

            }
        }
        return result;
    }






}

package com.techno71.fireservice.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;
import com.techno71.fireservice.Model.MyLocation;
import com.techno71.fireservice.R;
import com.techno71.fireservice.databinding.ActivityUserMapBinding;

import java.util.ArrayList;
import java.util.List;

public class UserMapActivity extends FragmentActivity implements
        OnMapReadyCallback,
        LocationListener,android.location.LocationListener,DirectionCallback  {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private SharedPreferences sharedPreferences_type;
    private String access_token = "";
    private String phone = "";
    private ArrayList markerPoints= new ArrayList();
    protected LatLng start=null;
    protected LatLng end=null;

    private ActivityUserMapBinding binding;
    //polyline object
    private List<Polyline> polylines=null;
    private DatabaseReference databaseReference;
    private LatLng userLocationLat;
    private LatLng myLocationLat ;

    public UserMapActivity() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityUserMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        sharedPreferences_type = getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("FireService").child("Agent_Location_Details");
        access_token = sharedPreferences_type.getString("access_token", "default_access_token001");
        phone = sharedPreferences_type.getString("phone", "01700000000");
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);

         double latitude=Double.parseDouble(""+sharedPreferences_type.getFloat("latitude001",00.00f));
        double longitude=Double.parseDouble(""+sharedPreferences_type.getFloat("longitude001",00.00f));

        //userLocaton
        userLocationLat = new LatLng(latitude,longitude);

        checkLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_user);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        binding.kmTv.setVisibility(View.GONE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);

        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(@NonNull Location location) {

                MyLocation myLocation=new MyLocation(
                        ""+location.getLatitude(),
                        ""+location.getLongitude(),
                        ""+access_token,
                        ""+phone

                );
                databaseReference.child(access_token).setValue(myLocation);
                myLocationLat = new LatLng(location.getLatitude(), location.getLongitude());
                getLocationMap();
            }
        });

    }


    private String[] colors = {"#7fff7272", "#7f31c7c5", "#7fff8a00"};
    private Double distance;

    private boolean isActivityStart =true;
    @SuppressLint("DefaultLocale")
    private void getLocationMap(){

        if (myLocationLat==null){

        }else {

            if (userLocationLat==null){

                //mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocationLat));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(14));

            } else {
                distance = SphericalUtil.computeDistanceBetween(myLocationLat, userLocationLat);

                GoogleDirection.withServerKey("AIzaSyDjCh3TS5G8Su8FkHacIUQtjuea968H3ys")
                        .from(myLocationLat)
                        .to(userLocationLat)
                        .avoid(AvoidType.HIGHWAYS)
                        .transportMode(TransportMode.WALKING)
                        //.alternativeRoute(true)
                        .execute(this);

            }
        }


    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onDirectionSuccess(@Nullable Direction direction) {
        assert direction != null;

        if (direction.isOK()) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLocationLat);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.position(userLocationLat);
            markerOptions1.title("User Position");
            markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    myLocationLat, 16f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocationLat));
            if (isActivityStart){
                isActivityStart=false;
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

            }
            mMap.addMarker(markerOptions1);
            //mMap.addMarker(markerOptions);

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));

            binding.kmTv.setVisibility(View.VISIBLE);
            binding.kmTv.setText("Distance \n " + String.format("%.2f", distance / 1000) + "km");
        }
    }

    @Override
    public void onDirectionFailure(@NonNull Throwable t) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    private void getLocation() {
        try {
            LocationManager locationManager=null;
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0.1f,this );
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }


    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    public void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {

            // Asking user if explanation is needed
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(this,
                        new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_PERMISSIONS_REQUEST_LOCATION);


            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        } else {
            getLocation();

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted. Do the
                // contacts-related task you need to do.
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                    getLocation();
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    assert mapFragment != null;
                    mapFragment.getMapAsync(this);
                }

            } else {

                checkLocationPermission();
                // Permission denied, Disable the functionality that depends on this permission.
                //Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
            }
            return;
        }
    }



}
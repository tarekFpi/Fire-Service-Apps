package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.utils.Utils;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.squareup.picasso.Picasso;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.Divisoin_model;
import com.techno71.fireservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;


public class Storage_MapsActivity extends FragmentActivity implements
        OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap mMap;

    private List<MarkerOptions>markerList=new ArrayList<>();

    private GoogleApiClient client;
    private LocationRequest locationRequest;
    private LatLng latLng_start;

    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 10000;
    private List<Address> addressList;

    private TextView textView_addrss;

    double latitude,longitude;

    double latitude_stg,longitude_stg;

    private String showStorage_url = Main_Url.ROOT_URL +"api/show-all-location";

    private String storage_exesting = Main_Url.ROOT_URL +"api/add-storage-exesting-check-point";

    private  String city="", country,PostalCode,getAddress, marker_tag="";
    int count=0;

    private  ProgressDialog progressDialog;

    private String accessTocken;
    private SharedPreferences sharedPreferences_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_maps);

        textView_addrss=(TextView)findViewById(R.id.text_addressStorg);

        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        accessTocken = sharedPreferences_type.getString("access_token","access_token found");


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_storage);
        mapFragment.getMapAsync(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        // Showing / hiding your current location
        // Enable / Disable zooming controls
         mMap.getUiSettings().setZoomControlsEnabled(true);
         mMap.getUiSettings().setCompassEnabled(true);
        // Enable / Disable my location button
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);

         if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            bulidGoogleApiClient();
            mMap.setMyLocationEnabled(true);
        }


       mMap.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {

                progressDialog.dismiss();

                LatLng latLng_drag  = mMap.getCameraPosition().target;

                latitude=latLng_drag.latitude;
                longitude=latLng_drag.longitude;

                   mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng_drag));
                    Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());


                        try {

                            List<Address> addresses = geocoder.getFromLocation(latLng_drag.latitude,latLng_drag.longitude, 1);
                            if (addresses.size() > 0) {
                                textView_addrss.setText(addresses.get(0).getLocality()+""+addresses.get(0).getAddressLine(0));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

            }
        });



     final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, showStorage_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    if(jsonObjectMain.getString("message").contains("Data is available!")){

                        JSONArray jsonArray= jsonObjectMain.getJSONArray("alllocations");

                        for (int i = 0; i <jsonArray.length() ;i++) {

                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                              latitude_stg=Double.parseDouble(jsonObject.getString("latitude"));
                              longitude_stg=Double.parseDouble(String.valueOf(jsonObject.getDouble("longtude")));

                             LatLng latLng_storag = new LatLng(latitude_stg, longitude_stg);


                            MarkerOptions markerStorage = new MarkerOptions();

                               marker_tag=jsonObject.getString("tag_color");

                             if(marker_tag.contains("Green")){
                                 //selectColors=HUE_GREEN;
                         markerStorage.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                             }if(marker_tag.contains("Yellow")){

                          markerStorage.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
                            }if(marker_tag.contains("Red")){

                            markerStorage.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            }

                            markerStorage.position(latLng_storag).title(jsonObject.getString("title"));
                            markerList.add(markerStorage);
                            progressDialog.dismiss();

                            //  mMap.addMarker(markerStorage);
                           /*  markerStorage.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng_storag));
                            mMap.animateCamera(CameraUpdateFactory.zoomBy(10));
                            */

                        }
                         showAllMarkers(mMap);
                        progressDialog.dismiss();

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Storage_MapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                netWorkError(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error","tec71");
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Storage_MapsActivity.this);
        requestQueue.add(stringRequest);

    }


    protected synchronized void bulidGoogleApiClient() {
        client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        client.connect();

    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
       locationRequest = new LocationRequest();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(5000);
        // locationRequest.setFastestInterval(100);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(client, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

  latLng_start=new LatLng(location.getLatitude(),location.getLongitude());

        progressDialog.dismiss();

        latitude=location.getLatitude();
        longitude=location.getLongitude();


            Geocoder geocoder = new Geocoder(
                    getApplicationContext(), Locale
                    .getDefault());
            try {

                addressList= geocoder.getFromLocation(latLng_start.latitude,latLng_start.longitude,1);

                    city =addressList.get(0).getLocality();
                    PostalCode =addressList.get(0).getPostalCode();
                    getAddress =addressList.get(0).getAddressLine(0);

                     country=addressList.get(0).getCountryName();

                        MarkerOptions markerOptions = new MarkerOptions();
                        markerOptions.position(latLng_start);
                        markerOptions.title("Current Location");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                // currentMarker = mMap.addMarker(markerOptions);
                        mMap.addMarker(markerOptions);
                 mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_start, 20));

                textView_addrss.setText(""+addressList.get(0).getLocality()+""+addressList.get(0).getAddressLine(0));

            }catch (Exception exception){

                Toast.makeText(getApplicationContext(), "error:"+exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

        if (client != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(client,this);

        }


    }

    public void Add_location(View view) {

       Intent intent = new Intent(Storage_MapsActivity.this,StorageAddActivity.class);
        intent.putExtra("latitude",String.valueOf(latitude));
        intent.putExtra("longitude",String.valueOf(longitude));
        intent.putExtra("Address",textView_addrss.getText().toString());
        setResult(1,intent);
        finish();
    }


    @Override
    public void onBackPressed() {

        Intent intent=new Intent(Storage_MapsActivity.this, StorageAddActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();

       // super.onBackPressed();
    }


   private void showAllMarkers(GoogleMap mMap) {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (MarkerOptions m : markerList) {
            builder.include(m.getPosition());

            // add the parkers to the map
            mMap.addMarker(m);
           // mMap.animateCamera(CameraUpdateFactory.zoomBy(14));
        //   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(),12));

        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

               // String Position =marker.getPosition().toString();
                MaterialDialog mDialog = new MaterialDialog.Builder(Storage_MapsActivity.this)
                        .setTitle("Storage with Flat add Notices")
                        // .setAnimation(R.raw.congratulations)
                        .setMessage("Are you sure you want to Storage Flat add ??")
                        .setCancelable(false)
                        .setPositiveButton("Yes!", new AbstractDialog.OnClickListener() {
                            @Override
                            public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();

                               Add_flat(marker.getPosition().latitude,marker.getPosition().longitude);

                            }
                        })
                        .setNegativeButton("Cancel", new AbstractDialog.OnClickListener() {
                            @Override
                            public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();

                            }
                        })
                        .build();
                mDialog.show();


       return false;
            }
        });

        LatLngBounds bounds = builder.build();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        int padding = (int) (width * 0.30);
        // Zoom and animate the google map to show all markers
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        //mMap.animateCamera(cu);
       mMap.animateCamera(CameraUpdateFactory.zoomBy(25));
    }

    private void Add_flat(double latitude, double longitude){

       String  title= sharedPreferences_type.getString("title","title not found");
        String  comName=   sharedPreferences_type.getString("comName","comName not found");
        String  OwnerName=  sharedPreferences_type.getString("OwnerName","OwnerName not found");
        String  ApprovedDate=   sharedPreferences_type.getString("ApprovedDate","ApprovedDate not found");
        String  renewDate=  sharedPreferences_type.getString("renewDate","renewDate not found");
        String  divisionName=   sharedPreferences_type.getString("divisionName","divisionName not found");
        String  districName=    sharedPreferences_type.getString("districName","districName not found");
        String  thanaName=   sharedPreferences_type.getString("thanaName","thanaName not found");
        String  colors=   sharedPreferences_type.getString("colors","colors not found");
        String  flat=   sharedPreferences_type.getString("flat","flat not found");
        String  CompanyType=  sharedPreferences_type.getString("CompanyType","CompanyType not found");
        String  details=  sharedPreferences_type.getString("details","Addres not found");
        String  encodeImage=  sharedPreferences_type.getString("encodeImage","Addres not found");

        Geocoder geocoder = new Geocoder(
                getApplicationContext(), Locale
                .getDefault());

        try {

            List<Address> addresses = geocoder.getFromLocation(latitude,longitude, 1);
            if (addresses.size() > 0) {
                textView_addrss.setText(addresses.get(0).getLocality()+""+addresses.get(0).getAddressLine(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST, storage_exesting, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    if(jsonObjectMain.getString("message").contains("Add Storage SuccessFully Pls Wait For Verify Process")){

                        progressDialog.dismiss();

                        Toast.makeText(Storage_MapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Storage_MapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                progressDialog.dismiss();
                netWorkError(error);
            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();

                hashMap.put("security_error","tec71");
                hashMap.put("axcess_token",accessTocken);
                hashMap.put("company_Name",comName);
                hashMap.put("company_Owner",OwnerName);
                hashMap.put("license_approved_date",ApprovedDate);
                hashMap.put("license_renew_date",renewDate);
                hashMap.put("division",divisionName);
                hashMap.put("distric",districName);
                hashMap.put("thana",thanaName);
                hashMap.put("latitude", String.valueOf(latitude));
                hashMap.put("longtude", String.valueOf(longitude));
                hashMap.put("company_Type",CompanyType);
                hashMap.put("company_detils",details);
                hashMap.put("alert_tag",colors);
                hashMap.put("storage_img",encodeImage);
                hashMap.put("address", textView_addrss.getText().toString().trim());
                hashMap.put("floor",flat);
                hashMap.put("title",title);
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Storage_MapsActivity.this);
        requestQueue.add(stringRequest);

    }


    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(Storage_MapsActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(Storage_MapsActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(Storage_MapsActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(Storage_MapsActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(Storage_MapsActivity.this, "No connection", Toast.LENGTH_SHORT).show();
            finish();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(Storage_MapsActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();
    }
}
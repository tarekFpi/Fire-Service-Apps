package com.techno71.fireservice.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.AvoidType;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.util.DirectionConverter;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.maps.android.SphericalUtil;
import com.squareup.picasso.Picasso;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Controller.Adepter_Location_Wise_Storage;
import com.techno71.fireservice.Model.AllLocations_WiseStorage_2;
import com.techno71.fireservice.Model.MyLocation;
import com.techno71.fireservice.R;
import com.techno71.fireservice.databinding.ActivityFireMapBinding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FireMapActivity extends FragmentActivity implements
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
    private String showAllLocation_1 = Main_Url.ROOT_URL + "api/show-all-location";
    private String showAllWiseLocation_2 = Main_Url.ROOT_URL + "api/show-all-loaction-wise-storage";
    private String showAllFiremanCommentLocation_3 = Main_Url.ROOT_URL + "api/storage-comment-show-by-fireman";

    private View view_navigation;
    //polyline object
    private List<Polyline> polylines=null;
    private DatabaseReference databaseReference;
    private LatLng userLocationLat;
    private LatLng myLocationLat ;

    private Toolbar toolbar;
    //private @NonNull ActivityFireMapBinding binding;
    private DrawerLayout drawerLayout;

    private String user_infor = Main_Url.ROOT_URL +"api/get-user-info";

    private NavigationView navigationView;

    private ActionBarDrawerToggle toggleButton;

    private TextView textView_fireName,textView_kmTv,textView_Type;

    private CircleImageView circleImageView;

    public static void vibrator(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(50);
        }
    }
    public FireMapActivity() {
    }
    //private String login_url = Main_Url.ROOT_URL + "api/login";

    private ProgressDialog pDialog;
        @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  binding= ActivityFireMapBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_fire_map);
            pDialog = new ProgressDialog(this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        sharedPreferences_type = getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);
        databaseReference = FirebaseDatabase.getInstance().getReference("FireService").child("Agent_Location_Details");
         access_token = sharedPreferences_type.getString("access_token", "access_token found");
        phone = sharedPreferences_type.getString("phon", "01700000000");
        fusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this);

        double latitude=sharedPreferences_type.getFloat("latitude001",00.0f);
        double longitude=sharedPreferences_type.getFloat("longitude001",00.00f);


   /*      double latitude= Double.parseDouble(sharedPreferences_type.getString("latitude001","00.00"));
         double longitude= Double.parseDouble(sharedPreferences_type.getString("longitude001","00.00"));
*/
        //userLocaton
       userLocationLat = new LatLng(latitude,longitude);

        checkLocationPermission();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

            toolbar=(Toolbar)findViewById(R.id.toolbar_admin);
            textView_kmTv=(TextView)findViewById(R.id.kmTv);

             drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layoutAdmin);
             navigationView=(NavigationView)findViewById(R.id.navigation_admin);

            toggleButton=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
            toggleButton.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

            drawerLayout.addDrawerListener(toggleButton);
            toggleButton.syncState();

            view_navigation= navigationView.inflateHeaderView(R.layout.admin_header);
            textView_fireName= view_navigation.findViewById(R.id.textNavadminName);

            circleImageView=view_navigation.findViewById(R.id.text_fireManImage);

            textView_Type= view_navigation.findViewById(R.id.textadminType);
            textView_Type.setText("Fire Man");


            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {


                    case R.id.sideAd_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.Side_AdlogOut:

                       Intent intent=new Intent(FireMapActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        Toast.makeText(FireMapActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        sharedPreferences_type.edit().clear().commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.side_AdchangePassword:

                        Intent intent_pass=new Intent(FireMapActivity.this,InformationLoddingActivity.class);
                        intent_pass.putExtra("Techno","userPass");
                        startActivity(intent_pass);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

//
                    case R.id.sideAd_profile:

                        Intent intent_prf=new Intent(FireMapActivity.this,InformationLoddingActivity.class);
                        intent_prf.putExtra("Techno","userPrf");
                        startActivity(intent_prf);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideAd_news:

                        Intent intent_news=new Intent(FireMapActivity.this,InformationLoddingActivity.class);
                        intent_news.putExtra("Techno","news");
                        startActivity(intent_news);

                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideAd_setting:

                        Intent intent_settting=new Intent(FireMapActivity.this,Setting_Activity.class);
                        startActivity(intent_settting);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                }
                return false;
            }
        });


            getUserInfromation();
    }

    @Override
    protected void onResume() {
        super.onResume();

        getUserInfromation();
    }

    private void getUserInfromation(){

      final ProgressDialog progressDialog = new ProgressDialog(FireMapActivity.this);

        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String  accessTocken= sharedPreferences_type.getString("access_token","access_token found");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, user_infor, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);


                    if(jsonObjectMain.getString("message").contains("Data is available!")){
                        progressDialog.dismiss();

                        JSONArray jsonArray= jsonObjectMain.getJSONArray("UserInfo");

                        for (int i = 0; i <jsonArray.length() ;i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            sharedPreferences_type.edit().putString("status",jsonObject.getString("company_verify_status")).commit();
                            sharedPreferences_type.edit().putString("user_type",jsonObject.getString("user_type")).commit();
                            sharedPreferences_type.edit().putString("user_id",jsonObject.getString("user_id")).commit();

                            textView_fireName.setText(""+jsonObject.getString("user_name"));

                            String image="https://fifaar.com/public/"+jsonObject.getString("user_picture");

                            Picasso.get().load(image).into(circleImageView);

                        }

                    }else if(jsonObjectMain.getString("message").contains("Data is Not available!")){
                        sharedPreferences_type.edit().remove("status");
                        sharedPreferences_type.edit().remove("user_type");

                        Toast.makeText(FireMapActivity.this,"User Information Not Found..", Toast.LENGTH_LONG).show();

                    }else if(jsonObjectMain.getString("message").contains("Sry Access Token Not match")){

                        Intent intent=new Intent(FireMapActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        sharedPreferences_type.edit().clear().commit();
                        Toast.makeText(FireMapActivity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }else{

                        progressDialog.dismiss();
                        Toast.makeText(FireMapActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("axcess_token",accessTocken);

                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(FireMapActivity.this);
        requestQueue.add(stringRequest);
    }

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(FireMapActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(FireMapActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(FireMapActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(FireMapActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(FireMapActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(FireMapActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
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

                new CountDownTimer(60000, 2000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        if (isActivityStart){
                            getLocationMap();

                        }
                    }

                    @Override
                    public void onFinish() {
                        getLocationMap();
                    }
                }.start();
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                vibrator(getApplicationContext());
                showAllWiseLocation_2(marker.getPosition().latitude,marker.getPosition().longitude);
                return false;
            }
        });
        showAllLocation_1();

    }


    private String[] colors = {"#7fff7272", "#7f31c7c5", "#7fff8a00"};
    private Double distance;

    private boolean isActivityStart =true;
    @SuppressLint("DefaultLocale")
    private void getLocationMap(){

        if (myLocationLat==null){

        }else {

            if (userLocationLat==null){

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

        //    markerOptions1.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
             markerOptions1.icon(BitmapDescriptorFactory.fromResource(R.drawable.flames));
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                myLocationLat, 16f);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocationLat));
            if (isActivityStart){
                isActivityStart=false;
                mMap.animateCamera(CameraUpdateFactory.zoomTo(1));

            }

            mMap.addMarker(markerOptions1);
            //mMap.addMarker(markerOptions);

            ArrayList<LatLng> directionPositionList = direction.getRouteList().get(0).getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));

            textView_kmTv.setVisibility(View.VISIBLE);
            textView_kmTv.setText("Distance \n " + String.format("%.2f", distance / 1000) + "km");
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
    public void showAllLocation_1() {
        //pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);


        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, showAllLocation_1,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObjectMain=new JSONObject(response);
                    JSONArray jsonArray= jsonObjectMain.getJSONArray("alllocations");

                    if (jsonObjectMain.getString("message").equals("Data is available!")){
                        
                        for (int i=0;i<jsonArray.length();i++){
                            String id= jsonArray.getJSONObject(i).getString("id");
                            String title= jsonArray.getJSONObject(i).getString("title");
                            double latitude= Double.parseDouble(jsonArray.getJSONObject(i).getString("latitude"));
                            double longtude= Double.parseDouble(jsonArray.getJSONObject(i).getString("longtude"));
                            String tag_color= jsonArray.getJSONObject(i).getString("tag_color");

                            if (tag_color.equals("Green")){
                                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtude)).title(title).snippet(id)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                            }else if (tag_color.equals("Red")){

                                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtude)).title(title).snippet(id)
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

                            }else {
                                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtude)).title(title).snippet(id)
                                   .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));

                            }
                            mMap.animateCamera(CameraUpdateFactory.zoomTo(1));

                        }
                        pDialog.dismiss();
                    }else {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();

                    }

                } catch (JSONException e) {
                    pDialog.dismiss();
                    Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();

                    new CountDownTimer(5000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            showAllLocation_1();

                        }
                    }.start();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Toast.makeText(FireMapActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        showAllLocation_1();

                    }
                }.start();


            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error","tec71");

                return hashMap;
            }
        };
        requestQueue.add(stringRequest1);

    }

    private List<AllLocations_WiseStorage_2> allLocationsWiseStorage2s;
    private Adepter_Location_Wise_Storage adepterLocationWiseStorage;
    public void showAllWiseLocation_2(double latitude,double longtitude) {
    //    pDialog.show();

        allLocationsWiseStorage2s=new ArrayList<>();
        adepterLocationWiseStorage=new Adepter_Location_Wise_Storage(this,allLocationsWiseStorage2s);
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        /*
        allLocationsWiseStorage2s= new Gson().fromJson(
                jsonArray.toString(),
                new TypeToken<List<Adepter_Location_Wise_Storage>>(){}.getType();
        */

        StringRequest stringRequest1 = new StringRequest(Request.Method.POST, showAllWiseLocation_2,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObjectMain=new JSONObject(response);
                    JSONArray jsonArray= jsonObjectMain.getJSONArray("districts");
                    if (jsonObjectMain.getString("message").equals("Data is available!")){

                        for (int i=0;i<jsonArray.length();i++){
                            String id= jsonArray.getJSONObject(i).getString("id");
                            String status= jsonArray.getJSONObject(i).getString("status");
                            if (status.equals("1")){
                                AllLocations_WiseStorage_2 storage_2=new AllLocations_WiseStorage_2(
                                        ""+jsonArray.getJSONObject(i).getString("id"),
                                        ""+jsonArray.getJSONObject(i).getString("floor"),
                                        ""+jsonArray.getJSONObject(i).getString("company_name"),
                                        ""+jsonArray.getJSONObject(i).getString("company_owner"),
                                        ""+jsonArray.getJSONObject(i).getString("license_approved_date"),
                                        ""+jsonArray.getJSONObject(i).getString("license_renew_date"),
                                        ""+jsonArray.getJSONObject(i).getString("address"),
                                        ""+jsonArray.getJSONObject(i).getString("thana"),
                                        ""+jsonArray.getJSONObject(i).getString("distric"),
                                        ""+jsonArray.getJSONObject(i).getString("division"),
                                        ""+jsonArray.getJSONObject(i).getString("company_type"),
                                        ""+jsonArray.getJSONObject(i).getString("company_detils"),
                                        ""+jsonArray.getJSONObject(i).getString("storage_img"),
                                        ""+jsonArray.getJSONObject(i).getString("alert_tag"),
                                        ""+jsonArray.getJSONObject(i).getString("status")

                                );
                                allLocationsWiseStorage2s.add(storage_2);
                            }

                        }
                        pDialog.dismiss();
                        bottomSheetDialog();

                    }else {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "No Data Found", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                  //  Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_SHORT).show();

                    pDialog.dismiss();
                    new CountDownTimer(5000, 1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            showAllWiseLocation_2(latitude,longtitude);

                        }
                    }.start();

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Flat Not Found", Toast.LENGTH_SHORT).show();

                //   Toast.makeText(FireMapActivity.this, ""+error, Toast.LENGTH_SHORT).show();
                new CountDownTimer(5000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        showAllWiseLocation_2(latitude,longtitude);

                    }
                }.start();

            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error","tec71");
                hashMap.put("latitude",""+latitude);
                hashMap.put("longtude",""+longtitude);

                return hashMap;
            }
        };
        requestQueue.add(stringRequest1);

    }

    private void bottomSheetDialog() {
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.dialog_location_fiar);
        RecyclerView recyclerView=bottomSheetDialog.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adepterLocationWiseStorage);
        bottomSheetDialog.show();
    }




}
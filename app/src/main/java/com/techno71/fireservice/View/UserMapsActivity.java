package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.firebase.messaging.FirebaseMessaging;
import com.squareup.picasso.Picasso;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Circle;
import com.techno71.fireservice.CircleAnimation;
import com.techno71.fireservice.Controller.Location_wishStorageController;
import com.techno71.fireservice.MainActivity;
import com.techno71.fireservice.Model.FetchData;
import com.techno71.fireservice.Model.LocationWithStorageShow;

import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;
import me.tankery.lib.circularseekbar.CircularSeekBar;


public class UserMapsActivity extends FragmentActivity implements
        Animation.AnimationListener,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener
         {

    private GoogleMap mMap;

    private static  final  int request_code=101;

    private double lat,lng;

    private Button button;

    private int Count_down=0;

    private Handler handler = new Handler();

    private View view;
    private String marker_tag;

    private int max=5000;

    private  double latitude_stg,longitude_stg;

    private LayoutInflater layoutInflater;

    private EditText editText_tile,editText_message;

    private   ProgressBar progressBar;

    private TextView textView_progCount,textView_userName;

    private List<LocationWithStorageShow>locationWithStorageShowList=new ArrayList<>();

    private Location_wishStorageController location_wishStorageController;

    int pStatus=0;

 private String  locatinUpdate= Main_Url.ROOT_URL +"api/update-map-location";

  //private String  locatinWith_storage= Main_Url.ROOT_URL +"api/show-all-storage-loaction";

  private String  loactionWise_storage= Main_Url.ROOT_URL +"api/show-all-loaction-wise-storage";

   private String  userLocation_storage= Main_Url.ROOT_URL +"api/show-all-storage-loaction";


  private String send_alert_url = Main_Url.ROOT_URL +"api/send-alert";


    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private ActionBarDrawerToggle toggleButton;

    private CircularSeekBar circularSeekBar;

    private  CountDownTimer countDownTimer;

    private BottomSheetDialog bottomSheetAlam;

    private SharedPreferences sharedPreferences_type;

    private String message;
   private String alertTypeName="";

    private String user_infor = Main_Url.ROOT_URL +"api/get-user-info";
    private String user_blockcheck = Main_Url.ROOT_URL +"api/check-user-block-or-not";

    private String alertType_url = Main_Url.ROOT_URL +"api/show-alert-type";

    private View view_navigation;

    private ImageView imageView_user;

    public Circle curve;
    private LinearLayout taplayout;
    private boolean cancel = false;

    private MeowBottomNavigation bottomNavigation;

    private final static int transaction_log = 1;

    private GoogleApiClient client;

    FusedLocationProviderClient fusedLocationProviderClient;

    private LocationRequest locationRequest;

    private double myLatitud,myLongitude;
    private List<MarkerOptions> markerList=new ArrayList<>();

    private SupportMapFragment mapFragment;

    private RecyclerView recyclerView_locationStorag;

     private CircleImageView circleImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_maps);
        toolbar=(Toolbar)findViewById(R.id.toolbar_usermap);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

         FirebaseMessaging.getInstance().setAutoInitEnabled(true);

         FirebaseMessaging.getInstance().subscribeToTopic("all");


        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_user);
        navigationView=(NavigationView)findViewById(R.id.navigation_user);


        bottomNavigation =(MeowBottomNavigation)findViewById(R.id.bottomNavigation);

        bottomNavigation.add(new MeowBottomNavigation.Model(transaction_log, R.drawable.ic_alert_24));

        toggleButton=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        toggleButton.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        drawerLayout.addDrawerListener(toggleButton);
        toggleButton.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.sideUser_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideUser_logOut:

                        Intent intent=new Intent(UserMapsActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        sharedPreferences_type.edit().clear().commit();
                        Toast.makeText(UserMapsActivity.this, "LogOut", Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.sideUser_changePassword:

                        Intent intent_pass=new Intent(UserMapsActivity.this,InformationLoddingActivity.class);
                        intent_pass.putExtra("Techno","userPass");
                        startActivity(intent_pass);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideUser_news:

                        Intent intent_news=new Intent(UserMapsActivity.this,InformationLoddingActivity.class);
                        intent_news.putExtra("Techno","news");
                        startActivity(intent_news);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideUser_mycomment:
                        Intent intent_comment=new Intent(UserMapsActivity.this,InformationLoddingActivity.class);
                        intent_comment.putExtra("Techno","comment");
                        startActivity(intent_comment);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.sideUser_profile:
                        Intent intent_prf=new Intent(UserMapsActivity.this,InformationLoddingActivity.class);
                        intent_prf.putExtra("Techno","userPrf");
                        startActivity(intent_prf);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.SideUser_setting:
                        Intent intent_settting=new Intent(UserMapsActivity.this,Setting_Activity.class);
                        startActivity(intent_settting);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideUser_call:

                        Intent intent_call=new Intent(UserMapsActivity.this,InformationLoddingActivity.class);
                        intent_call.putExtra("Techno","call");
                        startActivity(intent_call);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                return false;
            }
        });

        view_navigation =navigationView.inflateHeaderView(R.layout.user_header);

        circleImageView=view_navigation.findViewById(R.id.text_userImage);


        textView_userName=(TextView)view_navigation.findViewById(R.id.textNUserName);

        fusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this.getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
          mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    @Override
    protected void onStart() {
        super.onStart();

         getUserInfromation();

    }

    @Override
    protected void onResume() {
        super.onResume();

        language_model language=new language_model(this);
        language.loadLanguage();

        verify_Account();
    }

    private void getUserInfromation(){

        final ProgressDialog progressDialog = new ProgressDialog(UserMapsActivity.this);

        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

   String  accessTocken = sharedPreferences_type.getString("access_token","access_token found");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, user_infor, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    message= jsonObjectMain.getString("message");

                    if(message.contains("Data is available!")){
                        progressDialog.dismiss();

                JSONArray jsonArray= jsonObjectMain.getJSONArray("UserInfo");

                        for (int i = 0; i <jsonArray.length() ;i++) {

                       JSONObject jsonObject = jsonArray.getJSONObject(i);

                     textView_userName.setText(""+jsonObject.getString("user_name"));

                     String image="https://fifaar.com/public/"+jsonObject.getString("user_picture");

                     Picasso.get().load(image).into(circleImageView);

                        }

                    }else if(message.contains("Sry Access Token Not match")){
                        Toast.makeText(UserMapsActivity.this,message, Toast.LENGTH_LONG).show();
                        sharedPreferences_type.edit().clear().commit();
                        Intent intent=new Intent(UserMapsActivity.this,LoginActivity.class);
                        startActivity(intent);
                        finish();

                    } else{

                        progressDialog.dismiss();
                        Toast.makeText(UserMapsActivity.this, message, Toast.LENGTH_SHORT).show();
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

                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(UserMapsActivity.this);
        requestQueue.add(stringRequest);
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

    }


    private void verify_Account(){

                 String  accessTocken= sharedPreferences_type.getString("access_token","access_token found");

                 StringRequest stringRequest = new StringRequest(Request.Method.POST, user_blockcheck, new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {

                         try {
                             JSONObject jsonObjectMain=new JSONObject(response);

                             if(jsonObjectMain.getString("message").contains("Blocked")){

                                 Intent intent=new Intent(UserMapsActivity.this,LoginActivity.class);
                                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                 startActivity(intent);
                                 finish();
                                 sharedPreferences_type.edit().clear().commit();

                                 Toast.makeText(UserMapsActivity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                             }else if(!jsonObjectMain.getString("message").contains("Active")){

                                 Toast.makeText(UserMapsActivity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                             }

                         } catch (JSONException e) {
                             e.printStackTrace();

                         }
                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {

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

                 RequestQueue requestQueue1= Volley.newRequestQueue(UserMapsActivity.this);
                 requestQueue1.add(stringRequest);
             }

     protected synchronized void bulidGoogleApiClient() {
         client = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
         client.connect();

             }


       private void showAllMarkers(GoogleMap mMap) {

                 LatLngBounds.Builder builder = new LatLngBounds.Builder();
                 for (MarkerOptions m : markerList) {
                     builder.include(m.getPosition());
                     // add the parkers to the map
                     mMap.addMarker(m);
                     mMap.animateCamera(CameraUpdateFactory.zoomBy(14));
                     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(),12));
                 }

           LatLngBounds bounds = builder.build();
           int width = getResources().getDisplayMetrics().widthPixels;
           int height = getResources().getDisplayMetrics().heightPixels;
           int padding = (int) (width * 0.30);
           // Zoom and animate the google map to show all markers
           CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
           mMap.animateCamera(cu);
           mMap.animateCamera(CameraUpdateFactory.zoomBy(25));

            mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
          @Override
          public boolean onMarkerClick(@NonNull Marker marker) {

              vibrator(getApplicationContext());

              BottomSheetDialog  bottomSheet_option = new BottomSheetDialog(UserMapsActivity.this);
              LayoutInflater Inflater = getLayoutInflater();
              View view = Inflater.inflate(R.layout.alert_option_select, null);
              bottomSheet_option.setContentView(view);
              bottomSheet_option.show();
              Button button_flat=view.findViewById(R.id.button_flat_submit);

              button_flat.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                      Show_loaction_wise_Storage(marker.getPosition().latitude,marker.getPosition().longitude);
                      bottomSheet_option.dismiss();
                  }
              });

              Button button_alert=view.findViewById(R.id.floating_alert);


              button_alert.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View view) {

                      OpenAlert();

                      bottomSheet_option.dismiss();

                      LatLng latLng_drag  =new LatLng(marker.getPosition().latitude,marker.getPosition().longitude);

                      Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                      try {

                          List<Address> addresses = geocoder.getFromLocation(latLng_drag.latitude,latLng_drag.longitude, 1);
                          if (addresses.size() > 0) {

                              String address=  addresses.get(0).getAddressLine(0);
                              sharedPreferences_type.edit().putString("user_Address", address).commit();
                              sharedPreferences_type.edit().putString("latitude", String.valueOf(latLng_drag.latitude)).commit();
                              sharedPreferences_type.edit().putString("longitude", String.valueOf(latLng_drag.longitude)).commit();

                          }
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
                      bottomSheet_option.dismiss();
                  }
              });

              return false;

          }
      });

        }

             public static void vibrator(Context context) {
                 Vibrator v = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                     v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
                 } else {
                     v.vibrate(50);
                 }
             }


   private void  Show_loaction_wise_Storage(double latitude,double longitude){

       BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
       LayoutInflater layoutInflater1 = getLayoutInflater();
       View view1=layoutInflater1.inflate(R.layout.user_location_storag_show,null);
       bottomSheetDialog.setContentView(view1);

         recyclerView_locationStorag=(RecyclerView)view1.findViewById(R.id.recyclerView_locationStorag);
         recyclerView_locationStorag.setHasFixedSize(true);
         recyclerView_locationStorag.setLayoutManager(new LinearLayoutManager(this));

       StringRequest stringRequest = new StringRequest(Request.Method.POST, loactionWise_storage, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

               try {
                   JSONObject jsonObjectMain=new JSONObject(response);

                   if(jsonObjectMain.getString("message").contains("Data is available!")){

                       JSONArray jsonArray= jsonObjectMain.getJSONArray("districts");
                       locationWithStorageShowList.clear();
                    for (int i = 0; i <jsonArray.length() ;i++) {

                    JSONObject jsonObject=jsonArray.getJSONObject(i);

                  String image="https://fifaar.com/public/"+jsonObject.getString("storage_img");

                     if(jsonObject.getString("status").contains("1")){

                        LocationWithStorageShow withStorageShow=
                                new LocationWithStorageShow(jsonObject.getString("id"),
                                        jsonObject.getString("floor"),
                                        jsonObject.getString("company_name"),
                                        jsonObject.getString("company_owner"),
                                        jsonObject.getString("license_approved_date"),
                                        jsonObject.getString("license_renew_date"),
                                        jsonObject.getString("address"),
                                        jsonObject.getString("division"),
                                        jsonObject.getString("distric"),
                                        jsonObject.getString("thana"),
                                        jsonObject.getString("company_type"),
                                        jsonObject.getString("company_detils"),
                                        image,
                                        jsonObject.getString("alert_tag"),
                                        jsonObject.getString("status"));

                                  locationWithStorageShowList.add(withStorageShow);
                              }

                            }
                       location_wishStorageController=new Location_wishStorageController(locationWithStorageShowList,UserMapsActivity.this);
                       recyclerView_locationStorag.setAdapter(location_wishStorageController);
                       location_wishStorageController.notifyDataSetChanged();

                       bottomSheetDialog.show();
                        }else{
                       Toast.makeText(UserMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                   }
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               netWorkError(error);

           }
       }){
           @Override
           protected Map<String, String> getParams() {

               Map<String, String> hashMap = new HashMap<String, String>();
               hashMap.put("security_error","tec71");
               hashMap.put("latitude", String.valueOf(latitude));
               hashMap.put("longtude", String.valueOf(longitude));
               return hashMap;
           }
       };

       RequestQueue requestQueue= Volley.newRequestQueue(UserMapsActivity.this);
       requestQueue.add(stringRequest);

     }


     private void OpenAlert(){

                 bottomSheetAlam = new BottomSheetDialog(this);
                 LayoutInflater Inflater = getLayoutInflater();
                 View view1 = Inflater.inflate(R.layout.alarm_bottom_sheet_dialog, null);

                 CheckBox checkBox = view1.findViewById(R.id.check_Terms_Conditions);

                 Spinner spinner_alertType=(Spinner)view1.findViewById(R.id.alertType_spinner);
                 ArrayList<String> arrayList_alertType= new ArrayList<>();


                 StringRequest stringRequest = new StringRequest(Request.Method.POST, alertType_url, new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {

                         try {
                             JSONObject jsonObjectMain=new JSONObject(response);

                             arrayList_alertType.clear();

                             arrayList_alertType.add("Select--Alert--Type--");

                             if(jsonObjectMain.getString("message").contains("Data is available!")){

                                 JSONArray jsonArray= jsonObjectMain.getJSONArray("alerType");

                                 for (int i = 0; i <jsonArray.length() ;i++) {

                                     JSONObject jsonObject=jsonArray.getJSONObject(i);

                                     arrayList_alertType.add(jsonObject.getString("name"));
                                 }

                                 ArrayAdapter<String> arrayAdapter_alert=new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList_alertType);
                                 spinner_alertType.setAdapter(arrayAdapter_alert);


                                 spinner_alertType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                     @Override
                                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                                         alertTypeName= arrayList_alertType.get(position).toString();
                                     }
                                     @Override
                                     public void onNothingSelected(AdapterView<?> parent) {

                                     }
                                 });
                             }else{

                                 Toast.makeText(UserMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {

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

                 RequestQueue requestQueue= Volley.newRequestQueue(UserMapsActivity.this);
                 requestQueue.add(stringRequest);

                 editText_message = view1.findViewById(R.id.edit_userMessag);

                 curve = view1.findViewById(R.id.curve_alarm);
                 taplayout = view1.findViewById(R.id.layouttap);
                 final CircleAnimation circleAnimation = new CircleAnimation(curve, 127.0f);
                 this.curve.setCurveColor(ContextCompat.getColor(this, R.color.Fuchsia), ContextCompat.getColor(this, R.color.tap_dark));
                 circleAnimation.setDuration(2000);
                 circleAnimation.setAnimationListener(this);
                 taplayout.setOnTouchListener(new View.OnTouchListener() {

                     public boolean onTouch(View view, MotionEvent motionEvent) {
                         if (!checkBox.isChecked()) {
                             Toast.makeText(getApplicationContext(), "Select Terms Conditions", Toast.LENGTH_SHORT).show();
                             checkBox.setError("Terms Conditions!!");

                         }else if(spinner_alertType.getSelectedItem().toString().contains("Select--Alert--Type--")){

                             Toast.makeText(UserMapsActivity.this, "Please Your Alet Type", Toast.LENGTH_SHORT).show();

                         }  else{

                             // String message=  editText_message.getText().toString().trim();
                             switch (motionEvent.getAction()) {
                                 case 0:
                                     curve.startAnimation(circleAnimation);
                                     cancel = false;

                                     //  Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();
                                     return true;
                                 case 1:
                                 case 3:
                                     // Toast.makeText(MainActivity.this, "SuccessFull", Toast.LENGTH_SHORT).show();
                                     circleAnimation.cancel();
                                     cancel = true;
                                     return true;
                                 default:
                                     return false;
                             }
                         }
                         return true;
                     }
                 });

                 bottomSheetAlam.setContentView(view1);
                 bottomSheetAlam.show();

             }

     private void sendAlet(String alertTypeName){

                 // sharedPreferences_type.edit().putString("user_Address", address)
                 String  accessTocken= sharedPreferences_type.getString("access_token","access_token found");

                 String  user_Address= sharedPreferences_type.getString("user_Address","user_Address found");
                 String  latitude_storage= sharedPreferences_type.getString("latitude","user_Address found");
                 String  longitude_storage= sharedPreferences_type.getString("longitude","user_Address found");


                 String  storage_details=  user_Address+""+latitude_storage+""+longitude_storage;

                 StringRequest stringRequest = new StringRequest(Request.Method.POST, send_alert_url, new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {

                         try {

                             JSONObject jsonObjectMain = new JSONObject(response);
                             if(jsonObjectMain.getString("message").contains("Alert Send Successfully!")) {

                                 MaterialDialog mDialog = new MaterialDialog.Builder(UserMapsActivity.this)
                                         .setTitle("Successfully Send Notification..")
                                         .setAnimation(R.raw.successful)
                                         .setCancelable(false)
                                         .setPositiveButton("Ok", new AbstractDialog.OnClickListener() {
                                             @Override
                                             public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                                 dialogInterface.dismiss();

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

                                 Toast.makeText(UserMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                             }else {

                                 Toast.makeText(UserMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                             }
                         }  catch (Exception e) {
                             e.printStackTrace();
                         }

                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         netWorkError(error);

                     }
                 }){
                     @Override
                     protected Map<String, String> getParams() {

                         Map<String, String> hashMap = new HashMap<String, String>();
                         hashMap.put("security_error","tec71");
                         hashMap.put("axcess_token",accessTocken);
                         hashMap.put("detils",storage_details);
                         hashMap.put("latitude", latitude_storage);
                         hashMap.put("longtude", longitude_storage);
                         return hashMap;
                     }
                 };

                 RequestQueue requestQueue= Volley.newRequestQueue(UserMapsActivity.this);
                 requestQueue.add(stringRequest);
             }


         /*    private void ShowAlown(String message){

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
        try {

            List<Address> addresses = geocoder.getFromLocation(myLatitud,myLongitude, 1);
            if (addresses.size() > 0) {

                *//*UserNotificationsSender notificationsSender=new
                        UserNotificationsSender("/topics/all",""+addresses.get(0).getLocality()+""+addresses.get(0).getAddressLine(0),message,getApplicationContext(),UserMapsActivity.this);
                notificationsSender.SendNotifications();*//*
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

      MaterialDialog mDialog = new MaterialDialog.Builder(UserMapsActivity.this)
                .setTitle("Successfully Send Notification..")
                .setAnimation(R.raw.successful)
                // .setMessage("Dear  Candidate Congratulations you are shorlisted   for scratch card")
                .setCancelable(false)
                .setPositiveButton("Ok", new AbstractDialog.OnClickListener() {
                    @Override
                    public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                        dialogInterface.dismiss();

                        editText_message.setText("");

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

    }

*/
    @Override
    public void onBackPressed() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finishAffinity();
                    }
                }).show();
    }

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (cancel){
            Log.d("anim","Incomplete");

        }else {

            if (!editText_message.getText().toString().isEmpty()) {

                String message=editText_message.getText().toString();

                sendAlet(alertTypeName);
                bottomSheetAlam.dismiss();

            }else{


                sendAlet(alertTypeName);
                bottomSheetAlam.dismiss();

            }

        }
    }

         @Override
          public void onAnimationRepeat(Animation animation) {

          }

             @Override
             public void onConnected(@Nullable Bundle bundle) {
                 locationRequest = new LocationRequest();
                 locationRequest.setInterval(10000);
                 locationRequest.setFastestInterval(10000);
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

                 LatLng  latLng_curent=new LatLng(location.getLatitude(),location.getLongitude());

                 myLatitud=location.getLatitude();
                 myLongitude=location.getLongitude();

                 sharedPreferences_type.edit().putString("myLatitud",String.valueOf(location.getLatitude())).commit();
                 sharedPreferences_type.edit().putString("myLongitude",String.valueOf(location.getLongitude())).commit();

                 try {

                     MarkerOptions markerOptions = new MarkerOptions();
                     markerOptions.position(latLng_curent);
                     markerOptions.title("Current Location");
                     markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));

                     mMap.addMarker(markerOptions);
                     mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng_curent, 20));
                 }catch (Exception exception){

                     Toast.makeText(getApplicationContext(), "error:"+exception.getMessage(), Toast.LENGTH_SHORT).show();
                 }

                 if (client != null) {

                     LocationServices.FusedLocationApi.removeLocationUpdates(client,this);
                 }

                 UpdateLocation(location.getLatitude(),location.getLongitude());
                 ShowStorage(location.getLatitude(),location.getLongitude());
             }


             private void  ShowStorage(double latitude,double longitude){

                 StringRequest stringRequest = new StringRequest(Request.Method.POST, userLocation_storage, new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {

                         try {
                             JSONObject jsonObjectMain=new JSONObject(response);

                             if(jsonObjectMain.getString("message").contains("Data is available!")){

                                 JSONArray jsonArray= jsonObjectMain.getJSONArray("alllocations");

                                 for (int i = 0; i <jsonArray.length() ;i++) {

                                     JSONObject jsonObject=jsonArray.getJSONObject(i);

                                     if(jsonObject.getString("status").contains("1")){

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

                                         markerStorage.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                                         markerStorage.position(latLng_storag).title(jsonObject.getString("title"));
                                         markerList.add(markerStorage);
                                     }


                                 }
                                 showAllMarkers(mMap);

                             }else{

                             Toast.makeText(UserMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
                             }
                         } catch (JSONException e) {
                             e.printStackTrace();
                         }
                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         netWorkError(error);

                     }
                 }){
                     @Override
                     protected Map<String, String> getParams() {

                         Map<String, String> hashMap = new HashMap<String, String>();
                         hashMap.put("security_error","tec71");
                         hashMap.put("latitude", String.valueOf(latitude));
                         hashMap.put("longtude", String.valueOf(longitude));
                         return hashMap;
                     }
                 };

                 RequestQueue requestQueue= Volley.newRequestQueue(UserMapsActivity.this);
                 requestQueue.add(stringRequest);
             }

             private void UpdateLocation(double latitude,double longtude){

                 String  accessTocken= sharedPreferences_type.getString("access_token","access_token found");

                 StringRequest stringRequest = new StringRequest(Request.Method.POST, locatinUpdate, new Response.Listener<String>() {
                     @Override
                     public void onResponse(String response) {

                         try {
                             JSONObject jsonObjectMain=new JSONObject(response);

                             if(jsonObjectMain.getString("message").contains("Successfully Update Location")){

                               Toast.makeText(UserMapsActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
                             }

                         } catch (JSONException e) {
                             e.printStackTrace();

                         }
                     }
                 }, new Response.ErrorListener() {
                     @Override
                     public void onErrorResponse(VolleyError error) {
                         netWorkError(error);
                     }
                 }){
                     @Override
                     protected Map<String, String> getParams() {

                         Map<String, String> hashMap = new HashMap<String, String>();

                         hashMap.put("security_error","tec71");
                         hashMap.put("axcess_token",accessTocken);
                         hashMap.put("latitude", String.valueOf(latitude));
                         hashMap.put("longtude", String.valueOf(longtude));

                         return hashMap;
                     }
                 };

                 RequestQueue requestQueue= Volley.newRequestQueue(UserMapsActivity.this);
                 requestQueue.add(stringRequest);
             }

             private void netWorkError(VolleyError error) {

                 if (error instanceof NetworkError) {
                     Toast.makeText(UserMapsActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof ServerError) {
                     Toast.makeText(UserMapsActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof AuthFailureError) {
                     Toast.makeText(UserMapsActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof ParseError) {
                     Toast.makeText(UserMapsActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof NoConnectionError) {
                     Toast.makeText(UserMapsActivity.this, "No connection", Toast.LENGTH_SHORT).show();
                 } else if (error instanceof TimeoutError) {
                     Toast.makeText(UserMapsActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
                 }
                 error.printStackTrace();

             }

         }


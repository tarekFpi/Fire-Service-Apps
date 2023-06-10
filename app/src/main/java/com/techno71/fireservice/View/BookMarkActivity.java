package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Circle;
import com.techno71.fireservice.CircleAnimation;
import com.techno71.fireservice.Controller.BookMarkAdapter;
import com.techno71.fireservice.Model.BookMarkShow;
import com.techno71.fireservice.Model.language_model;
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

public class BookMarkActivity extends AppCompatActivity  implements
     Animation.AnimationListener{

    private BookMarkAdapter adapter;

    private List<BookMarkShow> markShowList=new ArrayList<>();

    private RecyclerView recyclerView;

    private String accessTocken="",userId;

    private String alertType_url = Main_Url.ROOT_URL +"api/show-alert-type";

    private String send_alert_url = Main_Url.ROOT_URL +"api/send-alert";

    private String alertTypeName="";

    private BottomSheetDialog bottomSheetAlam;

    public Circle curve;
    private LinearLayout taplayout;
    private boolean cancel = false;

    private Context context;
    private SharedPreferences sharedPreferences_type;

    private String bookmark_url= Main_Url.ROOT_URL +"api/show-bookmark-list";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);

        toolbar=(Toolbar)findViewById(R.id.toolbar_bookMark);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        context = getApplicationContext();

        sharedPreferences_type= getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        accessTocken = sharedPreferences_type.getString("access_token","access_token found");

        userId = sharedPreferences_type.getString("user_id","user_id found");

        language_model langua_model=new language_model(BookMarkActivity.this);
        langua_model.loadLanguage();

        recyclerView=(RecyclerView)findViewById(R.id.bookBark_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(BookMarkActivity.this));

        ShowData();

    }

    private void ShowData(){

        final ProgressDialog progressDialog = new ProgressDialog(BookMarkActivity.this);

        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, bookmark_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    if(jsonObjectMain.getString("message").contains("Data is available!")){

                        JSONArray jsonArray= jsonObjectMain.getJSONArray("bookmarklist");

                        for (int i = 0; i <jsonArray.length() ;i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String image="https://fifaar.com/public/"+jsonObject.getString("storage_img");

                            BookMarkShow bookMarkShow=
                                    new BookMarkShow(jsonObject.getString("company_id")
                                            ,jsonObject.getString("company_name")
                                            ,jsonObject.getString("company_owner"),
                                            jsonObject.getString("license_approved_date"),
                                            jsonObject.getString("license_renew_date"),
                                            jsonObject.getString("address"),
                                            jsonObject.getString("division"),
                                            jsonObject.getString("alert_tag"),
                                            jsonObject.getString("company_detils"),
                                            jsonObject.getString("company_type"),
                                            image,
                                            jsonObject.getString("status"),
                                            jsonObject.getString("latitude"),
                                            jsonObject.getString("longtude")
                                    );

                            markShowList.add(bookMarkShow);

                        }
                        adapter=new BookMarkAdapter(markShowList,BookMarkActivity.this);
                        recyclerView.setAdapter(adapter);

                        adapter.setOnItemClick(new BookMarkAdapter.onItemClickLisiner() {
                            @Override
                            public void OnClick_Lisiner(int position) {

                                vibrator(getApplicationContext());

                                BookMarkShow   item =markShowList.get(position);
                                double latitude= Double.parseDouble(item.getLatitude());
                                double longtude= Double.parseDouble(item.getLongtude());

                                LatLng latLng_drag  =new LatLng(latitude,longtude);

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

                                OpenAlert();
                            }
                        });

                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(BookMarkActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue= Volley.newRequestQueue(BookMarkActivity.this);
        requestQueue.add(stringRequest);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static void vibrator(Context context) {
        Vibrator v = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            v.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE));
        } else {
            v.vibrate(50);
        }
    }

    private void OpenAlert(){

        bottomSheetAlam = new BottomSheetDialog(BookMarkActivity.this);
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

                        ArrayAdapter<String> arrayAdapter_alert=new ArrayAdapter<String>(BookMarkActivity.this, android.R.layout.simple_list_item_1,arrayList_alertType);
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
                        Toast.makeText(BookMarkActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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


        RequestQueue requestQueue= Volley.newRequestQueue(BookMarkActivity.this);
        requestQueue.add(stringRequest);


        curve = view1.findViewById(R.id.curve_alarm);
        taplayout = view1.findViewById(R.id.layouttap);
        final CircleAnimation circleAnimation = new CircleAnimation(curve, 127.0f);
        this.curve.setCurveColor(ContextCompat.getColor(BookMarkActivity.this, R.color.Fuchsia), ContextCompat.getColor(BookMarkActivity.this, R.color.tap_dark));
        circleAnimation.setDuration(2000);
        circleAnimation.setAnimationListener(this);

        taplayout.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (!checkBox.isChecked()) {
                    Toast.makeText(BookMarkActivity.this, "Select Terms Conditions", Toast.LENGTH_SHORT).show();
                    checkBox.setError("Terms Conditions!!");

                }else if(spinner_alertType.getSelectedItem().toString().contains("Select--Alert--Type--")){

                    Toast.makeText(BookMarkActivity.this, "Please Your Alet Type", Toast.LENGTH_SHORT).show();

                }  else{


                    switch (motionEvent.getAction()) {
                        case 0:
                            curve.startAnimation(circleAnimation);
                            cancel = false;


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


    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

        if (cancel){

            Log.d("anim","Incomplete");

        }else {

            sendAlet(alertTypeName);
            bottomSheetAlam.dismiss();
        }
    }



    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    private void sendAlet(String alertTypeName){

        String  accessTocken= sharedPreferences_type.getString("access_token","access_token found");

        String  user_Address= sharedPreferences_type.getString("user_Address","user_Address found");
        String  latitude_storage= sharedPreferences_type.getString("latitude","user_Address found");
        String  longitude_storage= sharedPreferences_type.getString("longitude","user_Address found");

        String  storage_details=  user_Address+""+latitude_storage+""+longitude_storage;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, send_alert_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    if(jsonObjectMain.getString("message").contains("Alert Send Successfully!")) {

                        MaterialDialog mDialog = new MaterialDialog.Builder(BookMarkActivity.this)
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

                        Toast.makeText(BookMarkActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                    }else {

                        Toast.makeText(BookMarkActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

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

        RequestQueue requestQueue= Volley.newRequestQueue(BookMarkActivity.this);
        requestQueue.add(stringRequest);
    }


    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(BookMarkActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(BookMarkActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(BookMarkActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(BookMarkActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {

            Toast.makeText(BookMarkActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(BookMarkActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();
    }

}
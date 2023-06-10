package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private TextView textView_login_title,textView_login_signUp,textView_forgot;

    private Typeface typeface_login_signUp,typeface_subtitle,typeface1;

    private EditText editText_phone,editText_password;

    private SharedPreferences sharedPreferences_type;

    private String otp_resend = Main_Url.ROOT_URL + "api/resendotp";

    private String Login_url = Main_Url.ROOT_URL + "api/login";

    private String Devise_token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        editText_password=(EditText)findViewById(R.id.edit_userPasswrod);

        editText_phone=(EditText)findViewById(R.id.edit_usernumber);

        textView_forgot=(TextView)findViewById(R.id.txt_loging_forgotPass);
        textView_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this,ForgotPassword.class);
                startActivity(intent);
            }
        });


        textView_login_signUp=(TextView)findViewById(R.id.text_login_singUp);
        textView_login_signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
           //   Animatoo.animateSlideLeft(LoginActivity.this);
            }
        });

        typeface1= Typeface.createFromAsset(getAssets(),"fonts/Italic.ttf");
        textView_login_signUp.setTypeface(typeface1);

        turnGPSOn();

    }

    @Override
    protected void onStart() {

        language_model language=new language_model(this);
        language.loadLanguage();
        super.onStart();
    }

    public void Login_home(View view) {


        if(editText_phone.getText().toString().isEmpty()){

            editText_phone.setError("Please Your Phone !!");
            editText_phone.requestFocus();

        } else  if(editText_password.getText().toString().isEmpty()){

            editText_password.setError("Please Your Password !!");
            editText_password.requestFocus();

        }else  if(editText_password.getText().toString().trim().length()<8){

            editText_password.setError("Please Your Password 8 Character!!");
            editText_password.requestFocus();
        }else {

            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.show();
            progressDialog.setContentView(R.layout.custom_progress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {

                            if (!task.isSuccessful()) {
                                Toast.makeText(LoginActivity.this, "Fetching FCM registration token failed", Toast.LENGTH_SHORT).show();
                                return;
                            }

                            Devise_token = task.getResult();
                            // Log and toast
                            System.out.println("token:"+Devise_token);

                           /// Toast.makeText(LoginActivity.this, "Devise_token:"+Devise_token, Toast.LENGTH_SHORT).show();
                        }
                    });

           String phone=editText_phone.getText().toString().trim();
            String pass=editText_password.getText().toString().trim();

            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, Login_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObjectMain=new JSONObject(response);

                        if(jsonObjectMain.getString("message").contains("SuccessFully To Login User!")){

                            Toast.makeText(LoginActivity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                            sharedPreferences_type.edit().putString("phone",phone).commit();
                            sharedPreferences_type.edit().putString("access_token",jsonObjectMain.getString("access_token")).commit();
                            sharedPreferences_type.edit().putString("user_type",jsonObjectMain.getString("user_type")).commit();

                            if(jsonObjectMain.getString("user_type").contains("2")){

                                Intent intent=new Intent(LoginActivity.this,CompanyMapsActivity.class);
                                startActivity(intent);
                                finish();

                            }if(jsonObjectMain.getString("user_type").contains("3")){

                                Intent intent=new Intent(LoginActivity.this,UserMapsActivity.class);
                                startActivity(intent);
                                finish();

                            } if(jsonObjectMain.getString("user_type").contains("1")){

                                Intent intent=new Intent(LoginActivity.this,FireMapActivity.class);
                                startActivity(intent);
                                finish();
                            }

                           }else if(jsonObjectMain.getString("message").contains("Sry Your Account is Not Varifyed!")){

                            Toast.makeText(LoginActivity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                            ResendOtp();
                            sharedPreferences_type.edit().putString("phone",phone).commit();
                            Intent intent=new Intent(LoginActivity.this,LoginOtpVerify.class);
                            startActivity(intent);

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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
                    hashMap.put("mobile_no",phone);
                    hashMap.put("password",pass);
                    hashMap.put("device_token",Devise_token);
                    return hashMap;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
            requestQueue.add(stringRequest1);
        }
    }

    private void ResendOtp() {

        String phone=sharedPreferences_type.getString("phone","null phone");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, otp_resend, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);


                    if(jsonObjectMain.getString("message").contains("SuccessFully Re Send Otp!")){

                        Toast.makeText(LoginActivity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                        // sharedPreferences_type.edit().putString("phone",jsonObjectMain.getString("mobile_no")).commit();
                        Notification_Add(jsonObjectMain.getString("message"),"Resend OTp Send");

                    }else{

                        Toast.makeText(LoginActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("mobile_no",phone);
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(LoginActivity.this);
        requestQueue.add(stringRequest);
    }

    private void Notification_Add(String message,String title){

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){

            NotificationChannel notificationChannel=new NotificationChannel("Channel","Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager=(NotificationManager)getApplicationContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notification= new NotificationCompat.Builder(getApplicationContext(),"Channel")
                .setContentTitle(title)
                .setSmallIcon(R.drawable.notification_prog)
                .setContentText(message)
                .setAutoCancel(true)
                //.setSound(Uri.)
                .setWhen(System.currentTimeMillis());

        NotificationManagerCompat notificationManagerCompat=NotificationManagerCompat.from(getApplicationContext());
        notificationManagerCompat.notify(1,notification.build());

    }


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

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(LoginActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(LoginActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(LoginActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(LoginActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(LoginActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(LoginActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }

    private void turnGPSOn(){

        try {

            int off = Settings.Secure.getInt(getContentResolver(), Settings.Secure.LOCATION_MODE);
            if(off==0){
                showSettingAlert();

            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

    }

    public void showSettingAlert()
    {
        android.app.AlertDialog.Builder alertDialog = new  AlertDialog.Builder(LoginActivity.this);
        alertDialog.setTitle("GPS setting!");
        alertDialog.setMessage("GPS is not enabled, Do you want to go to settings menu? ");
        alertDialog.setPositiveButton("Setting", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(onGPS);


            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
}
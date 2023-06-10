package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Otp_Activity extends AppCompatActivity {

    private EditText editText_cod1,editText_cod2,editText_cod3,editText_cod4,editText_cod5,editText_cod6;

    private SharedPreferences sharedPreferences_type;
    private String message,Devise_token;

    private String otp_url = Main_Url.ROOT_URL + "api/otpverify";

    private String otp_resend = Main_Url.ROOT_URL + "api/resendotp";

    private CountDownTimer Count;

    private TextView timer_score,text_otpResend,textView_userPhone;

    private Button viefiy_btn;

    private int count_time=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        timer_score=(TextView)findViewById(R.id.text_otpResendTime);
        text_otpResend=(TextView)findViewById(R.id.text_otpResend);

        editText_cod1=(EditText)findViewById(R.id.edit_cod1);
        editText_cod2=(EditText)findViewById(R.id.edit_cod2);
        editText_cod3=(EditText)findViewById(R.id.edit_cod3);
        editText_cod4=(EditText)findViewById(R.id.edit_cod4);
        editText_cod5=(EditText)findViewById(R.id.edit_cod5);
        editText_cod6=(EditText)findViewById(R.id.edit_cod6);

        viefiy_btn=(Button)findViewById(R.id.viefiy_btn);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            // Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        // Get new FCM registration token
                        Devise_token = task.getResult();

                    }
                });


        editText_cod1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if(!charSequence.toString().trim().isEmpty()){
                    editText_cod2.requestFocus();
                }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_cod2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    editText_cod3.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_cod3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    editText_cod4.requestFocus();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_cod4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    editText_cod5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_cod5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().isEmpty()){
                    editText_cod6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText_cod6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence sequence, int start, int before, int count) {

                if(!sequence.toString().isEmpty()){

                    StringBuffer stringBuffer=new StringBuffer();


                    if(editText_cod1.getText().toString().isEmpty()){
                        editText_cod1.setError("Please OTP First Number.!!");
                        editText_cod1.requestFocus();
                    }
                    else if(editText_cod2.getText().toString().isEmpty()){
                        editText_cod2.setError("Please OTP Second Number");
                        editText_cod2.requestFocus();

                    } else if(editText_cod3.getText().toString().isEmpty()){
                        editText_cod3.setError("Please OTP Third Number");
                        editText_cod3.requestFocus();

                    }else if(editText_cod4.getText().toString().isEmpty()){
                        editText_cod4.setError("Please OTP Fourth Number");
                        editText_cod4.requestFocus();

                    }else if(editText_cod5.getText().toString().isEmpty()){
                        editText_cod5.setError("Please OTP Fifth Number");
                        editText_cod5.requestFocus();
                    } else if(editText_cod6.getText().toString().isEmpty()){
                        editText_cod6.setError("Please OTP Sixth Number");
                        editText_cod6.requestFocus();
                    }else{


                        String cod1=editText_cod1.getText().toString().trim();
                        String cod2=editText_cod2.getText().toString().trim();
                        String cod3=editText_cod3.getText().toString().trim();
                        String cod4=editText_cod4.getText().toString().trim();
                        String cod5=editText_cod5.getText().toString().trim();
                        String cod6=editText_cod6.getText().toString().trim();

                        stringBuffer.append(cod1);
                        stringBuffer.append(cod2);
                        stringBuffer.append(cod3);
                        stringBuffer.append(cod4);
                        stringBuffer.append(cod5);
                        stringBuffer.append(cod6);

                        final ProgressDialog progressDialog = new ProgressDialog(Otp_Activity.this);

                        progressDialog.show();
                        progressDialog.setContentView(R.layout.custom_progress);
                        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

                        String phone= sharedPreferences_type.getString("phone","null phone");


                        StringRequest stringRequest = new StringRequest(Request.Method.POST, otp_url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                try {
                                    JSONObject jsonObjectMain=new JSONObject(response);

                                    message= jsonObjectMain.getString("message");

                                    if(message.contains("SuccessFully To Verify & Login User!")){

                               Toast.makeText(Otp_Activity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                                        progressDialog.dismiss();
                                        sharedPreferences_type.edit().putString("access_token",jsonObjectMain.getString("access_token")).commit();
                                        sharedPreferences_type.edit().putString("user_type",jsonObjectMain.getString("user_type")).commit();

                                        progressDialog.dismiss();

                                        if(jsonObjectMain.getString("user_type").contains("2")){

                                            Intent intent=new Intent(Otp_Activity.this,CompanyMapsActivity.class);
                                            startActivity(intent);
                                            finish();

                                        }if(jsonObjectMain.getString("user_type").contains("3")){

                                            Intent intent=new Intent(Otp_Activity.this,UserMapsActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                        Notification_Add(message,"SuccessFully Verify");

                                         editText_cod1.setText("");
                                         editText_cod2.setText("");
                                         editText_cod3.setText("");
                                         editText_cod4.setText("");
                                         editText_cod5.setText("");
                                         editText_cod6.setText("");
                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(Otp_Activity.this, message, Toast.LENGTH_SHORT).show();
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
                                hashMap.put("otp",stringBuffer.toString());
                                hashMap.put("device_token",Devise_token);

                                return hashMap;
                            }
                        };

                        RequestQueue requestQueue= Volley.newRequestQueue(Otp_Activity.this);
                        requestQueue.add(stringRequest);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        textView_userPhone=(TextView)findViewById(R.id.text_userPhone);

        text_otpResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                timer_score.setVisibility(View.VISIBLE);

                 if(count_time!=5){
                    firstTimer();
                }else{
                    Toast.makeText(Otp_Activity.this, "Sorrey Resend Time Over!!"+count_time, Toast.LENGTH_SHORT).show();
                }

            }
        });

      String userPhone=sharedPreferences_type.getString("phone","null phone");

        if(userPhone.length()>3){
            String phon_last=  userPhone.substring(userPhone.length()-3);
            String fristNumber= userPhone.substring(0,3);

            textView_userPhone.setText("+88:\t"+fristNumber+"xxxxxx"+phon_last);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        language_model language=new language_model(this);
        language.loadLanguage();
    }


    private void firstTimer() {

      //long duration = TimeUnit.MILLISECONDS.toMinutes(20000);

        Count = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                timer_score.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));

               // viefiy_btn.setEnabled(false);
                 text_otpResend.setEnabled(false);
            }

            @Override
            public void onFinish() {

                 count_time=count_time+2;
                 if(count_time>5){
                     count_time--;
                 }
                timer_score.setText(count_time+" min");
                text_otpResend.setEnabled(true);

               ResendOtp();
            }
        }.start();
    }

    void ResendOtp(){

        ProgressDialog progressDialog = new ProgressDialog(Otp_Activity.this);
        progressDialog.setCancelable(false);
        progressDialog.show();
        String phone=sharedPreferences_type.getString("phone","null phone");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, otp_resend, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    message= jsonObjectMain.getString("message");

                    if(message.contains("SuccessFully Re Send Otp!")){

                        Toast.makeText(Otp_Activity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                       // sharedPreferences_type.edit().putString("phone",jsonObjectMain.getString("mobile_no")).commit();
                        Notification_Add(message,"Resend OTp");

                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(Otp_Activity.this, message, Toast.LENGTH_SHORT).show();
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
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Otp_Activity.this);
        requestQueue.add(stringRequest);
    }


    public void Verify_btn(View view) {

         if(editText_cod1.getText().toString().isEmpty()){
            editText_cod1.setError("Please OTP First Number.!!");
            editText_cod1.requestFocus();
        }
        else if(editText_cod2.getText().toString().isEmpty()){
            editText_cod2.setError("Please OTP Second Number");
            editText_cod2.requestFocus();

        } else if(editText_cod3.getText().toString().isEmpty()){
            editText_cod3.setError("Please OTP Third Number");
            editText_cod3.requestFocus();

        }else if(editText_cod4.getText().toString().isEmpty()){
            editText_cod4.setError("Please OTP Fourth Number");
            editText_cod4.requestFocus();

        }else if(editText_cod5.getText().toString().isEmpty()){
            editText_cod5.setError("Please OTP Fifth Number");
            editText_cod5.requestFocus();
        } else if(editText_cod6.getText().toString().isEmpty()){
            editText_cod6.setError("Please OTP Sixth Number");
            editText_cod6.requestFocus();
        }else {

             StringBuffer stringBuffer=new StringBuffer();

             String cod1=editText_cod1.getText().toString().trim();
             String cod2=editText_cod2.getText().toString().trim();
             String cod3=editText_cod3.getText().toString().trim();
             String cod4=editText_cod4.getText().toString().trim();
             String cod5=editText_cod5.getText().toString().trim();
             String cod6=editText_cod6.getText().toString().trim();

             stringBuffer.append(cod1);
             stringBuffer.append(cod2);
             stringBuffer.append(cod3);
             stringBuffer.append(cod4);
             stringBuffer.append(cod5);
             stringBuffer.append(cod6);

             final ProgressDialog progressDialog = new ProgressDialog(Otp_Activity.this);

             progressDialog.show();
             progressDialog.setContentView(R.layout.custom_progress);
             progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


             String phone=sharedPreferences_type.getString("phone","null phone");


             StringRequest stringRequest1 = new StringRequest(Request.Method.POST, otp_url, new Response.Listener<String>() {
                 @Override
                 public void onResponse(String response) {

                     try {
                         JSONObject jsonObjectMain=new JSONObject(response);

                        //  message= jsonObjectMain.getString("message");

                         if(jsonObjectMain.getString("message").contains("SuccessFully To Verify & Login User!")){

                             Toast.makeText(Otp_Activity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                             progressDialog.dismiss();
                             sharedPreferences_type.edit().putString("access_token",jsonObjectMain.getString("access_token")).commit();
                             sharedPreferences_type.edit().putString("user_type",jsonObjectMain.getString("user_type")).commit();

                             progressDialog.dismiss();

                             if(jsonObjectMain.getString("user_type").contains("2")){

                                 Intent intent=new Intent(Otp_Activity.this,CompanyMapsActivity.class);
                                 startActivity(intent);
                                 finish();
                             }if(jsonObjectMain.getString("user_type").contains("3")){

                                 Intent intent=new Intent(Otp_Activity.this,UserMapsActivity.class);
                                 startActivity(intent);
                                 finish();
                             }

                             Notification_Add(jsonObjectMain.getString("message"),"SuccessFully OTp");

                             editText_cod1.setText("");
                             editText_cod2.setText("");
                             editText_cod3.setText("");
                             editText_cod4.setText("");
                             editText_cod5.setText("");
                             editText_cod6.setText("");
                         }else{
                             progressDialog.dismiss();
                             Toast.makeText(Otp_Activity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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
                     hashMap.put("otp",stringBuffer.toString());
                     hashMap.put("device_token",Devise_token);

                     return hashMap;
                 }
             };

             RequestQueue requestQueue= Volley.newRequestQueue(Otp_Activity.this);
             requestQueue.add(stringRequest1);
        }
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

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(Otp_Activity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(Otp_Activity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(Otp_Activity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(Otp_Activity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(Otp_Activity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(Otp_Activity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }

}
package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
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
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotOtpMatch extends AppCompatActivity {

    private EditText editText_cod1,editText_cod2,editText_cod3,editText_cod4,editText_cod5,editText_cod6;

    private SharedPreferences sharedPreferences_type;
    private String message;

    private String otp_url = Main_Url.ROOT_URL + "api/forgot-password-otp-match";

    private TextView textView_otp;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_otp_match);

        toolbar = (Toolbar) findViewById(R.id.toolbar_forOtpMatch);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);
        textView_otp=(TextView)findViewById(R.id.text_forgetPhone);
        String phone= sharedPreferences_type.getString("phone","null phone");

         if(!phone.contains("null phone")){
             if(phone.length()>3){
                 String phon_last=  phone.substring(phone.length()-3);
                 String fristNumber= phone.substring(0,3);

                 textView_otp.setText("+88:\t"+fristNumber+"xxxxxx"+phon_last);
             }
         }

        editText_cod1=(EditText)findViewById(R.id.edit_forgotcod1);
        editText_cod2=(EditText)findViewById(R.id.edit_forgotcod2);
        editText_cod3=(EditText)findViewById(R.id.edit_forgotcod3);
        editText_cod4=(EditText)findViewById(R.id.edit_forgotcod4);
        editText_cod5=(EditText)findViewById(R.id.edit_forgotcod5);
        editText_cod6=(EditText)findViewById(R.id.edit_forgotcod6);

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

                        final ProgressDialog progressDialog = new ProgressDialog(ForgotOtpMatch.this);

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

                                    if(message.contains("SuccessFully To Match Forget OTp!")){

                                        Toast.makeText(ForgotOtpMatch.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                                          progressDialog.dismiss();

                                          Notification_Add(message,"Forget OTp");

                                        Intent intent=new Intent(ForgotOtpMatch.this,ForgotNewpassword.class);
                                        startActivity(intent);
                                        finish();

                                        editText_cod1.setText("");
                                        editText_cod2.setText("");
                                        editText_cod3.setText("");
                                        editText_cod4.setText("");
                                        editText_cod5.setText("");
                                        editText_cod6.setText("");

                                    }else{
                                        progressDialog.dismiss();
                                        Toast.makeText(ForgotOtpMatch.this, message, Toast.LENGTH_SHORT).show();
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
                                hashMap.put("mobile_no",phone);
                                hashMap.put("otp",stringBuffer.toString());

                                return hashMap;
                            }
                        };

                        RequestQueue requestQueue= Volley.newRequestQueue(ForgotOtpMatch.this);
                        requestQueue.add(stringRequest);
                    }

                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        language_model language=new language_model(this);
        language.loadLanguage();
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
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
            Toast.makeText(ForgotOtpMatch.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(ForgotOtpMatch.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(ForgotOtpMatch.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(ForgotOtpMatch.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(ForgotOtpMatch.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(ForgotOtpMatch.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }

    public void VerifyForgot_btn(View view) {
    }
}
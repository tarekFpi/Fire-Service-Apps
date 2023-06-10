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
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class ForgotPassword extends AppCompatActivity {

  private EditText editText_phone;

    private String forgot_url = Main_Url.ROOT_URL + "api/forgot-password";

    private SharedPreferences sharedPreferences_type;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        toolbar = (Toolbar) findViewById(R.id.toolbar_forgotpass);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        editText_phone=(EditText)findViewById(R.id.edit_forgotPass);

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

    public void forgot_btn(View view) {
        ForgotPasswrod();
    }

    private  void ForgotPasswrod(){

        if(editText_phone.getText().toString().isEmpty()){
            editText_phone.setError("Phone Number is Empty!!");
            editText_phone.requestFocus();

        }else{


           String phoneNumber=editText_phone.getText().toString().trim();

            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.show();
            progressDialog.setContentView(R.layout.custom_progress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);



            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, forgot_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObjectMain=new JSONObject(response);

                        if(jsonObjectMain.getString("message").contains("SuccessFully Send Otp!")){

                            Toast.makeText(ForgotPassword.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                           sharedPreferences_type.edit().putString("phone",jsonObjectMain.getString("mobile_no")).commit();

                            Intent intent=new Intent(ForgotPassword.this,ForgotOtpMatch.class);
                            startActivity(intent);
                            finish();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(ForgotPassword.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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
                    hashMap.put("mobile_no",phoneNumber);
                    return hashMap;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(ForgotPassword.this);
            requestQueue.add(stringRequest1);
        }

    }


    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(ForgotPassword.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(ForgotPassword.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(ForgotPassword.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(ForgotPassword.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(ForgotPassword.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(ForgotPassword.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }
}
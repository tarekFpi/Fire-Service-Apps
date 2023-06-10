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

public class ForgotNewpassword extends AppCompatActivity {

    private SharedPreferences sharedPreferences_type;

    private EditText editText_oldpass,editText_newPass,editText_Comfirm;

    private String forgotPass_url = Main_Url.ROOT_URL + "api/set-new-password";

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_newpassword);

        toolbar = (Toolbar) findViewById(R.id.toolbar_forgornewPass);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        editText_newPass=(EditText)findViewById(R.id.edit_fnewpass);

        editText_Comfirm=(EditText)findViewById(R.id.edit_fCompass);

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

    public void forgetnewPass(View view) {

        if(editText_newPass.getText().toString().isEmpty()){
            editText_newPass.setError("New Passowrd is Empty!!");
            editText_newPass.requestFocus();

        }else if(editText_Comfirm.getText().toString().isEmpty()){
            editText_Comfirm.setError("Confirm Passowrd is Empty!!");
            editText_Comfirm.requestFocus();
        }else{

            String phone= sharedPreferences_type.getString("phone","null phone");

            String Newpass=  editText_newPass.getText().toString().trim();
            String Conpass= editText_Comfirm.getText().toString().trim();


            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.show();
            progressDialog.setContentView(R.layout.custom_progress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);



            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, forgotPass_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObjectMain=new JSONObject(response);

                        if(jsonObjectMain.getString("message").contains("Password updated successfully")){

                            Toast.makeText(ForgotNewpassword.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                            Notification_Add(jsonObjectMain.getString("message"),"Chagne Password");

                            Intent intent=new Intent(ForgotNewpassword.this,LoginActivity.class);
                            startActivity(intent);
                            finish();

                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(ForgotNewpassword.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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
                    hashMap.put("password",Newpass);
                    hashMap.put("password_confirmation",Conpass);

                    return hashMap;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(ForgotNewpassword.this);
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
            Toast.makeText(ForgotNewpassword.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(ForgotNewpassword.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(ForgotNewpassword.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(ForgotNewpassword.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(ForgotNewpassword.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(ForgotNewpassword.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }
}
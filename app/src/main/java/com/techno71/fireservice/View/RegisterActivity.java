package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextView textView_singUp_login, textView_title;

    private Typeface typeface_signUp_login, typeface_title;

    private Toolbar toolbar;

    private String register_url = Main_Url.ROOT_URL + "api/register";

    private String message = "", deviceId;

    private EditText editText_userName, editText_phone,
            editText_pass, editText_Compass,editText_email;
    private static final int REQUEST_CODE = 101;

    private int item_position,userType;

    private Spinner spinner_userType;

    private SharedPreferences sharedPreferences_type;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        toolbar = (Toolbar) findViewById(R.id.toolbar_userSingUp);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);


        editText_userName=(EditText)findViewById(R.id.edit_regiUserName);
         editText_phone=(EditText)findViewById(R.id.edit_regiUserPhone);
         editText_pass=(EditText)findViewById(R.id.edit_regiUserPass);
         editText_Compass=(EditText)findViewById(R.id.edit_regiUserCompass);


        textView_singUp_login = (TextView) findViewById(R.id.text_singUp_login);
        textView_singUp_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                //   Animatoo.animateSlideDown(RegisterActivity.this);
            }
        });

        typeface_signUp_login = Typeface.createFromAsset(getAssets(), "fonts/regular.ttf");
        textView_singUp_login.setTypeface(typeface_signUp_login);

        spinner_userType=(Spinner)findViewById(R.id.userType_spinner);

        spinner_userType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                item_position = Integer.parseInt(String.valueOf(position));

                if (item_position !=0){
                    if (item_position == 1){
                        userType=3;  //user
                    }if (item_position == 2){
                        userType=2; //company
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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


    public void Registation(View view) {


        String userName= editText_userName.getText().toString().trim();

        String phone= editText_phone.getText().toString().trim();

        String ComfPassword= editText_pass.getText().toString().trim();

        String password= editText_Compass.getText().toString().trim();


        if(userType==0) {

           Toast.makeText(this, "Select User Type!!", Toast.LENGTH_SHORT).show();
           spinner_userType.requestFocus();

        }else if(TextUtils.isEmpty(userName)) {
            editText_userName.setError("Please Your User Name !!");
            editText_userName.requestFocus();
        }
        else if(TextUtils.isEmpty(phone)){
            editText_phone.setError("Please Your Phone Number !!");
            editText_phone.requestFocus();

        }else if(TextUtils.isEmpty(password)){
            editText_pass.setError("Please Your Password !!");
            editText_pass.requestFocus();

        }  else if(TextUtils.isEmpty(ComfPassword)){
            editText_Compass.setError("Please Your Confrim Password!!");
            editText_Compass.requestFocus();

        } else{

            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.show();
            progressDialog.setContentView(R.layout.custom_progress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


            String deviceUniqueIdentifier = Settings.Secure.getString(getApplicationContext().getApplicationContext()
                    .getContentResolver(), Settings.Secure.ANDROID_ID);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, register_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObjectMain=new JSONObject(response);

                        message= jsonObjectMain.getString("message");

                        if(message.contains("SuccessFully Send Otp!")){

                            Toast.makeText(RegisterActivity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                            progressDialog.dismiss();
                            editText_Compass.setText("");
                            editText_phone.setText("");
                            editText_pass.setText("");
                            editText_userName.setText("");
                            userType=0;

                            sharedPreferences_type.edit().putString("phone",jsonObjectMain.getString("mobile_no")).commit();
                            Notification_Add(message);

                            Intent intent=new Intent(RegisterActivity.this,Otp_Activity.class);
                            startActivity(intent);

                        }else{

                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_SHORT).show();
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
                    hashMap.put("user_name",userName);
                    hashMap.put("user_type",String.valueOf(userType));
                    hashMap.put("mobile_no",phone);
                    hashMap.put("password",password);
                    hashMap.put("password_confirmation",ComfPassword);
                    hashMap.put("mac_address",deviceUniqueIdentifier);

                    return hashMap;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(RegisterActivity.this);
            requestQueue.add(stringRequest);
        }
    }

    private void Notification_Add(String message){

        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){

            NotificationChannel notificationChannel=new NotificationChannel("Channel","Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager=(NotificationManager)getApplicationContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notification= new NotificationCompat.Builder(getApplicationContext(),"Channel")
                .setContentTitle("Register")
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
            Toast.makeText(RegisterActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(RegisterActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(RegisterActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(RegisterActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(RegisterActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(RegisterActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();
    }
}
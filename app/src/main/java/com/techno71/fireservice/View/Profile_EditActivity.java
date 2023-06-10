package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile_EditActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private String edit_url = Main_Url.ROOT_URL + "api/update-profile-image";

    private Button button;

    private Bitmap bitmap;

    private String encodeImageString="";

    private String user_infor = Main_Url.ROOT_URL +"api/get-user-info";

    private CircleImageView circleImageView;

    private TextView textView_name;

    private SharedPreferences sharedPreferences_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar_editprofile);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        button=(Button)findViewById(R.id.btn_updateImage);

        circleImageView=(CircleImageView) findViewById(R.id.user_EditprofileImage);

        textView_name=(TextView)findViewById(R.id.textProfilEditFullName);

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                file_Choose();
            }
        });

        ShowImage();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(encodeImageString.equals("")){

             Toast.makeText(Profile_EditActivity.this, "please Image Select!!", Toast.LENGTH_LONG).show();

                }else{

                    UpdateImage();
                }

            }
        });
    }


  private void ShowImage(){

      final ProgressDialog progressDialog = new ProgressDialog(Profile_EditActivity.this);

      progressDialog.show();
      progressDialog.setContentView(R.layout.custom_progress);
      progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

      String  accessTocken = sharedPreferences_type.getString("access_token","access_token found");

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

                          textView_name.setText(""+jsonObject.getString("user_name"));
                          String image="https://fifaar.com/public/"+jsonObject.getString("user_picture");

                          Picasso.get().load(image).into(circleImageView);
                       }

                  } else{

                      progressDialog.dismiss();
                      Toast.makeText(Profile_EditActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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

      RequestQueue requestQueue= Volley.newRequestQueue(Profile_EditActivity.this);
      requestQueue.add(stringRequest);

  }

    private void UpdateImage() {

        String  accessTocken = sharedPreferences_type.getString("access_token","access_token found");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, edit_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);


                    if(jsonObjectMain.getString("message").contains("Successfully Update Profile Image")){

                   Toast.makeText(Profile_EditActivity.this,jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                    }else{

                    Toast.makeText(Profile_EditActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("image",encodeImageString);
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(Profile_EditActivity.this);
        requestQueue.add(stringRequest);
    }


    private void file_Choose() {

        Dexter.withActivity(Profile_EditActivity.this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {

                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent, "Browse Image"), 1);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            Uri filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);

                bitmap = BitmapFactory.decodeStream(inputStream);

                Bitmap reducebitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, true);


                circleImageView.setImageBitmap(reducebitmap);
                encodeBitmapImage(reducebitmap);
            } catch (Exception ex) {
                Toast.makeText(this, ""+ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }


    private void encodeBitmapImage(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();

        encodeImageString = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
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

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(Profile_EditActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(Profile_EditActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(Profile_EditActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(Profile_EditActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(Profile_EditActivity.this, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(Profile_EditActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }

}
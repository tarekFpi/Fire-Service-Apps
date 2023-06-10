package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.squareup.picasso.Picasso;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.Districts_model;
import com.techno71.fireservice.Model.Divisoin_model;
import com.techno71.fireservice.R;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompanyVerify_Activity extends AppCompatActivity {

    private Spinner spinner_division,spinner_distric,spinner_thana;

    private String message="",getdivisoin_id,getdistricts_id, accessTocken,
            divisionName,districtsName,thanaName;

    private ArrayList<String> arrayList_divisionName = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_divisionName;

    private ArrayList<String> arrayList_districtsName = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_districtsName;

    private ArrayList<String> arrayList_thanasName = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_thanasName;

    private List<Divisoin_model> divisoin_id=new ArrayList<>();

    private List<Districts_model> districts_id=new ArrayList<>();

    private String divisions_url = Main_Url.ROOT_URL +"api/divisions-get";

    private String districts_url = Main_Url.ROOT_URL +"api/districts-get";

    private String thanas_url = Main_Url.ROOT_URL +"api/thanas-get";

    private String Verify_informatin = Main_Url.ROOT_URL +"api/company-verify";

    private CardView cardView_fontSide,cardView_backSide;

    private SharedPreferences sharedPreferences_type;

    private ImageView imageView_fontSide,imageView_image_backSide;

    private EditText editText_companyName,editText_ownerName,editText_address;

    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    String cameraPermission[];
    String storagePermission[];
    Uri imageuri;

    private String encodeImageFonts="",encodeImageBack="";

    private int Status=0;

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_verify);

        toolbar = (Toolbar) findViewById(R.id.toolbar_companyVerifie);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);


        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        accessTocken = sharedPreferences_type.getString("access_token","access_token found");

        spinner_division=(Spinner)findViewById(R.id.division_spinner);
        spinner_distric=(Spinner)findViewById(R.id.distric_spinner);
        spinner_thana=(Spinner)findViewById(R.id.thana_spinner);

        cardView_fontSide=(CardView)findViewById(R.id.cardview_nidfontSide);
        cardView_backSide=(CardView)findViewById(R.id.cardview_nidBack);

        imageView_fontSide=(ImageView)findViewById(R.id.company_fontSide);

        imageView_image_backSide=(ImageView)findViewById(R.id.image_backSide);

        editText_companyName=(EditText)findViewById(R.id.edit_companyVerifyName);
        editText_ownerName=(EditText)findViewById(R.id.edit_VerifycompanyOwner);
        editText_address=(EditText)findViewById(R.id.edit_VerifyAddress);

      cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
       storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        cardView_fontSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Status=1;
                showImagePicDialog();

            }
        });

        cardView_backSide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Status=2;
                showImagePicDialog();

            }
        });

        getDivision();
    }

    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromGallery();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // Requesting  gallery permission
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // checking camera permissions
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // Requesting camera permission
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    // Requesting camera and gallery
    // permission if not given
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    // Here we will pick image from gallery or camera
    private void pickFromGallery() {
        CropImage.activity().start(CompanyVerify_Activity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    InputStream inputStream = getContentResolver().openInputStream(resultUri);

                   Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                    Bitmap reducebitmap = Bitmap.createScaledBitmap(bitmap, 400, 400, true);

                    //Picasso.with(this).load(resultUri).into(userpic);
                    if(Status==1){
                     //   Picasso.get().load(resultUri).into(imageView_fontSide);
                        imageView_fontSide.setImageBitmap(reducebitmap);
                        encodeBitmapImage_fonts(reducebitmap);

                    }if(Status==2){
                     //   Picasso.get().load(resultUri).into(imageView_image_backSide);
                        imageView_image_backSide.setImageBitmap(reducebitmap);
                        encodeBitmapImage_back(reducebitmap);
                    }

                } catch (Exception ex) {
                    Toast.makeText(this, ""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                }


            }
        }
    }

    private void  encodeBitmapImage_fonts(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,100,  byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();

        encodeImageFonts = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    private void encodeBitmapImage_back(Bitmap bitmap) {

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();

        encodeImageBack = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);
    }


    private void getDivision(){

     final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        StringRequest stringRequest = new StringRequest(Request.Method.POST, divisions_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    message= jsonObjectMain.getString("message");

                    arrayList_divisionName.clear();
                    divisoin_id.clear();
                    arrayList_divisionName.add("Select--Division--");

                    if(message.contains("Data is available!")){

                        JSONArray jsonArray= jsonObjectMain.getJSONArray("divisions");

                        for (int i = 0; i <jsonArray.length() ;i++) {

                      JSONObject jsonObject=jsonArray.getJSONObject(i);

                   Divisoin_model addItem=new Divisoin_model(jsonObject.getString("id"));
                   divisoin_id.add(addItem);

                    arrayList_divisionName.add(jsonObject.getString("bn_name"));
                        }

                        arrayAdapter_divisionName=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList_divisionName);
                        spinner_division.setAdapter(arrayAdapter_divisionName);
                        progressDialog.dismiss();

                        spinner_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                 divisionName= arrayList_divisionName.get(position).toString();

                               if(position >=1){
                                  getdivisoin_id= divisoin_id.get(position-1).getId();
                                 Get_DistrictName(getdivisoin_id);
                 //   Toast.makeText(CompanyVerify_Activity.this, "getdivisoin_id:"+getdivisoin_id, Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(CompanyVerify_Activity.this, message, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue= Volley.newRequestQueue(CompanyVerify_Activity.this);
        requestQueue.add(stringRequest);

    }

    private void Get_DistrictName(String getdivisoin_id) {

        arrayList_districtsName.clear();
        districts_id.clear();
        arrayList_districtsName.add("Select--Districts--");

        StringRequest stringRequest2 = new StringRequest(Request.Method.POST, districts_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain2=new JSONObject(response);


                    if(jsonObjectMain2.getString("message").contains("Data is available!")){

                        JSONArray jsonArray2= jsonObjectMain2.getJSONArray("districts");

                        for (int i = 0; i <jsonArray2.length() ;i++) {

                            JSONObject jsonObject2=jsonArray2.getJSONObject(i);

                            Districts_model addItem=new Districts_model(jsonObject2.getString("id"));
                            districts_id.add(addItem);

                            arrayList_districtsName.add(jsonObject2.getString("bn_name"));
                        }

                        arrayAdapter_districtsName=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList_districtsName);
                        spinner_distric.setAdapter(arrayAdapter_districtsName);


                        spinner_distric.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                              districtsName= arrayList_districtsName.get(position).toString();

                                if(position >=1){

                                    arrayList_thanasName.clear();
                                    getdistricts_id=districts_id.get(position-1).getId();
                                     gettThana(getdistricts_id);

                              //   Toast.makeText(CompanyVerify_Activity.this, "getdistricts_id:"+getdistricts_id, Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{

                        Toast.makeText(CompanyVerify_Activity.this, jsonObjectMain2.getString("message"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("division_id",getdivisoin_id);
                return hashMap;
            }
        };

        RequestQueue requestQueue2= Volley.newRequestQueue(CompanyVerify_Activity.this);
        requestQueue2.add(stringRequest2);
    }



    private void gettThana(String getdistricts_id) {

        arrayList_thanasName.add("Select--Thana--");

        StringRequest stringRequest3 = new StringRequest(Request.Method.POST, thanas_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain3=new JSONObject(response);

                    if( jsonObjectMain3.getString("message").contains("Data is available!")){

                        JSONArray jsonArray3= jsonObjectMain3.getJSONArray("districts");

                        for (int i = 0; i <jsonArray3.length() ;i++) {

                            JSONObject jsonObject3=jsonArray3.getJSONObject(i);

                          arrayList_thanasName.add(jsonObject3.getString("bn_name"));
                        }

                        arrayAdapter_thanasName=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList_thanasName);
                        spinner_thana.setAdapter(arrayAdapter_thanasName);


                        spinner_thana.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                              thanaName= arrayList_thanasName.get(position).toString();
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{

                        Toast.makeText(CompanyVerify_Activity.this,  jsonObjectMain3.getString("message"), Toast.LENGTH_SHORT).show();
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
                hashMap.put("district_id",getdistricts_id);
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(CompanyVerify_Activity.this);
        requestQueue.add(stringRequest3);
    }


    public void Verify_btninformation(View view) {

        if(encodeImageFonts.equals("")){

     Toast.makeText(this, "Please NID Fonts Side Image", Toast.LENGTH_SHORT).show();

        }else if(encodeImageBack.equals("")){

         Toast.makeText(this, "Please NID Back Side Image", Toast.LENGTH_SHORT).show();

        }else if(spinner_division.getSelectedItem().toString().contains("Select--Division--")){

            Toast.makeText(this, "Please Your Division Select", Toast.LENGTH_SHORT).show();

        }else if(spinner_distric.getSelectedItem().toString().contains("Select--Districts--")){

            Toast.makeText(this, "Please Your Districts Select", Toast.LENGTH_SHORT).show();

        }else if(spinner_thana.getSelectedItem().toString().contains("Select--Thana--")){

            Toast.makeText(this, "Please Your Thana Select", Toast.LENGTH_SHORT).show();

        }else {

            final ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.show();
            progressDialog.setContentView(R.layout.custom_progress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


          String comName= editText_companyName.getText().toString().trim();
          String ownerName= editText_ownerName.getText().toString().trim();

          String address= editText_address.getText().toString().trim();

         String divisionName= spinner_division.getSelectedItem().toString();

         String districName= spinner_distric.getSelectedItem().toString();

         String thanaName= spinner_thana.getSelectedItem().toString();

              StringRequest stringRequest = new StringRequest(Request.Method.POST, Verify_informatin, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    if( jsonObjectMain.getString("message").contains("Send Information SuccessFully Pls Wait For Verify Process")){

                        Toast.makeText(CompanyVerify_Activity.this,  jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                         editText_companyName.setText("");
                         editText_ownerName.setText("");
                         editText_address.setText("");

                         Intent intent=new Intent(CompanyVerify_Activity.this,CompanyMapsActivity.class);
                         startActivity(intent);
                         finish();
                        progressDialog.dismiss();

                    }else{
                        progressDialog.dismiss();
                      Toast.makeText(CompanyVerify_Activity.this,  jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                netWorkError(error);
                progressDialog.dismiss();
            }
        }){
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> hashMap = new HashMap<String, String>();
                hashMap.put("security_error","tec71");
                hashMap.put("axcess_token",accessTocken);
                hashMap.put("comapny_name",comName);
                hashMap.put("company_owner",ownerName);
                hashMap.put("division",divisionName);
                hashMap.put("distric",districName);
                hashMap.put("thana",thanaName);
                hashMap.put("address",address);
                hashMap.put("nid_f",encodeImageFonts);
                hashMap.put("nid_b",encodeImageBack);
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(CompanyVerify_Activity.this);
        requestQueue.add(stringRequest);
        }

    }


    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(CompanyVerify_Activity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(CompanyVerify_Activity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(CompanyVerify_Activity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(CompanyVerify_Activity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(CompanyVerify_Activity.this, "No connection", Toast.LENGTH_SHORT).show();
            finish();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(CompanyVerify_Activity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();
    }


}
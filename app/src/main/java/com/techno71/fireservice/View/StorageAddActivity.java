package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
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
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.DateFragment;
import com.techno71.fireservice.Model.Districts_model;
import com.techno71.fireservice.Model.Divisoin_model;
import com.techno71.fireservice.Model.RenewDate;
import com.techno71.fireservice.R;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageAddActivity extends AppCompatActivity {

    private ImageView imageView_storage;
    private TextView textView_title;
    private static final int GalleryPick = 1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;

    String cameraPermission[];

    String storagePermission[];

    private Button button_submit;

    private ImageButton imageButtonApprovDate,imageButton_revnew;

    private String encodeImage="";

    private static EditText editText_approvDate,
            editText_renewDate,editText_storgeAddres,
            editText_ComName,editText_Owner,
            editText_Approved_Date,Companylocation,
            editText_CompanyType,
            editText_buildingTitle;


    private ArrayList<String> arrayList_divisionName = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_divisionName;

    private ArrayList<String> arrayList_districtsName = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_districtsName;

    private ArrayList<String> arrayList_thanasName = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_thanasName;

    private ArrayList<String> arrayList_color = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_color;


    private List<Divisoin_model> divisoin_id=new ArrayList<>();

    private List<Districts_model> districts_id=new ArrayList<>();

    private String divisions_url = Main_Url.ROOT_URL +"api/divisions-get";

    private String districts_url = Main_Url.ROOT_URL +"api/districts-get";

    private String thanas_url = Main_Url.ROOT_URL +"api/thanas-get";

   // private String storage_url= Main_Url.ROOT_URL +"api/add-storage";

   private String storage_url= Main_Url.ROOT_URL +"api/add-storage-new-check-point";

    private Spinner spinner_division,spinner_distric,
            spinner_thana,spinner_colors,spinner_flat;

    private Toolbar toolbar;

    private String message="",getdivisoin_id,getdistricts_id, accessTocken,
            divisionName,districtsName,thanaName;

    private SharedPreferences sharedPreferences_type;
    private ImageView imageView_choose;

    String latitude="",longitude="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage_add);

        toolbar = (Toolbar) findViewById(R.id.toolbar_companystorg);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        accessTocken = sharedPreferences_type.getString("access_token","access_token found");

        imageView_storage=(ImageView)findViewById(R.id.CompanyStorageImage);

        textView_title=(TextView)findViewById(R.id.textstorage_title);

        spinner_division=(Spinner)findViewById(R.id.storag_division_spinner);
        spinner_distric=(Spinner)findViewById(R.id.storag_distric_spinner);
        spinner_thana=(Spinner)findViewById(R.id.storag_thana_spinner);
        spinner_colors=(Spinner)findViewById(R.id.alertTag_spinner);
        spinner_flat=(Spinner)findViewById(R.id.storag_flat_spinner);

        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        imageView_storage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicDialog();
            }
        });

        imageView_choose=(ImageView)findViewById(R.id.imgeViewchoose_map);

        imageView_choose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(encodeImage.equals("")){

                    Toast.makeText(StorageAddActivity.this, "Storge Image Empty!!", Toast.LENGTH_SHORT).show();

                }else if(editText_ComName.getText().toString().isEmpty()){

                    Toast.makeText(StorageAddActivity.this, "Company Name Empty!!", Toast.LENGTH_SHORT).show();
                    editText_ComName.requestFocus();

                }else if(editText_Owner.getText().toString().isEmpty()){

                    Toast.makeText(StorageAddActivity.this, "Company Owner Empty!!", Toast.LENGTH_SHORT).show();
                    editText_Owner.requestFocus();

                }else if(editText_Approved_Date.getText().toString().isEmpty()){

                    Toast.makeText(StorageAddActivity.this, "Approved Date Empty!!", Toast.LENGTH_SHORT).show();
                    editText_Approved_Date.requestFocus();

                }else if(editText_renewDate.getText().toString().isEmpty()){

                    Toast.makeText(StorageAddActivity.this, "Renew Date Empty!!", Toast.LENGTH_SHORT).show();
                    editText_renewDate.requestFocus();

                }else if(spinner_flat.getSelectedItem().toString().contains("Select---Floor--")){

                    Toast.makeText(StorageAddActivity.this, "Please Your Floor Select", Toast.LENGTH_SHORT).show();

                }else if(spinner_division.getSelectedItem().toString().contains("Select--Division--")){

                    Toast.makeText(StorageAddActivity.this, "Please Your Division Select", Toast.LENGTH_SHORT).show();

                }else if(spinner_distric.getSelectedItem().toString().contains("Select--Districts--")){

                    Toast.makeText(StorageAddActivity.this, "Please Your Districts Select", Toast.LENGTH_SHORT).show();

                }else if(spinner_thana.getSelectedItem().toString().contains("Select--Thana--")){

                    Toast.makeText(StorageAddActivity.this, "Please Your Thana Select", Toast.LENGTH_SHORT).show();

                }else if(editText_CompanyType.getText().toString().isEmpty()){

                    Toast.makeText(StorageAddActivity.this, "Company Type Name  Empty!!", Toast.LENGTH_SHORT).show();
                    editText_CompanyType.requestFocus();

                }else if(editText_storgeAddres.getText().toString().isEmpty()){

                    Toast.makeText(StorageAddActivity.this, "Storge Address Empty!!", Toast.LENGTH_SHORT).show();
                    editText_storgeAddres.requestFocus();

                }  else{


                    String comName= editText_ComName.getText().toString().trim();
                    String OwnerName=  editText_Owner.getText().toString().trim();
                    String ApprovedDate= editText_Approved_Date.getText().toString().trim();
                    String renewDate= editText_renewDate.getText().toString().trim();
                    String details= editText_storgeAddres.getText().toString().trim();
                    String CompanyType= editText_CompanyType.getText().toString().trim();
                    String  buildingTitle= editText_buildingTitle.getText().toString().trim();

                    String divisionName= spinner_division.getSelectedItem().toString();

                    String districName= spinner_distric.getSelectedItem().toString();

                    String thanaName= spinner_thana.getSelectedItem().toString();

                    String colors= spinner_colors.getSelectedItem().toString();

                    String flat= spinner_flat.getSelectedItem().toString();

                    sharedPreferences_type.edit().putString("title",buildingTitle).commit();
                    sharedPreferences_type.edit().putString("comName",comName).commit();
                    sharedPreferences_type.edit().putString("OwnerName",OwnerName).commit();
                    sharedPreferences_type.edit().putString("ApprovedDate",ApprovedDate).commit();
                    sharedPreferences_type.edit().putString("renewDate",renewDate).commit();
                    sharedPreferences_type.edit().putString("divisionName",divisionName).commit();
                    sharedPreferences_type.edit().putString("districName",districName).commit();
                    sharedPreferences_type.edit().putString("thanaName",thanaName).commit();
                    sharedPreferences_type.edit().putString("colors",colors).commit();
                    sharedPreferences_type.edit().putString("flat",flat).commit();
                    sharedPreferences_type.edit().putString("CompanyType",CompanyType).commit();
                    sharedPreferences_type.edit().putString("details",details).commit();
                    sharedPreferences_type.edit().putString("CompanyType",CompanyType).commit();
                    sharedPreferences_type.edit().putString("encodeImage",CompanyType).commit();

                    Intent intent=new Intent(StorageAddActivity.this, Storage_MapsActivity.class);
                  //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent,1);

                }


            }
        });

        editText_ComName=(EditText)findViewById(R.id.edit_StcompanyName);
        editText_Owner=(EditText)findViewById(R.id.edit_StcompanyOwner);
        editText_Approved_Date=(EditText)findViewById(R.id.edit_license_approved);
        editText_renewDate=(EditText)findViewById(R.id.editLicense_renewDate);
        Companylocation=(EditText)findViewById(R.id.editStorage_CompanyLocation);
        editText_storgeAddres=(EditText)findViewById(R.id.edit_Companydetails);
        editText_CompanyType=(EditText)findViewById(R.id.editStorage_CompanyType);

        editText_buildingTitle=(EditText)findViewById(R.id.edit_buildingTitle);

        button_submit=(Button)findViewById(R.id.subit_storage);

        button_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Add_storage();
            }
        });

        imageButtonApprovDate=(ImageButton)findViewById(R.id.License_ApprovedBtn);

        imageButton_revnew=(ImageButton)findViewById(R.id.License_Renew_btn);

        editText_approvDate=(EditText)findViewById(R.id.edit_license_approved);

        editText_renewDate=(EditText)findViewById(R.id.editLicense_renewDate);

        imageButtonApprovDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment dateFragment = new DateFragment();
                dateFragment.show(getSupportFragmentManager(), "Date Picker");

            }
        });

        imageButton_revnew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            DialogFragment dateFragment = new RenewDate();
             dateFragment.show(getSupportFragmentManager(), "Date Picker");

         }
        });

        arrayList_color.clear();
        arrayList_color.add("--Select--Signals");
        arrayList_color.add("Green");
        arrayList_color.add("Yellow");
        arrayList_color.add("Red");
        arrayAdapter_color=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList_color);
        spinner_colors.setAdapter(arrayAdapter_color);

        getDivision();
      }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Add_storage(){

        if(encodeImage.equals("")){

            Toast.makeText(this, "Storge Image Empty!!", Toast.LENGTH_SHORT).show();

        }else if(editText_ComName.getText().toString().isEmpty()){

         Toast.makeText(this, "Company Name Empty!!", Toast.LENGTH_SHORT).show();
         editText_ComName.requestFocus();
         editText_ComName.setError("Company Name Empty!!");

        }else if(editText_Owner.getText().toString().isEmpty()){

            Toast.makeText(this, "Company Owner Empty!!", Toast.LENGTH_SHORT).show();
            editText_Owner.requestFocus();
            editText_Owner.setError("Company Owner Empty!!");

        }else if(editText_Approved_Date.getText().toString().isEmpty()){

            Toast.makeText(this, "Approved Date Empty!!", Toast.LENGTH_SHORT).show();
            editText_Approved_Date.requestFocus();
            editText_Approved_Date.setError("Approved Date Empty!!");

        }else if(editText_renewDate.getText().toString().isEmpty()){

            Toast.makeText(this, "Renew Date Empty!!", Toast.LENGTH_SHORT).show();
            editText_renewDate.requestFocus();
            editText_renewDate.setError("Renew Date Empty!!");

        }else if(spinner_flat.getSelectedItem().toString().contains("Select---Floor--")){

            Toast.makeText(this, "Please Your Floor Select", Toast.LENGTH_SHORT).show();

        }else if(spinner_division.getSelectedItem().toString().contains("Select--Division--")){

            Toast.makeText(this, "Please Your Division Select", Toast.LENGTH_SHORT).show();

        }else if(spinner_distric.getSelectedItem().toString().contains("Select--Districts--")){

            Toast.makeText(this, "Please Your Districts Select", Toast.LENGTH_SHORT).show();

        }else if(spinner_thana.getSelectedItem().toString().contains("Select--Thana--")){

            Toast.makeText(this, "Please Your Thana Select", Toast.LENGTH_SHORT).show();

        }else if(editText_CompanyType.getText().toString().isEmpty()){

            Toast.makeText(this, "Company Type Name  Empty!!", Toast.LENGTH_SHORT).show();
            editText_CompanyType.requestFocus();
            editText_CompanyType.setError("Company Type Name  Empty!!");

        }else if(editText_storgeAddres.getText().toString().isEmpty()){

            Toast.makeText(this, "Storge Address Empty!!", Toast.LENGTH_SHORT).show();
            editText_storgeAddres.requestFocus();
            editText_storgeAddres.setError("Storge Address Empty!!");

        }else if(Companylocation.getText().toString().isEmpty()){

            Toast.makeText(this, "Storge Location Empty!!", Toast.LENGTH_SHORT).show();
            Companylocation.requestFocus();
            Companylocation.setError("Storge Location Empty!!");

        } else if(latitude.equals("") & longitude.equals("") ){

            Toast.makeText(this, "Storge Location Empty!!", Toast.LENGTH_SHORT).show();

        }  else{

            ProgressDialog progressDialog = new ProgressDialog(this);

            progressDialog.show();
            progressDialog.setContentView(R.layout.custom_progress);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            String comName= editText_ComName.getText().toString().trim();
            String OwnerName=  editText_Owner.getText().toString().trim();
            String ApprovedDate= editText_Approved_Date.getText().toString().trim();
            String renewDate= editText_renewDate.getText().toString().trim();
            String Addres= editText_storgeAddres.getText().toString().trim();
            String CompanyType= editText_CompanyType.getText().toString().trim();
            String  location= Companylocation.getText().toString().trim();
            String  buildingTitle= editText_buildingTitle.getText().toString().trim();

            String divisionName= spinner_division.getSelectedItem().toString();

            String districName= spinner_distric.getSelectedItem().toString();

            String thanaName= spinner_thana.getSelectedItem().toString();

            String colors= spinner_colors.getSelectedItem().toString();

            String flat= spinner_flat.getSelectedItem().toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, storage_url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObjectMain=new JSONObject(response);

                        if(jsonObjectMain.getString("message").contains("Add Storage SuccessFully Pls Wait For Verify Process")){

                             editText_ComName.setText("");
                             editText_Owner.setText("");
                             editText_Approved_Date.setText("");
                             editText_renewDate.setText("");
                             editText_storgeAddres.setText("");
                             editText_CompanyType.setText("");
                             Companylocation.setText("");
                            editText_buildingTitle.setText("");
                            editText_buildingTitle.requestFocus();
                            progressDialog.dismiss();
                            encodeImage="";
                            Picasso.get().load(R.drawable.strog).into(imageView_storage);

                            Toast.makeText(StorageAddActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                        }else{
                            progressDialog.dismiss();
                            Toast.makeText(StorageAddActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
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
                    hashMap.put("company_Name",comName);
                    hashMap.put("company_Owner",OwnerName);
                    hashMap.put("license_approved_date",ApprovedDate);
                    hashMap.put("license_renew_date",renewDate);
                    hashMap.put("division",divisionName);
                    hashMap.put("distric",districName);
                    hashMap.put("thana",thanaName);
                    hashMap.put("company_latitude",latitude);
                    hashMap.put("company_longtude",longitude);
                    hashMap.put("company_Type",CompanyType);
                    hashMap.put("company_detils",Addres);
                    hashMap.put("alert_tag",colors);
                    hashMap.put("storage_img",encodeImage);
                    hashMap.put("address",location);
                    hashMap.put("floor",flat);
                    hashMap.put("title",buildingTitle);
                    return hashMap;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(StorageAddActivity.this);
            requestQueue.add(stringRequest);
        }

    }

    private void getDivision(){

        final ProgressDialog progressDialog = new ProgressDialog(this);

        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        arrayList_districtsName.clear();
        divisoin_id.clear();
        arrayList_thanasName.clear();
        arrayList_divisionName.add("Select--Division--");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, divisions_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);


                    if(jsonObjectMain.getString("message").contains("Data is available!")){

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
                              }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(StorageAddActivity.this, message, Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue= Volley.newRequestQueue(StorageAddActivity.this);
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
                    JSONObject jsonObjectMain=new JSONObject(response);

                    if(jsonObjectMain.getString("message").contains("Data is available!")){

                        JSONArray jsonArray2= jsonObjectMain.getJSONArray("districts");

                        for (int i = 0; i <jsonArray2.length() ;i++) {

                            JSONObject jsonObject=jsonArray2.getJSONObject(i);

                            Districts_model addItem=new Districts_model(jsonObject.getString("id"));
                            districts_id.add(addItem);

                            arrayList_districtsName.add(jsonObject.getString("bn_name"));
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
                                }else{
                                      arrayList_thanasName.clear();
                                  }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }else{

                        Toast.makeText(StorageAddActivity.this, jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue= Volley.newRequestQueue(StorageAddActivity.this);
        requestQueue.add(stringRequest2);
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

                            JSONObject jsonObject=jsonArray3.getJSONObject(i);

                            arrayList_thanasName.add(jsonObject.getString("bn_name"));
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

                        Toast.makeText(StorageAddActivity.this,  jsonObjectMain3.getString("message"), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue3= Volley.newRequestQueue(StorageAddActivity.this);
        requestQueue3.add(stringRequest3);
    }

    public static void ApprovDateSet(int year, int month, int day) {

   editText_approvDate.setText(month + "/" + day + "/" + year);

    }

    public static void RenewDateSet(int year, int month, int day) {

        editText_renewDate.setText(month + "/" + day + "/" + year);

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

    // checking storage permissions
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

    // Requesting camera and galler
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
        CropImage.activity().start(StorageAddActivity.this);
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
                    encodeBitmapImage_fonts(reducebitmap);
                    imageView_storage.setImageBitmap(reducebitmap);

                  //  Toast.makeText(this, "resultUri:"+resultUri, Toast.LENGTH_SHORT).show();
                    textView_title.setVisibility(View.GONE);
                } catch (Exception ex) {
                    Toast.makeText(this, ""+ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

        }  if(requestCode==1) {

            latitude = data.getStringExtra("latitude");
            longitude = data.getStringExtra("longitude");
            String Address = data.getStringExtra("Address");

            Companylocation.setText(""+ Address);

        }
    }


    private void  encodeBitmapImage_fonts(Bitmap bitmap){

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG,100,  byteArrayOutputStream);
        byte[] bytesofimage = byteArrayOutputStream.toByteArray();

         encodeImage = android.util.Base64.encodeToString(bytesofimage, Base64.DEFAULT);

    }

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(StorageAddActivity.this, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(StorageAddActivity.this, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(StorageAddActivity.this, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(StorageAddActivity.this, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(StorageAddActivity.this, "No connection", Toast.LENGTH_SHORT).show();
            finish();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(StorageAddActivity.this, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();
    }

}
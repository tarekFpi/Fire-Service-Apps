package com.techno71.fireservice.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;
import com.techno71.fireservice.View.CompanyMapsActivity;
import com.techno71.fireservice.View.CompanyVerify_Activity;
import com.techno71.fireservice.View.InformationLoddingActivity;
import com.techno71.fireservice.View.LoginActivity;
import com.techno71.fireservice.View.Profile_EditActivity;
import com.techno71.fireservice.View.Setting_Activity;
import com.techno71.fireservice.View.StorageAddActivity;
import com.techno71.fireservice.View.Storage_MapsActivity;
import com.techno71.fireservice.View.UserMapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import dev.shreyaspatil.MaterialDialog.AbstractDialog;
import dev.shreyaspatil.MaterialDialog.MaterialDialog;


public class UserProfile_Fragment extends Fragment {

    private LinearLayout linearLayout_editProf,linearLayout_pass;

    private TextView text_editPro,text_pass,
            textView_verify,textView_userName,textView_phone,textView_fullName;


    private String user_infor = Main_Url.ROOT_URL +"api/get-user-info";


    private SharedPreferences sharedPreferences_type;
    //user_profileImage

    private CircleImageView circleImageView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserProfile_Fragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static UserProfile_Fragment newInstance(String param1, String param2) {
        UserProfile_Fragment fragment = new UserProfile_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_user_profile_, container, false);

        sharedPreferences_type=getContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        language_model language=new language_model(getContext());
        language.loadLanguage();

        textView_verify=(TextView)view.findViewById(R.id.CompanyVerifyStatus);

        textView_phone=(TextView)view.findViewById(R.id.text_profilePhone);

        textView_userName=(TextView)view.findViewById(R.id.text_profileName);
        textView_fullName=(TextView)view.findViewById(R.id.textProfilFullName);

        circleImageView=(CircleImageView)view.findViewById(R.id.user_profileImage);


        text_editPro=(TextView) view.findViewById(R.id.editprofile);
        text_editPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(), Profile_EditActivity.class);
                startActivity(intent);
            }
        });

        language_model  langua_model=new language_model(getContext());
           langua_model.loadLanguage();

        text_pass=(TextView)view.findViewById(R.id.editPass);

        text_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 Intent intent_pass=new Intent(getContext(), InformationLoddingActivity.class);
                intent_pass.putExtra("Techno","userPass");
                startActivity(intent_pass);

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        getUserInfromation();
    }

    private void getUserInfromation(){

        final ProgressDialog progressDialog = new ProgressDialog(getContext());

        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        String  accessTocken= sharedPreferences_type.getString("access_token","access_token found");

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

                            sharedPreferences_type.edit().putString("status",jsonObject.getString("company_verify_status")).commit();
                            sharedPreferences_type.edit().putString("user_type",jsonObject.getString("user_type")).commit();

                            textView_phone.setText(""+jsonObject.getString("user_name"));
                            textView_fullName.setText(""+jsonObject.getString("user_name"));
                            textView_userName.setText(""+jsonObject.getString("mobile_no"));

                            String image="https://fifaar.com/public/"+jsonObject.getString("user_picture");
                             Picasso.get().load(image).into(circleImageView);

                            if(jsonObject.getString("user_type").contains("2")){

                                textView_verify.setVisibility(View.VISIBLE);

                               // Toast.makeText(getContext(), "status:"+jsonObject.getString("status"), Toast.LENGTH_SHORT).show();
                                if(jsonObject.getString("company_verify_status").contains("0")){
                                    textView_verify.setText("UnVerify");

                                    textView_verify.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {


                                            MaterialDialog mDialog = new MaterialDialog.Builder(getActivity())
                                                    .setTitle("Company Verify Notices")
                                                    .setMessage("Are you sure you want to Company Verify??")
                                                    .setCancelable(false)
                                                    .setPositiveButton("Les Go", new AbstractDialog.OnClickListener() {
                                                        @Override
                                                        public void onClick(dev.shreyaspatil.MaterialDialog.interfaces.DialogInterface dialogInterface, int which) {
                                                            dialogInterface.dismiss();

                                                            Intent intent=new Intent(getContext(),CompanyVerify_Activity.class);
                                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent);

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

                                        }
                                    });

                                }else if(jsonObject.getString("company_verify_status").contains("1")){

                                    textView_verify.setEnabled(false);

                                    textView_verify.setText("Processing");
                                    textView_verify.setTextColor(getResources().getColor(R.color.DarkOrchid));

                                }else if(jsonObject.getString("company_verify_status").contains("2")){

                                    textView_verify.setEnabled(false);

                                    textView_verify.setText("Verifyed");
                                    textView_verify.setTextColor(getResources().getColor(R.color.YellowGreen));

                                }
                            }

                        }

                    }else if(jsonObjectMain.getString("message").contains("Sry Access Token Not match")){

                        Intent intent=new Intent(getContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        sharedPreferences_type.edit().clear().commit();
                        Toast.makeText(getContext(),jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();
                    }else{

                        progressDialog.dismiss();
                        Toast.makeText(getContext(), jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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

        RequestQueue requestQueue= Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(getContext(), "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(getContext(), "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(getContext(), "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(getContext(), "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(getContext(), "No connection", Toast.LENGTH_SHORT).show();

        } else if (error instanceof TimeoutError) {
            Toast.makeText(getContext(), "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();
    }

}
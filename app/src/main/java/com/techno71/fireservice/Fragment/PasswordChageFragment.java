package com.techno71.fireservice.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.techno71.fireservice.View.Profile_EditActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class PasswordChageFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private EditText editText_oldPassword,
            editText_newPassworld,
            editText_ComfrimPass;

    private String changePassword = Main_Url.ROOT_URL +"api/change-password";

    private SharedPreferences sharedPreferences_type;

    private Button button_chagne;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public PasswordChageFragment() {
        // Required empty public constructor
    }

    public static PasswordChageFragment newInstance(String param1, String param2) {
        PasswordChageFragment fragment = new PasswordChageFragment();
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
        View view= inflater.inflate(R.layout.fragment_password_chage, container, false);

        language_model langua_model=new language_model(getContext());
        langua_model.loadLanguage();

        sharedPreferences_type=getActivity().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        button_chagne=(Button)view.findViewById(R.id.btn_update_Password);

        editText_oldPassword= (EditText)view.findViewById(R.id.editOld_password);

        editText_newPassworld= (EditText)view.findViewById(R.id.edit_new_password);

        editText_ComfrimPass= (EditText)view.findViewById(R.id.edit_confirm_password);

        button_chagne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              ChagePassword();
            }
        });

       return view;
    }

    private void ChagePassword(){


        if(editText_oldPassword.getText().toString().isEmpty()){

            editText_oldPassword.setError("Please Your Old Password !!");
            editText_oldPassword.requestFocus();

        }else if(editText_newPassworld.getText().toString().isEmpty()){
            editText_newPassworld.setError("Please New Password!!");
            editText_newPassworld.requestFocus();

        }else if(editText_ComfrimPass.getText().toString().isEmpty()){
            editText_ComfrimPass.setError("Please Your Confrim Password !!");
            editText_ComfrimPass.requestFocus();
        }else{

            String oldPassword= editText_oldPassword.getText().toString().trim();

            String  NewPassword= editText_newPassworld.getText().toString().trim();

            String  ComPassword= editText_ComfrimPass.getText().toString().trim();

            String  accessTocken = sharedPreferences_type.getString("access_token","access_token found");

            StringRequest stringRequest = new StringRequest(Request.Method.POST, changePassword, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObjectMain=new JSONObject(response);


                        if(jsonObjectMain.getString("message").contains("Password updated successfully")){

                            Toast.makeText(getActivity(),jsonObjectMain.getString("message"), Toast.LENGTH_LONG).show();

                             editText_ComfrimPass.setText("");
                             editText_oldPassword.setText("");
                             editText_newPassworld.setText("");
                             editText_oldPassword.requestFocus();

                        }else{

                            Toast.makeText(getActivity(), jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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
                    hashMap.put("old_password",oldPassword);
                    hashMap.put("password",NewPassword);
                    hashMap.put("password_confirmation",ComPassword);

                    return hashMap;
                }
            };

            RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
            requestQueue.add(stringRequest);
        }
    }

    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(getActivity(), "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(getActivity(), "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(getActivity(), "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(getActivity(), "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(getActivity(), "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(getActivity(), "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }
}
package com.techno71.fireservice.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.techno71.fireservice.Controller.CommentShowAdapter;
import com.techno71.fireservice.Model.commentShowModel;
import com.techno71.fireservice.R;
import com.techno71.fireservice.View.CompanyMapsActivity;
import com.techno71.fireservice.View.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Comment_Fragment extends Fragment {

    private RecyclerView recyclerView;

    private SharedPreferences sharedPreferences_type;

    private String comment_show = Main_Url.ROOT_URL +"api/storage-comment-show-by-user";

    private List<commentShowModel>commentShowModelList=new ArrayList<>();

    private CommentShowAdapter commentShowAdapter;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Comment_Fragment() {

    }


    // TODO: Rename and change types and number of parameters
    public static Comment_Fragment newInstance(String param1, String param2) {
        Comment_Fragment fragment = new Comment_Fragment();
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
        View view= inflater.inflate(R.layout.fragment_comment_, container, false);

        sharedPreferences_type=getActivity().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView_commentShow);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        ShowComment();

        return view;
    }

   private void ShowComment(){

       final ProgressDialog progressDialog = new ProgressDialog(getActivity());

       progressDialog.show();
       progressDialog.setContentView(R.layout.custom_progress);
       progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

       String  accessTocken= sharedPreferences_type.getString("access_token","access_token found");


       StringRequest stringRequest = new StringRequest(Request.Method.POST, comment_show, new Response.Listener<String>() {
           @Override
           public void onResponse(String response) {

               try {
                   JSONObject jsonObjectMain=new JSONObject(response);

                   if(jsonObjectMain.getString("message").contains("Data is available!")){
                       progressDialog.dismiss();

                       JSONArray jsonArray= jsonObjectMain.getJSONArray("UserComment");

                       for (int i = 0; i <jsonArray.length() ;i++) {

                           JSONObject jsonObject = jsonArray.getJSONObject(i);

          commentShowModel item =new commentShowModel(
                  jsonObject.getString("title"),
                  jsonObject.getString("floor"),
                  jsonObject.getString("comment"),
                  jsonObject.getString("alert_tag"),
                  jsonObject.getString("created_at")
              );
                   commentShowModelList.add(item);
                }

         commentShowAdapter=new CommentShowAdapter(commentShowModelList,getContext());
         recyclerView.setAdapter(commentShowAdapter);
         commentShowAdapter.notifyDataSetChanged();

                   } else{

                       progressDialog.dismiss();
                       Toast.makeText(getActivity(), jsonObjectMain.getString("message"), Toast.LENGTH_SHORT).show();
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

       RequestQueue requestQueue= Volley.newRequestQueue(getActivity());
       requestQueue.add(stringRequest);
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
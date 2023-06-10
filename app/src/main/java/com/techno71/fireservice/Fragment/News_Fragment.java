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
import com.techno71.fireservice.Controller.BookMarkAdapter;
import com.techno71.fireservice.Controller.NewsAdapter;
import com.techno71.fireservice.Model.BookMarkShow;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.Model.news_model;
import com.techno71.fireservice.R;
import com.techno71.fireservice.View.SingleNewsShowActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class News_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private NewsAdapter adapter;

    private List<news_model>modelList=new ArrayList<>();

    private RecyclerView recyclerView;

    private String news_url= Main_Url.ROOT_URL +"api/show-news";

    private SharedPreferences sharedPreferences_type;

    private String accessTocken="";


    private String mParam1;
    private String mParam2;

    public News_Fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static News_Fragment newInstance(String param1, String param2) {
        News_Fragment fragment = new News_Fragment();
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
        View view= inflater.inflate(R.layout.fragment_news_, container, false);
        sharedPreferences_type=getContext().getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        accessTocken = sharedPreferences_type.getString("access_token","access_token found");

        language_model language=new language_model(getContext());
        language.loadLanguage();

         recyclerView=(RecyclerView)view.findViewById(R.id.recyclerView_news);
         recyclerView.setHasFixedSize(true);
         recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        News_Show();
        return  view;
    }

    private void News_Show(){


        final ProgressDialog progressDialog = new ProgressDialog(getContext());

        progressDialog.show();
        progressDialog.setContentView(R.layout.custom_progress);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, news_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Toast.makeText(getContext(), "response:"+response, Toast.LENGTH_SHORT).show();
                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    if(jsonObjectMain.getString("message").contains("Data is available!")){

                        JSONArray jsonArray= jsonObjectMain.getJSONArray("districts");

                        for (int i = 0; i <jsonArray.length() ;i++) {

                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String image="https://fifaar.com/public/"+jsonObject.getString("image");

                            news_model bookMarkShow=
                                    new news_model(jsonObject.getString("id")
                                            ,jsonObject.getString("title")
                                            ,jsonObject.getString("description"),
                                            image,jsonObject.getString("created_at"));

                            modelList.add(bookMarkShow);
                        }
                        adapter=new NewsAdapter(getContext(),modelList);
                        recyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        progressDialog.dismiss();

                        adapter.setOnItemClick(new NewsAdapter.onItemClickLisiner() {
                            @Override
                            public void OnClick_Lisiner(int position) {

                                news_model item_position=modelList.get(position);

                                Intent intent=new Intent(getContext(), SingleNewsShowActivity.class);
                                intent.putExtra("details",item_position.getLongDesription());
                                intent.putExtra("image",item_position.getImage());
                                intent.putExtra("date",item_position.getUpdateDate());
                                intent.putExtra("title",item_position.getTitle());
                                startActivity(intent);
                            }
                        });

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
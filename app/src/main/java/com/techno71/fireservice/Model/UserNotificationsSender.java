/*
package com.techno71.fireservice.Model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.R;
import com.techno71.fireservice.View.CompanyMapsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UserNotificationsSender {
    String userFcmToken;
    String title;
    String body;
    Context mContext;
    Activity mActivity;
    String lat,Long;

    private String  locatinWith_storage= Main_Url.ROOT_URL +"api/test-alert";

    private SharedPreferences sharedPreferences_type;

    private RequestQueue requestQueue;
    private final String postUrl = "https://fcm.googleapis.com/fcm/send";
    private final String fcmServerKey ="AAAAAzO5fU8:APA91bEPhnmB1tg2giJ63voyZc2xcElAqTv2KlLKflk7KRZF7kM6PBm4tX9-nLnsGI3Lfu0-cREe6vZm8VELXxikG9htDzzsaW73u_ekdejjZi6WY85SttIk_bZIihluJPREFXKrF5SC";

    public UserNotificationsSender(String userFcmToken, String title, String body, Context mContext, Activity mActivity) {
        this.userFcmToken = userFcmToken;
        this.title = title;
        this.body = body;
        this.mContext = mContext;
        this.mActivity = mActivity;

    }

    public void SendNotifications() {

        File xmlFile = new java.io.File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/src/res/raw/alarm.mp3");

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
         //   notiObject.put("icon", R.drawable.ic_baseline_notifications_24); // enter icon that exists in drawable only
            notiObject.put("sound",xmlFile); //src/res/raw/ enter icon that exists in drawable only
            mainObj.put("notification", notiObject);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;

                }
            };
            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }


     */
/* sharedPreferences_type=mContext.getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

       String Latitud= sharedPreferences_type.getString("myLatitud","Latitud Not Found");
      String Longitude= sharedPreferences_type.getString("myLongitude","Longitude Not Found");


        String  accessTocken= sharedPreferences_type.getString("access_token","access_token found");

        StringRequest stringRequest = new StringRequest(Request.Method.POST, locatinWith_storage, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObjectMain=new JSONObject(response);

                    //if(jsonObjectMain.getString("message").contains("Data is available!")){

                        JSONArray jsonArray= jsonObjectMain.getJSONArray("user");

                        for (int i = 0; i <jsonArray.length() ;i++) {

                            JSONObject jsonObject=jsonArray.getJSONObject(i);

                            lat=jsonObject.getString("latitude");

                            Long =jsonObject.getString("longtude");

                             Log.e("Long:",Long);
                            SendMessage(lat,Long);
                           // Toast.makeText(mContext, "lat:"+lat, Toast.LENGTH_SHORT).show();
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
                hashMap.put("alert_type","আগুন লাগছে");
                hashMap.put("latitude",Latitud);
                hashMap.put("longtude", Longitude);
                return hashMap;
            }
        };

        RequestQueue requestQueue= Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);

*//*


    }

*/
/*
    void  SendMessage(String Latitud,String Longitude){

        File xmlFile = new java.io.File(Environment
                .getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                + "/src/res/raw/alarm.mp3");

        requestQueue = Volley.newRequestQueue(mActivity);
        JSONObject mainObj = new JSONObject();
        try {
            mainObj.put("to", userFcmToken);
            JSONObject notiObject = new JSONObject();
            notiObject.put("title", title);
            notiObject.put("body", body);
            notiObject.put("icon", R.drawable.ic_baseline_notifications_24); // enter icon that exists in drawable only
            notiObject.put("sound",xmlFile); //src/res/raw/ enter icon that exists in drawable only
            mainObj.put("notification", notiObject);
            mainObj.put("lat", Latitud);
            mainObj.put("lng", Longitude);

            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, postUrl, mainObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {

                    Map<String, String> header = new HashMap<>();
                    header.put("content-type", "application/json");
                    header.put("authorization", "key=" + fcmServerKey);
                    return header;

                }
            };
            requestQueue.add(request);

        } catch (JSONException e) {
            e.printStackTrace();
        }




    }
*//*


    private void netWorkError(VolleyError error) {

        if (error instanceof NetworkError) {
            Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(mContext, "Our server is busy please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(mContext, "AuthFailure Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(mContext, "Parse Error please try again later", Toast.LENGTH_SHORT).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(mContext, "No connection", Toast.LENGTH_SHORT).show();
        } else if (error instanceof TimeoutError) {
            Toast.makeText(mContext, "Server time out please try again later", Toast.LENGTH_SHORT).show();
        }
        error.printStackTrace();

    }
}
*/

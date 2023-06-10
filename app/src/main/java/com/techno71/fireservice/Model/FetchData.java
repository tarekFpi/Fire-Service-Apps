package com.techno71.fireservice.Model;

import android.os.AsyncTask;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

public class FetchData extends AsyncTask<Object,String,String> {

    String googleNearyByPlacesData;

    GoogleMap googleMap;
    String url;

    @Override
    protected String doInBackground(Object... objects) {

     try {
         googleMap=(GoogleMap)objects[0];
         url=(String) objects[1];

    DownloadUrl downloadUrl=new DownloadUrl();
    googleNearyByPlacesData=downloadUrl.retirveUrl(url);

     }catch (Exception exception){
         exception.printStackTrace();
     }

        return googleNearyByPlacesData;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        try {
            JSONObject jsonObject=new JSONObject(s);
            JSONArray jsonArray=jsonObject.getJSONArray("result");

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObject1=jsonArray.getJSONObject(i);
                JSONObject getlocation=jsonObject1.getJSONObject("geomtry").getJSONObject("locatoin");

                String lat=getlocation.getString("lat");

                String lng=getlocation.getString("lng");

                JSONObject getName=jsonArray.getJSONObject(i);
                String name=getName.getString("name");

                LatLng latLng=new LatLng(Double.parseDouble(lat),Double.parseDouble(lng));
                MarkerOptions markerOptions=new MarkerOptions();
                markerOptions.title(name);
                markerOptions.position(latLng);
                googleMap.addMarker(markerOptions);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,18));

            }

        }catch (Exception e){

        }
    }
}

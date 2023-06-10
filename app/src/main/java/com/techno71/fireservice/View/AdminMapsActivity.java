package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;


public class AdminMapsActivity extends FragmentActivity implements OnMapReadyCallback ,
        GoogleMap.OnMarkerClickListener{

    private GoogleMap mMap;

    private final LatLng PERTH = new LatLng(23.755520378399186, 90.41589518667055);
    private final LatLng SYDNEY = new LatLng(23.754125957876735, 90.41535874490094);
    private final LatLng BRISBANE = new LatLng(23.754626772600098, 90.41825553045686);
    private final LatLng melbourneLocation  = new LatLng(23.758353349135366, 90.42069155270602);
    private final LatLng melbourne5  = new LatLng(23.759531695385597, 90.41736561373438);
    private final LatLng melbourne6  = new LatLng(23.760405654017976, 90.41972591907546);
    private final LatLng melbourne7  = new LatLng(23.75246890227815, 90.42015088949918);

    //23.75246890227815, 90.42015088949918

    private Marker markerPerth;
    private Marker markerSydney;
    private Marker markerBrisbane;
    private Marker markermelbourne;
    private Marker markermel5;
    private Marker markermel6;
    private Marker markermel7;

    private Toolbar toolbar;

    private DrawerLayout drawerLayout;

    private NavigationView navigationView;

    private ActionBarDrawerToggle toggleButton;
    private SharedPreferences sharedPreferences_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_maps);
      //  toolbar=(Toolbar)findViewById(R.id.toolbar_admin);

        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawerLayout=(DrawerLayout)findViewById(R.id.drawer_layoutAdmin);
      //  navigationView=(NavigationView)findViewById(R.id.navigation_id);

        toggleButton=new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        toggleButton.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));


      /*  drawerLayout.addDrawerListener(toggleButton);
        toggleButton.syncState();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId()) {

                    case R.id.sideAd_home:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.Side_AdlogOut:

                        Intent intent=new Intent(AdminMapsActivity.this,LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                        sharedPreferences_type.edit().clear().commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.side_AdchangePassword:

                        Intent intent_pass=new Intent(AdminMapsActivity.this,InformationLoddingActivity.class);
                        intent_pass.putExtra("Techno","userPass");
                        startActivity(intent_pass);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.sideAd_profile:

                        Intent intent_prf=new Intent(AdminMapsActivity.this,InformationLoddingActivity.class);
                        intent_prf.putExtra("Techno","userPrf");
                        startActivity(intent_prf);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                    case R.id.SideAd_setting:

                        Intent intent_settting=new Intent(AdminMapsActivity.this,Setting_Activity.class);
                        startActivity(intent_settting);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
              //
                    case R.id.sideAd_Bookmarks:
                        Intent intent_book=new Intent(AdminMapsActivity.this,InformationLoddingActivity.class);
                        intent_book.putExtra("Techno","bookMark");
                        startActivity(intent_book);
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }
                return false;
            }
        });

*/
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
          .findFragmentById(R.id.Admin_map);
        mapFragment.getMapAsync(this);


    }

    @Override
    protected void onResume() {
        super.onResume();

        language_model language=new language_model(this);
        language.loadLanguage();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        markerPerth = mMap.addMarker(new MarkerOptions()
                .position(PERTH)
                .title("Perth"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(PERTH));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(PERTH,14));


        markerSydney = mMap.addMarker(new MarkerOptions()
                .position(SYDNEY)
                .title("Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(SYDNEY));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SYDNEY,14));

        markerBrisbane = mMap.addMarker(new MarkerOptions()
                .position(BRISBANE).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .title("Brisbane"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(BRISBANE));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(BRISBANE,14));

        markermelbourne = mMap.addMarker(new MarkerOptions()
                .position(melbourneLocation).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
                .title("Brisbane"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(melbourneLocation));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourneLocation,14));

        markermel5 = mMap.addMarker(new MarkerOptions()
                .position(melbourne5).icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Brisbane"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne5));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne5,14));



        markermel6 = mMap.addMarker(new MarkerOptions()
                .position(melbourne6).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Brisbane"));
        markermel6.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne6));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne6,14));


        markermel7 = mMap.addMarker(new MarkerOptions()
                .position(melbourne7).icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)).title("Techn71"));
    //  markermel7.setTag(0);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne7));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne7,14));

        mMap.setOnMarkerClickListener(this);

    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

       // Integer clickCount = (Integer) marker.getTag();

        if (marker.equals(markermel7))
        {
   Toast.makeText(this," 7has been clicked ",Toast.LENGTH_SHORT).show();

     Intent intent=new Intent(AdminMapsActivity.this,Admin_MarkDetailsActivity.class);
      startActivity(intent);
        }

        if (marker.equals(markermel6))
        {
            Toast.makeText(this," has been clicked ",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(AdminMapsActivity.this,Admin_MarkDetailsActivity.class);
            startActivity(intent);
        }

        if (marker.equals(markermel5))
        {
            Toast.makeText(this," has been clicked ",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(AdminMapsActivity.this,Admin_MarkDetailsActivity.class);
            startActivity(intent);
        }

        if (marker.equals(markermelbourne))
        {
            Toast.makeText(this," has been clicked ",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(AdminMapsActivity.this,Admin_MarkDetailsActivity.class);
            startActivity(intent);
        }

        if (marker.equals(markerBrisbane))
        {
            Toast.makeText(this," has been clicked ",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(AdminMapsActivity.this,Admin_MarkDetailsActivity.class);
            startActivity(intent);
        }

        if (marker.equals(markerSydney))
        {
            Toast.makeText(this," has been clicked ",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(AdminMapsActivity.this,Admin_MarkDetailsActivity.class);
            startActivity(intent);
        }

        if (marker.equals(markerSydney))
        {
            Toast.makeText(this," has been clicked ",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(AdminMapsActivity.this,Admin_MarkDetailsActivity.class);
            startActivity(intent);
        }

        if (marker.equals(markerPerth))
        {
            Toast.makeText(this," has been clicked ",Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(AdminMapsActivity.this,Admin_MarkDetailsActivity.class);
            startActivity(intent);
        }
           // clickCount = clickCount + 1;
           // marker.setTag(clickCount);




        return false;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to Exit?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        finishAffinity();
                    }
                }).show();
    }



}
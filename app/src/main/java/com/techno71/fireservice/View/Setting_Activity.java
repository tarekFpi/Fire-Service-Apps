package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;

public class Setting_Activity extends AppCompatActivity {

    private CardView cardView;
    private String  languaue[]= {"Bangla","English"};

    private language_model langua_model;

    private Toolbar toolbar;

    private CardView cardView_phoneCall;

    private SharedPreferences sharedPreferences_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        sharedPreferences_type=getApplicationContext().getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);

        toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        langua_model=new language_model(this);
        langua_model.loadLanguage();



        cardView=(CardView)findViewById(R.id.setting_cardLanguage);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(Setting_Activity.this);
                builder.setTitle("Choose Languang");
                 builder.setSingleChoiceItems(languaue, -1, new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {

                        if(which==0){

                            langua_model.setLocal("bn");
                             recreate();
                            dialog.dismiss();
                        }else if(which==1){

                             langua_model.setLocal("en");
                              recreate();
                            dialog.dismiss();
                         }
                     }
                 });
              builder.create();
              builder.show();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

  @Override
    public void onBackPressed() {

         goBack();

        // super.onBackPressed();
    }

  /*  @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {

            Toast.makeText(this, "finish", Toast.LENGTH_SHORT).show();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    void goBack() {

        String userType=sharedPreferences_type.getString("user_type","Type not Found..").toString();

       if(userType.contains("2")){

            Intent go_Back = new Intent(Setting_Activity.this, CompanyMapsActivity.class);
            go_Back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(go_Back);
            overridePendingTransition(R.anim.slide_in_left,
             R.anim.slide_out_right);
            finish();


       } if(userType.contains("3")){

            Intent go_Back = new Intent(Setting_Activity.this, UserMapsActivity.class);
            go_Back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(go_Back);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            finish();

        }if(userType.contains("1")){

            Intent go_Back = new Intent(Setting_Activity.this, FireMapActivity.class);
            go_Back.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            startActivity(go_Back);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);

            finish();

        }


    }
}
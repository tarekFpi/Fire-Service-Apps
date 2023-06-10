package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class SingleNewsShowActivity extends AppCompatActivity {

  private TextView textView_details,textView_newsTitle;
  private Toolbar toolbar;

  private ImageView imageView_user;

  private RelativeLayout relativeLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_news_show);

        toolbar=(Toolbar)findViewById(R.id.toolbar_newView);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("details");
        String title = intent.getStringExtra("title");
         String stringimage = intent.getStringExtra("image");

        textView_details=(TextView)findViewById(R.id.textview_newsDetails);

        textView_newsTitle=(TextView)findViewById(R.id.textview_newsTitle);

        textView_newsTitle.setText("Title:"+title);

        textView_details.setText("Description:"+ Html.fromHtml(stringExtra));

        imageView_user=(ImageView)findViewById(R.id.imageView_new);

        relativeLayout=(RelativeLayout) findViewById(R.id.relative_news_image);
        relativeLayout.setDrawingCacheEnabled(true);

        Picasso.get().load(stringimage).into(imageView_user);

     /*   intent.putExtra("image",item_position.getImage());
        intent.putExtra("date",item_position.getUpdateDate());*/
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.news_share,menu);

        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onResume() {
        super.onResume();

        language_model language=new language_model(this);
        language.loadLanguage();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        if(item.getItemId()==R.id.menu_new_share){
          //  finish();

            ShareNews_image();
        }

        return super.onOptionsItemSelected(item);
    }

    private void ShareNews_image(){

        try {
            Bitmap bitmap=BitmapCreate(relativeLayout);

            ///   File imagePath = new File(getApplicationContext().getFilesDir(), "images");
            File file=new File(getExternalCacheDir(),"backg2.png");
            FileOutputStream stream=  new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.PNG,100,stream);

            stream.flush();
            stream.close();
            file.setReadable(true,false);

            if(file!=null){

                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                intent.putExtra(Intent.EXTRA_SUBJECT,"Fire Apps News");

                String path = MediaStore.Images.Media.insertImage(getContentResolver(), String.valueOf(file), "Title", null);
                Uri imageUri =  Uri.parse(path);

                Intent intent_shar=new Intent(Intent.ACTION_SEND);
                intent_shar.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent_shar.putExtra(Intent.EXTRA_STREAM,imageUri);
                intent_shar.setType("image/png");
                startActivity(Intent.createChooser(intent_shar,"Fire Apps News"));

            }else{
                Toast.makeText(SingleNewsShowActivity.this, "file empty", Toast.LENGTH_SHORT).show();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("ResourceAsColor")
    private Bitmap BitmapCreate(View view){

        Bitmap returnBitmap=Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas=new Canvas(returnBitmap);

        Drawable drawable=view.getBackground();

        if(drawable!=null){

            drawable.draw(canvas);
        }else{

            canvas.drawColor(android.R.color.white);
        }
        view.draw(canvas);

        return    returnBitmap;
    }


    @SuppressLint("ResourceAsColor")
    private Bitmap getBitmap(View view){

        Bitmap bitmap=Bitmap.createBitmap(view.getWidth(),view.getHeight(),Bitmap.Config.ARGB_8888);

        Canvas canvas=new Canvas(bitmap);

        Drawable drawable=view.getBackground();

        if(drawable!=null){
          drawable.draw(canvas);
        }else{
            canvas.drawColor(android.R.color.white);
        }
          view.draw(canvas);


        return  bitmap;
    }


}
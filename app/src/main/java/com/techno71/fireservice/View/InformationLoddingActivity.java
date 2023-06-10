package com.techno71.fireservice.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.techno71.fireservice.Fragment.BookMarkFragment;
import com.techno71.fireservice.Fragment.Comment_Fragment;
import com.techno71.fireservice.Fragment.HelpCenter_Fragment;
import com.techno71.fireservice.Fragment.News_Fragment;
import com.techno71.fireservice.Fragment.PasswordChageFragment;
import com.techno71.fireservice.Fragment.UserProfile_Fragment;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;

import java.util.Locale;

public class InformationLoddingActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_lodding);
        toolbar=(Toolbar)findViewById(R.id.toolbar_lodding);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("Techno");

        if (stringExtra.equals("userPass")) {
            PasswordChageFragment passwordChageFragment = new PasswordChageFragment();
            toolbar.setTitle(R.string.change_password);
            replaceFragment(passwordChageFragment);
        }

       /* if (stringExtra.equals("FlatAdd")) {
            PasswordChageFragment passwordChageFragment = new PasswordChageFragment();
            toolbar.setTitle(R.string.change_password);
            replaceFragment(passwordChageFragment);
        }*/

        if (stringExtra.equals("comment")) {
            Comment_Fragment comment_fragment = new Comment_Fragment();
            toolbar.setTitle("My Comment");
            replaceFragment(comment_fragment);

        }

        if (stringExtra.equals("news")) {
            News_Fragment news_fragment = new News_Fragment();
            toolbar.setTitle(R.string.view_news);
            replaceFragment(news_fragment);
        }

        if (stringExtra.equals("userPrf")) {
            UserProfile_Fragment userProfile_fragment = new UserProfile_Fragment();
            toolbar.setTitle(R.string.view_profile);
            replaceFragment(userProfile_fragment);
        }

        if (stringExtra.equals("bookMark")) {
            BookMarkFragment bookMarkFragment = new BookMarkFragment();
            toolbar.setTitle(R.string.bookmark);
            replaceFragment(bookMarkFragment);
        }

        if (stringExtra.equals("call")) {
            HelpCenter_Fragment helpCenterFragment = new HelpCenter_Fragment();
            toolbar.setTitle(R.string.help_center_phone_call);
            replaceFragment(helpCenterFragment);
        }

    }

    private void replaceFragment(Fragment fragment) {

        androidx.fragment.app.FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.replace(R.id.fl_FromLoadContainer, fragment);
        beginTransaction.addToBackStack(fragment.toString());
        beginTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        beginTransaction.commit();
    }
 /*   @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            finish();
        }
        if(item.getItemId()==R.id.lang_bangla){

            setLocal("bn");
            recreate();

            return  true;
        } if(item.getItemId()==R.id.lang_english){

            setLocal("en");
            recreate();
            return  true;
        }

        return super.onOptionsItemSelected(item);
    }*/

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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //    super.onBackPressed();
        finish();
    }

/*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater=getMenuInflater();

        menuInflater.inflate(R.menu.languages_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }*/


}
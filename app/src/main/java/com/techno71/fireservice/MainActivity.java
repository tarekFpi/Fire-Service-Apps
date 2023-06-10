package com.techno71.fireservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements Animation.AnimationListener {
    public Circle curve;
    LinearLayout taplayout;
    boolean cancel = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       curve = findViewById(R.id.curve2);
        taplayout = findViewById(R.id.layouttap2);

        initanimation();

    }

    private void initanimation() {

        final CircleAnimation circleAnimation = new CircleAnimation(curve, 127.0f);
        this.curve.setCurveColor(ContextCompat.getColor(this, R.color.tap_light), ContextCompat.getColor(this, R.color.tap_dark));
        circleAnimation.setDuration(2000);
        circleAnimation.setAnimationListener(this);
        this.taplayout.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case 0:
                        curve.startAnimation(circleAnimation);
                        cancel = false;
                      //  Toast.makeText(MainActivity.this, "start", Toast.LENGTH_SHORT).show();
                        return true;
                    case 1:
                    case 3:

             //   Toast.makeText(MainActivity.this, "SuccessFull", Toast.LENGTH_SHORT).show();
                        circleAnimation.cancel();
                        cancel = true;
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    @Override
    public void onAnimationStart(Animation animation) {
        Log.d("anim","Animation started");

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (cancel){
            Log.d("anim","Incomplete");

        }else {
            Toast.makeText(MainActivity.this, "SuccessFull", Toast.LENGTH_SHORT).show();
            Log.d("anim","Complete");

        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
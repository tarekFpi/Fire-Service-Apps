package com.techno71.fireservice.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.techno71.fireservice.Controller.CommentAdapter;
import com.techno71.fireservice.Controller.Image_sliderAdapter;
import com.techno71.fireservice.Model.CommentModel;
import com.techno71.fireservice.Model.Slider_Model;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.ArrayList;
import java.util.List;

public class Admin_MarkDetailsActivity extends AppCompatActivity {

    private  boolean isExpands=false;
    private RecyclerView recyclerView;
    private LinearLayout linearLayout_toggle,linearlayout_latlong;

    private CommentAdapter commentAdapter;

    private List<CommentModel>modelList=new ArrayList<>();

    private List<Slider_Model>slider_modelList=new ArrayList<>();

    private Image_sliderAdapter image_sliderAdapter;

    private ArrayList<String> arrayList_spinnerColor = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter_spinnerColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_mark_details);

        linearLayout_toggle=(LinearLayout)findViewById(R.id.layout_toggle);

        linearlayout_latlong=(LinearLayout)findViewById(R.id.linearlayout_latlong);

        Spinner spinner= (Spinner)findViewById(R.id.spinner_admincolor);
        arrayList_spinnerColor.clear();
        arrayList_spinnerColor.add("Select----Colors---");
        arrayList_spinnerColor.add("RED");
        //arrayList_spinnerColor.add("GREEN");
        arrayList_spinnerColor.add("YELLOW");

        arrayAdapter_spinnerColors = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1,arrayList_spinnerColor);
        spinner.setAdapter(arrayAdapter_spinnerColors);

        SliderView sliderView = findViewById(R.id.image_slider_homepage);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();

        slider_modelList.add(new Slider_Model( R.drawable.thumbnail));
        slider_modelList.add(new Slider_Model( R.drawable.thum2));
        slider_modelList.add(new Slider_Model( R.drawable.thum2));
        slider_modelList.add(new Slider_Model( R.drawable.thumbnail));
        slider_modelList.add(new Slider_Model( R.drawable.thum2));
        slider_modelList.add(new Slider_Model( R.drawable.thumbnail));

        image_sliderAdapter =new Image_sliderAdapter(getApplicationContext(),slider_modelList);
        sliderView.setSliderAdapter(image_sliderAdapter);

        recyclerView=(RecyclerView)findViewById(R.id.recycelveiwComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        modelList.add(new CommentModel("1",R.drawable.gg,"এই  প্রতিষ্ঠা রাসয়নিক পর্দাথ আছে","",""));
        modelList.add(new CommentModel("1",R.drawable.gg,"এই  প্রতিষ্ঠা  গ্যাস  আছে ","",""));
        modelList.add(new CommentModel("1",R.drawable.gg,"এই  প্রতিষ্ঠা রাসয়নিক পর্দাথ আছে","",""));
        modelList.add(new CommentModel("1",R.drawable.gg," এই  প্রতিষ্ঠা অনেক আগে থাকে রাসয়নিক পর্দাথ আছে ","",""));
        modelList.add(new CommentModel("1",R.drawable.gg,"প্রতিষ্ঠা এলকোহল   পর্দাথ  আছে ","",""));
        modelList.add(new CommentModel("1",R.drawable.gg,"এই  প্রতিষ্ঠা রাসয়নিক পর্দাথ আছে","",""));
     //   modelList.add(new CommentModel("1",R.drawable.gg,"To make a circular ImageView add CircularImageView in your layout","",""));

        commentAdapter=new CommentAdapter(getApplicationContext(),modelList);
        recyclerView.setAdapter(commentAdapter);

      /*  recyclerView.setVisibility(isExpands? View.VISIBLE:View.GONE);
        linearLayout_toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isExpands==false){

                    recyclerView.setVisibility(View.VISIBLE);
                    linearlayout_latlong.setVisibility(View.GONE);
                    isExpands=true;
                }else{
                    recyclerView.setVisibility(View.GONE);
                    linearlayout_latlong.setVisibility(View.VISIBLE);
                    isExpands=false;
                }
            }
        });*/
    }

    @Override
    protected void onResume() {
        super.onResume();

        language_model language=new language_model(this);
        language.loadLanguage();
    }
}
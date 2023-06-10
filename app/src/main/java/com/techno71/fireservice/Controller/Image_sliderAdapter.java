package com.techno71.fireservice.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.techno71.fireservice.Model.Slider_Model;
import com.techno71.fireservice.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Image_sliderAdapter extends SliderViewAdapter<Image_sliderAdapter.MyviewHolder> {

    private Context context;
    private List<Slider_Model>slider_shopModels;


    public Image_sliderAdapter(Context context, List<Slider_Model> slider_shopModels) {
        this.context = context;
        this.slider_shopModels = slider_shopModels;
    }

    @Override
    public MyviewHolder onCreateViewHolder(ViewGroup parent) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

       View view= layoutInflater.inflate(R.layout.imageslider_layout,parent,false);

      return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyviewHolder viewHolder, int position) {

        Slider_Model item=slider_shopModels.get(position);

      Picasso.get().load(item.getImage()).into(viewHolder.imageView_slider);
    }

    @Override
    public int getCount() {
        return slider_shopModels.size();
    }

    public  class  MyviewHolder extends ViewHolder{

        private ImageView imageView_slider;

        public MyviewHolder(View itemView) {
            super(itemView);

           imageView_slider=(ImageView)itemView.findViewById(R.id.image_slider);
        }
    }
}

package com.techno71.fireservice.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techno71.fireservice.Model.news_model;
import com.techno71.fireservice.R;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

   private Context context;

    private  boolean isExpands=false;

    private List<news_model>modelList;

    private  static  onItemClickLisiner clickLisiner;

    private static int listposition=-1;

    public NewsAdapter(Context context, List<news_model> modelList) {
        this.context = context;
        this.modelList = modelList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(context);

        View view= layoutInflater.inflate(R.layout.news_simple_layout,parent,false);

        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {

        news_model item_position=modelList.get(position);
        myViewHolder.textView_title.setText(context.getString(R.string.news_title)+""+item_position.getTitle());
        myViewHolder.textView_uploadDate.setText(context.getString(R.string.news_uploadDate)+""+item_position.getUpdateDate());

        Picasso.get().load(item_position.getImage()).into(myViewHolder.imageView);

        setAnimiton(myViewHolder.itemView,position);
    }

    void setAnimiton(View viewAnimition,int position) {

        if (position > listposition) {

            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            animation.setDuration(1000);
            viewAnimition.startAnimation(animation);
            listposition = position;
        }
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView textView_title,textView_uploadDate;

        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            textView_title=itemView.findViewById(R.id.text_newstitleName);
            textView_uploadDate=itemView.findViewById(R.id.text_newsUpdate);

            imageView=itemView.findViewById(R.id.news_Image);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            clickLisiner.OnClick_Lisiner(position);
        }
    }

    public interface  onItemClickLisiner{
        void OnClick_Lisiner(int position);
    }

    public  void setOnItemClick(onItemClickLisiner clickLisiner){
        this. clickLisiner=clickLisiner;
    }
}

package com.techno71.fireservice.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techno71.fireservice.Model.CommentModel;
import com.techno71.fireservice.R;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

   private Context context;

   private List<CommentModel>modelList;

   public CommentAdapter(Context context, List<CommentModel> modelList) {
      this.context = context;
      this.modelList = modelList;
   }

   @Override
   public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      LayoutInflater layoutInflater=LayoutInflater.from(context);

     View view= layoutInflater.inflate(R.layout.comment_layout,parent,false);

      return new MyViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

      CommentModel item= modelList.get(position);

      Picasso.get().load(item.getUserImage()).into(holder.circleImageView);

      holder.textView_commentStatus.setText(""+item.getCommentStatus());
   }

   @Override
   public int getItemCount() {
      return modelList.size();
   }

   class MyViewHolder extends RecyclerView.ViewHolder{

      private CircularImageView circleImageView;

      private TextView textView_status,textView_commentStatus;

      public MyViewHolder(@NonNull View itemView) {
         super(itemView);

         circleImageView=(CircularImageView)itemView.findViewById(R.id.text_userCommentImage);

         textView_commentStatus=(TextView) itemView.findViewById(R.id.text_comment_status);
      }
   }
}

package com.techno71.fireservice.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techno71.fireservice.Model.commentShowModel;
import com.techno71.fireservice.R;

import java.util.List;

 public class CommentShowAdapter extends RecyclerView.Adapter<CommentShowAdapter.MyviewHolder> {

    private List<commentShowModel>commentShowModelList;

    private Context context;

    public CommentShowAdapter(List<commentShowModel> commentShowModelList, Context context) {
        this.commentShowModelList = commentShowModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater=LayoutInflater.from(context);

      View view=  layoutInflater.inflate(R.layout.my_comment,parent,false);

        return new MyviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, int position) {

        commentShowModel item=commentShowModelList.get(position);

        myviewHolder.textView_title.setText("Title:"+item.getTitle());
        myviewHolder.textView_comment.setText("Comment: "+item.getComment());
        myviewHolder.textView_floor.setText("Floor:"+item.getFloor());
        myviewHolder.textViewDate.setText("Date:"+item.getCreated_at());
        myviewHolder.textView_alert_tag.setText("Alert Tag:"+item.getAlert_tag());

    }

    @Override
    public int getItemCount() {
        return commentShowModelList.size();
    }

    class MyviewHolder extends RecyclerView.ViewHolder{

        private TextView textView_title,textView_floor,
                textView_comment,textView_alert_tag,textViewDate;

        public MyviewHolder(@NonNull View itemView) {
            super(itemView);

            textView_title=(TextView)itemView.findViewById(R.id.comment_title);
            textView_floor=(TextView)itemView.findViewById(R.id.comment_floor);
            textView_comment=(TextView)itemView.findViewById(R.id.comment_text);
            textView_alert_tag=(TextView)itemView.findViewById(R.id.comment_alert_tag);
            textViewDate=(TextView)itemView.findViewById(R.id.comment_created_at);
        }
    }

}

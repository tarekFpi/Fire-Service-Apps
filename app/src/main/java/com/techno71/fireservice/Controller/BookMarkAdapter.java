package com.techno71.fireservice.Controller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techno71.fireservice.Model.BookMarkShow;
import com.techno71.fireservice.R;

import java.util.List;

public   class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.MyviewHolder> {

   private List<BookMarkShow>markShowList;

   private Context context;

   private static int listposition=-1;

   private  static  onItemClickLisiner clickLisiner;

   public BookMarkAdapter(List<BookMarkShow> markShowList, Context context) {
      this.markShowList = markShowList;
      this.context = context;
   }

   @NonNull
   @Override
   public MyviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      LayoutInflater layoutInflater=LayoutInflater.from(context);

     View view= layoutInflater.inflate(R.layout.book_mark,parent,false);

      return new MyviewHolder(view);
   }

   @SuppressLint("ResourceAsColor")
   @Override
   public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, int position) {

      BookMarkShow item=markShowList.get(position);

      myviewHolder.textView_name.setText("Name:"+item.getConpanyName());
      myviewHolder.textView_location.setText("Location:"+item.getAddress());
      myviewHolder.textView_approvDate.setText("Approv Date:"+item.getLicense_approved_date());
      myviewHolder.textView_RenewDate.setText("Renew Date:"+item.getLicense_renew_date());
      myviewHolder.textView_Division.setText("Division :"+item.getDivision());
      myviewHolder.textViewAlert.setText("Tag:"+item.getAlert_tag());

      if(!item.getStatus().contains("1")){

        myviewHolder.textView_km.setText("Processing");
      }else{

         myviewHolder.textView_km.setText("verifying");
         myviewHolder.textView_km.setTextColor(R.color.Yellow);
      }

      Picasso.get().load(item.getImage()).into(myviewHolder.imageView);

      setAnimiton(myviewHolder.itemView,position);
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
      return markShowList.size();
   }

   class MyviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

      private TextView textView_name,textView_location,textView_km,
              textView_approvDate,
              textView_RenewDate,textView_Division,textViewAlert;

      private ImageView imageView;

      public MyviewHolder(@NonNull View itemView) {
         super(itemView);

         imageView=(ImageView)itemView.findViewById(R.id.book_markImage);

         textView_name=(TextView)itemView.findViewById(R.id.text_markoffName);

         textView_location=(TextView)itemView.findViewById(R.id.text_bookmarkLocation);

         textView_km=(TextView)itemView.findViewById(R.id.text_markoff_status);

         textView_approvDate=(TextView)itemView.findViewById(R.id.text_markoff_license_approved_date);

         textView_RenewDate=(TextView)itemView.findViewById(R.id.text_markoff_license_renew_date);

         textView_Division=(TextView)itemView.findViewById(R.id.text_bookmarkdivision);

         textViewAlert=(TextView)itemView.findViewById(R.id.text_bookmarkAlertTag);

         itemView.setOnClickListener(this);
      }

      @Override
      public void onClick(View view) {
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

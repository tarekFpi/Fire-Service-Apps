package com.techno71.fireservice.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.annotations.NotNull;
import com.techno71.fireservice.Model.AllLocations_WiseStorage_Comments_3;
import com.techno71.fireservice.R;
import com.techno71.fireservice.databinding.ModelLocationCommentsBinding;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adepter_Location_Comments extends RecyclerView.Adapter<Adepter_Location_Comments.ViewHolderClass> implements Filterable {
    private boolean testclick = false;
    private List<AllLocations_WiseStorage_Comments_3> model_mytrnsction;
    private List<AllLocations_WiseStorage_Comments_3> model_mytrnsction_Filter;
    private Activity activity;

    public Adepter_Location_Comments(Activity activity, List<AllLocations_WiseStorage_Comments_3> model_mytrnsction) {
        this.model_mytrnsction = model_mytrnsction;
        this.model_mytrnsction_Filter = model_mytrnsction;
        this.activity = activity;

    }

    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

        ModelLocationCommentsBinding binding=ModelLocationCommentsBinding.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_location_comments, parent, false));

        return new ViewHolderClass(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderClass holder, @SuppressLint("RecyclerView") int position) {

        AllLocations_WiseStorage_Comments_3 model = model_mytrnsction_Filter.get(position);

        holder.showData(model, position);

    }

    @Override
    public int getItemCount() {

     return model_mytrnsction_Filter.size();
    }

    @Override
    public Filter getFilter() {


        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String cracter = constraint.toString();
                if (cracter.isEmpty()) {
                    model_mytrnsction_Filter = model_mytrnsction;
                } else {
                    List<AllLocations_WiseStorage_Comments_3> filterlist = new ArrayList<>();
                    for (AllLocations_WiseStorage_Comments_3 row : model_mytrnsction) {

                    }
                    model_mytrnsction_Filter = filterlist;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = model_mytrnsction_Filter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                model_mytrnsction_Filter = (ArrayList<AllLocations_WiseStorage_Comments_3>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public static class ViewHolderClass extends RecyclerView.ViewHolder {

        private TextView  email, joiningDate,nameTv,amountTv;
        private CircleImageView parsonlogo;
		private DecimalFormat decimal=new DecimalFormat("#.##");
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyy, hh:mm a ", Locale.getDefault());
        private ModelLocationCommentsBinding binding;
        private ViewHolderClass(@NonNull ModelLocationCommentsBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;

        }


        @SuppressLint("SetTextI18n")
        public void showData(AllLocations_WiseStorage_Comments_3 model, int position) {
            if (model.getAlert_tag().toString().toLowerCase().equals("red")){
                binding.itemView.setCardBackgroundColor(Color.RED);
            }
            if (model.getAlert_tag().toString().toLowerCase().equals("green")){
                binding.itemView.setCardBackgroundColor(Color.GREEN);
            }
            if (model.getAlert_tag().toString().toLowerCase().equals("yellow")){
                binding.itemView.setCardBackgroundColor(Color.YELLOW);
            }
            binding.storeIdTv.setText(""+model.getStore_id());
            binding.commentTv.setText(""+model.getComment());
        }
    }
}

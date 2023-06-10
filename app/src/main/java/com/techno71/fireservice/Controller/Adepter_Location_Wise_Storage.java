package com.techno71.fireservice.Controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.database.annotations.NotNull;
import com.techno71.fireservice.ApiService.Main_Url;
import com.techno71.fireservice.Model.AllLocations_WiseStorage_2;
import com.techno71.fireservice.Model.AllLocations_WiseStorage_Comments_3;
import com.techno71.fireservice.R;
import com.techno71.fireservice.View.FireMapActivity;
import com.techno71.fireservice.databinding.ModelLocationWiseStorageBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Adepter_Location_Wise_Storage extends RecyclerView.Adapter<Adepter_Location_Wise_Storage.ViewHolderClass> implements Filterable {
    private final SharedPreferences sharedPreferences_type;
    private final String access_token;
    private final ProgressDialog pDialog;
    private boolean testclick = false;
    private List<AllLocations_WiseStorage_2> model_mytrnsction;
    private List<AllLocations_WiseStorage_2> model_mytrnsction_Filter;
    private Activity activity;

    public Adepter_Location_Wise_Storage(Activity activity, List<AllLocations_WiseStorage_2> model_mytrnsction) {
        this.model_mytrnsction = model_mytrnsction;
        this.model_mytrnsction_Filter = model_mytrnsction;
        this.activity = activity;
        sharedPreferences_type = activity.getSharedPreferences("com.techno71.fireservice", Context.MODE_PRIVATE);
        access_token = sharedPreferences_type.getString("access_token", "default_access_token001");
        pDialog = new ProgressDialog(activity);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

    }

    @Override
    public ViewHolderClass onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {

       ModelLocationWiseStorageBinding binding=ModelLocationWiseStorageBinding.bind(LayoutInflater.from(parent.getContext()).inflate(R.layout.model_location_wise_storage, parent, false));


        return new ViewHolderClass(binding);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolderClass holder, @SuppressLint("RecyclerView") int position) {

        AllLocations_WiseStorage_2 model = model_mytrnsction_Filter.get(position);

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
                    List<AllLocations_WiseStorage_2> filterlist = new ArrayList<>();
                    for (AllLocations_WiseStorage_2 row : model_mytrnsction) {

                    }
                    model_mytrnsction_Filter = filterlist;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = model_mytrnsction_Filter;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                model_mytrnsction_Filter = (ArrayList<AllLocations_WiseStorage_2>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private String showAllFiremanCommentLocation_3 = Main_Url.ROOT_URL + "api/storage-comment-show-by-fireman";

    public class ViewHolderClass extends RecyclerView.ViewHolder {


        private TextView  email, joiningDate,nameTv,amountTv;
        private CircleImageView parsonlogo;
		private DecimalFormat decimal=new DecimalFormat("#.##");
        private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyy, hh:mm a ", Locale.getDefault());
        private ModelLocationWiseStorageBinding binding;
        private ViewHolderClass(@NonNull ModelLocationWiseStorageBinding itemView) {
            super(itemView.getRoot());
            binding=itemView;

        }

        private List<AllLocations_WiseStorage_Comments_3> allLocationsWiseStorageComments3s;
        private com.techno71.fireservice.Controller.Adepter_Location_Comments adepter_location_comments;

        @SuppressLint("SetTextI18n")
        public void showData(AllLocations_WiseStorage_2 model, int position) {

            Glide.with(activity).load(Main_Url.ROOT_URL+model.getStorage_img()).into(binding.companyImg);
            binding.idTv.setText(""+model.getId());
            binding.floorTv.setText(""+model.getFloor());
            binding.companyNameTv.setText(""+model.getCompany_name());
            binding.companyOwnerTv.setText(""+model.getCompany_owner());
            binding.licenseApprovedDateTv.setText(""+model.getLicense_approved_date());
            binding.licenseRenewDateTv.setText(""+model.getLicense_renew_date());
            binding.addressTv.setText(""+model.getAddress());
            binding.thanaTv.setText(""+model.getThana());
            binding.districTv.setText(""+model.getDistric());
            binding.divisionTv.setText(""+model.getDivision());
            binding.companyTypeTv.setText(""+model.getCompany_type());
            binding.commpanyDetailsTv.setText(""+model.getCompany_detils());

            binding.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FireMapActivity.vibrator(activity);
                    pDialog.show();
                    showAllFiremanCommentLocation_3(access_token,""+model.getId());
                }
            });

        }
        public void showAllFiremanCommentLocation_3(String axcess_token,String store_id) {
            pDialog.show();

            allLocationsWiseStorageComments3s=new ArrayList<>();
            adepter_location_comments=new com.techno71.fireservice.Controller.Adepter_Location_Comments(activity,allLocationsWiseStorageComments3s);
            RequestQueue requestQueue = Volley.newRequestQueue(activity);

        /*
        allLocationsWiseStorage2s= new Gson().fromJson(
                jsonArray.toString(),
                new TypeToken<List<Adepter_Location_Wise_Storage>>(){}.getType();
        */

            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, showAllFiremanCommentLocation_3,new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    try {
                        Log.d("bbbbbbb",""+response);

                        JSONObject jsonObjectMain=new JSONObject(response);
                        JSONArray jsonArray= jsonObjectMain.getJSONArray("UserComment");
                        if (jsonObjectMain.getString("message").equals("Data is available!")){
                            for (int i=0;i<jsonArray.length();i++){
                                AllLocations_WiseStorage_Comments_3 storage_2=new AllLocations_WiseStorage_Comments_3(
                                        ""+jsonArray.getJSONObject(i).getString("id"),
                                        ""+jsonArray.getJSONObject(i).getString("store_id"),
                                        ""+jsonArray.getJSONObject(i).getString("comment"),
                                        ""+jsonArray.getJSONObject(i).getString("alert_tag")
                                );
                                allLocationsWiseStorageComments3s.add(storage_2);

                            }

                            bottomShetDialog();
                        }else {
                            pDialog.dismiss();
                            Toast.makeText(activity, "No Data Found", Toast.LENGTH_SHORT).show();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        pDialog.dismiss();
                        Toast.makeText(activity, ""+e, Toast.LENGTH_SHORT).show();

                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pDialog.dismiss();
                    Toast.makeText(activity, ""+error, Toast.LENGTH_SHORT).show();

                }
            }){
                @Override
                protected Map<String, String> getParams() {

                    Map<String, String> hashMap = new HashMap<String, String>();
                    hashMap.put("security_error","tec71");
                    hashMap.put("axcess_token",""+axcess_token);
                    hashMap.put("store_id",""+store_id);

                    return hashMap;
                }
            };
            requestQueue.add(stringRequest1);

        }

        private void bottomShetDialog(){
            pDialog.dismiss();

            BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(activity);
            bottomSheetDialog.setContentView(R.layout.dialog_location_fiar);
            RecyclerView recyclerView=bottomSheetDialog.findViewById(R.id.recyclerView);
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(adepter_location_comments);
            bottomSheetDialog.show();
        }
    }


}

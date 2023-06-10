package com.techno71.fireservice.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.techno71.fireservice.Model.language_model;
import com.techno71.fireservice.R;

public class HelpCenter_Fragment extends Fragment {

    private ImageView imageViewCall;

    private LottieAnimationView lottieAnimationView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HelpCenter_Fragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static HelpCenter_Fragment newInstance(String param1, String param2) {
        HelpCenter_Fragment fragment = new HelpCenter_Fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_help_center_, container, false);

         imageViewCall=(ImageView)view.findViewById(R.id.image_helpCall);

        language_model language=new language_model(getContext());
        language.loadLanguage();

         lottieAnimationView=(LottieAnimationView)view.findViewById(R.id.animation_view_call);

         lottieAnimationView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 String phone = "01713304386".toString().trim();
                 Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                 startActivity(intent);
             }
         });
        return view;
    }


}
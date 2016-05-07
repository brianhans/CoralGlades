package com.brianhans.coralglades.fragments;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brianhans.coralglades.R;
import com.brianhans.coralglades.views.TouchImageView;
import com.bumptech.glide.Glide;


public class PictureView extends Fragment
{
    public static final String ITEM_NUMBER = "item_number";


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Log.d("PictureView", "created");
        String type = getArguments().getString("type", "nothing");
        View view = inflater.inflate(R.layout.picture_fragment, container, false);
        View l = view.findViewById(R.id.linearLayout);
        TouchImageView img = (TouchImageView) view.findViewById(R.id.touchImageView);
        img.setMaxZoom(4f);

        if(type.equals("map")) {
            img.setImageResource(R.drawable.schoolmap);
            view.setBackgroundColor(Color.WHITE);
        }else if(type.equals("calendar"))
        {
            img.setImageResource(R.drawable.calendar);
        }else{
            Uri uri = Uri.parse(type);
            Glide.with(getContext()).load(uri).into(img);
            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("Image", "Closed");
                    FragmentManager fragmentManager = getFragmentManager();
                    fragmentManager.beginTransaction().remove(fragmentManager.findFragmentById(R.id.photoViewer)).commit();
                }
            });
        }

        Activity a = getActivity();
        if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }

}

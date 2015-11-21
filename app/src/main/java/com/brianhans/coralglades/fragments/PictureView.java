package com.brianhans.coralglades.fragments;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brianhans.coralglades.R;
import com.brianhans.coralglades.views.TouchImageView;


public class PictureView extends Fragment
{
    public static final String ITEM_NUMBER = "item_number";




    public PictureView()
    {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        String type = getArguments().getString("type", "nothing");
        View view = inflater.inflate(R.layout.picture_fragment, container, false);
        View l = view.findViewById(R.id.linearLayout);
        TouchImageView img = (TouchImageView) view.findViewById(R.id.touchImageView);
        img.setMaxZoom(4f);

        if(type.equals("map")) {
            img.setImageResource(R.drawable.schoolmap);
        }else
        {
            img.setImageResource(R.drawable.calendar);
        }

        Activity a = getActivity();
        if(a != null) a.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return view;
    }

}

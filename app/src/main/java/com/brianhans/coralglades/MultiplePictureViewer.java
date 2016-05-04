package com.brianhans.coralglades;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brianhans.coralglades.R;
import com.brianhans.coralglades.fragments.PictureView;

/**
 * Created by Brian on 4/26/2016.
 */
public class MultiplePictureViewer extends Fragment {

    private ViewPager pager;
    private PagerAdapter pagerAdapter;

    private String[] urls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        urls = getArguments().getStringArray("Urls");
        View view = inflater.inflate(R.layout.pager, container, false);
        pager = (ViewPager) view.findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        pager.setAdapter(pagerAdapter);
        return view;
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            bundle.putString("type", urls[position]);
            PictureView frag = new PictureView();
            frag.setArguments(bundle);
            return frag;
        }

        @Override
        public int getCount() {
            return urls.length;
        }

    }


}

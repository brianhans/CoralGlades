package com.brianhans.coralglades.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.brianhans.coralglades.R;

/**
 * Created by Brian on 8/27/16.
 */
public class BellFragment extends Fragment {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;

    private String[] titles = {"Regular", "Early Release", "PSD"};
    private String[] aday = {"1st Period: 7:40 AM - 9:10 AM\n2nd Period: 9:20 AM - 10:50 AM \n3rd Period: 11:00 AM - 1:00 PM\n4th Period: 1:10 PM - 2:40 PM",
            "1st Period:  7:40AM – 8:40AM\n2nd Period:  8:45AM – 9:45AM\n3rd Period:  9:50AM – 11:35AM\n4th Period:  11:40AM – 12:40PM",
            "1st Period:  7:40AM – 8:35AM\n2nd Period:  8:45AM – 9:40AM\n3rd Period:  9:45AM – 10:40AM\n4th Period:  10:45AM – 11:40AM"};
    private String[] bday = {"5th Period: 7:40 AM - 9:10 AM\n6th Period: 9:20 AM - 10:50 AM \n7th Period: 11:00 AM - 1:00 PM\n8th Period: 1:10 PM - 2:40 PM",
            "5th Period:  7:40AM – 8:40AM\n6th Period:  8:45AM – 9:45AM\n7th Period:  9:50AM – 11:35AM\n8th Period:  11:40AM – 12:40PM",
            "5th Period:  7:40AM – 8:35AM\n6th Period:  8:45AM – 9:40AM\n7th Period:  9:45AM – 10:40AM\n8th Period:  10:45AM – 11:40AM"};
    private String[] lunch = {"A Lunch 10:50 AM - 11:25 AM (11:30 AM - 1:00 PM)\nB Lunch 12:30 PM - 1:05 PM (11:00 AM - 12:30 PM)",
            "A Lunch 9:50AM – 10:30AM (10:35AM – 11:35AM)\nB Lunch 10:55AM – 11:35AM (9:50AM – 10:50AM)", "A “Grab and Go” lunch will be available in the cafeteria at 11:40AM"};


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bell_fragment, container, false);

        getActivity().findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());
        mViewPager = (ViewPager) view.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    private class SectionsPagerAdapter extends FragmentPagerAdapter{

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle args = new Bundle();
            args.putString("aday", aday[position]);
            args.putString("bday", bday[position]);
            args.putString("lunch", lunch[position]);
            TextFragment fragment = new TextFragment();
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }

    public static class TextFragment extends Fragment{

        private String aday;
        private String bday;
        private String lunch;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.bell_text_fragment, container, false);
            Bundle args = getArguments();
            ((TextView)view.findViewById(R.id.aday)).setText(args.getString("aday"));
            ((TextView)view.findViewById(R.id.bday)).setText(args.getString("bday"));
            ((TextView)view.findViewById(R.id.lunch)).setText(args.getString("lunch"));
            return view;
        }

    }

}

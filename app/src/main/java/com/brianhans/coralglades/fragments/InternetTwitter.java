package com.brianhans.coralglades.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.brianhans.coralglades.R;

public class InternetTwitter extends Fragment {


    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private SharedPreferences pref;
    private int size;
    private String[] users;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        users = new String[pref.getStringSet("users", null).size()];
        pref.getStringSet("users", null).toArray(users);
        size = users.length;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_internet_twitter, container, false);

        getActivity().findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) v.findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mTabLayout = (TabLayout) getActivity().findViewById(R.id.tab_layout);
        mTabLayout.setupWithViewPager(mViewPager);

        return v;
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = new Internet();
            Bundle args = new Bundle();
            args.putString("site", users[position]);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public int getCount() {
            return size;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return "@" + users[position];

        }
    }


}

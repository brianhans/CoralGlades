package com.brianhans.coralglades;


import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

//import com.brianhans.coralglades.fragments.internet;
//import com.brianhans.coralglades.fragments.home;
//import com.brianhans.coralglades.fragments.pictureView;

import java.util.ArrayList;


/*

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private LinearLayout mDrawer;
    private ListView mSettings;
    public static int toolbarHeight;

    private CharSequence mTitle;
    private String[] titles;
    private TypedArray icons;

    private ArrayList<NavDrawerItem> mNavItems;
    private ArrayList<NavDrawerItem> mSettingNavItems;
    private NavDrawerListAdapter adapter;
    private NavDrawerListAdapter settingsAdapter;

    public static int userAmount;
    private int amountOfUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarHeight = toolbar.getHeight();
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }

        amountOfUsers = pref.getInt("size", 0);


        mTitle = getTitle();
        titles = getResources().getStringArray(R.array.nav_items);
        icons = getResources().obtainTypedArray(R.array.nav_icons);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawer = (LinearLayout) findViewById(R.id.drawer);
        mSettings = (ListView) findViewById(R.id.settings_list);

        mNavItems = new ArrayList<NavDrawerItem>();
        mNavItems.add(new NavDrawerItem(titles[0], icons.getResourceId(0, -1)));
        mNavItems.add(new NavDrawerItem(titles[1], icons.getResourceId(1, -1)));
        mNavItems.add(new NavDrawerItem(titles[2], icons.getResourceId(2, -1)));
        mNavItems.add(new NavDrawerItem(titles[3], icons.getResourceId(3, -1)));
        mNavItems.add(new NavDrawerItem(titles[4], icons.getResourceId(4, -1)));
        mNavItems.add(new NavDrawerItem(titles[5], icons.getResourceId(5, -1)));

        mSettingNavItems = new ArrayList<>();
        mSettingNavItems.add(new NavDrawerItem("Settings", icons.getResourceId(6, -1)));
        mSettingNavItems.add(new NavDrawerItem("About", icons.getResourceId(7, -1)));

        icons.recycle();

        mDrawerList.setOnItemClickListener(new DrawerItemClickListener(0));
        mSettings.setOnItemClickListener(new DrawerItemClickListener(6));

        adapter = new NavDrawerListAdapter(getApplicationContext(), mNavItems, "list");
        settingsAdapter = new NavDrawerListAdapter(getApplicationContext(), mSettingNavItems, "settings");

        mDrawerList.setAdapter(adapter);
        mSettings.setAdapter(settingsAdapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open,R.string.drawer_close);
        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null && !pref.getBoolean("firstrun", true)) {
            selectItem(0);
        }else
        {
            Intent intent = new Intent(this, UserSelect.class);
            startActivity(intent);
        }
        mDrawerLayout.setDrawerShadow(R.drawable.card_shadow, GravityCompat.START);

    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        private int offset;
        TypedArray selectedIcon = getResources().obtainTypedArray(R.array.nav_selected);
        int[] selectedIcons = {selectedIcon.getResourceId(0, -1), selectedIcon.getResourceId(1, -1),
                selectedIcon.getResourceId(2, -1), selectedIcon.getResourceId(3, -1),
                selectedIcon.getResourceId(4, -1), selectedIcon.getResourceId(5, -1)};

        public DrawerItemClickListener(int offset)
        {
            super();
            this.offset = offset;
        }
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            setItemsNormal();

            if (!view.equals(mSettings.getChildAt(position)))
            {
                TextView textView = (TextView) view.findViewById(R.id.title);
                ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                imageView.setImageResource(selectedIcons[position]);
                int teal = getResources().getColor(R.color.colorPrimary);
                textView.setTextColor(teal);
            }

            selectItem(position + offset);
        }

        private void setItemsNormal()
        {
            for(int i = 0; i < mDrawerList.getChildCount(); i++)
            {
                View view = mDrawerList.getChildAt(i);
                ImageView imageView = (ImageView) view.findViewById(R.id.icon);
                TextView textView = (TextView) view.findViewById(R.id.title);
                imageView.setImageResource(mNavItems.get(i).getIcon());
                textView.setTextColor(Color.BLACK);
            }
        }


    }

    private void selectItem(int position) {
        Fragment fragment = null;
        Intent intent = null;
        Bundle args = new Bundle();
        boolean isFragment = true;

        switch (position) {
            case 0:
                fragment = new home();
                args.putInt("item_number", position);
                String[] users = getResources().getStringArray(R.array.users);
                userAmount = users.length;
                break;
            case 1:
                fragment = new pictureView();
                args.putInt(pictureView.ITEM_NUMBER, position);
                args.putString("type", "map");
                break;
            case 2:
                fragment = new pictureView();
                args.putInt(pictureView.ITEM_NUMBER, position);
                args.putString("type", "calendar");
                break;
            case 3:
                fragment = new internet();
                args.putInt(internet.ITEM_NUMBER, position);
                args.putString("site", "pinnacle");
                break;
            case 4:
                fragment = new internet();
                args.putInt(internet.ITEM_NUMBER, position);
                args.putString("site", "beep");
                break;
            case 5:
                fragment = new internet();
                args.putString("site", "vc");
                break;
            case 6:
                isFragment = false;
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
            case 7:
                isFragment = false;
                intent = new Intent(this, About.class);
                startActivity(intent);
                break;
        }
        if(isFragment) {
            fragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
            setTitle(titles[position]);
        }
        mDrawerLayout.closeDrawer(mDrawer);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.START | Gravity.LEFT)) {
            mDrawerLayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }

    public static Fragment newHomeFragment() {
        return new home();
    }

    public void setAmountOfUsers(int amount){
        this.amountOfUsers = amount;
    }

    public int getAmountOfUsers(){
        return this.amountOfUsers;
    }

}

*/

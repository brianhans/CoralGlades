package com.brianhans.coralglades;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.brianhans.coralglades.fragments.BellFragment;
import com.brianhans.coralglades.fragments.Home;
import com.brianhans.coralglades.fragments.Internet;
import com.brianhans.coralglades.fragments.PictureView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public final String TWITTERTAG = "twitter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(0).setChecked(true);
        Home home = new Home();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home, TWITTERTAG).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here
        int id = item.getItemId();
        findViewById(R.id.tab_layout).setVisibility(View.GONE);

        if (id == R.id.nav_home) {
            Home home = new Home();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, home, TWITTERTAG).commit();
        } else if (id == R.id.nav_map) {
            PictureView pictureView = new PictureView();
            Bundle args = new Bundle();
            args.putString("type", "map");
            pictureView.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pictureView).commit();
        } else if (id == R.id.nav_pinnacle) {
            Internet web = new Internet();
            Bundle args = new Bundle();
            args.putString("site", "pinnacle");
            web.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, web).commit();
        } else if (id == R.id.nav_beep) {
            Internet web = new Internet();
            Bundle args = new Bundle();
            args.putString("site", "beep");
            web.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, web).commit();
        } else if(id == R.id.nav_vc){
            Internet web = new Internet();
            Bundle args = new Bundle();
            args.putString("site", "vc");
            web.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, web).commit();
        }else if (id == R.id.nav_cal) {
            PictureView pictureView = new PictureView();
            Bundle args = new Bundle();
            args.putString("type", "calendar");
            pictureView.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, pictureView).commit();
        }else if(id == R.id.bell){
            BellFragment fragment = new BellFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
        else if (id == R.id.nav_about) {
            Intent intent = new Intent(this, About.class);
            startActivity(intent);
        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}

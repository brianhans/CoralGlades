package com.brianhans.coralglades;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.brianhans.coralglades.views.ObservableScrollView;
import com.brianhans.coralglades.views.ScrollViewListener;


/**
 * Created by Brian on 1/11/2015.
 */
public class About extends AppCompatActivity implements ScrollViewListener{

    ObservableScrollView scrollView;
    ImageView logo;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        logo = (ImageView) findViewById(R.id.logo);
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        scrollView.setScrollViewListener(this);

        if (savedInstanceState == null) {
            getSupportActionBar().setTitle("About");
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldX, int oldY) {
        int scrollY = scrollView.getScrollY();
        logo.setTranslationY(scrollY * 0.5f);
    }
}

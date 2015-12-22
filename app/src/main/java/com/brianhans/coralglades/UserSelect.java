package com.brianhans.coralglades;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.brianhans.coralglades.views.ObservableScrollView;
import com.brianhans.coralglades.views.ScrollViewListener;

import java.util.HashSet;

/**
 * Created by Brian on 11/29/2014.
 */
public class UserSelect extends AppCompatActivity implements ScrollViewListener {
    SharedPreferences.Editor editor;

    private ImageView school = null;
    private Activity activity = this;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.welcome_screen);

        //Sets up toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        if (savedInstanceState == null) {
            getSupportActionBar().setTitle("Welcome");
        }

        school = (ImageView) findViewById(R.id.imageView);
        ObservableScrollView scrollView = (ObservableScrollView) findViewById(R.id.scrollView);
        scrollView.setScrollViewListener(this);


        LayoutInflater inflater = getLayoutInflater();
        LinearLayout userList = (LinearLayout) findViewById(R.id.user_list);

        Resources res = getResources();
        final String[] account = res.getStringArray(R.array.username);
        final String[] name = res.getStringArray(R.array.name);
        final CheckBox[] checkBoxes = new CheckBox[account.length];
        checkBoxes[0] = (CheckBox) findViewById(R.id.principal_button);

        //Creates a checkbox for each user
        for (int i = 1; i < account.length; i++)
        {
            LinearLayout user_item = (LinearLayout) inflater.inflate(R.layout.welcome_card, null, false);
            checkBoxes[i] = (CheckBox) user_item.findViewById(R.id.checkBox_user);
            TextView tvName = (TextView) user_item.findViewById(R.id.name);
            TextView tvAccount = (TextView) user_item.findViewById(R.id.account);
            tvName.setText(name[i]);
            tvAccount.setText("@" + account[i]);
            userList.addView(user_item);
        }


        Button done = (Button) findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
                editor = prefs.edit();
                HashSet<String> users = new HashSet<>();
                for(int i = 0; i < account.length; i++)
                {
                    Log.d("checkbox", i + ": " + checkBoxes[i].isChecked());
                    if(checkBoxes[i].isChecked())
                    {
                        users.add(account[i]);
                    }
                }
                editor.putStringSet("users", users);
                for(String user : users){
                    Log.d("checkbox", user);
                }
                editor.putBoolean("firstrun", false);
                editor.commit();

                //Return to the main activity
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    //Creates a parallax effect for the picture
    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldX, int oldY) {
        int scrollY = scrollView.getScrollY();
        school.setTranslationY(scrollY * 0.5f);
    }

}

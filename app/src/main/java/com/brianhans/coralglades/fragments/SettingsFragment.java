package com.brianhans.coralglades.fragments;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import com.brianhans.coralglades.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by Brian on 1/17/2015.
 */
public class SettingsFragment extends PreferenceFragment {

    private Resources res;
    private String[] name;
    private String[] account;
    private List<String> users;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.settings_pref);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        res = getActivity().getResources();
        name = res.getStringArray(R.array.name);
        account = res.getStringArray(R.array.username);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        users = new ArrayList<>(pref.getStringSet("users", null));
        PreferenceCategory following = (PreferenceCategory) findPreference("following");

        for (int i = 0; i < account.length; i++) {
            CheckBoxPreference checkBoxPreference = new CheckBoxPreference(getActivity());
            checkBoxPreference.setDefaultValue(false);
            if (users.contains(account[i]))checkBoxPreference.setChecked(true);
            checkBoxPreference.setKey(account[i]);
            checkBoxPreference.setTitle(name[i]);
            following.addPreference(checkBoxPreference);
        }
    }

    @Override
    public void onPause() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor editor = pref.edit();

        HashSet<String> selected = new HashSet<>();
        for (int i = 0; i < account.length; i++) {
            CheckBoxPreference boxPreference = (CheckBoxPreference) findPreference(account[i]);
            if (boxPreference.isChecked()) {
                selected.add(account[i]);
            }
        }

        editor.putStringSet("users", selected);

        editor.commit();

        super.onPause();
    }
}

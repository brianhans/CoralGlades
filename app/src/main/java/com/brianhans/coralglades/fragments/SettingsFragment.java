package com.brianhans.coralglades.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.View;

import com.brianhans.coralglades.MainActivity;
import com.brianhans.coralglades.R;

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
    private CheckBoxPreference pictures;
    private CheckBoxPreference internet;
    private boolean initalWebpage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_pref);
        pictures = (CheckBoxPreference) findPreference("pictures");
        internet = (CheckBoxPreference) findPreference("webpage");

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        res = getActivity().getResources();
        name = res.getStringArray(R.array.name);
        account = res.getStringArray(R.array.username);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        Set<String> users = pref.getStringSet("users", null);
        PreferenceCategory following = (PreferenceCategory) findPreference("following");

        for (int i = 0; i < account.length; i++) {
            CheckBoxPreference checkBoxPreference = new CheckBoxPreference(getActivity());
            checkBoxPreference.setDefaultValue(false);
            checkBoxPreference.setKey(account[i]);
            checkBoxPreference.setTitle(name[i]);
            following.addPreference(checkBoxPreference);

            if (users.contains(account[i])){
                checkBoxPreference.setChecked(true);
            }
        }

        initalWebpage = pref.getBoolean("webpage", false);

        internet.setChecked(pref.getBoolean("webpage", false));
        pictures.setChecked(pref.getBoolean("pictures", true));

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
        editor.putBoolean("pictures", pictures.isChecked());
        editor.putBoolean("webpage", internet.isChecked());


        editor.commit();

        if (initalWebpage != internet.isChecked()) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }

        super.onPause();
    }
}

package com.brianhans.coralglades.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.brianhans.coralglades.CustomComparator;
import com.brianhans.coralglades.R;
import com.brianhans.coralglades.CustomRecycleAdapter;
import com.brianhans.coralglades.UserSelect;
import com.brianhans.coralglades.views.InternetTwitter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * Created by Brian on 10/24/2015.
 */
public class Home extends Fragment {

    private ArrayList<String> accounts;
    private ProgressBar loadingSpinner;
    private RecyclerView cardHolder;
    private CustomRecycleAdapter recycleAdapter;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        context = getContext();
        loadingSpinner = (ProgressBar) getView().findViewById(R.id.loading_spinner);
        cardHolder = (RecyclerView) getView().findViewById(R.id.card_holder);
        cardHolder.setLayoutManager(new LinearLayoutManager(context));
        cardHolder.setHasFixedSize(true);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean firstRun = pref.getBoolean("firstrun", true);

        //Runs user select if the program is run for the first time.
        if (firstRun) {
            Intent intent = new Intent(getActivity(), UserSelect.class);
            startActivity(intent);
        }else{
            accounts = new ArrayList(pref.getStringSet("users", null));
            if(false){
                GetUserTimeline getTweets = new GetUserTimeline();
                getTweets.execute(accounts);
            }else{
                InternetTwitter fragment = new InternetTwitter();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }
        }


    }

    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }



    private class GetUserTimeline extends AsyncTask<List<String>, Void, List<Status>> {

        final static String CONSUMER_KEY = "T6rCfkVNRlbZyaaT4VEogAv9C";
        final static String CONSUMER_SECRET = "pMK5pKidLZJCjxwFeP3G09pspqVWR7hmX3eahLe0URhLZbD4Mv";
        final static String ACCESS_TOKEN = "1239035221-1Ti1dwtyCCkd7hbl9PLLt8I5WEWlHYIwu6qJuDm";
        final static String ACCESS_SECRET = "OB1XrZCxWUK17EIIuTZ0QsERFWCK3zwgTF9detGALssvI";

        List<String> users = new ArrayList<>();


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("View", getView().toString());
            ProgressBar loadingSpinner = (ProgressBar) getView().findViewById(R.id.loading_spinner);
            loadingSpinner.setVisibility(View.VISIBLE);
        }

        protected List<twitter4j.Status> doInBackground(List<String>... params) {
            users = params[0];

            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true);
            cb.setOAuthConsumerKey(CONSUMER_KEY);
            cb.setOAuthConsumerSecret(CONSUMER_SECRET);
            cb.setOAuthAccessToken(ACCESS_TOKEN);
            cb.setOAuthAccessTokenSecret(ACCESS_SECRET);

            Twitter twitter = new TwitterFactory(cb.build()).getInstance();

            try {
                Log.d("TEST", "try");
                List<List<twitter4j.Status>> tweetList = new ArrayList<>();
                Log.d("limit", "Rate Limit: " + twitter.getUserTimeline().getRateLimitStatus().getRemaining());

                //Check if there are any calls to the Twitter API remaining
                if (twitter.getUserTimeline().getRateLimitStatus().getRemaining() == 0) {
                    return null;
                }
                for (String user : users) {
                    List<twitter4j.Status> tweets = twitter.getUserTimeline(user);
                    tweetList.add(tweets);
                }

                List<twitter4j.Status> sortedTweets = sortTweets(tweetList);
                return sortedTweets;

            } catch (TwitterException e) {
                e.printStackTrace();
                return null;
            }


        }

        protected void onPostExecute(List<twitter4j.Status> stats) {

            loadingSpinner.setVisibility(View.GONE);
            if (stats == null) {
                //Displays web version of twitter feed
            } else {
                //Adds Cards to screen
                recycleAdapter = new CustomRecycleAdapter(context, stats);
                cardHolder.setAdapter(recycleAdapter);
            }
        }

        public List<twitter4j.Status> sortTweets(List<List<twitter4j.Status>> tweets) {
            List<twitter4j.Status> sortedTweets = new ArrayList<>();

            //Merges all separate tweets into one list
            for (List<twitter4j.Status> tweetList : tweets) {
                for (twitter4j.Status tweet : tweetList) {
                    sortedTweets.add(tweet);
                }
            }
            Collections.sort(sortedTweets, new CustomComparator());

            return sortedTweets;
        }


    }
}
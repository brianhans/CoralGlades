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
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.brianhans.coralglades.CustomComparator;
import com.brianhans.coralglades.R;
import com.brianhans.coralglades.CustomRecycleAdapter;
import com.brianhans.coralglades.UserSelect;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.recyclerview.animators.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.adapters.SlideInBottomAnimationAdapter;
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
    private RecyclerView cardHolder;
    private CustomRecycleAdapter recycleAdapter;
    private Context context;
    private boolean webpage;
    private WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        setRetainInstance(true);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        webView = new WebView(getContext());
        webView.setWebViewClient(new Browser());
        context = getContext();
        cardHolder = (RecyclerView) getView().findViewById(R.id.card_holder);
        cardHolder.setLayoutManager(new LinearLayoutManager(context));
        cardHolder.setHasFixedSize(true);

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        boolean firstRun = pref.getBoolean("firstrun", true);

        webpage = pref.getBoolean("webpage", false);

        //Runs user select if the program is run for the first time.
        if (firstRun) {
            Intent intent = new Intent(getActivity(), UserSelect.class);
            startActivity(intent);
        }else{
            loadFeed();
        }

        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)view.findViewById(R.id.swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadFeed();
            }
        });

    }

    private void loadFeed(){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout)getView().findViewById(R.id.swipe);

        accounts = new ArrayList(pref.getStringSet("users", null));
        if(isNetworkConnected(context)) {
            if(webpage){
                cardHolder.removeAllViewsInLayout();
                InternetTwitter fragment = new InternetTwitter();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            }else {
                GetUserTimeline getTweets = new GetUserTimeline();
                getTweets.execute(accounts);
            }
        }else{
            Snackbar snackbar = Snackbar.make(refreshLayout, "No Internet Connection", Snackbar.LENGTH_LONG).setAction("RETRY", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadFeed();
                }
            });
            snackbar.show();
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

    public void stopRefreshing() {
        SwipeRefreshLayout refreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe);
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
    }


    private class GetUserTimeline extends AsyncTask<List<String>, Void, List<Status>> {

        final static String CONSUMER_KEY = "T6rCfkVNRlbZyaaT4VEogAv9C";
        final static String CONSUMER_SECRET = "pMK5pKidLZJCjxwFeP3G09pspqVWR7hmX3eahLe0URhLZbD4Mv";
        final static String ACCESS_TOKEN = "1239035221-1Ti1dwtyCCkd7hbl9PLLt8I5WEWlHYIwu6qJuDm";
        final static String ACCESS_SECRET = "OB1XrZCxWUK17EIIuTZ0QsERFWCK3zwgTF9detGALssvI";

        List<String> users = new ArrayList<>();
        RelativeLayout loadingSpinner = (RelativeLayout) getActivity().findViewById(R.id.loading_circle);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cardHolder.removeAllViewsInLayout();
            cardHolder.setVisibility(View.GONE);
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
                    Log.d("user", user);
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
                cardHolder.removeAllViewsInLayout();
                stopRefreshing();
                InternetTwitter fragment = new InternetTwitter();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
                CoordinatorLayout mMainLayout = (CoordinatorLayout) getActivity().findViewById(R.id.main_layout);
                Snackbar snackbar =  Snackbar.make(mMainLayout, "Out of API Calls", Snackbar.LENGTH_LONG);
                snackbar.show();
                getActivity().findViewById(R.id.back).setVisibility(View.VISIBLE);
            } else {
                cardHolder.removeAllViewsInLayout();
                cardHolder.setVisibility(View.VISIBLE);
                //Adds Cards to screen
                recycleAdapter = new CustomRecycleAdapter(context, stats);
                AlphaInAnimationAdapter adapter = new AlphaInAnimationAdapter(recycleAdapter);
                cardHolder.setAdapter(new SlideInBottomAnimationAdapter(adapter));
                stopRefreshing();
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

    private class Browser extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
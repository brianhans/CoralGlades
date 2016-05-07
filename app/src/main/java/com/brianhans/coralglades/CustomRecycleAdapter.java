package com.brianhans.coralglades;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.LocalDate;
import org.joda.time.ReadableDuration;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.URLEntity;

/**
 * Created by Brian on 11/21/2015.
 */
public class CustomRecycleAdapter extends RecyclerView.Adapter<CustomRecycleAdapter.MyViewHolder> {

    private final static String TAG = "cardCreator";
    private float scale;
    private List<Status> tweetList;
    private Context context;
    private Fragment fragment;
    private Hashtable<String, Bitmap> downloadedImages = new Hashtable<>();
    private boolean pictures;


    public CustomRecycleAdapter(Context context, List<Status> tweetList, Fragment fragment) {
        this.context = context;
        this.tweetList = tweetList;
        this.fragment = fragment;
        scale = context.getResources().getDisplayMetrics().density;
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pictures = pref.getBoolean("pictures", true);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.twitter_card, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int i) {
        Status tweet = tweetList.get(i);
        final MediaEntity[] images = tweet.getExtendedMediaEntities();

        new DownloadImage(holder.profilePicture).execute(tweet.getUser().getOriginalProfileImageURL());
        Log.d("Media", images.length + "");

        if (pictures && images.length > 0) {
            holder.thumb.setVisibility(View.VISIBLE);

            Uri uri = Uri.parse(images[0].getMediaURL());
            ImageView picture = new ImageView(context);

            //Sets images parameters
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.height = (int) (100 * scale + 0.5f);
            params.width = (int) (100 * scale + 0.5f);
            picture.setLayoutParams(params);

            final ImageView thumb = holder.thumb;
            thumb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("ImageViewer", "Clicked");
                    FrameLayout imageViewer = (FrameLayout) thumb.getRootView().getRootView().getRootView().findViewById(R.id.photoViewer);
                    imageViewer.setVisibility(View.VISIBLE);
                    String[] urls = new String[images.length];
                    for (int i = 0; i < images.length; i++) {
                        urls[i] = images[i].getMediaURL();
                    }
                    Fragment mpv = new MultiplePictureViewer();
                    Bundle bundle = new Bundle();
                    bundle.putStringArray("Urls", urls);
                    mpv.setArguments(bundle);
                    FragmentManager fragmentManager = fragment.getFragmentManager();
                    fragmentManager.beginTransaction().add(R.id.photoViewer, mpv).commit();
                }
            });
            Glide.with(context).load(uri).into(thumb);

        } else {
            holder.thumb.setVisibility(View.GONE);
        }

        holder.tweetText.setMovementMethod(LinkMovementMethod.getInstance());
        addTweetText(holder.tweetText, tweet);

        Duration difference = new Duration(tweet.getCreatedAt().getTime(), System.currentTimeMillis());
        int hours = difference.toStandardHours().getHours();
        String date = "";
        LocalDate today = new LocalDate().minusWeeks(1);
        if(hours < 1){
            if(difference.getStandardMinutes() < 0)
                date = difference.getStandardSeconds() + " s";
            else
                date = difference.getStandardMinutes() + " m";
        }else if (hours < 24){
            date = hours + " h";
        }else if(difference.isShorterThan(Duration.standardDays(7))){
            date =  difference.getStandardDays() + " d";
        }else{
            date = new DateTime(tweet.getCreatedAt()).toString("MM:dd");
        }
        holder.date.setText(date);

        holder.userName.setText(tweet.getUser().getName());
    }

    private void addTweetText(TextView text, Status tweet) {
        String contents = tweet.getText();
        Log.d(TAG, "addTweetText: " + contents);
        if(contents.contains("…")){
            for(int i = contents.indexOf("…"); i > 0; i--){
                if(contents.charAt(i) == ' '){
                    contents = contents.substring(0, i);
                    for(URLEntity url :tweet.getURLEntities()){
                        contents += " " + url.getURL();
                    }
                    break;
                }
            }

        }
        text.setText(contents);
        Linkify.addLinks(text, Linkify.WEB_URLS);

        SpannableStringBuilder stringBuilder = new SpannableStringBuilder(contents);
        URLSpan[] urls = text.getUrls();
        for (URLSpan url : urls) {
            makeLinksClickable(stringBuilder, url);
        }

        text.setText(stringBuilder);
    }

    private void makeLinksClickable(SpannableStringBuilder stringBuilder, final URLSpan url) {
        int start = stringBuilder.toString().indexOf(url.getURL().toString());
        int end = start + url.getURL().toString().length();
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                TextView textView = (TextView) widget;
                Spanned contents = (Spanned) textView.getText();
                String url = contents.subSequence(contents.getSpanStart(this), contents.getSpanEnd(this)) + "";
                Intent intent = new Intent(context, CustomBrowser.class);
                intent.putExtra("url", url);
                context.startActivity(intent);
            }
        };
        stringBuilder.setSpan(clickableSpan, start, end, 0);
        stringBuilder.removeSpan(url);
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView tweetText;
        TextView date;
        ImageView profilePicture;
        ImageView thumb;

        MyViewHolder(View itemView) {
            super(itemView);
            tweetText = (TextView) itemView.findViewById(R.id.tweet_text);
            date = (TextView) itemView.findViewById(R.id.date);
            profilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
            userName = (TextView) itemView.findViewById(R.id.user);
            thumb = (ImageView) itemView.findViewById(R.id.thumb);
        }
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {

        private WeakReference<ImageView> imageViewReference = null;

        public DownloadImage(ImageView imageView) {
            imageViewReference = new WeakReference<>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap pictures;
            String url = params[0];

            if (alreadyDownloaded(url)) {
                pictures = downloadedImages.get(url);
            } else {
                Bitmap image = Download(url);
                pictures = image;
                downloadedImages.put(url, image);
            }
            return pictures;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                imageView.setImageBitmap(bitmap);
            }
        }

        private boolean alreadyDownloaded(String url) {
            if (downloadedImages.containsKey(url)) {
                return true;
            } else {
                return false;
            }
        }

        private Bitmap Download(String url) {
            try {
                InputStream in = new java.net.URL(url).openStream();
                return BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                String err = (e.getMessage() == null) ? "Error" : e.getMessage();
                Log.e("Error:", err);
                e.printStackTrace();
            }
            return null;
        }
    }
}

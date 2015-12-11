package com.brianhans.coralglades;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import twitter4j.MediaEntity;
import twitter4j.Status;

/**
 * Created by Brian on 11/21/2015.
 */
public class CustomRecycleAdapter extends RecyclerView.Adapter<CustomRecycleAdapter.MyViewHolder> {

    private float scale;
    private List<Status> tweetList;
    private Context context;
    private Hashtable<String, Bitmap> downloadedImages = new Hashtable<>();


    public CustomRecycleAdapter(Context context, List<Status> tweetList) {
        this.context = context;
        this.tweetList = tweetList;
        scale = context.getResources().getDisplayMetrics().density;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.twitter_card, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        Status tweet = tweetList.get(i);
        MediaEntity[] images = tweet.getExtendedMediaEntities();

        new DownloadImage(holder.profilePicture).execute(tweet.getUser().getBiggerProfileImageURL());
        Log.d("Media", images.length + "");


        if (images.length > 0) {

            holder.imageHolder.setVisibility(View.VISIBLE);
            holder.imageHolder.removeAllViewsInLayout();


            for(int f =0; f < images.length; f++){
                Log.d("Media", images[f].getMediaURL());
                Uri uri = Uri.parse(images[f].getMediaURL());
                ImageView picture = new ImageView(context);

                //Sets images parameters
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                //Adds a right margin of 20dp
                if(f != images.length - 1){
                    int rightMargin = (int) (20 * scale + 0.5f);
                    params.setMargins(0,0,rightMargin,0);
                }
                picture.setLayoutParams(params);

                holder.imageHolder.addView(picture);
                Glide.with(context).load(uri).into(picture);

            }

        }else{
            holder.imageHolder.removeAllViewsInLayout();
            holder.imageHolder.setVisibility(View.GONE);
        }

        holder.tweetText.setText(tweet.getText());

        holder.date.setText(tweet.getCreatedAt().toString());
        holder.userName.setText(tweet.getUser().getName());
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView tweetText;
        TextView date;
        TextView link;
        ImageView profilePicture;
        LinearLayout imageHolder;

        MyViewHolder(View itemView) {
            super(itemView);
            tweetText = (TextView) itemView.findViewById(R.id.tweet_text);
            date = (TextView) itemView.findViewById(R.id.date);
            link = (TextView) itemView.findViewById(R.id.tweet_link);
            profilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
            userName = (TextView) itemView.findViewById(R.id.user);
            imageHolder = (LinearLayout) itemView.findViewById(R.id.imageHolder);
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
                Log.d("Download", "Got saved image at " + url);
                pictures = downloadedImages.get(url);
            } else {
                Log.d("Download", "Downloaded " + url);
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
                Log.d("Image", bitmap.getHeight() + " x " + bitmap.getWidth());

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

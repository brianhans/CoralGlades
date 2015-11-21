package com.brianhans.coralglades;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import twitter4j.Status;

/**
 * Created by Brian on 11/21/2015.
 */
public class CustomRecycleAdapter extends RecyclerView.Adapter<CustomRecycleAdapter.MyViewHolder>{
    private List<Status> tweetList;
    private Context context;


    public CustomRecycleAdapter(Context context, List<Status> tweetList){
        this.context = context;
        this.tweetList = tweetList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.twitter_card, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
       // holder.profilePicture.setImageResource();  Need to download tweetList.get(i).getUser().getProfileImageURL()
        holder.tweetText.setText(tweetList.get(i).getText());
        //holder.link.setText(tweetList.get(i).getURLEntities()[0].getText());
        holder.date.setText(tweetList.get(i).getCreatedAt().toString());
        holder.userName.setText(tweetList.get(i).getUser().getName());
    }

    @Override
    public int getItemCount() {
        return tweetList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        TextView userName;
        TextView tweetText;
        TextView date;
        TextView link;
        ImageView profilePicture;

        MyViewHolder(View itemView){
            super(itemView);
            tweetText = (TextView) itemView.findViewById(R.id.tweet_text);
            date = (TextView) itemView.findViewById(R.id.date);
            link = (TextView) itemView.findViewById(R.id.tweet_link);
            profilePicture = (ImageView) itemView.findViewById(R.id.profile_picture);
            userName = (TextView) itemView.findViewById(R.id.user);
        }
    }
}

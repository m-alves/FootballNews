package com.example.android.footballnews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Utilizador on 05/08/2017.
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.CustomViewHolder> {
    private List<Story> storyList;
    private Context mContext;
    private OnItemClickListener onItemClickListener;

    public MyRecyclerViewAdapter(Context context, ArrayList<Story> storyList) {
        this.storyList = storyList;
        this.mContext = context;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_row, null);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CustomViewHolder customViewHolder, int i) {
        final Story story = storyList.get(i);

        //Setting the views
        customViewHolder.storyTitle.setText(story.getTitle());
        customViewHolder.storySection.setText(story.getSection());
        customViewHolder.storyDate.setText(story.getDate());
        customViewHolder.storyAuthor.setText(story.getAuthor());
        customViewHolder.storySectionLabel.setText(R.string.section_label);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(story);
            }
        };
        customViewHolder.storyTitle.setOnClickListener(listener);
        customViewHolder.storySection.setOnClickListener(listener);
        customViewHolder.storyDate.setOnClickListener(listener);
        customViewHolder.storyAuthor.setOnClickListener(listener);
        customViewHolder.storySectionLabel.setOnClickListener(listener);
    }

    @Override
    public int getItemCount() {
        return storyList.size();
    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        protected TextView storyTitle;
        protected TextView storySection;
        protected TextView storyDate;
        protected TextView storyAuthor;
        protected TextView storySectionLabel;


        public CustomViewHolder(View view) {
            super(view);
            this.storyTitle = (TextView) view.findViewById(R.id.story_title);
            this.storySection = (TextView) view.findViewById(R.id.story_section);
            this.storyDate = (TextView) view.findViewById(R.id.story_date_published);
            this.storyAuthor = (TextView) view.findViewById(R.id.story_author);
            this.storySectionLabel = (TextView) view.findViewById(R.id.story_section_label);
        }
    }

    public void setStoriesList(List<Story> stories){
        storyList = stories;
        this.notifyDataSetChanged();
    }

    public OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
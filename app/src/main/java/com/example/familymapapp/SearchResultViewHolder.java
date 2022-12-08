package com.example.familymapapp;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

public class SearchResultViewHolder extends RecyclerView.ViewHolder {

    private TextView mSearchResultInformation;
    private ImageView mSearchResultIcon;

    private RelativeLayout mSearchResultLayout;

    private List<SearchResult> mSearchResultList = new ArrayList<>();

    private Context mContext;

    private String id;

    public SearchResultViewHolder(View itemView, Context context, List<SearchResult> searchResultList) {
        super(itemView);

        mSearchResultInformation = (TextView) itemView.findViewById(R.id.search_recycler_view);
        mSearchResultIcon = (ImageView) itemView.findViewById(R.id.search_recycler_view_image);
        mSearchResultLayout = (RelativeLayout) itemView.findViewById(R.id.search_item_layout);

        this.mSearchResultList = searchResultList;

        this.mContext = context;



        mSearchResultLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Switch to person or event activity depending on search result
                int position = getAdapterPosition();
                SearchResult searchResult = mSearchResultList.get(position);

                if(searchResult.getPerson()){
                    DataCache.getInstance().setActivityPerson(DataCache.getInstance().getPeople().get(id));
                    Intent intent = new Intent(mContext, PersonActivity.class);
                    mContext.startActivity(intent);
                }
                else if(searchResult.getEvent()){
                    DataCache.getInstance().setActivityEvent(DataCache.getInstance().getEvents().get(id));
                    Intent intent = new Intent(mContext, EventActivity.class);
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public void bind(final SearchResult item){
        //adapter calls bind on viewholder to switch out data
        if(item.getEvent()){
            mSearchResultIcon.setImageDrawable(new IconDrawable(
                    mContext, FontAwesomeIcons.fa_map_marker));
            mSearchResultInformation.setText(item.getInformation());
        }
        else if(item.getPerson()){
            if(item.getGender().equals("m"))
                mSearchResultIcon.setImageResource(R.drawable.man);
            else
                mSearchResultIcon.setImageResource((R.drawable.woman));

            mSearchResultInformation.setText(item.getInformation());
        }

        id = item.getID();
    }
}
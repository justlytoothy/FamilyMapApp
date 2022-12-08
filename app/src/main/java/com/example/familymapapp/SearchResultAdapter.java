package com.example.familymapapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultViewHolder> {

    private Context mContext;
    private List<SearchResult> mSearchResults;

    public SearchResultAdapter(Context context, List<SearchResult> searchResults) {
        mContext = context;

        mSearchResults = searchResults;
    }

    @Override
    public SearchResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View searchView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_search_result, parent, false);

        return new SearchResultViewHolder(searchView, mContext, mSearchResults);
    }

    @Override
    public void onBindViewHolder(SearchResultViewHolder holder, int position) {
        holder.bind(mSearchResults.get(position));
    }

    @Override
    public int getItemCount() {
        return mSearchResults.size();
    }
}
package com.example.familymapapp;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.List;
public class EventFilterAdapter extends RecyclerView.Adapter<EventFilterViewHolder>{

    private Context theContext;
    private List<EventTypeFilter> mEventTypeFilters;

    public EventFilterAdapter(Context context, List<EventTypeFilter> eventTypeFilters) {
        theContext = context;

        mEventTypeFilters = eventTypeFilters;
    }

    @Override
    public EventFilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View filterView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_event_filter, parent, false);

        return new EventFilterViewHolder(filterView, mEventTypeFilters);
    }

    @Override
    public void onBindViewHolder(EventFilterViewHolder holder, int position) {
        holder.bind(mEventTypeFilters.get(position));
    }

    @Override
    public int getItemCount() {
        return mEventTypeFilters.size();
    }

}
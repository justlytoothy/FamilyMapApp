package com.example.familymapapp;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventFilterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private TextView header;
    private TextView desc;
    private Switch filterSwitch;

    private List<EventTypeFilter> mEventTypeFilterList = new ArrayList<>();

    public EventFilterViewHolder(View itemView, List<EventTypeFilter> eventTypeFilterList) {
        super(itemView);

        header = (TextView) itemView.findViewById(R.id.filter_recycler_view_heading);
        desc = (TextView) itemView.findViewById(R.id.filter_recycler_view_description);
        filterSwitch = (Switch) itemView.findViewById(R.id.filter_recycler_view_switch);
        filterSwitch.setClickable(false);

        mEventTypeFilterList = eventTypeFilterList;

        itemView.setOnClickListener(this);
    }

    public void bind(final EventTypeFilter item){
        //adapter calls bind on viewholder to switch out data

        //Create the event header
        StringBuilder eventHead = new StringBuilder();
        eventHead.append(item.getEventType() + " Events\n");
        header.setText(eventHead.toString());

        //Create the event description
        StringBuilder eventDescription = new StringBuilder();
        eventDescription.append("FILTER BY " + item.getEventType().toUpperCase()
                + " EVENTS");
        desc.setText(eventDescription.toString());

        //grab the boolean that says if filter is on or off
        boolean eventFilter = item.isSwitchStatus();
        filterSwitch.setChecked(eventFilter);
    }

    @Override
    public void onClick(View view){
        int position = getAdapterPosition();
        EventTypeFilter eventFilter = this.mEventTypeFilterList.get(position);

        //User selected filter, change switch
        if(eventFilter.isSwitchStatus()){
            eventFilter.setSwitchStatus(false);
            filterSwitch.setChecked(false);
        }
        else {
            eventFilter.setSwitchStatus(true);
            filterSwitch.setChecked(true);
        }

        String eventType = eventFilter.getEventType();

        Filter filters = DataCache.getInstance().getFilters();

        Boolean filterStatus = eventFilter.isSwitchStatus();

        if(eventType.equals("Father's Side"))
            filters.setFatherSideFilter(filterStatus);
        else if(eventType.equals("Mother's Side"))
            filters.setMotherSideFilter(filterStatus);
        else if(eventType.equals("Male"))
            filters.setMaleEventFilter(filterStatus);
        else if(eventType.equals("Female"))
            filters.setFemaleEventFilter(filterStatus);
        else{
            //Change the bool for this event in the model class
            Map<String, Boolean> eventFilters = filters.getEventFilter();
            DataCache.getInstance().getFilters().changeEventFilter(eventType);
        }
    }
}
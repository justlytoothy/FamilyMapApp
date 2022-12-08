package com.example.familymapapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FilterActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter eventAdapter;
    private List<EventTypeFilter> eventTypeFilterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        recyclerView = findViewById(R.id.activity_filter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        eventTypeFilterList = new ArrayList<>();

        Filter filters = DataCache.getInstance().getFilters();
        Map<String, Boolean> filterMap = filters.getEventFilter();
        for (String t : filterMap.keySet()) {
            EventTypeFilter newFilter = new EventTypeFilter(t,filterMap.get(t));
            eventTypeFilterList.add(newFilter);
        }
        boolean dadFilter = filters.isFatherSideFilter();
        boolean momFilter = filters.isMotherSideFilter();
        EventTypeFilter dadSideItem = new EventTypeFilter("Father's Side",dadFilter);
        EventTypeFilter momSideItem = new EventTypeFilter("Mother's Side",momFilter);
        eventTypeFilterList.add(dadSideItem);
        eventTypeFilterList.add(momSideItem);
        boolean maleEvent = filters.isMaleEventFilter();
        boolean femaleEvent = filters.isFemaleEventFilter();
        EventTypeFilter maleFilter = new EventTypeFilter("Male", maleEvent);
        EventTypeFilter femaleFilter = new EventTypeFilter("Female", femaleEvent);

        eventTypeFilterList.add(maleFilter);
        eventTypeFilterList.add(femaleFilter);
        eventAdapter = new EventFilterAdapter(this, eventTypeFilterList);

        recyclerView.setAdapter(eventAdapter);
        recyclerView.setAdapter(eventAdapter);

    }
}
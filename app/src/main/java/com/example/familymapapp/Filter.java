package com.example.familymapapp;

import java.util.HashMap;
import java.util.Map;

public class Filter {

    private Map<String, Boolean> eventFilter;

    private boolean fatherSideFilter;

    private boolean motherSideFilter;

    private boolean maleEventFilter;

    private boolean femaleEventFilter;

    public Filter(){
        fatherSideFilter = true;
        motherSideFilter = true;
        maleEventFilter = true;
        femaleEventFilter = true;
        eventFilter = new HashMap<String, Boolean>();
        for(String eventType : DataCache.getInstance().getEventTypes()){
            eventFilter.put(eventType, true);
        }
    }

    public Filter(boolean fatherSideFilter, boolean motherSideFilter, boolean maleEventFilter, boolean femaleEventFilter, boolean genFilter) {
        this.fatherSideFilter = fatherSideFilter;
        this.motherSideFilter = motherSideFilter;
        this.maleEventFilter = maleEventFilter;
        this.femaleEventFilter = femaleEventFilter;
        eventFilter = new HashMap<String, Boolean>();
        for(String eventType : DataCache.getInstance().getEventTypes()){
            eventFilter.put(eventType, genFilter);
        }
    }


    public Map<String, Boolean> getEventFilter() {
        return eventFilter;
    }

    public boolean isFatherSideFilter() {
        return fatherSideFilter;
    }

    public boolean isMotherSideFilter() {
        return motherSideFilter;
    }

    public boolean isMaleEventFilter() {
        return maleEventFilter;
    }

    public boolean isFemaleEventFilter() {
        return femaleEventFilter;
    }

    public void setFatherSideFilter(boolean fatherSideFilter) {
        this.fatherSideFilter = fatherSideFilter;
    }

    public void setMotherSideFilter(boolean motherSideFilter) {
        this.motherSideFilter = motherSideFilter;
    }

    public void setMaleEventFilter(boolean maleEventFilter) {
        this.maleEventFilter = maleEventFilter;
    }

    public void setFemaleEventFilter(boolean femaleEventFilter) {
        this.femaleEventFilter = femaleEventFilter;
    }

    public void changeEventFilter(String eventType){
        Map<String, Boolean> newEventFilters = new HashMap<>();
        for(String type : DataCache.getInstance().getEventTypes()){
            if(eventType.equals(type)){
                if(eventFilter.get(type))
                    newEventFilters.put(type, false);
                else
                    newEventFilters.put(type, true);
            }
            else{
                newEventFilters.put(type, eventFilter.get(type));
            }
        }
        eventFilter = newEventFilters;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("FatherSideFilter: " + fatherSideFilter);
        str.append("\nMotherSideFilter: " + motherSideFilter);
        str.append("\nMaleEventFilter: " + maleEventFilter);
        str.append("\nFemaleEventFilter: " + femaleEventFilter);
        str.append("\nEventFilter:");
        for(String string : eventFilter.keySet()){
            str.append("\n\t" + string + "->" + eventFilter.get(string));
        }
        return str.toString();
    }
}
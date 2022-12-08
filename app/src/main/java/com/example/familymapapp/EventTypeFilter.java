package com.example.familymapapp;

public class EventTypeFilter {

    /**PRIVATE DATA MEMBERS**/
    private String eventType;

    private String description;

    private boolean switchStatus;

    public EventTypeFilter(String eventType, boolean switchStatus){
        this.eventType = eventType;
        this.switchStatus = switchStatus;
    }

    public String getEventType() {
        return eventType;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSwitchStatus() {
        return switchStatus;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSwitchStatus(boolean switchStatus) {
        this.switchStatus = switchStatus;
    }
}
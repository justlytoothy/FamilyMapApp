package com.example.familymapapp;

public class SearchResult {

    private String information;
    private String gender;

    private Boolean isPerson;
    private Boolean isEvent;

    private String id;

    public SearchResult(String information){
        this.information = information;
    }

    public String getInformation() {
        return information;
    }

    public String getGender() {
        return gender;
    }

    public Boolean getPerson() {
        return isPerson;
    }

    public Boolean getEvent() {
        return isEvent;
    }

    public String getID(){
        return id;
    }


    public void setInformation(String information) {
        this.information = information;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setPerson(Boolean person) {
        isPerson = person;
    }

    public void setEvent(Boolean event) {
        isEvent = event;
    }

    public void setID(String id) {
        this.id = id;
    }

}
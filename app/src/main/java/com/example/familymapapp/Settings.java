package com.example.familymapapp;

public class Settings {

    private boolean lifeStoryLinesFilter;

    private boolean familyTreeLinesFilter;

    private boolean spouseLinesFilter;

    private String lifeStoryLineColor;

    private String familyTreeLineColor;

    private String spouseLineColor;

    private String mapView;

    public Settings(){
        lifeStoryLinesFilter = true;
        familyTreeLinesFilter = true;
        spouseLinesFilter = true;

        lifeStoryLineColor = "green";
        familyTreeLineColor = "red";
        spouseLineColor = "blue";

        mapView = "normal";
    }
    public boolean isLifeStoryLinesFilter() {
        return lifeStoryLinesFilter;
    }

    public boolean isFamilyTreeLinesFilter() {
        return familyTreeLinesFilter;
    }

    public boolean isSpouseLinesFilter() {
        return spouseLinesFilter;
    }

    public String getLifeStoryLineColor() {
        return lifeStoryLineColor;
    }

    public String getFamilyTreeLineColor() {
        return familyTreeLineColor;
    }

    public String getSpouseLineColor() {
        return spouseLineColor;
    }

    public String getMapView() {
        return mapView;
    }

    public void setLifeStoryLinesFilter(boolean lifeStoryLinesFilter) {
        this.lifeStoryLinesFilter = lifeStoryLinesFilter;
    }

    public void setFamilyTreeLinesFilter(boolean familyTreeLinesFilter) {
        this.familyTreeLinesFilter = familyTreeLinesFilter;
    }

    public void setSpouseLinesFilter(boolean spouseLinesFilter) {
        this.spouseLinesFilter = spouseLinesFilter;
    }

    public void setLifeStoryLineColor(String lifeStoryLineColor) {
        this.lifeStoryLineColor = lifeStoryLineColor;
    }

    public void setFamilyTreeLineColor(String familyTreeLineColor) {
        this.familyTreeLineColor = familyTreeLineColor;
    }

    public void setSpouseLineColor(String spouseLineColor) {
        this.spouseLineColor = spouseLineColor;
    }

    public void setMapView(String mapView) {
        this.mapView = mapView;
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("LifeStoryLine: " + lifeStoryLinesFilter);
        string.append("\nFamilyTreeLine: " + familyTreeLinesFilter);
        string.append("\nSpouseLine: " + spouseLinesFilter);
        string.append("\nLifeStory Color: " + lifeStoryLineColor);
        string.append("\nFamilyTree Color: " + familyTreeLineColor);
        string.append("\nSpouseLine Color: " + spouseLineColor);
        return string.toString();
    }
}
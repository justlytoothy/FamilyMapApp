package com.example.familymapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import model.Event;
import model.Person;

public class MapsFragment extends Fragment {
    private GoogleMap currMap;
    private ArrayList<Polyline> lines = new ArrayList<>();
    private ArrayList<PolylineOptions> lineOptions = new ArrayList<>();
    private Marker prevMarker;
    public Map<Marker, Event> eventMarkers = new HashMap<>();
    private Map<Event, Marker> markerEvents = new HashMap<>();
    private Filter filters;
    private Settings settings;
    private LinearLayout event;
    private TextView eventDetails;
    private ImageView icon;
    public MapsFragment(){}

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            currMap = googleMap;
            Map<String,Event> events = DataCache.getInstance().getEvents();
            if (!DataCache.getInstance().getHasColors()) {
                DataCache.getInstance().setColors(events.values());
            }

            addMarkers();
            clearLines();
            drawLines();
            String userPersonID = DataCache.getInstance().getCurrPerson().getPersonID();
            if(getActivity().getClass() == MainActivity.class) {

                Event birthEvent = DataCache.getInstance().getPersonEvents().get(userPersonID).first();
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(birthEvent.getLatitude(), birthEvent.getLongitude())));
            }
            else if(getActivity().getClass() == EventActivity.class){
                Event selectedEvent = DataCache.getInstance().getActivityEvent();
                Marker eventMarker = markerEvents.get(selectedEvent);
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(eventMarker.getPosition(), 5.0f));

                if (settings.isLifeStoryLinesFilter()) {
                    addLifeStoryLine(eventMarker);
                }
                if (settings.isFamilyTreeLinesFilter()) {
                    addFamilyLine(eventMarker);
                }
                if (settings.isSpouseLinesFilter()) {
                    addSpouseLine(eventMarker);
                }

                displayEventDetails(selectedEvent);

                prevMarker = eventMarker;
            }

            setMapType();

            setMapListeners();

        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
        event = (LinearLayout) view.findViewById(R.id.personButton);
        eventDetails = (TextView) view.findViewById(R.id.event);
        icon = (ImageView) view.findViewById(R.id.genderIcon);

        if(getActivity().getClass() == MainActivity.class) {
            setHasOptionsMenu(true);
        }
        else if(getActivity().getClass() == EventActivity.class){
            setHasOptionsMenu(false);
        }
    }
    private void setMapListeners(){
        currMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.isVisible()) {
                    prevMarker = marker;
                    Event event = eventMarkers.get(marker);
                    displayEventDetails(event);
                    clearLines();
                    if (settings.isLifeStoryLinesFilter()) addLifeStoryLine(marker);
                    if (settings.isFamilyTreeLinesFilter()) addFamilyLine(marker);
                    if (settings.isSpouseLinesFilter()) addSpouseLine(marker);
                    return true;
                }
                else{
                    return false;
                }
            }
        });

        eventDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(prevMarker != null) {
                    Event event = eventMarkers.get(prevMarker);
                    Person person = DataCache.getInstance().getPeople().get(event.getPersonID());
                    DataCache.getInstance().setActivityPerson(person);

                    DataCache.getInstance().setMarkers(eventMarkers);
                    DataCache.getInstance().setLines(lines);

                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    intent.putExtra("person", person.getPersonID());
                    intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);

                    getActivity().startActivity(intent);
                }
            }
        });
    }

    private void addMarkers() {
        filters = DataCache.getInstance().getFilters();
        settings = DataCache.getInstance().getSettings();
        for (Event event : DataCache.getInstance().passesFilter()) {
            Float color = DataCache.getInstance().getColorMap().get(event.getEventType().toLowerCase());
            LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
            Marker marker = currMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title(event.getEventType())
                    .icon(BitmapDescriptorFactory.defaultMarker(color)));

            eventMarkers.put(marker, event);
            markerEvents.put(event, marker);
        }
        DataCache.getInstance().setMarkers(eventMarkers);
    }
    private void displayEventDetails(Event event){
        Person person = DataCache.getInstance().getPeople().get(event.getPersonID());
        if(person.getGender().equals("m")) {
            icon.setImageResource(R.drawable.man);
        }
        else{
            icon.setImageResource(R.drawable.woman);
        }
        String eventsDetails = person.getFirstName() + " " + person.getLastName() + "\n" +
                event.getEventType() + ": " + event.getCity() + ", " +
                event.getCountry();

        if(String.valueOf(event.getYear()).equals("") || String.valueOf(event.getYear()).equals(null)){
            eventsDetails = eventsDetails + " N/A";
        }
        else {
            eventsDetails = eventsDetails + " (" + event.getYear() + ")";
        }
        eventDetails.setText(eventsDetails);
    }
    @Override
    public void onResume() {
        super.onResume();

        if(DataCache.getInstance().isResync()) {
            currMap.clear();
            filters = DataCache.getInstance().getFilters();
            settings = DataCache.getInstance().getSettings();

            Event lastEvent = eventMarkers.get(prevMarker);
            boolean lastMarkerChanged = false;

            eventMarkers.clear();
            Marker marker = null;
            markerEvents.clear();

            for (String eventType : filters.getEventFilter().keySet()) {
                if (filters.getEventFilter().get(eventType)) {
                    for (Event event : DataCache.getInstance().getEventsByType().get(eventType)) {
                        Float color = DataCache.getInstance().getColorMap().get(event.getEventType());
                        LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                        marker = currMap.addMarker(new MarkerOptions()
                                .position(latLng)
                                .title(event.getEventType())
                                .icon(BitmapDescriptorFactory.defaultMarker(color)));
                        if (lastEvent != null && event.getEventID().equals(lastEvent.getEventID())) {
                            prevMarker = marker;
                            lastMarkerChanged = true;
                        }
                        eventMarkers.put(marker, event);
                        markerEvents.put(event, marker);
                    }
                }
            }
            DataCache.getInstance().setMarkers(eventMarkers);

            if (!lastMarkerChanged) {
                prevMarker = null;
            } else {
                displayEventDetails(eventMarkers.get(prevMarker));
            }

            DataCache.getInstance().setResync(false);
        }

        if(currMap != null) {
            settings = DataCache.getInstance().getSettings();
            filters = DataCache.getInstance().getFilters();

            checkEventFilters();

            clearLines();
            if(prevMarker != null && prevMarker.isVisible()){
                if(settings.isLifeStoryLinesFilter()) addLifeStoryLine(prevMarker);
                if(settings.isFamilyTreeLinesFilter()) addFamilyLine(prevMarker);
                if(settings.isSpouseLinesFilter()) addSpouseLine(prevMarker);
            }
            setMapType();
            setMapListeners();
        }
    }

    private void addLifeStoryLine(Marker marker){
        Event markedEvent = eventMarkers.get(marker);
        Person person = DataCache.getInstance().getPeople().get(markedEvent.getPersonID());
        TreeSet<Event> events = DataCache.getInstance().getPersonEvents().get(person.getPersonID());
        lifeStoryLine(events);
    }

    private void addFamilyLine(Marker marker){
        Event event = eventMarkers.get(marker);
        Person person = DataCache.getInstance().getPeople().get(event.getPersonID());
        float width = 12;
        familyTreeLine(event, person, width);
    }
    private void addSpouseLine(Marker marker){
        Event event = eventMarkers.get(marker);
        Person person = DataCache.getInstance().getPeople().get(event.getPersonID());
        Person spouse = DataCache.getInstance().getPeople().get(person.getSpouseID());
        if(spouse != null){
            Event spouseEvent =  DataCache.getInstance().getPersonEvents().get(spouse.getPersonID()).first();
            Integer color = findColor(settings.getSpouseLineColor());
            float defaultWidth = 12;

            createPolyline(event, spouseEvent, color, defaultWidth);
        }
    }

    private void familyTreeLine(Event event, Person person, float width){
        if(settings.isFamilyTreeLinesFilter()) {

            Integer color = findColor(settings.getFamilyTreeLineColor());

            Person father = DataCache.getInstance().getPeople().get(person.getFatherID());
            if (father != null) {
                Event fatherEvent = DataCache.getInstance().getPersonEvents().get(father.getPersonID()).first();
                createPolyline(event, fatherEvent, color, width);
                width = width - 3;
                familyTreeLine(fatherEvent, father, width);
            }
            width = width + 3;
            Person mother = DataCache.getInstance().getPeople().get(person.getMotherID());
            if (mother != null) {
                Event motherEvent = DataCache.getInstance().getPersonEvents().get(mother.getPersonID()).first();
                createPolyline(event, motherEvent, color, width);
                familyTreeLine(motherEvent, mother, width);
            }
        }
    }
    private void lifeStoryLine(TreeSet<Event> events){
        if(settings.isLifeStoryLinesFilter()) {
            Integer color = findColor(settings.getLifeStoryLineColor());
            PolylineOptions lifeStory = new PolylineOptions();
            for (Event event : events) {
                if(markerEvents.get(event).isVisible()) {
                    lifeStory.add(new LatLng(event.getLatitude(), event.getLongitude()))
                            .color(getContext().getResources().getColor(color))
                            .width(12);
                }
            }
            lineOptions.add(lifeStory);
            lines.add(currMap.addPolyline(lifeStory));
        }
    }

    private void createPolyline(Event firstEvent, Event secondEvent, Integer color, float width){
        if(markerEvents.get(firstEvent).isVisible() && markerEvents.get(secondEvent).isVisible()) {
            PolylineOptions polylineOptions = new PolylineOptions()
                    .color(getContext().getResources().getColor(color))
                    .add(new LatLng(firstEvent.getLatitude(), firstEvent.getLongitude()),
                            new LatLng(secondEvent.getLatitude(), secondEvent.getLongitude()))
                    .width(width);
            lineOptions.add(polylineOptions);
            lines.add(currMap.addPolyline(polylineOptions));
        }
    }

    private void drawLines(){
        for(PolylineOptions option : lineOptions){
            currMap.addPolyline(option);
        }
    }
    private void clearLines(){
        if(lines.size() > 0) {
            for (Polyline line : lines) {
                line.remove();
            }
            lines.clear();
        }
        lineOptions.clear();
    }

    private int findColor(String color){
        int lineColor;
        switch (color){
            case "red": lineColor = R.color.red;
                break;
            case "blue": lineColor = R.color.blue;
                break;
            case "cyan": lineColor = R.color.cyan;
                break;
            case "green": lineColor = R.color.green;
                break;
            case "magenta": lineColor = R.color.magenta;
                break;
            case "yellow": lineColor = R.color.yellow;
                break;
            case "white": lineColor = R.color.white;
                break;
            case "black": lineColor = R.color.black;
                break;
            default: lineColor = 0;
        }
        return lineColor;
    }
    private void checkEventFilters(){

        for(Marker eventMarker : eventMarkers.keySet()){
            eventMarker.setVisible(true);
            Event event = eventMarkers.get(eventMarker);
            if(!filters.getEventFilter().get(event.getEventType())){
                eventMarker.setVisible(false);
            }
            if(!filters.isFatherSideFilter() &&
                    DataCache.getInstance().getFatherSide().contains(event.getPersonID())){
                eventMarker.setVisible(false);
            }
            if(!filters.isMotherSideFilter() &&
                    DataCache.getInstance().getMotherSide().contains(event.getPersonID())){
                eventMarker.setVisible(false);
            }
            Person person = DataCache.getInstance().getPeople().get(event.getPersonID());
            if(!filters.isMaleEventFilter() &&
                    person.getGender().equals("m")){
                eventMarker.setVisible(false);
            }
            if(!filters.isFemaleEventFilter() &&
                    person.getGender().equals("f")){
                eventMarker.setVisible(false);
            }
        }
    }
    private void setMapType(){
        String mapType = settings.getMapView();
        switch (mapType){
            case "Normal": currMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case "Hybrid": currMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case "Satellite": currMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case "Terrain": currMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
        }
    }
}
package com.example.familymapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
//            for (Event event : events.values()) {
//                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(), event.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(DataCache.getInstance().getColorMap().get(event.getEventType()))));
//                marker.setTag(event);
//            }
//            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                @Override
//                public boolean onMarkerClick(@NonNull Marker marker) {
//                    Event markerEvent = (Event) marker.getTag();
//                    Toast.makeText(getActivity(), markerEvent.getEventType(), Toast.LENGTH_SHORT).show();
//                    return false;
//                }
//            });
            addMarkers();
            //Re-draw polyline
            clearLines();
            drawLines();
            //Initial Camera Position
            String userPersonID = DataCache.getInstance().getCurrPerson().getPersonID();
            if(getActivity().getClass() == MainActivity.class) {
                // Move the camera to the user's birth
                for (String s : DataCache.getInstance().getPersonEvents().keySet()) {
                    System.out.println("hey");
                    System.out.println(s);
                }
                Event birthEvent = DataCache.getInstance().getPersonEvents().get(userPersonID).first();
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(birthEvent.getLatitude(), birthEvent.getLongitude())));
            }
            else if(getActivity().getClass() == EventActivity.class){
                //Move the camera to the selected event
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

            //Set Map type
            setMapType();

            mapListeners();

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
    private void mapListeners(){
        // Set a listener for marker click.
        currMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.isVisible()) {
                    prevMarker = marker;
                    //Find the event connected to the chosen marker
                    Event event = eventMarkers.get(marker);
                    //Display the event's details
                    displayEventDetails(event);
                    //Clear, if any, polylines on map
                    clearLines();
                    //Add the given lines
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
                    //Store models used in the map fragment
                    Event event = eventMarkers.get(prevMarker);
                    Person person = DataCache.getInstance().getPeople().get(event.getPersonID());
                    DataCache.getInstance().setActivityPerson(person);

                    DataCache.getInstance().setMarkers(eventMarkers);
                    DataCache.getInstance().setLines(lines);

                    //Create an intent to switch to the PersonActivity
                    Intent intent = new Intent(getActivity(), PersonActivity.class);
                    intent.putExtra("person", person.getPersonID());
                    intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME);

                    getActivity().startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

        //Set search icon
        menu.findItem(R.id.search).setIcon(
                new IconDrawable(this.getActivity(), FontAwesomeIcons.fa_search).actionBarSize());
        //Set filter icon
        menu.findItem(R.id.filter).setIcon(
                new IconDrawable(this.getActivity(), FontAwesomeIcons.fa_filter).actionBarSize());
        //Set settings icon
        menu.findItem(R.id.settings).setIcon(
                new IconDrawable(this.getActivity(), FontAwesomeIcons.fa_gear).actionBarSize());
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent);
                return true;
            case R.id.filter:
                intent = new Intent(getActivity(), FilterActivity.class);
                getActivity().startActivity(intent);
                return true;
            case R.id.settings:
                intent = new Intent(getActivity(), SettingsActivity.class);
                getActivity().startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    private void addMarkers(){
        //Find the correct filters and settings
        filters = DataCache.getInstance().getFilters();
        settings = DataCache.getInstance().getSettings();
        //Iterate through map of event filters (on/off)
        for(String eventType : filters.getEventFilter().keySet()) {
            //Check if given eventType is filtered on or off
            if(filters.getEventFilter().get(eventType)) {
                //If on, generate markers of that type
                for (Event event : DataCache.getInstance().getEventsByType().get(eventType)) {
                    //Add markers for the given eventType
                    Float color = DataCache.getInstance().getColorMap().get(event.getEventType());
                    LatLng latLng = new LatLng(event.getLatitude(), event.getLongitude());
                    Marker marker = currMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(event.getEventType())
                            .icon(BitmapDescriptorFactory.defaultMarker(color)));

                    eventMarkers.put(marker, event);
                    markerEvents.put(event, marker);
                }
            }
        }
        DataCache.getInstance().setMarkers(eventMarkers);
    }
    private void displayEventDetails(Event event){
        //Find the person connected to the chosen event
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

    private void addLifeStoryLine(Marker marker){
        //Find the event connected to the chosen marker
        Event markedEvent = eventMarkers.get(marker);
        //Find the person connected to the chosen event
        Person person = DataCache.getInstance().getPeople().get(markedEvent.getPersonID());
        //Get the list of this person's life events
        TreeSet<Event> events = DataCache.getInstance().getPersonEvents().get(person.getPersonID());
        //Loop through all the life events chronologically
        lifeStoryLine(events);
    }

    private void addFamilyLine(Marker marker){
        //Find the event connected to the chosen marker
        Event event = eventMarkers.get(marker);
        //Find the person connected to the chosen event
        Person person = DataCache.getInstance().getPeople().get(event.getPersonID());
        //Draw the family tree
        float width = 12;
        familyTreeLine(event, person, width);
    }
    private void addSpouseLine(Marker marker){
        //Find the event connected to the chosen marker
        Event event = eventMarkers.get(marker);
        //Find the person connected to the chosen event
        Person person = DataCache.getInstance().getPeople().get(event.getPersonID());
        //Check if the person has a spouse
        Person spouse = DataCache.getInstance().getPeople().get(person.getSpouseID());
        if(spouse != null){
            //Find the spouse's first event
            Event spouseEvent =  DataCache.getInstance().getPersonEvents().get(spouse.getPersonID()).first();
            //Initialize the color of the line
            Integer color = findColor(settings.getSpouseLineColor());
            //Create a line from the chosen marker to the spouse's birth event... or earliest event
            float defaultWidth = 12;

            createPolyline(event, spouseEvent, color, defaultWidth);
        }
    }

    private void familyTreeLine(Event event, Person person, float width){
        if(settings.isFamilyTreeLinesFilter()) {
            //Initialize the line color
            Integer color = findColor(settings.getFamilyTreeLineColor());
            //Get the father
            Person father = DataCache.getInstance().getPeople().get(person.getFatherID());
            if (father != null) {
                //Find the father's first event and create a line
                Event fatherEvent = DataCache.getInstance().getPersonEvents().get(father.getPersonID()).first();
                createPolyline(event, fatherEvent, color, width);
                //Recurse through the father's line
                width = width - 3;
                familyTreeLine(fatherEvent, father, width);
            }
            width = width + 3;
            //Get the mother
            Person mother = DataCache.getInstance().getPeople().get(person.getMotherID());
            if (mother != null) {
                //Find the mother's first event and create a line
                Event motherEvent = DataCache.getInstance().getPersonEvents().get(mother.getPersonID()).first();
                createPolyline(event, motherEvent, color, width);
                //Recurse through the mothers' line
                familyTreeLine(motherEvent, mother, width);
            }
        }
    }
    private void lifeStoryLine(TreeSet<Event> events){
        if(settings.isLifeStoryLinesFilter()) {
            //Initialize the line color
            Integer color = findColor(settings.getLifeStoryLineColor());
            //Create a list of Latitude and longitudes from the events
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
//    Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(), event.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(googleColor)));
}
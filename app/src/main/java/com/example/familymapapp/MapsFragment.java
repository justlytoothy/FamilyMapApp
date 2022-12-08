package com.example.familymapapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import model.Event;

public class MapsFragment extends Fragment {
    private GoogleMap currMap;
    private ArrayList<Polyline> lines = new ArrayList<>();
    private ArrayList<PolylineOptions> lineOptions = new ArrayList<>();
    private Marker prevMarker;
    private Map<Marker, Event> eventMarkers = new HashMap<>();
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
            for (Event event : events.values()) {
                Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(), event.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(DataCache.getInstance().getColorMap().get(event.getEventType()))));
                marker.setTag(event);
            }
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(@NonNull Marker marker) {
                    Event markerEvent = (Event) marker.getTag();
                    Toast.makeText(getActivity(), markerEvent.getEventType(), Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            //Re-draw polyline
            clearLines();
            drawLines();
            //Initial Camera Position
            String userPersonID = DataCache.getInstance().getCurrPerson().getPersonID();
            if(getActivity().getClass() == MainActivity.class) {
                // Move the camera to the user's birth
                Event birthEvent = DataCache.getInstance().getPersonEvents().get(userPersonID).first();
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(
                        new LatLng(birthEvent.getLatitude(), birthEvent.getLongitude())));
            }
            else if(getActivity().getClass() == EventActivity.class){
                //Move the camera to the selected event
                Event selectedEvent = DataCache.getInstance().getActivityEvent();
                Marker eventMarker = this.eventMarkers.get(selectedEvent);
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

                mLastMarkerClicked = eventMarker;
            }

            //Set Map type
            setMapType();

            mapListeners();

        }
    };
    public void drawLines(Event startEvent, Event endEvent, int googleColor, float width) {
        // Create start and end points for the line
        LatLng startPoint = new LatLng(startEvent.getLatitude(), startEvent.getLongitude());
        LatLng endPoint = new LatLng(endEvent.getLatitude(), endEvent.getLongitude());
        // Add line to map by specifying its endpoints, color, and width
        PolylineOptions options = new PolylineOptions()
                .add(startPoint)
                .add(endPoint)
                .color(googleColor)
                .width(width);
        Polyline line = currMap.addPolyline(options);
//        line.remove();
    }
    private void removeLine(Polyline line) {
        line.remove();
    }
    public void clearLines() {}


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
//    Marker marker = map.addMarker(new MarkerOptions().position(new LatLng(event.getLatitude(), event.getLongitude())).icon(BitmapDescriptorFactory.defaultMarker(googleColor)));
}
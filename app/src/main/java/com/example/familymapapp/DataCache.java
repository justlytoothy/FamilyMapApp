package com.example.familymapapp;





import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import model.Event;
import model.Person;

public class DataCache {
    private static final String TAG = "Data Cache";
    private static DataCache instance = null;
    private Map<String,Person> people;
    private Map<String,Event> events;
    private boolean resync = false;
    private String username;
    private String password;
    private Map<String,List<Event>> eventsByType = new HashMap<>();
    private Person currPerson;
    private boolean hasColors = false;
    private Map<String, Float> colorMap = new HashMap<>();
    private String authToken;
    private Filter filters;
    private List<String> fatherSide = new ArrayList<>();
    private List<String> motherSide = new ArrayList<>();
    private Map<String, String> children = new HashMap<>();
    private Person activityPerson;
    private Event activityEvent;
    private Map<String, TreeSet<Event>> personEvents = new HashMap<>();
    private Map<Marker, Event> markers = new HashMap<>();
    private List<Polyline> lines;
    private Settings settings = new Settings();
    private HashMap<Integer, Person> family =  new HashMap<>();
    private List<String> eventTypes = new ArrayList<>();

    private HashMap<Integer, Event> personLifeEvents = new HashMap<>();





    public HashMap<Integer, Person> getFamily() {
        return family;
    }

    public void setFamily(HashMap<Integer, Person> family) {

        this.family = family;
    }
    public void storePeople(Set<Person> responsePersons){
        for(Person person : responsePersons){
            people.put(person.getPersonID(), person);
        }
        if(currPerson.getFatherID() != null || currPerson.getMotherID() != null) {
            fatherSide.add(currPerson.getFatherID());
            storeParentSide(people.get(currPerson.getFatherID()), fatherSide);

            motherSide.add(currPerson.getMotherID());
            storeParentSide(people.get(currPerson.getMotherID()), motherSide);

            storeChildren();
        }
    }
    public HashMap<Integer, Event> getPersonLifeEvents() {
        return personLifeEvents;
    }
    public void setPersonLifeEvents(HashMap<Integer, Event> personLifeEvents) {
        this.personLifeEvents = personLifeEvents;
    }
    public Settings getSettings() {
        return settings;
    }
    public void setSettings(Settings settings) {
        this.settings = settings;
    }
    public String getAuthToken() {
        return authToken;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    private DataCache() {
        authToken = "";
    }

    public static DataCache getInstance() {
        if (instance == null) {
            instance = new DataCache();
        }
        return instance;

    }
    public boolean getHasColors() {
        return hasColors;
    }

    public Map<String, Float> getColorMap() {
        return colorMap;
    }

    public void setHasColors(boolean colors) {
        this.hasColors = colors;
    }
    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> people) {
        this.people = new HashMap<>();
        for (Person p : people) {
            this.people.put(p.getPersonID(),p);
        }
        if(currPerson.getFatherID() != null || currPerson.getMotherID() != null) {
            fatherSide.add(currPerson.getFatherID());
            storeParentSide(this.people.get(currPerson.getFatherID()), fatherSide);
            motherSide.add(currPerson.getMotherID());
            storeParentSide(this.people.get(currPerson.getMotherID()), motherSide);
            storeChildren();
        }
    }

    public Map<String, TreeSet<Event>> getPersonEvents() {
        return personEvents;
    }

    public void storePersonsEvents(){
        for(String p : people.keySet()){
            TreeSet<Event> currEvents = new TreeSet<>(new eventCompare());
            for(Event event : events.values()){
                String eventID = event.getPersonID();
                if(eventID.equals(p)){
                    currEvents.add(event);
                }
            }
            personEvents.put(p, currEvents);
        }
    }
    public Map<String, Event> getEvents() {
        return events;
    }
    public void reset() {
        instance = null;
        settings = new Settings();
    }
    public void setEvents(List<Event> events) {

        this.events = new HashMap<>();
        for (Event e : events) {
            this.events.put(e.getEventID(),e);
            if(!eventsByType.keySet().contains(e.getEventType())){
                List<Event> tmp = new ArrayList<Event>();
                tmp.add(e);
                eventsByType.put(e.getEventType(), tmp);
            }
            else{
                eventsByType.get(e.getEventType()).add(e);
            }
        }
        setColors(this.events.values());
        filters = new Filter();
    }

    private void storeParentSide(Person person, List<String> list) {
        if (people.get(person.getFatherID()) instanceof Person
                && people.get(person.getMotherID()) instanceof Person) {

            Person father = people.get(person.getFatherID());
            Person mother = people.get(person.getMotherID());

            if (father != null) {
                list.add(person.getFatherID());
            }
            storeParentSide(father, list);

            if (mother != null) {
                list.add(person.getMotherID());
            }
            storeParentSide(mother, list);
        }
        return;
    }

    private void storeChildren(){
        for(Person person : people.values()){
            if(people.get(person.getFatherID()) instanceof Person
                    && people.get(person.getMotherID()) instanceof Person){
                children.put(people.get(person.getFatherID()).getPersonID(), person.getPersonID());
                children.put(people.get(person.getMotherID()).getPersonID(), person.getPersonID());
            }
        }
    }
    public Person getCurrPerson() {
        return currPerson;
    }

    public void setCurrPerson(Person currPerson) {
        this.currPerson = currPerson;
    }
    public Event getEventByID(String id) {
        return this.events.get(id);
    }
    public Person getPersonByID(String id) {
        return this.people.get(id);
    }
    public void setColors(Collection<Event> events) {
        this.colorMap = new HashMap<>();
        float [] colors;
        colors = new float[10];
        colors[0] = BitmapDescriptorFactory.HUE_AZURE;
        colors[1] = BitmapDescriptorFactory.HUE_MAGENTA;
        colors[2] = BitmapDescriptorFactory.HUE_RED;
        colors[3] = BitmapDescriptorFactory.HUE_YELLOW;
        colors[4] = BitmapDescriptorFactory.HUE_BLUE;
        colors[5] = BitmapDescriptorFactory.HUE_ORANGE;
        colors[6] = BitmapDescriptorFactory.HUE_ROSE;
        colors[7] = BitmapDescriptorFactory.HUE_CYAN;
        colors[8] = BitmapDescriptorFactory.HUE_GREEN;
        colors[9] = BitmapDescriptorFactory.HUE_VIOLET;
        int i = 0;
        for (Event e : events) {
            if (!this.colorMap.containsKey(e.getEventType().toLowerCase())) {
                this.colorMap.put(e.getEventType().toLowerCase(),colors[i]);
                if (i == 9) {
                    i = 0;
                }
                else {
                    i++;
                }
            }
        }
        this.hasColors = true;
    }
    public ArrayList<String> getEventTypes() {
        ArrayList<String> types = new ArrayList<>();
        for (Event e : events.values()) {
            if (!types.contains(e.getEventType())) {
                types.add(e.getEventType());
            }
        }
        return types;
    }

    public boolean isHasColors() {
        return hasColors;
    }

    public Filter getFilters() {
        return filters;
    }

    public void setFilters(Filter filters) {
        this.filters = filters;
    }

    public Person getActivityPerson() {
        return activityPerson;
    }

    public void setActivityPerson(Person activityPerson) {
        this.activityPerson = activityPerson;
    }

    public Event getActivityEvent() {
        return activityEvent;
    }

    public void setActivityEvent(Event activityEvent) {
        this.activityEvent = activityEvent;
    }

    public Map<Marker, Event> getMarkers() {
        return markers;
    }

    public void setMarkers(Map<Marker, Event> markers) {
        this.markers = markers;
    }

    public List<Polyline> getLines() {
        return lines;
    }

    public void setLines(List<Polyline> lines) {
        this.lines = lines;
    }

    public Map<String, String> getChildren() {
        return children;
    }
    public List<Event> passesFilter() {
        List<Event> events = new ArrayList<>();
        for(String eventType : filters.getEventFilter().keySet()) {
            if(filters.getEventFilter().get(eventType)) {
                for (Event event : DataCache.getInstance().getEventsByType().get(eventType)) {
                    events.add(event);
                }
            }
        }
        return events;
    }
    class eventCompare implements Comparator<Event> {

        @Override
        public int compare(Event f1, Event f2)
        {
            if (f1.getYear() > f2.getYear()) {
                return 1;
            }
            else {
                return -1;
            }
        }
    }
    public boolean isResync() {
        return resync;
    }

    public void setResync(boolean resync) {
        this.resync = resync;
    }
    public Map<String, List<Event>> getEventsByType() {
        return eventsByType;
    }

    public List<String> getFatherSide() {
        return fatherSide;
    }

    public List<String> getMotherSide() {
        return motherSide;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public List<SearchResult> search(CharSequence sequence){
        Map<String, Person> persons = DataCache.getInstance().getPeople();
        Map<String, Event> events = DataCache.getInstance().getEvents();
        List<SearchResult> searchResults = new ArrayList<>();

        sequence = sequence.toString().toLowerCase();
        searchResults.clear();
        for(Person person : persons.values()){
            String firstName = person.getFirstName().toLowerCase();
            String lastName = person.getLastName().toLowerCase();
            if(firstName.contains(sequence) ||
                    lastName.contains(sequence)){
                StringBuilder resultString = new StringBuilder();
                resultString.append(person.getFirstName() + " " + person.getLastName());
                SearchResult result = new SearchResult(resultString.toString());
                result.setPerson(true);
                result.setEvent(false);
                result.setID(person.getPersonID());
                result.setGender(person.getGender());
                searchResults.add(result);
            }
        }

        for(Event event : events.values()){
            String country = event.getCountry().toLowerCase();
            String city = event.getCity().toLowerCase();
            String type = event.getEventType().toLowerCase();
            String year = String.valueOf(event.getYear()).toLowerCase();
            if(country.contains(sequence) ||
                    city.contains(sequence) ||
                    type.contains(sequence) ||
                    year.contains(sequence)){
                if(Boolean.TRUE.equals(DataCache.getInstance().getFilters().getEventFilter().get(type))) {
                    Person person = persons.get(event.getPersonID());
                    StringBuilder resultString = new StringBuilder();
                    resultString.append(event.getEventType() + ": " + event.getCity() + "  "
                            + event.getCountry() + " (" + event.getYear() + ")\n"
                            + person.getFirstName() + " " + person.getLastName());
                    SearchResult result = new SearchResult(resultString.toString());
                    result.setEvent(true);
                    result.setPerson(false);
                    result.setID(event.getEventID());
                    searchResults.add(result);
                }
            }
        }
        return searchResults;
    }
}


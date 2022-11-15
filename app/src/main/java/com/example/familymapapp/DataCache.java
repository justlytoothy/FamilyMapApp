package com.example.familymapapp;





import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Event;
import model.Person;

public class DataCache {
    private static DataCache instance;
    private Map<String,Person> people;
    private Map<String,Event> events;
    private Person currPerson;

    private DataCache() {
    }

    public static DataCache getInstance() {
        return instance;

    }

    public Map<String, Person> getPeople() {
        return people;
    }

    public void setPeople(List<Person> persons) {
        this.people = new HashMap<String,Person>();
        for (Person p : persons) {
            this.people.put(p.getPersonID(),p);
        }
    }

    public Map<String, Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = new HashMap<String,Event>();
        for (Event e : events) {
            this.events.put(e.getEventID(),e);
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
}


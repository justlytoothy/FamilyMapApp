package com.example.familymapapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import model.Event;
import model.Person;

public class PersonDetailInserter {

    private HashMap familyPeople =  new HashMap<Integer, Person>();

    private HashMap personLifeEvents = new HashMap<Integer, Event>();

    private HashMap<String, List<String>> newExpandableListDetail = new HashMap<String, List<String>>();

    public void getData(Person person) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> lifeEvents = findLifeEvents(person);

        List<String> family = findFamily(person);

        newExpandableListDetail.put("LIFE EVENTS", lifeEvents);
        newExpandableListDetail.put("FAMILY", family);


        DataCache.getInstance().setFamily(familyPeople);
        DataCache.getInstance().setPersonLifeEvents(personLifeEvents);
    }

    public List<String> findLifeEvents(Person person){
        TreeSet<Event> personEvents = DataCache.getInstance().getPersonEvents().get(person.getPersonID());

        int iterator = 0;
        List<String> lifeEvents = new ArrayList<String>();
        for(Event event : personEvents){
            if(DataCache.getInstance().getFilters().getEventFilter().get(event.getEventType())) {
                String lifeEvent = event.getEventType() + ": " +
                        event.getCity() + ", " + event.getCountry();
                if (String.valueOf(event.getYear()).equals("") || String.valueOf(event.getYear()).equals(null)) {
                    lifeEvent = lifeEvent + " N/A\n";
                } else {
                    lifeEvent = lifeEvent + " (" + event.getYear() + ")\n";
                }

                lifeEvent = lifeEvent + person.getFirstName() + " " + person.getLastName();

                personLifeEvents.put(iterator, event);
                iterator++;
                lifeEvents.add(lifeEvent);
            }
        }

        return lifeEvents;
    }

    private List<String> findFamily(Person person){
        //Create a list to store the family members
        List<String> family = new ArrayList<String>();

        int iterator = 0;
        //Pull all people to find the parents and spouse
        Map<String, Person> people = DataCache.getInstance().getPeople();
        Person father = people.get(person.getFatherID());
        if(father != null) {
            String fatherString = father.getFirstName() + " "
                    + father.getLastName() + "\n" + "Father";
            family.add(fatherString);
            familyPeople.put(iterator, father);
            iterator++;
        }
        Person mother = people.get(person.getMotherID());
        if(mother != null) {
            String motherString = mother.getFirstName() + " "
                    + mother.getLastName() + "\n" + "Mother";
            family.add(motherString);
            familyPeople.put(iterator, mother);
            iterator++;
        }
        Person spouse = people.get(person.getSpouseID());
        if (spouse != null) {
            String spouseString = spouse.getFirstName() + " "
                    + spouse.getLastName() + "\n" + "Spouse";
            family.add(spouseString);
            familyPeople.put(iterator, spouse);
            iterator++;
        }

        //Pull all the children to find the child if any
        Map<String, String> children = DataCache.getInstance().getChildren();
        Person child = people.get(children.get(person.getPersonID()));
        if(child != null) {
            String childString = child.getFirstName() + " "
                    + child.getLastName() + "\n" + "Child";
            family.add(childString);
            familyPeople.put(iterator, child);
            iterator++;
        }

        return family;
    }

    public HashMap getFamilyPersons() {
        return familyPeople;
    }

    public HashMap getPersonLifeEvents() {
        return personLifeEvents;
    }

    public HashMap<String, List<String>> getExpandableListDetail() {
        return newExpandableListDetail;
    }
}
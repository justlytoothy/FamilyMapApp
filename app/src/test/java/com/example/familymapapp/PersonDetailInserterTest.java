package com.example.familymapapp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import model.Person;
import model.User;
import request.RegisterRequest;
import result.EventResult;
import result.PersonResult;

public class PersonDetailInserterTest {
    private ServerProxy serverProxy;
    private User userOne;
    private PersonDetailInserter personDetailInserter;
    @Before
    public void setUp() throws Exception {
        serverProxy = new ServerProxy("90","localhost");
        serverProxy.clear();
        personDetailInserter = new PersonDetailInserter();
        userOne = new User("userOne","passOne","emailOne","user","one","m","userOneID");
    }
    @Test
    public void chronologicalSort() {
        serverProxy.register(new RegisterRequest(userOne));
        PersonResult success = serverProxy.getFamily();
        Person person = new Person(success.getAssociatedUsername(), success.getPersonID(), success.getFirstName(), success.getLastName(), success.getGender(), success.getFatherID(), success.getMotherID(), success.getSpouseID());
        DataCache.getInstance().setCurrPerson(person);
        DataCache.getInstance().setPeople(success.getData());
        EventResult eventResult = serverProxy.getEvents();
        DataCache.getInstance().setEvents(eventResult.getData());
        DataCache.getInstance().setActivityPerson(person);
        DataCache.getInstance().setFilters(new Filter());
        DataCache.getInstance().storePersonsEvents();
        personDetailInserter.getData(person);
        List<String> events = personDetailInserter.findLifeEvents(person);
        assert(events.get(0).substring(0,5).equals("Birth"));

    }
    @Test
    public void chronologicalSortNeg() {
        serverProxy.register(new RegisterRequest(userOne));
        PersonResult success = serverProxy.getFamily();
        Person person = new Person(success.getAssociatedUsername(), success.getPersonID(), success.getFirstName(), success.getLastName(), success.getGender(), success.getFatherID(), success.getMotherID(), success.getSpouseID());
        DataCache.getInstance().setCurrPerson(person);
        DataCache.getInstance().setPeople(success.getData());
        EventResult eventResult = serverProxy.getEvents();
        DataCache.getInstance().setEvents(eventResult.getData());
        DataCache.getInstance().setActivityPerson(person);
        DataCache.getInstance().setFilters(new Filter());
        DataCache.getInstance().storePersonsEvents();
        personDetailInserter.getData(person);
        List<String> events = personDetailInserter.findLifeEvents(person);
        assert(events.get(2).substring(0,8).equals("Marriage"));
    }
}
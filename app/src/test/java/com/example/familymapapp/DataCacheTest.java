package com.example.familymapapp;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import model.Event;
import model.Person;
import model.User;
import request.RegisterRequest;
import result.EventResult;
import result.PersonResult;

public class DataCacheTest {
    private ServerProxy serverProxy;
    private User userOne;

    @Before
    public void setUp() throws Exception {
        serverProxy = new ServerProxy("90","localhost");
        serverProxy.clear();
        userOne = new User("userOne","passOne","emailOne","user","one","m","userOneID");
    }

    @Test
    public void relationships() {
        serverProxy.register(new RegisterRequest(userOne));
        PersonResult success = serverProxy.getFamily();
        DataCache.getInstance().setCurrPerson(new Person(success.getAssociatedUsername(), success.getPersonID(), success.getFirstName(), success.getLastName(), success.getGender(), success.getFatherID(), success.getMotherID(), success.getSpouseID()));
        DataCache.getInstance().setPeople(success.getData());
        assert(DataCache.getInstance().getFatherSide().size() > 0);
        assert(DataCache.getInstance().getMotherSide().size() > 0);

    }
    @Test
    public void relationshipsNeg() {
        serverProxy.register(new RegisterRequest(userOne));
        PersonResult success = serverProxy.getFamily();
        DataCache.getInstance().setCurrPerson(new Person(success.getAssociatedUsername(), success.getPersonID(), success.getFirstName(), success.getLastName(), success.getGender(), success.getFatherID(), success.getMotherID(), success.getSpouseID()));
        DataCache.getInstance().setPeople(success.getData());
        assert(DataCache.getInstance().getChildren().size() == 30);
    }
    @Test
    public void search() {
        serverProxy.register(new RegisterRequest(userOne));
        PersonResult success = serverProxy.getFamily();
        DataCache.getInstance().setCurrPerson(new Person(success.getAssociatedUsername(), success.getPersonID(), success.getFirstName(), success.getLastName(), success.getGender(), success.getFatherID(), success.getMotherID(), success.getSpouseID()));
        DataCache.getInstance().setPeople(success.getData());
        EventResult eventResult = serverProxy.getEvents();
        DataCache.getInstance().setEvents(eventResult.getData());
        List<SearchResult> searchResults = DataCache.getInstance().search("user");
        assert(searchResults.size() == 1);
    }
    @Test
    public void searchNeg() {
        serverProxy.register(new RegisterRequest(userOne));
        PersonResult success = serverProxy.getFamily();
        DataCache.getInstance().setCurrPerson(new Person(success.getAssociatedUsername(), success.getPersonID(), success.getFirstName(), success.getLastName(), success.getGender(), success.getFatherID(), success.getMotherID(), success.getSpouseID()));
        DataCache.getInstance().setPeople(success.getData());
        EventResult eventResult = serverProxy.getEvents();
        DataCache.getInstance().setEvents(eventResult.getData());
        List<SearchResult> searchResults = DataCache.getInstance().search("&^%##@");
        assert(searchResults.size() == 0);
    }
    @Test
    public void filter() {
        serverProxy.register(new RegisterRequest(userOne));
        PersonResult success = serverProxy.getFamily();
        DataCache.getInstance().setCurrPerson(new Person(success.getAssociatedUsername(), success.getPersonID(), success.getFirstName(), success.getLastName(), success.getGender(), success.getFatherID(), success.getMotherID(), success.getSpouseID()));
        DataCache.getInstance().setPeople(success.getData());
        EventResult eventResult = serverProxy.getEvents();
        DataCache.getInstance().setEvents(eventResult.getData());
        DataCache.getInstance().setFilters(new Filter(false,false,false,false,false));
        List<Event> filteredEvents = DataCache.getInstance().passesFilter();
        assert(0 == filteredEvents.size());
    }
    @Test
    public void filterNeg() {
        serverProxy.register(new RegisterRequest(userOne));
        PersonResult success = serverProxy.getFamily();
        DataCache.getInstance().setCurrPerson(new Person(success.getAssociatedUsername(), success.getPersonID(), success.getFirstName(), success.getLastName(), success.getGender(), success.getFatherID(), success.getMotherID(), success.getSpouseID()));
        DataCache.getInstance().setPeople(success.getData());
        EventResult eventResult = serverProxy.getEvents();
        DataCache.getInstance().setEvents(eventResult.getData());
        List<Event> filteredEvents = DataCache.getInstance().passesFilter();
        assert(filteredEvents.size() > 0);
    }

}
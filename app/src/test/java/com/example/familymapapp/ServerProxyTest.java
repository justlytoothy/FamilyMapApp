package com.example.familymapapp;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOError;
import java.io.IOException;

import model.Person;
import model.User;
import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

public class ServerProxyTest {
    private ServerProxy serverProxy;
    private User userOne;
    @Before
    public void setUp() throws Exception {
        serverProxy = new ServerProxy("90","localhost");
        serverProxy.clear();
        userOne = new User("userOne","passOne","emailOne","user","one","m","userOneID");
        //String username, String password, String email, String firstName, String lastName, String gender, String personID
    }


    @Test
    public void testLogin() {
        serverProxy.register(new RegisterRequest(userOne));
        LoginResult success = serverProxy.login(new LoginRequest("userOne","passOne"));
        assert(success.isSuccess());
    }

    @Test
    public void testRegister() {
        RegisterResult success = serverProxy.register(new RegisterRequest(userOne));
        assert(success.isSuccess());
    }

    @Test
    public void testGetFamily() {
        serverProxy.register(new RegisterRequest(userOne));
        PersonResult success = serverProxy.getFamily();
        assert(success.isSuccess());
    }

    @Test
    public void testGetEvents() {
        serverProxy.register(new RegisterRequest(userOne));
        EventResult success = serverProxy.getEvents();
        assert(success.isSuccess());
    }
    @Test
    public void testLoginNeg() {
        serverProxy.register(new RegisterRequest(userOne));
        LoginResult loginResult = serverProxy.login(new LoginRequest("not","correct"));
        assert(!loginResult.isSuccess());
    }

    @Test
    public void testRegisterNeg() {
        serverProxy.register(new RegisterRequest("user","pass","email","first","last","m"));
        RegisterResult failed = serverProxy.register(new RegisterRequest("user","pass","email","first","last","m"));
        assert(!failed.isSuccess());
    }

    @Test
    public void testGetFamilyNeg() {
        PersonResult failed = serverProxy.getFamily();
        assert(!failed.isSuccess());
    }

    @Test
    public void testGetEventsNeg() {
        EventResult failed = serverProxy.getEvents();
        assert(!failed.isSuccess());
    }
}
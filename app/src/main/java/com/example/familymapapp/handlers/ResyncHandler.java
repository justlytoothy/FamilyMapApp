package com.example.familymapapp.handlers;

import android.os.AsyncTask;
import android.widget.Toast;


import com.example.familymapapp.DataCache;
import com.example.familymapapp.ServerProxy;
import com.example.familymapapp.SettingsActivity;

import model.Person;
import request.LoginRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;


/**
 * Created by jakeg on 4/14/2018.
 */

public class ResyncHandler extends AsyncTask<LoginRequest, Void, LoginResult> {

    private SettingsActivity settingsActivity;

    private PersonResult personResponse;

    private EventResult eventResponse;

    public ResyncHandler(SettingsActivity activity){
        this.settingsActivity = activity;
    }


    @Override
    protected LoginResult doInBackground(LoginRequest... loginRequests) {
        ServerProxy serverProxy = new ServerProxy("90","10.0.2.2");

        LoginResult response = serverProxy.login(loginRequests[0]);

        personResponse = serverProxy.getFamily();
        DataCache.getInstance().setCurrPerson(new Person(personResponse.getAssociatedUsername(),personResponse.getPersonID(),personResponse.getFirstName(), personResponse.getLastName(),personResponse.getGender(),personResponse.getFatherID(),personResponse.getMotherID(), personResponse.getSpouseID()));
        eventResponse = serverProxy.getEvents();

        return response;
    }

    @Override
    protected void onPostExecute(LoginResult response) {
        if(response.getUsername() != null){

            DataCache.getInstance().setCurrPerson(new Person(personResponse.getAssociatedUsername(),personResponse.getPersonID(),personResponse.getFirstName(), personResponse.getLastName(),personResponse.getGender(),personResponse.getFatherID(),personResponse.getMotherID(), personResponse.getSpouseID()));

            DataCache.getInstance().setPeople(personResponse.getData());

            DataCache.getInstance().setEvents(eventResponse.getData());

            DataCache.getInstance().storePersonsEvents();

            settingsActivity.finish();
            return;
        }
        else{
            Toast.makeText(settingsActivity, "Failed to re-sync.", Toast.LENGTH_SHORT).show();
            return;
        }
    }

}
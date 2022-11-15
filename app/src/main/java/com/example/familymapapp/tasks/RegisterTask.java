package com.example.familymapapp.tasks;

import android.os.AsyncTask;

import com.example.familymapapp.ServerProxy;

import request.RegisterRequest;
import result.RegisterResult;

public class RegisterTask extends AsyncTask<RegisterRequest,Void, RegisterResult> {
    private String host = "localhost";
    private String port = "90";

    public interface Listener {
        void onError(Error e);
        void registerComplete(RegisterResult res);
    }

    private Listener listener;

    // Constructor for RegisterTask
    public RegisterTask(Listener listener) {
        this.listener = listener;

    }

    @Override
    protected RegisterResult doInBackground(RegisterRequest... registerRequests) {
        if (registerRequests.length == 0) {
            return null;
        }
        // Connect with server through proxy
        RegisterResult response = ServerProxy.register(host, port, registerRequests[0]);
        return response;
    }

    @Override
    protected void onPostExecute(RegisterResult res) {
        listener.registerComplete(res);
    }
}

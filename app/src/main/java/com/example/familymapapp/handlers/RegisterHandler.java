package com.example.familymapapp.handlers;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapapp.DataCache;
import com.example.familymapapp.ServerProxy;
import com.google.gson.Gson;

import request.RegisterRequest;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

public class RegisterHandler implements Runnable  {
    private static final String TAG = "Register Task";
    private final Handler handler;
    private RegisterRequest registerRequest;

    public RegisterHandler(Handler handler, RegisterRequest registerRequest) {
        this.handler = handler;
        this.registerRequest = registerRequest;
    }
    private void sendMessage(RegisterResult result) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean("success", result.isSuccess());
        messageBundle.putString("message",result.getMessage());
        messageBundle.putString("ob", new Gson().toJson(result));
        message.setData(messageBundle);
        handler.sendMessage(message);
    }
    private void sendMessage(PersonResult result) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean("success", result.isSuccess());
        messageBundle.putString("message",result.getMessage());
        messageBundle.putString("ob", new Gson().toJson(result));
        message.setData(messageBundle);
        handler.sendMessage(message);
    }
    @Override
    public void run() {
        RegisterResult res = ServerProxy.register(this.registerRequest);
        if (res.isSuccess()) {
            DataCache.getInstance().setAuthToken(res.getAuthtoken());
            PersonResult personResult = ServerProxy.getFamily();
            sendMessage(personResult);
        }
        else {
            sendMessage(res);
        }
    }

}

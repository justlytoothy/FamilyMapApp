package com.example.familymapapp.handlers;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.familymapapp.DataCache;
import com.example.familymapapp.ServerProxy;
import com.google.gson.Gson;

import request.LoginRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;

public class LoginHandler implements Runnable  {
    private static final String TAG = "Login Handler";
    private final Handler handler;
    private LoginRequest loginRequest;

    public LoginHandler(Handler handler, LoginRequest loginRequest) {
        this.handler = handler;
        this.loginRequest = loginRequest;
    }
    private void sendMessage(LoginResult result) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean("success", result.isSuccess());
        messageBundle.putString("message",result.getMessage());
        messageBundle.putString("ob", new Gson().toJson(result));
        message.setData(messageBundle);
        handler.sendMessage(message);
    }
    private void sendMessage(PersonResult result,EventResult eventResult) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putBoolean("success", result.isSuccess());
        messageBundle.putString("message",result.getMessage());
        messageBundle.putString("ob", new Gson().toJson(result));
        messageBundle.putString("event", new Gson().toJson(eventResult));
        message.setData(messageBundle);
        handler.sendMessage(message);
    }
    @Override
    public void run() {
        LoginResult res = ServerProxy.login(this.loginRequest);
        if (res.isSuccess()) {
            DataCache.getInstance().setAuthToken(res.getAuthToken());
            PersonResult personResult = ServerProxy.getFamily();
            EventResult eventResult = ServerProxy.getEvents();
            sendMessage(personResult,eventResult);
        }
        else {
            sendMessage(res);
        }

    }

}

package com.example.familymapapp.handlers;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.familymapapp.ServerProxy;
import com.google.gson.Gson;

import result.PersonResult;

public class FamilyHandler implements Runnable  {
    private static final String TAG = "Family Handler";
    private final Handler handler;

    public FamilyHandler(Handler handler) {
        this.handler = handler;
    }
    private void sendMessage(PersonResult result) {
        Message message = Message.obtain();
        Bundle messageBundle = new Bundle();
        messageBundle.putString("ob", new Gson().toJson(result));
        message.setData(messageBundle);
        handler.sendMessage(message);
    }
    @Override
    public void run() {
        ServerProxy serverProxy = new ServerProxy("90","10.0.2.2");
        PersonResult res = serverProxy.getFamily();
        sendMessage(res);
    }

}

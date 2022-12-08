package com.example.familymapapp;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.Gson;

import model.Person;
import request.LoginRequest;
import request.RegisterRequest;
import result.ClearResult;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

public class ServerProxy {
    private static final String TAG = "Server Proxy";
    private String port = "90";
    private String host = "10.0.2.2";

    public ServerProxy(String port, String host) {
        this.port = port;
        this.host = host;
    }


    public LoginResult login(LoginRequest request) {
        try {
            URL url = new URL("http://" + host + ":" + port + "/user/login");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");

            http.setDoOutput(true); // There is a request body

            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();
            Gson gson = new Gson();

            // This is the JSON string we will send in the HTTP request body
            String reqData = gson.toJson(request);

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();

            // Write the JSON data to the request body
            writeString(reqData, reqBody);

            // Close the request body output stream, indicating that the
            // request is complete
            reqBody.close();
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            LoginResult loginResult = gson.fromJson(respData, LoginResult.class);
            if (loginResult.isSuccess()) {
                DataCache.getInstance().setAuthToken(loginResult.getAuthToken());
            }
            return loginResult;
        } catch (IOException e) {
            e.printStackTrace();
            return new LoginResult("Error logging in",false);
        }
    }

    public RegisterResult register(RegisterRequest request) {
        try {
            URL url = new URL("http://" + host + ":" + port + "/user/register");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            http.setRequestMethod("POST");

            http.setDoOutput(true); // There is a request body

            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();
            Gson gson = new Gson();

            // This is the JSON string we will send in the HTTP request body
            String reqData = gson.toJson(request);

            // Get the output stream containing the HTTP request body
            OutputStream reqBody = http.getOutputStream();

            // Write the JSON data to the request body
            writeString(reqData, reqBody);

            // Close the request body output stream, indicating that the
            // request is complete
            reqBody.close();
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            RegisterResult registerResult = gson.fromJson(respData, RegisterResult.class);
            if (registerResult.isSuccess()) {
                DataCache.getInstance().setAuthToken(registerResult.getAuthtoken());
            }
            return registerResult;
        } catch (IOException e) {
            e.printStackTrace();
            return new RegisterResult("Error registering",false);
        }
    }

    public PersonResult getFamily() {
        String authToken = DataCache.getInstance().getAuthToken();
        try {
            URL url = new URL("http://" + host + ":" + port + "/person");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            PersonResult personResult = new Gson().fromJson(respData, PersonResult.class);
            return personResult;
        } catch (IOException e) {
            e.printStackTrace();
            return new PersonResult("Error getting people",false);
        }
    }

    public EventResult getEvents() {
        String authToken = DataCache.getInstance().getAuthToken();
        try {
            URL url = new URL("http://" + host + ":" + port + "/event");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("GET");
            http.setDoOutput(false);
            http.addRequestProperty("Authorization", authToken);
            http.addRequestProperty("Accept", "application/json");
            http.connect();
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            EventResult eventResult = new Gson().fromJson(respData, EventResult.class);
            return eventResult;
        } catch (IOException e) {
            e.printStackTrace();
            return new EventResult("Error getting events",false);
        }
    }
    public ClearResult clear() {
        try {
            URL url = new URL("http://" + host + ":" + port + "/clear");
            HttpURLConnection http = (HttpURLConnection)url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(false);
            http.connect();
            InputStream respBody = http.getInputStream();
            String respData = readString(respBody);
            ClearResult clearResult = new Gson().fromJson(respData, ClearResult.class);
            return clearResult;
        } catch (IOException e) {
            e.printStackTrace();
            return new ClearResult("Error clearing database",false);
        }
    }


    private String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}

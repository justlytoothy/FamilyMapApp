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

import request.LoginRequest;
import request.RegisterRequest;
import result.EventResult;
import result.LoginResult;
import result.PersonResult;
import result.RegisterResult;

public class ServerProxy {
    private static final String TAG = "Server Proxy";
    private static final String port = "90";
    private static final String host = "10.0.2.2";


    public static LoginResult login(LoginRequest request) {
        try {
            URL url = new URL("http://" + host + ":" + port + "/user/login");
            Log.d(TAG,url.toString());
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
            return loginResult;
        } catch (IOException e) {
            e.printStackTrace();
            return new LoginResult("Error logging in",false);
        }
    }

    public static RegisterResult register(RegisterRequest request) {
        try {
            URL url = new URL("http://" + host + ":" + port + "/user/register");
            Log.d(TAG,url.toString());
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
            return registerResult;
        } catch (IOException e) {
            e.printStackTrace();
            return new RegisterResult("Error registering",false);
        }
    }

    public static PersonResult getFamily() {
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

    public static EventResult getEvents(String authToken) {
        try {
            URL url = new URL("http://" + host + ":" + port + "/event");

            // Start constructing our HTTP request
            HttpURLConnection http = (HttpURLConnection)url.openConnection();

            // Specify that we are sending an HTTP GET request
            http.setRequestMethod("GET");

            // Indicate that this request will mot contain an HTTP request body
            http.setDoOutput(false);

            // Add an auth token to the request in the HTTP "Authorization" header
            http.addRequestProperty("Authorization", authToken);

            // Specify that we would like to receive the server's response in JSON
            // format by putting an HTTP "Accept" header on the request (this is not
            // necessary because our server only returns JSON responses, but it
            // provides one more example of how to adda  header to an HTTP request).
            http.addRequestProperty("Accept", "application/json");

            // Connect to the server and send the HTTP request
            http.connect();

            // By the time we get here, the HTTP response has been received from the server.
            // Check to make sure that the HTTP response from the server contains a 200
            // status code, which means "success". treat anything else as a failure.
            if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                // Get the input stream containing the HTTP response body
                InputStream respBody = http.getInputStream();

                // Extract JSON data from the HTTP response body
                String respData = readString(respBody);

                // Display the JSON data returned from the server
                System.out.println(respData);
            }
            else {
                // The HTTP response status code indicates an error
                // occurred, so print out the message from the HTTP response
                System.out.println("ERROR: " + http.getResponseMessage());

                // Get the error stream containing the HTTP response body (if any)
                InputStream respBody = http.getErrorStream();

                // Extract data from the HTTP response body
                String respData = readString(respBody);

                // Display the data returned from the server
                System.out.println(respData);

            }
        } catch (IOException e) {
            e.printStackTrace();
            return new EventResult("Error getting events",false);
        }
        return null;
    }

    private static String readString(InputStream is) throws IOException {
        StringBuilder sb = new StringBuilder();
        InputStreamReader sr = new InputStreamReader(is);
        char[] buf = new char[1024];
        int len;
        while ((len = sr.read(buf)) > 0) {
            sb.append(buf, 0, len);
        }
        return sb.toString();
    }

    private static void writeString(String str, OutputStream os) throws IOException {
        OutputStreamWriter sw = new OutputStreamWriter(os);
        sw.write(str);
        sw.flush();
    }
}

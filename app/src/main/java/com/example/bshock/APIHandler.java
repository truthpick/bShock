package com.example.bshock;

import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIHandler {
    private static final String TAG = "APIHandler";

    private static RequestQueue queue;
    private static final String APIUrlBase = "https://do.pishock.com/api/apioperate";

    static String username;
    static String name = "bShock";
    static String code;
    static String apikey;

    MainActivity mainActivity;
    SharedPreferences sharedPreferences;

    public APIHandler(MainActivity mainActivity){

    }

    public static void doShock(MainActivity mainActivity, String username, String code, int intensity, float duration, String apikey, int mode){
        String op = String.valueOf(mode); // op values determine which operation the shocker carries out

        if (queue == null){
            queue = Volley.newRequestQueue(mainActivity);
        }

        // TODO
        BigDecimal preciseDuration = new BigDecimal(duration).setScale(1, RoundingMode.DOWN);
        

        // Create JSON body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Username", username);
            jsonBody.put("Name", name);
            jsonBody.put("Code", code);
            if (mode != 2) {
                // Add this if shock or vibration mode. Beep has no intensity attribute
                jsonBody.put("Intensity", intensity);
            }
            jsonBody.put("Duration", preciseDuration.floatValue());//String.format("%.1f", duration)); // format to avoid floating point precision error
            jsonBody.put("Apikey", apikey);
            jsonBody.put("Op", op);
        } catch (Exception e) {
            Log.e(TAG, "doShock: JSON error: " + e.getMessage());
            return;
        }

        // Set up listeners for the request response
        Response.Listener<String> listener = response -> {
            Log.d(TAG, "doShock: got response: " + response);

        };

        Response.ErrorListener error = error1 -> {
            Log.d(TAG, "doShock: error: " + error1);
        };

        // Create request with JSON body and headers
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                APIUrlBase,
                listener,
                error
        ) {
            @Override
            public byte[] getBody() {
                return jsonBody.toString().getBytes(); // Send JSON body as bytes
            }

            @Override
            public String getBodyContentType() {
                return "application/json"; // Set correct content type
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Log.d(TAG, "doShock: request formed: " + jsonBody);

        queue.add(stringRequest);
    }

    public static void getShockerInfo(){

    }

    public static String getUsername() {
        return username;
    }

    public static String getCode() {
        return code;
    }

    public static String getApikey() {
        return apikey;
    }

    public void setUser(String username){
        this.username = username;
    }

    public void setShare(String shareCode){
        this.code = shareCode;
    }

    public void setApikey(String apikey){
        this.apikey = apikey;
    }
}

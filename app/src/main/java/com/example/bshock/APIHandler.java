package com.example.bshock;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class APIHandler {
    private static final String TAG = "APIHandler";

    private static RequestQueue queue;
    private static final String APIUrlBase = "https://do.pishock.com/api/apioperate";

    // API docs found at: https://apidocs.pishock.com/

    //Example: curl -d '{"Username":"puppy73","Name":"TG_Bot_Script",
    // "Code":"17519CD8GAP","Intensity":"6",
    // "Duration":"1","Apikey":"5c678926-d19e-4f86-42ad-21f5a76126db",
    // "Op":"0"}' -H 'Content-Type: application/json' https://do.pishock.com/api/apioperate

    static String username;
    static String name = "bShock";
    static String code;
    static String apikey;

    MainActivity mainActivity;

    public APIHandler(MainActivity mainActivity){
        // TODO set this up to get values from auth
        if(mainActivity.debug) {
            this.username = BuildConfig.USER;
            this.code = BuildConfig.CODE; // sharecode // TODO hardcoded
            this.apikey = BuildConfig.API_KEY;
        }
        else{
            // TODO
            Log.d(TAG, "APIHandler: hit non-debug block in apihandler constructor");
        }
    }

    public static void doShock(MainActivity mainActivity, int intensity, int duration){
        String op = "0"; // op values determine which operation the shocker carries out

        if (queue == null){
            queue = Volley.newRequestQueue(mainActivity);
        }

        // Create JSON body
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("Username", username);
            jsonBody.put("Name", name);
            jsonBody.put("Code", code);
            jsonBody.put("Intensity", intensity);
            jsonBody.put("Duration", duration);
            jsonBody.put("Apikey", apikey);
            jsonBody.put("Op", op);
        } catch (Exception e) {
            Log.e(TAG, "doShock: JSON error: " + e.getMessage());
            return;
        }

        // Set up listeners for the request response
        Response.Listener<JSONObject> listener = response -> {
            Log.d(TAG, "doShock: got response: " + response);
        };

        Response.ErrorListener error = error1 -> {
            Log.d(TAG, "doShock: error: " + error1);
        };

        // Create request with JSON body and headers
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST,
                APIUrlBase,
                jsonBody,
                listener,
                error
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        Log.d(TAG, "doShock: request formed: " + jsonBody);

        queue.add(jsonObjectRequest);
    }

    public void doVibrate(MainActivity mainActivity){
        String op = "1";

    }

    public void doBeep(MainActivity mainActivity){
        String op = "2";

    }

    public static void getShockerInfo(){

    }
}

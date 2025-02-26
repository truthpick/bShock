package com.example.bshock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bshock.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public boolean debug = false;
    String debugAPIKey = BuildConfig.API_KEY;

    ActivityMainBinding binding;

    // Initial values
    private int intensity = 0;
    private float duration = 1;
    private int mode = 0; // Mode of operation: 0 = shock, 1 = vibrate, 2 = beep
    private String modeName = "Shock";

    APIHandler apiHandler;
    String username = "";
    String APIKey = "";
    String sharecode = "";

    private Vibrator vibrator;
    private VibrationEffect shortVibration;

    private ActivityResultLauncher<Intent> activityResultLauncher;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up viewbinding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Show initial values (always do this from the in-use value!)
        binding.setIntensityButton.setText("Intensity: " + intensity);
        binding.setModeButton.setText("Mode: " + modeName);
        binding.durationButton.setText("Duration: " + duration);

        // Set up vibration
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        shortVibration = VibrationEffect.createOneShot(75, VibrationEffect.DEFAULT_AMPLITUDE);

        // Load saved values for creds
        sharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        apiHandler = new APIHandler(this);
        username = sharedPreferences.getString("username", "");
        APIKey = sharedPreferences.getString("apikey","");
        sharecode = sharedPreferences.getString("sharecode", "");

        apiHandler.setUser(username);
        apiHandler.setApikey(APIKey);
        apiHandler.setShare(sharecode);

        // Register ActivityResultLauncher to get credentials back from loginactivity
        activityResultLauncher =
                registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                        result -> {
                            if (result.getResultCode() == MainActivity.RESULT_OK){
                                Intent data = result.getData();
                                saveCredentials(data);
                            }
                            if (result.getResultCode() == MainActivity.RESULT_CANCELED){
                                // TODO figure out why this isn't called when back press in loginactivity
                                Log.d(TAG, "activityResultLauncher: cancel result");
                            }
                        });

        if (debug) {
            setDebugTools();
        }
        else {
            binding.debugUsername.setVisibility(View.INVISIBLE);
            binding.debugAPIKey.setVisibility(View.INVISIBLE);
            binding.debugSharecode.setVisibility(View.INVISIBLE);
        }
    }

    private void setDebugTools() {
        binding.debugUsername.setText("user: " + username);
        binding.debugAPIKey.setText("APIkey: " + APIKey);
        binding.debugSharecode.setText("Sharecode: " + sharecode);
    }

    public void setIntensity(){
        String intensityText = "Intensity: ";

        if (intensity < 100){
            intensity += 5;
        } else if (intensity >= 100){
            intensity = 0;
        }
        binding.setIntensityButton.setText(intensityText + intensity);
    }

    public void doSetIntensity(View v){
        setIntensity();
        Log.d(TAG, "doSetIntensity: setting intensity to: " + intensity);
    }

    public void setMode(){
        String modeText = "Mode: ";

        if (mode == 0){
            mode = 1;
            modeName = "Vibrate";
        } else if (mode == 1){
            mode = 2;
            modeName = "Beep";
        } else if (mode == 2){
            mode = 0;
            modeName = "Shock";
        } else {
            Log.e(TAG, "setMode: non 0/1/2 value for mode");
        }
        binding.setModeButton.setText(modeText + modeName);
        binding.sendCommandButton.setText(modeName);
    }

    public void toggleDuration(){
        String durationText = "Duration: ";

        if (duration <= 1){
            duration = 2;
        } else if (duration == 2){
            duration = 3;
        } else if (duration == 3){
            duration = 4;
        } else if (duration >= 4){
            duration = 1;
        } else {
            Log.e(TAG, "setMode: non 0/1/2 value for mode");
        }
        binding.durationButton.setText(durationText + duration);
    }

    public void doToggleDuration(View view){
        toggleDuration();
        Log.d(TAG, "doSetDuration: duration set to " + duration);
    }


    public void doSetMode(View view){
        setMode();
        Log.d(TAG, "doSetMode: mode set to mode " + mode + "(" + modeName +")");
    }

    public void doShock(View v){
        Log.d(TAG, "doShock: start hit");
        apiHandler.doShock(this, username, sharecode, intensity, duration, APIKey, mode);

        // do short vibration
        vibrator.cancel();
        vibrator.vibrate(shortVibration);
    }

    private void setDuration(){
        duration = Float.parseFloat(binding.durationET.getText().toString());
        Log.d(TAG, "setDuration: set duration to " + duration);
    }

    public void doSetDuration(View view){
        setDuration();
    }

    // TODO save creds to shared prefs
    public void launchLoginActivity(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        activityResultLauncher.launch(intent);
    }

    private void saveCredentials(Intent data){

        if (data != null) {
            username = data.getStringExtra("username");
            APIKey = data.getStringExtra("apikey");
            sharecode = data.getStringExtra("sharecode");
        } else {
            Log.d(TAG, "saveCredentials: null data passed back by ActivityResultLauncher");
            return;
        }

        if (username != null && APIKey != null && sharecode != null){
            Log.d(TAG, "saveCredentials: returned values non-null");
            Log.d(TAG, "saveCredentials: user: " + username + " apikey: " + APIKey + " sharecode: " + sharecode);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("apikey", APIKey);
            editor.putString("sharecode", sharecode);

            editor.apply();
        }

        if (debug) {
            setDebugTools();
        }
    }
}
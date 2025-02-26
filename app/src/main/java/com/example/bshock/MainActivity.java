package com.example.bshock;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bshock.databinding.ActivityMainBinding;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public boolean debug = true;
    String debugAPIKey = BuildConfig.API_KEY;

    ActivityMainBinding binding;

    // Initial values
    private int intensity = 1;
    private int duration = 1;
    private int mode = 0; // Mode of operation: 0 = shock, 1 = vibrate, 2 = beep
    private String modeName = "Shock";

    APIHandler apiHandler;

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
        binding.durationButton.setText("Duration: 1");

        // Create APIHandler object, which stores auth data
        apiHandler = new APIHandler(this);

    }

    public void setIntensity(){
        String intensityText = "Intensity: ";

        if (intensity == 1){
            intensity = 5;
        } else if (intensity == 5){
            intensity = 10;
        } else if (intensity == 10){
            intensity = 30;
        } else if (intensity == 30){
            intensity = 1;
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


    public void setDuration(){
        String durationText = "Duration: ";

        if (duration == 1){
            duration = 2;
        } else if (duration == 2){
            duration = 3;
        } else if (duration == 3){
            duration = 4;
        } else if (duration == 4){
            duration = 1;
        } else {
            Log.e(TAG, "setMode: non 0/1/2 value for mode");
        }
        binding.durationButton.setText(durationText + duration);
    }

    public void doSetDuration(View view){
        setDuration();
        Log.d(TAG, "doSetDuration: duration set to " + duration);
    }


    public void doSetMode(View view){
        setMode();
        Log.d(TAG, "doSetMode: mode set to mode " + mode + "(" + modeName +")");
    }

    public void doShock(View v){
        Log.d(TAG, "doShock: start hit");
        apiHandler.doShock(this, intensity, duration, mode);
    }
}
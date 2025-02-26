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

    public void doShock(View v){
        Log.d(TAG, "doShock: start hit");
        apiHandler.doShock(this, intensity, duration);
    }
}
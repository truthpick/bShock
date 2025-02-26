package com.example.bshock;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bshock.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    ActivityLoginBinding binding;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Get credentials from shared prefs (if applicable)
        sharedPreferences = getSharedPreferences("MY_PREFS", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        String APIKey = sharedPreferences.getString("apikey", "");
        String sharecode = sharedPreferences.getString("sharecode", "");

        // Populate edittexts with current values for credentials
        binding.usernameET.setText(username);
        binding.APIkeyET.setText(APIKey);
        binding.shareCodeET.setText(sharecode);
    }

    public void saveCredentials(View view){
        String usernameEntered = binding.usernameET.getText().toString();
        String apikeyEntered = binding.APIkeyET.getText().toString();
        String sharecodeEntered = binding.shareCodeET.getText().toString();

        Intent resultIntent = new Intent();
        resultIntent.putExtra("username", usernameEntered);
        resultIntent.putExtra("apikey", apikeyEntered);
        resultIntent.putExtra("sharecode", sharecodeEntered);

        setResult(RESULT_OK, resultIntent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this, "Credentials not saved", Toast.LENGTH_SHORT).show();
        Intent reusltIntent = new Intent();
        setResult(RESULT_CANCELED, reusltIntent);
        super.onBackPressed(); // Finish the activity normally
    }
}
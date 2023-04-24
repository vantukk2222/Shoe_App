package com.midterm.shoestore.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.Toast;

import com.midterm.shoestore.R;

public class AdminActivity extends AppCompatActivity {
    private Button leftAdmin_panel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        leftAdmin_panel = findViewById(R.id.leftAdmin_panel);
        leftAdmin_panel.setOnClickListener(view -> {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            preferences.edit().remove("uid").apply();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish(); // Đóng Activity hiện tại

            Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
        });
    }
}
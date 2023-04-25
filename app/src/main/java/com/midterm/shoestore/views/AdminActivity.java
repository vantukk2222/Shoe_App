package com.midterm.shoestore.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midterm.shoestore.R;
import com.midterm.shoestore.model.ShoeItem;

public class AdminActivity extends AppCompatActivity {
    private Button leftAdmin_panel;
    private Button addBtn;
    private EditText edtName, edtBrand, edtPrice, edtImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        edtName = findViewById(R.id.editTextShoeName);
        edtBrand = findViewById(R.id.editTextShoeBrand);
        edtPrice = findViewById(R.id.editTextShoePrice);
        edtImage = findViewById(R.id.editTextShoeImage);

        leftAdmin_panel = findViewById(R.id.leftAdmin_panel);
        leftAdmin_panel.setOnClickListener(view -> {

            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish(); // Đóng Activity hiện tại

        });
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(view-> {
            String strName = edtName.getText().toString().trim();
            String strBrand = edtBrand.getText().toString().trim();
            String strPrice = edtPrice.getText().toString().trim();
            String strImage = edtImage.getText().toString().trim();
            if(strName.equals(""))
            {
                edtName.setError("Error");
            }
            if(strBrand.equals("")) {
                edtBrand.setError("Error");
            }
            if(strPrice.equals("")) {
                edtPrice.setError("Error");
            }
            if(strImage.equals("")) {
                edtImage.setError("Error");
            }

            else{
                DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference("shoes");
                ShoeItem shoeItem = new ShoeItem(strName, strBrand, strImage, strPrice);
                shoesRef.push().setValue(shoeItem);
                finish();


            }
        });

    }
}
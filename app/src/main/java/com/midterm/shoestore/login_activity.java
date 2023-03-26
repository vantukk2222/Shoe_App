package com.midterm.shoestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class login_activity extends AppCompatActivity {
    EditText email, password;
    ImageView btn_login;
    TextView btn_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.txtmail_login);
        password = findViewById(R.id.txtpass_login);
        btn_login = findViewById(R.id.img_btn_login);
        btn_reg = findViewById(R.id.txt_reg);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login_activity.this, MainActivity.class);
                startActivity(i);

            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login_activity.this, register_activity.class);
                startActivity(i);
            }
        });

    }
}
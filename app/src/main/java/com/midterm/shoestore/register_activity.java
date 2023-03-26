package com.midterm.shoestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import javax.security.auth.login.LoginException;

public class register_activity extends AppCompatActivity {
    EditText email, pass, repass;
    ImageView btn_reg;
    TextView btn_return_login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.txtmail_reg);
        pass = findViewById(R.id.txt_pass_reg);
        repass = findViewById(R.id.txt_re_pass);
        btn_reg = findViewById(R.id.img_btn_reg);
        btn_return_login = findViewById(R.id.txt_reg_to_login);
        
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().equals("OK"))
                {
                    Intent i = new Intent(register_activity.this, information_person.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(register_activity.this, "Lỗi roài!!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_return_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                register_activity.this.finish();
            }
        });
    }
}
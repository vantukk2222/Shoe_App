package com.midterm.shoestore;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class information_person extends AppCompatActivity {
    TextView txtLeftEdit;
    ImageView img_btn_finish_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_person);
        img_btn_finish_reg = findViewById(R.id.img_btn_finish_reg);
        txtLeftEdit = findViewById(R.id.txtLeftEdit);

        img_btn_finish_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(information_person.this, MainActivity.class);
                startActivity(i);
            }
        });
        txtLeftEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                information_person.this.finish();
            }
        });
    }
}
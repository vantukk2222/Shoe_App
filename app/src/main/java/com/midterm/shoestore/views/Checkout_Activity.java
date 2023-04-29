package com.midterm.shoestore.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.midterm.shoestore.R;

public class Checkout_Activity extends AppCompatActivity {
    private TextView tvleft_co;
    private ImageView imgcheckcart_co;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_orders, new ordersFragment()).commit();

        tvleft_co = findViewById(R.id.tvleft_co);
        imgcheckcart_co = findViewById(R.id.imgcheckcart_co);

        tvleft_co.setOnClickListener(view -> finish());
        imgcheckcart_co.setOnClickListener(view ->{
            Intent intent_cart = new Intent(getApplicationContext(), show_list_cart.class);
            startActivity(intent_cart);
        });
    }
}
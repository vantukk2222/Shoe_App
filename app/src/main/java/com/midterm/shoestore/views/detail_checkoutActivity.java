package com.midterm.shoestore.views;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.midterm.shoestore.R;
import com.midterm.shoestore.model.Order;

public class detail_checkoutActivity extends AppCompatActivity {
    private Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_checkout);
        order = getIntent().getParcelableExtra("Order");
    }
}
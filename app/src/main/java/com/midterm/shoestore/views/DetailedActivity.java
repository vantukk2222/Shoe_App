package com.midterm.shoestore.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.midterm.shoestore.R;
import com.midterm.shoestore.model.ShoeItem;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    private ImageView shoeImageView;
    private TextView shoeNameTV, shoeBrandNameTV, shoePriceTV, btn_left;
    //private AppCompatButton addToCartBtn;
    private ShoeItem shoe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);

        shoe = getIntent().getParcelableExtra("shoeItem");
        initializeVariables();


        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailedActivity.this.finish();
            }
        });
        if (shoe != null) {
            setDataToWidgets();
        }

        /*addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertToRoom();
            }
        });*/

    }


    private void setDataToWidgets() {
        shoeNameTV.setText(shoe.getShoeName());
        shoeBrandNameTV.setText(shoe.getShoeBrandName());
        shoePriceTV.setText(String.valueOf(shoe.getShoePrice()));
        shoeImageView.setImageResource(shoe.getShoeImage());

    }

    private void initializeVariables() {

        //shoeCartList = new ArrayList<>();
        shoeImageView = findViewById(R.id.detailActivityShoeIV);
        shoeNameTV = findViewById(R.id.detailActivityShoeNameTv);
        shoeBrandNameTV = findViewById(R.id.detailActivityShoeBrandNameTv);
        shoePriceTV = findViewById(R.id.detailActivityShoePriceTv);
        btn_left = findViewById(R.id.txtLeftdetail);
        //addToCartBtn = findViewById(R.id.detailActivityAddToCartBtn);

        //viewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }
}
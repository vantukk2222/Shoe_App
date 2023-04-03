package com.midterm.shoestore.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.midterm.shoestore.R;
import com.midterm.shoestore.adapter.ShoeItemAdapter;
import com.midterm.shoestore.model.ShoeItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ShoeItemAdapter.ShoeClickedListeners {

    private RecyclerView recyclerView;
    private List<ShoeItem> shoeItemList;
    private ShoeItemAdapter adapter;
    private ImageView cartImageView;
    private DrawerLayout drawerlayout;
    private ImageView userImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables();
        setUpList();

        adapter.setShoeItemList(shoeItemList);
        recyclerView.setAdapter(adapter);


        cartImageView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CartActivity.class)));

//        userImageView.setOnClickListener(view -> {
//            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.clear();
//            editor.apply();
//
//            MainActivity.this.finish();
//            startActivity(new Intent(MainActivity.this, LoginActivity.class));
//        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)

            Toast.makeText(getApplicationContext(), "back press!", Toast.LENGTH_LONG).show();

        return false;
        // Disable back button..............
    }
    private void setUpList() {
        shoeItemList.add(new ShoeItem("Nike Revolution", "Nike", R.drawable.nike_revolution_road, 15));
        shoeItemList.add(new ShoeItem("Nike Flex Run 2021", "NIKE", R.drawable.flex_run_road_running, 20));
        shoeItemList.add(new ShoeItem("Court Zoom Vapor", "NIKE", R.drawable.nikecourt_zoom_vapor_cage, 18));
        shoeItemList.add(new ShoeItem("EQ21 Run COLD.RDY", "ADIDAS", R.drawable.adidas_eq_run, 16.5));
        shoeItemList.add(new ShoeItem("Adidas Ozelia", "ADIDAS", R.drawable.adidas_ozelia_shoes_grey, 20));
        shoeItemList.add(new ShoeItem("Adidas Questar", "ADIDAS", R.drawable.adidas_questar_shoes, 22));
        shoeItemList.add(new ShoeItem("Adidas Questar", "ADIDAS", R.drawable.adidas_questar_shoes, 12));
        shoeItemList.add(new ShoeItem("Adidas Ultraboost", "ADIDAS", R.drawable.adidas_ultraboost, 15));

    }

    private void initializeVariables() {

        cartImageView = findViewById(R.id.cartIv);
//        userImageView = findViewById(R.id.userIv);
        drawerlayout = findViewById(R.id.coordinatorLayout);
        shoeItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ShoeItemAdapter(this);

    }

    @Override
    public void onCardClicked(ShoeItem shoe) {

//        Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
//        intent.putExtra("shoeItem", shoe);
//        startActivity(intent);
    }


}
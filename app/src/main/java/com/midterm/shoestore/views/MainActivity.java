package com.midterm.shoestore.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midterm.shoestore.R;
import com.midterm.shoestore.adapter.ShoeItemAdapter;
import com.midterm.shoestore.model.ShoeItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ShoeItemAdapter.ShoeClickedListeners {

    private RecyclerView recyclerView;
    private List<ShoeItem> shoeItemList;
    private ShoeItemAdapter adapter;
    private CoordinatorLayout coordinatorLayout;
    private ImageView cartImageView;
    private ImageView userImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeVariables();
        setUpList();

        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shoeItemList = new ArrayList<>();
        // Thêm dữ liệu vào danh sách sản phẩm giày tại đây
        //adapter = new ShoeItemAdapter((ShoeItemAdapter.ShoeClickedListeners) shoeItemList);
        adapter.setShoeItemList(shoeItemList);

        recyclerView.setAdapter(adapter);


        cartImageView.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, CartActivity.class)));

        userImageView.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            MainActivity.this.finish();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK)

            Toast.makeText(getApplicationContext(), "back press!", Toast.LENGTH_LONG).show();

        return false;
        // Disable back button..............
    }
    /*private void setUpList() {
        shoeItemList.add(new ShoeItem("Nike Revolution", "Nike", R.drawable.nike_revolution_road, 15));
        shoeItemList.add(new ShoeItem("Nike Flex Run 2021", "NIKE", R.drawable.flex_run_road_running, 20));
        shoeItemList.add(new ShoeItem("Court Zoom Vapor", "NIKE", R.drawable.nikecourt_zoom_vapor_cage, 18));
        shoeItemList.add(new ShoeItem("EQ21 Run COLD.RDY", "ADIDAS", R.drawable.adidas_eq_run, 16.5));
        shoeItemList.add(new ShoeItem("Adidas Ozelia", "ADIDAS", R.drawable.adidas_ozelia_shoes_grey, 20));
        shoeItemList.add(new ShoeItem("Adidas Questar", "ADIDAS", R.drawable.adidas_questar_shoes, 22));
        shoeItemList.add(new ShoeItem("Adidas Questar", "ADIDAS", R.drawable.adidas_questar_shoes, 12));
        shoeItemList.add(new ShoeItem("Adidas Ultraboost", "ADIDAS", R.drawable.adidas_ultraboost, 15));

    }*/
    // Tham chiếu đến nút chứa dữ liệu giày trên Realtime Database
    DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference().child("shoes");
    shoesRef.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            // Lấy danh sách các sản phẩm giày từ dữ liệu trên Firebase Realtime Database
            List<Shoe> shoeList = new ArrayList<>();
            for (DataSnapshot shoeSnapshot : snapshot.getChildren()) {
                Shoe shoe = shoeSnapshot.getValue(Shoe.class);
                shoeList.add(shoe);
            }

            // Thiết lập adapter cho RecyclerView
            ShoeAdapter shoeAdapter = new ShoeAdapter(shoeList);
            shoeRecyclerView.setAdapter(shoeAdapter);

            // Ẩn ProgressBar nếu RecyclerView hiển thị dữ liệu
            if (shoeList.size() > 0) {
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {
            // Xử lý lỗi nếu có
        }
    });


    private void initializeVariables() {

        cartImageView = findViewById(R.id.cartIv);
        userImageView = findViewById(R.id.userIv);
        //coordinatorLayout = findViewById(R.id.coordinatorLayout);
        shoeItemList = new ArrayList<>();
        recyclerView = findViewById(R.id.mainRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ShoeItemAdapter(this);

    }

    @Override
    public void onCardClicked(ShoeItem shoe) {

        Intent intent = new Intent(MainActivity.this, DetailedActivity.class);
        intent.putExtra("shoeItem", shoe);
        startActivity(intent);
    }

    @Override
    public void onAddToCartBtnClicked(ShoeItem shoeItem) {

    }


}
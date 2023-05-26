package com.midterm.shoestore.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;


import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;
import com.midterm.shoestore.model.ShoeItem;

import java.util.ArrayList;
import java.util.List;

public class DetailedActivity extends AppCompatActivity {

    private ImageView shoeImageView;
    private TextView shoeNameTV, shoeBrandNameTV, shoePriceTV, btn_left;
    private AppCompatButton addToCartBtn;
    private ShoeItem shoe;

    private ArrayList<String> listID;
    private ArrayList<Integer> listquantity;

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
            setDataToWidgets(this);
        }

        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertToRoom();
            }
        });

    }

    private void insertToRoom()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cartRef = database.getReference("cart");
        String userId = uid; // id của user tương ứng
        String shoeId = shoe.getShoeID(); // id của shoe cần thêm vào cart
        int quantityToAdd  = 1; // số lượng của shoe cần thêm

        cartRef.child(userId).child("shoes").child(shoeId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Kiểm tra nếu shoe đã có trong cart của user
                if (snapshot.exists()) {
                    int currentQuantity = snapshot.child("quantity").getValue(Integer.class);
                    int newQuantity = currentQuantity + quantityToAdd;
                    // Cập nhật số lượng mới cho shoe
                    cartRef.child(userId).child("shoes").child(shoeId).child("quantity").setValue(newQuantity)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e("Add_to_cart", "Cập nhật số lượng thành công");

                                    Toast.makeText(DetailedActivity.this, "Đã thêm vào giỏ hàng !", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Add_to_cart", "Cập nhật số lượng thất bại", e);
                                }
                            });
                } else {
                    // Nếu shoe chưa có trong cart của user, thêm mới
                    cartRef.child(userId).child("shoes").child(shoeId).child("quantity").setValue(quantityToAdd)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e("Add_to_cart", "Thêm giày mới OK");
                                    Toast.makeText(DetailedActivity.this, "Đã thêm giày vào giỏ hàng !", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Add_to_cart", "Thêm giày mới FAILED", e);
                                }
                            });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Add_to_cart", "Lỗi ....", error.toException());
            }
        });

    }
    private void setDataToWidgets(Context context) {
        shoeNameTV.setText(shoe.getShoeName());
        shoeBrandNameTV.setText(shoe.getShoeBrandName());
        shoePriceTV.setText(String.valueOf(shoe.getShoePrice()));
        if (context != null) {
            Glide.with(context)
                    .load(shoe.getShoeImage())
                    .into(shoeImageView);
        }
        //shoeImageView.setImageResource(shoe.getShoeImage());

    }
    private void getID_quantity()
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");
        String userId = uid; // id của user tương ứng

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference cartRef = database.getReference("cart");

        cartRef.child(userId).child("shoes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Duyệt qua tất cả các node con của "shoes"
                for (DataSnapshot shoeSnapshot : snapshot.getChildren()) {
                    String shoeId = shoeSnapshot.getKey();
                    int shoeQuantity = shoeSnapshot.child("quantity").getValue(Integer.class);

                    listquantity.add(shoeQuantity);
                    listID.add(shoeId);
                }
                // TODO: Sử dụng danh sách shoeItemList để hiển thị sản phẩm trong giỏ hàng của user
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GET_ID_SHOE_IN_CART", "Error retrieving shoes in cart for user", error.toException());
            }
        });
    }
    private void initializeVariables() {

        //shoeCartList = new ArrayList<>();
        shoeImageView = findViewById(R.id.detailActivityShoeIV);
        shoeNameTV = findViewById(R.id.detailActivityShoeNameTv);
        shoeBrandNameTV = findViewById(R.id.detailActivityShoeBrandNameTv);
        shoePriceTV = findViewById(R.id.detailActivityShoePriceTv);
        btn_left = findViewById(R.id.txtLeftdetail);
        addToCartBtn = findViewById(R.id.detailActivityAddToCartBtn);
        listID = new ArrayList<>();
        listquantity = new ArrayList<>();

        //viewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }
}
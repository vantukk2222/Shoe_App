package com.midterm.shoestore.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;
import com.midterm.shoestore.adapter.ShoeItemAdapter;
import com.midterm.shoestore.listener.ShoeItemLoadListener;
import com.midterm.shoestore.model.ShoeItem;

import java.util.ArrayList;
import java.util.List;

public class EditDetailActivity extends AppCompatActivity {

    private ShoeItem shoe;
    private EditText shoeEdtBrandName, shoeEdtName, shoeEdtPrice, shoeEdtImage, shoeEdtInfo, shoeEdtID;
    private Button leftBtn, admitBtn;
    private List<ShoeItem> shoeItemList;
    HomeFragment homeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_detail);
        shoe = getIntent().getParcelableExtra("shoeItem");
        initializeVariables();

        leftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDetailActivity.this.finish();
            }
        });

        if (shoe != null) {
            setData();
        }

        admitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference("shoes");
                shoesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot shoeSnapshot : snapshot.getChildren()) {
                            ShoeItem shoeItem = shoeSnapshot.getValue(ShoeItem.class);
                            String shoeItemId = shoe.getShoeID();
                            String newName = String.valueOf(shoeEdtName.getText());
                            String newBrand = String.valueOf(shoeEdtBrandName.getText());
                            String newPrice = String.valueOf(shoeEdtPrice.getText());
                            String newImage = String.valueOf(shoeEdtImage.getText());

                            shoesRef.child(shoeItemId).child("shoeID").setValue(shoeItemId);
                            shoesRef.child(shoeItemId).child("shoeName").setValue(newName);
                            shoesRef.child(shoeItemId).child("shoeBrandName").setValue(newBrand);
                            shoesRef.child(shoeItemId).child("shoePrice").setValue(newPrice);
                            shoesRef.child(shoeItemId).child("shoeImage").setValue(newImage);

                            homeFragment.shoeItemLoadListener.onShoeItemLoadSuccess(shoeItemList);
                            finish();


                            /*DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference("shoes").child(shoeItemId);
                            shoesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String newName = String.valueOf(shoeEdtName.getText());
                                    String newBrand = String.valueOf(shoeEdtBrandName.getText());
                                    String newPrice = String.valueOf(shoeEdtPrice.getText());
                                    String newImage = String.valueOf(shoeEdtImage.getText());

                                    shoesRef.child("shoeName").setValue(newName);
                                    shoesRef.child("shoeBrandName").setValue(newBrand);
                                    shoesRef.child("shoePrice").setValue(newPrice);
                                    shoesRef.child("shoeImage").setValue(newImage);

                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    // Xử lý lỗi nếu có
                                }
                            });*/

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    private void setData() {
        shoeEdtName.setText(shoe.getShoeName());
        shoeEdtBrandName.setText(shoe.getShoeBrandName());
        shoeEdtPrice.setText(shoe.getShoePrice());
        shoeEdtImage.setText(shoe.getShoeImage());
        shoeEdtID.setText(shoe.getShoeID());


    }






    private void initializeVariables() {

        //shoeCartList = new ArrayList<>();
        shoeEdtImage = findViewById(R.id.editTextEditShoeImage);
        shoeEdtName = findViewById(R.id.editTextEditShoeName);
        shoeEdtBrandName = findViewById(R.id.editTextEditShoeBrand);
        shoeEdtPrice = findViewById(R.id.editTextEditShoePrice);
        shoeEdtID = findViewById(R.id.editTextEditShoeID);
        leftBtn = findViewById(R.id.left_btn);
        admitBtn = findViewById(R.id.admit_btn);
        //listID = new ArrayList<>();
        //listquantity = new ArrayList<>();

        //viewModel = new ViewModelProvider(this).get(CartViewModel.class);
    }
}
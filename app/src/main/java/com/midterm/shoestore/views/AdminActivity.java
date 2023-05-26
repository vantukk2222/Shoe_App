package com.midterm.shoestore.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;
import com.midterm.shoestore.adapter.ShoeItemAdapter;
import com.midterm.shoestore.listener.ShoeItemLoadListener;
import com.midterm.shoestore.model.ShoeItem;

import java.util.List;

import butterknife.BindView;

public class AdminActivity extends AppCompatActivity {
    private Button leftAdmin_panel;
    private Button addBtn;
    private EditText edtID, edtName, edtBrand, edtPrice, edtImage;
    private List<ShoeItem> shoeItemList;
    HomeFragment homeFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        edtName = findViewById(R.id.editTextShoeName);
        edtBrand = findViewById(R.id.editTextShoeBrand);
        edtPrice = findViewById(R.id.editTextShoePrice);
        edtImage = findViewById(R.id.editTextShoeImage);
        edtID = findViewById(R.id.editTextShoeID);

        leftAdmin_panel = findViewById(R.id.leftAdmin_panel);
        leftAdmin_panel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        /*leftAdmin_panel.setOnClickListener(view -> {

            Intent intent = new Intent(AdminActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish(); // Đóng Activity hiện tại

        });*/
        addBtn = findViewById(R.id.addBtn);
        addBtn.setOnClickListener(view-> {
            String strID = edtID.getText().toString().trim();
            String strName = edtName.getText().toString().trim();
            String strBrand = edtBrand.getText().toString().trim();
            String strPrice = edtPrice.getText().toString().trim();
            String strImage = edtImage.getText().toString().trim();
            if(strName.equals(""))
            {
                edtName.setError("Error");
            }
            if(strBrand.equals("")) {
                edtBrand.setError("Error");
            }
            if(strPrice.equals("")) {
                edtPrice.setError("Error");
            }
            if(strImage.equals("")) {
                edtImage.setError("Error");
            }

            else{
                DatabaseReference shoesRef = FirebaseDatabase.getInstance().getReference("shoes");
                shoesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot shoeSnapshot : snapshot.getChildren()) {
                            ShoeItem shoeItem = shoeSnapshot.getValue(ShoeItem.class);
                            String shoeItemId = strID;
                            shoesRef.child(shoeItemId).child("shoeID").setValue(shoeItemId);
                            shoesRef.child(shoeItemId).child("shoeName").setValue(strName);
                            shoesRef.child(shoeItemId).child("shoeBrandName").setValue(strBrand);
                            shoesRef.child(shoeItemId).child("shoePrice").setValue(strPrice);
                            shoesRef.child(shoeItemId).child("shoeImage").setValue(strImage);

                            homeFragment.shoeItemLoadListener.onShoeItemLoadSuccess(shoeItemList);
                            finish();

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


            }
        });

    }
}
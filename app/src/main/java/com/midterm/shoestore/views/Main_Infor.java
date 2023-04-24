package com.midterm.shoestore.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;

public class Main_Infor extends AppCompatActivity {
    private TextView tvleftinfor_st, txtName_st, txtMail_st, txtDoB_st, txtGender_st, txtPhoneNumber_st, btn_logout_settings;
    private LinearLayout layout_to_edit_profile, layout_to_cart_profile, layout_to_change_profile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_infor);


        txtName_st = findViewById(R.id.txtName_st);
        txtMail_st = findViewById(R.id.txtMail_st);
        txtDoB_st = findViewById(R.id.txtDoB_st);
        txtGender_st = findViewById(R.id.txtGender_st);
        txtPhoneNumber_st = findViewById(R.id.txtPhoneNumber_st);

        layout_to_edit_profile = findViewById(R.id.layout_to_edit_profile);
        layout_to_cart_profile = findViewById(R.id.layout_to_cart_profile);
        layout_to_change_profile = findViewById(R.id.layout_to_change_profile);

        btn_logout_settings = findViewById(R.id.btn_logout_settings);
        tvleftinfor_st = findViewById(R.id.tvleftinfor_st);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");
        String userId = uid; // id của user tương ứng
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Lấy tên của user
                String name = dataSnapshot.child("name").getValue(String.class);
                // Lấy giới tính của user
                boolean sex = dataSnapshot.child("sex").getValue(boolean.class);
                // Lấy ngày sinh của user
                String bod = dataSnapshot.child("bod").getValue(String.class);
                // Lấy số điện thoại của user
                String phoneNo = dataSnapshot.child("phoneno").getValue(String.class);

                txtName_st.setText(name);
                txtMail_st.setText(user.getEmail());
                if(sex == true) {
                    txtGender_st.setText("Nam");
                }
                else {
                    txtGender_st.setText("Nữ");
                }
                txtDoB_st.setText(bod);
                txtPhoneNumber_st.setText(phoneNo);
                Log.e("Get in4","Get OK");

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Get in4","Get Failed");
            }
        });


        tvleftinfor_st.setOnClickListener(view -> {
            finish();
        });
        btn_logout_settings.setOnClickListener(view -> {
            preferences.edit().remove("uid").apply();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish(); // Đóng Activity hiện tại

            Toast.makeText(getApplicationContext(), "Logout!", Toast.LENGTH_SHORT).show();
        });
        layout_to_edit_profile.setOnClickListener(view -> {
            Intent intent_edit_profile = new Intent(getApplicationContext(), edit_Profile.class);

            startActivity(intent_edit_profile);
        });
        layout_to_cart_profile.setOnClickListener(view ->{
            Intent intent_cart = new Intent(getApplicationContext(), show_list_cart.class);

            startActivity(intent_cart);
        });
        layout_to_change_profile.setOnClickListener(view -> {
            Intent intent_changepw = new Intent(getApplicationContext(), changePassword.class);

            startActivity(intent_changepw);
        });
    }
}
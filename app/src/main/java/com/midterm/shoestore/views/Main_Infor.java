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

import java.util.HashMap;
import java.util.Map;

public class Main_Infor extends AppCompatActivity {
    private TextView txt_change_pass_st, tvleftinfor_st, txtName_st, txtMail_st, txtDoB_st, txtGender_st, txtPhoneNumber_st, btn_logout_settings;
    private LinearLayout layout_to_check_cart_st, layout_to_check_shoes_st, layout_to_edit_profile, layout_to_cart_profile, layout_to_checkout_profile, layout_to_changePW_st;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_infor);


        txtName_st = findViewById(R.id.txtName_st);
        txtMail_st = findViewById(R.id.txtMail_st);
        txtDoB_st = findViewById(R.id.txtDoB_st);
        txtGender_st = findViewById(R.id.txtGender_st);
        txtPhoneNumber_st = findViewById(R.id.txtPhoneNumber_st);
        txt_change_pass_st = findViewById(R.id.txt_change_pass_st);

        layout_to_edit_profile = findViewById(R.id.layout_to_edit_profile);
        layout_to_cart_profile = findViewById(R.id.layout_to_cart_profile);
        layout_to_checkout_profile = findViewById(R.id.layout_to_checkout_profile);
        layout_to_changePW_st = findViewById(R.id.layout_to_changePW_st);
        layout_to_check_shoes_st = findViewById(R.id.layout_to_check_shoes_st);
        layout_to_check_cart_st = findViewById(R.id.layout_to_check_cart_st);


        btn_logout_settings = findViewById(R.id.btn_logout_settings);
        tvleftinfor_st = findViewById(R.id.tvleftinfor_st);



        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");
        String userId = uid; // id của user tương ứng
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userId);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(userId.equals("admin"))
        {
            layout_to_edit_profile.setVisibility(View.GONE);
            layout_to_changePW_st.setVisibility(View.GONE);
            layout_to_cart_profile.setVisibility(View.GONE);
            layout_to_checkout_profile.setVisibility(View.GONE);
        }
        else
        {
            layout_to_check_cart_st.setVisibility(View.GONE);
            layout_to_check_shoes_st.setVisibility(View.GONE);
        }
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
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
                else if(userId.equals("admin"))
                {
                    txtName_st.setText("Admin Panel");
                    txtMail_st.setText("shoeADControl@gmail.com");
                    txtGender_st.setText("Nam");
                    txtDoB_st.setText("2004-04-3");
                    txtPhoneNumber_st.setText("0965770497");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Get in4","Get Failed");
            }
        });

        //Chỉnh sửa thông tin cá nhân(User)
        layout_to_edit_profile.setOnClickListener(view -> {
            Intent intent_edit_profile = new Intent(getApplicationContext(), edit_Profile.class);

            startActivity(intent_edit_profile);
        });
        //Giỏ hàng User
        layout_to_cart_profile.setOnClickListener(view ->{
            Intent intent_cart = new Intent(getApplicationContext(), show_list_cart.class);

            startActivity(intent_cart);
        });
        //Đơn hàng User
        layout_to_checkout_profile.setOnClickListener(view ->{
            Toast.makeText(this, "Giỏ hàng: In progress", Toast.LENGTH_SHORT).show();
            getOrderDetails();
        });

        //Quản lý giày Admin
        layout_to_check_shoes_st.setOnClickListener(view ->{
            Toast.makeText(this, "Giày: In progress", Toast.LENGTH_SHORT).show();
        });

        //Quản lý đơn hàng Admin
        layout_to_check_cart_st.setOnClickListener(view ->{
            Toast.makeText(this, "Đơn hàng: In progress", Toast.LENGTH_SHORT).show();
        });



        //Đổi mật khẩu User
        txt_change_pass_st.setOnClickListener(view -> {
            Intent intent_changepw = new Intent(getApplicationContext(), changePassword.class);

            startActivity(intent_changepw);
        });
        //Rời settings
        tvleftinfor_st.setOnClickListener(view -> {
            finish();
        });

        //Đăng xuất
        btn_logout_settings.setOnClickListener(view -> {
            preferences.edit().remove("uid").apply();

            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish(); // Đóng Activity hiện tại

            Toast.makeText(getApplicationContext(), "Logout!", Toast.LENGTH_SHORT).show();
        });


    }
    public void getOrderDetails() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");
        DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference("orders");

        // Duyệt qua tất cả các order
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    String orderId = orderSnapshot.child("orderId").getValue(String.class);
                    String status = orderSnapshot.child("status").getValue(String.class);
                    String userID = orderSnapshot.child("userId").getValue(String.class);

                    // Kiểm tra nếu status = "pending"
                    if (status != null && status.equals("pending") && userID.equals(uid)) {
                        // Lấy ra các thông tin cần thiết của order
                        Map<String, Integer> shoeQuantities = new HashMap<>();
                        for (DataSnapshot shoeSnapshot : orderSnapshot.child("shoeQuantities").getChildren()) {
                            String shoeId = shoeSnapshot.getKey();
                            int quantity = shoeSnapshot.getValue(Integer.class);

                            shoeQuantities.put(shoeId, quantity);
                        }

                        String timePlaced = orderSnapshot.child("timePlaced").getValue(String.class);

                        // In ra console các thông tin của order
                        Log.e("ORDER_DETAILS", "orderId: " + orderId);
                        Log.e("ORDER_DETAILS", "shoeQuantities: " + shoeQuantities.toString());
                        Log.e("ORDER_DETAILS", "timePlaced: " + timePlaced);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("ORDER_DETAILS", "loadOrders:onCancelled", error.toException());
            }
        });
    }

}
package com.midterm.shoestore.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.midterm.shoestore.R;

public class Checkout_Activity extends AppCompatActivity {
    private TextView tvleft_co;
    private ImageView imgcheckcart_co;
    private RadioButton ChoXacNhan, DangGiao, DaGiao, DaHuy;
    private RadioGroup radioGroup_checkout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");
        tvleft_co = findViewById(R.id.tvleft_co);
        imgcheckcart_co = findViewById(R.id.imgcheckcart_co);
        ChoXacNhan = findViewById(R.id.ChoXacNhan);
        DangGiao = findViewById(R.id.DangGiao);
        DaGiao = findViewById(R.id.DaGiao);
        DaHuy = findViewById(R.id.DaHuy);
        radioGroup_checkout = findViewById(R.id.radioGroup_checkout);

        ordersFragment myFragment = new ordersFragment();
        Bundle bundle = new Bundle();
        final String[] status = {"delivering"};
        bundle.putString("MY_STRING", status[0]);
        myFragment.setArguments(bundle);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment_container_orders, myFragment, "MY_FRAGMENT")
                .commit();

        tvleft_co.setOnClickListener(view -> finish());
        imgcheckcart_co.setOnClickListener(view ->{
            Intent intent_cart = new Intent(getApplicationContext(), show_list_cart.class);
            startActivity(intent_cart);
        });
        radioGroup_checkout.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // Xử lý sự kiện khi người dùng thay đổi lựa chọn
                RadioButton selectedRadioButton = findViewById(checkedId);
                String selectedText = selectedRadioButton.getText().toString();

                if(selectedText.equals(getResources().getString(R.string.delivering)))
                {
                    if(uid.equals("admin"))
                    {
                        status[0] = "delivering";
                    }
                    else
                    {
                        status[0] = "delivering";
                    }
                    ordersFragment ordersFragments = (ordersFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_orders);
                    ordersFragments.onSearchQueryChanged(status[0]);
                }
                if(selectedText.equals(getResources().getString(R.string.pending)))
                {
                    if(uid.equals("admin"))
                    {
                        status[0] = "pending";
                    }
                    else
                    {
                        status[0] = "pending";
                    }
                    ordersFragment ordersFragments = (ordersFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_orders);
                    ordersFragments.onSearchQueryChanged(status[0]);
                }
                if(selectedText.equals(getResources().getString(R.string.cancelled)))
                {
                    if(uid.equals("admin"))
                    {
                        status[0] = "cancelled";
                    }
                    else
                    {
                        status[0] = "cancelled";
                    }
                    ordersFragment ordersFragments = (ordersFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_orders);
                    ordersFragments.onSearchQueryChanged(status[0]);
                }
                if(selectedText.equals(getResources().getString(R.string.delivered)))
                {
                    if(uid.equals("admin"))
                    {
                        status[0] = "delivered";
                    }
                    else
                    {
                        status[0] = "delivered";
                    }
                    ordersFragment ordersFragments = (ordersFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container_orders);
                    ordersFragments.onSearchQueryChanged(status[0]);
                }
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                preferences.edit().putString("status", status[0]).apply();

            }
        });

    }
}
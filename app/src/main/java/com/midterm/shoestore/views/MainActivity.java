package com.midterm.shoestore.views;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;
import com.midterm.shoestore.adapter.ShoeItemAdapter;
import com.midterm.shoestore.model.ShoeItem;
import com.midterm.shoestore.model.users;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView filterView;
    private DrawerLayout drawerlayout;
    private SearchView searchView;
    private Dialog dialog;
    private FloatingActionButton editShoeBtn;
    private ShoeItemAdapter adapter;
    private List<ShoeItem> shoeItemList;
    private ImageButton deleteBtn;
    private MenuItem menuItem;
    private boolean isDeleteBtnVisible = false;
    ShoeItemAdapter.ShoeClickedListeners shoeClickedListeners;
    HomeFragment homeFragment;

    @BindView(R.id.fragment_container)
    FrameLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar); //Ignore red line errors
        setSupportActionBar(toolbar);
        searchView = findViewById(R.id.searchView_home);
        filterView = findViewById(R.id.filter_home);
        drawerlayout = findViewById(R.id.drawer_layout);
        dialog = new Dialog(this);
        filterView.setVisibility(View.GONE);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        Menu menu = navigationView.getMenu();
        MenuItem cartItem = menu.findItem(R.id.nav_cart);
        MenuItem checkoutcartItem = menu.findItem(R.id.nav_checkout_cart);
        MenuItem adminItem = menu.findItem(R.id.nav_admin);
        MenuItem cartmanagementItem = menu.findItem(R.id.nav_cart_management);
        editShoeBtn = findViewById(R.id.editShoeBtn);

        if (uid.equals("admin")) {
            cartItem.setVisible(false);
            checkoutcartItem.setVisible(false);
            editShoeBtn.setVisibility(View.GONE);

        }
        else if(!uid.isEmpty()) {
            adminItem.setVisible(false);
            cartmanagementItem.setVisible(false);
            editShoeBtn.setVisibility(View.GONE);
        }

        View headerView = navigationView.getHeaderView(0);
        TextView name_navheader = headerView.findViewById(R.id.name_navheader);
        TextView phone_navheader = headerView.findViewById(R.id.phone_navheader);

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
        DatabaseReference userRef = usersRef.child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Check dữ liệu có tồn tại không
                if (snapshot.exists()) {
                    // Lấy giá trị dữ liệu user tương ứng
                    users user = snapshot.getValue(users.class);
//                    Toast.makeText(MainActivity.this, "Welcome back - "+user.getName(), Toast.LENGTH_SHORT).show();
                    name_navheader.setText(user.getName());
                    phone_navheader.setText(user.getPhoneno());
                    // Thực hiện xử lý với dữ liệu user vừa lấy được ở đây.
                } else {
                    name_navheader.setText("Admin Panel");
                    phone_navheader.setText("0965770497");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Xử lý khi có lỗi xảy ra.
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.open_nav, R.string.close_nav);

        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();
        if( savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        }
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                HomeFragment homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                homeFragment.onSearchQueryChanged(newText);
                return true;
            }
        });
        filterView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openbuydialog();
            }
        });

        FrameLayout frameLayout = findViewById(R.id.fragment_container);
        editShoeBtn = frameLayout.findViewById(R.id.editShoeBtn);
        editShoeBtn.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, AdminActivity.class);
            startActivity(intent);
        });

    }


    private void openbuydialog() {
        dialog.setContentView(R.layout.sort_filter_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextView leftSort, acpSort, resetSort;

        leftSort = dialog.findViewById(R.id.leftSort);
        acpSort = dialog.findViewById(R.id.acptSort);
        resetSort = dialog.findViewById(R.id.resetSort);

        CheckBox adidas, nike, sneaker, running, red, blue, black, size385, size39, size40, size405, size41, size42, size425, size43, size44;
        adidas = dialog.findViewById(R.id.brandAdidas);
        nike = dialog.findViewById(R.id.brandNike);

        sneaker = dialog.findViewById(R.id.cateSneaker);
        running = dialog.findViewById(R.id.cateRunning);

        red = dialog.findViewById(R.id.colorRed);
        blue = dialog.findViewById(R.id.colorBlue);
        black = dialog.findViewById(R.id.colorBlack);

        size385 = dialog.findViewById(R.id.size385);
        size39 = dialog.findViewById(R.id.size39);
        size40 = dialog.findViewById(R.id.size40);
        size405 = dialog.findViewById(R.id.size405);
        size41 = dialog.findViewById(R.id.size41);
        size42 = dialog.findViewById(R.id.size42);
        size425 = dialog.findViewById(R.id.size425);
        size43 = dialog.findViewById(R.id.size43);
        size44 = dialog.findViewById(R.id.size44);




        leftSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        acpSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        resetSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adidas.setChecked(false);
                nike.setChecked(false);
                sneaker.setChecked(false);
                running.setChecked(false);
                red.setChecked(false);
                blue.setChecked(false);
                black.setChecked(false);
                size385.setChecked(false);
                size39.setChecked(false);
                size40.setChecked(false);
                size405.setChecked(false);
                size41.setChecked(false);
                size42.setChecked(false);
                size425.setChecked(false);
                size43.setChecked(false);
                size44.setChecked(false);
            }
        });




        /* Them chuc nang o day*/
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        if (drawerlayout.isDrawerOpen(GravityCompat.START)) {
            drawerlayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
//

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");

        if(uid.equals("admin")) {

            switch (item.getItemId()) {
                case R.id.nav_home:
                    editShoeBtn = findViewById(R.id.editShoeBtn);
                    editShoeBtn.setVisibility(View.GONE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    break;
                case R.id.nav_settings:
                    Intent intent_setting = new Intent(getApplicationContext(), Main_Infor.class);
                    startActivity(intent_setting);
                    break;
                case R.id.nav_share:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
                    break;
                case R.id.nav_cart:

                    Intent intent_cart = new Intent(getApplicationContext(), show_list_cart.class);

                    startActivity(intent_cart);
                    break;
                case R.id.nav_logout:
                    //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    preferences.edit().remove("uid").apply();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish(); // Đóng Activity hiện tại

                    Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.nav_admin:
                    editShoeBtn = findViewById(R.id.editShoeBtn);
                    editShoeBtn.setVisibility(View.VISIBLE);
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

                    break;
                case R.id.nav_cart_management:
                    Intent intent_check_cart = new Intent(getApplicationContext(), Checkout_Activity.class);
                    startActivity(intent_check_cart);
                    break;
            }
        }
        else if(!uid.isEmpty()) {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                    break;
                case R.id.nav_settings:
                    Intent intent_setting = new Intent(getApplicationContext(), Main_Infor.class);
                    startActivity(intent_setting);

                    break;
                case R.id.nav_share:
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
                    break;
                case R.id.nav_checkout_cart:
                    Intent intent_check_cart = new Intent(getApplicationContext(), Checkout_Activity.class);
                    startActivity(intent_check_cart);
                    break;
                case R.id.nav_cart:

                    Intent intent_cart = new Intent(getApplicationContext(), show_list_cart.class);

                    startActivity(intent_cart);
                    break;
                case R.id.nav_logout:
                    //SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    preferences.edit().remove("uid").apply();

                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish(); // Đóng Activity hiện tại

                    Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
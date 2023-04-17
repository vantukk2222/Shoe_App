package com.midterm.shoestore.views;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.midterm.shoestore.R;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView filterView;
    private DrawerLayout drawerlayout;
    private SearchView searchView;
    private Dialog dialog;
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



        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;
            case R.id.nav_share:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareFragment()).commit();
                break;
            case R.id.nav_logout:
                Toast.makeText(this, "Logout!", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerlayout.closeDrawer(GravityCompat.START);
        return true;
    }

}
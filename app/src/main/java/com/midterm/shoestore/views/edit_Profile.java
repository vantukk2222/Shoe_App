package com.midterm.shoestore.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.midterm.shoestore.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class edit_Profile extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private TextView dateButton;
    private EditText edtName, edtPhone;
    private TextView checkEdit, leftEdit;
    private RadioButton r_male, r_fmale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initDatePicker();
        dateButton = findViewById(R.id.datePickerButton);
        r_male = findViewById(R.id.radioM);
        r_fmale = findViewById(R.id.radioF);
        edtName = findViewById(R.id.edtNameChange);
        edtPhone = findViewById(R.id.edtPhoneChange);
        checkEdit = findViewById(R.id.txtCheckEdit);
        leftEdit = findViewById(R.id.txtLeftEdit);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String uid = preferences.getString("uid", "");
        String userId = uid; // id của user tương ứng
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(userId);


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

                edtName.setText(name);
                if(sex == true) {
                    r_male.setChecked(true);
                }
                else {
                    r_fmale.setChecked(true);
                }
                dateButton.setText(bod);
                edtPhone.setText(phoneNo);
                Log.e("Get in4","Get OK");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Get in4","Get Failed");
            }
        });



        leftEdit.setOnClickListener(view -> finish());
        checkEdit.setOnClickListener(view -> {

            Map<String, Object> updateData = new HashMap<>();
            updateData.put("name", edtName.getText().toString().trim());
            updateData.put("sex", r_male.isChecked());
            updateData.put("bod", dateButton.getText().toString().trim());
            updateData.put("phoneno", edtPhone.getText().toString().trim());
            ref.updateChildren(updateData);
            Log.e("Update in4","Update OK");
            Toast.makeText(this,"Cập nhật thành công", Toast.LENGTH_SHORT).show();
            finish();
            Intent backLogin = new Intent(getApplicationContext(), LoginActivity.class);
            backLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(backLogin);
        });
    }

    private String getTodaysDate()
    {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day)
            {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

    }

    private String makeDateString(int day, int month, int year)
    {
        return year + "-" + getMonthFormat(month)+ "-" + day;
    }

    private String getMonthFormat(int month)
    {
        if(month == 1)
            return "01";
        if(month == 2)
            return "02";
        if(month == 3)
            return "03";
        if(month == 4)
            return "04";
        if(month == 5)
            return "05";
        if(month == 6)
            return "06";
        if(month == 7)
            return "07";
        if(month == 8)
            return "08";
        if(month == 9)
            return "09";
        if(month == 10)
            return "10";
        if(month == 11)
            return "11";
        if(month == 12)
            return "12";

        //default should never happen
        return "1";
    }

    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}
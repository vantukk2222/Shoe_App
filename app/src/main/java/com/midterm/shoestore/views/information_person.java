package com.midterm.shoestore.views;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.midterm.shoestore.R;

import java.util.Calendar;

public class information_person extends AppCompatActivity {
    private TextView txtLeftEdit, dateButton;
    private DatePickerDialog datePickerDialog;
    private EditText edtNameChange, edtPhoneChange;
    private RadioButton radioM;
    private ImageView img_btn_finish_reg;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_person);

        initDatePicker();

        mAuth = FirebaseAuth.getInstance();

        img_btn_finish_reg = findViewById(R.id.img_btn_finish_reg);
        txtLeftEdit = findViewById(R.id.txtLeftEdit);


        edtNameChange = findViewById(R.id.edtNameChange);
        radioM = findViewById(R.id.radioM);
        dateButton = findViewById(R.id.datePickerButton);
        edtPhoneChange = findViewById(R.id.edtPhoneChange);
        dateButton.setText(getTodaysDate());
        
        boolean gender = radioM.isChecked();


        Intent intent1 = getIntent();
        Bundle bundle1 = intent1.getBundleExtra("Bun_Account_Reg");


        String email = bundle1.getString("email");
        String pass = bundle1.getString("pass");

        img_btn_finish_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String strName = edtNameChange.getText().toString().trim();
                String strPhone = edtPhoneChange.getText().toString().trim();
                String strDoB = dateButton.getText().toString().trim();
                if(strName.equals(""))
                {
                    edtNameChange.setError("Error");
                }
                if(strPhone.equals("")) {
                    edtPhoneChange.setError("Error");
                }
                if(!checkNameis((strName)))
                {
                    edtNameChange.setError("Sai tên");
                }
                if(strPhone.length() != 10)
                {
                    edtPhoneChange.setError("Sai SDT");
                }
                else{
                    mAuth.createUserWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    final ProgressDialog mDialog = new ProgressDialog(information_person.this);
                                    mDialog.setMessage("Login....");
                                    mDialog.show();
                                    FirebaseUser user = task.getResult().getUser();
                                    // Lưu thông tin user vào Realtime Database
                                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                                            .child("users").child(user.getUid());
                                    userRef.child("name").setValue(strName);
                                    userRef.child("sex").setValue(gender);
                                    userRef.child("bod").setValue(strDoB);
                                    userRef.child("phoneno").setValue(strPhone);
                                    information_person.this.finish();
                                    // Thông báo cho người dùng đăng ký thành công và chuyển sang trang đăng nhập
                                    Intent i = new Intent(information_person.this, MainActivity.class);
                                    startActivity(i);
                                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.putBoolean("isLoggedIn", true);
                                    editor.apply();
                                    mDialog.dismiss();
                                } else {
                                    Toast.makeText(information_person.this, "Đã xảy ra lỗi gì rồi", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });
        txtLeftEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                information_person.this.finish();
            }
        });
    }
    public boolean checkNameis(String value)
    {
        for(int i = 0; i < value.length(); i++)
        {
            if(value.charAt(i) >= '0' && value.charAt(i) <= '9')
            {
                return false;
            }
        }
        return true;
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
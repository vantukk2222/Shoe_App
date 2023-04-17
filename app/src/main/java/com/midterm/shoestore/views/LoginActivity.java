package com.midterm.shoestore.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import com.midterm.shoestore.R;

public class LoginActivity extends AppCompatActivity {
    private EditText email, password;
    private ImageView btn_login;
    private TextView btn_reg;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.txtmail_login);
        password = findViewById(R.id.txtpass_login);
        btn_login = findViewById(R.id.img_btn_login);
        btn_reg = findViewById(R.id.txt_reg);
        mAuth = FirebaseAuth.getInstance();
        // Kiểm tra đăng nhập trong phương thức onCreate() của Activity đăng nhập
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (isLoggedIn) {
            // Chuyển người dùng đến Activity chính của ứng dụng
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

//        mAuth.sendPasswordResetEmail("ntu12082002@gmail.com").addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if(task.isSuccessful())
//                {
//                    Toast.makeText(LoginActivity.this, "OK send link", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        btn_login.setOnClickListener(view -> {
            String strEmail = email.getText().toString();
            String strPass = password.getText().toString();
            if(!strEmail.equals("") && !strPass.equals("") && Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())
            {
                mAuth.signInWithEmailAndPassword(strEmail, strPass).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        final ProgressDialog mDialog = new ProgressDialog(LoginActivity.this);
                        mDialog.setMessage("Login....");
                        mDialog.show();
                        LoginActivity.this.finish();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        LoginActivity.this.finish();
                        mDialog.dismiss();

                        // Lưu trạng thái đăng nhập thành công vào SharedPreferences
                        SharedPreferences sharedPreferences1 = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                        editor.putBoolean("isLoggedIn", true);
                        editor.apply();

                    } else {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            else {
                Toast.makeText(LoginActivity.this, "Đăng nhập lỗi!", Toast.LENGTH_SHORT).show();
            }

        });
        btn_reg.setOnClickListener(view -> {
            Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(i);
        });

    }
}
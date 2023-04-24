package com.midterm.shoestore.views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.midterm.shoestore.R;

public class changePassword extends AppCompatActivity {
    private EditText currentPassword, newPassword, retypetPassword;
    private Button btnChangePassword;
    private ImageView left_changepass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        currentPassword = findViewById(R.id.currentPassword);
        newPassword = findViewById(R.id.newPassword);
        retypetPassword = findViewById(R.id.retypetPassword);

        btnChangePassword = findViewById(R.id.btnChangePassword);
        left_changepass = findViewById(R.id.left_changepass);
        String newPass = newPassword.getText().toString().trim();
        String oldPass = currentPassword.getText().toString().trim();
        String retypePass = retypetPassword.getText().toString().trim();

        btnChangePassword.setOnClickListener(view -> {
            if(newPass.length() < 8 || retypePass.length() < 8)
            {
                newPassword.setError("Mật khẩu phải lớ́n hơn 8");
            }
            else if(!newPass.equals(retypePass))
            {
                newPassword.setError("Mật khẩu mới không giống");
            }
            else {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                String email = user.getEmail();
                String password = oldPass;
                AuthCredential authCredential = EmailAuthProvider.getCredential(email, password);
                user.reauthenticate(authCredential)
                        .addOnSuccessListener(unused -> user.updatePassword(newPass)
                                .addOnSuccessListener(unused1 -> {
                                    Log.e("Update PW", "OK");
                                    Toast.makeText(changePassword.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                                    currentPassword.setText("");
                                    newPassword.setText("");
                                    retypetPassword.setText("");
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("Update PW", "Failed dont know");
                                    Toast.makeText(changePassword.this, "Đổi mật khẩu thất bại!", Toast.LENGTH_SHORT).show();


                                }))
                        .addOnFailureListener(e -> {
                            Log.e("Update PW", "Failed oldPass ");
                            Toast.makeText(changePassword.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();

                        });
            }
        });
        left_changepass.setOnClickListener(view -> finish());

    }
}
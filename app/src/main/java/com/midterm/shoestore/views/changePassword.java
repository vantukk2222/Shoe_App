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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class changePassword extends AppCompatActivity {
    private EditText input_email;
    private Button btnChangePassword;
    private ImageView left_changepass;

    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        input_email = findViewById(R.id.input_email);

        btnChangePassword = findViewById(R.id.btnChangePassword);
        left_changepass = findViewById(R.id.left_changepass);
        mAuth = FirebaseAuth.getInstance();
        btnChangePassword.setOnClickListener(view -> {
            boolean isValid = isValidEmail(input_email.getText().toString().trim());
            if(isValid)
            {
                mAuth.sendPasswordResetEmail(input_email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(getApplicationContext(), "OK send link", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            else
            {
                input_email.setError("Require");
            }
        });
        left_changepass.setOnClickListener(view -> finish());

    }

    public static boolean isValidEmail(String email) {
        Matcher matcher = EMAIL_PATTERN.matcher(email);
        return matcher.matches();
    }
}
package com.midterm.shoestore.views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.midterm.shoestore.R;

public class RegisterActivity extends AppCompatActivity {
    private EditText email, pass, repass;
    private ImageView btn_reg;
    private TextView btn_return_login, btn_left_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        email = findViewById(R.id.txtmail_reg);
        pass = findViewById(R.id.txt_pass_reg);
        repass = findViewById(R.id.txt_re_pass);
        btn_reg = findViewById(R.id.img_btn_reg);
        btn_return_login = findViewById(R.id.txt_reg_to_login);
        btn_left_reg = findViewById(R.id.txtLefReg);

        btn_left_reg.setOnClickListener(view -> RegisterActivity.this.finish());

        btn_reg.setOnClickListener(view -> {


            String strEmail = email.getText().toString().trim();
            String strPass = pass.getText().toString().trim();
            String strRepass = repass.getText().toString().trim();
            if(Patterns.EMAIL_ADDRESS.matcher(strEmail).matches() && !TextUtils.isEmpty(strRepass) && !TextUtils.isEmpty(strEmail) && !TextUtils.isEmpty(strPass))
            {
                if(strPass.equals(strRepass)) {
                    Intent i = new Intent(RegisterActivity.this, information_person.class);
                    Bundle bundle = new Bundle();

                    bundle.putString("email", strEmail);
                    bundle.putString("pass", strPass);

                    i.putExtra("Bun_Account_Reg",bundle);

                    Log.e("email_in_reg", strEmail);
                    Log.e("pass_in_reg", strPass);
                    startActivity(i);
                }
            }
            else
            {
                if(strEmail.equals(""))
                {
                    email.setError("Lỗi rồi!");
                }
                if(strPass.equals(""))
                {
                    pass.setError("Lỗi rồi!");
                }
                if(strRepass.equals(""))
                {
                    repass.setError("Lỗi rồi!");
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(strEmail).matches())
                {
                    email.setError("Sai");
                }
            }

        });
        btn_return_login.setOnClickListener(v -> RegisterActivity.this.finish());
    }
}
package com.example.myapplication_finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {
    EditText login_EDT_phone;
    Button login_BTN_continue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, Menu.class);
            startActivity(intent);
            finish();
        }

        findVeiws();

        login_BTN_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String mobile = login_EDT_phone.getText().toString().trim();

                if (mobile.length() != 10) {
                    login_EDT_phone.setError("הכנס מספר טלפון תקין");
                    login_EDT_phone.requestFocus();
                    return;
                }

                Intent intent = new Intent(LogInActivity.this, VerifyPhoneActivity.class);
                intent.putExtra(Constants.PHONE, mobile);
                startActivity(intent);
            }
        });
        login_EDT_phone.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER) {
                        login_BTN_continue.performClick();
                        return true;
                    }
                }
                return false;
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent intent = new Intent(this, Menu.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        }
    }

    private void findVeiws() {
        login_EDT_phone = findViewById(R.id.login_EDT_phone);
        login_BTN_continue = findViewById(R.id.login_BTN_continue);
    }


}

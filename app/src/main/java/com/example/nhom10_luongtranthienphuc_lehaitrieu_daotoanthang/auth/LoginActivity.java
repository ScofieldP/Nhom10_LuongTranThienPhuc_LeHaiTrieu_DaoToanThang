package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;

public class LoginActivity extends AppCompatActivity {

    TextView txtNewMem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        txtNewMem = findViewById(R.id.txtNewMember);
        txtNewMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
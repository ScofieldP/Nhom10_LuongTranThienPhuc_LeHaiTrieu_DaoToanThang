package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;

public class RegisterActivity extends AppCompatActivity {
    TextView txtAlready;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        txtAlready = findViewById(R.id.txtAlready);
        txtAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}
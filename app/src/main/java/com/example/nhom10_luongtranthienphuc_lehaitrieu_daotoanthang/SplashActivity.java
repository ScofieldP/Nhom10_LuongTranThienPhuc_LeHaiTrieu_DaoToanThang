package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.window.SplashScreen;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.auth.LoginActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.intro.IntroActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, 3000);
    }
}
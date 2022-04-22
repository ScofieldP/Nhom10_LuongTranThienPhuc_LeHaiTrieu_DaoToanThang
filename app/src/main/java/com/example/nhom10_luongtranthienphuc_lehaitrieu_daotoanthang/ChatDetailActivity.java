package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class ChatDetailActivity extends AppCompatActivity {
    FirebaseDatabase fDB;
    FirebaseAuth fAuth;
    TextView tvUName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        tvUName = findViewById(R.id.tvUName);

        fDB = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();

        String senderID =  fAuth.getUid();
        String receiverID = getIntent().getStringExtra("userID");
        String username = getIntent().getStringExtra("username");

        tvUName.setText(username);

    }
}
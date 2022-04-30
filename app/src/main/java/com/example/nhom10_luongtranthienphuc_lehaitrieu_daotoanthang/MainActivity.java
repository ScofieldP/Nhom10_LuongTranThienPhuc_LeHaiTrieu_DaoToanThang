package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.fragment.FriendFragment;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.fragment.FriendRequestFragment;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.fragment.MessageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.Base64;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;
    RoundedImageView imgProfile;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        imgProfile = findViewById(R.id.imgProfile);
        displayUserProfile();
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        setSupportActionBar(toolbar);
        display(R.id.mnuMess);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                display(item.getItemId());
                return true;
            }
        });
    }

    void display(int id) {
        Fragment frag = null;
        switch (id) {
            case R.id.mnuMess:
                toolbar.setTitle("Tin nhắn");
                frag = new MessageFragment();
                break;
            case R.id.mnuFriend:
                toolbar.setTitle("Bạn bè");
                frag = new FriendFragment();
                break;
            case R.id.mnuFRequest:
                toolbar.setTitle("Lời mời kết bạn");
                frag = new FriendRequestFragment();
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        ft.commit();

    }

    void displayUserProfile(){
        sharedPreferences = getSharedPreferences("imgUser", Context.MODE_PRIVATE);
        String previouslyEncodedImage = sharedPreferences.getString("img_data",null);
            byte[] bytes = Base64.getDecoder().decode(previouslyEncodedImage);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
            imgProfile.setImageBitmap(bitmap);


    }

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference().child("presence").child(currentId).setValue("Offline");
    }

}
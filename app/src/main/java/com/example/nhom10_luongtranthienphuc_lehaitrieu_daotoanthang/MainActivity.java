package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.fragment.FriendFragment;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.fragment.FriendRequestFragment;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.fragment.MessageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
//    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
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
//                toolbar.setTitle("Tin nhắn");
                frag = new MessageFragment();
                break;
            case R.id.mnuFriend:
//                toolbar.setTitle("Bạn bè");
                frag = new FriendFragment();
                break;
            case R.id.mnuFRequest:
//                toolbar.setTitle("Lời mời kết bạn");
                frag = new FriendRequestFragment();
                break;
        }
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, frag);
        ft.commit();

    }
}
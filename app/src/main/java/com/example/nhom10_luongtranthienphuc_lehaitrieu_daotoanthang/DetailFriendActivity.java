package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter.SearchFriendAdapter;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.Message;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Base64;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailFriendActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView tvUser;
    AppCompatButton btnAddFriend;
    FirebaseAuth fAuth;
    FirebaseDatabase fDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_friend);
        circleImageView = findViewById(R.id.profile_image);
        tvUser = findViewById(R.id.tvUserName);
        btnAddFriend = findViewById(R.id.btnAddFriend);

//        Firebase
        fAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance();

        String username = getIntent().getStringExtra("username");
        tvUser.setText(username);
        circleImageView.setImageBitmap(getUserImage(getIntent().getStringExtra("img_profile")));


    }
    private Bitmap getUserImage(String encodedImage){
        byte []bytes = Base64.getDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);

    }
}
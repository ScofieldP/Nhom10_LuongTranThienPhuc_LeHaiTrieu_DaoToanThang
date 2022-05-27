package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter.SearchFriendAdapter;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFriendActivity extends AppCompatActivity {
    FirebaseDatabase fDB;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    ImageView backArrow;
    RecyclerView rvFindFriend;
    EditText searchbar;
    CircleImageView circleImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        backArrow = findViewById(R.id.back_arrow);
        circleImageView = findViewById(R.id.profile_image);
        rvFindFriend = findViewById(R.id.rvFindFriend);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SearchFriendActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        //        Firebase
        fDB = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        //RecyclerView (SearchAdapter)
        final ArrayList<User> friendList = new ArrayList<>();
        final SearchFriendAdapter searchFriendAdapter = new SearchFriendAdapter(friendList);
        rvFindFriend.setAdapter(searchFriendAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvFindFriend.setLayoutManager(layoutManager);

        fDB.getReference().child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    User users = dataSnapshot.getValue(User.class);
                    users.setUserID(dataSnapshot.getKey());
                    friendList.add(users);
                }
                searchFriendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
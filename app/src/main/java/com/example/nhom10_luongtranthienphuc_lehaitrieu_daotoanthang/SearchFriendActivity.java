package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import static com.google.firebase.crashlytics.buildtools.reloc.org.checkerframework.checker.units.UnitsTools.s;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

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
    CircleImageView circleImageView;
    ArrayList<User> friendList;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        backArrow = findViewById(R.id.back_arrow);
        circleImageView = findViewById(R.id.profile_image);
        rvFindFriend = findViewById(R.id.rvFindFriend);
        toolbar = findViewById(R.id.toolbarSearch);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tìm kiếm bạn bè");

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
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvFindFriend.setLayoutManager(layoutManager);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem menuItem = menu.findItem(R.id.search_friend);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Search(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);


    }
    private void Search(String s){
        fDB.getReference().child("users").orderByChild("username").startAt(s).endAt(s+"\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                friendList = new ArrayList<>();
                for (DataSnapshot dataSnapshot :snapshot.getChildren()){
                    User users = dataSnapshot.getValue(User.class);
                    users.setUserID(dataSnapshot.getKey());
                    friendList.add(users);
                }
                SearchFriendAdapter searchFriendAdapter = new SearchFriendAdapter(friendList);
                rvFindFriend.setAdapter(searchFriendAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
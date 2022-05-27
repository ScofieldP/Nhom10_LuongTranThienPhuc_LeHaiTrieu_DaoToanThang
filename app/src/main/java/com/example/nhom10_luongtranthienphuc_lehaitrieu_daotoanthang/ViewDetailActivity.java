package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewDetailActivity extends AppCompatActivity {
    DatabaseReference mUserRef, requestRef, friendRef;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    String username, profileImg;
    String mUsername, mProfileImg;
    CircleImageView circleImageView;
    TextView tvName;
    AppCompatButton btnAdd, btnDecline;
    String CurrentState = "nothing_happen";
    String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);
        userID  = getIntent().getStringExtra("userID");
        Toast.makeText(this, ""+userID, Toast.LENGTH_SHORT).show();

        circleImageView = findViewById(R.id.profile_image);
        tvName = findViewById(R.id.tvUserName);
        btnAdd = findViewById(R.id.btnAddFriend);
        btnDecline = findViewById(R.id.btnDecline);

        mUserRef = FirebaseDatabase.getInstance().getReference().child("users");
        requestRef = FirebaseDatabase.getInstance().getReference().child("requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("friends");

        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

//        của bạn bè
        LoadUser();

//        của mình
        loadMyProfile();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAction(userID);

            }
        });

        CheckUserExistane(userID);

    }

    private void CheckUserExistane(String userID) {
        friendRef.child(fUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CurrentState ="friend";
                    btnAdd.setText("Gửi tin nhắn");
                    btnDecline.setText("Hủy kết bạn");
                    btnDecline.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        friendRef.child(userID).child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    CurrentState ="friend";
                    btnAdd.setText("Gửi tin nhắn");
                    btnDecline.setText("Hủy kết bạn");
                    btnDecline.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        requestRef.child(fUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "I_sent_pending";
                        btnAdd.setText("Hủy lời mời");
                        btnDecline.setVisibility(View.GONE);
                    }
                }
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("decline")){
                        CurrentState = "I_sent_decline";
                        btnAdd.setText("Hủy lời mời");
                        btnDecline.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        requestRef.child(userID).child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("pending")){
                        CurrentState = "he_sent_pending";
                        btnAdd.setText("Chấp nhận lời mời kết bạn");
                        btnDecline.setText("Từ chối");
                        btnDecline.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (CurrentState.equals("nothing_happen")){
            CurrentState = "nothing_happen";
            btnAdd.setText("Kết bạn");
            btnDecline.setVisibility(View.GONE);
        }
    }

    private void PerformAction(String userID){
        if (CurrentState.equals("nothing_happen")){
            Map<String,Object> hashMap  = new HashMap<>();
            hashMap.put("status","pending");
            hashMap.put("username", username);
            requestRef.child(fUser.getUid()).child(userID).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ViewDetailActivity.this,"Đã gửi lời mời kết bạn", Toast.LENGTH_SHORT).show();
                        btnDecline.setVisibility(View.GONE);
                        CurrentState = "I_sent_pending";
                        btnAdd.setText("Hủy lời mời");
                    }
                    else {
                        Toast.makeText(ViewDetailActivity.this,""+task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
//        Dã gửi lời mời
        if (CurrentState.equals("I_sent_pending")|| CurrentState.equals("I_sent_decline")){
            requestRef.child(fUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(ViewDetailActivity.this, "Đã hủy lời mời kết bạn",Toast.LENGTH_SHORT).show();
                        CurrentState = "nothing_happen";
                        btnAdd.setText("Kết bạn");
                        btnDecline.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(ViewDetailActivity.this,""+task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }

        if (CurrentState.equals("he_sent_pending")){
            requestRef.child(userID).child(fUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Map<String, Object> user = new HashMap<>();
                        user.put("status","friend");
                        user.put("username",username);
                        user.put ("image",profileImg);

                        Map<String, Object> user1 = new HashMap<>();
                        user1.put("status","friend");
                        user1.put("username",mUsername);
                        user1.put ("image",mProfileImg);
                        friendRef.child(fUser.getUid()).child(userID).updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    friendRef.child(userID).child(fUser.getUid()).updateChildren(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            Toast.makeText(ViewDetailActivity.this, "Đã là bạn bè",Toast.LENGTH_SHORT).show();
                                            CurrentState = "friend";
                                            btnAdd.setText("Gửi tin nhắn");
                                            btnDecline.setText("Hủy kết bạn");
                                            btnDecline.setVisibility(View.VISIBLE);
                                        }
                                    });
                                }
                            }
                        });
                    }
                }
            });
        }
    }
    private void LoadUser() {
        mUserRef.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    profileImg = snapshot.child("image").getValue().toString();
                    username = snapshot.child("username").getValue().toString();
                    circleImageView.setImageBitmap(getUserImage(profileImg));
                    tvName.setText(username);
                }
                else {
                    Toast.makeText(ViewDetailActivity.this,"Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ViewDetailActivity.this, ""+error.getMessage().toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMyProfile(){
        mUserRef.child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    mProfileImg = snapshot.child("image").getValue().toString();
                    mUsername = snapshot.child("username").getValue().toString();
                }
                else {
                    Toast.makeText(ViewDetailActivity.this,"Data Not Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private Bitmap getUserImage(String encodedImage){
        byte []bytes = Base64.getDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);

    }

}
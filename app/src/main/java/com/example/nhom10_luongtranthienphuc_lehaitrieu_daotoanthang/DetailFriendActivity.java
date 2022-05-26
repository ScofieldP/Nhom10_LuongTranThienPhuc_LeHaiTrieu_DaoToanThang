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
import android.widget.Toast;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter.SearchFriendAdapter;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.Message;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailFriendActivity extends AppCompatActivity {
    CircleImageView circleImageView;
    TextView tvUser;
    AppCompatButton btnAddFriend, btnDecline;
    FirebaseAuth fAuth;
    FirebaseDatabase fDB;
    FirebaseUser fUser;
    String username;
    Bitmap encodedImage;
    String profileImg;
    String currentState ="nothing_happen";
    DatabaseReference mUserref,requestRef,friendRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_friend);
        circleImageView = findViewById(R.id.profile_image);
        tvUser = findViewById(R.id.tvUserName);
        btnAddFriend = findViewById(R.id.btnAddFriend);
        btnDecline = findViewById(R.id.btnDecline);

//        nhận từ searchfriendAdapter
        String userID = getIntent().getStringExtra("userID");
        username = getIntent().getStringExtra("username");
        tvUser.setText(username);
        encodedImage = getUserImage(getIntent().getStringExtra("img_profile"));
        profileImg = encodeImage(encodedImage);
        circleImageView.setImageBitmap(encodedImage);
//        Firebase
        fAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance();
        fUser = fAuth.getCurrentUser();
        mUserref = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        requestRef = FirebaseDatabase.getInstance().getReference().child("requests");
        friendRef = FirebaseDatabase.getInstance().getReference().child("friends");

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PerformAction(userID);
            }
        });
        CheckUserExitance(userID);
    }

    private void CheckUserExitance(String userID) {
//        ID của mình, đã là bạn bè
        friendRef.child(fUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    currentState = "friend";
                    btnAddFriend.setText("Send SMS");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        ID bạn bè
        friendRef.child(userID).child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    currentState = "friend";
                    btnAddFriend.setText("Send SMS");
                    btnDecline.setText("Unfriend");
                    btnDecline.setVisibility(View.VISIBLE);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//        Đã gửi
        requestRef.child(fUser.getUid()).child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("pending"));
                    {
                        currentState ="I_sent_pending";
                        btnAddFriend.setText("Hủy kết bạn");
                        btnDecline.setVisibility(View.GONE);

                    }
                    if (snapshot.child("status").getValue().toString().equals("decline"));
                    {
                        currentState ="I_sent_decline";
                        btnAddFriend.setText("Hủy kết bạn");
                        btnDecline.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//        Bạn bè gửi lời mời
        requestRef.child(userID).child(fUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    if (snapshot.child("status").getValue().toString().equals("pending"));
                    {
                        currentState ="he_sent_pending";
                        btnAddFriend.setText("Chấp nhận lời mời kết bạn");
                        btnDecline.setText("Decline Friend");
                        btnDecline.setVisibility(View.VISIBLE);

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (currentState.equals("nothing_happen")){
            currentState ="nothing_happen";
            btnAddFriend.setText("Kết bạn");
            btnDecline.setVisibility(View.GONE);
        }
    }

    private void PerformAction(String userID) {
//        Chưa là bạn bè
        if (currentState.equals("nothing_happen")){
            Map<String, Object> user = new HashMap<>();
            user.put("status","pending");
            requestRef.child(fUser.getUid()).child(userID).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if (task.isSuccessful()){
                        Toast.makeText(DetailFriendActivity.this, "Đã gửi lời mời kết bạn", Toast.LENGTH_SHORT).show();
                        btnDecline.setVisibility(View.GONE);
                        currentState = "I_sent_pending";
                        btnAddFriend.setText("Hủy kết bạn");
                    }
                    else {
                        Toast.makeText(DetailFriendActivity.this,"" +task.getException().toString(), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        if (currentState.equals("I_sent_pending") || currentState.equals("I_sent_decline")){
            requestRef.child(fUser.getUid()).child(userID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Toast.makeText(DetailFriendActivity.this, "Đã hủy lời mời kết bạn",Toast.LENGTH_SHORT).show();
                        currentState = "nothing_happen";
                        btnAddFriend.setText("Kết bạn");
                        btnDecline.setVisibility(View.GONE);
                    }
                    else {
                        Toast.makeText(DetailFriendActivity.this,"" +task.getException().toString(), Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
        if (currentState.equals("he_sent_pending")){
            requestRef.child(userID).child(fUser.getUid()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Map<String, Object> user = new HashMap<>();
                        user.put("status","friend");
                        user.put("username",username);
                        user.put ("image",profileImg);
                        friendRef.child(fUser.getUid()).child(userID).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    friendRef.child(userID).child(fUser.getUid()).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            Toast.makeText(DetailFriendActivity.this,"Đã là bạn bè",Toast.LENGTH_SHORT).show();
                                            currentState ="friend";
                                            btnAddFriend.setText("Send SMS");
                                            btnDecline.setText("Unfriend");
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
        if (currentState.equals("friend")){
            //
        }
    }

    private Bitmap getUserImage(String encodedImage){
        byte []bytes = Base64.getDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);

    }


    private String encodeImage(Bitmap bitmap){
        int previewWidth =150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }
}
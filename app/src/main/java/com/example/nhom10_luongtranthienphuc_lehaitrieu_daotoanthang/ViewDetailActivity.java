package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Base64;

import de.hdodenhof.circleimageview.CircleImageView;

public class ViewDetailActivity extends AppCompatActivity {
    DatabaseReference mUserRef;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    String username, profileImg;
    CircleImageView circleImageView;
    TextView tvName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail);
        String userID  = getIntent().getStringExtra("userID");
        Toast.makeText(this, ""+userID, Toast.LENGTH_SHORT).show();

        circleImageView = findViewById(R.id.profile_image);
        tvName = findViewById(R.id.tvUserName);
        mUserRef = FirebaseDatabase.getInstance().getReference().child("users").child(userID);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();

        LoadUser();
    }

    private void LoadUser() {
        mUserRef.addValueEventListener(new ValueEventListener() {
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
    private Bitmap getUserImage(String encodedImage){
        byte []bytes = Base64.getDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);

    }

}
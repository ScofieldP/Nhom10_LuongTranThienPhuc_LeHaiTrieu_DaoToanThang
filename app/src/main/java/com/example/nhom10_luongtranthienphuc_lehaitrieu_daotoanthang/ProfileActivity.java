package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.auth.ChangePasswordActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.auth.LoginActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    MaterialButton btnLogOut;
    CircleImageView imgProfile;
    TextView tvName, tvEmail, tvProfile, tvPassword;
    private String encodedImage;

    TextInputEditText tvNewName;
    FirebaseDatabase fdb;
    FirebaseAuth fAuth;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        btnLogOut = findViewById(R.id.btnLogOut);
        imgProfile = findViewById(R.id.profile_image);
        tvName = findViewById(R.id.tvPname);
        tvEmail = findViewById(R.id.tvPemail);
        tvProfile = findViewById(R.id.editProfile);
        tvPassword = findViewById(R.id.editPassword);

        fdb = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        String userID = fAuth.getCurrentUser().getUid();
        fdb.getReference().child("users").child(userID).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                user.setUserID(userID);
                tvName.setText(user.getUsername());
                tvEmail.setText(user.getEmail());
                imgProfile.setImageBitmap(getUserImage(user.image));

            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
        //
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        tvPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
                startActivity(intent);
            }
        });


//      Thay đổi tên người dùng
        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(ProfileActivity.this);
                dialog.setContentView(R.layout.activity_change_profile);
                Window window = dialog.getWindow();
                if (window == null){
                    return;

                }
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                WindowManager.LayoutParams windowAttribute = window.getAttributes();
                window.setAttributes(windowAttribute);

//                if (Gravity.BOTTOM == windowAttribute.gravity){
//                    dialog.setCancelable(true);
//                }
//                else {
//                    dialog.setCancelable(false);
//                }

                AppCompatButton btnCancel, btnConfirm;
                btnCancel = dialog.findViewById(R.id.btnCancel);
                btnConfirm = dialog.findViewById(R.id.btnConfitm);
                tvNewName = dialog.findViewById(R.id.tvMiniName);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btnConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String[] words = tvNewName.getText().toString().split("\\s");
                        Update(words[0], tvEmail.getText().toString(), encodedImage );
                    }
                });
                dialog.show();

            }

        });
    }


    private Bitmap getUserImage(String encodedImage){
        byte []bytes = Base64.getDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);

    }
    private void Update(String username, String email, String image) {
        fdb = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        encodedImage = encodeImage(getUserImage(user.image));
        image = encodedImage;
        DatabaseReference reference = fdb.getReference();
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("image", image);
        reference.child("users").child(fAuth.getUid()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Cập nhật thành công",
                                Toast.LENGTH_SHORT).show();

                        TextView tvNewName = findViewById(R.id.tvMiniName);
                        tvNewName.setText(username);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
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
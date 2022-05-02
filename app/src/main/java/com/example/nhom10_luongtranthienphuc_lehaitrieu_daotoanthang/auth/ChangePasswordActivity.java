package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.auth;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.ProfileActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class ChangePasswordActivity extends AppCompatActivity {
    TextView tvUserName;
    AppCompatButton btnConfirm;
    TextInputEditText tvOldPass, tvNewPass, tvConfirmPass;
    ImageView back_arrow;
    FirebaseAuth fAuth;
    FirebaseUser fUser;
    FirebaseDatabase fDB;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        tvUserName = findViewById(R.id.tvGetUs);
        tvNewPass = findViewById(R.id.tvPassword);
        tvConfirmPass = findViewById(R.id.tvConfirmPassword);
        btnConfirm = findViewById(R.id.btnRegister);
        back_arrow = findViewById(R.id.back_arrow);
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        fDB = FirebaseDatabase.getInstance();

        back_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePasswordActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newPass = tvNewPass.getText().toString();
                fUser.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Thay đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                            fAuth.signOut();
                            Intent intent = new Intent(ChangePasswordActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
            }
        });

        String userID = fAuth.getCurrentUser().getUid();
        fDB.getReference().child("users").child(userID).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
                user.setUserID(userID);
                tvUserName.setText(user.getUsername());


            }
        });
    }
}
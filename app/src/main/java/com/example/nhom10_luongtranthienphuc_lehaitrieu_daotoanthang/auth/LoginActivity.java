package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.auth;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.MainActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    TextView txtNewMem;
    EditText editUser, editPass;
    Button btnSignIn;
    FirebaseAuth fAuth;
    FirebaseDatabase fDB;
    ActivityResultLauncher<Intent> mActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Intent intent = result.getData();
                    String email = intent.getStringExtra("email");

                    editUser.setText(email);
                }
            }
    );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editUser = findViewById(R.id.edit_user_log_in);
        editPass = findViewById(R.id.edit_pass_log_in);
        btnSignIn = findViewById(R.id.btnLogin);
        txtNewMem = findViewById(R.id.txtNewMember);
        fAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance();
        if (fAuth.getCurrentUser() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();

        }
        //????ng k??
        txtNewMem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                mActivityLauncher.launch(intent);            }
        });

        //????ng nh???p
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = editUser.getText().toString();
                String pass = editPass.getText().toString();
                if (editUser.getText().toString().isEmpty()){
                    editUser.setError("Vui l??ng nh???p t??n t??i kho???n");
                    return;
                }
                if (editPass.getText().toString().isEmpty()){
                    editPass.setError("Vui l??ng nh???p m???t kh???u");
                    return;
                }
                if (editPass.length()<6){
                    editPass.setError("M???t kh???u kh??ng ???????c d?????i 6 k?? t???");
                    return;
                }
                fAuth.signInWithEmailAndPassword(user,pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this, "????ng nh???p th??nh c??ng", Toast.LENGTH_SHORT).show();
                                    Intent intent= new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else {
                                    Toast.makeText(LoginActivity.this, "????ng nh???p th???t b???i", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }

        });
    }
}
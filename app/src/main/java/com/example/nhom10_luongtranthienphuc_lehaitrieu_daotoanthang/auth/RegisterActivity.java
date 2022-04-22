package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.auth;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    TextView txtAlready;
    RoundedImageView imgProfile;
    EditText editMail, editUser, editPass,editPass1;
    MaterialButton btnSignUp;
    FirebaseAuth fAuth;
    private String encodedImage;
    String userID;
    FirebaseDatabase fDB;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imgProfile = findViewById(R.id.imgProfile);
        editMail = findViewById(R.id.editEmail);
        editUser = findViewById(R.id.editUser);
        editPass = findViewById(R.id.editPass);
        editPass1 = findViewById(R.id.editPass1);
        btnSignUp =  findViewById(R.id.btnRegister);
        fAuth = FirebaseAuth.getInstance();
        fDB = FirebaseDatabase.getInstance();

        //thêm avatar
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                pickImg.launch(intent);


            }
        });
        //đã có tài khoản
        txtAlready = findViewById(R.id.txtAlready);
        txtAlready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

//        Đăng kí
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = editMail.getText().toString();
                String password = editPass.getText().toString();
                String username = editUser.getText().toString();
                String image = encodedImage;
                if (!isValid(email)) {
                    editMail.setError("Sai phương thức nhập Email");
                    return;
                }
                if (editUser.getText().toString().isEmpty()) {
                    editUser.setError("Nhập tên người dùng");
                    return;
                }
                if (editPass.getText().toString().isEmpty()) {
                    editPass.setError("Nhập mật khẩu");
                    return;
                }
                if (editPass1.getText().toString().isEmpty()) {
                    editPass1.setError("Nhập mật khẩu");
                }
                if (!editPass.getText().toString().equals(editPass1.getText().toString())) {

                    editPass.setError("Mật khẩu không giống nhau");
                    editPass1.setText("");
                    return;
                }
                fAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Đăng kí thành công", Toast.LENGTH_LONG).show();
                                    userID = fAuth.getCurrentUser().getUid();
                                    DatabaseReference databaseReference = fDB.getReference();
                                    Map<String, Object> user = new HashMap<>();
                                    user.put("username", username);
                                    user.put("email", email);
                                    user.put("image", image);

                                    databaseReference.child("users").child(userID)
                                            .setValue(user)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> aVoid) {
                                                    if(aVoid.isSuccessful()){
                                                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                                        intent.putExtra("email", email);
                                                        setResult(Activity.RESULT_OK, intent);
                                                        sharedPreferences = getSharedPreferences("imgUser", Context.MODE_PRIVATE);
                                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                                        editor.putString("img_data", encodedImage);
                                                        editor.apply();
                                                        finish();
                                                    }

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });
    }

    //auth
    public static boolean isValid(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    //Avatar
    private String encodeImage(Bitmap bitmap){
        int previewWidth =150;
        int previewHeight = bitmap.getHeight() * previewWidth / bitmap.getWidth();
        Bitmap previewBitmap = Bitmap.createScaledBitmap(bitmap, previewWidth, previewHeight, false);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        previewBitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
        byte[] bytes = byteArrayOutputStream.toByteArray();
        return Base64.getEncoder().encodeToString(bytes);
    }

    private ActivityResultLauncher<Intent> pickImg = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
              if (result.getResultCode() == RESULT_OK){
                  if(result.getData()!= null){
                      Uri imgUri = result.getData().getData();
                      try {
                          InputStream inputStream = getContentResolver().openInputStream(imgUri);
                          Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                          imgProfile.setImageBitmap(bitmap);
                          encodedImage =encodeImage(bitmap);
                      } catch (FileNotFoundException e) {
                          e.printStackTrace();
                      }
                  }
              }
            }
            );
}
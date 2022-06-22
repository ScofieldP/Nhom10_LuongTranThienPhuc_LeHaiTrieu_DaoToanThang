package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter.ChatAdapter;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.Message;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatDetailActivity extends AppCompatActivity {
    FirebaseDatabase fDB;
    FirebaseAuth fAuth;
    FirebaseStorage storage;
    TextView tvUName, tvOnline;
    ImageView backArrow, send, btnAttach, btnVideo;
    RecyclerView rvChatDetails;
    VideoView videoView;
    EditText edtMess;
    CircleImageView circleImageView;
    String senderRoom, receiverRoom;
    String senderID;
    String receiverID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        tvUName = findViewById(R.id.tvUName);
        tvOnline = findViewById(R.id.txtOnline);
        backArrow = findViewById(R.id.back_arrow);
        send = findViewById(R.id.send);
        edtMess = findViewById(R.id.etMessage);
        circleImageView = findViewById(R.id.profile_image);
        btnAttach = findViewById(R.id.btnAttach);
        btnVideo = findViewById(R.id.btnVideo);
        rvChatDetails = findViewById(R.id.rvChatDetail);

        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

//        Firebase
        fDB = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();


//        Toolbar
//        Nhận từ messageadapter (header)
        senderID =  fAuth.getUid();
        receiverID = getIntent().getStringExtra("userID");
        String username = getIntent().getStringExtra("username");
        tvUName.setText(username);
        circleImageView.setImageBitmap(getUserImage(getIntent().getStringExtra("img_profile")));

        //        Online, offline
        fDB.getReference().child("presence").child(receiverID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    String status = snapshot.getValue(String.class);
                    if(!status.isEmpty()) {
                        if(status.equals("Offline")) {
                            tvOnline.setVisibility(View.GONE);
                        } else {
                            tvOnline.setText(status);
                            tvOnline.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

//      Đính tệp tin
        btnAttach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uploadImg.launch(intent);
            }
        });


//        Content
        //RecyclerView (ChatAdapter)
        final ArrayList<Message> messages = new ArrayList<>();

        Bitmap receiverImg;
        receiverImg = getUserImage(getIntent().getStringExtra("img_profile"));

        senderRoom = senderID + receiverID;
        receiverRoom = receiverID + senderID;

        final ChatAdapter chatAdapter = new ChatAdapter(messages,this, receiverImg,senderRoom, receiverRoom);
        rvChatDetails.setAdapter(chatAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvChatDetails.setLayoutManager(layoutManager);

        fDB.getReference().child("chats")
                .child(senderRoom)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messages.clear();
                        for (DataSnapshot snapshot1: snapshot.getChildren()){
                            Message model = snapshot1.getValue(Message.class);
                            model.setMessageID(snapshot1.getKey());
                            messages.add(model);
                        }
                        chatAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

//Gửi tin nhăn
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mess = edtMess.getText().toString();
                String randomKey = fDB.getReference().push().getKey();

                final Message mMess =  new Message(senderID, mess);
                mMess.setTimeStamp(new Date().getTime());
                edtMess.setText("");
                fDB.getReference().child("chats")
                        .child(senderRoom)
                        .child(randomKey)
                        .setValue(mMess).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        fDB.getReference().child("chats")
                                .child(receiverRoom)
                                .child(randomKey)
                                .setValue(mMess).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });
            }
        });
    }

        private Bitmap getUserImage(String encodedImage){
            byte []bytes = Base64.getDecoder().decode(encodedImage);
            return BitmapFactory.decodeByteArray(bytes,0, bytes.length);

        }
    private ActivityResultLauncher<Intent> uploadImg = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK){
                    if(result.getData()!= null){
                        Uri selectImg = result.getData().getData();
                        StorageReference reference = storage.getReference().child("chats");
                        reference.putFile(selectImg).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()){
                                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String filePath = uri.toString();
                                            String mess = edtMess.getText().toString();
                                            String randomKey = fDB.getReference().push().getKey();
                                            final Message mMess =  new Message(senderID, mess);
//                                            đẩy hình
                                            mMess.setMessage("photo");
                                            mMess.setImageUrl(filePath);
                                            mMess.setTimeStamp(new Date().getTime());
                                            edtMess.setText("");
                                            fDB.getReference().child("chats")
                                                    .child(senderRoom)
                                                    .child(randomKey)
                                                    .setValue(mMess).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    fDB.getReference().child("chats")
                                                            .child(receiverRoom)
                                                            .child(randomKey)
                                                            .setValue(mMess).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {

                                                        }
                                                    });
                                                }
                                            });
                                            Toast.makeText(ChatDetailActivity.this, filePath, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

                    }
                }
            }
    );

    @Override
    protected void onResume() {
        super.onResume();
        String currentId = FirebaseAuth.getInstance().getUid();
        fDB.getReference().child("presence").child(currentId).setValue("Online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        String currentId = FirebaseAuth.getInstance().getUid();
        fDB.getReference().child("presence").child(currentId).setValue("Offline");
    }
}
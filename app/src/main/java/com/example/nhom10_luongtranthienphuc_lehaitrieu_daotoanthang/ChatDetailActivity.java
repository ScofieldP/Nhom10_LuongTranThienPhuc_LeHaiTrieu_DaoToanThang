package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter.ChatAdapter;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatDetailActivity extends AppCompatActivity {
    FirebaseDatabase fDB;
    FirebaseAuth fAuth;
    TextView tvUName;
    ImageView backArrow, send;
    RecyclerView rvChatDetails;
    EditText edtMess;
    CircleImageView circleImageView,smallIcon;
    String senderRoom, receiverRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        tvUName = findViewById(R.id.tvUName);
        backArrow = findViewById(R.id.back_arrow);
        send = findViewById(R.id.send);
        edtMess = findViewById(R.id.etMessage);
        circleImageView = findViewById(R.id.profile_image);
//        smallIcon = findViewById(R.id.smaill_icon);
        rvChatDetails = findViewById(R.id.rvChatDetail);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        fDB = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();

//        Nhận từ messageadapter (header)
        final String senderID =  fAuth.getUid();
        String receiverID = getIntent().getStringExtra("userID");
        String username = getIntent().getStringExtra("username");
        tvUName.setText(username);
        circleImageView.setImageBitmap(getUserImage(getIntent().getStringExtra("img_profile")));


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

        String randomKey = fDB.getReference().push().getKey();
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mess = edtMess.getText().toString();
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

}
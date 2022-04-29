package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.Message;
import com.github.pgreze.reactions.ReactionPopup;
import com.github.pgreze.reactions.ReactionsConfig;
import com.github.pgreze.reactions.ReactionsConfigBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter{

    ArrayList<Message> mMessage;
    Context context;
    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;
    private final Bitmap receiverImg;
    String senderRoom;
    String receiverRoom;
    public ChatAdapter(ArrayList<Message> mMessage, Context context, Bitmap receiverImg, String senderRoom, String receiverRoom) {
        this.mMessage = mMessage;
        this.context = context;
        this.receiverImg = receiverImg;
        this.senderRoom = senderRoom;
        this.receiverRoom = senderRoom;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.item_sent,parent,false);
            return new SenderViewHolder(view);
        }
        else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_receiver,parent,false);
            return new ReceiverViewHolder(view);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (mMessage.get(position).getuID().equals(FirebaseAuth.getInstance().getUid())){
            return SENDER_VIEW_TYPE;
        }
        else
        {
            return RECEIVER_VIEW_TYPE;
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = mMessage.get(position);

        int [] reaction = new int[]{
                R.drawable.ic_fb_like,
                R.drawable.ic_fb_love,
                R.drawable.ic_fb_laugh,
                R.drawable.ic_fb_wow,
                R.drawable.ic_fb_sad,
                R.drawable.ic_fb_angry
        };
        ReactionsConfig config = new ReactionsConfigBuilder(context)
                .withReactions(reaction)
                .build();

        ReactionPopup popup = new ReactionPopup(context, config, (pos) -> {
            if (holder.getClass() == SenderViewHolder.class){
                ((SenderViewHolder)holder).reactFeeling.setImageResource(reaction[pos]);
                ((SenderViewHolder)holder).reactFeeling.setVisibility(View.VISIBLE);


            }
            else {
                ((ReceiverViewHolder)holder).reactFeeling.setImageResource(reaction[pos]);
                ((ReceiverViewHolder)holder).reactFeeling.setVisibility(View.VISIBLE);

            }
            message.setFeeling(pos);
            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(senderRoom)
                    .child(message.getMessageID())
                    .setValue(message);

            FirebaseDatabase.getInstance().getReference()
                    .child("chats")
                    .child(receiverRoom)
                    .child(message.getMessageID())
                    .setValue(message);
            return true; // true is closing popup, false is requesting a new selection
        });




        if (holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(message.getMessage());
            ((SenderViewHolder)holder).senderTime.setText(getDateFromTimeStamp(message.getTimeStamp()));
            if (message.getFeeling() >= 0){
//                message.setFeeling(reaction[(int ) message.getFeeling()]);
                ((SenderViewHolder)holder).reactFeeling.setImageResource(reaction[(int ) message.getFeeling()]);
                ((SenderViewHolder)holder).reactFeeling.setVisibility(View.VISIBLE);
            }
            else {
                ((SenderViewHolder)holder).reactFeeling.setVisibility(View.GONE);
            }
            ((SenderViewHolder)holder).senderMsg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popup.onTouch(view, motionEvent);
                    return false;
                }
            });
        }
        else {
            ((ReceiverViewHolder)holder).receiverMsg.setText(message.getMessage());
            if (message.getFeeling() >= 0){
                ((ReceiverViewHolder)holder).reactFeeling.setImageResource(reaction[(int ) message.getFeeling()]);
                ((ReceiverViewHolder)holder).reactFeeling.setVisibility(View.VISIBLE);
            }
            else {
                ((ReceiverViewHolder)holder).reactFeeling.setVisibility(View.GONE);
            }
            ((ReceiverViewHolder)holder).receiverMsg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popup.onTouch(view, motionEvent);
                    return false;
                }
            });
            ((ReceiverViewHolder)holder).circleImageView.setImageBitmap(receiverImg);
            ((ReceiverViewHolder)holder).receiverTime.setText(getDateFromTimeStamp(message.getTimeStamp()));
        }
    }

    @Override
    public int getItemCount() {
        return mMessage.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder{
        TextView receiverMsg, receiverTime;
        CircleImageView circleImageView;
        ImageView reactFeeling;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.tvReceiver);
            receiverTime = itemView.findViewById(R.id.tvDatetime);
            circleImageView = itemView.findViewById(R.id.smaill_icon);
            reactFeeling = itemView.findViewById(R.id.feelingReceiver);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, senderTime;
        ImageView reactFeeling;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.txtMess);
            senderTime = itemView.findViewById(R.id.dateTime);
            reactFeeling = itemView.findViewById(R.id.feeling);
        }
    }


    private String getDateFromTimeStamp(Long dt) {
        Date date = new Date (dt);
        return new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(date);
    }

}

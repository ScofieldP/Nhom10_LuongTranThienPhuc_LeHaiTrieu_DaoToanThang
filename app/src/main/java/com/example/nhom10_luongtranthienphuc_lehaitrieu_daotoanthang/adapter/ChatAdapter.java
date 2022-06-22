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

import com.bumptech.glide.Glide;
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





        if (holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMsg.setText(message.getMessage());
            ((SenderViewHolder)holder).senderTime.setText(getDateFromTimeStamp(message.getTimeStamp()));

//            Gửi hình
            if (message.getMessage().equals("photo")){
                ((SenderViewHolder)holder).sentImg.setVisibility(View.VISIBLE);
                ((SenderViewHolder)holder).senderMsg.setVisibility(View.GONE);
                Glide.with(context).load(message.getImageUrl()).into( ((SenderViewHolder)holder).sentImg);
            }


        }
        else {
            ((ReceiverViewHolder)holder).receiverMsg.setText(message.getMessage());
            if (message.getMessage().equals("photo")){
                ((ReceiverViewHolder)holder).receiImg.setVisibility(View.VISIBLE);
                ((ReceiverViewHolder)holder).receiverMsg.setVisibility(View.GONE);
                Glide.with(context).load(message.getImageUrl()).into( ((ReceiverViewHolder)holder).receiImg);
            }

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
        ImageView reactFeeling, receiImg;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);
            receiverMsg = itemView.findViewById(R.id.tvReceiver);
            receiverTime = itemView.findViewById(R.id.tvDatetime);
            circleImageView = itemView.findViewById(R.id.smaill_icon);
            reactFeeling = itemView.findViewById(R.id.feelingReceiver);
            receiImg = itemView.findViewById(R.id.receiverImg);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMsg, senderTime;
        ImageView reactFeeling, sentImg;

        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMsg = itemView.findViewById(R.id.txtMess);
            senderTime = itemView.findViewById(R.id.dateTime);
            reactFeeling = itemView.findViewById(R.id.feeling);
            sentImg = itemView.findViewById(R.id.sentImg);
        }
    }


    private String getDateFromTimeStamp(Long dt) {
        Date date = new Date (dt);
        return new SimpleDateFormat("dd-MM-yyyy hh:mm a").format(date);
    }

}

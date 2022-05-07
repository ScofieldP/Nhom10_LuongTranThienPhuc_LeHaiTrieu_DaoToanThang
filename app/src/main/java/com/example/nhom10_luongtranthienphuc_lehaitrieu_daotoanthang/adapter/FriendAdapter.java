package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.ChatDetailActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Base64;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<User> mUser;
    FirebaseUser fUser;
    FirebaseAuth fAuth;
    public FriendAdapter(ArrayList<User> mUser) {
        this.mUser = mUser;
    }
    public class ViewHolderFriend extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView tvUser;
        public ViewHolderFriend(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_image);
            tvUser = itemView.findViewById(R.id.tvName);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_friend,parent,false);
        return new ViewHolderFriend(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = mUser.get(position);
        ViewHolderFriend viewHolderFriend = (ViewHolderFriend) holder;
        viewHolderFriend.tvUser.setText(user.getUsername());
        viewHolderFriend.circleImageView.setImageBitmap(getUserImage(user.image));

        viewHolderFriend.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Truyền dữ liệu qua chatdetail
                Intent intent = new Intent(viewHolderFriend.itemView.getContext(), ChatDetailActivity.class);
                intent.putExtra("userID", user.getUserID());
                intent.putExtra("username", user.getUsername());
                intent.putExtra("img_profile", user.getImage());
                viewHolderFriend.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    //chuyển hình ảnh thành chuỗi
    private Bitmap getUserImage(String encodedImage){
        byte []bytes = Base64.getDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }

}

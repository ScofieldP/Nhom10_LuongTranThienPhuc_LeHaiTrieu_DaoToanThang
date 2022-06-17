package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.ChatDetailActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.DetailFriendActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.ViewDetailActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Base64;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchFriendAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    ArrayList<User> mUser;
    DatabaseReference mUserRef;
    FirebaseUser fUser;
    FirebaseAuth fAuth;
    public SearchFriendAdapter(ArrayList<User> mUser) {
        this.mUser = mUser;
    }
    public class ViewHolderFind extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView tvUser;
        public ViewHolderFind(@NonNull View itemView) {
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
        return new ViewHolderFind(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = mUser.get(position);
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference();
        ViewHolderFind viewHolderFind = (ViewHolderFind) holder;
        if (!fUser.getUid().equals(user.getUserID())){
            viewHolderFind.tvUser.setText(user.getUsername());
            viewHolderFind.circleImageView.setImageBitmap(getUserImage(user.image));
        }
        else {
            viewHolderFind.itemView.setVisibility(View.GONE);
            viewHolderFind.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }



        viewHolderFind.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Truyền dữ liệu qua viewdetail
                Intent intent = new Intent(viewHolderFind.itemView.getContext(), ViewDetailActivity.class);
                intent.putExtra("userID", user.getUserID());
//                intent.putExtra("username", user.getUsername());
//                intent.putExtra("img_profile", user.getImage());

                viewHolderFind.itemView.getContext().startActivity(intent);
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

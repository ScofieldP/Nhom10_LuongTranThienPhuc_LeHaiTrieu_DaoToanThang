package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Base64;

import de.hdodenhof.circleimageview.CircleImageView;

public class FriendRequestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<User> mUser;
    FirebaseAuth fAuth;
    FirebaseUser fUser;

    public FriendRequestAdapter(ArrayList<User> mUser) {
        this.mUser = mUser;
    }

    public class ViewHolderRequest extends RecyclerView.ViewHolder {
        CircleImageView circleImageView;
        TextView tvUser;
        AppCompatButton btnAccept, btnDecline;
        public ViewHolderRequest(@NonNull View itemView) {
            super(itemView);
            circleImageView = itemView.findViewById(R.id.profile_image);
            tvUser = itemView.findViewById(R.id.tvUName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnDecline = itemView.findViewById(R.id.btnDecline);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_request,parent,false);
        return new ViewHolderRequest(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        User user = mUser.get(position);
        fAuth = FirebaseAuth.getInstance();
        fUser = fAuth.getCurrentUser();
        ViewHolderRequest viewHolderRequest = (ViewHolderRequest) holder;
        viewHolderRequest.tvUser.setText(user.getUsername());
//        viewHolderRequest.circleImageView.setImageBitmap(getUserImage(user.image));
    }

    @Override
    public int getItemCount() {
        return mUser.size();
    }

    private Bitmap getUserImage(String encodedImage){
        byte []bytes = Base64.getDecoder().decode(encodedImage);
        return BitmapFactory.decodeByteArray(bytes,0, bytes.length);
    }

}

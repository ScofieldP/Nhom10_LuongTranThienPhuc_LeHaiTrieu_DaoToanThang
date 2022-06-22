package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Filterable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class SearchFriendAdapter extends RecyclerView.Adapter<SearchFriendAdapter.ViewHolderFind> implements Filerable{
    ArrayList<User> mUser;
    DatabaseReference mUserRef;
    FirebaseUser fUser;
    FirebaseAuth fAuth;

    ArrayList<User> mUserTmp;

    public SearchFriendAdapter(ArrayList<User> mUser) {
        this.mUser = mUser;
        this.mUserTmp = new ArrayList<>(mUser);
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

    //Xu li thang Search
    @Override
    public Filter getFilter() {
        return filter;
    }
    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<User> filteredList = new ArrayList<>();
            if(charSequence.toString().isEmpty()){
                filteredList.addAll(mUserTmp);
            } else{
                //khai bao String filterPattern la de tham chieu thang String new text ben activity qua
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for(User x:mUserTmp){
                    if(x.getUsername().toLowerCase().contains(filterPattern)){
                        filteredList.add(x);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredList;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            mUser.clear();
            mUser.addAll((ArrayList) filterResults.values);
            notifyDataSetChanged();
        }
    };


    @NonNull
    @Override
    public ViewHolderFind onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_friend,parent,false);
        return new ViewHolderFind(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderFind holder, int position) {
        User user = mUser.get(position);
        holder.tvUser.setText(user.getUsername());
        holder.circleImageView.setImageBitmap(getUserImage(user.getImage()));

        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserRef = FirebaseDatabase.getInstance().getReference();
        if (!fUser.getUid().equals(user.getUserID())){
            holder.tvUser.setText(user.getUsername());
            holder.circleImageView.setImageBitmap(getUserImage(user.image));
        }
        else {
            holder.itemView.setVisibility(View.GONE);
            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0,0));
        }




        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Truyền dữ liệu qua viewdetail
                Intent intent = new Intent(holder.itemView.getContext(), ViewDetailActivity.class);
                intent.putExtra("userID", user.getUserID());
//                intent.putExtra("username", user.getUsername());
//                intent.putExtra("img_profile", user.getImage());

                holder.itemView.getContext().startActivity(intent);
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


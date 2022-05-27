package com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.MainActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.R;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.SearchFriendActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.adapter.FriendRequestAdapter;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.auth.LoginActivity;
import com.example.nhom10_luongtranthienphuc_lehaitrieu_daotoanthang.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendRequestFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendRequestFragment extends Fragment {

    ArrayList<User> requestList;
    FirebaseDatabase fDB;
    FriendRequestAdapter friendRequestAdapter;
    RecyclerView rvRequest;
    FirebaseAuth fAuth;
    FirebaseUser fUser;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FriendRequestFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FriendRequestFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FriendRequestFragment newInstance(String param1, String param2) {
        FriendRequestFragment fragment = new FriendRequestFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
        requestList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_request, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvRequest = view.findViewById(R.id.rvRequests);
        friendRequestAdapter = new FriendRequestAdapter(requestList);
        rvRequest.setAdapter(friendRequestAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvRequest.setLayoutManager(layoutManager);
        rvRequest.addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL));
        fDB = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        fDB.getReference().child("requests").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User users = dataSnapshot.getValue(User.class);
                    users.setUserID(dataSnapshot.getKey());
                    requestList.add(users);
                }
                friendRequestAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_friend, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.mnuSearch){
            Intent intent = new Intent(getActivity(), SearchFriendActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
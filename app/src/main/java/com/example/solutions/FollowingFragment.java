package com.example.solutions;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FollowingFragment extends Fragment {

    private View FollowingView;
    private RecyclerView recyclerView;
    private DatabaseReference FollowData;
    private FirebaseAuth mAuth;

    DatabaseReference followPost;
    ArrayList<Follow> list;
    FollowAdapter followAdapter;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FollowingView = inflater.inflate(R.layout.fragment_following, container, false);

        recyclerView = (RecyclerView) FollowingView.findViewById(R.id.myPostRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();

        FollowData = FirebaseDatabase.getInstance().getReference().child("follow");

        list = new ArrayList<Follow>();

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //lấy id người đang đăng nhập
        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getUid();

        //Kết nối tới node có tên là Post
        followPost = FirebaseDatabase.getInstance().getReference().child("follow").child(uid);
        followPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //khi trên database sửa đổi, cần xóa list cũ để không hiện thị lặp dữ liệu cũ
                list.clear();
                //vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Follow fl = data.getValue(Follow.class);
                    list.add(fl);
                }
                followAdapter = new FollowAdapter(getContext(), list);
                recyclerView.setAdapter(followAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: "+databaseError,Toast.LENGTH_SHORT).show();
            }
        });


        return FollowingView;
    }
}

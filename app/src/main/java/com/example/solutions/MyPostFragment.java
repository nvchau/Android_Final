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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MyPostFragment extends Fragment {

    private View MyPostView;
    private RecyclerView recyclerView;

    private DatabaseReference HomeData;
    private FirebaseAuth mAuth;

    DatabaseReference myPost;
    ArrayList<Post> list;
    MyAdapter adapter;

    public MyPostFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MyPostView = inflater.inflate(R.layout.fragment_my_post, container, false);

        recyclerView = (RecyclerView) MyPostView.findViewById(R.id.myPostRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();

        HomeData = FirebaseDatabase.getInstance().getReference().child("post");

        list = new ArrayList<Post>();

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //Kết nối tới node có tên là Post
        myPost = FirebaseDatabase.getInstance().getReference().child("post");
        myPost.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //khi trên database sửa đổi, cần xóa list cũ để không hiện thị lặp dữ liệu cũ
                list.clear();
                //vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data: dataSnapshot.getChildren()){
                    Post p = data.getValue(Post.class);
                    if (p.getUid().equals(firebaseUser.getUid())){
                        list.add(p);
                    }
                }
                adapter = new MyAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: "+databaseError,Toast.LENGTH_SHORT).show();
            }
        });

        return MyPostView;
    }
}

package com.example.solutions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFragment extends Fragment {

    private View HomeView;
    private RecyclerView recyclerView;

    private DatabaseReference HomeData;
    private FirebaseAuth mAuth;

    DatabaseReference myPost;
    ArrayList<Post> list;
    MyAdapter adapter;

    ImageButton btnSearch;
    EditText edtSearch;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        HomeView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = (RecyclerView) HomeView.findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAuth = FirebaseAuth.getInstance();

        HomeData = FirebaseDatabase.getInstance().getReference().child("post");

        list = new ArrayList<Post>();

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
                    list.add(p);
                }
                adapter = new MyAdapter(getContext(), list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Error: "+databaseError,Toast.LENGTH_SHORT).show();
            }
        });

        edtSearch = HomeView.findViewById(R.id.editTextSearch);
        btnSearch = HomeView.findViewById(R.id.buttonSearch);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), Search.class);
                String strSearch = edtSearch.getText().toString();
                i.putExtra("search", strSearch);
                startActivity(i);
            }
        });

        return HomeView;
    }
}

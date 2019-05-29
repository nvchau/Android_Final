package com.example.solutions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Search extends AppCompatActivity {

    private EditText edtSearch;
    private ImageView btnSearch;
    private RecyclerView recyclerView;

    DatabaseReference myPost;
    ArrayList<Post> list;
    MyAdapter adapter;

    private DatabaseReference SearchData;
    private FirebaseAuth mAuth;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        recyclerView = (RecyclerView) findViewById(R.id.myRecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));

        mAuth = FirebaseAuth.getInstance();
        SearchData = FirebaseDatabase.getInstance().getReference().child("post");
        list = new ArrayList<Post>();

        //lấy giữ liệu từ home_fragment
        Intent intent = getIntent();
        String search = intent.getStringExtra("search");

        edtSearch = findViewById(R.id.editTextSearch);
        btnSearch = findViewById(R.id.buttonSearch);
        edtSearch.setText(search);

        String searchText = edtSearch.getText().toString();

        Toast.makeText(Search.this, "Started Search: "+searchText, Toast.LENGTH_LONG).show();

        final Query query = SearchData.orderByChild("keyword").startAt(searchText).endAt(searchText + "\uf8ff");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //khi trên database sửa đổi, cần xóa list cũ để không hiện thị lặp dữ liệu cũ
                list.clear();
                //vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Post p = data.getValue(Post.class);
                    list.add(p);
                }
                adapter = new MyAdapter(getBaseContext(), list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String searchText = edtSearch.getText().toString();

                Toast.makeText(Search.this, "Started Search: "+searchText, Toast.LENGTH_LONG).show();

                Query query = SearchData.orderByChild("keyword").startAt(searchText).endAt(searchText + "\uf8ff");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //khi trên database sửa đổi, cần xóa list cũ để không hiện thị lặp dữ liệu cũ
                        list.clear();
                        //vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                        for (DataSnapshot data : dataSnapshot.getChildren()) {
                            Post p = data.getValue(Post.class);
                            list.add(p);
                        }
                        adapter = new MyAdapter(getBaseContext(), list);
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }

}

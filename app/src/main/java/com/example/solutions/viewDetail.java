package com.example.solutions;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class viewDetail extends AppCompatActivity {
    private static final String TAG = "viewDetail";

    TextView username, title, content, keyword, email, dateTime;
    ImageView profilePic;
    EditText answer;
    Button addAnswer, btnFollow, btnDelete, btnUnFollow;
    String postID, userID;

    DatabaseReference databaseReference;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference3;
    DatabaseReference databaseReference4;
    DatabaseReference databaseReference5;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    //get comment
    private RecyclerView recyclerView;
    ArrayList<Comment> commentList;
    CommentAdapter commentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_detail);

        //BÀI POST CHI TIẾT
        title = (TextView) findViewById(R.id.txtTitle);
        username = (TextView) findViewById(R.id.txtUserName);
        keyword = (TextView) findViewById(R.id.txtKeyWord);
        content = (TextView) findViewById(R.id.txtContent);
        email = (TextView) findViewById(R.id.txtEmail);
        dateTime = (TextView) findViewById(R.id.txtDateTime);
        profilePic = (ImageView) findViewById(R.id.profilePic);
        answer = (EditText) findViewById(R.id.editTextAnswer);
        addAnswer = (Button) findViewById(R.id.ButtonAddAnswer);
        btnFollow = (Button) findViewById(R.id.btnFollowPost);
        btnDelete = (Button) findViewById(R.id.btnDeletePost);
        btnUnFollow = (Button) findViewById(R.id.btnUnFollowPost);
        //KẾT THÚC BÀI POST CHI TIẾT

        //LẤY DỮ LIỆU TỪ MyAdapter TRUYỀN QUA (dữ liệu bài post chi tiết)
        //Intent in = getIntent();
        //Bundle b  = in.getExtras();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            final ArrayList<String> array = (ArrayList<String>) bundle.getStringArrayList("dataPost");
            Log.i("List", "Passed Array List : " + array);
            assert array != null;
            username.setText(array.get(0));
            title.setText(array.get(1));
            keyword.setText(array.get(2));
            email.setText(array.get(3));
            content.setText(array.get(4));
            Picasso.with(getBaseContext()).load(array.get(5)).into(profilePic);
            //lấy id người đăng bài
            userID = array.get(6);
            //lấy id bài post
            postID = array.get(7);
            dateTime.setText(array.get(8));

            //KIỂM TRA NẾU BÀI ĐĂNG LÀ CỦA NGƯỜI ĐĂNG NHẬP THÌ ẨN NÚT FOLLOW
            mAuth = FirebaseAuth.getInstance(); //lấy id người đang đăng nhập
            String uid = mAuth.getUid();
            if (userID.equals(uid)) {
                //ẩn button follow
                btnFollow.setVisibility(View.GONE);
                //hiện button delete
                btnDelete.setVisibility(View.VISIBLE);
                btnUnFollow.setVisibility(View.GONE);
            } else {
                btnDelete.setVisibility(View.GONE);
                btnFollow.setVisibility(View.VISIBLE);
                btnUnFollow.setVisibility(View.GONE);
                //kiểm tra xem người đang đăng nhập đã follow bài viết chưa, nếu rồi thì đổi nút follow thành unFollow
                databaseReference4 = FirebaseDatabase.getInstance().getReference().child("follow").child(uid).child(postID);
                databaseReference4.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()){
                            btnFollow.setVisibility(View.GONE);
                            btnUnFollow.setVisibility(View.VISIBLE);
                        }
                        else {
                            btnFollow.setVisibility(View.VISIBLE);
                            btnUnFollow.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getBaseContext(), "Error: " + databaseError, Toast.LENGTH_SHORT).show();
                    }
                });

            }
            //KẾT THÚC KIỂM TRA FOLLOW
        }
        //KẾT THÚC LẤY DỮ LIỆU

        //HIỂN THỊ COMMENT
        recyclerView = (RecyclerView) findViewById(R.id.commentRecycle);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        commentList = new ArrayList<Comment>();
        //Kết nối tới node có tên là comment
        databaseReference = FirebaseDatabase.getInstance().getReference().child("comment").child(postID);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //khi trên database sửa đổi, cần xóa list cũ để không hiện thị lặp dữ liệu cũ
                commentList.clear();
                //vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Comment comment = data.getValue(Comment.class);
                    commentList.add(comment);
                }
                commentAdapter = new CommentAdapter(getBaseContext(), commentList);
                recyclerView.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getBaseContext(), "Error: " + databaseError, Toast.LENGTH_SHORT).show();
            }
        });
        //KẾT THÚC HIỂN THỊ COMMENT

        //THÊM COMMENT
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        //lấy id của bài post
//        final String postID = databaseReference.child("post").getKey();
        //lấy thời gian hiên tại
        final Date currentTime = Calendar.getInstance().getTime();

        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiểm tra nếu cmt rỗng thì báo lỗi
                if (TextUtils.isEmpty(answer.getText())) {
                    Toast.makeText(viewDetail.this, "Hãy nhập nội dung bình luận!", Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("uid", firebaseUser.getUid());
                    result.put("email", firebaseUser.getEmail());
                    result.put("answer", answer.getText().toString());
                    result.put("dateTime", currentTime.toString());
                    if (firebaseUser.getPhotoUrl() == null) {
                        result.put("profilePic", "https://cdn3.iconfinder.com/data/icons/seo-internet-marketing-flat-icons/250/web-code.png");
                    } else {
                        result.put("profilePic", firebaseUser.getPhotoUrl().toString());
                    }
                    if (firebaseUser.getDisplayName() == "" || firebaseUser.getDisplayName() == null) {
                        result.put("username", firebaseUser.getEmail());
                    } else {
                        result.put("username", firebaseUser.getDisplayName());
                    }

                    databaseReference.child("comment").child(postID).push().setValue(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(viewDetail.this, "Thêm bình luận thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Error", "onFailure: " + e.getMessage());
                            Toast.makeText(viewDetail.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    //làm mới khung nhập câu trả lời
                    answer.setText("");
                }
            }
        });
        //KẾT THÚC THÊM COMMENT

        //CHỨC NĂNG FOLLOW
        btnFollow.setOnClickListener(new View.OnClickListener() {
            String username, title, keyword, email, content, profilePic, userID, postID, dateTime;
            @Override
            public void onClick(View v) {
                //lấy id người đang đăng nhập
                mAuth = FirebaseAuth.getInstance();
                String uid = mAuth.getUid();
                //lấy dữ liệu từ MyAdapter truyển qua (dữ liệu bài post chi tiết)
                Bundle bundle = getIntent().getExtras();
                if(bundle != null){
                    final ArrayList<String> array = (ArrayList<String>) bundle.getStringArrayList("dataPost");
                    Log.i("List", "Passed Array List : " + array);
                    assert array != null;
                    username   = array.get(0);
                    title      = array.get(1);
                    keyword    = array.get(2);
                    email      = array.get(3);
                    content    = array.get(4);
                    profilePic = array.get(5);
                    userID     = array.get(6);
                    postID     = array.get(7);
                    dateTime   = array.get(8);

                    //ghi dữ liệu lên nood follow
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("uid", userID);
                    result.put("email", email);
                    result.put("title", title);
                    result.put("keyword", keyword);
                    result.put("content", content);
                    result.put("profilePic", profilePic);
                    result.put("username", username);
                    result.put("id", postID);
                    result.put("dateTime", dateTime);

                    databaseReference.child("follow").child(uid).child(postID).setValue(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(viewDetail.this, "Đã theo dõi bài viết này", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Error", "onFailure: " + e.getMessage());
                            Toast.makeText(viewDetail.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });
        //KẾT THÚC FOLLOW

        //XÓA BÀI VIẾT (ĐỐI VỚI CHỦ BÀI VIẾT)
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });

        //HỦY FOLLOW
        btnUnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth = FirebaseAuth.getInstance(); //lấy id người đang đăng nhập
                String uid = mAuth.getUid();
                databaseReference5 = FirebaseDatabase.getInstance().getReference().child("follow").child(uid).child(postID);
                databaseReference5.removeValue();
                Toast.makeText(viewDetail.this, "Đã hủy theo dõi bài viết này", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //hàm hiển thị dialog xóa bài viết
    public void showAlertDialog() {
        mAuth = FirebaseAuth.getInstance(); //lấy id người đang đăng nhập
        String uid = mAuth.getUid();
        //truy vấn database bài post
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("post").child(postID);
        //truy vấn database của comment
        databaseReference = FirebaseDatabase.getInstance().getReference().child("comment").child(postID);
        //truy vấn database của follow
        databaseReference3 = FirebaseDatabase.getInstance().getReference();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("XÓA BÀI VIẾT!");
        builder.setMessage("Bạn có muốn xóa bài viết này không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(viewDetail.this, "...", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    //xóa bài viết trong bảng post
                    databaseReference2.removeValue();
                    //xóa tất cả comment của bài viết đó
                    databaseReference.removeValue();
                    //XÓA FOLLOW NẾU BÀI VIẾT ĐANG ĐƯỢC FOLLOW
                    //truy vấn lồng: follow => userID => postID
                    databaseReference3.child("follow").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //truy vấn level 1: duyệt qua tất cả userID
                            for (DataSnapshot child: dataSnapshot.getChildren()){
                                String key = child.getKey();

                                final DatabaseReference dataRef3 = databaseReference3.child("follow").child(key);
                                dataRef3.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        //truy vấn level 2: ở mỗi userID sẽ tiếp tục duyệt tất cả postID có trong đó
                                        for (DataSnapshot child : dataSnapshot.getChildren()) {
                                            String key2 = child.getKey();
                                            //Toast.makeText(viewDetail.this, "Key: "+key, Toast.LENGTH_SHORT).show();
                                            if (postID.equals(key2)){
                                                dataRef3.child(key2).removeValue();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                        private void fun(String key) {

                        }
                    });
                    //kết thúc truy vấn lồng
                    Toast.makeText(viewDetail.this, "Đã xóa bài viết!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(viewDetail.this, MainActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    Toast.makeText(viewDetail.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

}

package com.example.solutions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Calendar;

public class addQuestion extends AppCompatActivity {

    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    EditText title, keyword, content;
    String username, email, avatar;
    Button addQuestion, cancel, reset;

    public int starCount = 0;
    public Map<String, Boolean> stars = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_question);

        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        title    = (EditText) findViewById(R.id.editTextTitle);
        keyword  = (EditText) findViewById(R.id.editTextKeyWord);
        content  = (EditText) findViewById(R.id.editTextContent);

        addQuestion = findViewById(R.id.buttonAddQuestion);
        cancel      = findViewById(R.id.buttonCancel);
        reset       = findViewById(R.id.buttonReset);
        //lấy thời gian hiên tại
        final Date currentTime = Calendar.getInstance().getTime();

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //kiểm tra không được để trống
                if (TextUtils.isEmpty(title.getText()) || TextUtils.isEmpty(keyword.getText()) || TextUtils.isEmpty(content.getText())) {
                    Toast.makeText(addQuestion.this, "Không được để trống nội dung!", Toast.LENGTH_SHORT).show();
                }else {
                    String pid = databaseReference.push().getKey();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("uid", firebaseUser.getUid());
                    result.put("email", firebaseUser.getEmail());
                    result.put("title", title.getText().toString());
                    result.put("keyword", keyword.getText().toString());
                    result.put("content", content.getText().toString());
                    if (firebaseUser.getPhotoUrl() == null){
                        result.put("profilePic", "https://cdn3.iconfinder.com/data/icons/seo-internet-marketing-flat-icons/250/web-code.png");
                    }else {
                        result.put("profilePic", firebaseUser.getPhotoUrl().toString());
                    }
                    if (firebaseUser.getDisplayName() == "" || firebaseUser.getDisplayName() == null){
                        result.put("username", firebaseUser.getEmail());
                    }else {
                        result.put("username", firebaseUser.getDisplayName());
                    }
                    result.put("dateTime", currentTime.toString());
                    result.put("id", pid);
                    Log.d("Mess", "onClick: "+result.toString());

                    databaseReference.child("post").child(pid).setValue(result).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(addQuestion.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(addQuestion.this, MainActivity.class));
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Error", "onFailure: "+e.getMessage());
                            Toast.makeText(addQuestion.this, "Thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(addQuestion.this, MainActivity.class);
                startActivity(intent);
                Toast.makeText(addQuestion.this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        });

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("");
                keyword.setText("");
                content.setText("");
            }
        });

    }

}

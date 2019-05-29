package com.example.solutions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    EditText edtEmail;
    EditText edtPassword;
    EditText edtComfirmPassword;
    TextView txtlogin;
    Button btnDangKy;

    String email, password, comfirmPassword;

    //khai báo firebase Auth
    private FirebaseAuth mAuthencation;
    //dùng để kiểm tra đăng nhập
    private FirebaseAuth.AuthStateListener mAuthListenner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        //Initialize Firebase Auth
        mAuthencation = FirebaseAuth.getInstance();

        //kiểm tra đã đăng nhập hay chưa
        mAuthListenner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {// nếu người dùng đã đăng nhập thì chuyển thằng vô trang chính
                    startActivity(new Intent(Register.this, MainActivity.class));
                }
            }
        };

        edtEmail           = (EditText) findViewById(R.id.editTexEmail);
        edtPassword        = (EditText) findViewById(R.id.editTextPassword);
        edtComfirmPassword = (EditText) findViewById(R.id.editTextComfirmPassword);
        btnDangKy          = (Button) findViewById(R.id.buttonDangKy);
        txtlogin           = (TextView)findViewById(R.id.textViewBackLogin);

        btnDangKy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                email = edtEmail.getText().toString();
                password = edtPassword.getText().toString();
                comfirmPassword = edtComfirmPassword.getText().toString();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(comfirmPassword)) {
                    if (TextUtils.isEmpty(edtEmail.getText())){
                        Toast.makeText(Register.this, "Email trống!", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(edtPassword.getText())){
                        Toast.makeText(Register.this, "Mật khẩu trống!", Toast.LENGTH_SHORT).show();
                    }
                    else if (TextUtils.isEmpty(edtComfirmPassword.getText())){
                        Toast.makeText(Register.this, "Vui lòng xác nhận lại mật khẩu!", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if (!comfirmPassword.equals(password))
                    {
                        Toast.makeText(Register.this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DangKy();
                    }
                }
            }
        });

        txtlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }

    //kiểm tra đăng nhập
//    @Override
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        mAuthencation.addAuthStateListener(mAuthListenner);
//    }

    //hàm đăng ký
    private void DangKy(){
        String email    = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        mAuthencation.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(Register.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(Register.this, "Lỗi!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

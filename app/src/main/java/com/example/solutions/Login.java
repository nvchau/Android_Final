package com.example.solutions;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class Login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, View.OnClickListener, FirebaseAuth.AuthStateListener{

    EditText edtEmail, edtPassword;
    TextView txtDangKy;
    Button btnDangNhap, btnLoginGoogle;

    ProgressBar progressBar;

    GoogleApiClient apiClient;
    public int REQUEST_LOGIN_GOOGLE_CODE = 99;
    public int VALIDATE_LOGIN_CODE = 0;
    SharedPreferences sharedPreferences;

    //khai báo firebase Auth
    private FirebaseAuth mAuthencation;
    //dùng để kiểm tra đăng nhập
    private FirebaseAuth.AuthStateListener mAuthListenner;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        progressBar = (ProgressBar) findViewById(R.id.progress_circular);

        //Initialize Firebase Auth
        mAuthencation = FirebaseAuth.getInstance();

        edtEmail    = (EditText) findViewById(R.id.editTexEmailLogin);
        edtPassword = (EditText) findViewById(R.id.editTextPasswordLogin);
        btnDangNhap = (Button) findViewById(R.id.buttonDangNhap);
        txtDangKy   = (TextView)findViewById(R.id.textViewDangKy);
        btnLoginGoogle = (Button) findViewById(R.id.buttonLoginGoogle);

        //set onClick
        btnDangNhap.setOnClickListener(this);
        txtDangKy.setOnClickListener(this);
        btnLoginGoogle.setOnClickListener(this);

        //kiểm tra đã đăng nhập hay chưa
//        mAuthListenner = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() != null) {// nếu người dùng đã đăng nhập thì chuyển thằng vô trang chính
//                    startActivity(new Intent(Login.this, MainActivity.class));
//                }
//            }
//        };

        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DangNhap();
            }
        });

        txtDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);
            }
        });

        btnLoginGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);//hiệu ứng load
                LoginGoogle(apiClient);
            }
        });

        sharedPreferences = getSharedPreferences("Save Login", MODE_PRIVATE);

        CreateClientLoginGoogle();
    }

    //đăng nhập bằng tài khoản đăng ký
    private void  DangNhap(){
        String email    = edtEmail.getText().toString();
        String password = edtPassword.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            if (TextUtils.isEmpty(email)){
                Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            }
            if (TextUtils.isEmpty(password)){
                Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_SHORT).show();
            }
        }else {
            progressBar.setVisibility(View.VISIBLE);//hiệu ứng load
            mAuthencation.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Login.this, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    //Khởi tạo client đăng nhập google
    private void CreateClientLoginGoogle() {
        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        apiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
                .build();
    }
    @Override
    protected void onStart() {
        super.onStart();
        mAuthencation.addAuthStateListener(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuthencation.removeAuthStateListener(this);
    }

    //Sau khi khởi tạo sẽ xuất hiện form để đăng nhập
    private void LoginGoogle(GoogleApiClient apiClient) {
        //1 : Login Với Google
        VALIDATE_LOGIN_CODE = 1;
        Intent iLoginGoogle = Auth.GoogleSignInApi.getSignInIntent(apiClient);
        startActivityForResult(iLoginGoogle, REQUEST_LOGIN_GOOGLE_CODE);
    }

    //Get token từ Google và chứng thực để đăng nhập trên firebase
    private void ValidateFireBaseToken(String TokenID) {
        if(VALIDATE_LOGIN_CODE == 1) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(TokenID, null);
            mAuthencation.signInWithCredential(authCredential);
        }else if(VALIDATE_LOGIN_CODE == 2){
            AuthCredential authCredential = FacebookAuthProvider.getCredential(TokenID);
            mAuthencation.signInWithCredential(authCredential);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_LOGIN_GOOGLE_CODE) {
            if(resultCode == RESULT_OK) {
                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                GoogleSignInAccount account = signInResult.getSignInAccount();
                String tokenID = account.getIdToken();
                ValidateFireBaseToken(tokenID);
            }
        }else{

        }
    }

    @Override
    public void onClick(View v) {
//        int id = v.getId();
//        switch (id) {
//            case R.id.btnLoginGoogle:
//                LoginGoogle(apiClient);
//                break;
////            case R.id.btnLoginFacebook:
////                LoginFacebook();
////                break;
//            case R.id.btnDangNhap:
//                DangNhap();
//                break;
//            case R.id.txtDangKy:
//                Intent iSignUp = new Intent(Login.this, Register.class);
//                startActivity(iSignUp);
//                break;
//        }
    }

    //Kiểm tra người dùng đăng nhập thành công hay đăng xuất
    @Override
    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("IDUser", user.getUid());
            editor.commit();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

package com.example.solutions;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebViewFragment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    DatabaseReference myPost;
//    RecyclerView recyclerView;
//    ArrayList<Post> list;
//    MyAdapter adapter;

    //khai báo firebase Auth
    private FirebaseAuth mAuthencation;
    //dùng để kiểm tra đăng nhập
    private FirebaseAuth.AuthStateListener mAuthListenner;

    //Toolbar (menubar)
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar (menubar)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //lắng nghe sự kiện click item of menu, khi chưa có item được chọn. thì mặc định là home fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

        //Initialize Firebase Auth
        mAuthencation = FirebaseAuth.getInstance();

        //kiểm tra đã đăng nhập hay chưa
        mAuthListenner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {// nếu người dùng chưa đăng nhập thì chuyển vô trang login
                    startActivity(new Intent(MainActivity.this, Login.class));
                }
            }
        };

        //gọi hàm header để set dữ liệu cho header
        header();

        TextView Add = (TextView) findViewById(R.id.buttonAdd);
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, addQuestion.class);
                startActivity(intent);
            }
        });

    }

    //lắng nghe sự kiện click item of menu bar (navigation drawer)
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
            Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_my_post) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MyPostFragment()).commit();
            Toast.makeText(this, "My post", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.nav_following){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FollowingFragment()).commit();
            Toast.makeText(this, "Post are following", Toast.LENGTH_SHORT).show();
        } else if(id == R.id.nav_contact){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ContactFragment()).commit();
            Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT).show();
        }else if(id == R.id.nav_website){
            Toast.makeText(this, "Our website", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout){
            //đăng xuất
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(MainActivity.this, "Đã đăng xuất!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, Login.class);
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }

    //Toolbar (menubar)
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }else {
            super.onBackPressed();
        }

    }

    //kiểm tra đăng nhập
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        mAuthencation.addAuthStateListener(mAuthListenner);
    }

    //hàm set dữ liệu cho header
    public void header(){
        FirebaseUser user = mAuthencation.getCurrentUser();
        //nếu người dùng đang đăng nhật thì lấy thông tin
        if (user != null){
            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            View header = navigationView.getHeaderView(0);
            TextView name = header.findViewById(R.id.textViewUserName);
            TextView email = header.findViewById(R.id.textViewEmail);
            ImageView avt= header.findViewById(R.id.imageViewProfileAvt);

            FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

            Picasso.with(this).load(firebaseUser.getPhotoUrl()).into(avt);
            name.setText(firebaseUser.getDisplayName());
            email.setText(firebaseUser.getEmail());
        }

    }
}
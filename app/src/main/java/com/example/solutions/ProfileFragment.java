package com.example.solutions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

public class ProfileFragment extends Fragment {

    private View ProfileView;

    private FirebaseAuth mAuthencation;
    //dùng để kiểm tra đăng nhập
    private FirebaseAuth.AuthStateListener mAuthListenner;

    private FirebaseUser firebaseUser;
    TextView name, email;
    ImageView avt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ProfileView = inflater.inflate(R.layout.fragment_profile, container, false);

        name  = ProfileView.findViewById(R.id.textViewUserName);
        email = ProfileView.findViewById(R.id.textViewEmail);
        avt   = ProfileView.findViewById(R.id.imageViewProfileAvt);

//        @SuppressLint("RestrictedApi")
//        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
//        if (acct != null) {
//            String personName = acct.getDisplayName();//tên đầy đủ
//            String personGivenName = acct.getGivenName();//họ (first name)
//            String personFamilyName = acct.getFamilyName();//tên (last name)
//            String personEmail = acct.getEmail();
//            String personId = acct.getId();
//            Uri personPhoto = acct.getPhotoUrl();
//
//            name.setText(personName);
//            email.setText(personEmail);
//            Picasso.with(getContext()).load(personPhoto).into(avt);
//        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //nếu người dùng đang đăng nhật thì lấy thông tin
        if (user != null){
            Picasso.with(getContext()).load(user.getPhotoUrl()).into(avt);
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
        }

        return ProfileView;
    }

}

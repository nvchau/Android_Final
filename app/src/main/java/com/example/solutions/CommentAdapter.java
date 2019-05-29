package com.example.solutions;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {

    Context context;
    ArrayList<Comment> commentArrayList;

    public CommentAdapter(Context c, ArrayList<Comment> cmt){
        context = c;
        commentArrayList = cmt;
    }
    //gửi dữ liệu qua fragment home
    private ArrayList<Comment> commentList;
    public CommentAdapter(ArrayList<Comment> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.answer.setText(commentArrayList.get(position).getAnswer());
        holder.email.setText(commentArrayList.get(position).getEmail());
//        holder.uid.setText(commentArrayList.get(position).getUid());
        holder.username.setText(commentArrayList.get(position).getUsername());
        holder.dateTime.setText(commentArrayList.get(position).getDateTime());
        //load ảnh bằng thư viện Picasso
        Picasso.with(context).load(commentArrayList.get(position).getProfilePic()).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return commentArrayList.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder {

        TextView answer, email, uid, username, dateTime;
        ImageView profilePic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            answer     = (TextView) itemView.findViewById(R.id.txtAnswer);
            email      = (TextView) itemView.findViewById(R.id.txtEmail);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            username   = (TextView) itemView.findViewById(R.id.txtUserName);
            dateTime   = (TextView) itemView.findViewById(R.id.txtDateTime);
        }
    }
}

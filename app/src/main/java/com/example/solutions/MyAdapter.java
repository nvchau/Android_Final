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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Post> postArrayList;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;

    public MyAdapter(Context c, ArrayList<Post> p){
        context = c;
        postArrayList = p;
    }
    //gửi dữ liệu qua fragment home
    private ArrayList<Post> list;
    public MyAdapter(ArrayList<Post> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.post, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(postArrayList.get(position).getUsername());
        holder.title.setText(postArrayList.get(position).getTitle());
        holder.keyword.setText(postArrayList.get(position).getKeyword());
        holder.email.setText(postArrayList.get(position).getEmail());
        //kiểm tra nếu độ dài content lớn hơn 400 thì chỉ trả về 40 ký tự
        if (postArrayList.get(position).getContent().length() > 50){
            holder.content.setText(postArrayList.get(position).getContent().substring(0,50)+"...");
        }else{
            holder.content.setText(postArrayList.get(position).getContent());
        }
        //load ảnh bằng thư viện Picasso
        Picasso.with(context).load(postArrayList.get(position).getProfilePic()).into(holder.profilePic);
        // nút Follow
        //holder.btnFollow.setVisibility(View.VISIBLE);
        //holder.onClick(position);//set conClick cho từng recycleView, lấy position (tương tự như id hay số thứ tự)
    }

    @Override
    public int getItemCount() {
        return postArrayList.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder {

        TextView username, title, content, keyword, email;
        ImageView profilePic;
        Button btnFollow;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username   = (TextView) itemView.findViewById(R.id.txtUserName);
            title      = (TextView) itemView.findViewById(R.id.txtTitle);
            keyword    = (TextView) itemView.findViewById(R.id.txtKeyWord);
            content    = (TextView) itemView.findViewById(R.id.txtContent);
            email      = (TextView) itemView.findViewById(R.id.txtEmail);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);

//            btnFollow    = (Button) itemView.findViewById(R.id.btnFollow);

            //click mỗi phần tử của recycleView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    final ArrayList<String> listPost = new ArrayList<>();
                    listPost.add(postArrayList.get(position).getUsername());
                    listPost.add(postArrayList.get(position).getTitle());
                    listPost.add(postArrayList.get(position).getKeyword());
                    listPost.add(postArrayList.get(position).getEmail());
                    listPost.add(postArrayList.get(position).getContent());
                    listPost.add(postArrayList.get(position).getProfilePic());
                    listPost.add(postArrayList.get(position).getUid());
                    listPost.add(postArrayList.get(position).getId());
                    listPost.add(postArrayList.get(position).getDateTime());

//                    Toast.makeText(context, listPost+"", Toast.LENGTH_SHORT).show();

                    Context context = v.getContext();
                    Intent intent = new Intent(context, viewDetail.class);
                    //đẩy dữ liệu qua view details
                    intent.putExtra("dataPost", listPost);
                    context.startActivity(intent);
//                    Post p = new Post();
//                    Toast.makeText(context, getAdapterPosition()+": "+p.getTitle(), Toast.LENGTH_SHORT).show();
//                    Context context = v.getContext();
//                    Intent intent = new Intent(context, viewDetail.class);
//                    context.startActivity(intent);
                }
            });
        }

        //onClick khi nhấp vào từng post cụ thể, hoặc nút
//        public void onClick(final int position){
//            btnFollow.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(context, "Chức năng này đang phát triển "+position, Toast.LENGTH_SHORT).show();
////                    Context context = v.getContext();
////                    Intent intent = new Intent(context, viewDetail.class);
////                    context.startActivity(intent);
//                }
//            });
//        }

    }
}

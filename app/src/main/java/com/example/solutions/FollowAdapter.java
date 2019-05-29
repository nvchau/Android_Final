package com.example.solutions;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class FollowAdapter extends RecyclerView.Adapter<FollowAdapter.MyViewHolder> {

    Context context;
    ArrayList<Follow> followArrayList;

    public FollowAdapter(Context c, ArrayList<Follow> follow){
        context = c;
        followArrayList = follow;
    }
    //gửi dữ liệu qua fragment home
    private ArrayList<Follow> followList;
    public FollowAdapter(ArrayList<Follow> followList) {
        this.followList = followList;
    }

    @NonNull
    @Override
    public FollowAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.post, parent, false));
    }

    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.email.setText(followArrayList.get(position).getEmail());
        holder.username.setText(followArrayList.get(position).getUsername());
        holder.title.setText(followArrayList.get(position).getTitle());
        holder.keyword.setText(followArrayList.get(position).getKeyword());
        if (followArrayList.get(position).getContent().length() > 50){
            holder.content.setText(followArrayList.get(position).getContent().substring(0,50)+"...");
        }else{
            holder.content.setText(followArrayList.get(position).getContent());
        }
        //load ảnh bằng thư viện Picasso
        Picasso.with(context).load(followArrayList.get(position).getProfilePic()).into(holder.profilePic);
    }

    @Override
    public int getItemCount() {
        return followArrayList.size();
    }

    class  MyViewHolder extends RecyclerView.ViewHolder {

        TextView username, title, content, keyword, email;
        ImageView profilePic;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            email      = (TextView) itemView.findViewById(R.id.txtEmail);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            username   = (TextView) itemView.findViewById(R.id.txtUserName);
            title      = (TextView) itemView.findViewById(R.id.txtTitle);
            keyword    = (TextView) itemView.findViewById(R.id.txtKeyWord);
            content    = (TextView) itemView.findViewById(R.id.txtContent);

            //click mỗi phần tử của recycleView
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();

                    final ArrayList<String> listPost = new ArrayList<>();
                    listPost.add(followArrayList.get(position).getUsername());
                    listPost.add(followArrayList.get(position).getTitle());
                    listPost.add(followArrayList.get(position).getKeyword());
                    listPost.add(followArrayList.get(position).getEmail());
                    listPost.add(followArrayList.get(position).getContent());
                    listPost.add(followArrayList.get(position).getProfilePic());
                    listPost.add(followArrayList.get(position).getUid());
                    listPost.add(followArrayList.get(position).getId());
                    listPost.add(followArrayList.get(position).getDateTime());

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
    }
}

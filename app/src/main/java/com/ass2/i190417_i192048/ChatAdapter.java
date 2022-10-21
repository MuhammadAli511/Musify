package com.ass2.i190417_i192048;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    List<ChatModal> ls;
    Context context;

    public ChatAdapter(List<ChatModal> ls, Context context){
        this.ls = ls;
        this.context = context;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent,int ViewType){
        View row = LayoutInflater.from(context).inflate(R.layout.row,parent,false);
        return new MyViewHolder(row);
    }

    public void onBindViewHolder(MyViewHolder myViewHolder,int position){
        myViewHolder.name.setText(ls.get(position).getPerson_name());
        myViewHolder.number.setText(ls.get(position).getPerson_number());
        myViewHolder.image.setImageResource(R.drawable.profile);
    }
    public int getItemCount() {
        return ls.size();
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name,number;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name);
            number = itemView.findViewById(R.id.user_num);
            image = itemView.findViewById(R.id.user_image);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((Activity)context).startActivity(new Intent(context,MessagesScreen.class));
                }
            });
        }
    }
}

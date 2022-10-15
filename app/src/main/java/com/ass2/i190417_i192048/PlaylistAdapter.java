package com.ass2.i190417_i192048;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder> {
    List<Playlists> dynamicList;
    Context contextVar;

    public PlaylistAdapter(List<Playlists> dynamicList, Context contextVar) {
        this.dynamicList = dynamicList;
        this.contextVar = contextVar;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(contextVar).inflate(R.layout.playlist_row,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.playListName.setText(dynamicList.get(position).getPlaylistName());
    }

    @Override
    public int getItemCount() {
        return dynamicList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView playListName;
        ImageView playListImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            playListName = itemView.findViewById(R.id.playListName);
            playListImage = itemView.findViewById(R.id.playListImage);
        }
    }

}
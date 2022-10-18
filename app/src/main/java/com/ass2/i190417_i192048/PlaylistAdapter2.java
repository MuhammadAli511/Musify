package com.ass2.i190417_i192048;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class PlaylistAdapter2 extends RecyclerView.Adapter<PlaylistAdapter2.ViewHolder> {
    Context mContext;
    List<Playlists> playlistsList;

    public PlaylistAdapter2(Context mContext, List<Playlists> playlistsList) {
        this.mContext = mContext;
        this.playlistsList = playlistsList;
    }

    @NonNull
    @Override
    public PlaylistAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter2.ViewHolder holder, int position) {
        String playListName = playlistsList.get(position).getPlaylistName();
        holder.playlistName.setText(playListName);
        Glide.with(mContext).load(playlistsList.get(position).getImageURL()).into(holder.playlistImage);

        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent oldIntent = ((SelectPlaylist) mContext).getIntent();
                String title = oldIntent.getStringExtra("title");
                Intent intent = new Intent(mContext, MusicAdded.class);
                intent.putExtra("playlistName", playListName);
                intent.putExtra("title", title);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return playlistsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView playlistName;
        public ImageView playlistImage;
        public LinearLayout parentLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            playlistName = (TextView) itemView.findViewById(R.id.playListName);
            playlistImage = (ImageView) itemView.findViewById(R.id.playListImage);
            parentLayout = (LinearLayout) itemView.findViewById(R.id.parentLayout);
        }
    }
}

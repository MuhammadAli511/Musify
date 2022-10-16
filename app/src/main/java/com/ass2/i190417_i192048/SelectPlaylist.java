package com.ass2.i190417_i192048;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class SelectPlaylist extends AppCompatActivity {
    RecyclerView recyclerView;
    ImageView addPlaylistButton;
    List<Playlists> dynamicList = new ArrayList<>();
    PlaylistAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_playlist);

        recyclerView = findViewById(R.id.recyclerView);
        addPlaylistButton = findViewById(R.id.addPlaylistButton);

        adapter = new PlaylistAdapter(dynamicList, SelectPlaylist.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(SelectPlaylist.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        addPlaylistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectPlaylist.this, AddPlaylist.class);
                startActivity(intent);
            }
        });

    }

    public List<Playlists> getData(){
        List <Playlists> list = new ArrayList<>();

    }

}
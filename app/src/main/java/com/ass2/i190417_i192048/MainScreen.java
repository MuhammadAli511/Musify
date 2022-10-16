package com.ass2.i190417_i192048;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainScreen extends AppCompatActivity {
    LinearLayout liked, add, search, listenlater;
    RecyclerView playlistRecyclerViewMain;
    FirebaseFirestore db;
    List<Playlists> playlistsList;
    PlaylistAdapter playlistAdapter;

    RecyclerView musicRecyclerViewMain;
    List<Music> musicList;
    MusicAdapter musicAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        // Variables
        liked = findViewById(R.id.liked);
        add = findViewById(R.id.add);
        search = findViewById(R.id.search);
        listenlater = findViewById(R.id.listenlater);
        db = FirebaseFirestore.getInstance();

        playlistRecyclerViewMain = findViewById(R.id.playlistRecyclerViewMain);
        playlistsList = new ArrayList<>();

        musicRecyclerViewMain = findViewById(R.id.musicRecyclerViewMain);
        musicList = new ArrayList<>();




        // Layouts
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        playlistRecyclerViewMain.setLayoutManager(layoutManager);
        getData();


        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        musicRecyclerViewMain.setLayoutManager(layoutManager2);
        getData2();





        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, UploadMusic.class));
            }
        });

    }

    public void getData(){
        db.collection("playlists").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Playlists playlists = new Playlists(document.getString("playlistName"), document.getString("imageURL"));
                        playlistsList.add(playlists);
                    }
                    playlistAdapter = new PlaylistAdapter(MainScreen.this, playlistsList);
                    playlistRecyclerViewMain.setAdapter(playlistAdapter);
                    playlistAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainScreen.this, "Error getting Playlists.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void getData2(){

        db.collection("Music").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Music music = new Music(document.getString("title"), document.getString("imageURL"));
                        musicList.add(music);
                    }
                    musicAdapter = new MusicAdapter(MainScreen.this, musicList);
                    musicRecyclerViewMain.setAdapter(musicAdapter);
                    musicAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainScreen.this, "Error getting Music.", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }










}


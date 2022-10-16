package com.ass2.i190417_i192048;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class MainScreen extends AppCompatActivity  {
    LinearLayout liked, add, search, listenlater;
    SeekBar seekBar;
    ImageView play_button;
    MediaPlayer mediaPlayer;
    //"gs://i190417-i192048.appspot.com/audio/sample-3s.mp3"
    String audioUrl;
    //String audioUrl = "https://firebasestorage.googleapis.com/v0/b/i190417-i192048.appspot.com/o/audio%2FAstro?alt=media&token=d11a4997-017b-4d0d-8d7e-40ed28c76a57";
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebasestorage;
    //String storageLocation =

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        seekBar = findViewById(R.id.seek_bar);
        play_button = findViewById(R.id.to_play_16);
        liked = findViewById(R.id.liked);
        add = findViewById(R.id.add);
        search = findViewById(R.id.search);
        listenlater = findViewById(R.id.listenlater);
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();


        mediaPlayer = new MediaPlayer();

        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StorageReference audioRef = storageReference.child("audio/Astro" );

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        audioRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                audioUrl = uri.toString();
                                System.out.println(audioUrl);
                                onPlayClick();
                                playAudio(audioUrl);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MainScreen.this,"Failure getting url",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MainScreen.this, "Fail to get audio url.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainScreen.this, UploadMusic.class));
            }
        });



    }

    private void playAudio(String audioUrl) {

        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA).build());

        if(!mediaPlayer.isPlaying()){
            try {
                //mediaPlayer.reset();
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        setSeekBar(audioUrl);
                        mediaPlayer.start();
                    }
                });
                mediaPlayer.prepareAsync();
                Toast.makeText(this, "Audio started playing..", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (mediaPlayer.isPlaying()){
            try{
                onPauseClick();
                mediaPlayer.reset();
                mediaPlayer.prepare();
                mediaPlayer.stop();
                mediaPlayer.release();

            } catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(MainScreen.this,"Audio Paused",Toast.LENGTH_SHORT).show();
        }

    }

//    public void initMediaPlayer(){
//        mediaPlayer.setOnErrorListener((MediaPlayer.OnErrorListener) this);
//    }

    @SuppressLint("ResourceAsColor")
    private void onPlayClick(){
        play_button.setBackgroundColor(R.color.black);
        play_button.setImageResource(R.drawable.pause);
    }
    @SuppressLint("ResourceAsColor")
    private void onPauseClick(){
        play_button.setBackgroundColor(R.color.black);
        play_button.setImageResource(R.drawable.playone);
    }

    public void setSeekBar(String audioUrl){

        System.out.println(mediaPlayer.getDuration());
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        Handler handler = new Handler();
        MainScreen.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int current_pos = mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(current_pos);
                    if(mediaPlayer.getDuration()/1000 == current_pos){
                        onPauseClick();
                        Toast.makeText(MainScreen.this,"Song Completed",Toast.LENGTH_SHORT).show();
                        mediaPlayer.seekTo(0);
                        seekBar.setProgress(0);
                        // this line (return) should be changed to something productive
                        // may restart activity or play the next coming music
                        return;
                    }

                }
                handler.postDelayed(this,1000);
            }
        });
    }

//    @Override
//    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
//        mediaPlayer.reset();
//        return false;
//    }
}
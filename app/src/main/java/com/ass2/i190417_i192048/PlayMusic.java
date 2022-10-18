package com.ass2.i190417_i192048;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
// glide
import com.bumptech.glide.Glide;


import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class PlayMusic extends AppCompatActivity {

    TextView musicTitle, currentTime;
    SeekBar musicSlider;
    ImageView musicImage, previousButton, pauseButton, nextButton, comment;
    List<Music> musicList;
    Music currentSong;
    MediaPlayer mediaPlayer = MusicMediaPlayer.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        Intent oldIntent = getIntent();
        musicList = (List<Music>) oldIntent.getSerializableExtra("musicList");
        Log.d("Music List Size : " , String.valueOf(musicList.size()));


        musicTitle = findViewById(R.id.musicTitle);
        currentTime = findViewById(R.id.currentTime);
        musicSlider = findViewById(R.id.musicSlider);
        musicImage = findViewById(R.id.musicImage);
        previousButton = findViewById(R.id.previousButton);
        pauseButton = findViewById(R.id.pauseButton);
        nextButton = findViewById(R.id.nextButton);
        comment = findViewById(R.id.comment);

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayMusic.this, MusicComments.class);
                intent.putExtra("title", currentSong.getTitle());
                intent.putExtra("musicList", (Serializable)musicList);
                startActivity(intent);
            }
        });

        try {
            setValues();
        } catch (IOException e) {
            e.printStackTrace();
        }
        PlayMusic.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    int mCurrentPosition = mediaPlayer.getCurrentPosition();
                    musicSlider.setProgress(mCurrentPosition);
                    currentTime.setText(convertToMMSS(mCurrentPosition +""));
                    if (mediaPlayer.isPlaying()){
                        pauseButton.setImageResource(R.drawable.pause);
                    } else{
                        pauseButton.setImageResource(R.drawable.playone);
                    }
                }
                new Handler().postDelayed(this, 100);
            }
        });

        musicSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    void setValues() throws IOException {
        currentSong = musicList.get(MusicMediaPlayer.currentIndex);
        Log.d("currentSong", currentSong.getTitle() + " " + currentSong.getDescription() + " " + currentSong.getImageURL() + " " + currentSong.getGenre() + " " + currentSong.getMusicURL());
        musicTitle.setText(currentSong.getTitle());
        Glide.with(this).load(currentSong.getImageURL()).into(musicImage);

        pauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }

            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicMediaPlayer.currentIndex == musicList.size() - 1) {
                    Toast.makeText(PlayMusic.this, "No more songs", Toast.LENGTH_SHORT).show();
                    return;
                }
                MusicMediaPlayer.currentIndex += 1;
                mediaPlayer.reset();
                try {
                    setValues();
                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        });

        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MusicMediaPlayer.currentIndex == 0) {
                    return;
                }
                MusicMediaPlayer.currentIndex -= 1;
                mediaPlayer.reset();
                try {
                    setValues();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });

        playMusic();



    }

    void playMusic() throws IOException {
        mediaPlayer.reset();
        try {
            mediaPlayer.setDataSource(currentSong.getMusicURL());
            mediaPlayer.prepare();
            mediaPlayer.start();
            musicSlider.setProgress(0);
            musicSlider.setMax(mediaPlayer.getDuration());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        String returnValue = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        return returnValue;

    }



}
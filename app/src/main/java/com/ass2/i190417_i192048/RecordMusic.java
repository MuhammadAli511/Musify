package com.ass2.i190417_i192048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RecordMusic extends AppCompatActivity {

    // changings done in playmusic and playmusic.xml
    LinearLayout liked, add, search, listenlater;
    ImageView recordMusic,listen_audio;
    private static final int MICROPHONE_PERMISSION_CODE = 100;
    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;
    boolean isRecording;
    SeekBar seekBar;
    TextView time,totalTime;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_music);
        recordMusic = findViewById(R.id.record);
        listen_audio = findViewById(R.id.start_stop);
        seekBar = findViewById(R.id.record_seek_bar);
        time = findViewById(R.id.recordTime);
        totalTime= findViewById(R.id.total_time);
        // grant microphone access from user
        if(isMicrophonePresent())
            getMicrophonePermission();



        recordMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isRecording){
                    stopRecording();
                    isRecording = false;
                }
                else {
                    startRecording();
                    isRecording = true;
                }
            }
        });
        mediaPlayer = new MediaPlayer();

        listen_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeSeekBarColor();
                playAudio(getRecordingFilePath());
            }
        });

    }

    private boolean isMicrophonePresent(){
        return this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE);
    }

    private void getMicrophonePermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_DENIED)
        {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.RECORD_AUDIO},MICROPHONE_PERMISSION_CODE);
        }
    }

    private String getRecordingFilePath(){
        ContextWrapper contextWrapper = new ContextWrapper(getApplicationContext());
        File musicDirectory = contextWrapper.getExternalFilesDir(Environment.DIRECTORY_MUSIC);
//        boolean isMountend = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
//        String filename = null;
//        filename = getExternalCacheDir().getAbsolutePath();
//        filename += "/audiorecordtest.3gp";
          File file = new File(musicDirectory,"testrecordingfile" + ".mp3");
        return file.getPath();
    }

    private void startRecording(){
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setOutputFile(getRecordingFilePath());
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            mediaRecorder.prepare();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        mediaRecorder.start();
        onPlayClick();
        Toast.makeText(RecordMusic.this,"File will be stored at " + getRecordingFilePath(),Toast.LENGTH_LONG).show();
    }

    private void stopRecording(){
        onPauseClick();
        mediaRecorder.stop();
        Toast.makeText(RecordMusic.this,"Recording stopped",Toast.LENGTH_SHORT).show();
        mediaRecorder.release();
        mediaRecorder = null;
        pauseListen();
    }

    @SuppressLint("ResourceAsColor")
    private void onPlayClick(){
        recordMusic.setBackgroundColor(R.color.black);
        recordMusic.setImageResource(R.drawable.pause_12);
    }
    @SuppressLint("ResourceAsColor")
    private void onPauseClick(){
        recordMusic.setBackgroundColor(R.color.black);
        recordMusic.setImageResource(R.drawable.stop);
    }

    @SuppressLint("ResourceAsColor")
    private void playListen(){
        listen_audio.setBackgroundColor(R.color.black);
        listen_audio.setImageResource(R.drawable.pause);
    }

    @SuppressLint("ResourceAsColor")
    private void pauseListen(){
        listen_audio.setBackgroundColor(R.color.black);
        listen_audio.setImageResource(R.drawable.playone);
    }

    private void playAudio(String audioUrl) {

        mediaPlayer.setAudioAttributes(new AudioAttributes.Builder().setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .setUsage(AudioAttributes.USAGE_MEDIA).build());

        if(!mediaPlayer.isPlaying()){
            try {
                playListen();
                mediaPlayer.setDataSource(audioUrl);
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                       // changeSeekBarColor();
                        setSeekBar(audioUrl);
                        mediaPlayer.start();
                        mediaPlayer.setLooping(true);
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
                pauseListen();
                //time.setText(convertToMMSS(String.valueOf(mediaPlayer.getCurrentPosition())));
                //mediaPlayer.pause();
                mediaPlayer.reset();
                mediaPlayer.prepare();
                mediaPlayer.stop();
                mediaPlayer.release();

            } catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(RecordMusic.this,"Audio Paused",Toast.LENGTH_SHORT).show();
        }

    }

    @SuppressLint("ResourceAsColor")
    private void changeSeekBarColor(){
        //seekBar.setBackgroundColor(R.color.black);
        seekBar.setAlpha(1.0F);
        //seekBar.setThumbTintMode(R.color.yellow);
    }

    public void setSeekBar(String audioUrl){

        seekBar.setMax(mediaPlayer.getDuration()/1000);
        Handler handler = new Handler();
        RecordMusic.this.runOnUiThread(new Runnable() {
            @SuppressLint({"DefaultLocale", "SetTextI18n"})
            @Override
            public void run() {
                if(mediaPlayer!=null){
                    int current_pos = mediaPlayer.getCurrentPosition()/1000;
                    time.setText(convertToMMSS(String.valueOf(mediaPlayer.getCurrentPosition())));
                    totalTime.setText(convertToMMSS(String.valueOf(mediaPlayer.getDuration())));
                    seekBar.setProgress(current_pos);

                }
                handler.postDelayed(this,1000);
            }
        });
        // listener for setting position of your seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mediaPlayer != null && b){
                    mediaPlayer.seekTo(i*1000); //i*1000
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

    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        @SuppressLint("DefaultLocale") String returnValue = String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
        return returnValue;

    }

}
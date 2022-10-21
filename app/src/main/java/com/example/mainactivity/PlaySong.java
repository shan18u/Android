package com.example.mainactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextClassificationContext;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class PlaySong extends AppCompatActivity implements OnDestroy {

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
        updateSeek.interrupt();
    }

    TextView textView;
    ImageView play, previous, next;
    ArrayList<File> songs;
    MediaPlayer mediaPlayer;
    String textContent;   // name of the song = curentSong
    int position;
    SeekBar seekBar;

    Thread updateSeek;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        textView = findViewById(R.id.textView);
        play = findViewById(R.id.play);
        previous = findViewById(R.id.previous);
        next = findViewById(R.id.next);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        songs = (ArrayList) bundle.getParcelableArrayList("songList");

        textContent = intent.getStringExtra("currentSong");
        textView.setText(textContent);                               // songs name visisble
        textView.setSelected(true);
        position = intent.getIntExtra("name", 0);  // play song
        Uri uri = Uri.parse(songs.get(position).toString());        // // get the song from the actual location and bring it to play 1
        mediaPlayer = MediaPlayer.create(this, uri);      // 2
        mediaPlayer.start();
        seekBar.setMax(mediaPlayer.getDuration());


      seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
          @Override
          public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
          }

          @Override
          public void onStartTrackingTouch(SeekBar seekBar) {

          }

          @Override
          public void onStopTrackingTouch(SeekBar seekBar) {
              mediaPlayer.seekTo(seekBar.getProgress());       // touch seekabr

          }
    });


    updateSeek = new Thread(){

        @Override
        public void run(){
        int currentPosition = 0;
        try {
            while (currentPosition<mediaPlayer.getCurrentPosition()){
                currentPosition= mediaPlayer.getCurrentPosition();
                seekBar.setProgress(currentPosition);
                sleep(800);
            }
        }
        catch(Exception e){
                e.printStackTrace();
        }
        }
    };
    updateSeek.start();

     play.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             if(mediaPlayer.isPlaying()){
                 play.setImageResource(R.drawable.play);
                 mediaPlayer.pause();
             }
             else{
                 play.setImageResource(R.drawable.pause);
                 mediaPlayer.start();
             }
         }
     });


        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 mediaPlayer.isPlaying();
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    if (position!=0) {
                        position = position - 1;
                    }
                    else {
                        position = songs.size() - 1;
                }
                Uri uri = Uri.parse(songs.get(position).toString());        // // get the song from the actual location and bring it to play 1
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);      // 2
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());


                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);

            }

        });



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.isPlaying();
                mediaPlayer.stop();
                mediaPlayer.release();
                if (position!=songs.size() - 1) {
                    position = position + 1;
                }
                else {
                    position = 0;
                }

                Uri uri = Uri.parse(songs.get(position).toString());        // // get the song from the actual location and bring it to play 1
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);      // 2
                mediaPlayer.start();
                play.setImageResource(R.drawable.pause);
                seekBar.setMax(mediaPlayer.getDuration());

                textContent = songs.get(position).getName().toString();
                textView.setText(textContent);

            }

        });

}}

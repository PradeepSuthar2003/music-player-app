package com.pogpks.pogplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class music_player extends AppCompatActivity {

    TextView music_title,artist,duration,min_progress;
    ArrayList<music_data> music_list = send_music_data_pos.music_list;
    ImageView play_btn,play_logo,pause_logo,ring_music,nextbtn,prevbtn;
    SeekBar seekBar;
    int x=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        music_title = findViewById(R.id.music_title);
        play_btn = findViewById(R.id.play_btn);
        play_logo = findViewById(R.id.play_logo);
        pause_logo = findViewById(R.id.pause_logo);
        ring_music = findViewById(R.id.music_logo);
        nextbtn = findViewById(R.id.next_btn);
        prevbtn = findViewById(R.id.perv_btn);
        artist = findViewById(R.id.artist_name);
        duration = findViewById(R.id.total_duration);
        min_progress = findViewById(R.id.min_progress);

        seekBar = findViewById(R.id.seekBar);

        setValues(send_music_data_pos.currentIndex);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser){
                    if(progress == seekBar.getMax()) {
                        try {
                            nextMusic();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    send_music_data_pos.getInstance().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        music_player.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(send_music_data_pos.getInstance() != null) {
                    seekBar.setProgress(send_music_data_pos.getInstance().getCurrentPosition());
                    min_progress.setText(convertToMMSS(send_music_data_pos.getInstance().getCurrentPosition() + ""));
                }
                if(seekBar.getProgress() == seekBar.getMax() && !send_music_data_pos.getInstance().isPlaying()) {
                    try {
                        nextMusic();
                        setValues(send_music_data_pos.currentIndex);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if(send_music_data_pos.getInstance().isPlaying())
                    ring_music.setRotation(x++);
                else
                    ring_music.setRotation(x);

                new Handler().postDelayed(this,100);
            }
        });

        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play_pause();
            }
        });

        nextbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    nextMusic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        prevbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    prevMusic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            playMusic(send_music_data_pos.currentIndex);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setValues(int pos){
        x=0;
        seekBar.setMax(Integer.parseInt(music_list.get(pos).duration));
        music_title.setText(music_list.get(pos).title);
        artist.setText(music_list.get(pos).artist);
        duration.setText(convertToMMSS(music_list.get(pos).duration));
    }

    public void playMusic(int pos) throws IOException {
        send_music_data_pos.getInstance().reset();
        send_music_data_pos.getInstance().setDataSource(music_list.get(pos).path);
        send_music_data_pos.getInstance().prepare();
        seekBar.setProgress(0);
        if(play_logo.getVisibility() != View.VISIBLE)
            send_music_data_pos.getInstance().start();
    }

    public void play_pause(){
        if(send_music_data_pos.getInstance().isPlaying()){
            send_music_data_pos.getInstance().pause();
            pause_logo.setVisibility(View.GONE);
            play_logo.setVisibility(View.VISIBLE);
        }else{
            send_music_data_pos.getInstance().start();
            pause_logo.setVisibility(View.VISIBLE);
            play_logo.setVisibility(View.GONE);
        }
    }

    public void nextMusic() throws IOException {
        if(send_music_data_pos.currentIndex==music_list.size()-1){
            return;
        }
        int next_song = send_music_data_pos.currentIndex+=1;
        playMusic(next_song);
        setValues(next_song);
    }

    public void prevMusic() throws IOException {
        if(send_music_data_pos.currentIndex==0){
            return;
        }
        int next_song = send_music_data_pos.currentIndex-=1;
        playMusic(next_song);
        setValues(next_song);
    }

    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);

        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}
package com.pogpks.pogplayer;

import android.content.Context;
import android.media.MediaPlayer;

import java.util.ArrayList;

public class send_music_data_pos {
    static MediaPlayer instance;

    public static MediaPlayer getInstance(){
        if(instance == null){
            instance = new MediaPlayer();
        }
        return instance;
    }

    public static int currentIndex = -1;
    public static ArrayList<music_data> music_list = null;
}

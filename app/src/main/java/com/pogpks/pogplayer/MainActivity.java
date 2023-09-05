package com.pogpks.pogplayer;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<music_data> music_list;
    TextView noMusicFound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        music_list = new ArrayList<>();
        noMusicFound = findViewById(R.id.nomusicfound);

        recyclerView = findViewById(R.id.recyle_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(!checkPermission()){
            getPermission();
        }

        String[] projection = {
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.COMPOSER
        };


        String selection = MediaStore.Audio.Media.IS_MUSIC + "!=0";

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,selection,null,null);

        while(cursor.moveToNext()){
            music_list.add(new music_data(cursor.getString(1),cursor.getString(2),cursor.getString(0),cursor.getString(3)));
        }

        if(music_list.size() == 0){
            noMusicFound.setVisibility(View.VISIBLE);
        }

        recyclerView.setAdapter(new musicAdapter(this,music_list));
    }

    public boolean checkPermission(){
        int response = ActivityCompat.checkSelfPermission(this,READ_EXTERNAL_STORAGE);
        if(response == PackageManager.PERMISSION_GRANTED){
            return true;
        }else{
            return false;
        }
    }

    public void getPermission(){
        ActivityCompat.requestPermissions(this,new String[]{READ_EXTERNAL_STORAGE},1234);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (recyclerView != null) {
            recyclerView.setAdapter(new musicAdapter(this, music_list));
        }
    }
}
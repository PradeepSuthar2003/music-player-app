package com.pogpks.pogplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class musicAdapter extends RecyclerView.Adapter<musicAdapter.ViewHolder>{

    Context context;
    ArrayList<music_data> music_list;

    public musicAdapter(Context context,ArrayList<music_data> music_list){
        this.context = context;
        this.music_list = music_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recy_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int pos = position;
        holder.music_title.setText(music_list.get(position).title);
        holder.music_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_music_data_pos.getInstance().reset();
                send_music_data_pos.currentIndex = pos;
                send_music_data_pos.music_list = music_list;
                Intent intent = new Intent(context,music_player.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                holder.music_title.setTextColor(Color.parseColor("#606053"));
            }
        });
    }

    @Override
    public int getItemCount() {
        return music_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView music_title;
        public ViewHolder(View v){
            super(v);
            music_title = v.findViewById(R.id.music_title);
        }
    }

}

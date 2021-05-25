package com.example.mello2;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Music_Adapter extends RecyclerView.Adapter<Music_Adapter.MyViewHolder> {
    private Context context;
    private ArrayList<Music_files> music_files;
    Music_Adapter(Context context,ArrayList<Music_files> music_files){
    this.context=context;
    this.music_files=music_files;
}
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view= LayoutInflater.from(context).inflate(R.layout.music_items,parent,false);
        return new  MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
    holder.file_name.setText(music_files.get(position).getTitel());
    holder.itemView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent= new Intent(context,PlayerActivity.class);
            context.startActivities(new Intent[]{intent});

        }
    });
    }

    @Override
    public int getItemCount() {
        return music_files.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView file_name,artist_name;
        ImageView album_art;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name= itemView.findViewById(R.id.title);
            artist_name= itemView.findViewById(R.id.artist);
            album_art= itemView.findViewById(R.id.image);
        }
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}

package com.example.mello2.Adapters;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mello2.Music_files;
import com.example.mello2.R;

import java.util.ArrayList;

import static com.example.mello2.Activities.MainActivity.art_album;
import static com.example.mello2.Activities.MainActivity.name_artist;
import static com.example.mello2.Activities.MainActivity.name_song;
import static com.example.mello2.Activities.MainActivity.playbtn;
import static com.example.mello2.Activities.MainActivity.player;
import static com.example.mello2.Activities.MainActivity.relativeLayout;

public class Album_details_adapter extends RecyclerView.Adapter<Album_details_adapter.MyViewHolder> {
    private Context context;
    private ArrayList<Music_files> music_files;



    public Album_details_adapter(Context context, ArrayList<Music_files> music_files){
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
        holder.artist_name.setText(music_files.get(position).getArtist());
        byte []image =getAlbumArt(music_files.get(position).getPath());
        if (image!=null){
            Glide.with(context).load(image).into(holder.album_art);
        }else {
            Glide.with(context).load(R.drawable.ic_baseline_music_note_24).into(holder.album_art);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                init_shortcut(image,music_files.get(position).getTitel(),music_files.get(position).getArtist());
                player.setCurrentSong(position);
                player.startSong();
                playbtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);

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
    void init_shortcut(byte[] bytes,String s,String s2){
        relativeLayout.setVisibility(View.VISIBLE);
        if (bytes!=null){
            Glide.with(context).load(bytes).into(art_album);
        }else {
            Glide.with(context).load(R.drawable.ic_baseline_music_note_24).into(art_album);
        }
        name_song.setText(s);
        name_artist.setText(s2);
    }

}

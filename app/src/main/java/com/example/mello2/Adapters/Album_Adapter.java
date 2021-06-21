package com.example.mello2.Adapters;

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

import com.bumptech.glide.Glide;
import com.example.mello2.Activities.Album_details_activity;
import com.example.mello2.Music_files;
import com.example.mello2.R;

import java.util.ArrayList;

public class Album_Adapter extends RecyclerView.Adapter<Album_Adapter.MyHolder> {
    private Context context;
    private ArrayList<Music_files> album_files;
    View view;

    public Album_Adapter(Context context, ArrayList<Music_files> album_files) {
        this.context = context;
        this.album_files = album_files;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.album_item,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.album_name.setText(album_files.get(position).getAlbum());
        holder.album_artist.setText(album_files.get(position).getArtist());
        byte []image =getAlbumArt(album_files.get(position).getPath());
        if (image!=null){
            Glide.with(context).load(image).into(holder.album_img);
        }else {
            Glide.with(context).load(R.drawable.ic_baseline_album_24).into(holder.album_img);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, Album_details_activity.class);
                intent.putExtra("Albumname",album_files.get(position).getAlbum());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return album_files.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView album_img;
        TextView album_name,album_artist;
        public MyHolder(@NonNull View itemView) {
            super(itemView);
            album_img=itemView.findViewById(R.id.album_art);
            album_name=itemView.findViewById(R.id.album_name);
            album_artist=itemView.findViewById(R.id.album_artist);
        }
    }
    private byte[] getAlbumArt(String uri){
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }

    public Context getContext() {
        return context;
    }
}

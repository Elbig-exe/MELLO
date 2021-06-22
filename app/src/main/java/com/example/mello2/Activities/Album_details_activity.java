package com.example.mello2.Activities;

import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.mello2.Adapters.Album_details_adapter;
import com.example.mello2.Music_files;
import com.example.mello2.R;

import java.util.ArrayList;

import static com.example.mello2.Activities.MainActivity.music_files;

public class Album_details_activity extends AppCompatActivity {
RecyclerView Album_song_list;
ImageView its_album_album;
TextView it_album_name;
String albumnname;
ArrayList<Music_files> song_list= new ArrayList<Music_files>();
Album_details_adapter album_adapter;
ImageView art_album,playbtn,skipbtn;
TextView name_song,name_artist;
RelativeLayout relativeLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_details_activity);
        initviews();
        relativeLayout.setVisibility(View.GONE);
        initVisuals();
        getAlbum_Songs();

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!(song_list.size()<1)){
            album_adapter=new Album_details_adapter(this,song_list);
            Album_song_list.setAdapter(album_adapter);
            Album_song_list.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        }
    }

    public void initviews(){
        Album_song_list=findViewById(R.id.song_list);
        its_album_album=findViewById(R.id.album_art);
        it_album_name=findViewById(R.id.album_name);
        art_album= findViewById(R.id.art_album_albums);
        playbtn= findViewById(R.id.playbutton_album);
        skipbtn= findViewById(R.id.skipbutton_album);
        name_artist= findViewById(R.id.name_artist_album);
        name_song= findViewById(R.id.name_song_album);
        relativeLayout= findViewById(R.id.album_shortcut);
    }
    public void initVisuals(){
        albumnname= getIntent().getStringExtra("Albumname");
        it_album_name.setText(albumnname);
    }
    public void getAlbum_Songs(){
        int i,j = 0;
        for (i=0;i<music_files.size();i++){
            if (albumnname.equals(music_files.get(i).getAlbum())){
                song_list.add(j,music_files.get(i));
                j++;
            }
        }
        byte []image = getAlbumArt(song_list.get(0).getPath());
        if (image != null) {
            Glide.with(this).asBitmap().load(image).into(its_album_album);
        } else {
            Glide.with(this).asBitmap().load(R.drawable.ic_baseline_person_24).into(its_album_album);
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
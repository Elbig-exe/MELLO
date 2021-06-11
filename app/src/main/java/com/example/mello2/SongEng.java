package com.example.mello2;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import static com.example.mello2.MainActivity.music_files;

public class SongEng {
    private ArrayList<Music_files> songslist;
    private MediaPlayer mediaPlayer;
    private Uri currentUri;
    private Context context;
    private boolean paused=false;
    int songPosition=-1;

    public SongEng(Context context){
        songslist=music_files;
        this.context=context;
    }
    void setContext(Context context){
        this.context=context;
    }
    boolean setCurrentSong(int position){
        songPosition=position;
        if (songslist!=null) {//setting the current song
            currentUri = Uri.parse(music_files.get(position).getPath());
            return true;
        }
        return false;
    }

    void startSong(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, currentUri);
        mediaPlayer.start();
        paused=false;
    }
    void seekTo(int sec){
        mediaPlayer.seekTo(sec*1000);
    }
    int getProgress(){
        if(mediaPlayer!=null){
            return mediaPlayer.getCurrentPosition()/1000;
        }
        return 0;
    }
    int getSongDuration(){
        if(mediaPlayer!=null) {
            return Integer.parseInt(songslist.get(songPosition).getDuration()) / 1000;
        }
        return 0;
    }
    String getSongName(){
        return songslist.get(songPosition).getTitel();
    }
    String getArtistName(){
        return songslist.get(songPosition).getArtist();
    }
    void playNext(){
        songPosition= (songPosition+1) % songslist.size();
        setCurrentSong(songPosition);
        startSong();
    }
    void playPrevious(){
        songPosition= (songPosition-1) <0? (songslist.size()-1):(songPosition-1);
        setCurrentSong(songPosition);
        startSong();
    }
    void pause(){
        mediaPlayer.pause();
        paused=true;
    }
    void resume(){
        mediaPlayer.start();
        paused=false;
    }
    byte[] getArt(){
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        retriever.setDataSource(currentUri.toString());
        byte[] art=retriever.getEmbeddedPicture();
        if (art!=null){
            return art;
        }
        else {
            return null;
        }
    }
    boolean isPaused(){
        return paused;
    }

}

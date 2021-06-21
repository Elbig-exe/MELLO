package com.example.mello2;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import static com.example.mello2.Activities.MainActivity.music_files;

public class SongEng {
    private ArrayList<Music_files> songslist;
    public static MediaPlayer mediaPlayer;
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
    public boolean setCurrentSong(int position){
        songPosition=position;
        if (songslist!=null) {//setting the current song
            currentUri = Uri.parse(music_files.get(position).getPath());
            return true;
        }
        return false;
    }
    public int getCurrentSong(){
        return songPosition;
    }

    public void startSong(){
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, currentUri);
        mediaPlayer.start();
        paused=false;
    }
    public void seekTo(int sec){
        mediaPlayer.seekTo(sec*1000);
    }
    public int getProgress(){
        if(mediaPlayer!=null){
            return mediaPlayer.getCurrentPosition()/1000;
        }
        return 0;
    }
    public int getSongDuration(){
        if(mediaPlayer!=null) {
            return Integer.parseInt(songslist.get(songPosition).getDuration()) / 1000;
        }
        return 0;
    }
    public String getSongName(){
        return songslist.get(songPosition).getTitel();
    }
    public String getArtistName(){
        return songslist.get(songPosition).getArtist();
    }
    public void playNext(){
        songPosition= (songPosition+1) % songslist.size();
        setCurrentSong(songPosition);
        startSong();
    }
    public void playPrevious(){
        songPosition= (songPosition-1) <0? (songslist.size()-1):(songPosition-1);
        setCurrentSong(songPosition);
        startSong();
    }
    public void pause(){
        mediaPlayer.pause();
        paused=true;
    }
    public void resume(){
        mediaPlayer.start();
        paused=false;
    }
    public byte[] getArt(){
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
    public boolean isPaused(){
        return paused;
    }
    public void imageAnimation(Context context, ImageView imageView, byte[] bytes){
        Animation aniout = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        Animation aniin = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
        aniout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (bytes!=null){
                    Glide.with(context).asBitmap().load(bytes).into(imageView);}
                else {
                    Glide.with(context).asBitmap().load(R.drawable.ic_baseline_person_24).into(imageView);
                }
                aniin.setAnimationListener(new Animation.AnimationListener() {

                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });


            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imageView.startAnimation(aniout);


    }

}

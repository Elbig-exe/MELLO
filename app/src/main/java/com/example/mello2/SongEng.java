package com.example.mello2;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.example.mello2.MainActivity.db;
import static com.example.mello2.MainActivity.music_files;

public class SongEng {
    private ArrayList<Music_files> songslist;
    private MediaPlayer mediaPlayer;
    private Uri currentUri;
    private Context context;
    private float learningRate;
    private boolean paused=false;
    Weights chosenWeight;
    List<Weights> weights;
    float randomLevel;
    int songPosition=-1;
    boolean shuffle;

    public void setShuffle(boolean shuffle){
        this.shuffle=shuffle;
    }

    private static float sigmoid(float x) {
        return ((float)(1 / (1 + Math.exp(-x))));
    }

    void setRandomLevel(float rand){
        this.randomLevel=rand;
    }

    public SongEng(Context context){
        songslist=music_files;
        chosenWeight=null;
        learningRate=0.3f;
        randomLevel=0.1f;
        shuffle=true;
        this.context=context;
    }

    void setContext(Context context){
        this.context=context;

    }

    boolean setCurrentSong(int position){
        new Thread(()-> {
            weights=db.getWeightsForSong(songslist.get(songPosition).getPath());
        }).start();

        //db.printWeightsForSong(songslist.get(position).getPath());
        songPosition=position;
        if (songslist!=null) {//setting the current song
            currentUri = Uri.parse(music_files.get(position).getPath());
            return true;
        }
        return false;
    }

    int getPositionOfSong(String path){
        new Thread(()-> {
            weights=db.getWeightsForSong(songslist.get(songPosition).getPath());
        }).start();
        for(int i=0;i<songslist.size();i++){
            if(songslist.get(i).getPath().equals(path)){
                return i;
            }
        }
        return 0;
    }

    boolean setCurrentSong(String path){
        songPosition=getPositionOfSong(path);
        if (songslist!=null) {//setting the current song
            currentUri = Uri.parse(path);
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

    void learnFromChosenWeight(){
        if(chosenWeight!=null){
            chosenWeight.topath=songslist.get(songPosition).getPath();

            if(goodDecision()){
                chosenWeight.weight=sigmoid((chosenWeight.weight+learningRate));
            }else{
                chosenWeight.weight=sigmoid((chosenWeight.weight-learningRate));
            }
            db.setWeight(chosenWeight);

        }
    }

    boolean goodDecision(){
        return getProgress()>getSongDuration()/2;
    }

    void playNextRand(){
        learnFromChosenWeight();
        //List<Weights> weights=db.getWeightsForSong(songslist.get(songPosition).getPath());
        Random rand=new Random(System.currentTimeMillis());
        int maxPos=0;
        float w;
        float max=0f;
        for(int i=0;i<weights.size();i++){
            w=sigmoid((weights.get(i).weight+(rand.nextFloat()*randomLevel))/2);
            if(w>max){
                max=w;
                maxPos=i;
            }
        }
        setCurrentSong(weights.get(maxPos).topath);
        if(goodDecision()){
            chosenWeight=weights.get(maxPos);
        }
    }

    void playNext(){
        if(shuffle){
            playNextRand();
        }else{
            songPosition= (songPosition+1) % songslist.size();
            setCurrentSong(songPosition);
        }
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

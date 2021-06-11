package com.example.mello2;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class PlayerActivity extends AppCompatActivity {
    TextView Song_name, Song_artist,timeplayed,song_duration;
    ImageView song_img, backbutton,menubutton,playbutton,next,previuse,shuffle,replaybn;
    SeekBar seekBar;
    int position=1;
    private Uri uri;
    private Handler handler=new Handler();
    private Thread prevth,nextth,playth;
    private SongEng player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        player=new SongEng(this);
        setContentView(R.layout.activity_player);
        initViews();//initialises text views and image views
        itializePlayer();//initialises the media player with the current song and starts it
        initSeekBarFunctionality();
    }
    void initSeekBarFunctionality(){
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser){
                    player.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int mcurentpos=player.getProgress();
                seekBar.setProgress(mcurentpos);
                timeplayed.setText(formatedText(mcurentpos));
                handler.postDelayed(this,1000);
            }
        });
    }
    private String formatedText(int mcurentpos) {
        String totalout="";
        String totalnew="";
        String sec=String.valueOf(mcurentpos%60);
        String min=String.valueOf((mcurentpos/60)%60);
        totalout=min+":"+sec;
        totalnew=min+":"+"0"+sec;
        if (sec.length()==1){
            return totalnew;
        }else {
            return totalout;
        }
    }
    //sets up the current song and starts it
    private void itializePlayer() {
        position=getIntent().getIntExtra("position",1);
        player.setCurrentSong(position);
        playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        player.startSong();
        initVisuals();
    }
    private void initViews(){
        Song_name=findViewById(R.id.Song_name);
        Song_artist =findViewById(R.id.Song_artist);
        timeplayed =findViewById(R.id.timeplayed);
        song_duration =findViewById(R.id.song_duration);
        song_img =findViewById(R.id.song_img);
        backbutton =findViewById(R.id.backbutton);
        menubutton =findViewById(R.id.more);
        playbutton =findViewById(R.id.play_button);
        next =findViewById(R.id.next);
        previuse =findViewById(R.id.previuse);
        shuffle= findViewById(R.id.shuffle);
        replaybn=findViewById(R.id.replay);
        seekBar=findViewById(R.id.seekbar);
    }

    void initVisuals(){
        Song_name.setText(player.getSongName());
        Song_artist.setText(player.getArtistName());
        seekBar.setMax(player.getSongDuration());
        song_duration.setText(formatedText(player.getSongDuration()));
        byte[] art=player.getArt();
        if (art!=null){
            Glide.with(this).asBitmap().load(art).into(song_img);
        } else {
            Glide.with(this).asBitmap().load(R.drawable.ic_baseline_person_24).into(song_img);
        }
    }

    private void nextClicked() {
        player.playNext();
        initVisuals();
            playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
    }

    private void previousClicked() {
        player.playPrevious();
        initVisuals();
        playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
    }

    private void playButtonClicked() {
        if(player.isPaused()){
            player.resume();
            playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        }else{
            player.pause();
            playbutton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
        }
    }

    private void prevthbtn() {
        prevth=new Thread(){
            @Override
            public void run() {
                super.run();
                previuse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        previousClicked();
                    }
                });
            }
        };
        prevth.start();
    }
    private void nextthbtn() {
        nextth=new Thread(){
            @Override
            public void run() {
                super.run();
                next.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        nextClicked();
                    }
                });
            }
        };
        nextth.start();
    }
    private void playthbtn() {
        playth=new Thread(){
            @Override
            public void run() {
                super.run();
                playbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playButtonClicked();
                    }
                });
            }
        };
        playth.start();
        }
    @Override
    protected void onResume() {
        playthbtn();
        prevthbtn();
        nextthbtn();
        super.onResume();
    }
}
package com.example.mello2.Activities;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mello2.R;

import static com.example.mello2.Activities.MainActivity.art_album;
import static com.example.mello2.Activities.MainActivity.name_artist;
import static com.example.mello2.Activities.MainActivity.name_song;
import static com.example.mello2.Activities.MainActivity.playbtn;
import static com.example.mello2.Activities.MainActivity.player;
import static com.example.mello2.SongEng.mediaPlayer;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    TextView Song_name, Song_artist,timeplayed,song_duration;
    public static ImageView song_img, backbutton,menubutton,playbutton,next,previuse,shuffle,replaybn;
    SeekBar seekBar;
    int position=1;
    private Uri uri;
    private Handler handler=new Handler();
    private Thread prevth,nextth,playth;
   // private SongEng player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //player.setContext(this);
        setContentView(R.layout.activity_player);
        initViews();//initialises text views and image views
        itializePlayer();//initialises the media player with the current song and starts it
        initSeekBarFunctionality();
        mediaPlayer.setOnCompletionListener(this);

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
        playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
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
        imageAnimation(this,song_img,art);
    }

    private void nextClicked() {
        player.playNext();
        initVisuals();
        playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        byte[] bytes= player.getArt();
        imageAnimation(this,song_img,bytes);
        name_song.setText(player.getSongName());
        name_artist.setText(player.getArtistName());
        imageAnimation(this,art_album,bytes);
    }

    private void previousClicked() {
        player.playPrevious();
        initVisuals();
        playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        byte[] bytes= player.getArt();
        imageAnimation(this,song_img,bytes);
        name_song.setText(player.getSongName());
        name_artist.setText(player.getArtistName());
        imageAnimation(this,art_album,bytes);
    }

    private void playButtonClicked() {
        if(player.isPaused()){
            player.resume();
            playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            playbtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        }else{
            player.pause();
            playbutton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
            playbtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
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
                        name_song.setText(player.getSongName());
                        name_artist.setText(player.getArtistName());
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
                        name_song.setText(player.getSongName());
                        name_artist.setText(player.getArtistName());
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
    public static void imageAnimation(Context context, ImageView imageView, byte[] bytes) {
        Animation aniout = AnimationUtils.loadAnimation(context, android.R.anim.fade_out);
        aniout.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                if (bytes != null) {
                    Glide.with(context).asBitmap().load(bytes).into(imageView);
                } else {
                    Glide.with(context).asBitmap().load(R.drawable.ic_baseline_person_24).into(imageView);
                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        imageView.startAnimation(aniout);


    }


    @Override
        public void onCompletion(MediaPlayer mp) {
            nextClicked();
            if(mediaPlayer!=null){
                mediaPlayer.start();
                mediaPlayer.setOnCompletionListener(this);
                playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            }
            name_song.setText(player.getSongName());
            name_artist.setText(player.getArtistName());
            imageAnimation(this,art_album,player.getArt());
        }

}



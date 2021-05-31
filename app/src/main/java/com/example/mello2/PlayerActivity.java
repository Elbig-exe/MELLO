package com.example.mello2;


import android.content.Context;
import android.media.MediaMetadataRetriever;
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

import java.util.ArrayList;

import static com.example.mello2.MainActivity.music_files;
import static com.example.mello2.MainActivity.repeat;
import static com.example.mello2.MainActivity.shuffle;

public class PlayerActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {
    TextView Song_name, Song_artist,timeplayed,song_duration;
    ImageView song_img, backbutton,menubutton,playbutton,next,previuse,shufflebtn,replaybn;
    SeekBar seekBar;
    ArrayList<Music_files> Songslist=new ArrayList<>();
    int position=1;
    private Uri uri;
    private static MediaPlayer mediaPlayer;
    private Handler handler=new Handler();
    private Thread prevth,nextth,playth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        initViews();
        getIntentMethod();
        Song_name.setText(Songslist.get(position).getTitel());
        Song_artist.setText(Songslist.get(position).getArtist());
        mediaPlayer.setOnCompletionListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mediaPlayer!=null&&fromUser){
                    mediaPlayer.seekTo(progress*1000);
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
                if (mediaPlayer!=null){
                    int mcurentpos=mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mcurentpos);
                    timeplayed.setText(formatedText(mcurentpos));

                }
                handler.postDelayed(this,1000);
            }
        });
        shufflebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shuffle){
                    shuffle=false;
                    shufflebtn.setImageResource(R.drawable.ic_baseline_shuffle_24);
                }else{
                    shuffle=true;
                    shufflebtn.setImageResource(R.drawable.ic_baseline_shuffle_24_on);
                }
            }
        });
        replaybn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (repeat){
                    repeat=false;
                    replaybn.setImageResource(R.drawable.ic_baseline_repeat_24);
                }else {
                    repeat=true;
                    replaybn.setImageResource(R.drawable.ic_baseline_repeat_one_24);
                }
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


    private void getIntentMethod() {
        position=getIntent().getIntExtra("position",1);
        Songslist= music_files;
        if (Songslist!=null) {
            playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            uri = Uri.parse(music_files.get(position).getPath());
        }
        if (mediaPlayer!=null) {
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                mediaPlayer.start();
        }
        else {
            mediaPlayer=MediaPlayer.create(this,uri);
            mediaPlayer.start();
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        metaData(uri);


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
        shufflebtn= findViewById(R.id.shuffle);
        replaybn=findViewById(R.id.replay);
        seekBar=findViewById(R.id.seekbar);

    }

    private void metaData(Uri uri){
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int dur= Integer.parseInt(Songslist.get(position).getDuration() )/1000;
        song_duration.setText(formatedText(dur));
        byte[] art=retriever.getEmbeddedPicture();
           imageAnimation(this,song_img,art);

    }

    @Override
    protected void onResume() {
        playthbtn();
        prevthbtn();
        nextthbtn();

        super.onResume();
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
    private void nextClicked() {
        if (mediaPlayer.isPlaying()){
            mediaPlayer.stop();
            mediaPlayer.release();
            position= (position+1) % Songslist.size();
            uri= Uri.parse(Songslist.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            Song_name.setText(Songslist.get(position).getTitel());
            Song_artist.setText(Songslist.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer!=null){
                        int mcurentpos=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mcurentpos);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            mediaPlayer.start();

        }else {
            mediaPlayer.stop();
            mediaPlayer.release();
            position= (position+1) % Songslist.size();
            uri= Uri.parse(Songslist.get(position).getPath());
            mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
            metaData(uri);
            Song_name.setText(Songslist.get(position).getTitel());
            Song_artist.setText(Songslist.get(position).getArtist());
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer!=null){
                        int mcurentpos=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mcurentpos);
                    }
                    handler.postDelayed(this,1000);
                }
            });
            mediaPlayer.setOnCompletionListener(this);
            playbutton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
            mediaPlayer.start();


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
                        previuseClicked();
                    }
                });
            }
        };
        prevth.start();


    }
    private void previuseClicked() {if (mediaPlayer.isPlaying()){
        mediaPlayer.stop();
        mediaPlayer.release();
        position= (position-1) <0? (Songslist.size()-1):(position-1);
        uri= Uri.parse(Songslist.get(position).getPath());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        metaData(uri);
        Song_name.setText(Songslist.get(position).getTitel());
        Song_artist.setText(Songslist.get(position).getArtist());
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    int mcurentpos=mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mcurentpos);
                }
                handler.postDelayed(this,1000);
            }
        });
        mediaPlayer.setOnCompletionListener(this);
        playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        mediaPlayer.start();

    }
    else {
        mediaPlayer.stop();
        mediaPlayer.release();
        position= (position-1) <0? (Songslist.size()-1):(position-1);
        uri= Uri.parse(Songslist.get(position).getPath());
        mediaPlayer=MediaPlayer.create(getApplicationContext(),uri);
        metaData(uri);
        Song_name.setText(Songslist.get(position).getTitel());
        Song_artist.setText(Songslist.get(position).getArtist());
        seekBar.setMax(mediaPlayer.getDuration()/1000);
        PlayerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer!=null){
                    int mcurentpos=mediaPlayer.getCurrentPosition()/1000;
                    seekBar.setProgress(mcurentpos);
                }
                handler.postDelayed(this,1000);
            }
        });
        mediaPlayer.setOnCompletionListener(this);
        playbutton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
        mediaPlayer.start();


    }
    }

    private void playthbtn() {
        playth=new Thread(){
            @Override
            public void run() {
                super.run();
                playbutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        playbuttonClicked();
                    }
                });
            }
        };
        playth.start();

        }
    private void playbuttonClicked() {
        if (mediaPlayer.isPlaying()){
            playbutton.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
            mediaPlayer.pause();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer!=null){
                        int mcurentpos=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mcurentpos);
                    }
                    handler.postDelayed(this,1000);
                }
            });
        }
        else {
            playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            mediaPlayer.start();
            seekBar.setMax(mediaPlayer.getDuration()/1000);
            PlayerActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mediaPlayer!=null){
                        int mcurentpos=mediaPlayer.getCurrentPosition()/1000;
                        seekBar.setProgress(mcurentpos);
                    }
                    handler.postDelayed(this,1000);
            }
        });
        }
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

    @Override
    public void onCompletion(MediaPlayer mp) {
        nextClicked();
        if(mediaPlayer!=null){
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(this);
            playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        }

    }

}
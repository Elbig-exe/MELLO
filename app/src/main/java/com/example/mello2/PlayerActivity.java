package com.example.mello2;


import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
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
import static com.example.mello2.MainActivity.music_files;

public class PlayerActivity extends AppCompatActivity {
    TextView Song_name, Song_artist,timeplayed,song_duration;
    ImageView song_img, backbutton,menubutton,playbutton,next,previuse,shuffle,replaybn;
    SeekBar seekBar;
    ArrayList<Music_files> Songslist=new ArrayList<>();
    int audio_index = 0;
    int position=1;
    private Uri uri;
    private MediaPlayer mediaPlayer;
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
                mediaPlayer.stop();
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
        shuffle= findViewById(R.id.shuffle);
        replaybn=findViewById(R.id.replay);
        seekBar=findViewById(R.id.seekbar);

    }
    private void metaData(Uri uri){
        MediaMetadataRetriever retriever= new MediaMetadataRetriever();
        retriever.setDataSource(uri.toString());
        int dur= Integer.parseInt(Songslist.get(position).getDuration() )/1000;
        song_duration.setText(formatedText(dur));
        byte[] art=retriever.getEmbeddedPicture();
        if (art!=null){
            Glide.with(this).asBitmap().load(art).into(song_img);
        }
        else {
            Glide.with(this).asBitmap().load(R.drawable.ic_baseline_person_24).into(song_img);
        }
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
        playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        mediaPlayer.start();

    }else {
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
}
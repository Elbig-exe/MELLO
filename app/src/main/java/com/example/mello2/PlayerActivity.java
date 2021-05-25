package com.example.mello2;


import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import static com.example.mello2.MainActivity.music_files;

public class PlayerActivity extends AppCompatActivity {
    TextView Song_name, Song_artist,timeplayed,song_duration;
    ImageView song_img, backbutton,menubutton,playbutton,next,previuse,shuffle,replaybn;
    SeekBar seekBar;
    ArrayList<Music_files> Songslist=new ArrayList<>();
    int audio_index = 1;
    int position=1;
    private Uri uri;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
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
        getIntentMethod(audio_index);
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
    }

    private void getIntentMethod(int pos) {
        Songslist= music_files;
        if (Songslist!=null){
            playbutton.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
            uri= Uri.parse(music_files.get(position).getPath());
            mediaPlayer=MediaPlayer.create(this,uri);
            mediaPlayer.start();
            audio_index=pos;
        }
        seekBar.setMax(mediaPlayer.getDuration()/1000);


    }


    private void initViews(){

    }
}
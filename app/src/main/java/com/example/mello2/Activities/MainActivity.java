package com.example.mello2.Activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mello2.Fragments.Albums;
import com.example.mello2.Fragments.Artists;
import com.example.mello2.Fragments.Playlists;
import com.example.mello2.Fragments.Search;
import com.example.mello2.Fragments.Tracks;
import com.example.mello2.Music_files;
import com.example.mello2.R;
import com.example.mello2.SongEng;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import static com.example.mello2.SongEng.mediaPlayer;

public class MainActivity extends AppCompatActivity {
    public static final int REQUAST=1;
    public static ArrayList<Music_files> music_files;
    public static SongEng player;
    public static boolean shuffle=false,repeat=false;
    Fragment tracks;
    Fragment artists;
    Fragment playlists;
    Fragment albums;
    Fragment search;
    public static ImageView art_album,playbtn,skipbtn,settingbtn;
    public static TextView name_song,name_artist;
    public static RelativeLayout relativeLayout;
    public static FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        init_views();
        relativeLayout.setVisibility(View.GONE);
        init_fragments();
        music_files= getAudio(this);
        BottomNavigationView navbar=findViewById(R.id.nav_bar);
        FrameLayout frameLayout = findViewById(R.id.frame);
        navbar.setOnNavigationItemSelectedListener(navL);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new Tracks()).commit();
        player=new SongEng(this);

    }

    void init_fragments (){
        tracks=new Tracks();
        artists= new Artists();
        playlists=new Playlists();
        albums=new Albums();
        search=new Search();

    }
    void init_views(){
        art_album= findViewById(R.id.art_album);
        playbtn= findViewById(R.id.playbutton);
        skipbtn= findViewById(R.id.skipbutton);
        name_artist= findViewById(R.id.name_artist);
        name_song= findViewById(R.id.name_song);
        relativeLayout= findViewById(R.id.shortcut);
        frameLayout= findViewById(R.id.frame);
        settingbtn=findViewById(R.id.settings_button);
    }
    public void playbtn(View view){
        if(mediaPlayer.isPlaying()){
            player.pause();
            playbtn.setImageResource(R.drawable.ic_baseline_play_circle_filled_24);
        }else {
            player.resume();
            playbtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
        }
    }
    public void skip(View view){
        player.playNext();
        byte[] bytes= player.getArt();
        Glide.with(this).asBitmap().load(bytes).into(art_album);
        name_song.setText(player.getSongName());
        name_artist.setText(player.getArtistName());
        playbtn.setImageResource(R.drawable.ic_baseline_pause_circle_filled_24);
    }
    public void setting(View view){
        Intent intent= new Intent(this, SettingsActivity.class);
        this.startActivity(intent);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navL =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;
                    switch (item.getItemId()) {
                        case R.id.Tracks:
                            selected=new Tracks();
                            break;
                        case R.id.artist:
                            selected= new Artists();
                            break;
                        case R.id.playlist:
                            selected=new Playlists();
                            break;
                        case R.id.album:
                            selected= new Albums();
                            break;
                        case R.id.search:
                            selected=new Search();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame,selected).commit();
                    return true;
                }
            };
    private void permission() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUAST);
        }
        else {
            Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast", Toast.LENGTH_SHORT);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permission, @NonNull int[]grantResults){
        super.onRequestPermissionsResult(requestCode,permission,grantResults);
        if (requestCode==REQUAST){
            if (grantResults[0]==PackageManager.PERMISSION_GRANTED){
                BottomNavigationView navbar = findViewById(R.id.nav_bar);
                Toast.makeText(getApplicationContext(), "This is a message displayed in a Toast", Toast.LENGTH_SHORT);
                navbar.setOnNavigationItemSelectedListener(navL);
            }else {
                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUAST);
            }
        }


    }
    public static ArrayList<Music_files> getAudio(Context context){
        ArrayList<Music_files> tempArrayList= new ArrayList<>();
        Uri uri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[]projection ={
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.DATA
        };
        Cursor cursor=context.getContentResolver().query(uri,projection,null,null,null);
        if (cursor!=null){
            while (cursor.moveToNext()){
                String artist=cursor.getString(0);
                String album=cursor.getString(1);
                String duration=cursor.getString(2);
                String titel=cursor.getString(3);
                String path=cursor.getString(4);
                Music_files music_files= new Music_files(titel,duration,artist,path,album);
                //Log.e("Music_list","  Album:"+album+"  title:"+titel+"  duration:"+ duration+"  artist:"+artist+"  path:"+path);
                tempArrayList.add(music_files);
            }
            cursor.close();
        }
        return tempArrayList;
    }
    public void song_details(View view){
        Intent intent= new Intent(this, PlayerActivity.class);
        intent.putExtra("position",player.getCurrentSong());
        this.startActivity(intent);

    }


}
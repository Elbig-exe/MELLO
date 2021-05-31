package com.example.mello2;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int REQUAST=1;
    static ArrayList<Music_files> music_files;
    public static boolean shuffle=false,repeat=false;
    Fragment tracks;
    Fragment artists;
    Fragment playlists;
    Fragment albums;
    Fragment search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        permission();
        init_fragments();
        music_files= getAudio(this);
        BottomNavigationView navbar=findViewById(R.id.nav_bar);
        FrameLayout frameLayout = findViewById(R.id.frame);
        navbar.setOnNavigationItemSelectedListener(navL);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new Tracks()).commit();
        /*MusicDatabase db = Room.databaseBuilder(getApplicationContext(),
                MusicDatabase.class, "musicDb").build();
        SongDao songDao=db.songDao();
        Song song=new Song();
        song.path=music_files.get(1).getPath();
        songDao.insertAll(song);*/
    }

    void init_fragments (){
        tracks=new Tracks();
        artists= new Artists();
        playlists=new Playlists();
        albums=new Albums();
        search=new Search();

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

}
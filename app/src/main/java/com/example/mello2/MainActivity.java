package com.example.mello2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    public static final int REQUAST=1;
    ArrayList<ModelAudio> audioArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navbar=findViewById(R.id.nav_bar);
        FrameLayout frameLayout = findViewById(R.id.frame);
        permission();
        navbar.setOnNavigationItemSelectedListener(navL);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame,new Tracks()).commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navL =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;
                    switch (item.getItemId()) {
                        case R.id.Tracks:
                            selected= new Tracks();
                            break;
                        case R.id.artist:
                            selected= new Artists();
                            break;
                        case R.id.playlist:
                            selected= new Playlists();
                            break;
                        case R.id.album:
                            selected= new Albums();
                            break;
                        case R.id.search:
                            selected= new Search();
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

}
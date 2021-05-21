package com.example.mello2;

import android.app.Application;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class Tracks extends Fragment {
    ArrayList<ModelAudio> audioArrayList;
    RecyclerView recyclerView;
    MediaPlayer mediaPlayer;
    double current_pos, total_duration;
    TextView current, total,audio_name;
    ImageView prev, next, pause;
    SeekBar seekBar;
    int audio_index = 0;
    public Tracks() {

    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_tracks, container, false);
        return view ;
    }

}
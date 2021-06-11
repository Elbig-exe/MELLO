package com.example.mello2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.mello2.MainActivity.music_files;

public class Albums extends Fragment {
    RecyclerView recyclerView;
    Album_Adapter album_adapter;

    public Albums() {
        // Required empty public constructor
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_albums, container, false);
        recyclerView= view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        if (!(music_files.size()<1)){
            album_adapter=new Album_Adapter(getContext(),music_files);
            recyclerView.setAdapter(album_adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        }
        return view ;
    }
}
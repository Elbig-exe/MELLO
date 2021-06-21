package com.example.mello2.Databases;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.mello2.Song;
import com.example.mello2.SongDao;


@Database(entities = {Song.class}, version = 1)
public abstract class MusicDatabase extends RoomDatabase {
    public abstract SongDao songDao();
}



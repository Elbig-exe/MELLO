package com.example.mello2;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Song.class}, version = 1)
public abstract class MusicDatabase extends RoomDatabase {
    public abstract SongDao songDao();
}



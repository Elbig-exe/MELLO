package com.example.mello2;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {Song.class,Weights.class}, version = 2)
public abstract class MusicDatabase extends RoomDatabase {
    public abstract SongDao songDao();

    public abstract WeightsDao weightsDao();
}



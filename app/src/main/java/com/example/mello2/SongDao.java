package com.example.mello2;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface SongDao {
    @Query("SELECT * FROM Song")
    List<Song> getAll();

    @Query("SELECT * FROM Song WHERE path IN (:songIds)")
    List<Song> loadAllByIds(int[] songIds);

    @Insert
    void insertAll(Song... songs);

    @Delete
    void delete(Song song);
}
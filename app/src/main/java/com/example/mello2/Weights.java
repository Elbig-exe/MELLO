package com.example.mello2;


import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity(foreignKeys = {@ForeignKey(entity = Song.class,
        parentColumns = "path",
        childColumns = "topath",
        onDelete = ForeignKey.CASCADE)
})
public class Weights{
    @PrimaryKey
    @NonNull
    public String path;
    public float weight;
    public String topath;
}

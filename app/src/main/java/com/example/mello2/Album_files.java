package com.example.mello2;

public class Album_files {
    byte[] art;
    String name;

    public byte[] getArt(){
        return art;
    }
    public String getName(){
        return name;
    }

    @Override
    public boolean equals(Object o) {
        Album_files al=(Album_files)o;
        return name.equals(al.name);
    }
}

package com.example.mello2;

public class Artists_files {
        byte[] art;
        String artist;
        public String getArtist() {
            return artist;
        }
        public byte[] getArt() {
            return art;
        }
        @Override
        public boolean equals(Object o) {
            Artists_files al = (Artists_files) o;
            return artist.equals(al.artist);
        }

}

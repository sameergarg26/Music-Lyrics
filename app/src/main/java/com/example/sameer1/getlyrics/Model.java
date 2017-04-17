package com.example.sameer1.getlyrics;

/**
 * Created by Sameer1 on 06-04-2017.
 */
public class Model {
    public String song_name;
    public String album_name;
    public String artist_name;
    public String song_lyrics;

    public Model(){
        song_name = "";
        album_name = "";
        artist_name = "";
        song_lyrics = "";
    }

    public String getSongName(){
        return song_name;
    }

    public String getAlbumName(){
        return album_name;
    }

    public String getArtist_name(){
        return artist_name;
    }

    public String getSongLyrics(){
        return song_lyrics;
    }

}

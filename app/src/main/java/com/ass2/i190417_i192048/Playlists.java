package com.ass2.i190417_i192048;

import java.util.ArrayList;
import java.util.List;

public class Playlists {
    public String playlistName;
    public String imageURL;
    public List<Music> musicList;

    public Playlists(String playlistName, String imageURL) {
        this.playlistName = playlistName;
        this.imageURL = imageURL;
        musicList = new ArrayList<>();
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public List<Music> getMusicList() {
        return musicList;
    }

    public void setMusicList(List<Music> musicList) {
        this.musicList = musicList;
    }
}

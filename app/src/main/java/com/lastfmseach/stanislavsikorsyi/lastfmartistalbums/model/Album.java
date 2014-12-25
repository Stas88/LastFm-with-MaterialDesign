package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model;

import java.util.List;

/**
 * Created by stanislavsikorsyi on 24.12.14.
 */
public class Album implements IAlbum {

    private int playcount;
    private String name;
    private List<Image> image;


    public Album() {}

    @Override
    public int getPlaycount() {
        return playcount;
    }

    @Override
    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public List<Image> getImage() {
        return image;
    }

    @Override
    public void setImage(List<Image> image) {
        this.image = image;
    }


}

package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model;

import java.util.List;

/**
 * Created by stanislavsikorsyi on 24.12.14.
 */
public interface IAlbum {
    int getPlaycount();

    void setPlaycount(int playcount);

    String getName();

    void setName(String name);

    List<Image> getImage();

    void setImage(List<Image> image);

    long getId();
}

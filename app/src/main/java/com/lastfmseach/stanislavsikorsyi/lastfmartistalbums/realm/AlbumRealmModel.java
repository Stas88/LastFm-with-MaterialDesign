package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.realm;

import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.IAlbum;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.Image;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.Track;

import java.util.List;

import io.realm.RealmObject;

/**
 * Created by stanislavsikorsyi on 24.12.14.
 */
public class AlbumRealmModel extends RealmObject {

    private int playcount;
    private String name;
    private String imageUrl;

    public int getPlaycount() {
        return playcount;
    }

    public void setPlaycount(int playcount) {
        this.playcount = playcount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

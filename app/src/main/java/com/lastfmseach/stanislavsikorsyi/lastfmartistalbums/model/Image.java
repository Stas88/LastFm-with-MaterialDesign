package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model;

import com.google.gson.annotations.SerializedName;

/**
 * Image model
 */
public class Image {


    private String size;

    @SerializedName("#text")
    private String text;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

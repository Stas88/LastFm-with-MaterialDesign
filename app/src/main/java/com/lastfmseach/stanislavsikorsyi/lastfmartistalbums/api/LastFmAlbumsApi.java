package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.api;

import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.TopAlbums;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.TopAlbumsHolder;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.Track;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.TrackHolder;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.utils.Constants;

import retrofit.Callback;
import retrofit.http.POST;
import retrofit.http.Query;

/**
 * Api description interface
 */
public interface LastFmAlbumsApi {


    @POST("/2.0/")
    void pullAlbums(@Query(Constants.METHOD) String method,
                     @Query(Constants.ARTIST) String artist,
                     @Query(Constants.API_KEY) String key,
                     @Query(Constants.FORMAT) String jsonFormat,
                     Callback<TopAlbumsHolder> callback);


    @POST("/2.0/")
    void pullTracks(@Query(Constants.METHOD) String method,
                    @Query(Constants.ARTIST) String artist,
                    @Query(Constants.ALBUM) String album,
                    @Query(Constants.API_KEY) String key,
                    @Query(Constants.FORMAT) String jsonFormat,
                    Callback<TrackHolder> callback);



}

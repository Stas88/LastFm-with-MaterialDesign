package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;


/**
 * Get API of LsstFM with the help of factory method
 */
public class ApiClient {

    private static LastFmAlbumsApi lastFmApi;

    public static final String SEARCH_API_4SHARED_ENDPOINT = "http://ws.audioscrobbler.com";
    public static final String RETROFIT_TAG = "retrofit";

    public static LastFmAlbumsApi getAlbumsByArtist() {
            RestAdapter restAdapter =
                    new RestAdapter.Builder()
                                    .setLogLevel(RestAdapter.LogLevel.FULL)
                                    .setLog(new RestAdapter.Log() {
                                        public void log(String msg) {
                                            Log.i(RETROFIT_TAG, msg);
                                        }
                                    })
                                    .setEndpoint(SEARCH_API_4SHARED_ENDPOINT)
                                    .build();
            lastFmApi = restAdapter.create(LastFmAlbumsApi.class);
            return lastFmApi;
    }


    public static LastFmAlbumsApi getTracks() {

        Gson gson = new GsonBuilder()
                //Register interpolator in order to prevent building full document
                .registerTypeAdapterFactory(new ItemTypeAdapterFactory()) // This is the important line ;)
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();


            RestAdapter restAdapter =
                    new RestAdapter.Builder()
                            .setLogLevel(RestAdapter.LogLevel.FULL)
                            .setLog(new RestAdapter.Log() {
                                public void log(String msg) {
                                    Log.i(RETROFIT_TAG, msg);
                                }
                            })
                            .setEndpoint(SEARCH_API_4SHARED_ENDPOINT)
                            .setConverter(new GsonConverter(gson))
                            .build();
            lastFmApi = restAdapter.create(LastFmAlbumsApi.class);
        return lastFmApi;
    }


}

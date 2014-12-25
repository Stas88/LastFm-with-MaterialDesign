package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.fragments;

import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.R;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.adapters.TracksAdapter;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.api.ApiClient;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.Track;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.TrackHolder;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Fragent shows tracks of Album
 */
public class TracksFragment extends Fragment {


    private final String TAG = "TracksFragment";
    private ListView trackListView;
    private TracksAdapter tracksAdapter;
    private boolean isDownloadInProgress = false;
    private List<Track> trackList = new ArrayList<Track>();
    private String artistName;
    private String albumName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_songs, container, false);
        trackListView = (ListView)rootView.findViewById(R.id.listView_of_tracks);
        artistName = getArguments().getString(Constants.ARTIST_NAME);
        albumName = getArguments().getString(Constants.ALBUM_NAME);
        return rootView;
    }

    /**
     * This method will only be called once when the retained
     * Fragment is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Retain this fragment across configuration changes.
        setRetainInstance(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListView();
        downloadAlbumData();
    }

    /**
     * Initialization of adapter and listView itself
     */
    private void initListView() {
        tracksAdapter = new TracksAdapter(getActivity(), trackList, this);
        trackListView.setAdapter(tracksAdapter);
        trackListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClickItem(position);
            }
        });
    }

    private void onClickItem(int position) {

    }
    /**
     * Download data from server
     */
    public void downloadAlbumData() {
        if (!isDownloadInProgress) {
            isDownloadInProgress = true;
            ApiClient.getTracks().pullTracks(Constants.METHOD_GET_ALBUM_INFO,
                    artistName, albumName,
                    getResources().getString(R.string.api_key),
                    Constants.JSON,
                    new Callback<TrackHolder>() {

                        @Override
                        public void success(TrackHolder tracks, Response response) {
                            //consumeApiData(albums.getTopAlbums().getAlbum());
                            Log.d(TAG, "success:  " + tracks.getTracks().size());

                        }

                        @Override
                        public void failure(RetrofitError retrofitError) {
                            consumeApiData(null);
                            Log.d(TAG, "failure downloading:  " + retrofitError.getMessage());
                        }
                    });
        }
    }

    /**
     * Consume and render data.
     * @param tracksFromServer
     */
    private void consumeApiData(List<Track> tracksFromServer) {
        if (tracksFromServer != null) {
            trackList.clear();
            // Add the found albums to list to render
            trackList.addAll(tracksFromServer);
            // Tell the adapter that it needs to rerender
            tracksAdapter.notifyDataSetChanged();

        }
        isDownloadInProgress = false;
    }


}


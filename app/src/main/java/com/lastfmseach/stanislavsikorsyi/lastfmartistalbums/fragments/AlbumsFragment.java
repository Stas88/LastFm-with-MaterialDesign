package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.R;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.adapters.AlbumAdapter;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.api.ApiClient;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.Album;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.IAlbum;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.Image;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.TopAlbumsHolder;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.realm.AlbumRealmModel;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.ui.DragController;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.utils.Constants;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Fragment to show albums of certain artist in list
 */
public class AlbumsFragment extends Fragment implements AlbumAdapter.ItemClickListener {


    private final String TAG = "AlbumsFragment";
    private RecyclerView recyclerView;
    private AlbumAdapter albumAdapter;
    private boolean isDownloadInProgress = false;
    private List<IAlbum> albumList = new ArrayList<IAlbum>();
    public  String artistName = "Cher";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(
                R.layout.fragment_album_search, container, false);
        ImageView overlay = (ImageView) rootView.findViewById(R.id.overlay);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.listView_of_albums);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 3);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(new DragController(recyclerView, overlay));
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

    public void setAlbums(List<IAlbum> albums) {
        AlbumAdapter adapter = new AlbumAdapter(getActivity(), albums, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void itemClicked(IAlbum item) {
        onClickItem(item);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initListView();
        if(Utils.isOnline(getActivity())) {
            downloadAlbumData();
        } else {
            Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
            getAlbumsFromRealmStorage();
        }
    }

    /**
     * Initialization of adapter and listView itself
     */
    private void initListView() {
        albumAdapter = new AlbumAdapter(getActivity(), albumList, this);
        recyclerView.setAdapter(albumAdapter);
    }

    /**
     * Click on file item
     * @param item Item of album
     */
    private void onClickItem(IAlbum item) {
        if(Utils.isOnline(getActivity())) {
            TracksFragment newFragment = new TracksFragment();
            Bundle args = new Bundle();
            args.putString(Constants.ALBUM_NAME, item.getName());
            args.putString(Constants.ARTIST_NAME, artistName);
            Log.d(TAG, "album:" + item.getName());
            newFragment.setArguments(args);

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, newFragment);
            transaction.addToBackStack("Album");
            transaction.commit();
        } else {
            Toast.makeText(getActivity(), R.string.no_connection, Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Download data from server in separate thread
     */
    public void downloadAlbumData() {
        if (!isDownloadInProgress) {
            isDownloadInProgress = true;
            ApiClient.getAlbumsByArtist().pullAlbums(Constants.METHOD_GET_ALBUMS,
                    artistName,
                    getResources().getString(R.string.api_key),
                    Constants.JSON,
                    new Callback<TopAlbumsHolder>() {

                        @Override
                        public void success(TopAlbumsHolder albums, Response response) {
                            consumeApiData(albums.getTopAlbums().getAlbum());

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
     * @param albumsFromServer
     */
    private void consumeApiData(List<Album> albumsFromServer) {
        if (albumsFromServer != null) {
            albumList.clear();
            // Add the found albums to list to render
            albumList.addAll(albumsFromServer);
            // Tell the adapter that it needs to rerender
            albumAdapter.notifyDataSetChanged();
            //Save cache
            addObjectsToRealmStorage(albumsFromServer);


        }
        isDownloadInProgress = false;
    }

    /**
     * Adding these objects to the cache in nosql storage
     * @param albumsFromServer List of albums from server
     */
    private void addObjectsToRealmStorage(List<Album> albumsFromServer) {
        Realm realm = Realm.getInstance(getActivity());
        realm.beginTransaction();

        //Delete previously saved objects from search results
        RealmQuery<AlbumRealmModel> query = realm.where(AlbumRealmModel.class);
        RealmResults<AlbumRealmModel> result = query.findAll();
        result.clear();

        //Write new objects of current search results
        for(Album album : albumsFromServer) {
            AlbumRealmModel albumRealm = realm.createObject(AlbumRealmModel.class); // Create a new object
            albumRealm.setName(album.getName());
            albumRealm.setPlaycount(album.getPlaycount());
            albumRealm.setImageUrl(album.getImage().get(Constants.IMAGE_SIZE_SMALL).getText());
        }
        realm.commitTransaction();
        realm.close();
    }

    public void getAlbumsFromRealmStorage() {
        Realm realm = Realm.getInstance(getActivity());
        realm.beginTransaction();

        //Query previously saved objects from search results
        RealmQuery<AlbumRealmModel> query = realm.where(AlbumRealmModel.class);
        RealmResults<AlbumRealmModel> result = query.findAll();
        List<Album> albumsFromCache = new ArrayList<Album>();
        for(AlbumRealmModel albumRealm : result) {
            Album album = new Album();
            album.setName(albumRealm.getName());
            album.setPlaycount(albumRealm.getPlaycount());
            Image image = new Image();
            image.setText(albumRealm.getImageUrl());
            List<Image> imageList = new ArrayList<Image>();
            imageList.add(image);
            album.setImage(imageList);
            albumsFromCache.add(album);
        }
        realm.commitTransaction();
        realm.close();

        albumList.clear();
        albumList.addAll(albumsFromCache);

    }


}

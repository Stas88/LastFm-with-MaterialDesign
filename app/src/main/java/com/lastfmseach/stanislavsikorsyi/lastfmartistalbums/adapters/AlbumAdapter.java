package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.adapters;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.R;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.IAlbum;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.utils.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Adapter for Albums from search
 */
public class AlbumAdapter extends ArrayAdapter<IAlbum> {


    private final Context context;
    private final List<IAlbum> albumList;
    private LayoutInflater inflater;
    private Fragment fragment;

    public AlbumAdapter(Context context, List<IAlbum> albumModels, Fragment fragment) {
        super(context, R.layout.albums_list_item, albumModels);
        this.context = context;
        this.albumList = albumModels;
        this.fragment = fragment;
        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (null == rowView) {
            rowView = inflater.inflate(R.layout.albums_list_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) rowView.findViewById(R.id.album_thumbnail);
            viewHolder.name = (TextView) rowView.findViewById(R.id.album_name);
            viewHolder.playground = (TextView) rowView.findViewById(R.id.album_playcount);

            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();

        IAlbum albumModel = albumList.get(position);
        String albumName = albumModel.getName();
        String imageUrl = albumModel.getImage().get(Constants.IMAGE_SIZE_SMALL).getText();

        int playground = albumModel.getPlaycount();

        holder.name.setText(albumName);
        holder.playground.setText(String.valueOf(playground));

        //Load the album image on a background thread
        if(imageUrl != null) {
            Picasso.with(context)
                    .load(imageUrl).centerCrop().resize(100,100)
                    .into(holder.imageView);
        }
        return rowView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView playground;
        public ImageView imageView;
    }


}


package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.adapters;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.R;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.Track;

import java.util.List;

/**
 * Adapter for Files from search
 */
public class TracksAdapter extends ArrayAdapter<Track> {


    private final Context context;
    private final List<Track> trackList;
    private LayoutInflater inflater;
    private Fragment fragment;

    public TracksAdapter(Context context, List<Track> trackModels, Fragment fragment) {
        super(context, R.layout.albums_list_item, trackModels);
        this.context = context;
        this.trackList = trackModels;
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
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();

        return rowView;
    }

    private static class ViewHolder {
    }


}


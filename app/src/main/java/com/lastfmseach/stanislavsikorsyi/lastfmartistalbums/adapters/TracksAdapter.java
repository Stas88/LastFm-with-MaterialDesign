package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.adapters;

import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.R;
import com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.model.Track;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
            rowView = inflater.inflate(R.layout.tracks_list_item, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView)rowView.findViewById(R.id.track_name);
            viewHolder.duration = (TextView)rowView.findViewById(R.id.track_duration);
            rowView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) rowView.getTag();
        Track track = trackList.get(position);
        String name = track.getName();
        int duration = Integer.valueOf(track.getDuration());
        holder.name.setText(name);
        int seconds = (int) (duration) % 60 ;
        int minutes = (int) ((duration / 60) % 60);
        holder.duration.setText(String.format("%d : %02d",
                minutes,seconds));
        return rowView;
    }

    private static class ViewHolder {
        public TextView name;
        public TextView duration;
    }


}


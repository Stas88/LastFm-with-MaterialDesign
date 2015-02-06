package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.adapters;

import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
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
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {


    private static Context context;
    private final List<IAlbum> albumList;
    private Fragment fragment;
    private ItemClickListener itemClickListener;


    public AlbumAdapter(Context context, List<IAlbum> albumModels,
                        @NonNull ItemClickListener itemClickListener) {
        this.context = context;
        this.albumList = albumModels;
        this.itemClickListener = itemClickListener;
        setHasStableIds(true);

    }

    public interface ItemClickListener {
        public void itemClicked(IAlbum item);
    }

    @Override
    public long getItemId(int position) {
        return albumList.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        Context context = viewGroup.getContext();
        View parent = LayoutInflater.from(context).inflate(R.layout.albums_list_item, viewGroup, false);
        return ViewHolder.newInstance(parent);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final IAlbum item = albumList.get(position);
        viewHolder.setName(item.getName());
        viewHolder.setPlaycount(String.valueOf(item.getPlaycount()));
        viewHolder.setImage(item.getImage().get(0).getText());
        viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.itemClicked(item);
            }
        });
    }

    public int getPositionForId(long id) {
        for (int i = 0; i < albumList.size(); i++) {
            if (albumList.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    public void moveItem(int start, int end) {
        int max = Math.max(start, end);
        int min = Math.min(start, end);
        if (min >= 0 && max < albumList.size()) {
            IAlbum item = albumList.remove(min);
            albumList.add(max, item);
            notifyItemMoved(min, max);
        }
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder{

        private View parent;
        public TextView name;
        public TextView playground;
        public ImageView imageView;

        public static ViewHolder newInstance(View parent) {
            TextView name =  (TextView) parent.findViewById(R.id.track_name);
            TextView playground = (TextView) parent.findViewById(R.id.track_duration);
            ImageView imageView =   (ImageView) parent.findViewById(R.id.album_thumbnail);
            return new ViewHolder(parent, name, playground, imageView);
        }

        private ViewHolder (View parent, TextView name, TextView playground, ImageView imageView) {
            super(parent);
            this.parent = parent;
            this.name = name;
            this.playground = playground;
            this.imageView = imageView;
        }

        public void setName(CharSequence text) {
            name.setText(text);
        }

        public void setImage(CharSequence text) {
            Log.d("ImageSet", "text: " + text.toString());
            String imageUrl = text.toString();
            //Load the album image on a background thread
            if(imageUrl != null) {
                Picasso.with(context)
                        .load(imageUrl).centerCrop().resize(100,100)
                        .into(imageView);
            }
        }

        public void setPlaycount(CharSequence text) {
            playground.setText(text);
        }

        public void setOnClickListener(View.OnClickListener listener) {
            parent.setOnClickListener(listener);
        }
    }
}


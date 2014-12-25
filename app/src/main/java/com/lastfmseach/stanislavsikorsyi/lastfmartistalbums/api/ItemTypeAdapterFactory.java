package com.lastfmseach.stanislavsikorsyi.lastfmartistalbums.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import org.json.JSONArray;

import java.io.IOException;

/**
 * Created by stanislavsikorsyi on 25.12.14.
 */public class ItemTypeAdapterFactory implements TypeAdapterFactory {

    public static final String TAG = "ItemTypeAdapterFactory";

    public <T> TypeAdapter<T> create(Gson gson, final TypeToken<T> type) {

        final TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);
        final TypeAdapter<JsonElement> elementAdapter = gson.getAdapter(JsonElement.class);


        return new TypeAdapter<T>() {

            public void write(JsonWriter out, T value) throws IOException {
                delegate.write(out, value);
            }

            public T read(JsonReader in) throws IOException {

                JsonElement jsonElement = elementAdapter.read(in);

                if (jsonElement.isJsonObject()) {
                    JsonObject objectRoot  = jsonElement.getAsJsonObject();
                    Log.d(TAG, "objectRoot.isJsonObject(): " + objectRoot.isJsonObject());
                    Log.d(TAG, "objectRoot.isJsonNull(): " + objectRoot.isJsonNull());
                    Log.d(TAG, "objectRoot.isJsonArray(): " + objectRoot.isJsonArray());
                    Log.d(TAG, "objectRoot. == null: " + (objectRoot == null));
                    Log.d(TAG, "objectRoot.has(tracks): " + objectRoot.has("tracks"));
                    Log.d(TAG, "objectRoot.has(name): " + objectRoot.has("name"));

                    if (objectRoot.has("album") && objectRoot.get("album").isJsonObject()) {
                        JsonObject album = objectRoot.getAsJsonObject("album");
                        Log.d(TAG, "album.has(tracks): " + album.has("tracks"));
                        Log.d(TAG, "album.has(name): " + album.has("name"));
                        Log.d(TAG, "album.has(duration): " + album.has("duration"));
                        Log.d(TAG, "album.toString() " + album.toString());

                        if (album.has("tracks") && album.get("tracks").isJsonObject()) {
                            JsonObject tracks = album.getAsJsonObject("tracks");
                            Log.d(TAG, "album.has(duration): " + album.has("duration"));
                            Log.d(TAG, "tracks.toString() " + tracks.toString());
                            jsonElement = tracks;
                        }
                    }
                }
                return delegate.fromJsonTree(jsonElement);
            }
        };
    }
}

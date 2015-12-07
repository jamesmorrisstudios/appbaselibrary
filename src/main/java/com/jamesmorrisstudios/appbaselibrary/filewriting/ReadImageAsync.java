package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 10/3/2015.
 */
public class ReadImageAsync extends AsyncTask<Void, Void, Bitmap> {
    private final ImageReadListener listener;
    private final Uri uri;
    private final String path;
    private final FileWriter.FileLocation location;

    public ReadImageAsync(@NonNull String path, @NonNull FileWriter.FileLocation location, @NonNull ImageReadListener listener) {
        this.path = path;
        this.uri = null;
        this.location = location;
        this.listener = listener;
    }

    public ReadImageAsync(@NonNull Uri uri, @NonNull FileWriter.FileLocation location, @NonNull ImageReadListener listener) {
        this.path = null;
        this.uri = uri;
        this.location = location;
        this.listener = listener;
    }

    @Override
    protected Bitmap doInBackground(Void... params) {
        if(uri != null) {
            return FileWriter.readImage(uri, location);
        }
        if(path != null) {
            return FileWriter.readImage(path, location);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap image) {
        listener.readComplete(image);
    }

    public interface ImageReadListener {
        void readComplete(@Nullable Bitmap image);
    }
}

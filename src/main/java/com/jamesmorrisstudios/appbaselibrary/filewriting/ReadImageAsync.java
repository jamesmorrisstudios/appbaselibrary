package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Async task to read an image file
 * <p/>
 * Created by James on 10/3/2015.
 */
public final class ReadImageAsync extends AsyncTask<Void, Void, Bitmap> {
    private final ImageReadListener listener;
    private final Uri uri;
    private final String path;
    private final FileWriter.FileLocation location;

    /**
     * Constructor with a local path
     *
     * @param path     String path
     * @param location Storage location
     * @param listener Callback listener
     */
    public ReadImageAsync(@NonNull String path, @NonNull FileWriter.FileLocation location, @NonNull ImageReadListener listener) {
        this.path = path;
        this.uri = null;
        this.location = location;
        this.listener = listener;
    }

    /**
     * Constructor with a Uri path
     *
     * @param uri      Uri path
     * @param location Storage location
     * @param listener Callback listener
     */
    public ReadImageAsync(@NonNull Uri uri, @NonNull FileWriter.FileLocation location, @NonNull ImageReadListener listener) {
        this.path = null;
        this.uri = uri;
        this.location = location;
        this.listener = listener;
    }

    /**
     * Background task
     *
     * @param params Ignored
     * @return Bitmap that was read
     */
    @Nullable
    @Override
    protected final Bitmap doInBackground(Void... params) {
        if (uri != null) {
            return FileWriter.readImage(uri, location);
        }
        if (path != null) {
            return FileWriter.readImage(path, location);
        }
        return null;
    }

    /**
     * Task complete
     *
     * @param image Bitmap that was read
     */
    @Override
    protected final void onPostExecute(@Nullable Bitmap image) {
        listener.readComplete(image);
    }

    /**
     * Image read listener
     */
    public interface ImageReadListener {

        /**
         * @param image Image that was read
         */
        void readComplete(@Nullable Bitmap image);
    }
}

package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Async task to read a file
 * <p/>
 * Created by James on 10/3/2015.
 */
public final class ReadStringAsync extends AsyncTask<Void, Void, String> {
    private final StringReadListener listener;
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
    public ReadStringAsync(@NonNull final String path, @NonNull final FileWriter.FileLocation location, @NonNull final StringReadListener listener) {
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
    public ReadStringAsync(@NonNull final Uri uri, @NonNull final FileWriter.FileLocation location, @NonNull final StringReadListener listener) {
        this.path = null;
        this.uri = uri;
        this.location = location;
        this.listener = listener;
    }

    /**
     * Background task
     *
     * @param params Ignored
     * @return Byte array
     */
    @Nullable
    @Override
    protected final String doInBackground(final Void... params) {
        if (uri != null) {
            return FileWriter.readString(uri, location);
        }
        if (path != null) {
            return FileWriter.readString(path, location);
        }
        return null;
    }

    /**
     * Task complete
     *
     * @param result Byte array
     */
    @Override
    protected final void onPostExecute(@Nullable String result) {
        listener.readComplete(result);
    }

    /**
     * File read listener
     */
    public interface StringReadListener {

        /**
         * @param data Byte array that was read.
         */
        void readComplete(@Nullable final String data);
    }
}

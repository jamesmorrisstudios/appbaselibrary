package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Async task to write a file
 * <p/>
 * Created by James on 10/3/2015.
 */
public final class WriteFileAsync extends AsyncTask<Void, Void, Uri> {
    private final FileWriteListener listener;
    private final String path;
    private final FileWriter.FileLocation location;
    private final byte[] data;

    /**
     * Constructor with a local path
     *
     * @param path     String path
     * @param data     byte array to write
     * @param location Storage location
     * @param listener Callback listener
     */
    public WriteFileAsync(@NonNull String path, @NonNull byte[] data, @NonNull FileWriter.FileLocation location, @NonNull FileWriteListener listener) {
        this.path = path;
        this.data = data;
        this.location = location;
        this.listener = listener;
    }

    /**
     * Background task
     *
     * @param params Ignored
     * @return Uri of written file
     */
    @Nullable
    @Override
    protected final Uri doInBackground(Void... params) {
        return FileWriter.writeFile(path, data, location);
    }

    /**
     * Task complete
     *
     * @param result Uri of written file
     */
    @Override
    protected final void onPostExecute(@Nullable Uri result) {
        listener.writeComplete(result);
    }

    /**
     * File write listener
     */
    public interface FileWriteListener {

        /**
         * @param filePath Uri of written file
         */
        void writeComplete(@Nullable Uri filePath);
    }
}

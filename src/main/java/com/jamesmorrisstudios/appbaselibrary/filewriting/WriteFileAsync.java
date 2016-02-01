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
    private final boolean append;

    /**
     * Constructor with a local path. Overwrites any data existing in the file.
     *
     * @param path     String path
     * @param data     byte array to write
     * @param location Storage location
     * @param listener Callback listener
     */
    public WriteFileAsync(@NonNull final String path, @NonNull final byte[] data, @NonNull final FileWriter.FileLocation location, @NonNull final FileWriteListener listener) {
        this.path = path;
        this.data = data;
        this.location = location;
        this.listener = listener;
        this.append = false;
    }

    /**
     * Constructor with a local path
     *
     * @param path     String path
     * @param data     byte array to write
     * @param location Storage location
     * @param listener Callback listener
     * @param append   true to append data, false to overwrite
     */
    public WriteFileAsync(@NonNull final String path, @NonNull final byte[] data, @NonNull final FileWriter.FileLocation location, @NonNull final FileWriteListener listener, final boolean append) {
        this.path = path;
        this.data = data;
        this.location = location;
        this.listener = listener;
        this.append = append;
    }

    /**
     * Background task
     *
     * @param params Ignored
     * @return Uri of written file
     */
    @Nullable
    @Override
    protected final Uri doInBackground(final Void... params) {
        if (append) {
            return FileWriter.appendFile(path, data, location);
        } else {
            return FileWriter.writeFile(path, data, location);
        }
    }

    /**
     * Task complete
     *
     * @param result Uri of written file
     */
    @Override
    protected final void onPostExecute(@Nullable final Uri result) {
        listener.writeComplete(result);
    }

    /**
     * File write listener
     */
    public interface FileWriteListener {

        /**
         * @param filePath Uri of written file
         */
        void writeComplete(@Nullable final Uri filePath);
    }
}

package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Async task to write an image file
 * <p/>
 * Created by James on 10/3/2015.
 */
public final class WriteImageAsync extends AsyncTask<Void, Void, Uri> {
    private final ImageWriteListener listener;
    private final String path;
    private final FileWriter.FileLocation location;
    private final Bitmap data;

    /**
     * Constructor with a local path
     *
     * @param path     String path
     * @param data     Bitmap to write
     * @param location Storage location
     * @param listener Callback listener
     */
    public WriteImageAsync(@NonNull String path, @NonNull Bitmap data, @NonNull FileWriter.FileLocation location, @NonNull ImageWriteListener listener) {
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
        return FileWriter.writeImage(path, data, location);
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
     * Image write listener
     */
    public interface ImageWriteListener {

        /**
         * @param filePath Uri of written image file
         */
        void writeComplete(@Nullable Uri filePath);
    }
}

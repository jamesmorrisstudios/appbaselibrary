package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 10/3/2015.
 */
public class WriteImageAsync extends AsyncTask<Void, Void, Uri> {
    private final ImageWriteListener listener;
    private final String path;
    private final FileWriter.FileLocation location;
    private final Bitmap data;

    public WriteImageAsync(@NonNull String path, @NonNull Bitmap data, @NonNull FileWriter.FileLocation location, @NonNull ImageWriteListener listener) {
        this.path = path;
        this.data = data;
        this.location = location;
        this.listener = listener;
    }

    @Override
    protected Uri doInBackground(Void... params) {
        return FileWriter.writeImage(path, data, location);
    }

    @Override
    protected void onPostExecute(Uri result) {
        listener.writeComplete(FileWriter.getFileUri(path, location));
    }

    public interface ImageWriteListener {
        void writeComplete(@Nullable Uri filePath);
    }
}

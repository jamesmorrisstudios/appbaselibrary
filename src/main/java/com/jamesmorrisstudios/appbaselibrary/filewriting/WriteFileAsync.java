package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 10/3/2015.
 */
public class WriteFileAsync extends AsyncTask<Void, Void, Uri> {
    private final FileWriteListener listener;
    private final String path;
    private final FileWriter.FileLocation location;
    private final byte[] data;

    public WriteFileAsync(@NonNull String path, @NonNull byte[] data, @NonNull FileWriter.FileLocation location, @NonNull FileWriteListener listener) {
        this.path = path;
        this.data = data;
        this.location = location;
        this.listener = listener;
    }

    @Override
    protected Uri doInBackground(Void... params) {
        return FileWriter.writeFile(path, data, location);
    }

    @Override
    protected void onPostExecute(Uri result) {
        listener.writeComplete(FileWriter.getFileUri(path, location));
    }

    public interface FileWriteListener {
        void writeComplete(@Nullable Uri filePath);
    }
}

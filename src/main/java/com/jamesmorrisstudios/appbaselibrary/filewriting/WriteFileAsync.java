package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by James on 10/3/2015.
 */
public class WriteFileAsync extends AsyncTask<Void, Void, Boolean> {
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
    protected Boolean doInBackground(Void... params) {
        return FileWriter.writeFile(path, data, location);
    }

    @Override
    protected void onPostExecute(Boolean result) {
        listener.writeComplete(result);
    }

    public interface FileWriteListener {
        void writeComplete(boolean success);
    }
}

package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by James on 10/3/2015.
 */
public class ReadFileAsync extends AsyncTask<Void, Void, byte[]> {
    private final FileReadListener listener;
    private final Uri uri;
    private final String path;
    private final FileWriter.FileLocation location;

    public ReadFileAsync(@NonNull String path, @NonNull FileWriter.FileLocation location, @NonNull FileReadListener listener) {
        this.path = path;
        this.uri = null;
        this.location = location;
        this.listener = listener;
    }

    public ReadFileAsync(@NonNull Uri uri, @NonNull FileWriter.FileLocation location, @NonNull FileReadListener listener) {
        this.path = null;
        this.uri = uri;
        this.location = location;
        this.listener = listener;
    }

    @Override
    protected byte[] doInBackground(Void... params) {
        if(path == null) {
            return FileWriter.readFile(uri, location);
        }
        return FileWriter.readFile(path, location);
    }

    @Override
    protected void onPostExecute(byte[] result) {
        listener.readComplete(result);
    }

    public interface FileReadListener {
        void readComplete(byte[] data);
    }
}

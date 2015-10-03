package com.jamesmorrisstudios.appbaselibrary.filewriting;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

/**
 * Created by James on 10/3/2015.
 */
public class ReadFileAsync extends AsyncTask<Void, Void, byte[]> {
    private final FileReadListener listener;
    private final String path;
    private final FileWriter.FileLocation location;

    public ReadFileAsync(@NonNull String path, @NonNull FileWriter.FileLocation location, @NonNull FileReadListener listener) {
        this.path = path;
        this.location = location;
        this.listener = listener;
    }

    @Override
    protected byte[] doInBackground(Void... params) {
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

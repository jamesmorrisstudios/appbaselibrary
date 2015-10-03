package com.jamesmorrisstudios.appbaselibrary.dialogHelper;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by James on 10/2/2015.
 */
public class FileBrowserRequest {
    public final DirType dirType;
    public final FileBrowserRequestListener fileBrowserRequestListener;
    public final boolean allowCreateDir;
    public final String extension;

    public FileBrowserRequest(@NonNull DirType dirType, boolean allowCreateDir, @Nullable String extension, @NonNull FileBrowserRequestListener fileBrowserRequestListener) {
        this.dirType = dirType;
        this.allowCreateDir = allowCreateDir;
        this.fileBrowserRequestListener = fileBrowserRequestListener;
        this.extension = extension;
    }

    public enum DirType {
        FILE, DIRECTORY
    }

    public interface FileBrowserRequestListener {
        void path(@Nullable Uri path);
    }

}

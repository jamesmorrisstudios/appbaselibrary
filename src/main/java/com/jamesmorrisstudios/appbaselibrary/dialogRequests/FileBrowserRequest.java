package com.jamesmorrisstudios.appbaselibrary.dialogRequests;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Request to build a file browser dialog.
 * Call execute to display the dialog
 * <p/>
 * Created by James on 10/2/2015.
 */
public final class FileBrowserRequest extends AbstractDialogRequest {
    public final DirType dirType;
    public final OnFileBrowserRequestListener onFileBrowserRequestListener;
    public final boolean allowCreateDir;
    public final String extension;

    /**
     * @param dirType                      Directory type
     * @param allowCreateDir               Allow the user to create a new directory
     * @param extension                    Allowed file extensions to sort on comma separated in form of ".png,.jpg". Null will execute all files.
     * @param onFileBrowserRequestListener Listener
     */
    public FileBrowserRequest(@NonNull DirType dirType, boolean allowCreateDir, @Nullable String extension, @NonNull OnFileBrowserRequestListener onFileBrowserRequestListener) {
        this.dirType = dirType;
        this.allowCreateDir = allowCreateDir;
        this.onFileBrowserRequestListener = onFileBrowserRequestListener;
        this.extension = extension;
    }

    /**
     * Possible selection types. File or directory.
     */
    public enum DirType {
        FILE, DIRECTORY
    }

    /**
     * Listener
     */
    public interface OnFileBrowserRequestListener {

        /**
         * @param path Path to the selected item
         */
        void itemSelected(@Nullable Uri path);

        /**
         * No item selected
         */
        void canceled();
    }

}

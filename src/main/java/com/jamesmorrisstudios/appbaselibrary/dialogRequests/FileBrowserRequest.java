package com.jamesmorrisstudios.appbaselibrary.dialogRequests;


import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;

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
    public final boolean allowMultiSelect;
    public final String extension;

    /**
     * @param dirType                      Directory type
     * @param allowCreateDir               Allow the user to create a new directory
     * @param extension                    Allowed file extensions to sort on comma separated in form of ".png,.jpg". Null will execute all files.
     * @param onFileBrowserRequestListener Listener
     */
    public FileBrowserRequest(@NonNull final DirType dirType, final boolean allowCreateDir, final boolean allowMultiSelect, @Nullable final String extension, @NonNull final OnFileBrowserRequestListener onFileBrowserRequestListener) {
        this.dirType = dirType;
        this.allowCreateDir = allowCreateDir;
        this.allowMultiSelect = allowMultiSelect;
        this.extension = extension;
        this.onFileBrowserRequestListener = onFileBrowserRequestListener;
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
        void itemSelected(@Nullable final Uri path);

        /**
         * @param path Path to the selected items
         */
        void multiItemSelected(@Nullable final ArrayList<Uri> path);

        /**
         * No item selected
         */
        void canceled();
    }

}

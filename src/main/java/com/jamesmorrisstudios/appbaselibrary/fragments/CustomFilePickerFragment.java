package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment;
import com.nononsenseapps.filepicker.FilePickerFragment;

import java.io.File;

/**
 * Created by James on 10/2/2015.
 */
public class CustomFilePickerFragment extends FilePickerFragment {
    // File extension to filter on
    private String extension = null;

    public final void setExtension(@Nullable String extension) {
        this.extension = extension;
    }

    /**
     * For consistency, the top level the back button checks against should be the start path.
     * But it will fall back on /.
     */
    public File getBackTop() {
        if (getArguments().containsKey(KEY_START_PATH)) {
            return getPath(getArguments().getString(KEY_START_PATH));
        } else {
            return new File("/");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (R.id.nnf_action_createdir == menuItem.getItemId()) {
            Activity activity = getActivity();
            if (activity instanceof AppCompatActivity) {
                CustomNewFolderFragment.showDialog(((AppCompatActivity) activity).getSupportFragmentManager(), CustomFilePickerFragment.this);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @return true if the current path is the startpath or /
     */
    public boolean isBackTop() {
        return 0 == compareFiles(mCurrentPath, getBackTop()) || 0 == compareFiles(mCurrentPath, new File("/"));
    }

    /**
     * Go up on level, same as pressing on "..".
     */
    public void goUp() {
        mCurrentPath = getParent(mCurrentPath);
        mCheckedItems.clear();
        mCheckedVisibleViewHolders.clear();
        refresh();
    }

    /**
     *
     * @param file
     * @return The file extension. If file has no extension, it returns null.
     */
    private String getExtension(@NonNull File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0) {
            return null;
        } else {
            return path.substring(i);
        }
    }

    @Override
    protected boolean isItemVisible(final File file) {
        if (!isDir(file) && (mode == MODE_FILE || mode == MODE_FILE_AND_DIR)) {
            return extension == null || extension.equalsIgnoreCase(getExtension(file));
        }
        return isDir(file);
    }

}

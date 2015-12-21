package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.nononsenseapps.filepicker.FilePickerFragment;

import java.io.File;

/**
 * Extension of the file picker fragment to add in file extension filtering
 * <p/>
 * Created by James on 10/2/2015.
 */
public final class CustomFilePickerFragment extends FilePickerFragment {
    private String[] extensions = null;

    /**
     * @param extensions Allowed file extensions. Null for all
     */
    public final void setExtension(@Nullable String[] extensions) {
        this.extensions = extensions;
    }

    /**
     * For consistency, the top level the back button checks against should be the start itemSelected.
     * But it will fall back on /.
     */
    @NonNull
    public File getBackTop() {
        if (getArguments().containsKey(KEY_START_PATH)) {
            return getPath(getArguments().getString(KEY_START_PATH));
        } else {
            return new File("/");
        }
    }

    /**
     * @param menuItem Menu Item
     * @return True if consumed action
     */
    @Override
    public final boolean onOptionsItemSelected(@NonNull MenuItem menuItem) {
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
     * @return true if the current itemSelected is the startpath or /
     */
    public final boolean isBackTop() {
        return 0 == compareFiles(mCurrentPath, getBackTop()) || 0 == compareFiles(mCurrentPath, new File("/"));
    }

    /**
     * Go up on level, same as pressing on "..".
     */
    public final void goUp() {
        mCurrentPath = getParent(mCurrentPath);
        mCheckedItems.clear();
        mCheckedVisibleViewHolders.clear();
        refresh();
    }

    /**
     * @param file File to check
     * @return The file extension. If file has no extension, it returns null.
     */
    @Nullable
    private String getExtension(@NonNull File file) {
        String path = file.getPath();
        int i = path.lastIndexOf(".");
        if (i < 0) {
            return null;
        } else {
            return path.substring(i);
        }
    }

    /**
     * @param file File to check
     * @return True if dir or allowed file extension
     */
    @Override
    protected final boolean isItemVisible(@NonNull final File file) {
        if (!isDir(file) && (mode == MODE_FILE || mode == MODE_FILE_AND_DIR)) {
            if (extensions == null) {
                return true;
            }
            for (String extension : extensions) {
                if (extension.equalsIgnoreCase(getExtension(file))) {
                    return true;
                }
            }
            return false;
        }
        return isDir(file);
    }

}

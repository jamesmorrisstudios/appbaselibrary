package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

/**
 * Customized new folder fragment from the file browser.
 * Ensures that dialogs are styled properly
 * <p/>
 * Created by James on 11/12/2015.
 */
public final class CustomNewFolderFragment extends CustomNewItemFragment {
    private static final String TAG = "new_folder_fragment";

    /**
     * Show the dialog
     *
     * @param fm       Fragment Manager
     * @param listener Listener
     */
    public static void showDialog(@NonNull final FragmentManager fm, @NonNull final OnNewFolderListener listener) {
        CustomNewItemFragment d = new CustomNewFolderFragment();
        d.setListener(listener);
        d.show(fm, TAG);
    }

    /**
     * @param itemName directory name
     * @return True if valid directory name
     */
    @Override
    protected boolean validateName(@NonNull final String itemName) {
        return !TextUtils.isEmpty(itemName)
                && !itemName.contains("/")
                && !itemName.equals(".")
                && !itemName.equals("..");
    }

}

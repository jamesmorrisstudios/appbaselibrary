package com.jamesmorrisstudios.appbaselibrary.fragments;

import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

/**
 * Created by James on 11/12/2015.
 */
public class CustomNewFolderFragment extends CustomNewItemFragment  {
    private static final String TAG = "new_folder_fragment";

    public static void showDialog(final FragmentManager fm, final OnNewFolderListener listener) {
        CustomNewItemFragment d = new CustomNewFolderFragment();
        d.setListener(listener);
        d.show(fm, TAG);
    }

    @Override
    protected boolean validateName(final String itemName) {
        return !TextUtils.isEmpty(itemName)
                && !itemName.contains("/")
                && !itemName.equals(".")
                && !itemName.equals("..");
    }

}

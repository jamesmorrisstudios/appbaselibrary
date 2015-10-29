package com.jamesmorrisstudios.appbaselibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.fragments.CustomFilePickerFragment;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;

/**
 * Created by James on 10/2/2015.
 */
public class CustomFilePickerActivity extends FilePickerActivity {
    public static final String EXTRA_EXTENSION = "nononsense.intent" + ".EXTENSION";
    public static final String EXTRA_THEME = "nononsense.intent" + ".THEME";

    protected String extension = null;
    protected int theme = -1;

    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            Log.v("FilePicker", "Intent");
            //if(intent.hasExtra("EXTRA_EXTENSION")) {
                extension = intent.getStringExtra(EXTRA_EXTENSION);
            //}
            //if(intent.hasExtra("EXTRA_THEME")) {
                Log.v("FilePicker", "get Theme");
                theme = intent.getIntExtra(EXTRA_THEME, -1);
            //}
        }
        if(theme != -1) {
            Log.v("FilePicker", "Set Theme");
            setTheme(theme);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * Need access to the fragment
     */
    CustomFilePickerFragment currentFragment;

    /**
     * Return a copy of the new fragment and set the variable above.
     */
    @Override
    protected AbstractFilePickerFragment<File> getFragment(
            final String startPath, final int mode, final boolean allowMultiple,
            final boolean allowCreateDir) {
        currentFragment = new CustomFilePickerFragment();
        currentFragment.setExtension(extension);
        // startPath is allowed to be null. In that case, default folder should be SD-card and not "/"
        currentFragment.setArgs(startPath != null ? startPath : Environment.getExternalStorageDirectory().getPath(),
                mode, allowMultiple, allowCreateDir);
        return currentFragment;
    }

    /**
     * Override the back-button.
     */
    @Override
    public void onBackPressed() {
        // If at top most level, normal behaviour
        if (currentFragment.isBackTop()) {
            super.onBackPressed();
        } else {
            // Else go up
            currentFragment.goUp();
        }
    }

}

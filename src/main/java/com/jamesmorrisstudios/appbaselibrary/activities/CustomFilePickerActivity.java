package com.jamesmorrisstudios.appbaselibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

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
    public static final String EXTRA_THEME_PRIMARY = "nononsense.intent" + ".THEME_PRIMARY";
    public static final String EXTRA_THEME_ACCENT = "nononsense.intent" + ".THEME_ACCENT";

    protected String extension = null;
    protected int theme = -1, themePrimary = -1, themeAccent = -1;

    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            Log.v("FilePicker", "Intent");
            extension = intent.getStringExtra(EXTRA_EXTENSION);
            theme = intent.getIntExtra(EXTRA_THEME, -1);
            themePrimary = intent.getIntExtra(EXTRA_THEME_PRIMARY, -1);
            themeAccent = intent.getIntExtra(EXTRA_THEME_ACCENT, -1);
        }
        if(theme != -1) {
            setTheme(theme);
        }
        if(themePrimary != -1) {
            setTheme(themePrimary);
        }
        if(themeAccent != -1) {
            setTheme(themeAccent);
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
        if(extension != null) {
            String[] extensions = extension.split(",");
            currentFragment.setExtension(extensions);
        }
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

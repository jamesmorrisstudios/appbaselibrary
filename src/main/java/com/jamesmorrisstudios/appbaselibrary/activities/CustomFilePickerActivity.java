package com.jamesmorrisstudios.appbaselibrary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jamesmorrisstudios.appbaselibrary.fragments.CustomFilePickerFragment;
import com.nononsenseapps.filepicker.AbstractFilePickerFragment;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;

/**
 * Custom File Picker that adds theme and extension support
 * <p/>
 * Created by James on 10/2/2015.
 */
public final class CustomFilePickerActivity extends FilePickerActivity {
    public static final String EXTRA_EXTENSION = "nononsense.intent" + ".EXTENSION";
    public static final String EXTRA_THEME = "nononsense.intent" + ".THEME";
    public static final String EXTRA_THEME_PRIMARY = "nononsense.intent" + ".THEME_PRIMARY";
    public static final String EXTRA_THEME_ACCENT = "nononsense.intent" + ".THEME_ACCENT";
    protected String extension = null;
    protected int theme = -1, themePrimary = -1, themeAccent = -1;
    private CustomFilePickerFragment currentFragment;

    /**
     * Create this activity
     *
     * @param savedInstanceState Saved instance state
     */
    protected final void onCreate(@Nullable Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            extension = intent.getStringExtra(EXTRA_EXTENSION);
            theme = intent.getIntExtra(EXTRA_THEME, -1);
            themePrimary = intent.getIntExtra(EXTRA_THEME_PRIMARY, -1);
            themeAccent = intent.getIntExtra(EXTRA_THEME_ACCENT, -1);
        }
        if (theme != -1) {
            setTheme(theme);
        }
        if (themePrimary != -1) {
            setTheme(themePrimary);
        }
        if (themeAccent != -1) {
            setTheme(themeAccent);
        }
        super.onCreate(savedInstanceState);
    }

    /**
     * Return a copy of the new fragment and set the variable above.
     *
     * @param startPath      Starting path
     * @param mode           Directory or File mode
     * @param allowMultiple  Allow multi selection (not currently supported)
     * @param allowCreateDir Allow the user to create a new directory
     * @return Returns the file picker fragment
     */
    @Override
    @NonNull
    protected final AbstractFilePickerFragment<File> getFragment(final String startPath, final int mode, final boolean allowMultiple, final boolean allowCreateDir) {
        currentFragment = new CustomFilePickerFragment();
        if (extension != null) {
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
    public final void onBackPressed() {
        // If at top most level, normal behaviour
        if (currentFragment.isBackTop()) {
            super.onBackPressed();
        } else {
            // Else go up
            currentFragment.goUp();
        }
    }

}

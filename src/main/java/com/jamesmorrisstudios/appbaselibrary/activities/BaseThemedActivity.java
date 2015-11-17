package com.jamesmorrisstudios.appbaselibrary.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.ThemeManager;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;

import java.util.Locale;

/**
 * Created by James on 11/14/2015.
 */
public abstract class BaseThemedActivity extends AppCompatActivity {

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        applyTheme();
        setLocale();
        updateImmersiveMode(true);
        super.onCreate(savedInstanceState);
    }

    protected final void applyTheme() {
        AppBase.getInstance().applyTheme();
        setTheme(ThemeManager.getAppStyle());
        setTheme(ThemeManager.getToolbarStyle());
        setTheme(ThemeManager.getAccentColorStyle());
        setTheme(ThemeManager.getPrimaryColorStyle());
    }

    protected final void setLocale() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_language);

        switch(Prefs.getInt(pref, key, 0)) {
            case 0: //Automatic so restore to original
                Utils.restoreLocale();
                break;
            case 1: //English
                Utils.setLocale(Locale.US);
                break;
        }
    }

    /**
     * @param hasFocus
     */
    protected final void updateImmersiveMode(boolean hasFocus) {
        int newUiOptions = 0;
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_immersive);
        if (Prefs.getBoolean(pref, key, false)) {
            if (hasFocus) {
                if (Build.VERSION.SDK_INT >= 16) {
                    newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                }
                if (Build.VERSION.SDK_INT >= 19) {
                    newUiOptions |= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
                }
                getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
            }
        } else {
            if (hasFocus) {
                if (Build.VERSION.SDK_INT >= 16) {
                    newUiOptions |= View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                }
                getWindow().getDecorView().setSystemUiVisibility(newUiOptions);
            }
        }
    }

}

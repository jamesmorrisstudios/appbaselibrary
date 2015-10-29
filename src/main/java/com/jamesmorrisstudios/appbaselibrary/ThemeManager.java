package com.jamesmorrisstudios.appbaselibrary;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;

/**
 * Created by James on 10/29/2015.
 */
public class ThemeManager {

    public enum AppTheme {
        DARK, LIGHT
    }

    public static AppTheme getAppTheme() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_theme);

        switch(Prefs.getInt(pref, key, 0)) {
            case 0: //Light
                return AppTheme.LIGHT;
            case 1: //Dark
                return AppTheme.DARK;
            default:
                return AppTheme.LIGHT;
        }
    }

}

package com.jamesmorrisstudios.appbaselibrary;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;

/**
 * Created by James on 12/5/2015.
 */
public class AppBaseUtils {

    public static boolean useCustomRingtonePicker() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_custom_ringtone);
        return Prefs.getBoolean(pref, key, true);
    }

}

package com.jamesmorrisstudios.appbaselibrary;

import android.content.res.Configuration;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

import java.util.Locale;

/**
 * User Locale functions
 * <p/>
 * Created by James on 12/8/2015.
 */
public final class UtilsLocale {
    private static Locale backupLocale = null;

    /**
     * Restores to the previous locale if one is set
     */
    public static void restoreLocale() {
        if (backupLocale != null) {
            setLocale(backupLocale);
        }
    }

    /**
     * @param localeName Locale name to set to
     */
    public static void setLocale(@NonNull final String localeName) {
        Locale locale = new Locale(localeName);
        setLocale(locale);
    }

    /**
     * @param locale Locale to set to
     */
    public static void setLocale(@NonNull final Locale locale) {
        if (backupLocale == null) {
            backupLocale = Locale.getDefault();
        }
        Locale.setDefault(locale);
        Configuration config2 = new Configuration();
        config2.locale = locale;
        AppBase.getContext().getResources().updateConfiguration(config2, AppBase.getContext().getResources().getDisplayMetrics());
    }

}

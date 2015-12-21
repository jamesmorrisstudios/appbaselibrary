package com.jamesmorrisstudios.appbaselibrary;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;
import com.jamesmorrisstudios.appbaselibrary.time.UtilsTime;

import java.util.Locale;

/**
 * Utility functions related to app settings
 * <p/>
 * Created by James on 12/5/2015.
 */
public final class UtilsAppBase {

    /**
     * @return True if custom ringtone picker is enabled
     */
    public static boolean useCustomRingtonePicker() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_custom_ringtone);
        return Prefs.getBoolean(pref, key, true);
    }

    /**
     * @return True if immersive mode is enabled
     */
    public static boolean useImmersiveMode() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_immersive);
        return Prefs.getBoolean(pref, key, false);
    }

    /**
     * Applies the users chosen locale
     */
    public static void applyLocale() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_language);
        switch (Prefs.getInt(pref, key, 0)) {
            case 0: //Automatic so restore to original
                UtilsLocale.restoreLocale();
                break;
            case 1: //English
                UtilsLocale.setLocale(Locale.US);
                break;
        }
    }

    /**
     * Sets the first day of the week to the user chosen day. Or leaves automatic as needed.
     */
    public static void applyFirstDayOfWeek() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        String key = AppBase.getContext().getString(R.string.pref_notification_first_day);
        switch (Prefs.getInt(pref, key, 0)) {
            case 0:
                UtilsTime.setFirstDayOfWeek(UtilsTime.DayOfWeek.AUTOMATIC);
                break;
            case 1:
                UtilsTime.setFirstDayOfWeek(UtilsTime.DayOfWeek.SUNDAY);
                break;
            case 2:
                UtilsTime.setFirstDayOfWeek(UtilsTime.DayOfWeek.MONDAY);
                break;
            case 3:
                UtilsTime.setFirstDayOfWeek(UtilsTime.DayOfWeek.TUESDAY);
                break;
            case 4:
                UtilsTime.setFirstDayOfWeek(UtilsTime.DayOfWeek.WEDNESDAY);
                break;
            case 5:
                UtilsTime.setFirstDayOfWeek(UtilsTime.DayOfWeek.THURSDAY);
                break;
            case 6:
                UtilsTime.setFirstDayOfWeek(UtilsTime.DayOfWeek.FRIDAY);
                break;
            case 7:
                UtilsTime.setFirstDayOfWeek(UtilsTime.DayOfWeek.SATURDAY);
                break;
        }
    }

    /**
     * @return True if first launch
     */
    public static boolean isFirstLaunch() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        return Prefs.getBoolean(pref, "APP_FIRST_LAUNCH", true);
    }

    /**
     * Commits first launch as completed
     */
    public static void setFirstLaunchComplete() {
        String pref = AppBase.getContext().getString(R.string.settings_pref);
        Prefs.putBoolean(pref, "APP_FIRST_LAUNCH", false);
    }

}

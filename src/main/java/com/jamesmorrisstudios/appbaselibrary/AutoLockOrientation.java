package com.jamesmorrisstudios.appbaselibrary;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

/**
 * Manages locking and unlocking device orientation in some cases.
 * <p/>
 * Created by James on 12/5/2015.
 */
public final class AutoLockOrientation {
    private static boolean useAutoLock = false;

    /**
     * Locks the orientation if not already locked
     *
     * @param activity Activity to lock
     */
    public static void enableAutoLock(@NonNull final AppCompatActivity activity) {
        if (useAutoLock) {
            return;
        }
        if (UtilsDisplay.getOrientationLock(activity) == UtilsDisplay.Orientation.UNDEFINED) {
            UtilsDisplay.lockOrientationCurrent(activity);
            useAutoLock = true;
        } else {
            useAutoLock = false;
        }
    }

    /**
     * Unlocks orientation if previously locked by enableAutoLock
     *
     * @param activity Activity to unlock
     */
    public static void disableAutoLock(@NonNull final AppCompatActivity activity) {
        if (useAutoLock) {
            UtilsDisplay.unlockOrientation(activity);
            useAutoLock = false;
        }
    }

}

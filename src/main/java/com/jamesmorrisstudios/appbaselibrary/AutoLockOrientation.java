package com.jamesmorrisstudios.appbaselibrary;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by James on 12/5/2015.
 */
public class AutoLockOrientation {

    private static boolean useAutoLock = false;

    public static void enableAutoLock(AppCompatActivity activity) {
        if(useAutoLock) {
            return;
        }
        if(Utils.getOrientationLock(activity) == Utils.Orientation.UNDEFINED) {
            Utils.lockOrientationCurrent(activity);
            useAutoLock = true;
        } else {
            useAutoLock = false;
        }
    }

    public static void disableAutoLock(AppCompatActivity activity) {
        if(useAutoLock) {
            Utils.unlockOrientation(activity);
            useAutoLock = false;
        }
    }


}

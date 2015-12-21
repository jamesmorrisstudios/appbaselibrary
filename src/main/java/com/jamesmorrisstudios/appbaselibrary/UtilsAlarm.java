package com.jamesmorrisstudios.appbaselibrary;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

/**
 * Alarm handling utilities
 * <p/>
 * Created by James on 12/17/2015.
 */
public final class UtilsAlarm {

    /**
     * @return The alarm manager
     */
    @NonNull
    private static AlarmManager getAlarmManager() {
        return (AlarmManager) AppBase.getContext().getSystemService(Context.ALARM_SERVICE);
    }

    /**
     * Schedule the alarm with the proper alarm manager call for this android version
     *
     * @param wakeTime Wake time in milliseconds
     * @param intent   Intent to send
     */
    public static void setExact(final long wakeTime, @NonNull final Intent intent) {
        setExact(wakeTime, PendingIntent.getBroadcast(AppBase.getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    /**
     * Schedule the alarm with the proper alarm manager call for this android version
     *
     * @param wakeTime    Wake time in milliseconds
     * @param intent      Intent to send
     * @param requestCode Request code
     */
    public static void setExact(final long wakeTime, @NonNull final Intent intent, final int requestCode) {
        setExact(wakeTime, PendingIntent.getBroadcast(AppBase.getContext(), requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    /**
     * Schedule the alarm with the proper alarm manager call for this android version
     *
     * @param wakeTime Wake time in milliseconds
     * @param pi       Pending intent
     */
    public static void setExact(final long wakeTime, @NonNull final PendingIntent pi) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getAlarmManager().setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, wakeTime, pi);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getAlarmManager().setExact(AlarmManager.RTC_WAKEUP, wakeTime, pi);
        } else {
            getAlarmManager().set(AlarmManager.RTC_WAKEUP, wakeTime, pi);
        }
    }

    /**
     * Cancel the given alarm
     *
     * @param intent Intent
     */
    public static void cancel(@NonNull final Intent intent) {
        cancel(intent, 0);
    }

    /**
     * Cancel the given alarm
     *
     * @param intent      Intent
     * @param requestCode Request code
     */
    public static void cancel(@NonNull final Intent intent, final int requestCode) {
        cancel(PendingIntent.getBroadcast(AppBase.getContext(), requestCode, intent, 0));
    }

    /**
     * Cancel the given alarm
     *
     * @param pi Pending intent
     */
    public static void cancel(@NonNull final PendingIntent pi) {
        getAlarmManager().cancel(pi);
    }

}

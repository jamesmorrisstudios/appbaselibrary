package com.jamesmorrisstudios.appbaselibrary;

import android.app.PendingIntent;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

/**
 * TODO in progress
 * <p/>
 * Location handling utilities
 * <p/>
 * Created by James on 12/17/2015.
 */
public final class UtilsLocation {

    @NonNull
    private static LocationManager getLocationManager() {
        return (LocationManager) AppBase.getContext().getSystemService(Context.LOCATION_SERVICE);
    }

    public static void addProximityAlert() {

    }

    public static void removeProximityAlert(@NonNull final PendingIntent pi) {

    }

    public static void requestUpdates(final long minTime, final float minDistance, @NonNull final Criteria criteria, @NonNull final PendingIntent intent) {

    }

    public static void removeUpdates(@NonNull final PendingIntent intent) {

    }


    @NonNull
    public static Location getLastKnownLocation() {
        return null;
    }


}

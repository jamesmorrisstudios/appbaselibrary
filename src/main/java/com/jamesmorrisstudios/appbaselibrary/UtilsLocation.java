package com.jamesmorrisstudios.appbaselibrary;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

/**
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

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public static void addProximityAlert(final double latitude, final double longitude, final float radius, @NonNull final Intent intent) {
        addProximityAlert(latitude, longitude, radius, PendingIntent.getBroadcast(AppBase.getContext(), 50, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public static void addProximityAlert(final double latitude, final double longitude, final float radius, @NonNull final PendingIntent pi) {
        getLocationManager().addProximityAlert(latitude, longitude, radius, -1, pi);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public static void removeProximityAlert(@NonNull final Intent intent) {
        removeProximityAlert(PendingIntent.getBroadcast(AppBase.getContext(), 50, intent, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public static void removeProximityAlert(@NonNull final PendingIntent pi) {
        getLocationManager().removeProximityAlert(pi);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public static void requestUpdates(final long minTime, final float minDistance, @NonNull final Criteria criteria, @NonNull final PendingIntent pi) {
        getLocationManager().requestLocationUpdates(minTime, minDistance, criteria, pi);
    }

    @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public static void removeUpdates(@NonNull final PendingIntent pi) {
        getLocationManager().removeUpdates(pi);
    }

    @NonNull
    @RequiresPermission(allOf = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION})
    public static Location getLastKnownLocation() {
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
        criteria.setCostAllowed(false);
        criteria.setSpeedRequired(false);
        return getLocationManager().getLastKnownLocation(getLocationManager().getBestProvider(criteria, true));
    }

}

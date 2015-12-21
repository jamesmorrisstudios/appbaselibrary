package com.jamesmorrisstudios.appbaselibrary;

import android.Manifest;
import android.content.Context;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.items.RingtoneItem;

import java.util.ArrayList;

/**
 * Utility functions for ringtones
 * <p/>
 * Created by James on 12/8/2015.
 */
public final class UtilsRingtone {
    private static Vibrator vibrator = (Vibrator) AppBase.getContext().getSystemService(Context.VIBRATOR_SERVICE);
    private static Ringtone ringtone = null;

    /**
     * Vibrate the device
     *
     * @param pattern Vibrate pattern
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void vibrate(@NonNull final long[] pattern) {
        vibrator.vibrate(pattern, -1);
    }

    /**
     * Cancel any current vibrate
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void vibrateCancel() {
        vibrator.cancel();
    }

    /**
     * Play the given ringtone
     *
     * @param tone Uri to ringtone
     */
    public static void ringtonePlay(@NonNull final Uri tone) {
        try {
            ringtone = RingtoneManager.getRingtone(AppBase.getContext(), tone);
            ringtone.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Cancel ringtone playback
     */
    public static void ringtoneCancel() {
        if (ringtone != null) {
            ringtone.stop();
        }
    }

    /**
     * Get list of all ringtones on device of specified type
     *
     * @param type Ringtone type
     * @return List of all ringtone items
     */
    @NonNull
    public static ArrayList<RingtoneItem> getRingtones(@NonNull final RingtoneType type) {
        ArrayList<RingtoneItem> ringtoneItems = new ArrayList<>();
        RingtoneManager manager = new RingtoneManager(AppBase.getContext());
        if (type == RingtoneType.NOTIFICATION) {
            manager.setType(RingtoneManager.TYPE_NOTIFICATION);
        } else if (type == RingtoneType.RINGTONE) {
            manager.setType(RingtoneManager.TYPE_RINGTONE);
        }
        Cursor cursor = manager.getCursor();
        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            //String uri = cursor.getString(RingtoneManager.URI_COLUMN_INDEX);
            Uri ringtoneURI = manager.getRingtoneUri(cursor.getPosition());
            ringtoneItems.add(new RingtoneItem(type, title, ringtoneURI));
        }
        return ringtoneItems;
    }

    /**
     * Ringtone types
     */
    public enum RingtoneType {
        NOTIFICATION, RINGTONE
    }

}

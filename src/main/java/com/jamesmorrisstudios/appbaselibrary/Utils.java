/*
 * Copyright (c) 2015.   James Morris Studios
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jamesmorrisstudios.appbaselibrary;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Toast;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.math.vectors.IntVector2;
import com.jamesmorrisstudios.appbaselibrary.time.UtilsTime;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Utility and Constant values
 * <p/>
 * Created by James on 9/16/2014.
 */
public final class Utils {
    public static final String stringType = "UTF-8";

    /**
     * Gets the screensize as an intVector
     * This should work with all devices android 4.0+
     *
     * @return The screensize
     */
    @NonNull
    public static IntVector2 getDisplaySize() {
        int width = 0, height = 0;
        final DisplayMetrics metrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) AppBase.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Method mGetRawH, mGetRawW;

        try {
            // For JellyBean 4.2 (API 17) and onward
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
                display.getRealMetrics(metrics);

                width = metrics.widthPixels;
                height = metrics.heightPixels;
            } else {
                mGetRawH = Display.class.getMethod("getRawHeight");
                mGetRawW = Display.class.getMethod("getRawWidth");

                try {
                    width = (Integer) mGetRawW.invoke(display);
                    height = (Integer) mGetRawH.invoke(display);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (NoSuchMethodException e3) {
            e3.printStackTrace();
        }
        return new IntVector2(width, height);
    }

    /**
     * Gets the screensize as if the device is in portrait view.
     * If in portrait this and getDisplaySize will return the same thing.
     *
     * @return The screensize formatted as if in portrait
     */
    @NonNull
    public static IntVector2 getDisplaySizeAsPortrait() {
        IntVector2 size = getDisplaySize();
        if (size.y >= size.x) {
            return size;
        } else {
            int x = size.x;
            size.x = size.y;
            size.y = x;
            return size;
        }
    }

    /**
     * Converts a dip value into a pixel value
     * Usually you want to use getDipInt unless you are performing additional calculations with the result.
     *
     * @param dp Dip value
     * @return Pixel value
     */
    public static float getDip(int dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, AppBase.getContext().getResources().getDisplayMetrics());
    }

    /**
     * Converts a dip value into a pixel value rounded to the nearest int
     * This is typically the desired choice as pixels are only in ints
     *
     * @param dip Dip value
     * @return Pixel value
     */
    public static int getDipInt(int dip) {
        return Math.round(getDip(dip));
    }

    /**
     * Gets the current device orientation.
     * There are some reports that this may return the wrong result on some devices
     * but I have not found any yet. I may update this will a fallback screensize check
     *
     * @return The device orientation
     */
    public static Orientation getOrientation() {
        switch (AppBase.getContext().getResources().getConfiguration().orientation) {
            case Configuration.ORIENTATION_UNDEFINED:
                return Orientation.UNDEFINED;
            case Configuration.ORIENTATION_PORTRAIT:
                return Orientation.PORTRAIT;
            case Configuration.ORIENTATION_LANDSCAPE:
                return Orientation.LANDSCAPE;
            default:
                return Orientation.UNDEFINED;
        }
    }

    /**
     * Gets the screen size bucket category
     *
     * @return The screensize
     */
    @NonNull
    public static ScreenSize getScreenSize() {
        int screenLayout = AppBase.getContext().getResources().getConfiguration().screenLayout;
        screenLayout &= Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenLayout) {
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                return ScreenSize.SMALL;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
                return ScreenSize.NORMAL;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                return ScreenSize.LARGE;
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                return ScreenSize.XLARGE;
            default:
                return ScreenSize.UNDEFINED;
        }

    }

    /**
     * Gets the height of the status bar if visible.
     * Must pass in if immersive is enabled as that effects the height.
     *
     * @param immersiveEnabled If immersive mode is enabled
     * @return The height of the status bar in pixels.
     */
    public static int getStatusHeight(boolean immersiveEnabled) {
        if (immersiveEnabled) {
            return 0;
        } else {
            Resources resources = AppBase.getContext().getResources();
            int result = 0;
            int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId);
            }
            return result;
        }
    }

    public static int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        if (AppBase.getContext().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            return TypedValue.complexToDimensionPixelSize(tv.data, AppBase.getContext().getResources().getDisplayMetrics());
        }
        return 0;
    }

    /**
     * Gets the height of the navigation bar
     *
     * @param immersiveEnabled If immersive mode is enabled
     * @return The height of the navigation bar in pixels.
     */
    public static int getNavigationHeight(boolean immersiveEnabled) {
        if (immersiveEnabled) {
            return 0;
        } else {
            Resources resources = AppBase.getContext().getResources();
            int id = resources.getIdentifier(
                    getOrientation() == Orientation.PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape",
                    "dimen", "android");
            if (id > 0) {
                return resources.getDimensionPixelSize(id);
            }
            return 0;
        }
    }

    /**
     * Gets the location of the navigation bar.
     * Note that not all devices have an onscreen navigation bar.
     * This will report side for those without so always check the height of the bar before assuming its there.
     *
     * @return The location of the navigation bar
     */
    @NonNull
    public static NavigationBarLocation getNavigationBarLocation() {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) AppBase.getContext().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displaymetrics);
        int viewHeight = displaymetrics.heightPixels;
        if (getDisplaySize().y != viewHeight) {
            return NavigationBarLocation.BOTTOM;
        } else {
            return NavigationBarLocation.SIDE;
        }
    }

    /**
     * Gets the formatted version string of the app in the format 1.2.3
     *
     * @return The formatted version string
     */
    public static String getVersionName() {
        PackageInfo pInfo;
        try {
            pInfo = AppBase.getContext().getPackageManager().getPackageInfo(AppBase.getContext().getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Displays a popup toast for a short time
     *
     * @param text Text to display
     */
    public static void toastShort(String text) {
        Toast.makeText(AppBase.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Displays a popup toast for a long time
     *
     * @param text Text to display
     */
    public static void toastLong(String text) {
        Toast.makeText(AppBase.getContext(), text, Toast.LENGTH_LONG).show();
    }

    /**
     * Backwards compatible method of removing the global layout listener
     *
     * @param src    The view to remove from
     * @param victim The global layout listener to remove
     */
    @SuppressWarnings("deprecation")
    public static void removeGlobalLayoutListener(@NonNull View src, @NonNull ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            src.getViewTreeObserver().removeOnGlobalLayoutListener(victim);
        } else {
            src.getViewTreeObserver().removeGlobalOnLayoutListener(victim);
        }
    }

    /**
     * Get the formatted date time for the current locale
     *
     * @param timeStamp Timestamp value
     * @return The formatted date time
     */
    @SuppressWarnings("deprecation")
    public static String getFormattedDateTime(long timeStamp) {
        int flags = DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME
                | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY
                | DateUtils.FORMAT_ABBREV_MONTH;
        if(UtilsTime.is24HourTime()) {
            flags |= DateUtils.FORMAT_24HOUR;
        }
        return DateUtils.formatDateTime(AppBase.getContext(), timeStamp, flags);
    }

    /**
     * Get the formatted date for the current locale
     *
     * @param timeStamp Timestamp value
     * @return The formatted date
     */
    @SuppressWarnings("deprecation")
    public static String getFormattedDate(long timeStamp) {
        int flags = DateUtils.FORMAT_SHOW_DATE
                | DateUtils.FORMAT_SHOW_WEEKDAY | DateUtils.FORMAT_ABBREV_WEEKDAY
                | DateUtils.FORMAT_ABBREV_MONTH;
        if(UtilsTime.is24HourTime()) {
            flags |= DateUtils.FORMAT_24HOUR;
        }
        return DateUtils.formatDateTime(AppBase.getContext(), timeStamp, flags);
    }

    /**
     * Get the formatted time for the current locale
     *
     * @param timeStamp Timestamp value
     * @return The formatted time
     */
    public static String getFormattedTime(long timeStamp) {
        int flags = DateUtils.FORMAT_SHOW_TIME;
        return DateUtils.formatDateTime(AppBase.getContext(), timeStamp, flags);
    }

    /**
     * Parses a string that contains only an int value into an int.
     *
     * @param value        String value containing only an int
     * @param defaultValue Value that returns on parsing error
     * @return Parsed value
     */
    public static int stringToInt(String value, int defaultValue) {
        try {
            return Integer.parseInt(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Parses a string that contains only a float value into an float.
     *
     * @param value        String value containing only a float
     * @param defaultValue Value that returns on parsing error
     * @return Parsed value
     */
    public static float stringToFloat(String value, float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Parses a string that contains only a byte value into a byte.
     *
     * @param value        String value containing only a byte
     * @param defaultValue Value that returns on parsing error
     * @return Parsed value
     */
    public static byte stringToByte(String value, byte defaultValue) {
        try {
            return Byte.parseByte(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    @NonNull
    public static String getFormattedDuration(long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }

        long days = TimeUnit.MILLISECONDS.toDays(millis);
        millis -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        millis -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        millis -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

        StringBuilder sb = new StringBuilder(64);
        if (days > 0) {
            sb.append(days);
            sb.append(days == 1 ? getStringSpaced(R.string.day_singular) : getStringSpaced(R.string.day_plural));
        }
        if (hours > 0) {
            sb.append(hours);
            sb.append(hours == 1 ? getStringSpaced(R.string.hour_singular) : getStringSpaced(R.string.hour_plural));
        }
        if (minutes > 0) {
            sb.append(minutes);
            sb.append(minutes == 1 ? getStringSpaced(R.string.minute_singular) : getStringSpaced(R.string.minute_plural));
        }
        if (seconds > 0) {
            sb.append(seconds);
            sb.append(seconds == 1 ? getStringSpaced(R.string.second_singular) : getStringSpaced(R.string.second_plural));
        }
        return (sb.toString());
    }

    @NonNull
    public static String getFormattedDurationCompact(long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
                millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis))
        );
    }

    /*
    TimeUnit.MILLISECONDS.toHours(millis),
                TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
     */

    /**
     * Internal helper for the formatted duration function
     *
     * @param stringId String id
     * @return The string with spaces around it
     */
    @NonNull
    private static String getStringSpaced(@StringRes int stringId) {
        return " " + AppBase.getContext().getResources().getString(stringId) + " ";
    }

    /**
     * Locks the device window in the current mode.
     * If current mode is unspecificed this does nothing.
     */
    public static void lockOrientationCurrent(@NonNull AppCompatActivity activity) {
        if (getOrientation() == Orientation.PORTRAIT) {
            lockOrientationPortrait(activity);
        } else if (getOrientation() == Orientation.LANDSCAPE) {
            lockOrientationLandscape(activity);
        }
    }

    /**
     * Locks the device window in landscape mode.
     */
    public static void lockOrientationLandscape(@NonNull AppCompatActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Locks the device window in portrait mode.
     */
    public static void lockOrientationPortrait(@NonNull AppCompatActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Allows user to freely use portrait or landscape mode.
     */
    public static void unlockOrientation(@NonNull AppCompatActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    /**
     * Gets the current orientation lock status. Undefined if allowed to rotate freely
     */
    public static Orientation getOrientationLock(@NonNull AppCompatActivity activity) {
        switch(activity.getRequestedOrientation()) {
            case ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED:
                return Orientation.UNDEFINED;
            case ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE:
                return Orientation.LANDSCAPE;
            case ActivityInfo.SCREEN_ORIENTATION_PORTRAIT:
                return Orientation.PORTRAIT;
            default:
                return Orientation.UNDEFINED;
        }
    }

    /**
     * Checks if the given value is one of the items in the array
     *
     * @param value Value to check for
     * @param array Array to check from
     * @return True if the item is in the array
     */
    public static boolean isInArray(@NonNull String value, @NonNull String[] array) {
        for (String item : array) {
            if (item == null) {
                continue;
            }
            if (item.equals(value)) {
                return true;
            }
        }
        return false;
    }

    public static void openLink(String link) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            AppBase.getContext().startActivity(intent);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.v("Utils", ex.getMessage());
            Utils.toastShort(AppBase.getContext().getResources().getString(R.string.failed_open_link));
        }
    }

    public static void shareText(@NonNull final String chooserTitle, @NonNull final String shareText, @NonNull final String shareType) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType(shareType);
        share.putExtra(Intent.EXTRA_TEXT, shareText);
        AppBase.getContext().startActivity(Intent.createChooser(share, chooserTitle));
    }

    public static void shareStream(@NonNull final String chooserTitle, @NonNull final URI uri, @NonNull final String shareType) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType(shareType);
        AppBase.getContext().startActivity(Intent.createChooser(share, chooserTitle));
    }

    /**
     * Generates a unique String
     *
     * @return Unique string
     */
    @NonNull
    public static String generateUniqueString() {
        return UUID.randomUUID().toString();
    }

    /**
     * Device orientations
     */
    public enum Orientation {
        UNDEFINED, PORTRAIT, LANDSCAPE
    }

    /**
     * Android defined screen size categories
     */
    public enum ScreenSize {
        SMALL, NORMAL, LARGE, XLARGE, UNDEFINED
    }

    /**
     * Location of the navigation bar.
     */
    public enum NavigationBarLocation {
        BOTTOM, SIDE
    }

}

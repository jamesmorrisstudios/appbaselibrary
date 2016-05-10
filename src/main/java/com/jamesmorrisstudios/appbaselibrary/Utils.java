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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.jamesmorrisstudios.appbaselibrary.activities.AppLauncherActivity;
import com.jamesmorrisstudios.appbaselibrary.activityHandlers.SnackbarRequest;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

/**
 * Utility and Constant values
 * <p/>
 * Created by James on 9/16/2014.
 */
public final class Utils {
    public static final String stringType = "UTF-8";
    public static final String stringType16 = "UTF-16";
    public final static Random rand = new Random();
    private static int requestCode = 1;

    /**
     * Displays a popup toast for a short time
     *
     * @param text Text to display
     * @return true if display, False if error
     */
    /*
    public static boolean toastShort(@NonNull final String text) {
        try {
            Toast.makeText(AppBase.getContext(), text, Toast.LENGTH_SHORT).show();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    */

    /**
     * Displays a popup toast for a long time
     *
     * @param text Text to display
     * @return true if display, False if error
     */
    /*
    public static boolean toastLong(@NonNull final String text) {
        try {
            Toast.makeText(AppBase.getContext(), text, Toast.LENGTH_LONG).show();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }
    */

    /**
     * Backwards compatible method of removing the global layout listener
     *
     * @param src    The view to remove from
     * @param victim The global layout listener to remove
     */
    @SuppressWarnings("deprecation")
    public static void removeGlobalLayoutListener(@NonNull final View src, @NonNull final ViewTreeObserver.OnGlobalLayoutListener victim) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            src.getViewTreeObserver().removeOnGlobalLayoutListener(victim);
        } else {
            src.getViewTreeObserver().removeGlobalOnLayoutListener(victim);
        }
    }

    /**
     * Parses a string that contains only an int value into an int.
     *
     * @param value        String value containing only an int
     * @param defaultValue Value that returns on parsing error
     * @return Parsed value
     */
    public static int stringToInt(@NonNull final String value, final int defaultValue) {
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
    public static float stringToFloat(@NonNull final String value, final float defaultValue) {
        try {
            return Float.parseFloat(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Parses a string that contains only a double value into an double.
     *
     * @param value        String value containing only a double
     * @param defaultValue Value that returns on parsing error
     * @return Parsed value
     */
    public static double stringToDouble(@NonNull final String value, final double defaultValue) {
        try {
            return Double.parseDouble(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Parses a string that contains only a long value into an long.
     *
     * @param value        String value containing only a long
     * @param defaultValue Value that returns on parsing error
     * @return Parsed value
     */
    public static long stringToLong(@NonNull final String value, final long defaultValue) {
        try {
            return Long.parseLong(value);
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
    public static byte stringToByte(@NonNull final String value, final byte defaultValue) {
        try {
            return Byte.parseByte(value);
        } catch (Exception ex) {
            return defaultValue;
        }
    }

    /**
     * Checks if the given value is one of the items in the array
     *
     * @param value Value to check for
     * @param array Array to check from
     * @return True if the item is in the array
     */
    public static boolean isInArray(@NonNull final String value, @NonNull final String[] array) {
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

    /**
     * Checks if the given value is one of the items in the array
     *
     * @param value Value to check for
     * @param array Array to check from
     * @return True if the item is in the array
     */
    public static boolean isInArray(@NonNull final String value, @NonNull final ArrayList<String> array) {
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

    /**
     * Removes any instance of the given value from the array
     *
     * @param value Value to check for
     * @param array Array to check from
     */
    public static void removeFromArray(@NonNull final String value, @NonNull final ArrayList<String> array) {
        for (int i = array.size() - 1; i >= 0; i--) {
            if (array.get(i).equals(value)) {
                array.remove(i);
            }
        }
    }

    /**
     * @return Gets the package name
     */
    @NonNull
    public static String getPackage() {
        return AppBase.getContext().getPackageName();
    }

    /**
     * Hides the keyboard if visible
     */
    public static void hideKeyboard(@NonNull final AppCompatActivity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * Hides the keyboard if visible from a specific view. Use if keyboard is in a dialog
     */
    public static void hideKeyboardFrom(@NonNull final Context context, @NonNull final View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    /**
     * Restarts the app and loads to the given page and scroll position
     *
     * @param activity Activity context
     * @param page     String page tag
     * @param scrollY  Scroll position. -1 for top
     */
    public static void restartApp(@NonNull final Activity activity, @Nullable final String page, final int scrollY, @Nullable final Bundle bundle) {
        Intent i = new Intent(activity, AppLauncherActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (page != null) {
            i.putExtra("PAGE", page);
        }
        if (scrollY != -1) {
            i.putExtra("SCROLL_Y", scrollY);
        }
        if (bundle != null) {
            i.putExtra("EXTRAS", bundle);
        }
        AppBase.getContext().startActivity(i);
        Log.v("Utils", "Restart App");
        //activity.finish();
    }

    public static void openLink(@NonNull Context context, @NonNull final String link, boolean background, boolean showOnError) {
        try {
            String link2 = link;
            if (link2.startsWith("www")) {
                link2 = "http://" + link2;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(link2));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if(background) {
                intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
            }
            context.startActivity(intent);
            Log.v("Utils", link2);
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.v("Utils", ex.getMessage());
            if(showOnError) {
                new SnackbarRequest(AppBase.getContext().getResources().getString(R.string.failed), SnackbarRequest.SnackBarDuration.SHORT).execute();
            }
        }
    }

    /**
     * Open the given link
     *
     * @param link Link to open
     */
    public static void openLink(@NonNull final String link) {
        openLink(AppBase.getContext(), link, false, true);
    }


    /**
     * Open the given link
     *
     * @param link Link to open
     */
    public static void openLinkBackground(@NonNull Context context, @NonNull final String link) {
        openLink(context, link, false, false);
    }

    /**
     * Open Android share chooser for text
     *
     * @param chooserTitle Title
     * @param shareText    Text to share
     * @param shareType    Type of share
     */
    public static void shareText(@NonNull final String chooserTitle, @NonNull final String shareText, @NonNull final String shareType) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.setType(shareType);
        share.putExtra(Intent.EXTRA_TEXT, shareText);
        Intent chooser = Intent.createChooser(share, chooserTitle);
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppBase.getContext().startActivity(chooser);
    }

    /**
     * Open Android share chooser for data stream such as an image or file
     *
     * @param chooserTitle Title
     * @param uri          Uri of stream
     * @param shareType    Type of share
     */
    public static void shareStream(@NonNull final String chooserTitle, @NonNull final Uri uri, @NonNull final String shareType) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        share.putExtra(Intent.EXTRA_STREAM, uri);
        share.setType(shareType);
        Intent chooser = Intent.createChooser(share, chooserTitle);
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppBase.getContext().startActivity(chooser);
    }

    public static void openSettingsPage() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.fromParts("package", Utils.getPackage(), null));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        AppBase.getContext().startActivity(intent);
    }

    /**
     * @param fillValue True or false fill value
     * @param size      Size of boolean array
     * @return Boolean array filled with given data
     */
    @NonNull
    public static boolean[] getFilledBoolArray(final boolean fillValue, final int size) {
        boolean[] array = new boolean[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = fillValue;
        }
        return array;
    }

    /**
     * @return Unique UUID
     */
    @NonNull
    public static UUID generateUniqueUUID() {
        return UUID.randomUUID();
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
     * @return Unique Long
     */
    public static long generateUniqueLong() {
        return UUID.randomUUID().getLeastSignificantBits();
    }

    /**
     * @return Unique Int
     */
    public static int getNextRequestCode() {
        requestCode++;
        if (requestCode >= 256) {
            requestCode = 1;
        }
        return requestCode;
    }

    /**
     * @return Unique Int
     */
    public static int generateUniqueInt() {
        return (int) UUID.randomUUID().getLeastSignificantBits();
    }

    /**
     * @return Unique Int only using lower 16 bits
     */
    public static int generateUniqueIntLower16() {
        return Math.abs(UtilsBits.intToShort(generateUniqueInt()));
    }

    /**
     * @return Unique Int only using lower 8 bits
     */
    public static int generateUniqueIntLower8() {
        return Math.abs(UtilsBits.intToByte(generateUniqueInt()));
    }

}

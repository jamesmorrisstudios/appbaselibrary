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

package com.jamesmorrisstudios.appbaselibrary.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

import java.util.ArrayList;

/**
 * Wrapper for the Android SharedPreferences class that makes it simpler to use
 * <p/>
 * Created by James on 5/2/2015.
 */
public final class Prefs {

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @param data     ArrayList to save
     */
    public static void putStringArrayList(@NonNull final String prefFile, @NonNull final String key, @NonNull final ArrayList<String> data) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int index = 0;
        while (sharedPref.contains(key + ":" + index)) {
            editor.remove(key + ":" + index);
            index++;
        }
        for (int i = 0; i < data.size(); i++) {
            editor.putString(key + ":" + i, data.get(i));
        }
        editor.apply();
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     */
    public static void deleteStringArrayList(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int index = 0;
        while (sharedPref.contains(key + ":" + index)) {
            editor.remove(key + ":" + index);
            index++;
        }
        editor.apply();
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @return String array list. Empty array if key does not exist.
     */
    @NonNull
    public static ArrayList<String> getStringArrayList(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        ArrayList<String> data = new ArrayList<>();
        int index = 0;
        while (sharedPref.contains(key + ":" + index)) {
            data.add(sharedPref.getString(key + ":" + index, ""));
            index++;
        }
        return data;
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @param data     String to save
     */
    public static void putString(@NonNull final String prefFile, @NonNull final String key, @NonNull final String data) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, data);
        editor.apply();
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @return String to retrieve. Empty string if key does not exist.
     */
    @NonNull
    public static String getString(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @return True if the key exists. False otherwise
     */
    public static boolean getStringExists(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        return sharedPref.contains(key);
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @param data     Boolean to save
     */
    public static void putBoolean(@NonNull final String prefFile, @NonNull final String key, final boolean data) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(key, data);
        editor.apply();
    }

    /**
     * @param prefFile   File to save into
     * @param key        Key name
     * @param defaultVal Value to return if save does not exist
     * @return Boolean value
     */
    public static boolean getBoolean(@NonNull final String prefFile, @NonNull final String key, final boolean defaultVal) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        return sharedPref.getBoolean(key, defaultVal);
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @return True if the key exists. False otherwise
     */
    public static boolean getBooleanExists(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        return sharedPref.contains(key);
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @param data     Int to save
     */
    public static void putInt(@NonNull final String prefFile, @NonNull final String key, final int data) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(key, data);
        editor.apply();
    }

    /**
     * @param prefFile   File to save into
     * @param key        Key name
     * @param defaultVal Value to return if save does not exist
     * @return Int value
     */
    public static int getInt(@NonNull final String prefFile, @NonNull final String key, final int defaultVal) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        return sharedPref.getInt(key, defaultVal);
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @return True if the key exists. False otherwise
     */
    public static boolean getIntExists(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        return sharedPref.contains(key);
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @param data     Value to save
     */
    public static void putLong(@NonNull final String prefFile, @NonNull final String key, final long data) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(key, data);
        editor.apply();
    }

    /**
     * @param prefFile   File to save into
     * @param key        Key name
     * @param defaultVal Value to return if save does not exist
     * @return Long value
     */
    public static long getLong(@NonNull final String prefFile, @NonNull final String key, final long defaultVal) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        return sharedPref.getLong(key, defaultVal);
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @return True if the key exists. False otherwise
     */
    public static boolean getLongExists(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        return sharedPref.contains(key);
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @param data     Int array to save
     */
    public static void putIntArrayList(@NonNull final String prefFile, @NonNull final String key, @NonNull final ArrayList<Integer> data) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int index = 0;
        while (sharedPref.contains(key + ":" + index)) {
            editor.remove(key + ":" + index);
            index++;
        }
        for (int i = 0; i < data.size(); i++) {
            editor.putInt(key + ":" + i, data.get(i));
        }
        editor.apply();
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     */
    public static void deleteIntArrayList(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int index = 0;
        while (sharedPref.contains(key + ":" + index)) {
            editor.remove(key + ":" + index);
            index++;
        }
        editor.apply();
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @return Int arrayList
     */
    @NonNull
    public static ArrayList<Integer> getIntArrayList(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        ArrayList<Integer> data = new ArrayList<>();
        int index = 0;
        while (sharedPref.contains(key + ":" + index)) {
            data.add(sharedPref.getInt(key + ":" + index, 0));
            index++;
        }
        return data;
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @param data     Long array to save
     */
    public static void putLongArrayList(@NonNull final String prefFile, @NonNull final String key, @NonNull final ArrayList<Long> data) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int index = 0;
        while (sharedPref.contains(key + ":" + index)) {
            editor.remove(key + ":" + index);
            index++;
        }
        for (int i = 0; i < data.size(); i++) {
            editor.putLong(key + ":" + i, data.get(i));
        }
        editor.apply();
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     */
    public static void deleteLongArrayList(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        int index = 0;
        while (sharedPref.contains(key + ":" + index)) {
            editor.remove(key + ":" + index);
            index++;
        }
        editor.apply();
    }

    /**
     * @param prefFile File to save into
     * @param key      Key name
     * @return Long arrayList
     */
    @NonNull
    public static ArrayList<Long> getLongArrayList(@NonNull final String prefFile, @NonNull final String key) {
        SharedPreferences sharedPref = AppBase.getContext().getSharedPreferences(prefFile, Context.MODE_PRIVATE);
        ArrayList<Long> data = new ArrayList<>();
        int index = 0;
        while (sharedPref.contains(key + ":" + index)) {
            data.add(sharedPref.getLong(key + ":" + index, 0));
            index++;
        }
        return data;
    }

}

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

import android.support.annotation.NonNull;
import android.util.Log;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

/**
 * Logger class
 * <p/>
 * Created by James on 4/28/2015.
 */
public class Logger {

    /**
     * Helper function to check if that logging category is active
     *
     * @param category Category
     * @return True if the given category should be logged
     */
    private static boolean isCategoryEnabled(@NonNull LoggerCategory category) {
        switch (category) {
            case UTILITIES:
                return AppBase.getContext().getResources().getBoolean(R.bool.debug_utilities);
            case USER_INTERFACE:
                return AppBase.getContext().getResources().getBoolean(R.bool.debug_user_interface);
            case MAIN:
                return AppBase.getContext().getResources().getBoolean(R.bool.debug_main);
            case REMOTE:
                return AppBase.getContext().getResources().getBoolean(R.bool.debug_remote);
            default:
                return false;
        }
    }

    /**
     * Unexplained or otherwise "can't happen" errors
     *
     * @param category logging category
     * @param tag      Class tag
     * @param message  Message to print
     */
    public static void wtf(@NonNull LoggerCategory category, @NonNull String tag, @NonNull String message) {
        if (isCategoryEnabled(category)) {
            Log.wtf(tag, message);
        }
    }

    /**
     * Verbose
     * Most verbose logging possible, use for mostly meaningless stuff for in depth troubleshooting
     *
     * @param category logging category
     * @param tag      Class tag
     * @param message  Message to print
     */
    public static void v(@NonNull LoggerCategory category, @NonNull String tag, @NonNull String message) {
        if (isCategoryEnabled(category)) {
            Log.v(tag, message);
        }
    }

    /**
     * Debug
     * Low level information for finding bugs in code
     *
     * @param category logging category
     * @param tag      Class tag
     * @param message  Message to print
     */
    public static void d(@NonNull LoggerCategory category, @NonNull String tag, @NonNull String message) {
        if (isCategoryEnabled(category)) {
            Log.d(tag, message);
        }
    }

    /**
     * Info
     * Something happened that is quite interesting to know when and if it did
     *
     * @param category logging category
     * @param tag      Class tag
     * @param message  Message to print
     */
    public static void i(@NonNull LoggerCategory category, @NonNull String tag, @NonNull String message) {
        if (isCategoryEnabled(category)) {
            Log.i(tag, message);
        }
    }

    /**
     * Warn
     * Something is wrong but may not affect gameState play so just logRemote it and move on
     *
     * @param category logging category
     * @param tag      Class tag
     * @param message  Message to print
     */
    public static void w(@NonNull LoggerCategory category, @NonNull String tag, @NonNull String message) {
        if (isCategoryEnabled(category)) {
            Log.w(tag, message);
        }
    }

    /**
     * Error
     * Something broke in order to get this far. Use to explain unhandled exceptions or the like
     *
     * @param category logging category
     * @param tag      Class tag
     * @param message  Message to print
     */
    public static void e(@NonNull LoggerCategory category, @NonNull String tag, @NonNull String message) {
        if (isCategoryEnabled(category)) {
            Log.e(tag, message);
        }
    }

    /**
     * Logging categories
     */
    public enum LoggerCategory {
        UTILITIES, USER_INTERFACE, MAIN, REMOTE
    }

}

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

/**
 * Logger class
 * <p/>
 * Created by James on 4/28/2015.
 */
public final class Logger {
    private static boolean[] categories = new boolean[LoggerCategory.values().length];

    /**
     * Set the enable state for a specific category
     *
     * @param category Category
     * @param enable   Enable state
     */
    public static void setCategory(@NonNull final LoggerCategory category, final boolean enable) {
        categories[category.ordinal()] = enable;
    }

    /**
     * Set the enable state for all categories at the same time
     *
     * @param enable Enable state
     */
    public static void setAllCategories(final boolean enable) {
        for (int i = 0; i < categories.length; i++) {
            categories[i] = enable;
        }
    }

    /**
     * Helper function to check if that logging category is active
     *
     * @param category Category
     * @return True if the given category should be logged
     */
    private static boolean isCategoryEnabled(@NonNull final LoggerCategory category) {
        return categories[category.ordinal()];
    }

    /**
     * Unexplained or otherwise "can't happen" errors
     *
     * @param category logging category
     * @param tag      Class tag
     * @param message  Message to print
     */
    public static void wtf(@NonNull final LoggerCategory category, @NonNull final String tag, @NonNull final String message) {
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
    public static void v(@NonNull final LoggerCategory category, @NonNull final String tag, @NonNull final String message) {
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
    public static void d(@NonNull final LoggerCategory category, @NonNull final String tag, @NonNull final String message) {
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
    public static void i(@NonNull final LoggerCategory category, @NonNull final String tag, @NonNull final String message) {
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
    public static void w(@NonNull final LoggerCategory category, @NonNull final String tag, @NonNull final String message) {
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
    public static void e(@NonNull final LoggerCategory category, @NonNull final String tag, @NonNull final String message) {
        if (isCategoryEnabled(category)) {
            Log.e(tag, message);
        }
    }

    /**
     * Logging categories
     */
    public enum LoggerCategory {

        /**
         * App Base Library
         */
        BASE,

        /**
         * Google Play Services Library
         */
        GPS,

        /**
         * Primary App
         */
        APP,

        /**
         * Other Library
         */
        OTHER
    }

}

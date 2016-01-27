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

package com.jamesmorrisstudios.appbaselibrary.math;

import android.support.annotation.NonNull;

import com.jamesmorrisstudios.appbaselibrary.math.vectors.Vector2;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Static math functions beyond what is included in the Java Math library
 * <p/>
 * Created by James on 12/10/2014.
 */
public final class UtilsMath {

    /**
     * Forces the given value to be within the bounds provided for ints
     *
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @param val Value to bound
     * @return The input value bound within min and max
     */
    public static int inBoundsInt(final int min, final int max, final int val) {
        return Math.min(Math.max(min, val), max);
    }

    /**
     * Forces the given value to be within the bounds provided for ints
     *
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @param val Value to bound
     * @return The input value bound within min and max
     */
    public static long inBoundsLong(final long min, final long max, final long val) {
        return Math.min(Math.max(min, val), max);
    }

    /**
     * Forces the given value to be within the bounds provided for floats
     *
     * @param min Minimum allowed value
     * @param max Maximum allowed value
     * @param val Value to bound
     * @return The input value bound within min and max
     */
    public static float inBounds(final float min, final float max, final float val) {
        return Math.min(Math.max(min, val), max);
    }

    /**
     * Moves the range of a interpolater value between 0 and 1 to the range defined by
     * the start and end values
     *
     * @param start        Start value when interpolater is 0
     * @param end          End value when interpolater is 1
     * @param interpolator Interpolater value that should range between 0 and 1
     * @return The range shifted output value
     */
    public static float interpolate(final float start, final float end, final float interpolator) {
        return (start * (1 - interpolator) + end * interpolator);
    }

    /**
     * Formats the number with , or . based on their locale
     *
     * @param number Number to format
     * @return String formatted based on the users locale
     */
    @NonNull
    public static String formatDisplayNumber(final long number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    /**
     * Formats the number with , or . based on their locale
     *
     * @param number Number to format
     * @return String formatted based on the users locale
     */
    @NonNull
    public static String formatDisplayNumber(final int number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    /**
     * @param a First vector
     * @param b Second vector
     * @return The dot product of a dot b
     */
    public static float dotProduct(@NonNull final Vector2 a, @NonNull final Vector2 b) {
        return a.x * b.x + a.y * b.y;
    }

    /**
     * @param a The input vector
     * @return The vector after being normalized (magnitude 1)
     */
    @NonNull
    public static Vector2 normalize(@NonNull final Vector2 a) {
        float mag = magnitude(a);
        if (mag != 0) {
            return new Vector2(a.x / mag, a.y / mag);
        }
        return a;
    }

    /**
     * @param a The input vector
     * @return The magnitude of the vector
     */
    public static float magnitude(@NonNull final Vector2 a) {
        return (float) Math.sqrt(Math.pow(a.x, 2) + Math.pow(a.y, 2));
    }

    /**
     * @param a Input vector a
     * @param b Input vector b
     * @return True if the vectors are basically equal
     */
    public static boolean closeTo(@NonNull final Vector2 a, @NonNull final Vector2 b) {
        float margin = 4.0f;
        return Math.abs(a.x - b.x) < margin && Math.abs(a.y - b.y) < margin;
    }

}

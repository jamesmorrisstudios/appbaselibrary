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
    public static int inBoundsInt(int min, int max, int val) {
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
    public static float inBounds(float min, float max, float val) {
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
    public static float interpolate(float start, float end, float interpolator) {
        return (start * (1 - interpolator) + end * interpolator);
    }

    /**
     * Formats the number with , or . based on their locale
     *
     * @param number Number to format
     * @return String formatted based on the users locale
     */
    public static String formatDisplayNumber(long number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

    /**
     * Formats the number with , or . based on their locale
     *
     * @param number Number to format
     * @return String formatted based on the users locale
     */
    public static String formatDisplayNumber(int number) {
        return NumberFormat.getNumberInstance(Locale.getDefault()).format(number);
    }

}

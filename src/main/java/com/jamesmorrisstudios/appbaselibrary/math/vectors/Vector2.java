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

package com.jamesmorrisstudios.appbaselibrary.math.vectors;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Vector class with 2 float dimensions
 * <p/>
 * Created by James on 7/29/2014.
 */
public class Vector2 implements Serializable {
    public float x, y;

    /**
     * Creates a new float vector
     *
     * @param x X value
     * @param y Y value
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return A new float vector with all values equal to 0
     */
    @NonNull
    public static Vector2 zero() {
        return new Vector2(0, 0);
    }

    /**
     * @return A new float vector with all values equal to 1
     */
    @NonNull
    public static Vector2 unit() {
        return new Vector2(1, 1);
    }

    /**
     * @param a First vector
     * @param b Second vector
     * @return The dot product of a dot b
     */
    public static float dotProduct(@NonNull Vector2 a, @NonNull Vector2 b) {
        return a.x * b.x + a.y * b.y;
    }

    /**
     * @param a The input vector
     * @return The vector after being normalized (magnitude 1)
     */
    @NonNull
    public static Vector2 normalize(@NonNull Vector2 a) {
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
    public static float magnitude(@NonNull Vector2 a) {
        return (float) Math.sqrt(Math.pow(a.x, 2) + Math.pow(a.y, 2));
    }

    /**
     * @param a Input vector a
     * @param b Input vector b
     * @return True if the vectors are basically equal
     */
    public static boolean closeTo(@NonNull Vector2 a, @NonNull Vector2 b) {
        float margin = 4.0f;
        return Math.abs(a.x - b.x) < margin && Math.abs(a.y - b.y) < margin;
    }

    /**
     * @param o A second object to compare to
     * @return True if the objects are equal
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (o != null && o instanceof Vector2) {
            Vector2 v = (Vector2) o;
            return x == v.x && y == v.y;
        }
        return false;
    }

    /**
     * @param x Sets the x parameter
     * @param y Sets the y parameter
     */
    public void set(float x, float y) {
        this.x = x;
        this.y = y;
    }

}

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

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Vector class with 2 float dimensions
 * <p/>
 * Created by James on 7/29/2014.
 */
public final class Vector2 implements Serializable {
    @SerializedName("x")
    public float x;
    @SerializedName("y")
    public float y;

    /**
     * Creates a new float vector
     *
     * @param x X value
     * @param y Y value
     */
    public Vector2(final float x, final float y) {
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
     * @param o A second object to compare to
     * @return True if the objects are equal
     */
    @Override
    public final boolean equals(@Nullable final Object o) {
        if (o != null && o instanceof Vector2) {
            Vector2 v = (Vector2) o;
            return x == v.x && y == v.y;
        }
        return false;
    }

}

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
 * Vector class with 4 integer dimensions
 * <p/>
 * Created by James on 7/29/2014.
 */
public final class IntVector4 implements Serializable {
    @SerializedName("x")
    public int x;
    @SerializedName("y")
    public int y;
    @SerializedName("z")
    public int z;
    @SerializedName("w")
    public int w;

    /**
     * Creates a new int vector
     *
     * @param x X value
     * @param y Y value
     * @param z Z value
     * @param w W value
     */
    public IntVector4(final int x, final int y, final int z, final int w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * @return A new int vector with all values equal to 0
     */
    @NonNull
    public static IntVector4 zero() {
        return new IntVector4(0, 0, 0, 0);
    }

    /**
     * @return A new int vector with all values equal to 1
     */
    @NonNull
    public static IntVector4 unit() {
        return new IntVector4(1, 1, 1, 1);
    }

    /**
     * @param o A second object to compare to
     * @return True if the objects are equal
     */
    @Override
    public final boolean equals(@Nullable final Object o) {
        if (o != null && o instanceof IntVector4) {
            IntVector4 v = (IntVector4) o;
            return x == v.x && y == v.y && z == v.z && w == v.w;
        }
        return false;
    }

}

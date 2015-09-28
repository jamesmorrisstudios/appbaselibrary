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
 * Vector class with 4 float dimensions
 * <p/>
 * Created by James on 7/29/2014.
 */
public class Vector4 extends Vector3 implements Serializable {
    public float w;

    /**
     * Creates a new float vector
     *
     * @param x X value
     * @param y Y value
     * @param z Z value
     * @param w W value
     */
    public Vector4(float x, float y, float z, float w) {
        super(x, y, z);
        this.w = w;
    }

    /**
     * @return A new float vector with all values equal to 0
     */
    @NonNull
    public static Vector4 zero() {
        return new Vector4(0, 0, 0, 0);
    }

    /**
     * @return A new float vector with all values equal to 1
     */
    @NonNull
    public static Vector4 unit() {
        return new Vector4(1, 1, 1, 1);
    }

    /**
     * @param o A second object to compare to
     * @return True if the objects are equal
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (o != null && o instanceof Vector4) {
            Vector4 v = (Vector4) o;
            return x == v.x && y == v.y && z == v.z && w == v.w;
        }
        return false;
    }

    /**
     * @param x Sets the x parameter
     * @param y Sets the y parameter
     * @param z Sets the z parameter
     * @param w Sets the w parameter
     */
    public void set(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.x = z;
        this.w = w;
    }

}

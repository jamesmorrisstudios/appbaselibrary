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
 * Vector class with 3 float dimensions
 * <p/>
 * Created by James on 7/29/2014.
 */
public class Vector3 extends Vector2 implements Serializable {
    public float z;

    /**
     * Creates a new float vector
     *
     * @param x X value
     * @param y Y value
     * @param z Z value
     */
    public Vector3(float x, float y, float z) {
        super(x, y);
        this.z = z;
    }

    /**
     * @return A new float vector with all values equal to 0
     */
    @NonNull
    public static Vector3 zero() {
        return new Vector3(0, 0, 0);
    }

    /**
     * @return A new float vector with all values equal to 1
     */
    @NonNull
    public static Vector3 unit() {
        return new Vector3(1, 1, 1);
    }

    /**
     * @param o A second object to compare to
     * @return True if the objects are equal
     */
    @Override
    public boolean equals(@Nullable Object o) {
        if (o != null && o instanceof Vector3) {
            Vector3 v = (Vector3) o;
            return x == v.x && y == v.y && z == v.z;
        }
        return false;
    }

    /**
     * @param x Sets the x parameter
     * @param y Sets the y parameter
     * @param z Sets the Z parameter
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.x = z;
    }

}

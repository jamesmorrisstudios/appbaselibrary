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
 * Handler that wraps all calls for the Event Bus system
 * <p/>
 * Created by James on 9/16/2014.
 */
public final class Bus {
    public static final String TAG = "Bus";
    /**
     * The actual otto bus system. Nothing beyond this class has direct access to it
     */
    private static final com.squareup.otto.Bus BUS = new com.squareup.otto.Bus();

    /**
     * Registers the object with the event bus.
     *
     * @param object Object to register
     */
    public static void register(@NonNull Object object) {
        try {
            BUS.register(object);
        } catch (Exception ex) {
            Log.e(TAG, "ERROR: Register Error: " + ex.getMessage());
        }
    }

    /**
     * Unregisters the object with the event bus.
     *
     * @param object Object to unregister
     */
    public static void unregister(@NonNull Object object) {
        try {
            BUS.unregister(object);
        } catch (Exception ex) {
            Log.e(TAG, "ERROR: UnRegister Error: " + ex.getMessage());
        }
    }

    /**
     * Posts an enum
     *
     * @param event Enum to post
     */
    public static void postEnum(@NonNull Enum event) {
        postObject(event);
    }

    /**
     * Posts a generic object
     *
     * @param event Object to post
     */
    public static void postObject(@NonNull Object event) {
        try {
            BUS.post(event);
        } catch (Exception ex) {
            Log.e(TAG, "ERROR: Post Error: " + ex.getMessage());
        }
    }

}

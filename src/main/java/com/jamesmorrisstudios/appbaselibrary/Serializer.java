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
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.Charset;

/**
 * Data serializer and deserializer
 * <p/>
 * Created by James on 4/28/2015.
 */
public final class Serializer {

    /**
     * Serializes a generic class.
     *
     * @return The byte array of the save. Null on error
     */
    @Nullable
    public static byte[] serializeClass(@NonNull final Object obj, final boolean useCompression) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            byte[] data = gson.toJsonTree(obj).toString().getBytes(Charset.forName(Utils.stringType));
            if (useCompression) {
                return UtilsCompression.compress(data);
            } else {
                return data;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Deserialize a class from a byte array
     * If using compression it will auto fallback to no compression if loading fails.
     *
     * @param bytes Byte array
     * @param clazz Class type
     * @param <T>   Generic...
     * @return The deserialized class. Null on error
     */
    @Nullable
    public static <T> T deserializeClass(@NonNull final byte[] bytes, @NonNull final Class<T> clazz, final boolean useCompression) {
        if (useCompression) {
            byte[] data = UtilsCompression.decompress(bytes);
            if (data != null) {
                T clazzReturn = deserializeClassInternal(data, clazz);
                if (clazzReturn != null) {
                    return clazzReturn;
                }
            }
        }
        return deserializeClassInternal(bytes, clazz);
    }

    /**
     * @param bytes Byte array
     * @param clazz Class type
     * @param <T>   Generic...
     * @return The deserialized class. Null on error
     */
    @Nullable
    private static <T> T deserializeClassInternal(@NonNull final byte[] bytes, @NonNull final Class<T> clazz) {
        String st;
        try {
            st = new String(bytes, Utils.stringType);
        } catch (Exception e1) {
            //Log.v("Serializer", "Failed to deserialize: String conversion");
            return null;
        }
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(st, clazz);
        } catch (Exception e) {
            e.printStackTrace();
            //Log.v("Serializer", "Failed to deserialize: builder: "+st);
            return null;
        }
    }

}

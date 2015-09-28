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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.nio.charset.Charset;

/**
 * Data serializer and deserializer
 * <p/>
 * Created by James on 4/28/2015.
 */
public class Serializer {

    /**
     * Serializes a generic class.
     *
     * @return The byte array of the save. Null on error
     */
    @Nullable
    public static <T> byte[] serializeClass(@NonNull Object obj) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try {
            return gson.toJsonTree(obj).toString().getBytes(Charset.forName(Utils.stringType));
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Deserialize a class from a byte array
     *
     * @param bytes Byte array
     * @param clazz Class type
     * @param <T>   Generic...
     * @return The deserialized class. Null on error
     */
    @Nullable
    public static <T> T deserializeClass(@NonNull byte[] bytes, @NonNull Class<T> clazz) {
        String st;
        try {
            st = new String(bytes, Utils.stringType);
        } catch (Exception e1) {
            return null;
        }
        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            return gson.fromJson(st, clazz);
        } catch (Exception e) {
            return null;
        }
    }

}

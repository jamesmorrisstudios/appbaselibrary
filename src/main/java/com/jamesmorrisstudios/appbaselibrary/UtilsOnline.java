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

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jamesmorrisstudios.appbaselibrary.app.AppBase;


/**
 * Use of the functions in this class requires you to add the network permission in your manifest
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * <p/>
 * Created by James on 4/28/2015.
 */
public final class UtilsOnline {

    /**
     * Checks if the user has an internet connection
     *
     * @return True if we have a data connection, false otherwise
     */
    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) AppBase.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnected();
    }

}

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

package com.jamesmorrisstudios.appbaselibrary.notification;

import android.app.PendingIntent;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;

/**
 * Wrapper for notification actions that abstracts from the actual notification
 * <p/>
 * Created by James on 4/30/2015.
 */
public class NotificationAction {
    @DrawableRes
    private int iconRes;
    private String text;
    private PendingIntent pendingIntent;

    public NotificationAction(@DrawableRes int iconRes, @NonNull String text, @NonNull PendingIntent pendingIntent) {
        this.iconRes = iconRes;
        this.text = text;
        this.pendingIntent = pendingIntent;
    }

    @DrawableRes
    public int getIconRes() {
        return iconRes;
    }

    @NonNull
    public String getText() {
        return text;
    }

    @NonNull
    public PendingIntent getPendingIntent() {
        return pendingIntent;
    }

}

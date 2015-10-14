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
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

import java.util.ArrayList;

/**
 * Content builder for notifications. This allows for a substantial amount of custom behind the scenes construction
 * of the notification with limited work on the callers part.
 * <p/>
 * Created by James on 4/30/2015.
 */
public class NotificationContent {
    private String title, content;
    private Uri tone;
    @DrawableRes
    private int iconRes;
    private NotificationType type;
    private NotificationTheme theme;
    private NotificationVibrate vibrate = NotificationVibrate.SHORT;
    private boolean useLed = false;
    private NotificationPriority priority = NotificationPriority.DEFAULT;
    private boolean onGoing = false;
    private int accentColor = 0;
    private int ledColor = 0, id;
    private ArrayList<NotificationAction> actions = new ArrayList<>();
    private PendingIntent contentIntent, deleteIntent;
    private int notifCounter;

    /**
     * Constructor of all the required notification details
     *
     * @param theme
     * @param type
     * @param title
     * @param content
     * @param tone
     * @param iconRes
     * @param accentColor
     * @param id
     */
    public NotificationContent(@NonNull NotificationTheme theme, @NonNull NotificationType type, @NonNull String title, @NonNull String content, @Nullable Uri tone, @DrawableRes int iconRes, int accentColor, int id, int notifCounter) {
        this.theme = theme;
        this.type = type;
        this.title = title;
        this.content = content;
        this.tone = tone;
        this.iconRes = iconRes;
        this.accentColor = accentColor;
        this.id = id;
        this.notifCounter = notifCounter;
    }

    public int getId() {
        return id;
    }

    @NonNull
    public final NotificationType getType() {
        return type;
    }

    @NonNull
    public final NotificationTheme getTheme() {
        return theme;
    }

    public void enableLed(int ledColor) {
        this.useLed = true;
        this.ledColor = ledColor;
    }

    public void addContentIntent(PendingIntent contentIntent) {
        this.contentIntent = contentIntent;
    }

    public final boolean getOnGoing() {
        return onGoing;
    }

    public final void setOnGoing(boolean onGoing) {
        this.onGoing = onGoing;
    }

    public boolean hasAccentColor() {
        return accentColor != 0;
    }

    public int getAccentColor() {
        return accentColor;
    }

    public void addDeleteIntent(PendingIntent deleteIntent) {
        this.deleteIntent = deleteIntent;
    }

    public void addAction(@NonNull NotificationAction action) {
        actions.add(action);
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getContent() {
        return content;
    }

    @Nullable
    public Uri getTone() {
        return tone;
    }

    @DrawableRes
    int getIconRes() {
        return iconRes;
    }

    public boolean hasTone() {
        return tone != null;
    }

    public NotificationVibrate getVibrate() {
        return vibrate;
    }

    public void setVibrate(NotificationVibrate vibrate) {
        this.vibrate = vibrate;
    }

    public long[] getVibratePattern() {
        switch (vibrate) {
            case DISABLED:
                Log.v("Notification", "Disabled");
                return new long[]{0};
            case SHORT:
                Log.v("Notification", "Short");
                return new long[]{50, 200, 100, 200};
            case MEDIUM:
                Log.v("Notification", "Medium");
                return new long[]{50, 500, 350, 500};
            case LONG:
                Log.v("Notification", "Long");
                return new long[]{50, 1200, 800, 1200};
            case PATTERN_1:
                Log.v("Notification", "Pattern 1");
                return new long[] {0, 250, 200, 250, 150, 150, 75, 150, 75, 150};
            case PATTERN_2:
                Log.v("Notification", "Pattern 1");
                return new long[] {0,150,50,75,50,75,50,150,50,75,50,75,50,300};
            case PATTERN_3:
                Log.v("Notification", "Pattern 1");
                return new long[] {0,200,50,175,50,150,50,125,50,100,50,75,50,50,50,75,50,100,50,125,50,150,50,157,50,200};
            case PATTERN_4:
                Log.v("Notification", "Pattern 1");
                return new long[] {0,50,100,50,100,50,100,400,100,300,100,350,50,200,100,100,50,600};
            case PATTERN_5:
                Log.v("Notification", "Pattern 1");
                return new long[] {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
            case PATTERN_6:
                Log.v("Notification", "Pattern 1");
                return new long[] {0,100,200,100,100,100,100,100,200,100,500,100,225,100};
        }
        return new long[]{1000, 1000};
    }

    public NotificationPriority getNotificationPriority() {
        return priority;
    }

    public void setNotificationPriority(NotificationPriority priority) {
        this.priority = priority;
    }

    public boolean getUseLed() {
        return useLed;
    }

    public int getLedColor() {
        return ledColor;
    }

    public ArrayList<NotificationAction> getActions() {
        return actions;
    }

    public PendingIntent getContentIntent() {
        return contentIntent;
    }

    public PendingIntent getDeleteIntent() {
        return deleteIntent;
    }

    public boolean hasContentIntent() {
        return contentIntent != null;
    }

    public boolean hasDeleteIntent() {
        return deleteIntent != null;
    }

    public int getNotifCounter() {
        return notifCounter;
    }

    public enum NotificationType {
        NORMAL, CUSTOM, CUSTOM_SNOOZE
    }

    public enum NotificationTheme {
        DARK, LIGHT
    }

    public enum NotificationPriority {
        DEFAULT(AppBase.getContext().getResources().getString(R.string.default_)),
        LOW(AppBase.getContext().getResources().getString(R.string.low)),
        HIGH(AppBase.getContext().getResources().getString(R.string.high)),
        MAX(AppBase.getContext().getResources().getString(R.string.max));

        public final String name;

        NotificationPriority(String name) {
            this.name = name;
        }
    }

    public enum NotificationVibrate {
        DISABLED(AppBase.getContext().getResources().getString(R.string.disabled)),
        DEFAULT(AppBase.getContext().getResources().getString(R.string.default_)),
        SHORT(AppBase.getContext().getResources().getString(R.string.short_)),
        MEDIUM(AppBase.getContext().getResources().getString(R.string.medium)),
        LONG(AppBase.getContext().getResources().getString(R.string.long_)),
        PATTERN_1(AppBase.getContext().getResources().getString(R.string.pattern_1)),
        PATTERN_2(AppBase.getContext().getResources().getString(R.string.pattern_2)),
        PATTERN_3(AppBase.getContext().getResources().getString(R.string.pattern_3)),
        PATTERN_4(AppBase.getContext().getResources().getString(R.string.pattern_4)),
        PATTERN_5(AppBase.getContext().getResources().getString(R.string.pattern_5)),
        PATTERN_6(AppBase.getContext().getResources().getString(R.string.pattern_6));

        public final String name;

        NotificationVibrate(String name) {
            this.name = name;
        }
    }

}

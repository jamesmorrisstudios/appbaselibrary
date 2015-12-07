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
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
    private NotificationThemeText themeText;
    private NotificationVibrate vibrate = NotificationVibrate.SHORT;
    private long[] vibrateCustom = null;
    private boolean useLed = false;
    private NotificationPriority priority = NotificationPriority.DEFAULT;
    private boolean onGoing = false;
    private int accentColor = 0;
    private int ledColor = 0, id;
    private ArrayList<NotificationAction> actions = new ArrayList<>();
    private PendingIntent contentIntent, deleteIntent;
    private int notifCounter;
    private Bitmap iconOverride = null;
    private NotificationLights lights = NotificationLights.MEDIUM_ON_MEDIUM_OFF;

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
    public NotificationContent(@NonNull NotificationTheme theme, @NonNull NotificationThemeText themeText, @NonNull NotificationType type, @NonNull String title, @NonNull String content, @Nullable Uri tone, @DrawableRes int iconRes, int accentColor, int id, int notifCounter) {
        this.theme = theme;
        this.themeText = themeText;
        this.type = type;
        this.title = title;
        this.content = content;
        this.tone = tone;
        this.iconRes = iconRes;
        this.accentColor = accentColor;
        this.id = id;
        this.notifCounter = notifCounter;
    }

    public NotificationThemeText getThemeText() {
        return themeText;
    }

    public NotificationLights getLights() {
        return lights;
    }

    public void setLights(@NonNull NotificationLights lights) {
        this.lights = lights;
    }

    public Bitmap getIconOverride() {
        return iconOverride;
    }

    public void setIconOverride(Bitmap iconOverride) {
        this.iconOverride = iconOverride;
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

    public long[] getVibrateCustom() {
        return vibrateCustom;
    }

    public void setVibrateCustom(long[] vibrateCustom) {
        this.vibrateCustom = vibrateCustom;
    }

    public long[] getVibratePattern() {
        return vibrate.getPattern();
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
        DARK, LIGHT, SYSTEM
    }

    public enum NotificationThemeText {
        DARK_TEXT, LIGHT_TEXT
    }

    public enum NotificationPriority {
        DEFAULT,
        LOW,
        HIGH,
        MAX;

        public String getName() {
            switch(this) {
                case DEFAULT:
                    return AppBase.getContext().getResources().getString(R.string.default_);
                case LOW:
                    return AppBase.getContext().getResources().getString(R.string.low);
                case HIGH:
                    return AppBase.getContext().getResources().getString(R.string.high);
                case MAX:
                    return AppBase.getContext().getResources().getString(R.string.max);
                default:
                    return AppBase.getContext().getResources().getString(R.string.default_);
            }
        }
    }

    public enum NotificationVibrate {
        DISABLED,
        DEFAULT,
        SHORT,
        MEDIUM,
        LONG,
        PATTERN_1,
        PATTERN_2,
        PATTERN_3,
        PATTERN_4,
        PATTERN_5,
        PATTERN_6;

        public String getName() {
            switch(this) {
                case DISABLED:
                    return AppBase.getContext().getResources().getString(R.string.disabled);
                case DEFAULT:
                    return AppBase.getContext().getResources().getString(R.string.default_);
                case SHORT:
                    return AppBase.getContext().getResources().getString(R.string.short_);
                case MEDIUM:
                    return AppBase.getContext().getResources().getString(R.string.medium);
                case LONG:
                    return AppBase.getContext().getResources().getString(R.string.long_);
                case PATTERN_1:
                    return AppBase.getContext().getResources().getString(R.string.pattern_1);
                case PATTERN_2:
                    return AppBase.getContext().getResources().getString(R.string.pattern_2);
                case PATTERN_3:
                    return AppBase.getContext().getResources().getString(R.string.pattern_3);
                case PATTERN_4:
                    return AppBase.getContext().getResources().getString(R.string.pattern_4);
                case PATTERN_5:
                    return AppBase.getContext().getResources().getString(R.string.pattern_5);
                case PATTERN_6:
                    return AppBase.getContext().getResources().getString(R.string.pattern_6);
                default:
                    return AppBase.getContext().getResources().getString(R.string.default_);
            }
        }

        public long[] getPattern() {
            switch (this) {
                case DISABLED:
                    return new long[]{0};
                case SHORT:
                    return new long[]{50, 200, 100, 200};
                case MEDIUM:
                    return new long[]{50, 500, 350, 500};
                case LONG:
                    return new long[]{50, 1200, 800, 1200};
                case PATTERN_1:
                    return new long[]{0, 250, 200, 250, 150, 150, 75, 150, 75, 150};
                case PATTERN_2:
                    return new long[]{0, 150, 50, 75, 50, 75, 50, 150, 50, 75, 50, 75, 50, 300};
                case PATTERN_3:
                    return new long[]{0, 200, 50, 175, 50, 150, 50, 125, 50, 100, 50, 75, 50, 50, 50, 75, 50, 100, 50, 125, 50, 150, 50, 157, 50, 200};
                case PATTERN_4:
                    return new long[]{0, 50, 100, 50, 100, 50, 100, 400, 100, 300, 100, 350, 50, 200, 100, 100, 50, 600};
                case PATTERN_5:
                    return new long[]{0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500};
                case PATTERN_6:
                    return new long[]{0, 100, 200, 100, 100, 100, 100, 100, 200, 100, 500, 100, 225, 100};
            }
            return new long[]{1000, 1000};
        }
    }

    public enum NotificationLights {
        SHORT_ON_SHORT_OFF,
        SHORT_ON_MEDIUM_OFF,
        SHORT_ON_LONG_OFF,
        MEDIUM_ON_SHORT_OFF,
        MEDIUM_ON_MEDIUM_OFF,
        MEDIUM_ON_LONG_OFF,
        LONG_ON_SHORT_OFF,
        LONG_ON_MEDIUM_OFF,
        LONG_ON_LONG_OFF;

        public String getName() {
            switch(this) {
                case SHORT_ON_SHORT_OFF:
                    return AppBase.getContext().getResources().getString(R.string.short_on) + " : " + AppBase.getContext().getResources().getString(R.string.short_off);
                case SHORT_ON_MEDIUM_OFF:
                    return AppBase.getContext().getResources().getString(R.string.short_on) + " : " + AppBase.getContext().getResources().getString(R.string.medium_off);
                case SHORT_ON_LONG_OFF:
                    return AppBase.getContext().getResources().getString(R.string.short_on) + " : " + AppBase.getContext().getResources().getString(R.string.long_off);
                case MEDIUM_ON_SHORT_OFF:
                    return AppBase.getContext().getResources().getString(R.string.medium_on) + " : " + AppBase.getContext().getResources().getString(R.string.short_off);
                case MEDIUM_ON_MEDIUM_OFF:
                    return AppBase.getContext().getResources().getString(R.string.medium_on) + " : " + AppBase.getContext().getResources().getString(R.string.medium_off);
                case MEDIUM_ON_LONG_OFF:
                    return AppBase.getContext().getResources().getString(R.string.medium_on) + " : " + AppBase.getContext().getResources().getString(R.string.long_off);
                case LONG_ON_SHORT_OFF:
                    return AppBase.getContext().getResources().getString(R.string.long_on) + " : " + AppBase.getContext().getResources().getString(R.string.short_off);
                case LONG_ON_MEDIUM_OFF:
                    return AppBase.getContext().getResources().getString(R.string.long_on) + " : " + AppBase.getContext().getResources().getString(R.string.medium_off);
                case LONG_ON_LONG_OFF:
                    return AppBase.getContext().getResources().getString(R.string.long_on) + " : " + AppBase.getContext().getResources().getString(R.string.long_off);
                default:
                    return AppBase.getContext().getResources().getString(R.string.short_on) + " : " + AppBase.getContext().getResources().getString(R.string.short_off);
            }
        }

        public int[] getPattern() {
            switch(this) {
                case SHORT_ON_SHORT_OFF:
                    return new int[]{500, 500};
                case SHORT_ON_MEDIUM_OFF:
                    return new int[]{500, 1500};
                case SHORT_ON_LONG_OFF:
                    return new int[]{500, 2500};
                case MEDIUM_ON_SHORT_OFF:
                    return new int[]{1500, 500};
                case MEDIUM_ON_MEDIUM_OFF:
                    return new int[]{1500, 1500};
                case MEDIUM_ON_LONG_OFF:
                    return new int[]{1500, 2500};
                case LONG_ON_SHORT_OFF:
                    return new int[]{2500, 500};
                case LONG_ON_MEDIUM_OFF:
                    return new int[]{2500, 1500};
                case LONG_ON_LONG_OFF:
                    return new int[]{2500, 2500};
                default:
                    return new int[]{1500, 1500};
            }
        }
    }

}

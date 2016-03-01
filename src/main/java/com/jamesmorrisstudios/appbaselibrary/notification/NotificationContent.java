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

import android.app.Notification;
import android.app.PendingIntent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsTheme;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;

import java.util.ArrayList;

/**
 * Content builder for notifications. This allows for a substantial amount of custom behind the scenes construction
 * of the notification with limited work on the callers part.
 * <p/>
 * Created by James on 4/30/2015.
 */
public final class NotificationContent {
    //Required Data
    public final NotificationThemeBackground themeBackground;
    public final NotificationThemeText themeText;
    public final NotificationType type;
    public final String title, content;
    @DrawableRes
    public final int iconRes;
    public final int notificationId;
    public final PendingIntent contentIntent, deleteIntent;
    //Actions Optional but common
    private ArrayList<NotificationAction> actions = new ArrayList<>();
    //Optional Data
    private Uri tone = null;
    private NotificationVibrate vibrate = NotificationVibrate.DEFAULT;
    private boolean useLed = true;
    private int ledColor = UtilsTheme.getAccentColor();
    //Uncommon optional
    private int accentColor = UtilsTheme.getAccentColor();
    private NotificationPriority priority = NotificationPriority.DEFAULT;
    private NotificationLedPattern ledPattern = NotificationLedPattern.MEDIUM_ON_MEDIUM_OFF;
    private long[] vibrateCustom = null;
    private boolean onGoing = false;
    private Bitmap iconOverride = null;
    private boolean wideButtons = false;

    /**
     * Builder that includes all required components of the notification
     *
     * @param themeBackground Background theme
     * @param themeText       Text theme
     * @param type            Notification Type
     * @param title           Notification Title
     * @param content         Notification Content
     * @param iconRes         Notification Icon resource ID (set override with its own function)
     * @param contentIntent   Click intent
     * @param deleteIntent    Delete intent
     * @param notificationId  Notification Id
     */
    public NotificationContent(@NonNull final NotificationThemeBackground themeBackground, @NonNull final NotificationThemeText themeText,
                               @NonNull final NotificationType type, @NonNull final String title, @NonNull final String content, @DrawableRes final int iconRes,
                               @NonNull final PendingIntent contentIntent, @NonNull final PendingIntent deleteIntent, final int notificationId) {
        this.themeBackground = themeBackground;
        this.themeText = themeText;
        this.type = type;
        this.title = title;
        this.content = content;
        this.iconRes = iconRes;
        this.notificationId = notificationId;
        this.contentIntent = contentIntent;
        this.deleteIntent = deleteIntent;
    }

    /**
     * @param action Notification Action
     */
    public final void addAction(@NonNull final NotificationAction action) {
        actions.add(action);
    }

    /**
     * @return Get action list. May be empty
     */
    @NonNull
    public final ArrayList<NotificationAction> getActions() {
        return actions;
    }

    /**
     * @return Get tone/ May be null
     */
    @Nullable
    public final Uri getTone() {
        return tone;
    }

    /**
     * @param tone Set tone. Null for none
     */
    public final void setTone(@Nullable final Uri tone) {
        this.tone = tone;
    }

    /**
     * @param vibrate Set vibrate pattern
     */
    public final void setVibrate(@NonNull final NotificationVibrate vibrate) {
        this.vibrate = vibrate;
    }

    /**
     * @return true for wide buttons. Default false
     */
    public boolean isWideButtons() {
        return wideButtons;
    }

    /**
     * @param wideButtons true for wide buttons. Default false
     */
    public final void setWideButtons(final boolean wideButtons) {
        this.wideButtons = wideButtons;
    }

    /**
     * If default vibrate then don't use this
     *
     * @return Get vibrate pattern.
     */
    @NonNull
    public final long[] getVibratePattern() {
        if (vibrateCustom != null) {
            return vibrateCustom;
        }
        return vibrate.getPattern();
    }

    /**
     * @return True if you should use the default vibrate pattern
     */
    public final boolean isVibrateDefault() {
        return vibrate == NotificationVibrate.DEFAULT;
    }

    /**
     * @return Is vibrate custom enabled
     */
    public final boolean isVibrateCustom() {
        return vibrateCustom != null;
    }

    /**
     * @param vibrateCustom Custom vibrate pattern
     */
    public final void setVibrateCustom(@Nullable final long[] vibrateCustom) {
        this.vibrateCustom = vibrateCustom;
    }

    /**
     * Set LED mode
     *
     * @param enable Enable state
     * @param color  LED color
     */
    public final void setLed(final boolean enable, final int color) {
        this.useLed = enable;
        this.ledColor = color;
    }

    /**
     * @return True if LED is enabled
     */
    public final boolean getLed() {
        return useLed;
    }

    /**
     * @return LED color. Defaults to app accent color
     */
    public final int getLedColor() {
        return ledColor;
    }

    /**
     * @return Get LED blink pattern
     */
    @NonNull
    public final int[] getLedPattern() {
        return ledPattern.getPattern();
    }

    /**
     * @param ledPattern Set LED blink pattern
     */
    public final void setLedPattern(@NonNull final NotificationLedPattern ledPattern) {
        this.ledPattern = ledPattern;
    }

    /**
     * @return Accent color. Defaults to app accent color if not set
     */
    public final int getAccentColor() {
        return accentColor;
    }

    /**
     * @param accentColor Accent color
     */
    public final void setAccentColor(@ColorInt final int accentColor) {
        this.accentColor = accentColor;
    }

    /**
     * @return Notification priority. Defaults to Default if not set
     */
    public final int getNotificationPriority() {
        return priority.getPriority();
    }

    /**
     * @param priority Notifcation priority.
     */
    public void setNotificationPriority(@NonNull final NotificationPriority priority) {
        this.priority = priority;
    }

    /**
     * @return Get the icon override.
     */
    @Nullable
    public final Bitmap getIconOverride() {
        return iconOverride;
    }

    /**
     * @param iconOverride Set the icon override. Only works fully on Android 6.0+
     */
    public final void setIconOverride(@Nullable final Bitmap iconOverride) {
        this.iconOverride = iconOverride;
    }

    /**
     * @return True if ongoing notification (cannot swipe away)
     */
    public final boolean getOnGoing() {
        return onGoing;
    }

    /**
     * @param onGoing True if ongoing notification (cannot swipe away)
     */
    public final void setOnGoing(final boolean onGoing) {
        this.onGoing = onGoing;
    }

    /**
     * Notification types
     */
    public enum NotificationType {
        NORMAL, CUSTOM, CUSTOM_SNOOZE
    }

    /**
     * Notification background Theme
     */
    public enum NotificationThemeBackground {
        DARK, LIGHT, SYSTEM
    }

    /**
     * Notification text themeBackground
     */
    public enum NotificationThemeText {
        DARK_TEXT, LIGHT_TEXT
    }

    /**
     * Notification Priority
     */
    public enum NotificationPriority {
        DEFAULT,
        LOW,
        HIGH,
        MAX;

        /**
         * @return Priority Name
         */
        @NonNull
        public final String getName() {
            switch (this) {
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

        /**
         * @return The priority int
         */
        public final int getPriority() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                switch (this) {
                    case DEFAULT:
                        return Notification.PRIORITY_DEFAULT;
                    case LOW:
                        return Notification.PRIORITY_LOW;
                    case HIGH:
                        return Notification.PRIORITY_HIGH;
                    case MAX:
                        return Notification.PRIORITY_MAX;
                    default:
                        return Notification.PRIORITY_DEFAULT;
                }
            } else {
                switch (this) {
                    case DEFAULT:
                        return NotificationCompat.PRIORITY_DEFAULT;
                    case LOW:
                        return NotificationCompat.PRIORITY_LOW;
                    case HIGH:
                        return NotificationCompat.PRIORITY_HIGH;
                    case MAX:
                        return NotificationCompat.PRIORITY_MAX;
                    default:
                        return NotificationCompat.PRIORITY_DEFAULT;
                }
            }
        }
    }

    /**
     * Notifcation Vibrate Patterns
     */
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

        /**
         * @return Pattern Name
         */
        @NonNull
        public final String getName() {
            switch (this) {
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

        /**
         * @return Vibrate Pattern
         */
        @NonNull
        public final long[] getPattern() {
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

    /**
     * Notification LED pattern
     */
    public enum NotificationLedPattern {
        SHORT_ON_SHORT_OFF,
        SHORT_ON_MEDIUM_OFF,
        SHORT_ON_LONG_OFF,
        MEDIUM_ON_SHORT_OFF,
        MEDIUM_ON_MEDIUM_OFF,
        MEDIUM_ON_LONG_OFF,
        LONG_ON_SHORT_OFF,
        LONG_ON_MEDIUM_OFF,
        LONG_ON_LONG_OFF;

        /**
         * @return Pattern Name
         */
        @NonNull
        public final String getName() {
            switch (this) {
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

        /**
         * @return Pattern
         */
        @NonNull
        public final int[] getPattern() {
            switch (this) {
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

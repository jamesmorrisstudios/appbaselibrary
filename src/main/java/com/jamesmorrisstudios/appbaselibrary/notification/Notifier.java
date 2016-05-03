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

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresPermission;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.UtilsDisplay;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.time.TimeItem;
import com.jamesmorrisstudios.appbaselibrary.time.UtilsTime;

/**
 * Notification handler class.
 * This generates and displays notifications given a list of reminder items
 * <p/>
 * Created by James on 4/28/2015.
 */
public final class Notifier {

    /**
     * Dismiss the given notification
     *
     * @param id Id of the notification
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void dismissNotification(final int id) {
        // Gets an instance of the NotificationManager service
        NotificationManagerCompat mNotifyMgr = NotificationManagerCompat.from(AppBase.getContext());
        // cancels the notification
        mNotifyMgr.cancel(id);
    }

    /**
     * Builds and displays a notification with the given parameters
     *
     * @param notif NotificationContent
     */
    @RequiresPermission(Manifest.permission.VIBRATE)
    public static void buildNotification(@NonNull final NotificationContent notif) {
        TimeItem timeNow = UtilsTime.getTimeNow();
        Log.v("Notification shown", notif.title + " " + timeNow.getHourInTimeFormatString() + ":" + timeNow.getMinuteString());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            buildNotificationSub(notif);
        } else {
            buildNotificationSubCompat(notif);
        }
    }

    /**
     * Build the notification if using Android 6+
     *
     * @param notif Notification content
     */
    @TargetApi(Build.VERSION_CODES.M)
    private static void buildNotificationSub(@NonNull final NotificationContent notif) {
        Notification notification;
        Notification.Builder mBuilder;
        //noinspection ResourceType
        mBuilder = new Notification.Builder(AppBase.getContext())
                .setContentTitle(notif.title)
                .setCategory(notif.getCategory().getCategory())
                .setPriority(notif.getNotificationPriority())
                .setContentText(notif.content);
        if (notif.getIconOverride() != null) {
            mBuilder.setSmallIcon(Icon.createWithBitmap(notif.getIconOverride()));
        } else {
            mBuilder.setSmallIcon(notif.iconRes);
        }
        if (notif.isVibrateDefault() && !notif.isVibrateCustom()) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        } else {
            mBuilder.setVibrate(notif.getVibratePattern());
        }
        mBuilder.setSound(notif.getTone());
        if (notif.getLed()) {
            mBuilder.setLights(notif.getLedColor(), notif.getLedPattern()[0], notif.getLedPattern()[1]);
        }
        mBuilder.setContentIntent(notif.contentIntent);
        mBuilder.setDeleteIntent(notif.deleteIntent);
        mBuilder.setColor(notif.getAccentColor());
        mBuilder.setAutoCancel(!notif.getOnGoing());
        mBuilder.setOngoing(notif.getOnGoing());
        Notification.WearableExtender wearableExtender = new Notification.WearableExtender();
        for (NotificationAction action : notif.getActions()) {
            mBuilder.addAction(action.getIconRes(), action.getText(), action.getPendingIntent());
            wearableExtender.addAction(new Notification.Action(action.getIconRes(), action.getText(), action.getPendingIntent()));
        }
        if (notif.getActions().size() > 0) {
            mBuilder.extend(wearableExtender);
        }
        if (notif.type == NotificationContent.NotificationType.CUSTOM) {
            notification = addCustomNotification(notif, mBuilder);
        } else if (notif.type == NotificationContent.NotificationType.CUSTOM_SNOOZE) {
            notification = addCustomSnoozeNotification(notif, mBuilder);
        } else {
            mBuilder.setStyle(new Notification.BigTextStyle().bigText(notif.content));
            notification = mBuilder.build();
        }
        // Gets an instance of the NotificationManager service
        NotificationManagerCompat mNotifyMgr = NotificationManagerCompat.from(AppBase.getContext());
        // Builds the notification and issues it.
        mNotifyMgr.notify(notif.notificationId, notification);
    }

    /**
     * Add the custom notification for api >= 23
     *
     * @param notif    NotificationContent
     * @param mBuilder Builder
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    @NonNull
    private static Notification addCustomNotification(@NonNull final NotificationContent notif, @NonNull final Notification.Builder mBuilder) {
        mBuilder.setContent(getContentView(notif, false));
        Notification notification = mBuilder.build();
        notification.bigContentView = getContentBigView(notif, false);
        return notification;
    }

    /**
     * Add the snooze notification for api >= 23
     *
     * @param notif    NotificationContent
     * @param mBuilder Builder
     * @return
     */
    @TargetApi(Build.VERSION_CODES.M)
    @NonNull
    private static Notification addCustomSnoozeNotification(@NonNull final NotificationContent notif, @NonNull final Notification.Builder mBuilder) {
        mBuilder.setContent(getContentView(notif, true));
        Notification notification = mBuilder.build();
        notification.bigContentView = getContentBigView(notif, true);
        return notification;
    }

    /**
     * @param notif NotificationContent
     */
    private static void buildNotificationSubCompat(@NonNull final NotificationContent notif) {
        Notification notification;
        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(AppBase.getContext())
                .setSmallIcon(notif.iconRes)
                .setContentTitle(notif.title)
                .setCategory(notif.getCategory().getCategoryCompat())
                .setPriority(notif.getNotificationPriority())
                .setContentText(notif.content);
        if (notif.isVibrateDefault() && !notif.isVibrateCustom()) {
            mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        } else {
            mBuilder.setVibrate(notif.getVibratePattern());
        }
        mBuilder.setSound(notif.getTone());
        if (notif.getLed()) {
            mBuilder.setLights(notif.getLedColor(), notif.getLedPattern()[0], notif.getLedPattern()[1]);
        }
        mBuilder.setContentIntent(notif.contentIntent);
        mBuilder.setDeleteIntent(notif.deleteIntent);
        mBuilder.setColor(notif.getAccentColor());
        mBuilder.setAutoCancel(!notif.getOnGoing());
        mBuilder.setOngoing(notif.getOnGoing());
        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
        for (NotificationAction action : notif.getActions()) {
            mBuilder.addAction(action.getIconRes(), action.getText(), action.getPendingIntent());
            wearableExtender.addAction(new NotificationCompat.Action(action.getIconRes(), action.getText(), action.getPendingIntent()));
        }
        if (notif.getActions().size() > 0) {
            mBuilder.extend(wearableExtender);
        }
        if (notif.type == NotificationContent.NotificationType.CUSTOM) {
            notification = addCustomNotificationCompat(notif, mBuilder);
        } else if (notif.type == NotificationContent.NotificationType.CUSTOM_SNOOZE) {
            notification = addCustomSnoozeNotificationCompat(notif, mBuilder);
        } else {
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(notif.content));
            notification = mBuilder.build();
        }
        // Gets an instance of the NotificationManager service
        NotificationManagerCompat mNotifyMgr = NotificationManagerCompat.from(AppBase.getContext());
        // Builds the notification and issues it.
        mNotifyMgr.notify(notif.notificationId, notification);
    }

    /**
     * Add the custom notification for api < 23
     *
     * @param notif    NotificationContent
     * @param mBuilder Builder
     * @return
     */
    @NonNull
    private static Notification addCustomNotificationCompat(@NonNull final NotificationContent notif, @NonNull final NotificationCompat.Builder mBuilder) {
        mBuilder.setContent(getContentView(notif, false));
        Notification notification = mBuilder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification.bigContentView = getContentBigView(notif, false);
        }
        return notification;
    }

    /**
     * Add the snooze notification for api < 23
     *
     * @param notif    NotificationContent
     * @param mBuilder Builder
     * @return
     */
    @NonNull
    private static Notification addCustomSnoozeNotificationCompat(@NonNull final NotificationContent notif, @NonNull final NotificationCompat.Builder mBuilder) {
        mBuilder.setContent(getContentView(notif, true));
        Notification notification = mBuilder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification.bigContentView = getContentBigView(notif, true);
        }
        return notification;
    }

    /**
     * Get the content view of the custom notification
     *
     * @param notif  NotificationContent
     * @param snooze True to show snooze button
     * @return Remove Content View
     */
    @NonNull
    private static RemoteViews getContentView(@NonNull final NotificationContent notif, final boolean snooze) {
        RemoteViews contentView;
        if (notif.isWideButtons()) {
            if (notif.themeText == NotificationContent.NotificationThemeText.LIGHT_TEXT) {
                contentView = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_dark_wide);
            } else {
                contentView = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_light_wide);
            }
        } else {
            if (notif.themeText == NotificationContent.NotificationThemeText.LIGHT_TEXT) {
                contentView = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_dark_narrow);
            } else {
                contentView = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_light_narrow);
            }
        }
        if (notif.themeBackground == NotificationContent.NotificationThemeBackground.DARK) {
            contentView.setInt(R.id.layout, "setBackgroundResource", R.color.backgroundDark);
        } else if (notif.themeBackground == NotificationContent.NotificationThemeBackground.LIGHT) {
            contentView.setInt(R.id.layout, "setBackgroundResource", R.color.backgroundLight);
        }
        if (notif.getIconOverride() != null) {
            contentView.setImageViewBitmap(R.id.image, notif.getIconOverride());
        } else {
            contentView.setImageViewResource(R.id.image, notif.iconRes);
        }
        Bitmap circle = getCircle(notif.getAccentColor(), UtilsDisplay.getDipInt(40));
        contentView.setImageViewBitmap(R.id.imageBackground, circle);
        contentView.setTextViewText(R.id.title, notif.title);
        contentView.setTextViewText(R.id.text, notif.content);
        if (snooze) {
            contentView.setViewVisibility(R.id.imageSnooze, View.VISIBLE);
            if (notif.getActions().size() == 3) {
                NotificationAction act1 = notif.getActions().get(0);
                contentView.setImageViewResource(R.id.imageCancel, act1.getIconRes());
                contentView.setOnClickPendingIntent(R.id.imageCancel, act1.getPendingIntent());
                NotificationAction act2 = notif.getActions().get(1);
                contentView.setImageViewResource(R.id.imageSnooze, act2.getIconRes());
                contentView.setOnClickPendingIntent(R.id.imageSnooze, act2.getPendingIntent());
                NotificationAction act3 = notif.getActions().get(2);
                contentView.setImageViewResource(R.id.imageAck, act3.getIconRes());
                contentView.setOnClickPendingIntent(R.id.imageAck, act3.getPendingIntent());
            }
        } else {
            contentView.setViewVisibility(R.id.imageSnooze, View.GONE);
            if (notif.getActions().size() == 2) {
                NotificationAction act1 = notif.getActions().get(0);
                contentView.setImageViewResource(R.id.imageCancel, act1.getIconRes());
                contentView.setOnClickPendingIntent(R.id.imageCancel, act1.getPendingIntent());
                NotificationAction act3 = notif.getActions().get(1);
                contentView.setImageViewResource(R.id.imageAck, act3.getIconRes());
                contentView.setOnClickPendingIntent(R.id.imageAck, act3.getPendingIntent());
            }
        }
        return contentView;
    }

    /**
     * Get the big content view of the custom notification
     *
     * @param notif  NotificationContent
     * @param snooze True to show snooze button
     * @return Remove Content View
     */
    @NonNull
    private static RemoteViews getContentBigView(@NonNull final NotificationContent notif, final boolean snooze) {
        RemoteViews contentViewBig;
        if (notif.isWideButtons()) {
            if (notif.themeText == NotificationContent.NotificationThemeText.LIGHT_TEXT) {
                contentViewBig = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_big_dark_wide);
            } else {
                contentViewBig = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_big_light_wide);
            }
        } else {
            if (notif.themeText == NotificationContent.NotificationThemeText.LIGHT_TEXT) {
                contentViewBig = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_big_dark_narrow);
            } else {
                contentViewBig = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_big_light_narrow);
            }
        }
        if (notif.themeBackground == NotificationContent.NotificationThemeBackground.DARK) {
            contentViewBig.setInt(R.id.layout, "setBackgroundResource", R.color.backgroundDark);
        } else if (notif.themeBackground == NotificationContent.NotificationThemeBackground.LIGHT) {
            contentViewBig.setInt(R.id.layout, "setBackgroundResource", R.color.backgroundLight);
        }
        Bitmap circleBig = getCircle(notif.getAccentColor(), UtilsDisplay.getDipInt(40));
        contentViewBig.setImageViewBitmap(R.id.imageBackground, circleBig);
        if (notif.getIconOverride() != null) {
            contentViewBig.setImageViewBitmap(R.id.image, notif.getIconOverride());
        } else {
            contentViewBig.setImageViewResource(R.id.image, notif.iconRes);
        }
        if(notif.getImageLarge() != null) {
            contentViewBig.setViewVisibility(R.id.imageLarge, View.VISIBLE);
            contentViewBig.setImageViewBitmap(R.id.imageLarge, notif.getImageLarge());
        } else {
            contentViewBig.setViewVisibility(R.id.imageLarge, View.GONE);
        }
        contentViewBig.setTextViewText(R.id.title, notif.title);
        if(!notif.content.trim().isEmpty()) {
            contentViewBig.setViewVisibility(R.id.text, View.VISIBLE);
            contentViewBig.setTextViewText(R.id.text, notif.content);
        } else {
            contentViewBig.setViewVisibility(R.id.text, View.GONE);
        }
        if (snooze) {
            contentViewBig.setViewVisibility(R.id.imageSnooze, View.VISIBLE);
            if (notif.getActions().size() == 3) {
                NotificationAction act1 = notif.getActions().get(0);
                contentViewBig.setImageViewResource(R.id.imageCancel, act1.getIconRes());
                contentViewBig.setOnClickPendingIntent(R.id.imageCancel, act1.getPendingIntent());
                NotificationAction act2 = notif.getActions().get(1);
                contentViewBig.setImageViewResource(R.id.imageSnooze, act2.getIconRes());
                contentViewBig.setOnClickPendingIntent(R.id.imageSnooze, act2.getPendingIntent());
                NotificationAction act3 = notif.getActions().get(2);
                contentViewBig.setImageViewResource(R.id.imageAck, act3.getIconRes());
                contentViewBig.setOnClickPendingIntent(R.id.imageAck, act3.getPendingIntent());
            }
        } else {
            contentViewBig.setViewVisibility(R.id.imageSnooze, View.GONE);
            if (notif.getActions().size() == 2) {
                NotificationAction act1 = notif.getActions().get(0);
                contentViewBig.setImageViewResource(R.id.imageCancel, act1.getIconRes());
                contentViewBig.setOnClickPendingIntent(R.id.imageCancel, act1.getPendingIntent());
                NotificationAction act3 = notif.getActions().get(1);
                contentViewBig.setImageViewResource(R.id.imageAck, act3.getIconRes());
                contentViewBig.setOnClickPendingIntent(R.id.imageAck, act3.getPendingIntent());
            }
        }
        return contentViewBig;
    }

    /**
     * Build a circle drawable of the given color
     *
     * @param color      Color
     * @param edgeLength Edge length of the square containing the circle
     * @return Bitmap circle
     */
    @NonNull
    private static Bitmap getCircle(final int color, final int edgeLength) {
        Bitmap circle = Bitmap.createBitmap(edgeLength, edgeLength, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circle);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawCircle(edgeLength / 2.0f, edgeLength / 2.0f, edgeLength / 2.0f, paint);
        return circle;
    }

}

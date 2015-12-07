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

import android.annotation.TargetApi;
import android.app.Notification;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.time.TimeItem;
import com.jamesmorrisstudios.appbaselibrary.time.UtilsTime;

/**
 * Notification handler class.
 * This generates and displays notifications given a list of reminder items
 * If using this class add this permission to your manifest <uses-permission android:name="android.permission.VIBRATE" />
 * <p/>
 * Created by James on 4/28/2015.
 */
public final class Notifier {

    /**
     * Dismiss the given notification
     *
     * @param id Id of the notification
     */
    public static void dismissNotification(int id) {
        // Gets an instance of the NotificationManager service
        NotificationManagerCompat mNotifyMgr = NotificationManagerCompat.from(AppBase.getContext());
        // cancels the notification
        mNotifyMgr.cancel(id);
    }

    /**
     * Builds and displays a notification with the given parameters
     *
     * @param notif Notification data
     */
    public static void buildNotification(NotificationContent notif) {
        TimeItem timeNow = UtilsTime.getTimeNow();
        Log.v("Notification shown", notif.getTitle() + " " + timeNow.getHourInTimeFormatString() + ":" + timeNow.getMinuteString() + " Vibrate: " + notif.getVibrate().getName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            buildNotificationSub(notif);
        } else {
            buildNotificationSubCompat(notif);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static void buildNotificationSub(NotificationContent notif) {
        Notification notification;

        Notification.Builder mBuilder;
        mBuilder = new Notification.Builder(AppBase.getContext())
                .setDefaults(getDefaults(notif))
                //.setSmallIcon(notif.getIconRes())
                .setContentTitle(notif.getTitle())
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(getPriority(notif))
                .setContentText(notif.getContent());

        if (notif.getIconOverride() != null) {
            mBuilder.setSmallIcon(Icon.createWithBitmap(notif.getIconOverride()));
        } else {
            mBuilder.setSmallIcon(notif.getIconRes());
        }
        if (notif.getVibrateCustom() == null) {
            if (notif.getVibrate() == NotificationContent.NotificationVibrate.DEFAULT) {
                //Leave as is
            } else {
                mBuilder.setVibrate(notif.getVibratePattern());
            }
        } else {
            mBuilder.setVibrate(notif.getVibrateCustom());
        }
        if (notif.hasTone()) {
            mBuilder.setSound(notif.getTone());
        }
        if (notif.getUseLed()) {
            mBuilder.setLights(notif.getLedColor(), notif.getLights().getPattern()[0], notif.getLights().getPattern()[1]);
        }
        if (notif.hasContentIntent()) {
            mBuilder.setContentIntent(notif.getContentIntent());
        }
        if (notif.hasDeleteIntent()) {
            mBuilder.setDeleteIntent(notif.getDeleteIntent());
        }
        if (notif.hasAccentColor()) {
            mBuilder.setColor(notif.getAccentColor());
        }
        if (notif.getOnGoing()) {
            mBuilder.setAutoCancel(false);
            mBuilder.setOngoing(true);
        } else {
            mBuilder.setAutoCancel(true);
            mBuilder.setOngoing(false);
        }

        Notification.WearableExtender wearableExtender = new Notification.WearableExtender();
        for (NotificationAction action : notif.getActions()) {
            mBuilder.addAction(action.getIconRes(), action.getText(), action.getPendingIntent());
            wearableExtender.addAction(new Notification.Action(action.getIconRes(), action.getText(), action.getPendingIntent()));
        }
        if (notif.getActions().size() > 0) {
            mBuilder.extend(wearableExtender);
        }

        if (notif.getType() == NotificationContent.NotificationType.CUSTOM) {
            notification = addCustomNotification(notif, mBuilder);
        } else if(notif.getType() == NotificationContent.NotificationType.CUSTOM_SNOOZE) {
            notification = addCustomSnoozeNotification(notif, mBuilder);
        } else {
            mBuilder.setStyle(new Notification.BigTextStyle().bigText(notif.getContent()));
            notification = mBuilder.build();
        }

        // Gets an instance of the NotificationManager service
        NotificationManagerCompat mNotifyMgr = NotificationManagerCompat.from(AppBase.getContext());
        // Builds the notification and issues it.
        Log.v("NOTIFIER", "Notify: " + notif.getId());
        mNotifyMgr.notify(notif.getId(), notification);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static Notification addCustomNotification(NotificationContent notif, Notification.Builder mBuilder) {
        mBuilder.setContent(getContentView(notif, false));
        Notification notification = mBuilder.build();
        notification.bigContentView = getContentBigView(notif, false);
        return notification;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static Notification addCustomSnoozeNotification(NotificationContent notif, Notification.Builder mBuilder) {
        mBuilder.setContent(getContentView(notif, true));
        Notification notification = mBuilder.build();
        notification.bigContentView = getContentBigView(notif, true);
        return notification;
    }

    private static void buildNotificationSubCompat(NotificationContent notif) {
        Notification notification;

        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(AppBase.getContext())
                .setDefaults(getDefaultsCompat(notif))
                .setSmallIcon(notif.getIconRes())
                .setContentTitle(notif.getTitle())
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(getPriorityCompat(notif))
                .setContentText(notif.getContent());

        if(notif.getVibrateCustom() == null) {
            if (notif.getVibrate() == NotificationContent.NotificationVibrate.DEFAULT) {
                //Leave as is
            } else {
                mBuilder.setVibrate(notif.getVibratePattern());
            }
        } else {
            mBuilder.setVibrate(notif.getVibrateCustom());
        }
        if (notif.hasTone()) {
            mBuilder.setSound(notif.getTone());
        }
        if (notif.getUseLed()) {
            mBuilder.setLights(notif.getLedColor(), 1000, 1500);
        }
        if (notif.hasContentIntent()) {
            mBuilder.setContentIntent(notif.getContentIntent());
        }
        if (notif.hasDeleteIntent()) {
            mBuilder.setDeleteIntent(notif.getDeleteIntent());
        }
        if (notif.hasAccentColor()) {
            mBuilder.setColor(notif.getAccentColor());
        }
        if (notif.getOnGoing()) {
            mBuilder.setAutoCancel(false);
            mBuilder.setOngoing(true);
        } else {
            mBuilder.setAutoCancel(true);
            mBuilder.setOngoing(false);
        }

        NotificationCompat.WearableExtender wearableExtender = new NotificationCompat.WearableExtender();
        for (NotificationAction action : notif.getActions()) {
            mBuilder.addAction(action.getIconRes(), action.getText(), action.getPendingIntent());
            wearableExtender.addAction(new NotificationCompat.Action(action.getIconRes(), action.getText(), action.getPendingIntent()));
        }
        if(notif.getActions().size() > 0) {
            mBuilder.extend(wearableExtender);
        }

        if (notif.getType() == NotificationContent.NotificationType.CUSTOM) {
            notification = addCustomNotificationCompat(notif, mBuilder);
        } else if(notif.getType() == NotificationContent.NotificationType.CUSTOM_SNOOZE) {
            notification = addCustomSnoozeNotificationCompat(notif, mBuilder);
        } else {
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(notif.getContent()));
            notification = mBuilder.build();
        }

        // Gets an instance of the NotificationManager service
        NotificationManagerCompat mNotifyMgr = NotificationManagerCompat.from(AppBase.getContext());
        // Builds the notification and issues it.
        Log.v("NOTIFIER", "Notify: " + notif.getId());
        mNotifyMgr.notify(notif.getId(), notification);
    }

    private static Notification addCustomNotificationCompat(NotificationContent notif, NotificationCompat.Builder mBuilder) {
        mBuilder.setContent(getContentView(notif, false));
        Notification notification = mBuilder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification.bigContentView = getContentBigView(notif, false);
        }
        return notification;
    }

    private static Notification addCustomSnoozeNotificationCompat(NotificationContent notif, NotificationCompat.Builder mBuilder) {
        mBuilder.setContent(getContentView(notif, true));
        Notification notification = mBuilder.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification.bigContentView = getContentBigView(notif, true);
        }
        return notification;
    }

    private static RemoteViews getContentView(NotificationContent notif, boolean snooze) {
        RemoteViews contentView;
        if (notif.getThemeText() == NotificationContent.NotificationThemeText.LIGHT_TEXT) {
            contentView = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_dark);
        } else {
            contentView = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_light);
        }

        if(notif.getTheme() == NotificationContent.NotificationTheme.DARK) {
            contentView.setInt(R.id.layout, "setBackgroundResource", R.color.backgroundDark);
        } else if(notif.getTheme() == NotificationContent.NotificationTheme.LIGHT) {
            contentView.setInt(R.id.layout, "setBackgroundResource", R.color.backgroundLight);
        }

        if(notif.getIconOverride() != null) {
            contentView.setImageViewBitmap(R.id.image, notif.getIconOverride());
        } else {
            contentView.setImageViewResource(R.id.image, notif.getIconRes());
        }
        Bitmap circle = getCircle(notif.getAccentColor(), Utils.getDipInt(40));
        contentView.setImageViewBitmap(R.id.imageBackground, circle);
        contentView.setTextViewText(R.id.title, notif.getTitle());
        contentView.setTextViewText(R.id.text, notif.getContent());

        if(snooze) {
            contentView.setViewVisibility(R.id.imageSnooze, View.VISIBLE);
            if(notif.getActions().size() == 3) {
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

    private static RemoteViews getContentBigView(NotificationContent notif, boolean snooze) {
        RemoteViews contentViewBig;
        if (notif.getThemeText() == NotificationContent.NotificationThemeText.LIGHT_TEXT) {
            contentViewBig = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_big_dark);
        } else {
            contentViewBig = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_big_light);
        }

        if(notif.getTheme() == NotificationContent.NotificationTheme.DARK) {
            contentViewBig.setInt(R.id.layout, "setBackgroundResource", R.color.backgroundDark);
        } else if(notif.getTheme() == NotificationContent.NotificationTheme.LIGHT) {
            contentViewBig.setInt(R.id.layout, "setBackgroundResource", R.color.backgroundLight);
        }

        Bitmap circleBig = getCircle(notif.getAccentColor(), Utils.getDipInt(40));
        contentViewBig.setImageViewBitmap(R.id.imageBackground, circleBig);
        if(notif.getIconOverride() != null) {
            contentViewBig.setImageViewBitmap(R.id.image, notif.getIconOverride());
        } else {
            contentViewBig.setImageViewResource(R.id.image, notif.getIconRes());
        }
        contentViewBig.setTextViewText(R.id.title, notif.getTitle());
        contentViewBig.setTextViewText(R.id.text, notif.getContent());
        if(snooze) {
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

    @TargetApi(Build.VERSION_CODES.M)
    private static int getDefaults(NotificationContent notif) {
        int defaults = 0;
        if (notif.getVibrate() == NotificationContent.NotificationVibrate.DEFAULT && notif.getVibrateCustom() == null) {
            defaults |= Notification.DEFAULT_VIBRATE;
        }
        return defaults;
    }

    @TargetApi(Build.VERSION_CODES.M)
    private static int getPriority(NotificationContent notif) {
        if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.DEFAULT) {
            return Notification.PRIORITY_DEFAULT;
        } else if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.LOW) {
            return Notification.PRIORITY_LOW;
        } else if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.HIGH) {
            return Notification.PRIORITY_HIGH;
        } else if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.MAX) {
            return Notification.PRIORITY_MAX;
        } else {
            return Notification.PRIORITY_DEFAULT;
        }
    }

    private static int getDefaultsCompat(NotificationContent notif) {
        int defaults = 0;
        if (notif.getVibrate() == NotificationContent.NotificationVibrate.DEFAULT && notif.getVibrateCustom() == null) {
            defaults |= NotificationCompat.DEFAULT_VIBRATE;
        }
        return defaults;
    }

    private static int getPriorityCompat(NotificationContent notif) {
        if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.DEFAULT) {
            return NotificationCompat.PRIORITY_DEFAULT;
        } else if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.LOW) {
            return NotificationCompat.PRIORITY_LOW;
        } else if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.HIGH) {
            return NotificationCompat.PRIORITY_HIGH;
        } else if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.MAX) {
            return NotificationCompat.PRIORITY_MAX;
        } else {
            return NotificationCompat.PRIORITY_DEFAULT;
        }
    }

    /**
     * Build a circle drawable of the given color
     *
     * @param color      Color
     * @param edgeLength Edge length of the square containing the circle
     * @return Bitmap circle
     */
    private static Bitmap getCircle(int color, int edgeLength) {
        Bitmap circle = Bitmap.createBitmap(edgeLength, edgeLength, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circle);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawCircle(edgeLength / 2.0f, edgeLength / 2.0f, edgeLength / 2.0f, paint);
        return circle;
    }

}

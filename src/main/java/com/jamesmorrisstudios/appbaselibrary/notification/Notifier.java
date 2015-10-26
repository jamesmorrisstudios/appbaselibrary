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
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
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
     * Builds and displays a notification with the given parameters
     *
     * @param notif Notification data
     */
    public static void buildNotification(NotificationContent notif) {

        TimeItem timeNow = UtilsTime.getTimeNow();
        Log.v("Notification shown", notif.getTitle() + " " + timeNow.getHourInTimeFormatString() + ":" + timeNow.getMinuteString() + " Vibrate: " + notif.getVibrate().name);

        int defaults = 0;

        if (notif.getVibrate() == NotificationContent.NotificationVibrate.DEFAULT) {
            defaults |= NotificationCompat.DEFAULT_VIBRATE;
        }
        int priority = NotificationCompat.PRIORITY_DEFAULT;
        if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.DEFAULT) {
            priority = NotificationCompat.PRIORITY_DEFAULT;
        } else if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.LOW) {
            priority = NotificationCompat.PRIORITY_LOW;
        } else if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.HIGH) {
            priority = NotificationCompat.PRIORITY_HIGH;
        } else if (notif.getNotificationPriority() == NotificationContent.NotificationPriority.MAX) {
            priority = NotificationCompat.PRIORITY_MAX;
        }

        Notification notification;

        Bundle bundle = new Bundle();
        bundle.putInt(Integer.toString(notif.getNotifCounter()), notif.getNotifCounter());

        NotificationCompat.Builder mBuilder;
        mBuilder = new NotificationCompat.Builder(AppBase.getContext())
                .setExtras(bundle)
                .setDefaults(defaults)
                .setSmallIcon(notif.getIconRes())
                .setContentTitle(notif.getTitle())
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setPriority(priority)
                .setContentText(notif.getContent());

        if (notif.getVibrate() == NotificationContent.NotificationVibrate.DEFAULT) {
            //Leave as is
        } else {
            mBuilder.setVibrate(notif.getVibratePattern());
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
            notification = addCustomNotifcation(notif, mBuilder);
        } else if(notif.getType() == NotificationContent.NotificationType.CUSTOM_SNOOZE) {
            notification = addCustomSnoozeNotifcation(notif, mBuilder);
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

    private static Notification addCustomNotifcation(NotificationContent notif, NotificationCompat.Builder mBuilder) {
        //Compact
        RemoteViews contentView;
        if (notif.getTheme() == NotificationContent.NotificationTheme.LIGHT) {
            contentView = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_light);
        } else {
            contentView = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_dark);
        }

        contentView.setViewVisibility(R.id.imageSnooze, View.GONE);

        contentView.setImageViewResource(R.id.image, notif.getIconRes());
        Bitmap circle = getCircle(notif.getAccentColor(), Utils.getDipInt(40));
        contentView.setImageViewBitmap(R.id.imageBackground, circle);
        contentView.setTextViewText(R.id.title, notif.getTitle());
        contentView.setTextViewText(R.id.text, notif.getContent());

        if (notif.getActions().size() == 2) {
            NotificationAction act1 = notif.getActions().get(0);
            contentView.setImageViewResource(R.id.imageCancel, act1.getIconRes());
            contentView.setOnClickPendingIntent(R.id.imageCancel, act1.getPendingIntent());

            NotificationAction act3 = notif.getActions().get(1);
            contentView.setImageViewResource(R.id.imageAck, act3.getIconRes());
            contentView.setOnClickPendingIntent(R.id.imageAck, act3.getPendingIntent());
        }
        mBuilder.setContent(contentView);
        Notification notification = mBuilder.build();

        //Big
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Log.v("NOTIFIER", "Adding big content view");
            RemoteViews contentViewBig;
            if (notif.getTheme() == NotificationContent.NotificationTheme.LIGHT) {
                contentViewBig = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_big_light);
            } else {
                contentViewBig = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_big_dark);
            }
            contentViewBig.setViewVisibility(R.id.imageSnooze, View.GONE);
            Bitmap circleBig = getCircle(notif.getAccentColor(), Utils.getDipInt(40));
            contentViewBig.setImageViewBitmap(R.id.imageBackground, circleBig);
            contentViewBig.setImageViewResource(R.id.image, notif.getIconRes());
            contentViewBig.setTextViewText(R.id.title, notif.getTitle());
            contentViewBig.setTextViewText(R.id.text, notif.getContent());

            if (notif.getActions().size() == 2) {
                NotificationAction act1 = notif.getActions().get(0);
                contentViewBig.setImageViewResource(R.id.imageCancel, act1.getIconRes());
                contentViewBig.setOnClickPendingIntent(R.id.imageCancel, act1.getPendingIntent());

                NotificationAction act3 = notif.getActions().get(1);
                contentViewBig.setImageViewResource(R.id.imageAck, act3.getIconRes());
                contentViewBig.setOnClickPendingIntent(R.id.imageAck, act3.getPendingIntent());
            }
            notification.bigContentView = contentViewBig;
        }
        return notification;
    }

    private static Notification addCustomSnoozeNotifcation(NotificationContent notif, NotificationCompat.Builder mBuilder) {
        //Compact
        RemoteViews contentView;
        if (notif.getTheme() == NotificationContent.NotificationTheme.LIGHT) {
            contentView = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_light);
        } else {
            contentView = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_dark);
        }

        contentView.setImageViewResource(R.id.image, notif.getIconRes());
        Bitmap circle = getCircle(notif.getAccentColor(), Utils.getDipInt(40));
        contentView.setImageViewBitmap(R.id.imageBackground, circle);
        contentView.setTextViewText(R.id.title, notif.getTitle());
        contentView.setTextViewText(R.id.text, notif.getContent());

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
        mBuilder.setContent(contentView);
        Notification notification = mBuilder.build();

        //Big
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Log.v("NOTIFIER", "Adding big content view");
            RemoteViews contentViewBig;
            if (notif.getTheme() == NotificationContent.NotificationTheme.LIGHT) {
                contentViewBig = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_big_light);
            } else {
                contentViewBig = new RemoteViews(AppBase.getContext().getPackageName(), R.layout.notification_big_dark);
            }
            Bitmap circleBig = getCircle(notif.getAccentColor(), Utils.getDipInt(40));
            contentViewBig.setImageViewBitmap(R.id.imageBackground, circleBig);
            contentViewBig.setImageViewResource(R.id.image, notif.getIconRes());
            contentViewBig.setTextViewText(R.id.title, notif.getTitle());
            contentViewBig.setTextViewText(R.id.text, notif.getContent());

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
            notification.bigContentView = contentViewBig;
        }
        return notification;
    }

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

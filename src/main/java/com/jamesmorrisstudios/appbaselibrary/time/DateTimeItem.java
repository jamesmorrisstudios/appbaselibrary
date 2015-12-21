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

package com.jamesmorrisstudios.appbaselibrary.time;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

/**
 * Object wrapper that holds both date and time items
 * <p/>
 * Created by James on 4/28/2015.
 */
public final class DateTimeItem implements Comparable<DateTimeItem> {
    @SerializedName("dateItem")
    public DateItem dateItem;
    @SerializedName("timeItem")
    public TimeItem timeItem;

    /**
     * Constructor
     */
    public DateTimeItem() {
        this.dateItem = new DateItem();
        this.timeItem = new TimeItem();
    }

    /**
     * Constructor
     *
     * @param dateItem Date Item
     * @param timeItem Time Item
     */
    public DateTimeItem(@NonNull final DateItem dateItem, @NonNull final TimeItem timeItem) {
        this.dateItem = dateItem;
        this.timeItem = timeItem;
    }

    /**
     * Constructor that makes a deep copy of the item
     *
     * @param dateTimeItem dateTimeItem to copy
     */
    public DateTimeItem(@NonNull final DateTimeItem dateTimeItem) {
        this.dateItem = new DateItem(dateTimeItem.dateItem);
        this.timeItem = new TimeItem(dateTimeItem.timeItem);
    }

    /**
     * Constructor
     *
     * @param timeMillis Unix time in milliseconds
     */
    public DateTimeItem(final long timeMillis) {
        setDateTimeFromMillis(timeMillis);
    }

    /**
     * @param item Date Time Item
     * @return String packaged version for use in saving
     */
    @NonNull
    public static String encodeToString(@NonNull final DateTimeItem item) {
        return DateItem.encodeToString(item.dateItem) + "," + TimeItem.encodeToString(item.timeItem);
    }

    /**
     * @param item String packaged version previously encoded with encode to string
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem decodeFromString(@NonNull final String item) {
        String[] vals = item.split(",");
        if (vals.length != 2) {
            return new DateTimeItem(new DateItem(0, 0, 0), new TimeItem(0, 0));
        }
        return new DateTimeItem(DateItem.decodeFromString(vals[0]), TimeItem.decodeFromString(vals[1]));
    }

    /**
     * @return Unix time in milliseconds
     */
    public final long getTimeMillis() {
        return UtilsTime.getTimeMillis(this);
    }

    /**
     * @param timeMillis Unix time in milliseconds
     */
    public final void setDateTimeFromMillis(final long timeMillis) {
        DateTimeItem dateTime = UtilsTime.getDateTime(timeMillis);
        this.dateItem = dateTime.dateItem;
        this.timeItem = dateTime.timeItem;
        validateItem();
    }

    /**
     * Validate that the date and time are within parameters
     */
    public final void validateItem() {
        if (dateItem == null) {
            dateItem = new DateItem();
        } else {
            dateItem.validateItem();
        }
        if (timeItem == null) {
            timeItem = new TimeItem();
        } else {
            timeItem.validateItem();
        }
    }

    /**
     * @return A copy of this object
     */
    @NonNull
    public final DateTimeItem copy() {
        return new DateTimeItem(this);
    }

    /**
     * @param another compare to dateTimeItem
     * @return -1 if before dateTimeItem, 0 if equal, 1 if after dateTimeItem
     */
    @Override
    public final int compareTo(@NonNull final DateTimeItem another) {
        int compDate = this.dateItem.compareTo(another.dateItem);
        int compTime = this.timeItem.compareTo(another.timeItem);
        if (compDate != 0) {
            return compDate;
        }
        return compTime;
    }

}

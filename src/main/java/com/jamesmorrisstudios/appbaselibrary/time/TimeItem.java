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
import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.math.UtilsMath;

/**
 * Time item for scheduling. This assumes 24 hour time. Adjust to AM, PM as needed
 * <p/>
 * Created by James on 4/28/2015.
 */
public final class TimeItem implements Comparable<TimeItem> {
    @SerializedName("hour")
    public int hour;
    @SerializedName("minute")
    public int minute;

    /**
     * Constructor
     */
    public TimeItem() {
        this.hour = 0;
        this.minute = 0;
    }

    /**
     * Constructor
     *
     * @param hour   Starting hour
     * @param minute Starting minute
     */
    public TimeItem(final int hour, final int minute) {
        this.hour = hour;
        this.minute = minute;
        validateItem();
    }

    /**
     * Constructor
     *
     * @param totalMinutes Minutes from midnight
     */
    public TimeItem(final int totalMinutes) {
        this.hour = totalMinutes / 60;
        this.minute = totalMinutes % 60;
        validateItem();
    }

    /**
     * Constructor deep copy
     *
     * @param timeItem timeItem to copy
     */
    public TimeItem(@NonNull final TimeItem timeItem) {
        this.hour = timeItem.hour;
        this.minute = timeItem.minute;
    }

    /**
     * Constructor
     *
     * @param timeMillis Unix time in milliseconds
     */
    public TimeItem(final long timeMillis) {
        setTimeFromMillis(timeMillis);
    }

    /**
     * @param item Time Item
     * @return String packaged version for use in saving
     */
    @NonNull
    public static String encodeToString(@NonNull final TimeItem item) {
        return item.hour + ":" + item.minute;
    }

    /**
     * @param item String packaged version previously encoded with encode to string
     * @return Time Item
     */
    @NonNull
    public static TimeItem decodeFromString(@NonNull final String item) {
        String[] vals = item.split(":");
        if (vals.length != 2) {
            return new TimeItem(0, 0);
        }
        return new TimeItem(Utils.stringToInt(vals[0], 0), Utils.stringToInt(vals[1], 0));
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
    public final void setTimeFromMillis(final long timeMillis) {
        TimeItem timeItem = UtilsTime.getTime(timeMillis);
        this.hour = timeItem.hour;
        this.minute = timeItem.minute;
        validateItem();
    }

    /**
     * Validates that the time is valid
     */
    public final void validateItem() {
        this.hour = UtilsMath.inBoundsInt(0, 23, this.hour);
        this.minute = UtilsMath.inBoundsInt(0, 59, this.minute);
    }

    /**
     * @return A copy of this object
     */
    @NonNull
    public final TimeItem copy() {
        return new TimeItem(this);
    }

    /**
     * @return True if device is set to 24 hour time
     */
    public final boolean is24Hour() {
        return UtilsTime.is24HourTime();
    }

    /**
     * @return True if AM (check if 24 hour time before using this)
     */
    public final boolean isAM() {
        return UtilsTime.isAM(hour);
    }

    /**
     * @return Get hour in current time format (12 hour or 24 hour)
     */
    public final int getHourInTimeFormat() {
        return UtilsTime.getHourInTimeFormat(hour);
    }

    /**
     * @return Get hour in current time format (12 hour or 24 hour) as a string
     */
    @NonNull
    public final String getHourInTimeFormatString() {
        return Integer.toString(UtilsTime.getHourInTimeFormat(hour));
    }

    /**
     * @return Minutes value in formatted string
     */
    @NonNull
    public final String getMinuteString() {
        return String.format("%02d", minute);
    }

    /**
     * @return Number of minutes after midnight (hour == 0 and minute == 0)
     */
    public final int toMinutes() {
        return this.hour * 60 + this.minute;
    }

    /**
     * @param obj Object to compare with
     * @return True if equal, false otherwise
     */
    @Override
    public final boolean equals(@Nullable final Object obj) {
        if (obj != null && obj instanceof TimeItem) {
            TimeItem item = (TimeItem) obj;
            return this.hour == item.hour && this.minute == item.minute;
        } else {
            return false;
        }
    }

    /**
     * @param another compare to TimeItem
     * @return -1 if before TimeItem, 0 if equal, 1 if after TimeItem
     */
    @Override
    public final int compareTo(@NonNull final TimeItem another) {
        if (this.hour < another.hour) {
            return -1;
        }
        if (this.hour > another.hour) {
            return 1;
        }
        //Hour equal at this point
        if (this.minute < another.minute) {
            return -1;
        }
        if (this.minute > another.minute) {
            return 1;
        }
        //Hour, and minute equal at this point
        return 0;
    }

}

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
 * Date item
 * <p/>
 * Created by James on 4/28/2015.
 */
public final class DateItem implements Comparable<DateItem> {
    @SerializedName("year")
    public int year;
    @SerializedName("month")
    public int month;
    @SerializedName("dayOfMonth")
    public int dayOfMonth;

    /**
     * Constructor
     */
    public DateItem() {
        this.year = 1970;
        this.month = 0;
        this.dayOfMonth = 1;
    }

    /**
     * @param year       The year whatever it actually is ex. 2015
     * @param month      The month 0-11
     * @param dayOfMonth The day of the month 1-31
     */
    public DateItem(final int year, final int month, final int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        validateItem();
    }

    /**
     * Constructor deep copy of existing date item
     *
     * @param dateItem Date Item
     */
    public DateItem(@NonNull final DateItem dateItem) {
        this.year = dateItem.year;
        this.month = dateItem.month;
        this.dayOfMonth = dateItem.dayOfMonth;
    }

    /**
     * Constructor from unixtime
     *
     * @param timeMillis Unixtime in milliseconds
     */
    public DateItem(final long timeMillis) {
        setDateFromMillis(timeMillis);
    }

    /**
     * @param item The date item
     * @return String packaged version for use in saving
     */
    @NonNull
    public static String encodeToString(@NonNull final DateItem item) {
        return item.year + ":" + item.month + ":" + item.dayOfMonth;
    }

    /**
     * @param item String previously encoded with encode to string
     * @return The Date item
     */
    @NonNull
    public static DateItem decodeFromString(@NonNull final String item) {
        String[] vals = item.split(":");
        if (vals.length != 3) {
            return new DateItem(0, 0, 0);
        }
        return new DateItem(Utils.stringToInt(vals[0], 0), Utils.stringToInt(vals[1], 0), Utils.stringToInt(vals[2], 0));
    }

    /**
     * @return Unixtime in milliseconds
     */
    public final long getTimeMillis() {
        return UtilsTime.getTimeMillis(this);
    }

    /**
     * @param timeMillis Unixtime in milliseconds
     */
    public final void setDateFromMillis(final long timeMillis) {
        DateItem dateTime = UtilsTime.getDate(timeMillis);
        this.year = dateTime.year;
        this.month = dateTime.month;
        this.dayOfMonth = dateTime.dayOfMonth;
        validateItem();
    }

    /**
     * Validate that the date range is valid. Note that this still may not represent a valid date.
     */
    public final void validateItem() {
        this.year = UtilsMath.inBoundsInt(1970, 3000, this.year);
        this.month = UtilsMath.inBoundsInt(0, 11, this.month);
        this.dayOfMonth = UtilsMath.inBoundsInt(1, 31, this.dayOfMonth);
    }

    /**
     * @return A copy of this object
     */
    @NonNull
    public final DateItem copy() {
        return new DateItem(this.year, this.month, this.dayOfMonth);
    }

    /**
     * @param obj Object to compare with
     * @return True if equal, false otherwise
     */
    @Override
    public final boolean equals(@Nullable final Object obj) {
        if (obj != null && obj instanceof DateItem) {
            DateItem item = (DateItem) obj;
            return this.year == item.year && this.month == item.month && this.dayOfMonth == item.dayOfMonth;
        } else {
            return false;
        }
    }

    /**
     * @param another compare to dateItem
     * @return -1 if before dateItem, 0 if equal, 1 if after dateItem
     */
    @Override
    public final int compareTo(@NonNull final DateItem another) {
        if (this.year < another.year) {
            return -1;
        }
        if (this.year > another.year) {
            return 1;
        }
        //Year equal at this point
        if (this.month < another.month) {
            return -1;
        }
        if (this.month > another.month) {
            return 1;
        }
        //Year and month equal at this point
        if (this.dayOfMonth < another.dayOfMonth) {
            return -1;
        }
        if (this.dayOfMonth > another.dayOfMonth) {
            return 1;
        }
        //Year, month, and day equal at this point
        return 0;
    }

}

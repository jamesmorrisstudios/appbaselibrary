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

import java.util.Calendar;
import java.util.Date;

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
     * @param year       The year whatever it actually is ex. 2015
     * @param month      The month 0-11
     * @param dayOfMonth The day of the month 1-31
     */
    public DateItem(int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
    }

    public DateItem(DateItem dateItem) {
        this.year = dateItem.year;
        this.month = dateItem.month;
        this.dayOfMonth = dateItem.dayOfMonth;
    }

    /**
     * @return A copy of this object
     */
    @NonNull
    public final DateItem copy() {
        return new DateItem(this.year, this.month, this.dayOfMonth);
    }

    /**
     * @param item The date item
     * @return String packaged version for use in saving
     */
    @NonNull
    public static String encodeToString(@NonNull DateItem item) {
        return item.year + ":" + item.month + ":" + item.dayOfMonth;
    }

    /**
     * @param item String previously encoded with encode to string
     * @return The Date item
     */
    @NonNull
    public static DateItem decodeFromString(@NonNull String item) {
        String[] vals = item.split(":");
        if (vals.length != 3) {
            return new DateItem(0, 0, 0);
        }
        return new DateItem(Utils.stringToInt(vals[0], 0), Utils.stringToInt(vals[1], 0), Utils.stringToInt(vals[2], 0));
    }

    /**
     * @param obj Object to compare with
     * @return True if equal, false otherwise
     */
    @Override
    public final boolean equals(@Nullable Object obj) {
        if (obj != null && obj instanceof DateItem) {
            DateItem item = (DateItem) obj;
            return this.year == item.year && this.month == item.month && this.dayOfMonth == item.dayOfMonth;
        } else {
            return false;
        }
    }

    //-1 is this is before another
    //1 is this is after another
    //0 if the same
    @Override
    public int compareTo(@NonNull DateItem another) {
        if(this.year < another.year) {
            return -1;
        }
        if(this.year > another.year) {
            return 1;
        }
        //Year equal at this point
        if(this.month < another.month) {
            return -1;
        }
        if(this.month > another.month) {
            return 1;
        }
        //Year and month equal at this point
        if(this.dayOfMonth < another.dayOfMonth) {
            return -1;
        }
        if(this.dayOfMonth > another.dayOfMonth) {
            return 1;
        }
        //Year, month, and day equal at this point
        return 0;
    }
}

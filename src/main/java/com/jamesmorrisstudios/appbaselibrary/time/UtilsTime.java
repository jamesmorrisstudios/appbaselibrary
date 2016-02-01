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
import android.support.annotation.StringRes;
import android.text.format.DateFormat;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.Utils;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Time related utility functions
 * <p/>
 * Created by James on 4/28/2015.
 */
public final class UtilsTime {

    /**
     * @return True if the user preference is 24 hour time
     */
    public static boolean is24HourTime() {
        return DateFormat.is24HourFormat(AppBase.getContext());
    }

    /**
     * @param hour Hour in 24 hour (calendar form)
     * @return True if am, false if pm
     */
    public static boolean isAM(final int hour) {
        return hour < 12;
    }

    /**
     * Converts the 24 hour calendar form to the user preferred style
     *
     * @param hour Hour in 24 hour (calendar form)
     * @return Hour in the user preferred style
     */
    public static int getHourInTimeFormat(final int hour) {
        if (is24HourTime() || hour <= 12 && hour >= 1) {
            return hour;
        } else if (hour >= 13) {
            return hour - 12;
        } else {
            return 12;
        }
    }

    /**
     * @return Android Calendar set for current time with users chosen first day of the week
     */
    @NonNull
    public static Calendar getCalendar() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int ordinal = getFirstDayOfWeekOrdinal();
        if (ordinal != DayOfWeek.AUTOMATIC.ordinal()) {
            switch (DayOfWeek.values()[ordinal]) {
                case SUNDAY:
                    calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                    break;
                case MONDAY:
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    break;
                case TUESDAY:
                    calendar.setFirstDayOfWeek(Calendar.TUESDAY);
                    break;
                case WEDNESDAY:
                    calendar.setFirstDayOfWeek(Calendar.WEDNESDAY);
                    break;
                case THURSDAY:
                    calendar.setFirstDayOfWeek(Calendar.THURSDAY);
                    break;
                case FRIDAY:
                    calendar.setFirstDayOfWeek(Calendar.FRIDAY);
                    break;
                case SATURDAY:
                    calendar.setFirstDayOfWeek(Calendar.SATURDAY);
                    break;
                default:
                    calendar.setFirstDayOfWeek(Calendar.SUNDAY);
                    break;
            }
        }
        return calendar;
    }

    /**
     * @param timeMillis Unix time in milliseconds
     * @return Android Calendar set for given time with users chosen first day of the week
     */
    @NonNull
    public static Calendar getCalendar(final long timeMillis) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(timeMillis);
        return calendar;
    }

    /**
     * @param dateTime Date Time Item
     * @return Android Calendar
     */
    @NonNull
    public static Calendar getCalendar(@NonNull final DateTimeItem dateTime) {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.YEAR, dateTime.dateItem.year);
        calendar.set(Calendar.MONTH, dateTime.dateItem.month);
        calendar.set(Calendar.DAY_OF_MONTH, dateTime.dateItem.dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, dateTime.timeItem.hour);
        calendar.set(Calendar.MINUTE, dateTime.timeItem.minute);
        return calendar;
    }

    /**
     * @param date Date Item
     * @return Android Calendar
     */
    @NonNull
    public static Calendar getCalendar(@NonNull final DateItem date) {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.YEAR, date.year);
        calendar.set(Calendar.MONTH, date.month);
        calendar.set(Calendar.DAY_OF_MONTH, date.dayOfMonth);
        return calendar;
    }

    /**
     * @param time Time Item
     * @return Android Calendar
     */
    @NonNull
    public static Calendar getCalendar(@NonNull final TimeItem time) {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, time.hour);
        calendar.set(Calendar.MINUTE, time.minute);
        return calendar;
    }

    /**
     * @param calendar Android Calendar
     * @return Unix time in milliseconds
     */
    public static long getTimeMillis(@NonNull final Calendar calendar) {
        return calendar.getTimeInMillis();
    }

    /**
     * @param dateTime Date Time Item
     * @return Unix time in milliseconds
     */
    public static long getTimeMillis(@NonNull final DateTimeItem dateTime) {
        return getCalendar(dateTime).getTimeInMillis();
    }

    /**
     * @param date Date Item
     * @return Unix time in milliseconds
     */
    public static long getTimeMillis(@NonNull final DateItem date) {
        return getCalendar(date).getTimeInMillis();
    }

    /**
     * @param timeItem Time Item
     * @return Unix time in milliseconds
     */
    public static long getTimeMillis(@NonNull final TimeItem timeItem) {
        return getCalendar(timeItem).getTimeInMillis();
    }

    /**
     * @param timeMillis Unix time in milliseconds
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem getDateTime(final long timeMillis) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(timeMillis);
        return getDateTime(calendar);
    }

    /**
     * @param timeMillis Unix time in milliseconds
     * @return Date Item
     */
    @NonNull
    public static DateItem getDate(final long timeMillis) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(timeMillis);
        return getDate(calendar);
    }

    /**
     * @param timeMillis Unix time in milliseconds
     * @return Time Item
     */
    @NonNull
    public static TimeItem getTime(final long timeMillis) {
        Calendar calendar = getCalendar();
        calendar.setTimeInMillis(timeMillis);
        return getTime(calendar);
    }

    /**
     * @param calendar Android Calendar
     * @return Date
     */
    @NonNull
    public static Date calendarToDate(@NonNull final Calendar calendar) {
        return calendar.getTime();
    }

    /**
     * @param date Date
     * @return Android Calendar
     */
    @NonNull
    public static Calendar dateToCalendar(@NonNull final Date date) {
        Calendar calendar = getCalendar();
        calendar.setTime(date);
        return calendar;
    }

    /**
     * @param dateItem Date Item
     * @return Date
     */
    @NonNull
    public static Date dateItemToDate(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        return calendarToDate(calendar);
    }

    /**
     * @param date Date
     * @return Date Item
     */
    @NonNull
    public static DateItem dateToDateItem(@NonNull final Date date) {
        Calendar calendar = dateToCalendar(date);
        return getDate(calendar);
    }

    /**
     * @param dateArr Date array
     * @return Date Item array
     */
    @NonNull
    public static ArrayList<DateItem> dateArrToDateItemArr(@NonNull final List<Date> dateArr) {
        ArrayList<DateItem> dateItemArr = new ArrayList<>();
        for (Date date : dateArr) {
            dateItemArr.add(dateToDateItem(date));
        }
        return dateItemArr;
    }

    /**
     * @param dateItemArr Date Item array
     * @return Date Array
     */
    @NonNull
    public static ArrayList<Date> dateItemArrToDateArr(@NonNull final List<DateItem> dateItemArr) {
        ArrayList<Date> dateArr = new ArrayList<>();
        for (DateItem dateItem : dateItemArr) {
            dateArr.add(dateItemToDate(dateItem));
        }
        return dateArr;
    }

    /**
     * @param calendar Android Calendar
     * @return Time Item
     */
    @NonNull
    public static TimeItem getTime(@NonNull final Calendar calendar) {
        return new TimeItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    /**
     * @param calendar Android Calendar
     * @return Date Item
     */
    @NonNull
    public static DateItem getDate(@NonNull final Calendar calendar) {
        return new DateItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * @param calendar Android Calendar
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem getDateTime(@NonNull final Calendar calendar) {
        return new DateTimeItem(getDate(calendar), getTime(calendar));
    }

    /**
     * Gets the current time
     *
     * @return Current time
     */
    @NonNull
    public static TimeItem getTimeNow() {
        Calendar calendar = getCalendar();
        return new TimeItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    /**
     * Gets the current date
     *
     * @return Current date
     */
    @NonNull
    public static DateItem getDateNow() {
        Calendar calendar = getCalendar();
        return new DateItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Gets the current date time
     *
     * @return Current date time
     */
    @NonNull
    public static DateTimeItem getDateTimeNow() {
        return new DateTimeItem(getDateNow(), getTimeNow());
    }

    /**
     * @return Current day of week
     */
    @NonNull
    public static DayOfWeek getCurrentDayOfWeek() {
        Calendar calendar = getCalendar();
        return getDayFromCalendar(calendar.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * @return First day of week
     */
    @NonNull
    public static DayOfWeek getFirstDayOfWeek() {
        Calendar calendar = getCalendar();
        return getDayFromCalendar(calendar.getFirstDayOfWeek());
    }

    /**
     * @param firstDayOfWeek First day of week
     */
    public static void setFirstDayOfWeek(@NonNull final DayOfWeek firstDayOfWeek) {
        setFirstDayOfWeekOrdinal(firstDayOfWeek.ordinal());
    }

    /**
     * Internal
     *
     * @return Gets the index of the first day of the week
     */
    private static int getFirstDayOfWeekOrdinal() {
        return Prefs.getInt("com.jamesmorrisstudios.utilitieslibrary.UtilsTime", "dayOfWeekIndex", DayOfWeek.AUTOMATIC.ordinal());
    }

    /**
     * Internal
     *
     * @param ordinal Sets the index of the first day of the week
     */
    private static void setFirstDayOfWeekOrdinal(final int ordinal) {
        Prefs.putInt("com.jamesmorrisstudios.utilitieslibrary.UtilsTime", "dayOfWeekIndex", ordinal);
    }

    /**
     * @param calendarDay Android Calendar
     * @return Day of Week
     */
    @NonNull
    public static DayOfWeek getDayFromCalendar(final int calendarDay) {
        switch (calendarDay) {
            case Calendar.SUNDAY:
                return DayOfWeek.SUNDAY;
            case Calendar.MONDAY:
                return DayOfWeek.MONDAY;
            case Calendar.TUESDAY:
                return DayOfWeek.TUESDAY;
            case Calendar.WEDNESDAY:
                return DayOfWeek.WEDNESDAY;
            case Calendar.THURSDAY:
                return DayOfWeek.THURSDAY;
            case Calendar.FRIDAY:
                return DayOfWeek.FRIDAY;
            case Calendar.SATURDAY:
                return DayOfWeek.SATURDAY;
            default:
                return DayOfWeek.SUNDAY;
        }
    }

    /**
     * @return An array of each day of the week matching the users locale of first day
     */
    @NonNull
    public static DayOfWeek[] getWeekArray() {
        switch (getFirstDayOfWeek()) {
            case SUNDAY:
                return new DayOfWeek[]{DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
            case MONDAY:
                return new DayOfWeek[]{DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY};
            case TUESDAY:
                return new DayOfWeek[]{DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY};
            case WEDNESDAY:
                return new DayOfWeek[]{DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY};
            case THURSDAY:
                return new DayOfWeek[]{DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY};
            case FRIDAY:
                return new DayOfWeek[]{DayOfWeek.FRIDAY, DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY};
            case SATURDAY:
                return new DayOfWeek[]{DayOfWeek.SATURDAY, DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY};
            default:
                return new DayOfWeek[]{DayOfWeek.SUNDAY, DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SATURDAY};
        }
    }

    /**
     * @return An array of each day of the week matching the users locale of first day strings
     */
    @NonNull
    public static String[] getWeekNameArray() {
        DayOfWeek[] week = getWeekArray();
        String[] weekString = new String[7];
        for (int i = 0; i < week.length; i++) {
            weekString[i] = week[i].getName();
        }
        return weekString;
    }

    /**
     * @return An array of each day of the week matching the users locale of first day strings of the short names
     */
    @NonNull
    public static String[] getWeekNameShortArray() {
        DayOfWeek[] week = getWeekArray();
        String[] weekString = new String[7];
        for (int i = 0; i < week.length; i++) {
            weekString[i] = week[i].getNameShort();
        }
        return weekString;
    }

    /**
     * @param dateTime Date Time Item
     * @return Formatted time
     */
    @NonNull
    public static String getTimeFormatted(@NonNull final DateTimeItem dateTime) {
        Calendar calendar = getCalendar(dateTime);
        return DateFormat.getTimeFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * @param timeItem Time Item
     * @return Formatted time
     */
    @NonNull
    public static String getTimeFormatted(@NonNull final TimeItem timeItem) {
        Calendar calendar = getCalendar(timeItem);
        return DateFormat.getTimeFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * Returns the current date formatted to the users locale
     *
     * @param date Date to format
     * @return Formatted date
     */
    @NonNull
    public static String getDateFormatted(@NonNull final DateItem date) {
        Calendar calendar = getCalendar(date);
        return DateFormat.getDateFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * Returns the current date formatted to the users locale
     *
     * @param dateTime DateTime to format
     * @return Formatted date time
     */
    @NonNull
    public static String getDateTimeFormatted(@NonNull final DateTimeItem dateTime) {
        Calendar calendar = getCalendar(dateTime);
        return DateFormat.getDateFormat(AppBase.getContext()).format(calendar.getTime()) + " " + DateFormat.getTimeFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * @param date Date Item
     * @return Formatted date
     */
    @NonNull
    public static String getMediumDateFormatted(@NonNull final DateItem date) {
        Calendar calendar = getCalendar(date);
        return DateFormat.getMediumDateFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * @param dateTime Date Time Item
     * @return Formatted date time
     */
    @NonNull
    public static String getMediumDateTimeFormatted(@NonNull final DateTimeItem dateTime) {
        Calendar calendar = getCalendar(dateTime);
        return DateFormat.getMediumDateFormat(AppBase.getContext()).format(calendar.getTime()) + " " + DateFormat.getTimeFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * Returns the current date formatted to the users locale
     *
     * @param date Date to format
     * @return Formatted date
     */
    @NonNull
    public static String getLongDateFormatted(@NonNull final DateItem date) {
        Calendar calendar = getCalendar(date);
        return DateFormat.getLongDateFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * Returns the current date time formatted to the users locale
     *
     * @param dateTime DateTime to format
     * @return Formatted date time
     */
    @NonNull
    public static String getLongDateTimeFormatted(@NonNull final DateTimeItem dateTime) {
        Calendar calendar = getCalendar(dateTime);
        return DateFormat.getLongDateFormat(AppBase.getContext()).format(calendar.getTime()) + " " + DateFormat.getTimeFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * Date formatted to US locale in export format
     *
     * @param date Date Item
     * @return Formatted date
     */
    @NonNull
    public static String getUniversalDateFormatted(@NonNull final DateItem date) {
        Calendar calendar = getCalendar(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        return sdf.format(calendar.getTime());
    }

    /**
     * Date Time formatted to US locale in export format
     *
     * @param dateTime Date Time Item
     * @return Formatted date time
     */
    @NonNull
    public static String getUniversalDateTimeFormatted(@NonNull final DateTimeItem dateTime) {
        Calendar calendar = getCalendar(dateTime);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US);
        return sdf.format(calendar.getTime());
    }

    /**
     * Convert a millisecond duration to a string format
     *
     * @param millis A duration to convert to a string form
     * @return A string of the form "X Days Y Hours Z Minutes A Seconds".
     */
    @NonNull
    public static String getFormattedDuration(final long millis) {
        if (millis < 0) {
            throw new IllegalArgumentException("Duration must be greater than zero!");
        }
        long millis2 = millis;
        long days = TimeUnit.MILLISECONDS.toDays(millis2);
        millis2 -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(millis2);
        millis2 -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis2);
        millis2 -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis2);
        StringBuilder sb = new StringBuilder(64);
        if (days > 0) {
            sb.append(days);
            sb.append(days == 1 ? getStringSpaced(R.string.time_day) : getStringSpaced(R.string.time_days));
        }
        if (hours > 0) {
            sb.append(hours);
            sb.append(hours == 1 ? getStringSpaced(R.string.time_hour) : getStringSpaced(R.string.time_hours));
        }
        if (minutes > 0) {
            sb.append(minutes);
            sb.append(minutes == 1 ? getStringSpaced(R.string.time_minute) : getStringSpaced(R.string.time_minutes));
        }
        if (seconds > 0) {
            sb.append(seconds);
            sb.append(seconds == 1 ? getStringSpaced(R.string.time_second) : getStringSpaced(R.string.time_seconds));
        }
        return (sb.toString());
    }

    /**
     * @param millis A duration to convert to a string form
     * @return A string in the form mm:ss:ms
     */
    @NonNull
    public static String getFormattedDurationCompact(final long millis) {
        return String.format("%02d:%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)),
                millis - TimeUnit.SECONDS.toMillis(TimeUnit.MILLISECONDS.toSeconds(millis))
        );
    }

    /**
     * Internal helper for the formatted duration function
     *
     * @param stringId String id
     * @return The string with spaces around it
     */
    @NonNull
    private static String getStringSpaced(@StringRes final int stringId) {
        return " " + AppBase.getContext().getResources().getString(stringId) + " ";
    }

    /**
     * @param timeItem Time Item
     * @param amount   Amount to add. Can be negative
     * @return Time Item
     */
    @NonNull
    public static TimeItem addMinute(@NonNull final TimeItem timeItem, final int amount) {
        Calendar calendar = getCalendar(timeItem);
        calendar.add(Calendar.MINUTE, amount);
        return getTime(calendar);
    }

    /**
     * @param timeItem Time Item
     * @param amount   Amount to add. Can be negative
     * @return Time Item
     */
    @NonNull
    public static TimeItem addHour(@NonNull final TimeItem timeItem, final int amount) {
        Calendar calendar = getCalendar(timeItem);
        calendar.add(Calendar.HOUR_OF_DAY, amount);
        return getTime(calendar);
    }

    /**
     * @param dateItem Date Item
     * @param amount   Amount to add. Can be negative
     * @return Date Item
     */
    @NonNull
    public static DateItem addDayOfYear(@NonNull final DateItem dateItem, final int amount) {
        Calendar calendar = getCalendar(dateItem);
        calendar.add(Calendar.DAY_OF_YEAR, amount);
        return getDate(calendar);
    }

    /**
     * @param dateItem Date Item
     * @param amount   Amount to add. Can be negative
     * @return Date Item
     */
    @NonNull
    public static DateItem addWeekOfYear(@NonNull final DateItem dateItem, final int amount) {
        Calendar calendar = getCalendar(dateItem);
        calendar.add(Calendar.WEEK_OF_YEAR, amount);
        return getDate(calendar);
    }

    /**
     * @param dateItem Date Item
     * @param amount   Amount to add. Can be negative
     * @return Date Item
     */
    @NonNull
    public static DateItem addMonthOfYear(@NonNull final DateItem dateItem, final int amount) {
        Calendar calendar = getCalendar(dateItem);
        calendar.add(Calendar.MONTH, amount);
        return getDate(calendar);
    }

    /**
     * @param dateItem Date Item
     * @param amount   Amount to add. Can be negative
     * @return Date Item
     */
    @NonNull
    public static DateItem addYear(@NonNull final DateItem dateItem, final int amount) {
        Calendar calendar = getCalendar(dateItem);
        calendar.add(Calendar.YEAR, amount);
        return getDate(calendar);
    }

    /**
     * @param dateTimeItem Date Time Item
     * @param amount       Amount to add. Can be negative
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem addMinute(@NonNull final DateTimeItem dateTimeItem, final int amount) {
        Calendar calendar = getCalendar(dateTimeItem);
        calendar.add(Calendar.MINUTE, amount);
        return getDateTime(calendar);
    }

    /**
     * @param dateTimeItem Date Time Item
     * @param amount       Amount to add. Can be negative
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem addHour(@NonNull final DateTimeItem dateTimeItem, final int amount) {
        Calendar calendar = getCalendar(dateTimeItem);
        calendar.add(Calendar.HOUR_OF_DAY, amount);
        return getDateTime(calendar);
    }

    /**
     * @param dateTimeItem Date Time Item
     * @param amount       Amount to add. Can be negative
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem addDayOfYear(@NonNull final DateTimeItem dateTimeItem, final int amount) {
        Calendar calendar = getCalendar(dateTimeItem);
        calendar.add(Calendar.DAY_OF_YEAR, amount);
        return getDateTime(calendar);
    }

    /**
     * @param dateTimeItem Date Time Item
     * @param amount       Amount to add. Can be negative
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem addWeekOfYear(@NonNull final DateTimeItem dateTimeItem, final int amount) {
        Calendar calendar = getCalendar(dateTimeItem);
        calendar.add(Calendar.WEEK_OF_YEAR, amount);
        return getDateTime(calendar);
    }

    /**
     * @param dateTimeItem Date Time Item
     * @param amount       Amount to add. Can be negative
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem addMonthOfYear(@NonNull final DateTimeItem dateTimeItem, final int amount) {
        Calendar calendar = getCalendar(dateTimeItem);
        calendar.add(Calendar.MONTH, amount);
        return getDateTime(calendar);
    }

    /**
     * @param dateTimeItem Date Time Item
     * @param amount       Amount to add. Can be negative
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem addYear(@NonNull final DateTimeItem dateTimeItem, final int amount) {
        Calendar calendar = getCalendar(dateTimeItem);
        calendar.add(Calendar.YEAR, amount);
        return getDateTime(calendar);
    }

    /**
     * Assumes week starts on the first of the month. May not follow firstDayOfWeek
     *
     * @return 1 for first week, 2 for second, etc
     */
    public static int getDayOfWeekInMonth() {
        return getDayOfWeekInMonth(getDateNow());
    }

    /**
     * Assumes week starts on the first of the month. May not follow firstDayOfWeek
     *
     * @param dateItem Date Item
     * @return 1 for first week, 2 for second, etc
     */
    public static int getDayOfWeekInMonth(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * @param dateItem Date Item
     * @return True if date is in the last full week of the month. Does not follow weeks based on firstDayOfWeek
     */
    public static boolean isLastDayOfFullWeekInMonth(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        //Get the last day of the month containing dateItem
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        DateItem lastDayOfMonth = getDate(calendar);
        //Subtract a week
        calendar.add(Calendar.WEEK_OF_YEAR, -1);
        DateItem weekBeforeLastDay = getDate(calendar);
        //Check if dateItem is within that range ( dateItem ]
        return dateItem.compareTo(lastDayOfMonth) <= 0 && dateItem.compareTo(weekBeforeLastDay) > 0;
    }

    /**
     * @param dateItem Date Item
     * @return True if date is in the last week of month as defined by day of week in month
     */
    public static boolean isLastDayOfWeekInMonth(@NonNull final DateItem dateItem) {
        return getDayOfWeekInMonth(dateItem) == getLastDayOfWeekInMonth(dateItem);
    }

    /**
     * @param dateItem Date Item
     * @return The last week of the month based on day of week in month
     */
    public static int getLastDayOfWeekInMonth(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, 1);
        do {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        } while (calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH) == 1);
        return calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
    }

    /**
     * @return The current day of the week
     */
    public static int getDayOfWeek() {
        return getDayOfWeek(getDateNow());
    }

    /**
     * This does NOT change based on the first day of the week.
     *
     * @param dateItem Date Item
     * @return Day of the week
     */
    public static int getDayOfWeek(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    /**
     * This adjusts based off of the first day of the week
     *
     * @param dateItem Date Item
     * @return Day of the week (1-7)
     */
    public static int getDayOfWeekAdjusted(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        DayOfWeek day = getDayFromCalendar(calendar.get(Calendar.DAY_OF_WEEK));
        DayOfWeek[] week = getWeekArray();
        for (int i = 0; i < week.length; i++) {
            if (week[i] == day) {
                return i + 1;
            }
        }
        return 1;
    }

    /**
     * @return The current day of month
     */
    public static int getDayOfMonth() {
        return getDayOfMonth(getDateNow());
    }

    /**
     * @param dateItem Date Item
     * @return Day of month
     */
    public static int getDayOfMonth(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @return Current day of year
     */
    public static int getDayOfYear() {
        return getDayOfYear(getDateNow());
    }

    /**
     * @param dateItem Date Item
     * @return Day of year
     */
    public static int getDayOfYear(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * @return Current week of month
     */
    public static int getWeekOfMonth() {
        return getWeekOfMonth(getDateNow());
    }

    /**
     * @param dateItem Date Item
     * @return Week of month
     */
    public static int getWeekOfMonth(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * @return Current week of year
     */
    public static int getWeekOfYear() {
        return getWeekOfYear(getDateNow());
    }

    /**
     * @param dateItem Date Item
     * @return Week of year
     */
    public static int getWeekOfYear(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * @return Current month of year
     */
    public static int getMonthOfYear() {
        return getMonthOfYear(getDateNow());
    }

    /**
     * @param dateItem Date Item
     * @return Month of year
     */
    public static int getMonthOfYear(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * @param dateItem Date Item
     * @return True if last week of month
     */
    public static boolean isLastWeekOfMonth(@NonNull final DateItem dateItem) {
        return getWeekOfMonth(dateItem) == getLastWeekOfMonth(dateItem);
    }

    /**
     * @param dateItem Date Item
     * @return The last week of the month
     */
    public static int getLastWeekOfMonth(@NonNull final DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, 1);
        do {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        } while (calendar.get(Calendar.WEEK_OF_MONTH) == 1);
        return calendar.get(Calendar.WEEK_OF_MONTH);
    }

    /**
     * @param items List to copy
     * @return Deep copied list
     */
    @NonNull
    public static ArrayList<TimeItem> cloneArrayListTime(@NonNull final ArrayList<TimeItem> items) {
        ArrayList<TimeItem> newItems = new ArrayList<>();
        for (TimeItem item : items) {
            newItems.add(item.copy());
        }
        return newItems;
    }

    /**
     * @param dates List to copy
     * @return Deep copied list
     */
    @NonNull
    public static ArrayList<DateItem> cloneArrayListDate(@NonNull final ArrayList<DateItem> dates) {
        ArrayList<DateItem> newItems = new ArrayList<>();
        for (DateItem item : dates) {
            newItems.add(item.copy());
        }
        return newItems;
    }

    /**
     * Number of items should not exceed range/5
     *
     * @param range       range in minutes
     * @param wiggle      0 - 1.0
     * @param numberItems number of items to generate
     * @return Array of times
     */
    @NonNull
    public static int[] getRandomTimes(final int range, final float wiggle, final int numberItems) {
        int itemSplit = Math.round((range * 1.0f) / numberItems);
        int[] values = new int[numberItems];
        for (int i = 0; i < values.length; i++) {
            values[i] = Math.round((i * 1.0f) / (numberItems) * range) + itemSplit / 2 + Math.round((itemSplit * wiggle) * (Utils.rand.nextFloat() - 0.5f));
            if (i > 0) {
                values[i] = Math.min(Math.max(values[i], values[i - 1] + 2), range);
            }
        }
        return values;
    }

    /**
     * @param time      int time
     * @param startTime Time Item
     * @param endTime   Time Item
     * @return Time Item
     */
    @NonNull
    public static TimeItem getTimeInDay(final int time, @NonNull final TimeItem startTime, @NonNull final TimeItem endTime) {
        int compare = startTime.compareTo(endTime);
        if (compare == 0) {
            return new TimeItem(0, 0);
        }
        //range is 0 to 1439 (23:59)
        int startMinutes = startTime.toMinutes();
        int endMinutes = endTime.toMinutes();
        if (compare > 0) {
            //Start time is after end time. We run centered around midnight. non standard
            if (time <= endMinutes) {
                return new TimeItem(time);
            } else {
                return new TimeItem(time + startMinutes - endMinutes);
            }
        } else if (compare < 0) {
            //Start time is before end time. This is normal difference work here
            return new TimeItem(time + startMinutes);
        }
        return new TimeItem(0, 0);
    }

    /**
     * @param firstDateTime  Date Time Item
     * @param secondDateTime Date Time Item
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem getOldestDateTime(@NonNull final DateTimeItem firstDateTime, @NonNull final DateTimeItem secondDateTime) {
        int compare = firstDateTime.compareTo(secondDateTime);
        if (compare <= 0) {
            return firstDateTime;
        }
        return secondDateTime;
    }

    /**
     * @param firstDateTime  Date Time Item
     * @param secondDateTime Date Time Item
     * @return Date Time Item
     */
    @NonNull
    public static DateTimeItem getNewestDateTime(@NonNull final DateTimeItem firstDateTime, @NonNull final DateTimeItem secondDateTime) {
        int compare = firstDateTime.compareTo(secondDateTime);
        if (compare >= 0) {
            return firstDateTime;
        }
        return secondDateTime;
    }

    /**
     * @param firstDate  Date Item
     * @param secondDate Date Item
     * @return Date Item
     */
    @NonNull
    public static DateItem getOldestDate(@NonNull final DateItem firstDate, @NonNull final DateItem secondDate) {
        int compare = firstDate.compareTo(secondDate);
        if (compare <= 0) {
            return firstDate;
        }
        return secondDate;
    }

    /**
     * @param firstDate  Date Item
     * @param secondDate Date Item
     * @return Date Item
     */
    @NonNull
    public static DateItem getNewestDate(@NonNull final DateItem firstDate, @NonNull final DateItem secondDate) {
        int compare = firstDate.compareTo(secondDate);
        if (compare >= 0) {
            return firstDate;
        }
        return secondDate;
    }

    /**
     * @param firstTime  Time Item
     * @param secondTime Time Item
     * @return Time Item
     */
    @NonNull
    public static TimeItem getOldestTime(@NonNull final TimeItem firstTime, @NonNull final TimeItem secondTime) {
        int compare = firstTime.compareTo(secondTime);
        if (compare <= 0) {
            return firstTime;
        }
        return secondTime;
    }

    /**
     * @param firstTime  Time Item
     * @param secondTime Time Item
     * @return Time Item
     */
    @NonNull
    public static TimeItem getNewestTIme(@NonNull final TimeItem firstTime, @NonNull final TimeItem secondTime) {
        int compare = firstTime.compareTo(secondTime);
        if (compare <= 0) {
            return firstTime;
        }
        return secondTime;
    }

    /**
     * @param startTime Time Item
     * @param endTime   Time Item
     * @return number of minutes in range. Inclusive
     */
    public static int getMinutesInRange(@NonNull final TimeItem startTime, @NonNull final TimeItem endTime) {
        int compare = startTime.compareTo(endTime);
        if (compare == 0) {
            return 0;
        }
        //range is 0 to 1439 (23:59)
        int startMinutes = startTime.toMinutes();
        int endMinutes = endTime.toMinutes();
        if (compare > 0) {
            //Start time is after end time. We run centered around midnight. non standard
            return endMinutes + 1440 - startMinutes;
        } else if (compare < 0) {
            //Start time is before end time. This is normal difference work here
            return endMinutes - startMinutes;
        }
        return 0;
    }

    /**
     * Months of the year
     */
    public enum MonthOfYear {
        JANUARY,
        FEBRUARY,
        MARCH,
        APRIL,
        MAY,
        JUNE,
        JULY,
        AUGUST,
        SEPTEMBER,
        OCTOBER,
        NOVEMBER,
        DECEMBER;

        /**
         * Auto Translated
         *
         * @return Full month name
         */
        @NonNull
        public final String getName() {
            switch (this) {
                case JANUARY:
                    return getCalendar(new DateItem(2000, 0, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case FEBRUARY:
                    return getCalendar(new DateItem(2000, 1, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case MARCH:
                    return getCalendar(new DateItem(2000, 2, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case APRIL:
                    return getCalendar(new DateItem(2000, 3, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case MAY:
                    return getCalendar(new DateItem(2000, 4, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case JUNE:
                    return getCalendar(new DateItem(2000, 5, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case JULY:
                    return getCalendar(new DateItem(2000, 6, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case AUGUST:
                    return getCalendar(new DateItem(2000, 7, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case SEPTEMBER:
                    return getCalendar(new DateItem(2000, 8, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case OCTOBER:
                    return getCalendar(new DateItem(2000, 9, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case NOVEMBER:
                    return getCalendar(new DateItem(2000, 10, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                case DECEMBER:
                    return getCalendar(new DateItem(2000, 11, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
                default:
                    return getCalendar(new DateItem(2000, 0, 1)).getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault());
            }
        }

        /**
         * Auto Translated
         *
         * @return Abbreviated month name
         */
        @NonNull
        public final String getNameShort() {
            switch (this) {
                case JANUARY:
                    return getCalendar(new DateItem(2000, 0, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case FEBRUARY:
                    return getCalendar(new DateItem(2000, 1, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case MARCH:
                    return getCalendar(new DateItem(2000, 2, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case APRIL:
                    return getCalendar(new DateItem(2000, 3, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case MAY:
                    return getCalendar(new DateItem(2000, 4, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case JUNE:
                    return getCalendar(new DateItem(2000, 5, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case JULY:
                    return getCalendar(new DateItem(2000, 6, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case AUGUST:
                    return getCalendar(new DateItem(2000, 7, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case SEPTEMBER:
                    return getCalendar(new DateItem(2000, 8, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case OCTOBER:
                    return getCalendar(new DateItem(2000, 9, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case NOVEMBER:
                    return getCalendar(new DateItem(2000, 10, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                case DECEMBER:
                    return getCalendar(new DateItem(2000, 11, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
                default:
                    return getCalendar(new DateItem(2000, 0, 1)).getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault());
            }
        }
    }

    /**
     * Day of the week.
     */
    public enum DayOfWeek {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY,
        AUTOMATIC;

        /**
         * Auto Translated
         *
         * @return Full name of the day of the week
         */
        @NonNull
        public final String getName() {
            switch (this) {
                case SUNDAY:
                    return getCalendar(new DateItem(2015, 1, 1)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                case MONDAY:
                    return getCalendar(new DateItem(2015, 1, 2)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                case TUESDAY:
                    return getCalendar(new DateItem(2015, 1, 3)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                case WEDNESDAY:
                    return getCalendar(new DateItem(2015, 1, 4)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                case THURSDAY:
                    return getCalendar(new DateItem(2015, 1, 5)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                case FRIDAY:
                    return getCalendar(new DateItem(2015, 1, 6)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                case SATURDAY:
                    return getCalendar(new DateItem(2015, 1, 7)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
                case AUTOMATIC:
                    return AppBase.getContext().getResources().getString(R.string.automatic);
                default:
                    return AppBase.getContext().getResources().getString(R.string.automatic);
            }
        }

        /**
         * Auto Translated
         *
         * @return Abbreviated name of the day of the week
         */
        @NonNull
        public final String getNameShort() {
            switch (this) {
                case SUNDAY:
                    return getCalendar(new DateItem(2015, 1, 1)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
                case MONDAY:
                    return getCalendar(new DateItem(2015, 1, 2)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
                case TUESDAY:
                    return getCalendar(new DateItem(2015, 1, 3)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
                case WEDNESDAY:
                    return getCalendar(new DateItem(2015, 1, 4)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
                case THURSDAY:
                    return getCalendar(new DateItem(2015, 1, 5)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
                case FRIDAY:
                    return getCalendar(new DateItem(2015, 1, 6)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
                case SATURDAY:
                    return getCalendar(new DateItem(2015, 1, 7)).getDisplayName(Calendar.DAY_OF_WEEK, Calendar.SHORT, Locale.getDefault());
                case AUTOMATIC:
                    return AppBase.getContext().getResources().getString(R.string.automatic);
                default:
                    return AppBase.getContext().getResources().getString(R.string.automatic);
            }
        }
    }

}

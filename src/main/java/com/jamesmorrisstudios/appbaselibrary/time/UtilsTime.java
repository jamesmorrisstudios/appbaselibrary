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
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jamesmorrisstudios.appbaselibrary.R;
import com.jamesmorrisstudios.appbaselibrary.app.AppBase;
import com.jamesmorrisstudios.appbaselibrary.preferences.Prefs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

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
    public static boolean isAM(int hour) {
        return hour < 12;
    }

    /**
     * Converts the 24 hour calendar form to the user preferred style
     *
     * @param hour Hour in 24 hour (calendar form)
     * @return Hour in the user preferred style
     */
    public static int getHourInTimeFormat(int hour) {
        if (is24HourTime() || hour <= 12 && hour >= 1) {
            return hour;
        } else if (hour >= 13) {
            return hour - 12;
        } else {
            return 12;
        }
    }

    /**
     * Get the current time minus one minute
     *
     * @param startTime Starting time
     * @return The time minus one minute
     */
    public static TimeItem getTimeMinusOne(@NonNull TimeItem startTime) {
        if (startTime.minute == 0) {
            return new TimeItem(startTime.hour - 1, 59);
        }
        return new TimeItem(startTime.hour, startTime.minute - 1);
    }

    /**
     * Sets the time in the given text views.
     * Very specialized helper function
     *
     * @param hour   Hour textView
     * @param minute Minute textView
     * @param am     AM text view
     * @param pm     PM textView
     * @param item   Time item
     */
    public static void setTime(@NonNull TextView hour, @NonNull TextView minute, @NonNull TextView am, @NonNull TextView pm, @NonNull TimeItem item) {
        hour.setText(item.getHourInTimeFormatString());
        minute.setText(item.getMinuteString());
        if (item.is24Hour()) {
            am.setVisibility(View.GONE);
            pm.setVisibility(View.GONE);
        } else if (item.isAM()) {
            am.setVisibility(View.VISIBLE);
            pm.setVisibility(View.INVISIBLE);
        } else {
            am.setVisibility(View.INVISIBLE);
            pm.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @return
     */
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

    public static Calendar getCalendar(DateTimeItem dateTime) {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.YEAR, dateTime.dateItem.year);
        calendar.set(Calendar.MONTH, dateTime.dateItem.month);
        calendar.set(Calendar.DAY_OF_MONTH, dateTime.dateItem.dayOfMonth);
        calendar.set(Calendar.HOUR_OF_DAY, dateTime.timeItem.hour);
        calendar.set(Calendar.MINUTE, dateTime.timeItem.minute);
        return calendar;
    }

    public static Calendar getCalendar(DateItem date) {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.YEAR, date.year);
        calendar.set(Calendar.MONTH, date.month);
        calendar.set(Calendar.DAY_OF_MONTH, date.dayOfMonth);
        return calendar;
    }

    public static Calendar getCalendar(TimeItem time) {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.HOUR_OF_DAY, time.hour);
        calendar.set(Calendar.MINUTE, time.minute);
        return calendar;
    }

    public static long getTimeMillis(DateItem date) {
        Calendar calendar = getCalendar(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        return calendar.getTimeInMillis();
    }

    public static long getTimeMillis(DateTimeItem dateTime) {
        return getCalendar(dateTime).getTimeInMillis();
    }

    /**
     * Gets the current time as a time item
     *
     * @return Current time
     */
    @NonNull
    public static TimeItem getTimeNow() {
        Calendar calendar = getCalendar();
        return new TimeItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    @NonNull
    public static TimeItem getTime(Calendar calendar) {
        return new TimeItem(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    /**
     * Gets the current date as a date item
     *
     * @return Current date
     */
    @NonNull
    public static DateItem getDateNow() {
        Calendar calendar = getCalendar();
        return new DateItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    @NonNull
    public static DateItem getDate(Calendar calendar) {
        return new DateItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    /**
     * Gets the current time as a time item
     *
     * @return Current time
     */
    @NonNull
    public static DateTimeItem getDateTimeNow() {
        return new DateTimeItem(getDateNow(), getTimeNow());
    }

    /**
     * Gets the given date time as a time item
     *
     * @return given date time
     */
    @NonNull
    public static DateTimeItem getDateTime(Calendar calendar) {
        return new DateTimeItem(getDate(calendar), getTime(calendar));
    }

    @NonNull
    public static DayOfWeek getCurrentDayOfWeek() {
        Calendar calendar = getCalendar();
        return getDayFromCalendar(calendar.get(Calendar.DAY_OF_WEEK));
    }

    /**
     * @return
     */
    @NonNull
    public static DayOfWeek getFirstDayOfWeek() {
        Calendar calendar = getCalendar();
        return getDayFromCalendar(calendar.getFirstDayOfWeek());
    }

    public static void setFirstDayOfWeek(DayOfWeek firstDayOfWeek) {
        setFirstDayOfWeekOrdinal(firstDayOfWeek.ordinal());
    }

    private static int getFirstDayOfWeekOrdinal() {
        return Prefs.getInt("com.jamesmorrisstudios.utilitieslibrary.UtilsTime", "dayOfWeekIndex", DayOfWeek.AUTOMATIC.ordinal());

    }

    private static void setFirstDayOfWeekOrdinal(int ordinal) {
        Prefs.putInt("com.jamesmorrisstudios.utilitieslibrary.UtilsTime", "dayOfWeekIndex", ordinal);

    }

    @NonNull
    private static DayOfWeek getDayFromCalendar(int calendarDay) {
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

    private static int getCalendarFromDay(DayOfWeek day) {
        switch (day) {
            case SUNDAY:
                return Calendar.SUNDAY;
            case MONDAY:
                return Calendar.MONDAY;
            case TUESDAY:
                return Calendar.TUESDAY;
            case WEDNESDAY:
                return Calendar.WEDNESDAY;
            case THURSDAY:
                return Calendar.THURSDAY;
            case FRIDAY:
                return Calendar.FRIDAY;
            case SATURDAY:
                return Calendar.SATURDAY;
            default:
                return Calendar.SUNDAY;
        }
    }

    @NonNull
    public static DayOfWeek incrementDayOfWeek(@NonNull DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case SUNDAY:
                return DayOfWeek.MONDAY;
            case MONDAY:
                return DayOfWeek.TUESDAY;
            case TUESDAY:
                return DayOfWeek.WEDNESDAY;
            case WEDNESDAY:
                return DayOfWeek.THURSDAY;
            case THURSDAY:
                return DayOfWeek.FRIDAY;
            case FRIDAY:
                return DayOfWeek.SATURDAY;
            case SATURDAY:
                return DayOfWeek.SUNDAY;
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
    public static String[] getWeekStringArray() {
        DayOfWeek[] week = getWeekArray();
        String[] weekString = new String[7];
        for (int i = 0; i < week.length; i++) {
            weekString[i] = week[i].getName();
        }
        return weekString;
    }

    /**
     * @return An array of each day of the week matching the users locale of first day strings of the first letter only
     */
    @NonNull
    public static String[] getWeekStringFirstLetterArray() {
        String[] week = getWeekStringArray();
        String[] weekLetter = new String[week.length];
        for (int i = 0; i < week.length; i++) {
            weekLetter[i] = week[i].substring(0, 1);
        }
        return weekLetter;
    }

    /**
     * Returns the current date formatted to the users locale
     *
     * @param date Date to format
     * @return Formatted date
     */
    @NonNull
    public static String getLongDateFormatted(@NonNull DateItem date) {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.YEAR, date.year);
        calendar.set(Calendar.MONTH, date.month);
        calendar.set(Calendar.DAY_OF_MONTH, date.dayOfMonth);
        return DateFormat.getLongDateFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * Returns the current date formatted to the users locale
     *
     * @param dateTime DateTime to format
     * @return Formatted date
     */
    @NonNull
    public static String getLongDateTimeFormatted(@NonNull DateTimeItem dateTime) {
        Calendar calendar = getCalendar(dateTime);
        return DateFormat.getLongDateFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * Returns the current date formatted to the users locale
     *
     * @param date Date to format
     * @return Formatted date
     */
    @NonNull
    public static String getDateFormatted(@NonNull DateItem date) {
        Calendar calendar = getCalendar();
        calendar.set(Calendar.YEAR, date.year);
        calendar.set(Calendar.MONTH, date.month);
        calendar.set(Calendar.DAY_OF_MONTH, date.dayOfMonth);
        return DateFormat.getDateFormat(AppBase.getContext()).format(calendar.getTime());
    }

    /**
     * Returns the current date formatted to the users locale
     *
     * @param dateTime DateTime to format
     * @return Formatted date
     */
    @NonNull
    public static String getDateTimeFormatted(@NonNull DateTimeItem dateTime) {
        Calendar calendar = getCalendar(dateTime);
        return DateFormat.getDateFormat(AppBase.getContext()).format(calendar.getTime());
    }

    @NonNull
    public static String getShortDateTimeFormatted(@NonNull DateTimeItem dateTime) {
        Calendar calendar = getCalendar(dateTime);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss", Locale.US);
        return sdf.format(calendar.getTime());
    }

    @NonNull
    public static String getShortDateFormatted(@NonNull DateItem date) {
        Calendar calendar = getCalendar(date);
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
        return sdf.format(calendar.getTime());
    }

    public static DateTimeItem getDateTimePlusMinutes(@NonNull DateTimeItem dateTime, int minutes) {
        Calendar calendar = getCalendar(dateTime);
        calendar.add(Calendar.MINUTE, minutes);
        return getDateTime(calendar);
    }



    public static WeekOfMonth getWeekOfMonth() {
        return getWeekOfMonth(getDateNow());
    }

    public static WeekOfMonth getWeekOfMonth(DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        return WeekOfMonth.getWeekOfMonth(calendar.get(Calendar.WEEK_OF_MONTH));
    }

    public static boolean isLastWeekOfMonth(DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        Log.v("UtilsTime","Last Week of month "+getLastWeekOfMonth(dateItem).getName());
        return WeekOfMonth.getWeekOfMonth(calendar.get(Calendar.WEEK_OF_MONTH)) == getLastWeekOfMonth(dateItem);
    }

    public static WeekOfMonth getLastWeekOfMonth(DateItem dateItem) {
        Calendar calendar = getCalendar(dateItem);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.add(Calendar.MONTH, 1);
        do {
            calendar.add(Calendar.DAY_OF_YEAR, -1);
        } while(WeekOfMonth.getWeekOfMonth(calendar.get(Calendar.WEEK_OF_MONTH)) == WeekOfMonth.FIRST);
        Log.v("UtilsTime", "Day of Month "+calendar.get(Calendar.DAY_OF_MONTH));
        return WeekOfMonth.getWeekOfMonth(calendar.get(Calendar.WEEK_OF_MONTH));
    }

    /**
     * Check that the given time is within the given bounds inclusive of end value only
     *
     * @param start Start time
     * @param end   End time
     * @param value Time to check against
     * @return True if within
     */
    public static boolean timeInBoundsInclusiveEnd(@NonNull TimeItem start, @NonNull TimeItem end, @NonNull TimeItem value) {
        return timeBefore(start, value) && timeBeforeOrEqual(value, end);
    }

    /**
     * Check that the current time is within the given bounds inclusive of both end points
     *
     * @param start Start time
     * @param end   End time
     * @return True if within
     */
    public static boolean timeInBoundsInclusive(@NonNull TimeItem start, @NonNull TimeItem end) {
        TimeItem timeNow = UtilsTime.getTimeNow();
        return timeBeforeOrEqual(start, timeNow) && timeBeforeOrEqual(timeNow, end);
    }

    /**
     * @param newTime New time
     * @param oldTime Old time
     * @return True if new time is before or equal to old time
     */
    public static boolean timeBeforeOrEqual(@NonNull TimeItem newTime, @NonNull TimeItem oldTime) {
        return (newTime.hour * 60 + newTime.minute) - (oldTime.hour * 60 + oldTime.minute) <= 0;
    }

    /**
     * @param newTime New time
     * @param oldTime Old time
     * @return True if new time is before old time
     */
    public static boolean timeBefore(@NonNull TimeItem newTime, @NonNull TimeItem oldTime) {
        return (newTime.hour * 60 + newTime.minute) - (oldTime.hour * 60 + oldTime.minute) < 0;
    }

    @NonNull
    public static ArrayList<TimeItem> cloneArrayListTime(ArrayList<TimeItem> items) {
        ArrayList<TimeItem> newItems = new ArrayList<>();
        for(TimeItem item : items) {
            newItems.add(item.copy());
        }
        return newItems;
    }

    /**
     * Day of the week. Use .name for the String name
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

        public int getIndex() {
            int index = (ordinal() - getFirstDayOfWeek().ordinal()) % 7;
            if (index < 0) {
                index += 7;
            }
            return index;
        }

        public String getName() {
            switch(this) {
                case SUNDAY:
                    return AppBase.getContext().getResources().getString(R.string.day_sunday);
                case MONDAY:
                    return AppBase.getContext().getResources().getString(R.string.day_monday);
                case TUESDAY:
                    return AppBase.getContext().getResources().getString(R.string.day_tuesday);
                case WEDNESDAY:
                    return AppBase.getContext().getResources().getString(R.string.day_wednesday);
                case THURSDAY:
                    return AppBase.getContext().getResources().getString(R.string.day_thursday);
                case FRIDAY:
                    return AppBase.getContext().getResources().getString(R.string.day_friday);
                case SATURDAY:
                    return AppBase.getContext().getResources().getString(R.string.day_saturday);
                case AUTOMATIC:
                    return AppBase.getContext().getResources().getString(R.string.automatic);
                default:
                    return AppBase.getContext().getResources().getString(R.string.automatic);
            }
        }
    }

    public enum WeekOfMonth {
        FIRST(1),
        SECOND(2),
        THIRD(3),
        FOURTH(4),
        FIFTH(5);

        public final int number;

        WeekOfMonth(int number) {
            this.number = number;
        }

        public static WeekOfMonth getWeekOfMonth(int number) {
            switch(number) {
                case 1:
                    return WeekOfMonth.FIRST;
                case 2:
                    return WeekOfMonth.SECOND;
                case 3:
                    return WeekOfMonth.THIRD;
                case 4:
                    return WeekOfMonth.FOURTH;
                case 5:
                    return WeekOfMonth.FIFTH;
            }
            return WeekOfMonth.FIRST;
        }

        public String getName() {
            switch(this) {
                case FIRST:
                    return AppBase.getContext().getResources().getString(R.string.first);
                case SECOND:
                    return AppBase.getContext().getResources().getString(R.string.second);
                case THIRD:
                    return AppBase.getContext().getResources().getString(R.string.third);
                case FOURTH:
                    return AppBase.getContext().getResources().getString(R.string.fourth);
                case FIFTH:
                    return AppBase.getContext().getResources().getString(R.string.fifth);
                default:
                    return AppBase.getContext().getResources().getString(R.string.first);
            }
        }

    }

}

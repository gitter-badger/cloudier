package net.kyouko.cloudier.util;

import android.content.Context;

import net.kyouko.cloudier.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Util class for generating string descriptions of dates.
 *
 * @author beta
 */
public class TimeUtil {

    public static String getDescription(Context context, Calendar calendar) {
        Calendar now = new GregorianCalendar();

        SimpleDateFormat timeFormat = new SimpleDateFormat(context.getString(R.string.text_date_time_pattern));
        SimpleDateFormat dateFormat = new SimpleDateFormat(context.getString(R.string.text_date_date_time_pattern));
        SimpleDateFormat dateFormatWithYear = new SimpleDateFormat(context.getString(R.string.text_date_date_time_pattern_with_year));

        if (calendar.get(Calendar.YEAR) == now.get(Calendar.YEAR)) {
            if (calendar.get(Calendar.DAY_OF_MONTH) == now.get(Calendar.DAY_OF_MONTH)) {
                int deltaSeconds = (now.get(Calendar.HOUR) * 3600 + now.get(Calendar.MINUTE) * 60 + now.get(Calendar.SECOND))
                        - (calendar.get(Calendar.HOUR) * 3600 + calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND));
                if (deltaSeconds == 0) {
                    return context.getString(R.string.text_date_just_now);
                } else if (deltaSeconds < 0) {
                    return timeFormat.format(calendar.getTime());
                }
                if (deltaSeconds == 1) {
                    return context.getString(R.string.text_date_1_second_ago);
                } else if (deltaSeconds < 60) {
                    return context.getString(R.string.text_date_seconds_ago, deltaSeconds);
                } else {
                    int deltaMinutes = (now.get(Calendar.HOUR) * 60 + now.get(Calendar.MINUTE))
                            - (calendar.get(Calendar.HOUR) * 60 + calendar.get(Calendar.MINUTE));
                    if (deltaMinutes == 1) {
                        return context.getString(R.string.text_date_1_minute_ago);
                    } else if (deltaMinutes < 60) {
                        return context.getString(R.string.text_date_minutes_ago, deltaMinutes);
                    } else {
                        return context.getString(R.string.text_date_today, timeFormat.format(calendar.getTime()));
                    }
                }
            } else if (calendar.get(Calendar.DAY_OF_MONTH) + 1 == now.get(Calendar.DAY_OF_MONTH)) {
                return context.getString(R.string.text_date_yesterday, timeFormat.format(calendar.getTime()));
            } else {
                return dateFormat.format(calendar.getTime());
            }
        } else {
            return dateFormatWithYear.format(calendar.getTime());
        }
    }


    public static Calendar convertTimestampToCalendar(long timestamp) {
        long timestampInMillis = timestamp * 1000;
        Date date = new Date(timestampInMillis);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

}

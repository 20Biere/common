package be.itlive.common.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.commons.lang3.time.FastDateFormat;

/**
 * Some useful and basics date functions.
 * Don't add any JodaTime or JDK8 DateTime API in here.
 *
 * @author vbiertho
 */
public final class DateUtil {

    /**
     * Time Zone.
     */
    public static final String TIME_ZONE = "Europe/Brussels";

    /**
     * Alternate(Old) Date Format. Mainly used in backend, console logging and hris.
     */
    public static final String ALTERNATE_DATE_FORMAT = "dd-MM-yyyy";

    /**
     * Old(Alternate) General Date Pattern.
     */
    public static final String ALTERNATE_DATE_FORMAT_REGEXP = "\\d{1,2}-\\d{1,2}-\\d{4}";

    /**
     * Default(New) date format in HR apps.
     */
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    /**
     * New(Default) General Date Pattern.
     */
    public static final String DEFAULT_DATE_FORMAT_REGEXP = "\\d{1,2}/\\d{1,2}/\\d{4}";

    private static final FastDateFormat FORMAT = FastDateFormat.getInstance(ALTERNATE_DATE_FORMAT, TimeZone.getTimeZone(TIME_ZONE), new Locale("nl"));

    private static final FastDateFormat FORMAT_TIMED = FastDateFormat.getInstance(ALTERNATE_DATE_FORMAT + " HH:mm:ss",
            TimeZone.getTimeZone(TIME_ZONE), new Locale("nl"));

    private DateUtil() {
    }

    /**
     * Converts a string to a date.
     * @param date the date string to convert
     * @return a Date object
     * @throws ParseException if the string has an invalid format
     */
    public static Date getDateFromString(final String date) throws ParseException {
        FastDateFormat formatter = FastDateFormat.getInstance(getPattern(date), TimeZone.getTimeZone(TIME_ZONE), new Locale("nl"));
        return formatter.parse(date);
    }

    /**
     * Get the Date pattern of given String, or default.
     * @param dateString the string date.
     * @return a date pattern for the given string date.
     */
    private static String getPattern(final String dateString) {
        String pattern = DEFAULT_DATE_FORMAT;
        if (dateString != null && dateString.matches("[0-3]?\\d-[0-1]?\\d-\\d{4}")) {
            pattern = ALTERNATE_DATE_FORMAT;
        }
        return pattern;
    }

    /**
     * Convert a date to its string representation.
     * @param date the date to convert
     * @return a String
     */
    public static String getStringFromDate(final Date date) {
        if (date == null) {
            return "NULL";
        }
        return FORMAT.format(date);
    }

    /**
     * Convert a date to its string representation.
     * @param date the date to convert
     * @return a String
     */
    public static String getStringFromDateTimed(final Date date) {
        if (date == null) {
            return "NULL";
        }
        return FORMAT_TIMED.format(date);
    }

    /**
     * @param date the date.
     * @param otherDate the other date.
     * @return true if date is after or on the same day as the other date, false otherwise.
     */
    public static boolean isAfterOrEquals(final Date date, final Date otherDate) {
        return (date != null && otherDate != null) && (date.after(otherDate) || date.getTime() == otherDate.getTime());
    }

    /**
     * @param date the date.
     * @param otherDate the other date.
     * @return true if date is before or on the same day as the other date, false otherwise.
     */
    public static boolean isBeforeOrEquals(final Date date, final Date otherDate) {
        return (date != null) && (otherDate == null || (date.before(otherDate) || date.getTime() == otherDate.getTime()));
    }

    /**
     * Ignoring time differences, check if giventDate is in the future only based on day.
     * @param date Date to check.
     * @return true if given date is in the future (day base).
     */
    public static boolean isFutureDay(final Date date) {
        if (date == null) {
            return false;
        }
        Date truncated = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
        return truncated.after(today());
    }

    /**
     * Checks whether 2 dates represents the same day.
     * @param date1 the first date
     * @param date2 the second date
     * @return true if day(date1)=day(date2)
     */
    public static boolean isSameDay(final Date date1, final Date date2) {
        Date truncDate1 = date1 == null ? null : DateUtils.truncate(date1, Calendar.DAY_OF_MONTH);
        Date truncDate2 = date2 == null ? null : DateUtils.truncate(date2, Calendar.DAY_OF_MONTH);

        return truncDate1.getTime() == truncDate2.getTime();
    }

    /**
     * @param date the string date to parse.
     * @return the date for the given string date.
     * @throws ParseException when that String can't be parsed.
     */
    public static Date parse(final String date) throws ParseException {
        if (StringUtils.isEmpty(date)) {
            return null;
        }

        FastDateFormat format = null;

        if (date.matches(DEFAULT_DATE_FORMAT_REGEXP)) {
            format = FastDateFormat.getInstance(DEFAULT_DATE_FORMAT);
        } else if (date.matches(ALTERNATE_DATE_FORMAT_REGEXP)) {
            format = FastDateFormat.getInstance(ALTERNATE_DATE_FORMAT);
        } else {
            return null;
        }

        return format.parse(date);
    }

    /**
     * @return a date at 00:00:00
     */
    public static Date today() {
        return todayCalendar().getTime();
    }

    /**
     * @return a calendar at 00:00:00
     */
    private static Calendar todayCalendar() {
        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);
        return todayCalendar;
    }

    /**
     * @return tomorrow at 00:00:00
     */
    public static Date tomorrow() {
        Calendar tomorrowCalendar = todayCalendar();
        tomorrowCalendar.roll(Calendar.DAY_OF_MONTH, 1);
        return tomorrowCalendar.getTime();
    }

    /**
     * @return yesterday at 00:00:00
     */
    public static Date yesterday() {
        Calendar yesterdayCalendar = todayCalendar();
        yesterdayCalendar.roll(Calendar.DAY_OF_MONTH, -1);
        return yesterdayCalendar.getTime();
    }

    /**
     * Gets the oldest date of all given.
     * @param dates dates among which find the oldest.
     * @return oldest of <code>dates</code>, <tt>null</tt> if <code>dates</code> is <tt>null</tt> or all dates are <tt>null</tt>.
     */
    public static Date min(final Date... dates) {
        Date min = null;
        if (dates != null && dates.length > 0) {
            if (dates.length == 1) {
                min = dates[0];
            } else {
                for (int i = 0; i < dates.length; i++) {
                    if (min == null || dates[i] != null && dates[i].before(min)) {
                        min = dates[i];
                    }
                }
            }
        }
        return min;
    }

    /**
     * Gets the newest calendar of all given.
     * @param calendars calendars among which find the newest.
     * @return newest of <code>calendars</code>, <tt>null</tt> if <code>calendars</code> is <tt>null</tt> or all calendars are <tt>null</tt>.
     */
    public static Calendar max(final Calendar... calendars) {
        Calendar max = null;
        if (calendars != null && calendars.length > 0) {
            if (calendars.length == 1) {
                max = calendars[0];
            } else {
                for (int i = 0; i < calendars.length; i++) {
                    if (max == null || calendars[i] != null && calendars[i].after(max)) {
                        max = calendars[i];
                    }
                }
            }
        }
        return max;
    }

    /**
     * Gets the newest date of all given.
     * @param dates dates among which find the newest.
     * @return newest of <code>dates</code>, <tt>null</tt> if <code>dates</code> is <tt>null</tt> or all dates are <tt>null</tt>.
     */
    public static Date max(final Date... dates) {
        Date max = null;
        if (dates != null && dates.length > 0) {
            if (dates.length == 1) {
                max = dates[0];
            } else {
                for (int i = 0; i < dates.length; i++) {
                    if (max == null || dates[i] != null && dates[i].after(max)) {
                        max = dates[i];
                    }
                }
            }
        }
        return max;
    }
}

package com.google.zxing.client.result;

import com.mapzen.android.lost.internal.FusionEngine;
import dji.pilot.publics.util.DJITimeUtils;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class CalendarParsedResult extends ParsedResult {
    private static final Pattern DATE_TIME = Pattern.compile("[0-9]{8}(T[0-9]{6}Z?)?");
    private static final Pattern RFC2445_DURATION = Pattern.compile("P(?:(\\d+)W)?(?:(\\d+)D)?(?:T(?:(\\d+)H)?(?:(\\d+)M)?(?:(\\d+)S)?)?");
    private static final long[] RFC2445_DURATION_FIELD_UNITS = {604800000, DJITimeUtils.MILLIS_IN_DAY, 3600000, FusionEngine.RECENT_UPDATE_THRESHOLD_IN_MILLIS, 1000};
    private final String[] attendees;
    private final String description;
    private final long end;
    private final boolean endAllDay;
    private final double latitude;
    private final String location;
    private final double longitude;
    private final String organizer;
    private final long start;
    private final boolean startAllDay;
    private final String summary;

    public CalendarParsedResult(String summary2, String startString, String endString, String durationString, String location2, String organizer2, String[] attendees2, String description2, double latitude2, double longitude2) {
        super(ParsedResultType.CALENDAR);
        this.summary = summary2;
        try {
            this.start = parseDate(startString);
            if (endString == null) {
                long durationMS = parseDurationMS(durationString);
                this.end = durationMS < 0 ? -1 : this.start + durationMS;
            } else {
                try {
                    this.end = parseDate(endString);
                } catch (ParseException pe) {
                    throw new IllegalArgumentException(pe.toString());
                }
            }
            this.startAllDay = startString.length() == 8;
            this.endAllDay = endString != null && endString.length() == 8;
            this.location = location2;
            this.organizer = organizer2;
            this.attendees = attendees2;
            this.description = description2;
            this.latitude = latitude2;
            this.longitude = longitude2;
        } catch (ParseException pe2) {
            throw new IllegalArgumentException(pe2.toString());
        }
    }

    public String getSummary() {
        return this.summary;
    }

    @Deprecated
    public Date getStart() {
        return new Date(this.start);
    }

    public long getStartTimestamp() {
        return this.start;
    }

    public boolean isStartAllDay() {
        return this.startAllDay;
    }

    @Deprecated
    public Date getEnd() {
        if (this.end < 0) {
            return null;
        }
        return new Date(this.end);
    }

    public long getEndTimestamp() {
        return this.end;
    }

    public boolean isEndAllDay() {
        return this.endAllDay;
    }

    public String getLocation() {
        return this.location;
    }

    public String getOrganizer() {
        return this.organizer;
    }

    public String[] getAttendees() {
        return this.attendees;
    }

    public String getDescription() {
        return this.description;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public String getDisplayResult() {
        StringBuilder result = new StringBuilder(100);
        maybeAppend(this.summary, result);
        maybeAppend(format(this.startAllDay, this.start), result);
        maybeAppend(format(this.endAllDay, this.end), result);
        maybeAppend(this.location, result);
        maybeAppend(this.organizer, result);
        maybeAppend(this.attendees, result);
        maybeAppend(this.description, result);
        return result.toString();
    }

    private static long parseDate(String when) throws ParseException {
        if (!DATE_TIME.matcher(when).matches()) {
            throw new ParseException(when, 0);
        } else if (when.length() == 8) {
            DateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.ENGLISH);
            format.setTimeZone(TimeZone.getTimeZone("GMT"));
            return format.parse(when).getTime();
        } else if (when.length() != 16 || when.charAt(15) != 'Z') {
            return parseDateTimeString(when);
        } else {
            long milliseconds = parseDateTimeString(when.substring(0, 15));
            Calendar calendar = new GregorianCalendar();
            long milliseconds2 = milliseconds + ((long) calendar.get(15));
            calendar.setTime(new Date(milliseconds2));
            return ((long) calendar.get(16)) + milliseconds2;
        }
    }

    private static String format(boolean allDay, long date) {
        DateFormat dateTimeInstance;
        if (date < 0) {
            return null;
        }
        if (allDay) {
            dateTimeInstance = DateFormat.getDateInstance(2);
        } else {
            dateTimeInstance = DateFormat.getDateTimeInstance(2, 2);
        }
        return dateTimeInstance.format(Long.valueOf(date));
    }

    private static long parseDurationMS(CharSequence durationString) {
        long durationMS = -1;
        if (durationString != null) {
            Matcher m = RFC2445_DURATION.matcher(durationString);
            if (m.matches()) {
                durationMS = 0;
                for (int i = 0; i < RFC2445_DURATION_FIELD_UNITS.length; i++) {
                    String fieldValue = m.group(i + 1);
                    if (fieldValue != null) {
                        durationMS += RFC2445_DURATION_FIELD_UNITS[i] * ((long) Integer.parseInt(fieldValue));
                    }
                }
            }
        }
        return durationMS;
    }

    private static long parseDateTimeString(String dateTimeString) throws ParseException {
        return new SimpleDateFormat("yyyyMMdd'T'HHmmss", Locale.ENGLISH).parse(dateTimeString).getTime();
    }
}

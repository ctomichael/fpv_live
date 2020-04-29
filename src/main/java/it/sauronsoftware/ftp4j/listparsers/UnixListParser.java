package it.sauronsoftware.ftp4j.listparsers;

import dji.pilot.publics.util.DJITimeUtils;
import it.sauronsoftware.ftp4j.FTPFile;
import it.sauronsoftware.ftp4j.FTPListParseException;
import it.sauronsoftware.ftp4j.FTPListParser;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UnixListParser implements FTPListParser {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("MMM dd yyyy HH:mm", Locale.US);
    private static final Pattern PATTERN = Pattern.compile("^([dl\\-])[r\\-][w\\-][xSs\\-][r\\-][w\\-][xSs\\-][r\\-][w\\-][xTt\\-]\\s+(?:\\d+\\s+)?\\S+\\s*\\S+\\s+(\\d+)\\s+(?:(\\w{3})\\s+(\\d{1,2}))\\s+(?:(\\d{4})|(?:(\\d{1,2}):(\\d{1,2})))\\s+([^\\\\*?\"<>|]+)(?: -> ([^\\\\*?\"<>|]+))?$");

    public FTPFile[] parse(String[] lines) throws FTPListParseException {
        boolean checkYear;
        Date md;
        int size = lines.length;
        if (size == 0) {
            return new FTPFile[0];
        }
        if (lines[0].startsWith("total")) {
            size--;
            String[] lines2 = new String[size];
            for (int i = 0; i < size; i++) {
                lines2[i] = lines[i + 1];
            }
            lines = lines2;
        }
        Calendar now = Calendar.getInstance();
        int currentYear = now.get(1);
        FTPFile[] ret = new FTPFile[size];
        int i2 = 0;
        while (i2 < size) {
            Matcher m = PATTERN.matcher(lines[i2]);
            if (m.matches()) {
                ret[i2] = new FTPFile();
                String typeString = m.group(1);
                String sizeString = m.group(2);
                String monthString = m.group(3);
                String dayString = m.group(4);
                String yearString = m.group(5);
                String hourString = m.group(6);
                String minuteString = m.group(7);
                String nameString = m.group(8);
                String linkedString = m.group(9);
                if (typeString.equals("-")) {
                    ret[i2].setType(0);
                } else if (typeString.equals("d")) {
                    ret[i2].setType(1);
                } else if (typeString.equals("l")) {
                    ret[i2].setType(2);
                    ret[i2].setLink(linkedString);
                } else {
                    throw new FTPListParseException();
                }
                try {
                    ret[i2].setSize(Long.parseLong(sizeString));
                    if (dayString.length() == 1) {
                        dayString = "0" + dayString;
                    }
                    StringBuffer mdString = new StringBuffer();
                    mdString.append(monthString);
                    mdString.append(' ');
                    mdString.append(dayString);
                    mdString.append(' ');
                    if (yearString == null) {
                        mdString.append(currentYear);
                        checkYear = true;
                    } else {
                        mdString.append(yearString);
                        checkYear = false;
                    }
                    mdString.append(' ');
                    if (hourString == null || minuteString == null) {
                        mdString.append("00:00");
                    } else {
                        if (hourString.length() == 1) {
                            hourString = "0" + hourString;
                        }
                        if (minuteString.length() == 1) {
                            minuteString = "0" + minuteString;
                        }
                        mdString.append(hourString);
                        mdString.append(':');
                        mdString.append(minuteString);
                    }
                    try {
                        synchronized (DATE_FORMAT) {
                            md = DATE_FORMAT.parse(mdString.toString());
                        }
                        if (checkYear) {
                            Calendar mc = Calendar.getInstance();
                            mc.setTime(md);
                            if (mc.after(now) && mc.getTimeInMillis() - now.getTimeInMillis() > DJITimeUtils.MILLIS_IN_DAY) {
                                mc.set(1, currentYear - 1);
                                md = mc.getTime();
                            }
                        }
                        ret[i2].setModifiedDate(md);
                        ret[i2].setName(nameString);
                        i2++;
                    } catch (ParseException e) {
                        throw new FTPListParseException();
                    }
                } finally {
                    FTPListParseException fTPListParseException = new FTPListParseException();
                }
            } else {
                throw new FTPListParseException();
            }
        }
        return ret;
    }
}

package com.adobe.xmp.impl;

import com.adobe.xmp.XMPDateTime;
import com.adobe.xmp.XMPException;
import com.drew.metadata.exif.makernotes.NikonType2MakernoteDirectory;
import dji.utils.TimeUtils;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.SimpleTimeZone;
import org.bouncycastle.pqc.math.linearalgebra.Matrix;

public final class ISO8601Converter {
    private ISO8601Converter() {
    }

    public static XMPDateTime parse(String str) throws XMPException {
        return parse(str, new XMPDateTimeImpl());
    }

    public static XMPDateTime parse(String str, XMPDateTime xMPDateTime) throws XMPException {
        int i;
        int i2;
        int i3;
        if (str == null) {
            throw new XMPException("Parameter must not be null", 4);
        }
        if (str.length() != 0) {
            ParseState parseState = new ParseState(str);
            if (parseState.ch(0) == '-') {
                parseState.skip();
            }
            int gatherInt = parseState.gatherInt("Invalid year in date string", 9999);
            if (!parseState.hasNext() || parseState.ch() == '-') {
                if (parseState.ch(0) == '-') {
                    gatherInt = -gatherInt;
                }
                xMPDateTime.setYear(gatherInt);
                if (parseState.hasNext()) {
                    parseState.skip();
                    int gatherInt2 = parseState.gatherInt("Invalid month in date string", 12);
                    if (!parseState.hasNext() || parseState.ch() == '-') {
                        xMPDateTime.setMonth(gatherInt2);
                        if (parseState.hasNext()) {
                            parseState.skip();
                            int gatherInt3 = parseState.gatherInt("Invalid day in date string", 31);
                            if (!parseState.hasNext() || parseState.ch() == 'T') {
                                xMPDateTime.setDay(gatherInt3);
                                if (parseState.hasNext()) {
                                    parseState.skip();
                                    xMPDateTime.setHour(parseState.gatherInt("Invalid hour in date string", 23));
                                    if (parseState.hasNext()) {
                                        if (parseState.ch() == ':') {
                                            parseState.skip();
                                            int gatherInt4 = parseState.gatherInt("Invalid minute in date string", 59);
                                            if (!parseState.hasNext() || parseState.ch() == ':' || parseState.ch() == 'Z' || parseState.ch() == '+' || parseState.ch() == '-') {
                                                xMPDateTime.setMinute(gatherInt4);
                                            } else {
                                                throw new XMPException("Invalid date string, after minute", 5);
                                            }
                                        }
                                        if (parseState.hasNext()) {
                                            if (parseState.hasNext() && parseState.ch() == ':') {
                                                parseState.skip();
                                                int gatherInt5 = parseState.gatherInt("Invalid whole seconds in date string", 59);
                                                if (!parseState.hasNext() || parseState.ch() == '.' || parseState.ch() == 'Z' || parseState.ch() == '+' || parseState.ch() == '-') {
                                                    xMPDateTime.setSecond(gatherInt5);
                                                    if (parseState.ch() == '.') {
                                                        parseState.skip();
                                                        int pos = parseState.pos();
                                                        int gatherInt6 = parseState.gatherInt("Invalid fractional seconds in date string", 999999999);
                                                        if (!parseState.hasNext() || parseState.ch() == 'Z' || parseState.ch() == '+' || parseState.ch() == '-') {
                                                            int pos2 = parseState.pos() - pos;
                                                            while (pos2 > 9) {
                                                                gatherInt6 /= 10;
                                                                pos2--;
                                                            }
                                                            while (pos2 < 9) {
                                                                gatherInt6 *= 10;
                                                                pos2++;
                                                            }
                                                            xMPDateTime.setNanoSecond(gatherInt6);
                                                        } else {
                                                            throw new XMPException("Invalid date string, after fractional second", 5);
                                                        }
                                                    }
                                                } else {
                                                    throw new XMPException("Invalid date string, after whole seconds", 5);
                                                }
                                            } else if (!(parseState.ch() == 'Z' || parseState.ch() == '+' || parseState.ch() == '-')) {
                                                throw new XMPException("Invalid date string, after time", 5);
                                            }
                                            if (parseState.hasNext()) {
                                                if (parseState.ch() == 'Z') {
                                                    parseState.skip();
                                                    i = 0;
                                                    i2 = 0;
                                                    i3 = 0;
                                                } else if (parseState.hasNext()) {
                                                    if (parseState.ch() == '+') {
                                                        i3 = 1;
                                                    } else if (parseState.ch() == '-') {
                                                        i3 = -1;
                                                    } else {
                                                        throw new XMPException("Time zone must begin with 'Z', '+', or '-'", 5);
                                                    }
                                                    parseState.skip();
                                                    i2 = parseState.gatherInt("Invalid time zone hour in date string", 23);
                                                    if (!parseState.hasNext()) {
                                                        i = 0;
                                                    } else if (parseState.ch() == ':') {
                                                        parseState.skip();
                                                        i = parseState.gatherInt("Invalid time zone minute in date string", 59);
                                                    } else {
                                                        throw new XMPException("Invalid date string, after time zone hour", 5);
                                                    }
                                                } else {
                                                    i = 0;
                                                    i2 = 0;
                                                    i3 = 0;
                                                }
                                                xMPDateTime.setTimeZone(new SimpleTimeZone(i3 * ((i * 60 * 1000) + (i2 * NikonType2MakernoteDirectory.TAG_NIKON_SCAN * 1000)), ""));
                                                if (parseState.hasNext()) {
                                                    throw new XMPException("Invalid date string, extra chars at end", 5);
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                throw new XMPException("Invalid date string, after day", 5);
                            }
                        }
                    } else {
                        throw new XMPException("Invalid date string, after month", 5);
                    }
                }
            } else {
                throw new XMPException("Invalid date string, after year", 5);
            }
        }
        return xMPDateTime;
    }

    public static String render(XMPDateTime xMPDateTime) {
        StringBuffer stringBuffer = new StringBuffer();
        if (xMPDateTime.hasDate()) {
            DecimalFormat decimalFormat = new DecimalFormat("0000", new DecimalFormatSymbols(Locale.ENGLISH));
            stringBuffer.append(decimalFormat.format((long) xMPDateTime.getYear()));
            if (xMPDateTime.getMonth() == 0) {
                return stringBuffer.toString();
            }
            decimalFormat.applyPattern("'-'00");
            stringBuffer.append(decimalFormat.format((long) xMPDateTime.getMonth()));
            if (xMPDateTime.getDay() == 0) {
                return stringBuffer.toString();
            }
            stringBuffer.append(decimalFormat.format((long) xMPDateTime.getDay()));
            if (xMPDateTime.hasTime()) {
                stringBuffer.append('T');
                decimalFormat.applyPattern("00");
                stringBuffer.append(decimalFormat.format((long) xMPDateTime.getHour()));
                stringBuffer.append(':');
                stringBuffer.append(decimalFormat.format((long) xMPDateTime.getMinute()));
                if (!(xMPDateTime.getSecond() == 0 && xMPDateTime.getNanoSecond() == 0)) {
                    decimalFormat.applyPattern(":00.#########");
                    stringBuffer.append(decimalFormat.format(((double) xMPDateTime.getSecond()) + (((double) xMPDateTime.getNanoSecond()) / 1.0E9d)));
                }
                if (xMPDateTime.hasTimeZone()) {
                    int offset = xMPDateTime.getTimeZone().getOffset(xMPDateTime.getCalendar().getTimeInMillis());
                    if (offset == 0) {
                        stringBuffer.append((char) Matrix.MATRIX_TYPE_ZERO);
                    } else {
                        int i = offset / TimeUtils.TIMECONSTANT_HOUR;
                        int abs = Math.abs((offset % TimeUtils.TIMECONSTANT_HOUR) / 60000);
                        decimalFormat.applyPattern("+00;-00");
                        stringBuffer.append(decimalFormat.format((long) i));
                        decimalFormat.applyPattern(":00");
                        stringBuffer.append(decimalFormat.format((long) abs));
                    }
                }
            }
        }
        return stringBuffer.toString();
    }
}

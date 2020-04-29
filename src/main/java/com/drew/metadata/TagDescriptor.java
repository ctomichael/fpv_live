package com.drew.metadata;

import com.drew.lang.Rational;
import com.drew.lang.StringUtil;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.metadata.Directory;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.math.RoundingMode;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TagDescriptor<T extends Directory> {
    static final /* synthetic */ boolean $assertionsDisabled = (!TagDescriptor.class.desiredAssertionStatus());
    @NotNull
    protected final T _directory;

    public TagDescriptor(@NotNull T directory) {
        this._directory = directory;
    }

    @Nullable
    public String getDescription(int tagType) {
        int length;
        Object object = this._directory.getObject(tagType);
        if (object == null) {
            return null;
        }
        if (object.getClass().isArray() && (length = Array.getLength(object)) > 16) {
            return String.format("[%d values]", Integer.valueOf(length));
        }
        if (object instanceof Date) {
            return new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy").format((Date) object).replaceAll("([0-9]{2} [^ ]+)$", ":$1");
        }
        return this._directory.getString(tagType);
    }

    @Nullable
    public static String convertBytesToVersionString(@Nullable int[] components, int majorDigits) {
        if (components == null) {
            return null;
        }
        StringBuilder version = new StringBuilder();
        int i = 0;
        while (i < 4 && i < components.length) {
            if (i == majorDigits) {
                version.append('.');
            }
            char c = (char) components[i];
            if (c < '0') {
                c = (char) (c + '0');
            }
            if (i != 0 || c != '0') {
                version.append(c);
            }
            i++;
        }
        return version.toString();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getVersionBytesDescription(int tagType, int majorDigits) {
        int[] values = this._directory.getIntArray(tagType);
        if (values == null) {
            return null;
        }
        return convertBytesToVersionString(values, majorDigits);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getIndexedDescription(int tagType, @NotNull String... descriptions) {
        return getIndexedDescription(tagType, 0, descriptions);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getIndexedDescription(int tagType, int baseIndex, @NotNull String... descriptions) {
        String description;
        Integer index = this._directory.getInteger(tagType);
        if (index == null) {
            return null;
        }
        int arrayIndex = index.intValue() - baseIndex;
        return (arrayIndex < 0 || arrayIndex >= descriptions.length || (description = descriptions[arrayIndex]) == null) ? "Unknown (" + index + ")" : description;
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getByteLengthDescription(int tagType) {
        byte[] bytes = this._directory.getByteArray(tagType);
        if (bytes == null) {
            return null;
        }
        Object[] objArr = new Object[2];
        objArr[0] = Integer.valueOf(bytes.length);
        objArr[1] = bytes.length == 1 ? "" : "s";
        return String.format("(%d byte%s)", objArr);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getSimpleRational(int tagType) {
        Rational value = this._directory.getRational(tagType);
        if (value == null) {
            return null;
        }
        return value.toSimpleString(true);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getDecimalRational(int tagType, int decimalPlaces) {
        Rational value = this._directory.getRational(tagType);
        if (value == null) {
            return null;
        }
        return String.format("%." + decimalPlaces + "f", Double.valueOf(value.doubleValue()));
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getFormattedInt(int tagType, @NotNull String format) {
        Integer value = this._directory.getInteger(tagType);
        if (value == null) {
            return null;
        }
        return String.format(format, value);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getFormattedFloat(int tagType, @NotNull String format) {
        Float value = this._directory.getFloatObject(tagType);
        if (value == null) {
            return null;
        }
        return String.format(format, value);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getFormattedString(int tagType, @NotNull String format) {
        String value = this._directory.getString(tagType);
        if (value == null) {
            return null;
        }
        return String.format(format, value);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getEpochTimeDescription(int tagType) {
        Long value = this._directory.getLongObject(tagType);
        if (value == null) {
            return null;
        }
        return new Date(value.longValue()).toString();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getBitFlagDescription(int tagType, @NotNull Object... labels) {
        boolean isBitSet;
        char c;
        Integer value = this._directory.getInteger(tagType);
        if (value == null) {
            return null;
        }
        List<String> parts = new ArrayList<>();
        for (int bitIndex = 0; labels.length > bitIndex; bitIndex++) {
            Object labelObj = labels[bitIndex];
            if (labelObj != null) {
                if ((value.intValue() & 1) == 1) {
                    isBitSet = true;
                } else {
                    isBitSet = false;
                }
                if (labelObj instanceof String[]) {
                    String[] labelPair = (String[]) labelObj;
                    if ($assertionsDisabled || labelPair.length == 2) {
                        if (isBitSet) {
                            c = 1;
                        } else {
                            c = 0;
                        }
                        parts.add(labelPair[c]);
                    } else {
                        throw new AssertionError();
                    }
                } else if (isBitSet && (labelObj instanceof String)) {
                    parts.add((String) labelObj);
                }
            }
            value = Integer.valueOf(value.intValue() >> 1);
        }
        return StringUtil.join(parts, ", ");
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String get7BitStringFromBytes(int tagType) {
        byte[] bytes = this._directory.getByteArray(tagType);
        if (bytes == null) {
            return null;
        }
        int length = bytes.length;
        int index = 0;
        while (true) {
            if (index >= bytes.length) {
                break;
            }
            int i = bytes[index] & 255;
            if (i == 0 || i > 127) {
                length = index;
            } else {
                index++;
            }
        }
        return new String(bytes, 0, length);
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getStringFromBytes(int tag, Charset cs) {
        byte[] values = this._directory.getByteArray(tag);
        if (values == null) {
            return null;
        }
        try {
            return new String(values, cs.name()).trim();
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getRationalOrDoubleString(int tagType) {
        Rational rational = this._directory.getRational(tagType);
        if (rational != null) {
            return rational.toSimpleString(true);
        }
        Double d = this._directory.getDoubleObject(tagType);
        if (d != null) {
            return new DecimalFormat("0.###").format(d);
        }
        return null;
    }

    @Nullable
    protected static String getFStopDescription(double fStop) {
        DecimalFormat format = new DecimalFormat("0.0");
        format.setRoundingMode(RoundingMode.HALF_UP);
        return "f/" + format.format(fStop);
    }

    @Nullable
    protected static String getFocalLengthDescription(double mm) {
        DecimalFormat format = new DecimalFormat("0.#");
        format.setRoundingMode(RoundingMode.HALF_UP);
        return format.format(mm) + " mm";
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getLensSpecificationDescription(int tag) {
        Rational[] values = this._directory.getRationalArray(tag);
        if (values == null || values.length != 4 || (values[0].isZero() && values[2].isZero())) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        if (values[0].equals(values[1])) {
            sb.append(values[0].toSimpleString(true)).append("mm");
        } else {
            sb.append(values[0].toSimpleString(true)).append('-').append(values[1].toSimpleString(true)).append("mm");
        }
        if (!values[2].isZero()) {
            sb.append(' ');
            DecimalFormat format = new DecimalFormat("0.0");
            format.setRoundingMode(RoundingMode.HALF_UP);
            if (values[2].equals(values[3])) {
                sb.append(getFStopDescription(values[2].doubleValue()));
            } else {
                sb.append("f/").append(format.format(values[2].doubleValue())).append('-').append(format.format(values[3].doubleValue()));
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getOrientationDescription(int tag) {
        return getIndexedDescription(tag, 1, "Top, left side (Horizontal / normal)", "Top, right side (Mirror horizontal)", "Bottom, right side (Rotate 180)", "Bottom, left side (Mirror vertical)", "Left side, top (Mirror horizontal and rotate 270 CW)", "Right side, top (Rotate 90 CW)", "Right side, bottom (Mirror horizontal and rotate 90 CW)", "Left side, bottom (Rotate 270 CW)");
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getShutterSpeedDescription(int tag) {
        Float apexValue = this._directory.getFloatObject(tag);
        if (apexValue == null) {
            return null;
        }
        if (apexValue.floatValue() <= 1.0f) {
            float fApexPower = ((float) Math.round(((double) ((float) (1.0d / Math.exp(((double) apexValue.floatValue()) * Math.log(2.0d))))) * 10.0d)) / 10.0f;
            DecimalFormat format = new DecimalFormat("0.##");
            format.setRoundingMode(RoundingMode.HALF_UP);
            return format.format((double) fApexPower) + " sec";
        }
        return "1/" + ((int) Math.exp(((double) apexValue.floatValue()) * Math.log(2.0d))) + " sec";
    }

    /* access modifiers changed from: protected */
    @Nullable
    public String getLightSourceDescription(short wbtype) {
        switch (wbtype) {
            case 0:
                return "Unknown";
            case 1:
                return "Daylight";
            case 2:
                return "Fluorescent";
            case 3:
                return "Tungsten (Incandescent)";
            case 4:
                return "Flash";
            case 9:
                return "Fine Weather";
            case 10:
                return "Cloudy";
            case 11:
                return "Shade";
            case 12:
                return "Daylight Fluorescent";
            case 13:
                return "Day White Fluorescent";
            case 14:
                return "Cool White Fluorescent";
            case 15:
                return "White Fluorescent";
            case 16:
                return "Warm White Fluorescent";
            case 17:
                return "Standard Light A";
            case 18:
                return "Standard Light B";
            case 19:
                return "Standard Light C";
            case 20:
                return "D55";
            case 21:
                return "D65";
            case 22:
                return "D75";
            case 23:
                return "D50";
            case 24:
                return "ISO Studio Tungsten";
            case 255:
                return "Other";
            default:
                return getDescription(wbtype);
        }
    }
}

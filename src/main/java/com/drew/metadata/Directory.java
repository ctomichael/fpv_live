package com.drew.metadata;

import com.drew.lang.Rational;
import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import com.drew.lang.annotations.SuppressWarnings;
import dji.log.DJILogUtils;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class Directory {
    static final /* synthetic */ boolean $assertionsDisabled = (!Directory.class.desiredAssertionStatus());
    private static final String _floatFormatPattern = "0.###";
    @NotNull
    protected final Collection<Tag> _definedTagList = new ArrayList();
    protected TagDescriptor _descriptor;
    @NotNull
    private final Collection<String> _errorList = new ArrayList(4);
    @Nullable
    private Directory _parent;
    @NotNull
    protected final Map<Integer, Object> _tagMap = new HashMap();

    @NotNull
    public abstract String getName();

    /* access modifiers changed from: protected */
    @NotNull
    public abstract HashMap<Integer, String> getTagNameMap();

    protected Directory() {
    }

    public boolean isEmpty() {
        return this._errorList.isEmpty() && this._definedTagList.isEmpty();
    }

    public boolean containsTag(int tagType) {
        return this._tagMap.containsKey(Integer.valueOf(tagType));
    }

    @NotNull
    public Collection<Tag> getTags() {
        return Collections.unmodifiableCollection(this._definedTagList);
    }

    public int getTagCount() {
        return this._definedTagList.size();
    }

    public void setDescriptor(@NotNull TagDescriptor descriptor) {
        if (descriptor == null) {
            throw new NullPointerException("cannot set a null descriptor");
        }
        this._descriptor = descriptor;
    }

    public void addError(@NotNull String message) {
        this._errorList.add(message);
    }

    public boolean hasErrors() {
        return this._errorList.size() > 0;
    }

    @NotNull
    public Iterable<String> getErrors() {
        return Collections.unmodifiableCollection(this._errorList);
    }

    public int getErrorCount() {
        return this._errorList.size();
    }

    @Nullable
    public Directory getParent() {
        return this._parent;
    }

    public void setParent(@NotNull Directory parent) {
        this._parent = parent;
    }

    public void setInt(int tagType, int value) {
        setObject(tagType, Integer.valueOf(value));
    }

    public void setIntArray(int tagType, @NotNull int[] ints) {
        setObjectArray(tagType, ints);
    }

    public void setFloat(int tagType, float value) {
        setObject(tagType, Float.valueOf(value));
    }

    public void setFloatArray(int tagType, @NotNull float[] floats) {
        setObjectArray(tagType, floats);
    }

    public void setDouble(int tagType, double value) {
        setObject(tagType, Double.valueOf(value));
    }

    public void setDoubleArray(int tagType, @NotNull double[] doubles) {
        setObjectArray(tagType, doubles);
    }

    public void setStringValue(int tagType, @NotNull StringValue value) {
        if (value == null) {
            throw new NullPointerException("cannot set a null StringValue");
        }
        setObject(tagType, value);
    }

    public void setString(int tagType, @NotNull String value) {
        if (value == null) {
            throw new NullPointerException("cannot set a null String");
        }
        setObject(tagType, value);
    }

    public void setStringArray(int tagType, @NotNull String[] strings) {
        setObjectArray(tagType, strings);
    }

    public void setStringValueArray(int tagType, @NotNull StringValue[] strings) {
        setObjectArray(tagType, strings);
    }

    public void setBoolean(int tagType, boolean value) {
        setObject(tagType, Boolean.valueOf(value));
    }

    public void setLong(int tagType, long value) {
        setObject(tagType, Long.valueOf(value));
    }

    public void setDate(int tagType, @NotNull Date value) {
        setObject(tagType, value);
    }

    public void setRational(int tagType, @NotNull Rational rational) {
        setObject(tagType, rational);
    }

    public void setRationalArray(int tagType, @NotNull Rational[] rationals) {
        setObjectArray(tagType, rationals);
    }

    public void setByteArray(int tagType, @NotNull byte[] bytes) {
        setObjectArray(tagType, bytes);
    }

    public void setObject(int tagType, @NotNull Object value) {
        if (value == null) {
            throw new NullPointerException("cannot set a null object");
        }
        if (!this._tagMap.containsKey(Integer.valueOf(tagType))) {
            this._definedTagList.add(new Tag(tagType, this));
        }
        this._tagMap.put(Integer.valueOf(tagType), value);
    }

    public void setObjectArray(int tagType, @NotNull Object array) {
        setObject(tagType, array);
    }

    public int getInt(int tagType) throws MetadataException {
        Integer integer = getInteger(tagType);
        if (integer != null) {
            return integer.intValue();
        }
        Object o = getObject(tagType);
        if (o == null) {
            throw new MetadataException("Tag '" + getTagName(tagType) + "' has not been set -- check using containsTag() first");
        }
        throw new MetadataException("Tag '" + tagType + "' cannot be converted to int.  It is of type '" + o.getClass() + "'.");
    }

    @Nullable
    public Integer getInteger(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if (o instanceof Number) {
            return Integer.valueOf(((Number) o).intValue());
        }
        if ((o instanceof String) || (o instanceof StringValue)) {
            try {
                return Integer.valueOf(Integer.parseInt(o.toString()));
            } catch (NumberFormatException e) {
                long val = 0;
                for (byte aByte : o.toString().getBytes()) {
                    val = (val << 8) + ((long) (aByte & 255));
                }
                return Integer.valueOf((int) val);
            }
        } else {
            if (o instanceof Rational[]) {
                Rational[] rationals = (Rational[]) o;
                if (rationals.length == 1) {
                    return Integer.valueOf(rationals[0].intValue());
                }
            } else if (o instanceof byte[]) {
                byte[] bytes = (byte[]) o;
                if (bytes.length == 1) {
                    return Integer.valueOf(bytes[0]);
                }
            } else if (o instanceof int[]) {
                int[] ints = (int[]) o;
                if (ints.length == 1) {
                    return Integer.valueOf(ints[0]);
                }
            } else if (o instanceof short[]) {
                short[] shorts = (short[]) o;
                if (shorts.length == 1) {
                    return Integer.valueOf(shorts[0]);
                }
            }
            return null;
        }
    }

    @Nullable
    public String[] getStringArray(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if (o instanceof String[]) {
            return (String[]) o;
        }
        if (o instanceof String) {
            return new String[]{(String) o};
        } else if (o instanceof StringValue) {
            return new String[]{o.toString()};
        } else if (o instanceof StringValue[]) {
            StringValue[] stringValues = (StringValue[]) o;
            String[] strings = new String[stringValues.length];
            for (int i = 0; i < strings.length; i++) {
                strings[i] = stringValues[i].toString();
            }
            return strings;
        } else if (o instanceof int[]) {
            int[] ints = (int[]) o;
            String[] strings2 = new String[ints.length];
            for (int i2 = 0; i2 < strings2.length; i2++) {
                strings2[i2] = Integer.toString(ints[i2]);
            }
            return strings2;
        } else if (o instanceof byte[]) {
            byte[] bytes = (byte[]) o;
            String[] strings3 = new String[bytes.length];
            for (int i3 = 0; i3 < strings3.length; i3++) {
                strings3[i3] = Byte.toString(bytes[i3]);
            }
            return strings3;
        } else if (!(o instanceof Rational[])) {
            return null;
        } else {
            Rational[] rationals = (Rational[]) o;
            String[] strings4 = new String[rationals.length];
            for (int i4 = 0; i4 < strings4.length; i4++) {
                strings4[i4] = rationals[i4].toSimpleString(false);
            }
            return strings4;
        }
    }

    @Nullable
    public StringValue[] getStringValueArray(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if (o instanceof StringValue[]) {
            return (StringValue[]) o;
        }
        if (!(o instanceof StringValue)) {
            return null;
        }
        return new StringValue[]{(StringValue) o};
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: int[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: int[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    @com.drew.lang.annotations.Nullable
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int[] getIntArray(int r10) {
        /*
            r9 = this;
            r2 = 0
            java.lang.Object r3 = r9.getObject(r10)
            if (r3 != 0) goto L_0x0008
        L_0x0007:
            return r2
        L_0x0008:
            boolean r7 = r3 instanceof int[]
            if (r7 == 0) goto L_0x0012
            int[] r3 = (int[]) r3
            int[] r3 = (int[]) r3
            r2 = r3
            goto L_0x0007
        L_0x0012:
            boolean r7 = r3 instanceof com.drew.lang.Rational[]
            if (r7 == 0) goto L_0x002d
            com.drew.lang.Rational[] r3 = (com.drew.lang.Rational[]) r3
            r4 = r3
            com.drew.lang.Rational[] r4 = (com.drew.lang.Rational[]) r4
            int r7 = r4.length
            int[] r2 = new int[r7]
            r1 = 0
        L_0x001f:
            int r7 = r2.length
            if (r1 >= r7) goto L_0x0007
            r7 = r4[r1]
            int r7 = r7.intValue()
            r2[r1] = r7
            int r1 = r1 + 1
            goto L_0x001f
        L_0x002d:
            boolean r7 = r3 instanceof short[]
            if (r7 == 0) goto L_0x0044
            short[] r3 = (short[]) r3
            r5 = r3
            short[] r5 = (short[]) r5
            int r7 = r5.length
            int[] r2 = new int[r7]
            r1 = 0
        L_0x003a:
            int r7 = r5.length
            if (r1 >= r7) goto L_0x0007
            short r7 = r5[r1]
            r2[r1] = r7
            int r1 = r1 + 1
            goto L_0x003a
        L_0x0044:
            boolean r7 = r3 instanceof byte[]
            if (r7 == 0) goto L_0x005b
            byte[] r3 = (byte[]) r3
            r0 = r3
            byte[] r0 = (byte[]) r0
            int r7 = r0.length
            int[] r2 = new int[r7]
            r1 = 0
        L_0x0051:
            int r7 = r0.length
            if (r1 >= r7) goto L_0x0007
            byte r7 = r0[r1]
            r2[r1] = r7
            int r1 = r1 + 1
            goto L_0x0051
        L_0x005b:
            boolean r7 = r3 instanceof java.lang.CharSequence
            if (r7 == 0) goto L_0x0078
            r6 = r3
            java.lang.CharSequence r6 = (java.lang.CharSequence) r6
            int r7 = r6.length()
            int[] r2 = new int[r7]
            r1 = 0
        L_0x0069:
            int r7 = r6.length()
            if (r1 >= r7) goto L_0x0007
            char r7 = r6.charAt(r1)
            r2[r1] = r7
            int r1 = r1 + 1
            goto L_0x0069
        L_0x0078:
            boolean r7 = r3 instanceof java.lang.Integer
            if (r7 == 0) goto L_0x0007
            r7 = 1
            int[] r2 = new int[r7]
            r7 = 0
            java.lang.Integer r3 = (java.lang.Integer) r3
            int r8 = r3.intValue()
            r2[r7] = r8
            goto L_0x0007
        */
        throw new UnsupportedOperationException("Method not decompiled: com.drew.metadata.Directory.getIntArray(int):int[]");
    }

    @Nullable
    public byte[] getByteArray(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if (o instanceof StringValue) {
            return ((StringValue) o).getBytes();
        }
        if (o instanceof Rational[]) {
            Rational[] rationals = (Rational[]) o;
            byte[] bytes = new byte[rationals.length];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = rationals[i].byteValue();
            }
            return bytes;
        } else if (o instanceof byte[]) {
            return (byte[]) o;
        } else {
            if (o instanceof int[]) {
                int[] ints = (int[]) o;
                byte[] bytes2 = new byte[ints.length];
                for (int i2 = 0; i2 < ints.length; i2++) {
                    bytes2[i2] = (byte) ints[i2];
                }
                return bytes2;
            } else if (o instanceof short[]) {
                short[] shorts = (short[]) o;
                byte[] bytes3 = new byte[shorts.length];
                for (int i3 = 0; i3 < shorts.length; i3++) {
                    bytes3[i3] = (byte) shorts[i3];
                }
                return bytes3;
            } else if (o instanceof CharSequence) {
                CharSequence str = (CharSequence) o;
                byte[] bytes4 = new byte[str.length()];
                for (int i4 = 0; i4 < str.length(); i4++) {
                    bytes4[i4] = (byte) str.charAt(i4);
                }
                return bytes4;
            } else if (!(o instanceof Integer)) {
                return null;
            } else {
                return new byte[]{((Integer) o).byteValue()};
            }
        }
    }

    public double getDouble(int tagType) throws MetadataException {
        Double value = getDoubleObject(tagType);
        if (value != null) {
            return value.doubleValue();
        }
        Object o = getObject(tagType);
        if (o == null) {
            throw new MetadataException("Tag '" + getTagName(tagType) + "' has not been set -- check using containsTag() first");
        }
        throw new MetadataException("Tag '" + tagType + "' cannot be converted to a double.  It is of type '" + o.getClass() + "'.");
    }

    @Nullable
    public Double getDoubleObject(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if ((o instanceof String) || (o instanceof StringValue)) {
            try {
                return Double.valueOf(Double.parseDouble(o.toString()));
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (o instanceof Number) {
            return Double.valueOf(((Number) o).doubleValue());
        } else {
            return null;
        }
    }

    public float getFloat(int tagType) throws MetadataException {
        Float value = getFloatObject(tagType);
        if (value != null) {
            return value.floatValue();
        }
        Object o = getObject(tagType);
        if (o == null) {
            throw new MetadataException("Tag '" + getTagName(tagType) + "' has not been set -- check using containsTag() first");
        }
        throw new MetadataException("Tag '" + tagType + "' cannot be converted to a float.  It is of type '" + o.getClass() + "'.");
    }

    @Nullable
    public Float getFloatObject(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if ((o instanceof String) || (o instanceof StringValue)) {
            try {
                return Float.valueOf(Float.parseFloat(o.toString()));
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (o instanceof Number) {
            return Float.valueOf(((Number) o).floatValue());
        } else {
            return null;
        }
    }

    public long getLong(int tagType) throws MetadataException {
        Long value = getLongObject(tagType);
        if (value != null) {
            return value.longValue();
        }
        Object o = getObject(tagType);
        if (o == null) {
            throw new MetadataException("Tag '" + getTagName(tagType) + "' has not been set -- check using containsTag() first");
        }
        throw new MetadataException("Tag '" + tagType + "' cannot be converted to a long.  It is of type '" + o.getClass() + "'.");
    }

    @Nullable
    public Long getLongObject(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if ((o instanceof String) || (o instanceof StringValue)) {
            try {
                return Long.valueOf(Long.parseLong(o.toString()));
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (o instanceof Number) {
            return Long.valueOf(((Number) o).longValue());
        } else {
            return null;
        }
    }

    public boolean getBoolean(int tagType) throws MetadataException {
        Boolean value = getBooleanObject(tagType);
        if (value != null) {
            return value.booleanValue();
        }
        Object o = getObject(tagType);
        if (o == null) {
            throw new MetadataException("Tag '" + getTagName(tagType) + "' has not been set -- check using containsTag() first");
        }
        throw new MetadataException("Tag '" + tagType + "' cannot be converted to a boolean.  It is of type '" + o.getClass() + "'.");
    }

    @Nullable
    @SuppressWarnings(justification = "keep API interface consistent", value = "NP_BOOLEAN_RETURN_NULL")
    public Boolean getBooleanObject(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if (o instanceof Boolean) {
            return (Boolean) o;
        }
        if ((o instanceof String) || (o instanceof StringValue)) {
            try {
                return Boolean.valueOf(Boolean.getBoolean(o.toString()));
            } catch (NumberFormatException e) {
                return null;
            }
        } else if (!(o instanceof Number)) {
            return null;
        } else {
            return Boolean.valueOf(((Number) o).doubleValue() != 0.0d);
        }
    }

    @Nullable
    public Date getDate(int tagType) {
        return getDate(tagType, null, null);
    }

    @Nullable
    public Date getDate(int tagType, @Nullable TimeZone timeZone) {
        return getDate(tagType, null, timeZone);
    }

    @Nullable
    public Date getDate(int tagType, @Nullable String subsecond, @Nullable TimeZone timeZone) {
        Object o = getObject(tagType);
        if (o instanceof Date) {
            return (Date) o;
        }
        Date date = null;
        if ((o instanceof String) || (o instanceof StringValue)) {
            String[] datePatterns = {"yyyy:MM:dd HH:mm:ss", "yyyy:MM:dd HH:mm", DJILogUtils.FORMAT_2, "yyyy-MM-dd HH:mm", "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm", DJILogUtils.FORMAT_1, "yyyy-MM", "yyyyMMdd", "yyyy"};
            String dateString = o.toString();
            Matcher subsecondMatcher = Pattern.compile("(\\d\\d:\\d\\d:\\d\\d)(\\.\\d+)").matcher(dateString);
            if (subsecondMatcher.find()) {
                subsecond = subsecondMatcher.group(2).substring(1);
                dateString = subsecondMatcher.replaceAll("$1");
            }
            Matcher timeZoneMatcher = Pattern.compile("(Z|[+-]\\d\\d:\\d\\d)$").matcher(dateString);
            if (timeZoneMatcher.find()) {
                timeZone = TimeZone.getTimeZone("GMT" + timeZoneMatcher.group().replaceAll("Z", ""));
                dateString = timeZoneMatcher.replaceAll("");
            }
            String[] arr$ = datePatterns;
            int i$ = 0;
            while (true) {
                if (i$ >= arr$.length) {
                    break;
                }
                try {
                    DateFormat parser = new SimpleDateFormat(arr$[i$]);
                    if (timeZone != null) {
                        parser.setTimeZone(timeZone);
                    } else {
                        parser.setTimeZone(TimeZone.getTimeZone("GMT"));
                    }
                    date = parser.parse(dateString);
                } catch (ParseException e) {
                    i$++;
                }
            }
        }
        if (date == null) {
            return null;
        }
        if (subsecond == null) {
            return date;
        }
        try {
            int millisecond = (int) (Double.parseDouble("." + subsecond) * 1000.0d);
            if (millisecond < 0 || millisecond >= 1000) {
                return date;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.set(14, millisecond);
            return calendar.getTime();
        } catch (NumberFormatException e2) {
            return date;
        }
    }

    @Nullable
    public Rational getRational(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if (o instanceof Rational) {
            return (Rational) o;
        }
        if (o instanceof Integer) {
            return new Rational((long) ((Integer) o).intValue(), 1);
        }
        if (o instanceof Long) {
            return new Rational(((Long) o).longValue(), 1);
        }
        return null;
    }

    @Nullable
    public Rational[] getRationalArray(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if (o instanceof Rational[]) {
            return (Rational[]) o;
        }
        return null;
    }

    @Nullable
    public String getString(int tagType) {
        Object o = getObject(tagType);
        if (o == null) {
            return null;
        }
        if (o instanceof Rational) {
            return ((Rational) o).toSimpleString(true);
        }
        if (o.getClass().isArray()) {
            int arrayLength = Array.getLength(o);
            Class<?> componentType = o.getClass().getComponentType();
            StringBuilder string = new StringBuilder();
            if (Object.class.isAssignableFrom(componentType)) {
                for (int i = 0; i < arrayLength; i++) {
                    if (i != 0) {
                        string.append(' ');
                    }
                    string.append(Array.get(o, i).toString());
                }
            } else if (componentType.getName().equals("int")) {
                for (int i2 = 0; i2 < arrayLength; i2++) {
                    if (i2 != 0) {
                        string.append(' ');
                    }
                    string.append(Array.getInt(o, i2));
                }
            } else if (componentType.getName().equals("short")) {
                for (int i3 = 0; i3 < arrayLength; i3++) {
                    if (i3 != 0) {
                        string.append(' ');
                    }
                    string.append((int) Array.getShort(o, i3));
                }
            } else if (componentType.getName().equals("long")) {
                for (int i4 = 0; i4 < arrayLength; i4++) {
                    if (i4 != 0) {
                        string.append(' ');
                    }
                    string.append(Array.getLong(o, i4));
                }
            } else if (componentType.getName().equals("float")) {
                for (int i5 = 0; i5 < arrayLength; i5++) {
                    if (i5 != 0) {
                        string.append(' ');
                    }
                    string.append(new DecimalFormat(_floatFormatPattern).format((double) Array.getFloat(o, i5)));
                }
            } else if (componentType.getName().equals("double")) {
                for (int i6 = 0; i6 < arrayLength; i6++) {
                    if (i6 != 0) {
                        string.append(' ');
                    }
                    string.append(new DecimalFormat(_floatFormatPattern).format(Array.getDouble(o, i6)));
                }
            } else if (componentType.getName().equals("byte")) {
                for (int i7 = 0; i7 < arrayLength; i7++) {
                    if (i7 != 0) {
                        string.append(' ');
                    }
                    string.append((int) (Array.getByte(o, i7) & 255));
                }
            } else {
                addError("Unexpected array component type: " + componentType.getName());
            }
            return string.toString();
        } else if (o instanceof Double) {
            return new DecimalFormat(_floatFormatPattern).format(((Double) o).doubleValue());
        } else {
            if (o instanceof Float) {
                return new DecimalFormat(_floatFormatPattern).format((double) ((Float) o).floatValue());
            }
            return o.toString();
        }
    }

    @Nullable
    public String getString(int tagType, String charset) {
        byte[] bytes = getByteArray(tagType);
        if (bytes == null) {
            return null;
        }
        try {
            return new String(bytes, charset);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    @Nullable
    public StringValue getStringValue(int tagType) {
        Object o = getObject(tagType);
        if (o instanceof StringValue) {
            return (StringValue) o;
        }
        return null;
    }

    @Nullable
    public Object getObject(int tagType) {
        return this._tagMap.get(Integer.valueOf(tagType));
    }

    @NotNull
    public String getTagName(int tagType) {
        HashMap<Integer, String> nameMap = getTagNameMap();
        if (nameMap.containsKey(Integer.valueOf(tagType))) {
            return nameMap.get(Integer.valueOf(tagType));
        }
        String hex = Integer.toHexString(tagType);
        while (hex.length() < 4) {
            hex = "0" + hex;
        }
        return "Unknown tag (0x" + hex + ")";
    }

    public boolean hasTagName(int tagType) {
        return getTagNameMap().containsKey(Integer.valueOf(tagType));
    }

    @Nullable
    public String getDescription(int tagType) {
        if ($assertionsDisabled || this._descriptor != null) {
            return this._descriptor.getDescription(tagType);
        }
        throw new AssertionError();
    }

    public String toString() {
        Object[] objArr = new Object[3];
        objArr[0] = getName();
        objArr[1] = Integer.valueOf(this._tagMap.size());
        objArr[2] = this._tagMap.size() == 1 ? "tag" : "tags";
        return String.format("%s Directory (%d %s)", objArr);
    }
}

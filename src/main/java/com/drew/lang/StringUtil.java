package com.drew.lang;

import com.drew.lang.annotations.NotNull;
import com.drew.lang.annotations.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

public final class StringUtil {
    @NotNull
    public static String join(@NotNull Iterable<? extends CharSequence> strings, @NotNull String delimiter) {
        int capacity = 0;
        int delimLength = delimiter.length();
        Iterator<? extends CharSequence> iter = strings.iterator();
        if (iter.hasNext()) {
            capacity = 0 + ((CharSequence) iter.next()).length() + delimLength;
        }
        StringBuilder buffer = new StringBuilder(capacity);
        Iterator<? extends CharSequence> iter2 = strings.iterator();
        if (iter2.hasNext()) {
            buffer.append((CharSequence) iter2.next());
            while (iter2.hasNext()) {
                buffer.append(delimiter);
                buffer.append((CharSequence) iter2.next());
            }
        }
        return buffer.toString();
    }

    @NotNull
    public static <T extends CharSequence> String join(@NotNull T[] strings, @NotNull String delimiter) {
        int capacity = 0;
        int delimLength = delimiter.length();
        for (T value : strings) {
            capacity += value.length() + delimLength;
        }
        StringBuilder buffer = new StringBuilder(capacity);
        boolean first = true;
        T[] arr$ = strings;
        for (T value2 : arr$) {
            if (!first) {
                buffer.append(delimiter);
            } else {
                first = false;
            }
            buffer.append((CharSequence) value2);
        }
        return buffer.toString();
    }

    @NotNull
    public static String fromStream(@NotNull InputStream stream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder sb = new StringBuilder();
        while (true) {
            String line = reader.readLine();
            if (line == null) {
                return sb.toString();
            }
            sb.append(line);
        }
    }

    public static int compare(@Nullable String s1, @Nullable String s2) {
        boolean null1;
        boolean null2;
        if (s1 == null) {
            null1 = true;
        } else {
            null1 = false;
        }
        if (s2 == null) {
            null2 = true;
        } else {
            null2 = false;
        }
        if (null1 && null2) {
            return 0;
        }
        if (null1) {
            return -1;
        }
        if (null2) {
            return 1;
        }
        return s1.compareTo(s2);
    }

    @NotNull
    public static String urlEncode(@NotNull String name) {
        return name.replace(" ", "%20");
    }
}

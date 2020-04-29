package com.mapbox.mapboxsdk.module.http;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import okhttp3.OkHttpClient;
import okio.Buffer;

public class HttpRequestUtil {
    public static void setLogEnabled(boolean enabled) {
        HttpRequestImpl.enableLog(enabled);
    }

    public static void setPrintRequestUrlOnFailure(boolean enabled) {
        HttpRequestImpl.enablePrintRequestUrlOnFailure(enabled);
    }

    public static void setOkHttpClient(@Nullable OkHttpClient client) {
        HttpRequestImpl.setOkHttpClient(client);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.writeUtf8(java.lang.String, int, int):okio.Buffer
     arg types: [java.lang.String, int, int]
     candidates:
      okio.Buffer.writeUtf8(java.lang.String, int, int):okio.BufferedSink
      okio.BufferedSink.writeUtf8(java.lang.String, int, int):okio.BufferedSink
      okio.Buffer.writeUtf8(java.lang.String, int, int):okio.Buffer */
    @NonNull
    static String toHumanReadableAscii(String s) {
        int i = 0;
        int length = s.length();
        while (i < length) {
            int c = s.codePointAt(i);
            if (c <= 31 || c >= 127) {
                Buffer buffer = new Buffer();
                buffer.writeUtf8(s, 0, i);
                int j = i;
                while (j < length) {
                    int c2 = s.codePointAt(j);
                    buffer.writeUtf8CodePoint((c2 <= 31 || c2 >= 127) ? 63 : c2);
                    j += Character.charCount(c2);
                }
                return buffer.readUtf8();
            }
            i += Character.charCount(c);
        }
        return s;
    }
}

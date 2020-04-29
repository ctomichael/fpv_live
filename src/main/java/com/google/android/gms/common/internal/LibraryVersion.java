package com.google.android.gms.common.internal;

import android.support.annotation.NonNull;
import com.billy.cc.core.component.CCUtil;
import com.google.android.gms.common.annotation.KeepForSdk;
import com.google.android.gms.common.util.VisibleForTesting;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

@KeepForSdk
public class LibraryVersion {
    private static final GmsLogger zzel = new GmsLogger("LibraryVersion", "");
    private static LibraryVersion zzem = new LibraryVersion();
    private ConcurrentHashMap<String, String> zzen = new ConcurrentHashMap<>();

    @KeepForSdk
    public static LibraryVersion getInstance() {
        return zzem;
    }

    @VisibleForTesting
    protected LibraryVersion() {
    }

    @KeepForSdk
    public String getVersion(@NonNull String str) {
        String str2;
        Preconditions.checkNotEmpty(str, "Please provide a valid libraryName");
        if (this.zzen.containsKey(str)) {
            return this.zzen.get(str);
        }
        Properties properties = new Properties();
        try {
            InputStream resourceAsStream = LibraryVersion.class.getResourceAsStream(String.format("/%s.properties", str));
            if (resourceAsStream != null) {
                properties.load(resourceAsStream);
                String property = properties.getProperty("version", null);
                zzel.v("LibraryVersion", new StringBuilder(String.valueOf(str).length() + 12 + String.valueOf(property).length()).append(str).append(" version is ").append(property).toString());
                str2 = property;
            } else {
                GmsLogger gmsLogger = zzel;
                String valueOf = String.valueOf(str);
                gmsLogger.e("LibraryVersion", valueOf.length() != 0 ? "Failed to get app version for libraryName: ".concat(valueOf) : new String("Failed to get app version for libraryName: "));
                str2 = null;
            }
        } catch (IOException e) {
            IOException iOException = e;
            GmsLogger gmsLogger2 = zzel;
            String valueOf2 = String.valueOf(str);
            gmsLogger2.e("LibraryVersion", valueOf2.length() != 0 ? "Failed to get app version for libraryName: ".concat(valueOf2) : new String("Failed to get app version for libraryName: "), iOException);
            str2 = null;
        }
        if (str2 == null) {
            str2 = CCUtil.PROCESS_UNKNOWN;
            zzel.d("LibraryVersion", ".properties file is dropped during release process. Failure to read app version isexpected druing Google internal testing where locally-built libraries are used");
        }
        this.zzen.put(str, str2);
        return str2;
    }
}

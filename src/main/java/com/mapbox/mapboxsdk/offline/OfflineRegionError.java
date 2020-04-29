package com.mapbox.mapboxsdk.offline;

import android.support.annotation.Keep;
import android.support.annotation.NonNull;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class OfflineRegionError {
    public static final String REASON_CONNECTION = "REASON_CONNECTION";
    public static final String REASON_NOT_FOUND = "REASON_NOT_FOUND";
    public static final String REASON_OTHER = "REASON_OTHER";
    public static final String REASON_SERVER = "REASON_SERVER";
    public static final String REASON_SUCCESS = "REASON_SUCCESS";
    @NonNull
    private final String message;
    @NonNull
    private final String reason;

    @Retention(RetentionPolicy.SOURCE)
    public @interface ErrorReason {
    }

    @Keep
    private OfflineRegionError(@NonNull String reason2, @NonNull String message2) {
        this.reason = reason2;
        this.message = message2;
    }

    @NonNull
    public String getReason() {
        return this.reason;
    }

    @NonNull
    public String getMessage() {
        return this.message;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OfflineRegionError that = (OfflineRegionError) o;
        if (this.reason.equals(that.reason)) {
            return this.message.equals(that.message);
        }
        return false;
    }

    public int hashCode() {
        return (this.reason.hashCode() * 31) + this.message.hashCode();
    }

    public String toString() {
        return "OfflineRegionError{reason='" + this.reason + '\'' + ", message='" + this.message + '\'' + '}';
    }
}

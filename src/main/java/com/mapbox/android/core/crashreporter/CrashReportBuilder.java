package com.mapbox.android.core.crashreporter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.os.EnvironmentCompat;
import dji.publics.protocol.ResponseBase;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import org.json.JSONException;

public final class CrashReportBuilder {
    private static final String OS_VERSION_FORMAT = "Android-%s";
    private static final String STACK_TRACE_ELEMENT_FORMAT = "%s.%s(%s:%d)";
    private static final String THREAD_DETAILS_FORMAT = "tid:%s|name:%s|priority:%s";
    private final Context applicationContext;
    private final List<Throwable> causalChain = new ArrayList(4);
    private boolean isSilent;
    private final String sdkIdentifier;
    private final String sdkVersion;
    private Thread uncaughtExceptionThread;

    private CrashReportBuilder(Context applicationContext2, String sdkIdentifier2, String sdkVersion2) {
        this.applicationContext = applicationContext2;
        this.sdkIdentifier = sdkIdentifier2;
        this.sdkVersion = sdkVersion2;
    }

    public static CrashReport fromJson(String json) throws IllegalArgumentException {
        try {
            return new CrashReport(json);
        } catch (JSONException je) {
            throw new IllegalArgumentException(je.toString());
        }
    }

    static CrashReportBuilder setup(Context context, String sdkIdentifier2, String sdkVersion2) {
        return new CrashReportBuilder(context, sdkIdentifier2, sdkVersion2);
    }

    /* access modifiers changed from: package-private */
    public CrashReportBuilder isSilent(boolean silent) {
        this.isSilent = silent;
        return this;
    }

    /* access modifiers changed from: package-private */
    public CrashReportBuilder addCausalChain(@NonNull List<Throwable> causalChain2) {
        this.causalChain.addAll(causalChain2);
        return this;
    }

    /* access modifiers changed from: package-private */
    public CrashReportBuilder addExceptionThread(@NonNull Thread thread) {
        this.uncaughtExceptionThread = thread;
        return this;
    }

    /* access modifiers changed from: package-private */
    public CrashReport build() {
        CrashReport report = new CrashReport(new GregorianCalendar());
        report.put("sdkIdentifier", this.sdkIdentifier);
        report.put("sdkVersion", this.sdkVersion);
        report.put("osVersion", String.format(OS_VERSION_FORMAT, Build.VERSION.RELEASE));
        report.put(ResponseBase.STRING_MODEL, Build.MODEL);
        report.put("device", Build.DEVICE);
        report.put("isSilent", Boolean.toString(this.isSilent));
        report.put("stackTraceHash", getStackTraceHash(this.causalChain));
        report.put("stackTrace", getStackTrace(this.causalChain));
        if (this.uncaughtExceptionThread != null) {
            report.put("threadDetails", String.format(THREAD_DETAILS_FORMAT, Long.valueOf(this.uncaughtExceptionThread.getId()), this.uncaughtExceptionThread.getName(), Integer.valueOf(this.uncaughtExceptionThread.getPriority())));
        }
        report.put("appId", this.applicationContext.getPackageName());
        report.put(ResponseBase.STRING_APPVERSION, getAppVersion(this.applicationContext));
        return report;
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    @NonNull
    public String getStackTrace(@NonNull List<Throwable> throwables) {
        StringBuilder result = new StringBuilder();
        for (Throwable throwable : throwables) {
            StackTraceElement[] stackTraceElements = throwable.getStackTrace();
            for (StackTraceElement element : stackTraceElements) {
                if (element.getClassName().startsWith(this.sdkIdentifier)) {
                    result.append(String.format(Locale.US, STACK_TRACE_ELEMENT_FORMAT, element.getClassName(), element.getMethodName(), element.getFileName(), Integer.valueOf(element.getLineNumber()))).append(10);
                }
            }
        }
        return result.toString();
    }

    @VisibleForTesting
    @NonNull
    static String getStackTraceHash(@NonNull List<Throwable> throwables) {
        StringBuilder result = new StringBuilder();
        for (Throwable throwable : throwables) {
            StackTraceElement[] stackTraceElements = throwable.getStackTrace();
            for (StackTraceElement element : stackTraceElements) {
                result.append(element.getClassName());
                result.append(element.getMethodName());
            }
        }
        return Integer.toHexString(result.toString().hashCode());
    }

    @NonNull
    private static String getAppVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return EnvironmentCompat.MEDIA_UNKNOWN;
        }
    }
}

package com.mapbox.android.telemetry;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import okio.Buffer;

public class TelemetryUtils {
    private static final String BACKGROUND = "Background";
    private static final String CODE_DIVISION_MULTIPLE_ACCESS = "CDMA";
    private static final String DATE_AND_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final int DEFAULT_BATTERY_LEVEL = -1;
    private static final Locale DEFAULT_LOCALE = Locale.US;
    private static final String EMPTY_STRING = "";
    private static final String ENHANCED_DATA_GSM_EVOLUTION = "EDGE";
    private static final String ENHANCED_HIGH_RATE_PACKET_DATA = "EHRPD";
    private static final String EVOLUTION_DATA_OPTIMIZED_0 = "EVDO_0";
    private static final String EVOLUTION_DATA_OPTIMIZED_A = "EVDO_A";
    private static final String EVOLUTION_DATA_OPTIMIZED_B = "EVDO_B";
    private static final String FOREGROUND = "Foreground";
    private static final String GENERAL_PACKET_RADIO_SERVICE = "GPRS";
    private static final String HIGH_SPEED_DOWNLINK_PACKET_ACCESS = "HSDPA";
    private static final String HIGH_SPEED_PACKET_ACCESS = "HSPA";
    private static final String HIGH_SPEED_PACKET_ACCESS_PLUS = "HSPAP";
    private static final String HIGH_SPEED_UNLINK_PACKET_ACCESS = "HSUPA";
    private static final String INTEGRATED_DIGITAL_ENHANCED_NETWORK = "IDEN";
    private static final String KEY_META_DATA_WAKE_UP = "com.mapbox.AdjustWakeUp";
    private static final String LONG_TERM_EVOLUTION = "LTE";
    static final String MAPBOX_SHARED_PREFERENCE_KEY_VENDOR_ID = "mapboxVendorId";
    private static final Map<Integer, String> NETWORKS = new HashMap<Integer, String>() {
        /* class com.mapbox.android.telemetry.TelemetryUtils.AnonymousClass1 */

        {
            put(7, TelemetryUtils.SINGLE_CARRIER_RTT);
            put(4, TelemetryUtils.CODE_DIVISION_MULTIPLE_ACCESS);
            put(2, TelemetryUtils.ENHANCED_DATA_GSM_EVOLUTION);
            put(14, TelemetryUtils.ENHANCED_HIGH_RATE_PACKET_DATA);
            put(5, TelemetryUtils.EVOLUTION_DATA_OPTIMIZED_0);
            put(6, TelemetryUtils.EVOLUTION_DATA_OPTIMIZED_A);
            put(12, TelemetryUtils.EVOLUTION_DATA_OPTIMIZED_B);
            put(1, TelemetryUtils.GENERAL_PACKET_RADIO_SERVICE);
            put(8, TelemetryUtils.HIGH_SPEED_DOWNLINK_PACKET_ACCESS);
            put(10, TelemetryUtils.HIGH_SPEED_PACKET_ACCESS);
            put(15, TelemetryUtils.HIGH_SPEED_PACKET_ACCESS_PLUS);
            put(9, TelemetryUtils.HIGH_SPEED_UNLINK_PACKET_ACCESS);
            put(11, TelemetryUtils.INTEGRATED_DIGITAL_ENHANCED_NETWORK);
            put(13, TelemetryUtils.LONG_TERM_EVOLUTION);
            put(3, TelemetryUtils.UNIVERSAL_MOBILE_TELCO_SERVICE);
            put(0, "Unknown");
        }
    };
    private static final String NO_STATE = "";
    private static final int PERCENT_SCALE = 100;
    private static final String SINGLE_CARRIER_RTT = "1xRTT";
    private static final String TAG = "TelemetryUtils";
    private static final String THREE_STRING_FORMAT = "%s/%s/%s";
    private static final String TWO_STRING_FORMAT = "%s %s";
    private static final int UNAVAILABLE_BATTERY_LEVEL = -1;
    private static final String UNIVERSAL_MOBILE_TELCO_SERVICE = "UMTS";
    private static final String UNKNOWN = "Unknown";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_AND_TIME_PATTERN, Locale.US);

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: okio.Buffer.writeUtf8(java.lang.String, int, int):okio.Buffer
     arg types: [java.lang.String, int, int]
     candidates:
      okio.Buffer.writeUtf8(java.lang.String, int, int):okio.BufferedSink
      okio.BufferedSink.writeUtf8(java.lang.String, int, int):okio.BufferedSink
      okio.Buffer.writeUtf8(java.lang.String, int, int):okio.Buffer */
    public static String toHumanReadableAscii(String s) {
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

    public static String obtainUniversalUniqueIdentifier() {
        return UUID.randomUUID().toString();
    }

    public static String obtainApplicationState(Context context) {
        List<ActivityManager.RunningAppProcessInfo> appProcesses = ((ActivityManager) context.getSystemService("activity")).getRunningAppProcesses();
        if (appProcesses == null) {
            return "";
        }
        String packageName = context.getPackageName();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.importance == 100 && appProcess.processName.equals(packageName)) {
                return FOREGROUND;
            }
        }
        return BACKGROUND;
    }

    public static int obtainBatteryLevel(Context context) {
        Intent batteryStatus = registerBatteryUpdates(context);
        if (batteryStatus == null) {
            return -1;
        }
        int level = batteryStatus.getIntExtra("level", -1);
        int scale = batteryStatus.getIntExtra("scale", -1);
        if (level < 0 || scale <= 0) {
            return -1;
        }
        return Math.round((((float) level) / ((float) scale)) * 100.0f);
    }

    public static boolean isPluggedIn(Context context) {
        boolean pluggedIntoUSB;
        boolean pluggedIntoAC;
        Intent batteryStatus = registerBatteryUpdates(context);
        if (batteryStatus == null) {
            return false;
        }
        int chargePlug = batteryStatus.getIntExtra("plugged", -1);
        if (chargePlug == 2) {
            pluggedIntoUSB = true;
        } else {
            pluggedIntoUSB = false;
        }
        if (chargePlug == 1) {
            pluggedIntoAC = true;
        } else {
            pluggedIntoAC = false;
        }
        if (pluggedIntoUSB || pluggedIntoAC) {
            return true;
        }
        return false;
    }

    public static String obtainCellularNetworkType(Context context) {
        return NETWORKS.get(Integer.valueOf(((TelephonyManager) context.getSystemService("phone")).getNetworkType()));
    }

    public static String obtainCurrentDate() {
        return dateFormat.format(new Date());
    }

    public static String generateCreateDateFormatted(Date date) {
        return dateFormat.format(date);
    }

    static String createFullUserAgent(String userAgent, Context context) {
        String appIdentifier = obtainApplicationIdentifier(context);
        String newUserAgent = toHumanReadableAscii(String.format(DEFAULT_LOCALE, TWO_STRING_FORMAT, appIdentifier, userAgent));
        if (TextUtils.isEmpty(appIdentifier)) {
            return userAgent;
        }
        return newUserAgent;
    }

    static boolean isEmpty(String string) {
        if (string == null || string.length() == 0) {
            return true;
        }
        return false;
    }

    public static String retrieveVendorId() {
        if (MapboxTelemetry.applicationContext == null) {
            return updateVendorId();
        }
        String mapboxVendorId = obtainSharedPreferences(MapboxTelemetry.applicationContext).getString(MAPBOX_SHARED_PREFERENCE_KEY_VENDOR_ID, "");
        if (isEmpty(mapboxVendorId)) {
            return updateVendorId();
        }
        return mapboxVendorId;
    }

    static SharedPreferences obtainSharedPreferences(Context context) {
        return context.getSharedPreferences("MapboxSharedPreferences", 0);
    }

    private static String updateVendorId() {
        String uniqueId = obtainUniversalUniqueIdentifier();
        if (MapboxTelemetry.applicationContext != null) {
            SharedPreferences.Editor editor = obtainSharedPreferences(MapboxTelemetry.applicationContext).edit();
            editor.putString(MAPBOX_SHARED_PREFERENCE_KEY_VENDOR_ID, uniqueId);
            editor.apply();
        }
        return uniqueId;
    }

    private static String obtainApplicationIdentifier(Context context) {
        try {
            String packageName = context.getPackageName();
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return String.format(DEFAULT_LOCALE, THREE_STRING_FORMAT, packageName, packageInfo.versionName, Integer.valueOf(packageInfo.versionCode));
        } catch (Exception e) {
            return "";
        }
    }

    @Nullable
    private static Intent registerBatteryUpdates(Context context) {
        try {
            return context.registerReceiver(null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
        } catch (Exception exc) {
            Log.e(TAG, String.format("%s: Failed receiver registration for ACTION_BATTERY_CHANGED", exc.toString()));
            return null;
        }
    }

    static boolean adjustWakeUpMode(Context context) {
        try {
            ApplicationInfo appInformation = context.getPackageManager().getApplicationInfo(context.getPackageName(), 128);
            if (appInformation == null || appInformation.metaData == null) {
                return false;
            }
            return appInformation.metaData.getBoolean(KEY_META_DATA_WAKE_UP, false);
        } catch (PackageManager.NameNotFoundException exception) {
            exception.printStackTrace();
            return false;
        }
    }
}

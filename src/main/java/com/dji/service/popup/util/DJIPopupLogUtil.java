package com.dji.service.popup.util;

import android.support.annotation.Nullable;
import com.dji.api.FlightHttpApi;
import com.dji.megatronking.stringfog.lib.annotation.DJIStringFog;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import java.util.Date;

@EXClassNullAway
public class DJIPopupLogUtil {
    public static boolean IsDebugForDJILog = false;
    private static final String TAG = "popup";
    @DJIStringFog
    public static String mEncryptKey = "6FAB4D6FHNB31748A958B758781C454W";
    @DJIStringFog
    public static String mSignKey = "mc15K8vpkrNjm2X7cEVxyfycyOccngMJ";

    public static String getServerUrl() {
        return FlightHttpApi.getDJIPopupV2Url();
    }

    public static void LOG_FOR_RELEASE(@Nullable String log) {
        DJILog.logWriteD(TAG, new Date() + " @ " + log, TAG, new Object[0]);
    }

    public static void LOG_FOR_DEBUG(String log) {
        if (IsDebugForDJILog) {
            DJILog.e(TAG, log, new Object[0]);
        }
    }
}

package dji.logic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.usb.P3.UsbAccessoryService;

@EXClassNullAway
public class DJIUsbAccessoryAccessManager extends BroadcastReceiver {
    public static final String ACTION_SDK_ACCESS_RELEASED = "dji.sdk.RELEASED";
    public static final String ACTION_SDK_ACCESS_REQUESTED = "dji.sdk.REQUESTED";
    private static final String TAG = "UsbAccessoryAccessManager";

    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ACTION_SDK_ACCESS_REQUESTED) && UsbAccessoryService.isConnectedToProduct()) {
            DJILog.d(TAG, "access requested", new Object[0]);
            UsbAccessoryService.disconnect();
        } else if (action.equals(ACTION_SDK_ACCESS_RELEASED) && UsbAccessoryService.isWaitingForDisconnect()) {
            DJILog.d(TAG, "access released", new Object[0]);
            UsbAccessoryService.connect();
        }
    }
}

package dji.logic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.ServiceManager;

@Keep
@EXClassNullAway
public class DJIPilotStartupReceiver extends BroadcastReceiver {
    public static final String ACTION_GO3 = "dji.go3.STARTUP";
    public static final String ACTION_GO4 = "dji.go4.STARTUP";
    public static final String ACTION_SDK = "dji.sdk.STARTUP";
    public static boolean ANOTHER_DJIGO_STARTED = false;

    public void onReceive(Context context, Intent intent) {
        Log.i("DJIPilotStartupReceiver", "receive action:" + intent.getAction());
        if (intent.getAction().equals(ACTION_SDK)) {
            ANOTHER_DJIGO_STARTED = true;
            ServiceManager.Destroy();
        }
    }
}

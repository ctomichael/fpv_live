package dji.midware.data.manager.Dpad;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIScreenManager {
    private static final String TAG = "DJIScreenManager";
    private Activity mCtx;
    private ScreenReceiver mScreenReceiver;
    private boolean mSupportScreenReceiver;

    public DJIScreenManager() {
        this.mSupportScreenReceiver = false;
        this.mSupportScreenReceiver = DpadProductManager.getInstance().isDpad();
    }

    public void onCreate(Activity context) {
        LOG("onCreate,context=" + context);
        this.mCtx = context;
        DJIEventBusUtil.register(this);
        if (this.mSupportScreenReceiver) {
            IntentFilter filter = new IntentFilter("android.intent.action.SCREEN_OFF");
            filter.addAction("android.intent.action.SCREEN_ON");
            this.mScreenReceiver = new ScreenReceiver();
            this.mCtx.registerReceiver(this.mScreenReceiver, filter);
        }
        keepOn(true);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent3MainThread(DataCameraEvent event) {
        if (event.equals(DataCameraEvent.ConnectOK)) {
            keepOn(true);
        } else {
            keepOn(false);
        }
    }

    /* access modifiers changed from: private */
    public void keepOn(boolean isOn) {
        LOG("keepOn,isOn=" + isOn);
        if (this.mCtx == null) {
            return;
        }
        if (isOn) {
            this.mCtx.getWindow().addFlags(128);
        } else if (!DataOsdGetPushCommon.getInstance().isMotorUp()) {
            LOG("keepOn,isMotorUp=false,will off screen!");
            this.mCtx.getWindow().clearFlags(128);
        }
    }

    public void onDestroy() {
        LOG("onDestroy");
        DJIEventBusUtil.unRegister(this);
        if (this.mScreenReceiver != null) {
            this.mCtx.unregisterReceiver(this.mScreenReceiver);
        }
    }

    private class ScreenReceiver extends BroadcastReceiver {
        private ScreenReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            DJIScreenManager.this.LOG("onReceive,action=" + action);
            if ("android.intent.action.SCREEN_ON".equals(action)) {
                DJIScreenManager.this.keepOn(true);
            } else if ("android.intent.action.SCREEN_OFF".equals(action)) {
                DJIScreenManager.this.keepOn(false);
            }
        }
    }

    /* access modifiers changed from: private */
    @Deprecated
    public void LOG(String log) {
    }
}

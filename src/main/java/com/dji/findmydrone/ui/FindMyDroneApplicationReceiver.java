package com.dji.findmydrone.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Keep;
import com.dji.component.persistence.DJIPersistenceStorage;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.model.P3.DataOsdGetPushCommon;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.ContextUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@Keep
@EXClassNullAway
public class FindMyDroneApplicationReceiver extends BroadcastReceiver {
    private static final String FMD_LATITUDE = "fmd_latitude";
    private static final String FMD_LONGITUDE = "fmd_longitude";
    private static final String FMD_UPDATE_TIME = "fmd_update_time";
    public static final String TAG = "FindMyDroneApplicationReceiver";
    public static FindMyDroneApplicationReceiver instance = null;
    private Context appContext;
    /* access modifiers changed from: private */
    public boolean isRecord = true;
    /* access modifiers changed from: private */
    public double latitude;
    /* access modifiers changed from: private */
    public double longitude;
    /* access modifiers changed from: private */
    public boolean needUpdate = false;
    private Runnable recordRunnable = new Runnable() {
        /* class com.dji.findmydrone.ui.FindMyDroneApplicationReceiver.AnonymousClass1 */

        public void run() {
            FindMyDroneApplicationReceiver.this.recordData();
        }
    };
    private Runnable updateRunnable = new Runnable() {
        /* class com.dji.findmydrone.ui.FindMyDroneApplicationReceiver.AnonymousClass2 */

        public void run() {
            boolean unused = FindMyDroneApplicationReceiver.this.needUpdate = false;
            DataOsdGetPushCommon common = DataOsdGetPushCommon.getInstance();
            if (DataOsdGetPushHome.getInstance().isGetted() && Utils.isGPSValid(common.getLatitude(), common.getLongitude())) {
                long unused2 = FindMyDroneApplicationReceiver.this.updateTime = System.currentTimeMillis();
                double unused3 = FindMyDroneApplicationReceiver.this.latitude = common.getLatitude();
                double unused4 = FindMyDroneApplicationReceiver.this.longitude = common.getLongitude();
                boolean unused5 = FindMyDroneApplicationReceiver.this.isRecord = false;
            }
        }
    };
    /* access modifiers changed from: private */
    public long updateTime;

    public static FindMyDroneApplicationReceiver get() {
        if (instance == null) {
            instance = new FindMyDroneApplicationReceiver();
            instance.initEvenBus();
        }
        return instance;
    }

    public void onReceive(Context context, Intent intent) {
        get();
    }

    private void initEvenBus() {
        EventBus.getDefault().register(this);
        this.appContext = ContextUtil.getContext();
        BackgroundLooper.postDelayed(this.recordRunnable, 1000);
    }

    /* access modifiers changed from: private */
    public void recordData() {
        if (!this.isRecord) {
            DJIPersistenceStorage.putString(FMD_LATITUDE, "" + this.latitude);
            DJIPersistenceStorage.putString(FMD_LONGITUDE, "" + this.longitude);
            DJIPersistenceStorage.putLong(FMD_UPDATE_TIME, this.updateTime);
            this.isRecord = true;
            DJILog.d(TAG, "recordData", new Object[0]);
        }
        BackgroundLooper.postDelayed(this.recordRunnable, 1000);
    }

    public double getLatitude() {
        try {
            return Double.valueOf(DJIPersistenceStorage.getString(FMD_LATITUDE, "")).doubleValue();
        } catch (Exception e) {
            return 0.0d;
        }
    }

    public double getLongitude() {
        try {
            return Double.valueOf(DJIPersistenceStorage.getString(FMD_LONGITUDE, "")).doubleValue();
        } catch (Exception e) {
            return 0.0d;
        }
    }

    public long getUpdateTime() {
        return DJIPersistenceStorage.getLong(FMD_UPDATE_TIME, 0);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEvent3BackgroundThread(DataOsdGetPushCommon common) {
        if (!this.needUpdate) {
            this.needUpdate = true;
            BackgroundLooper.postDelayed(this.updateRunnable, 500);
        }
    }
}

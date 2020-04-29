package dji.midware.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetDeviceStatus;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.media.DJIVideoDecoder;

@EXClassNullAway
public class RepeatDataBase implements DJIDataCallBack, Runnable {
    public final String TAG = "RepeatDataBase";
    private DJIDataCallBack callback;
    private int count;
    private DJIDataSyncListener listener;
    private int repeatDelayTime;
    private int repeatTime;

    private void demo1() {
        new RepeatDataBase(new DataCommonGetDeviceStatus().setReceiveType(DeviceType.BATTERY), 3, DJIVideoDecoder.connectLosedelay, new DJIDataCallBack() {
            /* class dji.midware.util.RepeatDataBase.AnonymousClass1 */

            public void onSuccess(Object model) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
    }

    private void demo2() {
        new RepeatDataBase(new DataCommonGetDeviceStatus().setReceiveType(DeviceType.BATTERY), new DJIDataCallBack() {
            /* class dji.midware.util.RepeatDataBase.AnonymousClass2 */

            public void onSuccess(Object model) {
            }

            public void onFailure(Ccode ccode) {
            }
        });
    }

    public RepeatDataBase(DJIDataSyncListener listener2, DJIDataCallBack callback2) {
        this.listener = listener2;
        this.repeatTime = 3;
        this.repeatDelayTime = 1000;
        this.callback = callback2;
        this.count = 0;
    }

    public RepeatDataBase(DJIDataSyncListener listener2, int repeatTime2, DJIDataCallBack callback2) {
        this.listener = listener2;
        this.repeatTime = repeatTime2;
        this.repeatDelayTime = 1000;
        this.callback = callback2;
        this.count = 0;
    }

    public RepeatDataBase(DJIDataSyncListener listener2, int repeatTime2, int repeatDelayTime2, DJIDataCallBack callBack) {
        this.listener = listener2;
        this.repeatTime = repeatTime2;
        this.repeatDelayTime = repeatDelayTime2;
        this.callback = callBack;
        this.count = 0;
    }

    public void start() {
        this.listener.start(this);
    }

    public void onSuccess(Object model) {
        if (this.callback != null) {
            this.callback.onSuccess(model);
        }
    }

    public void onFailure(Ccode ccode) {
        this.count++;
        if (this.count < this.repeatTime) {
            BackgroundLooper.postDelayed(this, (long) this.repeatDelayTime);
            DJILogHelper.getInstance().LOGI("UpgradeLog", "retry time: " + System.currentTimeMillis(), "UpgradeLog");
        } else if (this.callback != null) {
            this.callback.onFailure(ccode);
        }
    }

    public void run() {
        start();
    }
}

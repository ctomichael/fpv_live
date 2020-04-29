package dji.midware.data.model.extension;

import android.os.Handler;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.manager.P3.ServiceManager;
import dji.midware.data.model.P3.DataOsdGetPushConfig;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.usb.P3.LB2VideoController;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DataOsdConfigEx implements Runnable, DJIDataCallBack {
    private static DataOsdConfigEx dataOsdConfigEx;
    public final String TAG = "DataOsdConfigEx";
    private int bandwidth = -1;
    private DataOsdGetPushConfig dataOsdGetPushConfig = DataOsdGetPushConfig.getInstance();
    private Handler handler = new Handler(BackgroundLooper.getLooper());

    public static DataOsdConfigEx get() {
        if (dataOsdConfigEx == null) {
            dataOsdConfigEx = new DataOsdConfigEx();
        }
        return dataOsdConfigEx;
    }

    private DataOsdConfigEx() {
        DJIEventBusUtil.register(this);
        refresh();
    }

    public void refresh() {
        if (ServiceManager.getInstance().isConnected()) {
            this.handler.removeCallbacks(this);
            this.handler.post(this);
            return;
        }
        this.handler.removeCallbacks(this);
    }

    public DataOsdGetPushConfig getDataOsdGetPushConfig() {
        return this.dataOsdGetPushConfig;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        refresh();
        this.handler.postDelayed(new Runnable() {
            /* class dji.midware.data.model.extension.DataOsdConfigEx.AnonymousClass1 */

            public void run() {
                DataOsdConfigEx.this.refresh();
            }
        }, 2000);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushConfig event) {
        if (this.bandwidth != this.dataOsdGetPushConfig.getBandWidthPercent()) {
            this.bandwidth = this.dataOsdGetPushConfig.getBandWidthPercent();
            EventBus.getDefault().post(this);
        }
    }

    public void run() {
        if (LB2VideoController.getInstance().getEncodeMode() == LB2VideoController.EncodeMode.SINGLE) {
            this.dataOsdGetPushConfig.start(this);
            DJILog.d("dataOsdGetPushConfig", "run", new Object[0]);
            this.handler.postDelayed(this, 1000);
        }
    }

    public void onSuccess(Object model) {
        if (this.bandwidth != this.dataOsdGetPushConfig.getBandWidthPercent()) {
            this.bandwidth = this.dataOsdGetPushConfig.getBandWidthPercent();
            EventBus.getDefault().post(this);
        }
    }

    public void onFailure(Ccode ccode) {
    }
}

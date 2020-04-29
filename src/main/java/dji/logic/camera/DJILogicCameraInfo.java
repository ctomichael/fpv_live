package dji.logic.camera;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.P3.DataCameraGetPushStateInfo;
import dji.midware.data.model.P3.DataCameraSetDate;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.natives.FPVController;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJILogicCameraInfo {
    private static DJILogicCameraInfo mInstance = null;
    private long setDateStartTime = 0;
    private int version = 0;

    public static synchronized DJILogicCameraInfo getInstance() {
        DJILogicCameraInfo dJILogicCameraInfo;
        synchronized (DJILogicCameraInfo.class) {
            if (mInstance == null) {
                mInstance = new DJILogicCameraInfo();
            }
            if (!EventBus.getDefault().isRegistered(mInstance)) {
                mInstance.init();
            }
            dJILogicCameraInfo = mInstance;
        }
        return dJILogicCameraInfo;
    }

    private DJILogicCameraInfo() {
        setRateType();
    }

    public void init() {
        DJIEventBusUtil.register(this);
        if (DataCameraGetPushStateInfo.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCameraGetPushStateInfo.getInstance());
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
    }

    public int getVersion() {
        return this.version;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraGetPushStateInfo stateInfo) {
        int mVerstion = stateInfo.getVerstion();
        if (this.version != mVerstion) {
            this.version = mVerstion;
            setRateType();
        }
        if (!stateInfo.getTimeSyncState() && System.currentTimeMillis() >= this.setDateStartTime + 500) {
            DJILogHelper.getInstance().LOGD("DJILogicCameraInfo", "camera sync time " + DJIProductManager.getInstance().getType());
            this.setDateStartTime = System.currentTimeMillis();
            DataCameraSetDate.getInstance().start((DJIDataCallBack) null);
        }
    }

    private void setRateType() {
        if (this.version != 0) {
            FPVController.native_setIsNewRate(true);
        } else {
            FPVController.native_setIsNewRate(false);
        }
    }
}

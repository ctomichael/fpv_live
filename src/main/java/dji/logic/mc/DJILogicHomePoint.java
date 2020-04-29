package dji.logic.mc;

import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.logic.utils.DJICEAreas;
import dji.logic.utils.DJILocationUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataOsdGetPushHome;
import dji.midware.data.model.P3.DataRcSetPowerMode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJILogicHomePoint {
    private static DJILogicHomePoint mInstance = null;
    /* access modifiers changed from: private */
    public volatile int isOsdDone = 0;
    /* access modifiers changed from: private */
    public volatile int isWifiDone = 0;

    public static synchronized DJILogicHomePoint getInstance() {
        DJILogicHomePoint dJILogicHomePoint;
        synchronized (DJILogicHomePoint.class) {
            if (mInstance == null) {
                mInstance = new DJILogicHomePoint();
            }
            if (!EventBus.getDefault().isRegistered(mInstance)) {
                mInstance.init();
            }
            dJILogicHomePoint = mInstance;
        }
        return dJILogicHomePoint;
    }

    private DJILogicHomePoint() {
    }

    public void init() {
        DJIEventBusUtil.register(this);
        if (DataOsdGetPushHome.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataOsdGetPushHome.getInstance());
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        this.isWifiDone = 0;
        this.isOsdDone = 0;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        this.isWifiDone = 0;
        this.isOsdDone = 0;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdGetPushHome pushHome) {
        if ((DJIProductManager.getInstance().getType() == ProductType.litchiC || DJIProductManager.getInstance().getType() == ProductType.P34K) && pushHome.isHomeRecord() && DJILocationUtils.isAvailable(pushHome.getLatitude(), pushHome.getLongitude())) {
            final boolean isInCEArea = DJICEAreas.isInCEArea(pushHome.getLatitude(), pushHome.getLongitude());
            if (this.isWifiDone == 0) {
                this.isWifiDone = 1;
                DJICEAreas.startSetArea(isInCEArea, new DJIDataCallBack() {
                    /* class dji.logic.mc.DJILogicHomePoint.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        int unused = DJILogicHomePoint.this.isWifiDone = 2;
                        DJILogHelper.getInstance().LOGD("Hehe", "setArea isInCEArea success ", false, true);
                    }

                    public void onFailure(Ccode ccode) {
                        int unused = DJILogicHomePoint.this.isWifiDone = 0;
                        DJILogHelper.getInstance().LOGD("Hehe", "setArea isInCEArea=" + isInCEArea + " " + ccode, false, true);
                    }
                });
            }
            if (this.isOsdDone == 0) {
                this.isOsdDone = 1;
                DataRcSetPowerMode.getInstance().setMode(isInCEArea ? DataRcSetPowerMode.DJIRcPowerMode.CE : DataRcSetPowerMode.DJIRcPowerMode.FCC).start(new DJIDataCallBack() {
                    /* class dji.logic.mc.DJILogicHomePoint.AnonymousClass2 */

                    public void onSuccess(Object model) {
                        int unused = DJILogicHomePoint.this.isOsdDone = 2;
                        DJILogHelper.getInstance().LOGD("Hehe", "setArea osd isInCEArea success ", false, true);
                    }

                    public void onFailure(Ccode ccode) {
                        int unused = DJILogicHomePoint.this.isOsdDone = 0;
                    }
                });
            }
        }
    }
}

package dji.midware.usb.P3;

import android.os.Handler;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataCameraEvent;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataDm368GetParams;
import dji.midware.data.model.P3.DataDm368GetPushStatus;
import dji.midware.data.model.P3.DataDm368SetParams;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.data.model.extension.DataOsdConfigEx;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.MidwareBackgroundLooper;
import dji.midware.util.RepeatDataBase;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class LB2VideoController {
    private static final String TAG = "LB2VideoController";
    private static LB2VideoController lb2VideoController;
    private DualType curDualType;
    private SingleType curSingleType;
    private int dualBandwidth = -1;
    private EncodeMode encodeMode;
    /* access modifiers changed from: private */
    public Handler handler;
    DataDm368GetParams mDataDm386GetParams = new DataDm368GetParams();
    /* access modifiers changed from: private */
    public Runnable runnable = new Runnable() {
        /* class dji.midware.usb.P3.LB2VideoController.AnonymousClass2 */

        public void run() {
            LB2VideoController.this.updateDualBandWidth();
            LB2VideoController.this.handler.postDelayed(LB2VideoController.this.runnable, 2100);
        }
    };
    private int singleBandwidth = -1;
    private DJIDataCallBack updateDualBandWidthCallback = new DJIDataCallBack() {
        /* class dji.midware.usb.P3.LB2VideoController.AnonymousClass1 */

        public void onSuccess(Object model) {
            LB2VideoController.log("get BandwidthPercentage from drone success");
            LB2VideoController.this.updateValue();
        }

        public void onFailure(Ccode ccode) {
            LB2VideoController.log("get BandwidthPercentage from drone fail");
        }
    };

    public enum DualType {
        HDMI,
        AV,
        MIX
    }

    public enum EncodeMode {
        DEFAULT,
        SINGLE,
        DUAL,
        NONE
    }

    public enum SingleType {
        LB,
        EXT,
        MIX
    }

    public static LB2VideoController getInstance() {
        if (lb2VideoController == null) {
            lb2VideoController = new LB2VideoController();
        }
        return lb2VideoController;
    }

    private LB2VideoController() {
        init();
    }

    private void init() {
        DJIEventBusUtil.register(this);
        updateValue();
        this.handler = new Handler(MidwareBackgroundLooper.getLooper());
        this.handler.postDelayed(this.runnable, 1500);
    }

    /* access modifiers changed from: private */
    public void updateValue() {
        EncodeMode tmpMode = EncodeMode.DEFAULT;
        if (isSingleType()) {
            tmpMode = EncodeMode.SINGLE;
        }
        if (isDualType()) {
            tmpMode = EncodeMode.DUAL;
        }
        boolean update = false;
        if (tmpMode != this.encodeMode) {
            this.encodeMode = tmpMode;
            update = true;
            EventBus.getDefault().post(this.encodeMode);
            if (this.encodeMode == EncodeMode.SINGLE) {
                DataOsdConfigEx.get().refresh();
            }
        }
        if ((this.encodeMode != EncodeMode.DEFAULT && update) || this.singleBandwidth != getSingleBandWidth() || this.dualBandwidth != getDualBandWidth()) {
            DJILog.i(TAG, "encodeMode : " + this.encodeMode, new Object[0]);
            updateSingleType();
            updateDualType();
            this.singleBandwidth = getSingleBandWidth();
            this.dualBandwidth = getDualBandWidth();
        }
    }

    private boolean isSingleType() {
        ProductType type = DJIProductManager.getInstance().getType();
        if ((type == ProductType.A2 || type == ProductType.PM820 || type == ProductType.PM820PRO || type == ProductType.Grape2 || type == ProductType.N3 || type == ProductType.A3) && DataDm368GetPushStatus.getInstance().isGetted() && DataDm368GetPushStatus.getInstance().getEncodeMode() == 0) {
            return true;
        }
        return false;
    }

    private boolean isDualType() {
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.A2 || type == ProductType.PM820 || type == ProductType.PM820PRO || type == ProductType.Grape2 || type == ProductType.N3 || type == ProductType.A3) {
            if (!DataDm368GetPushStatus.getInstance().isGetted() || DataDm368GetPushStatus.getInstance().getEncodeMode() != 1) {
                return false;
            }
            return true;
        } else if (type == ProductType.Tomato || type == ProductType.Pomato || type == ProductType.Orange2 || type == ProductType.M200 || type == ProductType.Potato || type == ProductType.M210 || type == ProductType.M210RTK) {
            return true;
        } else {
            return false;
        }
    }

    private void updateSingleType() {
        SingleType type = null;
        if (this.encodeMode == EncodeMode.SINGLE) {
            SingleType type2 = SingleType.LB;
            int bandWidth = getSingleBandWidth();
            if (bandWidth == 0) {
                type = SingleType.EXT;
            } else if (bandWidth == 10) {
                type = SingleType.LB;
            } else {
                type = SingleType.MIX;
            }
        }
        if (type != this.curSingleType) {
            this.curSingleType = type;
            if (this.curSingleType != null) {
                EventBus.getDefault().post(this.curSingleType);
            }
            DJILog.i(TAG, "curSingleType : " + this.curSingleType, new Object[0]);
        }
    }

    private void updateDualType() {
        DualType type = null;
        if (this.encodeMode == EncodeMode.DUAL) {
            type = DualType.HDMI;
            ProductType productType = DJIProductManager.getInstance().getType();
            if (!(productType == ProductType.Tomato || productType == ProductType.Pomato || productType == ProductType.PomatoSDR || productType == ProductType.Potato || productType == ProductType.PomatoRTK)) {
                int bandWidth = getDualBandWidth();
                DJILog.i(TAG, "dual bandwidth : " + bandWidth, new Object[0]);
                type = bandWidth == 0 ? DualType.AV : bandWidth == 10 ? DualType.HDMI : DualType.MIX;
            }
        }
        if (type != this.curDualType) {
            this.curDualType = type;
            if (this.curDualType != null) {
                EventBus.getDefault().post(this.curDualType);
            }
            DJILog.i(TAG, "curDualType : " + this.curDualType, new Object[0]);
        }
    }

    public void setCurSingleType(SingleType type) {
        DJILog.d(TAG, "setCurSingleType : " + type, new Object[0]);
        if (this.encodeMode == EncodeMode.SINGLE && this.curSingleType != type) {
            this.curSingleType = type;
            if (this.curSingleType != null) {
                EventBus.getDefault().post(this.curSingleType);
            }
        }
    }

    public void setCurDualType(DualType type) {
        if (this.encodeMode == EncodeMode.DUAL && this.curDualType != type) {
            this.curDualType = type;
            if (this.curDualType != null) {
                EventBus.getDefault().post(this.curDualType);
            }
        }
    }

    public void updateDualBandWidth() {
        if (this.encodeMode == EncodeMode.DUAL) {
            this.mDataDm386GetParams.set(DataDm368SetParams.DM368CmdId.BandwidthPercentage).start(this.updateDualBandWidthCallback);
        }
    }

    public SingleType getCurSingleType() {
        return this.curSingleType;
    }

    public DualType getCurDualType() {
        return this.curDualType;
    }

    public EncodeMode getEncodeMode() {
        return this.encodeMode;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataDm368GetPushStatus event) {
        updateValue();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOsdConfigEx event) {
        updateValue();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        updateValue();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(ProductType event) {
        updateValue();
        updateDualBandWidth();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCameraEvent event) {
        if (event == DataCameraEvent.ConnectOK) {
            DataDm368GetParams getter = DataDm368GetParams.getInstance();
            getter.set(DataDm368SetParams.DM368CmdId.BandwidthPercentage);
            new RepeatDataBase(getter, 6, new DJIDataCallBack() {
                /* class dji.midware.usb.P3.LB2VideoController.AnonymousClass3 */

                public void onSuccess(Object model) {
                    LB2VideoController.this.updateValue();
                }

                public void onFailure(Ccode ccode) {
                }
            }).start();
            return;
        }
        updateValue();
    }

    public int getSingleBandWidth() {
        return DataOsdConfigEx.get().getDataOsdGetPushConfig().getBandWidthPercent();
    }

    public int getDualBandWidth() {
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.A3 || type == ProductType.PM820 || type == ProductType.PM820PRO || type == ProductType.M200 || type == ProductType.M210 || type == ProductType.M210RTK) {
            if (DataDm368GetPushStatus.getInstance().isGetted()) {
                return DataDm368GetPushStatus.getInstance().getDualEncodeModePercentage();
            }
            if (DataDm368GetParams.getInstance().isGetted()) {
                return DataDm368GetParams.getInstance().getBandWidthPercent();
            }
            return 10;
        } else if (DataRcGetPushParams.getInstance().isGetted()) {
            return DataRcGetPushParams.getInstance().getBandWidth();
        } else {
            if (DataDm368GetParams.getInstance().isGetted()) {
                return DataDm368GetParams.getInstance().getBandWidthPercent();
            }
            return 10;
        }
    }

    public static void log(String log) {
    }
}

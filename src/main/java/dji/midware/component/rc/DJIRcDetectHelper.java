package dji.midware.component.rc;

import android.text.TextUtils;
import android.util.Log;
import dji.component.accountcenter.IMemberProtocol;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.log.DJILogHelper;
import dji.logic.utils.DJIPublicUtils;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataEvent;
import dji.midware.data.model.P3.DataCommonGetDeviceInfo;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataRcHandShake;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.link.DJILinkDaemonService;
import dji.midware.link.DJILinkType;
import dji.midware.usb.P3.DJIUsbAccessoryReceiver;
import dji.midware.usb.P3.UsbAccessoryService;
import dji.midware.util.BytesUtil;
import dji.midware.util.DJIEventBusUtil;
import dji.midware.util.RepeatDataBase;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIRcDetectHelper {
    private static final String RC_WM230_STRING = "wm230";
    /* access modifiers changed from: private */
    public static final String TAG = DJIRcDetectHelper.class.getSimpleName();
    public static final String VERSION_KUMQUAT_SUPPORT_ISO_SHUTTER = "01.04.00.00";
    private DJIComponentManager.RcComponentType lastHardWareType;
    private String lastRcProductInfo;
    /* access modifiers changed from: private */
    public DataCommonGetDeviceInfo mDeviceInfoGetter;
    /* access modifiers changed from: private */
    public DataCommonGetVersion mKumquat1860Getter;
    /* access modifiers changed from: private */
    public DataCommonGetVersion mOsdGetter;

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DJIRcDetectHelper INSTANCE = new DJIRcDetectHelper();

        private SingletonHolder() {
        }
    }

    private DJIRcDetectHelper() {
    }

    public static final DJIRcDetectHelper getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void init() {
        DJIEventBusUtil.register(this);
        if (DJILinkDaemonService.getInstance().getLinkType() == DJILinkType.NON) {
            onEvent3BackgroundThread(DataEvent.ConnectLose);
        } else {
            onEvent3BackgroundThread(DataEvent.ConnectOK);
        }
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
    }

    public DataCommonGetVersion getKumquat1860Getter() {
        DJILog.logWriteD("DJIRcDetectHelper", getDate() + " Kumquat " + (this.mKumquat1860Getter == null), new Object[0]);
        if (this.mKumquat1860Getter != null) {
            return this.mKumquat1860Getter;
        }
        DataCommonGetVersion osdGetter = new DataCommonGetVersion();
        osdGetter.setDeviceType(DeviceType.DM368_G);
        osdGetter.setDeviceModel(1);
        return osdGetter;
    }

    public DataCommonGetVersion getOsdGetter() {
        return this.mOsdGetter;
    }

    public ProductType getProductTypeByOsd() {
        if (this.mOsdGetter == null) {
            return null;
        }
        return getRcType(this.mOsdGetter);
    }

    public ProductType getProductTypeByUsbInfo() {
        if (DJIProductManager.getInstance().getRcType() == ProductType.PomatoRTK || UsbAccessoryService.getInstance().getUsbModel() == DJIUsbAccessoryReceiver.UsbModel.AG) {
            return ProductType.PomatoRTK;
        }
        if (getRcComponentType() == DJIComponentManager.RcComponentType.P4RTK) {
            return ProductType.PomatoRTK;
        }
        return ProductType.None;
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEvent event) {
        DJILogHelper.getInstance().LOGD(TAG, "DataEvent = " + event, false, false);
        this.lastHardWareType = null;
        if (event == DataEvent.ConnectLose) {
            destroyDeviceGetter();
            destroyOsdGetter();
            destroyKumquat1860Getter();
            return;
        }
        initDeviceGetter();
        initOsdGetter();
        initKumquat1860Getter();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcHandShake handShake) {
        if (this.lastRcProductInfo == null || !this.lastRcProductInfo.equals(handShake.getRCInfo())) {
            this.lastRcProductInfo = handShake.getRCInfo();
            this.lastHardWareType = null;
            EventBus.getDefault().post(this);
        }
    }

    private void initOsdGetter() {
        if (this.mOsdGetter == null) {
            final DataCommonGetVersion osdGetter = new DataCommonGetVersion();
            osdGetter.setClearCameraLose(false);
            osdGetter.setDeviceType(DeviceType.OSD);
            new RepeatDataBase(osdGetter, 6, 500, new DJIDataCallBack() {
                /* class dji.midware.component.rc.DJIRcDetectHelper.AnonymousClass1 */

                public void onSuccess(Object model) {
                    DataCommonGetVersion unused = DJIRcDetectHelper.this.mOsdGetter = osdGetter;
                    EventBus.getDefault().post(DJIRcDetectHelper.this);
                }

                public void onFailure(Ccode ccode) {
                    DJILog.d(DJIRcDetectHelper.TAG, "osdGetter fails : " + ccode, new Object[0]);
                }
            }).start();
        }
    }

    private void initDeviceGetter() {
        if (this.mDeviceInfoGetter == null) {
            final DataCommonGetDeviceInfo deviceInfoGetter = new DataCommonGetDeviceInfo();
            deviceInfoGetter.setReceiveType(DeviceType.DM368_G);
            deviceInfoGetter.setReceiveId(1);
            deviceInfoGetter.start(new DJIDataCallBack() {
                /* class dji.midware.component.rc.DJIRcDetectHelper.AnonymousClass2 */

                public void onSuccess(Object model) {
                    DataCommonGetDeviceInfo unused = DJIRcDetectHelper.this.mDeviceInfoGetter = deviceInfoGetter;
                    DJILog.logWriteD("DJIRcDetectHelper", "initDeviceGetter()" + DJIRcDetectHelper.this.mDeviceInfoGetter.getInfo(), new Object[0]);
                    EventBus.getDefault().post(DJIRcDetectHelper.this);
                }

                public void onFailure(Ccode ccode) {
                }
            });
        }
    }

    private void destroyDeviceGetter() {
        if (this.mDeviceInfoGetter != null) {
            this.mDeviceInfoGetter = null;
            EventBus.getDefault().post(this);
        }
    }

    private void destroyOsdGetter() {
        if (this.mOsdGetter != null) {
            this.mOsdGetter = null;
            EventBus.getDefault().post(this);
        }
    }

    private void initKumquat1860Getter() {
        if (this.mKumquat1860Getter == null) {
            final DataCommonGetVersion osdGetter = new DataCommonGetVersion();
            osdGetter.setDeviceType(DeviceType.DM368_G);
            osdGetter.setDeviceModel(1);
            new RepeatDataBase(osdGetter, 6, 500, new DJIDataCallBack() {
                /* class dji.midware.component.rc.DJIRcDetectHelper.AnonymousClass3 */

                public void onSuccess(Object model) {
                    DataCommonGetVersion unused = DJIRcDetectHelper.this.mKumquat1860Getter = osdGetter;
                    DJILog.logWriteD("DJIRcDetectHelper", DJIRcDetectHelper.getDate() + " GetKumquat ", new Object[0]);
                    EventBus.getDefault().post(DJIRcDetectHelper.this);
                }

                public void onFailure(Ccode ccode) {
                    DJILog.d(DJIRcDetectHelper.TAG, "osdGetter fails : " + ccode, new Object[0]);
                }
            }).start();
        }
    }

    private void destroyKumquat1860Getter() {
        if (this.mKumquat1860Getter != null) {
            this.mKumquat1860Getter = null;
            EventBus.getDefault().post(this);
        }
    }

    public DJIComponentManager.RcComponentType getRcComponentType() {
        RcHardWareType rcHardWareType;
        RcHardWareType rcHWType;
        RcHardWareType handshakeInfo;
        if (this.lastHardWareType != null) {
            return this.lastHardWareType;
        }
        if (this.lastRcProductInfo != null && !TextUtils.isEmpty(this.lastRcProductInfo) && (handshakeInfo = RcHardWareType.getByDeviceString(this.lastRcProductInfo)) != null && ProductType.isValidType(handshakeInfo.productType)) {
            DJILog.d(TAG, "" + handshakeInfo.hwName + ", " + handshakeInfo.rcComponentType, new Object[0]);
            if (handshakeInfo.rcComponentType != null) {
                this.lastHardWareType = handshakeInfo.rcComponentType;
                return handshakeInfo.rcComponentType;
            }
        }
        if (this.mDeviceInfoGetter != null && !TextUtils.isEmpty(this.mDeviceInfoGetter.getInfo()) && (rcHWType = RcHardWareType.getByDeviceString(this.mDeviceInfoGetter.getInfo())) != null && ProductType.isValidType(rcHWType.productType)) {
            Log.d(TAG, "" + rcHWType.hwName + ", " + rcHWType.rcComponentType);
            if (rcHWType.rcComponentType != null) {
                this.lastHardWareType = rcHWType.rcComponentType;
                return rcHWType.rcComponentType;
            }
        }
        if (this.mOsdGetter == null || this.mOsdGetter.getRecData() == null || (rcHardWareType = RcHardWareType.getByOsdData(this.mOsdGetter.getRecData())) == null || !ProductType.isValidType(rcHardWareType.productType)) {
            return null;
        }
        DJILog.d(TAG, "" + rcHardWareType.hwName + ", " + rcHardWareType.rcComponentType, new Object[0]);
        this.lastHardWareType = rcHardWareType.rcComponentType;
        return rcHardWareType.rcComponentType;
    }

    public static ProductType getRcType(DataCommonGetVersion osdGetter) {
        if (osdGetter == null || osdGetter.getRecData() == null) {
            return null;
        }
        int version1 = ((Integer) osdGetter.get(20, 1, Integer.class)).intValue();
        int version2 = ((Integer) osdGetter.get(19, 1, Integer.class)).intValue();
        int version3 = ((Integer) osdGetter.get(18, 1, Integer.class)).intValue();
        RcHardWareType rcHardWareType = RcHardWareType.getByOsdData(osdGetter.getRecData());
        if (rcHardWareType != null && ProductType.isValidType(rcHardWareType.productType)) {
            return rcHardWareType.productType;
        }
        DataCommonGetVersion kumquat1860Getter = getInstance().getKumquat1860Getter();
        if (hasOldVersionSDRModule(osdGetter)) {
            return ProductType.KumquatX;
        }
        DJILogHelper.getInstance().LOGD(TAG, "1765 Loader[" + BytesUtil.byte2hex(osdGetter.getRecData()) + IMemberProtocol.STRING_SEPERATOR_RIGHT, false, true);
        if (version2 == 0 && 1 == version3) {
            return ProductType.Grape2;
        }
        if (1 == version2 && 50397447 < DJIPublicUtils.formatVersion(osdGetter.getLoader("."))) {
            return ProductType.Pomato;
        }
        if (10 == version1) {
            return ProductType.Mammoth;
        }
        if ("wm230".equals(osdGetter.getHardwareVer())) {
            return ProductType.WM230;
        }
        int type = ((Integer) osdGetter.get(24, 1, Integer.class)).intValue();
        DJILogHelper.getInstance().LOGD(TAG, "1765 firmware type=" + type, true, true);
        if (type >= 4) {
            int version = ((Integer) osdGetter.get(20, 1, Integer.class)).intValue() % 9;
            DJILogHelper.getInstance().LOGD(TAG, "1765 loader version=" + version, true, true);
            if (version == 1) {
                return ProductType.Orange;
            }
            if (version == 2) {
                return ProductType.litchiX;
            }
            if (version == 3) {
                return ProductType.litchiX;
            }
            if (version == 4) {
                return ProductType.Orange;
            }
            if (version == 0) {
                if (DJILinkDaemonService.getInstance().getLinkType().equals(DJILinkType.WIFI)) {
                    return ProductType.litchiC;
                }
                return null;
            } else if (version == 5) {
                return ProductType.P34K;
            } else {
                return null;
            }
        } else {
            DataCommonGetVersion dm368Getter = new DM368GBlockRequest().getDM368();
            if (dm368Getter == null) {
                return ProductType.litchiX;
            }
            int dm368Type = ((Integer) dm368Getter.get(24, 1, Integer.class)).intValue();
            if (dm368Type == 1) {
                return ProductType.Orange;
            }
            if (dm368Type == 2) {
                return ProductType.litchiX;
            }
            return ProductType.Orange;
        }
    }

    public boolean hasGotRcTypeInfo() {
        return (this.mOsdGetter == null && this.mDeviceInfoGetter == null) ? false : true;
    }

    public RcHardWareType getRcHardWareType() {
        if (this.mOsdGetter != null) {
            return RcHardWareType.getByOsdData(this.mOsdGetter.getRecData());
        }
        return null;
    }

    private static boolean hasOldVersionSDRModule(DataCommonGetVersion osdGetter) {
        if (osdGetter == null) {
            return false;
        }
        return ((Integer) osdGetter.get(19, 1, Integer.class)).intValue() == 6;
    }

    /* access modifiers changed from: private */
    public static String getDate() {
        new StringBuilder("");
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());
    }
}

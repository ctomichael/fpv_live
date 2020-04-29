package dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk;

import android.os.Handler;
import android.os.Message;
import dji.common.error.DJIError;
import dji.common.error.DJIFlightControllerError;
import dji.common.flightcontroller.LocationCoordinate3D;
import dji.common.flightcontroller.PositioningSolution;
import dji.common.flightcontroller.RTKState;
import dji.common.flightcontroller.ReceiverInfo;
import dji.common.flightcontroller.rtk.LocationStandardDeviation;
import dji.common.model.LocationCoordinate2D;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCenterRTKPushStatus;
import dji.midware.data.model.P3.DataFlycGetPushRTKLocationData;
import dji.midware.data.model.P3.DataFlycSetRTKState;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.RTKKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class RTKAbstraction extends DJISubComponentHWAbstraction {
    private static final int MSG_COUNTDOWN = 1;
    private static final int RTK_HEART_BEAT_INTERVAL = 1;
    private static final int RTK_LOCATION_UPDATE_TIMEOUT = 2;
    private static final int RTK_UPDATE_TIMEOUT = 3;
    private Handler.Callback callback = new Handler.Callback() {
        /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKAbstraction.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            if (msg.what == 1) {
                if (RTKAbstraction.this.rtkHeartBeatTimeOut < 0) {
                    boolean unused = RTKAbstraction.this.isRTKConnected = false;
                    RTKAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(RTKAbstraction.this.isRTKConnected), RTKAbstraction.this.convertKeyToPath(RTKKeys.IS_RTK_CONNECTED));
                } else {
                    RTKAbstraction.access$010(RTKAbstraction.this);
                    RTKAbstraction.this.handler.sendEmptyMessageDelayed(1, 1000);
                }
            }
            return false;
        }
    };
    /* access modifiers changed from: private */
    public Handler handler = new Handler(BackgroundLooper.getLooper(), this.callback);
    /* access modifiers changed from: private */
    public boolean isRTKConnected;
    /* access modifiers changed from: private */
    public volatile int rtkHeartBeatTimeOut = 3;
    private long rtkLastLocationUpdateTime = 0;

    static /* synthetic */ int access$010(RTKAbstraction x0) {
        int i = x0.rtkHeartBeatTimeOut;
        x0.rtkHeartBeatTimeOut = i - 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(RTKKeys.class, getClass());
    }

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }

    public void syncPushDataFromMidware() {
        if (DataCenterRTKPushStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataCenterRTKPushStatus.getInstance());
        }
        if (DataFlycGetPushRTKLocationData.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushRTKLocationData.getInstance());
        }
    }

    @Setter(RTKKeys.RTK_ENABLED)
    public void setRTKEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        DataFlycSetRTKState.getInstance().setIsOpen(enabled).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.rtk.RTKAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                if (callback2 != null) {
                    callback2.onSuccess(model);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback2 != null) {
                    callback2.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataCenterRTKPushStatus status) {
        notifyRTKStatusChanged(status);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushRTKLocationData event) {
        if (event.isGetted()) {
            this.rtkLastLocationUpdateTime = System.currentTimeMillis();
            notifyValueChangeForKeyPath(new LocationCoordinate3D(event.getLatitude(), event.getLongitude(), event.getHeight()), convertKeyToPath(RTKKeys.RTK_FUSION_MOBILE_LOCATION));
            notifyValueChangeForKeyPath(Float.valueOf(((float) event.getHeading()) * 0.1f), convertKeyToPath(RTKKeys.RTK_FUSION_MOBILE_HEADING));
            notifyValueChangeForKeyPath(Boolean.valueOf(event.isRTKHealthy()), convertKeyToPath(RTKKeys.IS_RTK_FUSION_DATA_USABLE));
        }
    }

    /* access modifiers changed from: protected */
    public void notifyRTKStatusChanged(DataCenterRTKPushStatus status) {
        if (!this.isRTKConnected) {
            this.isRTKConnected = true;
            notifyValueChangeForKeyPath(Boolean.valueOf(this.isRTKConnected), convertKeyToPath(RTKKeys.IS_RTK_CONNECTED));
            if (this.handler.hasMessages(1)) {
                this.handler.removeMessages(1);
            }
            this.handler.sendEmptyMessageDelayed(1, 1000);
        }
        this.rtkHeartBeatTimeOut = 3;
        if (this.callback != null) {
            boolean isRTKBeingUsed = false;
            if (Math.abs(this.rtkLastLocationUpdateTime - System.currentTimeMillis()) < 2000) {
                isRTKBeingUsed = DataFlycGetPushRTKLocationData.getInstance().isRTKHealthy();
            }
            notifyValueChangeForKeyPath(buildRTKPushInfo().isRTKBeingUsed(isRTKBeingUsed).build(), convertKeyToPath(RTKKeys.RTK_STATE));
            notifyValueChangeForKeyPath(Boolean.valueOf(isRTKBeingUsed), convertKeyToPath(RTKKeys.IS_RTK_FUSION_DATA_USABLE));
        }
    }

    /* access modifiers changed from: protected */
    public RTKState.Builder buildRTKPushInfo() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11;
        boolean z12 = true;
        DataFlycGetPushRTKLocationData pushInfo = DataFlycGetPushRTKLocationData.getInstance();
        DataCenterRTKPushStatus status = DataCenterRTKPushStatus.getInstance();
        RTKState.Builder positioningSolution = new RTKState.Builder().error(getRtkErrorFromErrorCode(status.getErrorCode())).positioningSolution(getPositioningSolution(status.getStatus()));
        if ((status.getAntannaMGPSNum() & 1) == 1) {
            z = true;
        } else {
            z = false;
        }
        RTKState.Builder msReceiver1GPSInfo = positioningSolution.msReceiver1GPSInfo(ReceiverInfo.createInstance(z, status.getAntannaMGPSNum() >>> 1));
        if ((status.getAntannaSGPSNum() & 1) == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        RTKState.Builder msReceiver2GPSInfo = msReceiver1GPSInfo.msReceiver2GPSInfo(ReceiverInfo.createInstance(z2, status.getAntannaSGPSNum() >>> 1));
        if ((status.getBaseStationGPSNum() & 1) == 1) {
            z3 = true;
        } else {
            z3 = false;
        }
        RTKState.Builder bsReceiverGPSInfo = msReceiver2GPSInfo.bsReceiverGPSInfo(ReceiverInfo.createInstance(z3, status.getBaseStationGPSNum() >>> 1));
        if ((status.getAntannaMBeidouNum() & 1) == 1) {
            z4 = true;
        } else {
            z4 = false;
        }
        RTKState.Builder msReceiver1BeiDouInfo = bsReceiverGPSInfo.msReceiver1BeiDouInfo(ReceiverInfo.createInstance(z4, status.getAntannaMBeidouNum() >>> 1));
        if ((status.getAntannaSBeidouNum() & 1) == 1) {
            z5 = true;
        } else {
            z5 = false;
        }
        RTKState.Builder msReceiver2BeiDouInfo = msReceiver1BeiDouInfo.msReceiver2BeiDouInfo(ReceiverInfo.createInstance(z5, status.getAntannaSBeidouNum() >>> 1));
        if ((status.getBaseStationBeidouNum() & 1) == 1) {
            z6 = true;
        } else {
            z6 = false;
        }
        RTKState.Builder bsReceiverBeiDouInfo = msReceiver2BeiDouInfo.bsReceiverBeiDouInfo(ReceiverInfo.createInstance(z6, status.getBaseStationBeidouNum() >>> 1));
        if ((status.getAntannaMGlonassNum() & 1) == 1) {
            z7 = true;
        } else {
            z7 = false;
        }
        RTKState.Builder msReceiver1GLONASSInfo = bsReceiverBeiDouInfo.msReceiver1GLONASSInfo(ReceiverInfo.createInstance(z7, status.getAntannaMGlonassNum() >>> 1));
        if ((status.getAntannaSGlonassNum() & 1) == 1) {
            z8 = true;
        } else {
            z8 = false;
        }
        RTKState.Builder msReceiver2GLONASSInfo = msReceiver1GLONASSInfo.msReceiver2GLONASSInfo(ReceiverInfo.createInstance(z8, status.getAntannaSGlonassNum() >>> 1));
        if ((status.getBaseStationGlonassNum() & 1) == 1) {
            z9 = true;
        } else {
            z9 = false;
        }
        RTKState.Builder bsReceiverGLONASSInfo = msReceiver2GLONASSInfo.bsReceiverGLONASSInfo(ReceiverInfo.createInstance(z9, status.getBaseStationGlonassNum() >>> 1));
        if ((status.getAntannaMGalileoNum() & 1) == 1) {
            z10 = true;
        } else {
            z10 = false;
        }
        RTKState.Builder msReceiver1GalileoInfo = bsReceiverGLONASSInfo.msReceiver1GalileoInfo(ReceiverInfo.createInstance(z10, status.getAntannaMGalileoNum() >>> 1));
        if ((status.getAntannaSGalileoNum() & 1) == 1) {
            z11 = true;
        } else {
            z11 = false;
        }
        RTKState.Builder msReceiver2GalileoInfo = msReceiver1GalileoInfo.msReceiver2GalileoInfo(ReceiverInfo.createInstance(z11, status.getAntannaSGalileoNum() >>> 1));
        if ((status.getBaseStationGalileoNum() & 1) != 1) {
            z12 = false;
        }
        return msReceiver2GalileoInfo.bsReceiverGalileoInfo(ReceiverInfo.createInstance(z12, status.getBaseStationGalileoNum() >>> 1)).msAntenna1Location(new LocationCoordinate2D(status.getSkyLat(), status.getSkyLng())).msAntenna1Altitude(status.getSkyHeight()).bsLocation(new LocationCoordinate2D(status.getGroundLat(), status.getGroundLng())).bsAltitude(status.getGroundHeight()).heading(status.getLocateAngle()).isHeadingValid(status.isDirectionEnabled()).locating(status.getLocateEnable()).msFusionLocation(new LocationCoordinate2D(pushInfo.getLatitude(), pushInfo.getLongitude())).msFusionAltitude(pushInfo.getHeight()).msFusionHeading(((float) pushInfo.getHeading()) * 0.1f).mobileStationStandardDeviation(new LocationStandardDeviation(status.getStdLat(), status.getStdLon(), status.getStdAlt()));
    }

    /* access modifiers changed from: protected */
    public PositioningSolution getPositioningSolution(int value) {
        return PositioningSolution.find(value);
    }

    /* access modifiers changed from: protected */
    public DJIError getRtkErrorFromErrorCode(int errorCode) {
        switch (errorCode) {
            case 0:
                return null;
            case 1:
                return DJIFlightControllerError.RTK_CANNOT_START;
            case 2:
                return DJIFlightControllerError.RTK_CONNECTION_BROKEN;
            case 3:
                return DJIFlightControllerError.RTK_BS_ANTENNA_ERROR;
            case 4:
                return DJIFlightControllerError.RTK_BS_COORDINATE_RESET;
            case 5:
                return DJIFlightControllerError.RTK_INTIALIZING;
            case 6:
                return DJIFlightControllerError.BASE_STATION_NOT_ACTIVATED;
            case 7:
                return DJIFlightControllerError.RTK_RTCM_TYPE_CHANGE;
            case 8:
                return DJIFlightControllerError.BASE_STATION_IS_MOVED;
            case 9:
                return DJIFlightControllerError.BASE_STATION_FELL;
            default:
                return DJIError.COMMON_UNKNOWN;
        }
    }

    @Getter(RTKKeys.RTK_ENABLED)
    public void getRTKEnabled(DJISDKCacheHWAbstraction.InnerCallback callback2) {
        CallbackUtils.onSuccess(callback2, Boolean.valueOf(DataCenterRTKPushStatus.getInstance().isRtkEnabled()));
    }
}

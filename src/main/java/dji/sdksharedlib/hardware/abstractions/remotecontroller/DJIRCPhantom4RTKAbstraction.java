package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import android.os.Handler;
import android.os.Message;
import dji.common.error.DJIRemoteControllerError;
import dji.common.remotecontroller.MultiDeviceAggregationState;
import dji.common.remotecontroller.MultiDeviceState;
import dji.common.remotecontroller.PairingDevice;
import dji.common.util.CallbackUtils;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.P3.DataCommonGetVersion;
import dji.midware.data.model.P3.DataCommonSetDate;
import dji.midware.data.model.P3.DataGetPushMultiRcPairingStatus;
import dji.midware.data.model.P3.DataRcEnableBaseStationRTK;
import dji.midware.data.model.P3.DataRcMultiPairing;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BackgroundLooper;
import dji.midware.util.BytesUtil;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.listener.DJIParamAccessListener;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class DJIRCPhantom4RTKAbstraction extends DJIRCPhantom3Abstraction implements DJIParamAccessListener {
    private static final int RETRY_TIMES = 3;
    private static final int TRY_SET_DEVICE_TIME = 1;
    private Handler.Callback callback = new Handler.Callback() {
        /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4RTKAbstraction.AnonymousClass1 */

        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    DataCommonSetDate setter = new DataCommonSetDate();
                    setter.setRecvType(DeviceType.DM368_G).setRecevicerId(1);
                    setter.start(new DJIDataCallBack() {
                        /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4RTKAbstraction.AnonymousClass1.C00211 */

                        public void onSuccess(Object model) {
                            int unused = DJIRCPhantom4RTKAbstraction.this.setDeviceTimeRemainingRetryTimes = 3;
                            if (DJIRCPhantom4RTKAbstraction.this.handler.hasMessages(1)) {
                                DJIRCPhantom4RTKAbstraction.this.handler.removeMessages(1);
                            }
                        }

                        public void onFailure(Ccode ccode) {
                            if (DJIRCPhantom4RTKAbstraction.this.setDeviceTimeRemainingRetryTimes > 0) {
                                DJIRCPhantom4RTKAbstraction.access$010(DJIRCPhantom4RTKAbstraction.this);
                                if (DJIRCPhantom4RTKAbstraction.this.handler.hasMessages(1)) {
                                    DJIRCPhantom4RTKAbstraction.this.handler.removeMessages(1);
                                }
                                DJIRCPhantom4RTKAbstraction.this.handler.sendEmptyMessageDelayed(1, 2000);
                            }
                        }
                    });
                    return false;
                default:
                    return false;
            }
        }
    };
    /* access modifiers changed from: private */
    public Handler handler = new Handler(BackgroundLooper.getLooper(), this.callback);
    /* access modifiers changed from: private */
    public int setDeviceTimeRemainingRetryTimes = 3;

    static /* synthetic */ int access$010(DJIRCPhantom4RTKAbstraction x0) {
        int i = x0.setDeviceTimeRemainingRetryTimes;
        x0.setDeviceTimeRemainingRetryTimes = i - 1;
        return i;
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
        this.handler.sendEmptyMessage(1);
    }

    public void syncPushDataFromMidware() {
        onEvent3BackgroundThread(DataGetPushMultiRcPairingStatus.getInstance());
    }

    public void destroy() {
        CacheHelper.removeListener(this);
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return "Phantom 4 RTK Controller";
    }

    /* access modifiers changed from: protected */
    public boolean isMultiDevicePairingSupported() {
        return true;
    }

    @Setter(RemoteControllerKeys.RTK_CHANNEL_ENABLE)
    public void setRTKChannelEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        new DataRcEnableBaseStationRTK().enableBaseStationRTK(enabled).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4RTKAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    @Action(RemoteControllerKeys.START_MULTI_DEVICE_PAIRING)
    public void enterPairingMode(final DJISDKCacheHWAbstraction.InnerCallback callback2, final PairingDevice target) {
        getPairingModel(DataRcMultiPairing.PairAction.ENTER_PAIRING, target).eraseAllPairingInformation().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4RTKAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                final DataRcMultiPairing setter = DJIRCPhantom4RTKAbstraction.this.getPairingModel(DataRcMultiPairing.PairAction.ENTER_PAIRING, target).setNumber(0);
                setter.start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4RTKAbstraction.AnonymousClass3.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        if (setter.getPairState() == DataRcMultiPairing.PairState.PAIRING) {
                            CallbackUtils.onSuccess(callback2, (Object) null);
                        } else {
                            CallbackUtils.onFailure(callback2, DJIRemoteControllerError.COMMON_EXECUTION_FAILED);
                        }
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback2, DJIRemoteControllerError.getDJIError(ccode));
                    }
                });
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    @Action(RemoteControllerKeys.STOP_MULTI_DEVICE_PAIRING)
    public void stopMultiDevicePairing(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        getPairingModel(DataRcMultiPairing.PairAction.EXIT_PAIRING, PairingDevice.AIRCRAFT).setNumber(0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4RTKAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback2, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    /* access modifiers changed from: private */
    public DataRcMultiPairing getPairingModel(DataRcMultiPairing.PairAction action, PairingDevice target) {
        return new DataRcMultiPairing().setAction(action).setMode(DataRcMultiPairing.PairMode.AGRICULTURE).setTarget(DataRcMultiPairing.PairTarget.find(target.value()));
    }

    public void getSerialNumber(final DJISDKCacheHWAbstraction.InnerCallback callback2) {
        final DataCommonGetVersion getter = new DataCommonGetVersion();
        getter.setDeviceType(DeviceType.DM368_G);
        getter.setDeviceModel(1);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCPhantom4RTKAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                String hwStr = BytesUtil.getStringUTF8Offset(getter.getRecData(), 1, 16);
                if (!hwStr.contains("WM334")) {
                    CallbackUtils.onSuccess(callback2, hwStr);
                } else {
                    CallbackUtils.onFailure(callback2, DJIRemoteControllerError.COMMON_UNDEFINED);
                }
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback2, DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataGetPushMultiRcPairingStatus status) {
        if (status.isGetted()) {
            MultiDeviceState aircraftMultiDeviceState = MultiDeviceState.find(status.getAircraftPairingStatus().value());
            MultiDeviceState baseStationSDRMultiDeviceState = mapMultiDeviceStateForGS(status.getBaseStationSDRPairingState().value());
            notifyValueChangeForKeyPath(new MultiDeviceAggregationState.Builder().aircraftMultiDeviceState(aircraftMultiDeviceState).baseStationSDRMultiDeviceState(baseStationSDRMultiDeviceState).build(), RemoteControllerKeys.MULTI_DEVICES_PAIRING_STATE);
            notifyValueChangeForKeyPath(Boolean.valueOf(baseStationSDRMultiDeviceState == MultiDeviceState.CONNECTED), RemoteControllerKeys.RTK_CHANNEL_ENABLE);
        }
    }

    private MultiDeviceState mapMultiDeviceStateForGS(int status) {
        if (status == 1) {
            return MultiDeviceState.DISCONNECTED;
        }
        if (status == 2) {
            return MultiDeviceState.CONNECTED;
        }
        return MultiDeviceState.find(status);
    }
}

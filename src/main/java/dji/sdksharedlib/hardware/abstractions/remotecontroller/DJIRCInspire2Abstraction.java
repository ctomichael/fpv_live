package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import android.text.TextUtils;
import dji.common.error.DJIError;
import dji.common.error.DJINarrowBandModuleError;
import dji.common.error.DJIRemoteControllerError;
import dji.common.remotecontroller.AuthorizationInfo;
import dji.common.remotecontroller.ChargeMobileMode;
import dji.common.remotecontroller.MasterSlaveState;
import dji.common.remotecontroller.RCMode;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.narrowband.NarrowBandExchangeEvent;
import dji.internal.narrowband.NarrowBandSlaveMode;
import dji.internal.narrowband.SlaveChannelState;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataDm368GetGParams;
import dji.midware.data.model.P3.DataNarrowBandBaseInfoConfig;
import dji.midware.data.model.P3.DataNarrowBandExchangeMode;
import dji.midware.data.model.P3.DataNarrowBandGetPushDeviceList;
import dji.midware.data.model.P3.DataNarrowBandGetPushStateInfo;
import dji.midware.data.model.P3.DataOnBoardSetMappedGimbal;
import dji.midware.data.model.P3.DataOnboardGetPushMixInfo;
import dji.midware.data.model.P3.DataRcGetPushParams;
import dji.midware.data.model.P3.DataRcMasterSlaveId;
import dji.midware.data.model.P3.DataRcSetAppSpecialControl;
import dji.midware.data.model.P3.DataWifiConnectMasterWithIdAuthCode;
import dji.midware.data.model.P3.DataWifiGetAuthCode;
import dji.midware.data.model.P3.DataWifiGetPushMasterSlaveStatus;
import dji.midware.data.model.P3.DataWifiScanMasterList;
import dji.midware.data.model.P3.DataWifiSetMasterSlaveAuthCode;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.RemoteControllerKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;
import java.util.List;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class DJIRCInspire2Abstraction extends DJIRCAbstraction {
    public DJIRCInspire2Abstraction() {
        this.supportMasterSlaveModeV2 = true;
        this.remoteFocusCheckingSupported = true;
        this.hardwareState.getC1Button().setPresent(true);
        this.hardwareState.getC2Button().setPresent(true);
        this.hardwareState.getGoHomeButton().setPresent(true);
        this.hardwareState.getPauseButton().setPresent(true);
        this.hardwareState.getRecordButton().setPresent(true);
        this.hardwareState.getRightWheel().setPresent(true);
        this.hardwareState.getShutterButton().setPresent(true);
        this.hardwareState.getTransformationSwitch().setPresent(true);
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNameInspire2;
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [int, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        notifyValueChangeForKeyPath((Object) 0, convertKeyToPath(RemoteControllerKeys.CONTROLLING_GIMBAL_INDEX));
        onEvent3BackgroundThread(DataOnboardGetPushMixInfo.getInstance());
        onEvent3BackgroundThread(DataNarrowBandBaseInfoConfig.getInstance());
        onEvent3BackgroundThread(DataNarrowBandGetPushStateInfo.getInstance());
        onEvent3BackgroundThread(DataNarrowBandGetPushDeviceList.getInstance());
        onEvent3BackgroundThread(DataNarrowBandExchangeMode.getInstance());
        if (DataWifiGetPushMasterSlaveStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataWifiGetPushMasterSlaveStatus.getInstance());
        }
        if (DataRcGetPushParams.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataRcGetPushParams.getInstance());
        }
    }

    @Getter(RemoteControllerKeys.MASTER_SLAVE_ID)
    public void getMasterSlaveId(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (((Boolean) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED), false)).booleanValue()) {
            callback.onFails(DJINarrowBandModuleError.NARROW_BAND_INSERTED);
            return;
        }
        final DataRcMasterSlaveId getter = new DataRcMasterSlaveId();
        getter.setGetMode(true);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, getter.getRcId());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(RemoteControllerKeys.MASTER_SLAVE_ID)
    public void setMasterSlaveId(String rcId, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (rcId == null || rcId.length() != 6) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (((Boolean) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED), false)).booleanValue()) {
            callback.onFails(DJINarrowBandModuleError.NARROW_BAND_INSERTED);
        } else {
            DataRcMasterSlaveId setter = new DataRcMasterSlaveId();
            setter.setGetMode(false);
            setter.setRcId(rcId);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass2 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    @Setter(RemoteControllerKeys.SET_MASTER_AUTH_CODE)
    public void setMasterAuthCode(String code, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (((Boolean) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED), false)).booleanValue()) {
            new DataNarrowBandBaseInfoConfig().setNarrowBandAuthCode(code).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        } else {
            DataWifiSetMasterSlaveAuthCode.getInstance().setAuthCode(code).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass4 */

                public void onSuccess(Object model) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                }

                public void onFailure(Ccode ccode) {
                    if (callback != null) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                }
            });
        }
    }

    @Action(RemoteControllerKeys.CONNECT_TO_MASTER_WITH_ID)
    public void joinMasterWithID(final DJISDKCacheHWAbstraction.InnerCallback callback, final AuthorizationInfo mode) {
        if (callback != null) {
            if (TextUtils.isEmpty(mode.getMasterId())) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
            } else if (!TextUtils.isEmpty(mode.getAuthCode())) {
                DataWifiConnectMasterWithIdAuthCode.getInstance().setMasterId(mode.getMasterId()).setAuthCode(mode.getAuthCode()).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass5 */

                    public void onSuccess(Object model) {
                        callback.onSuccess(null);
                    }

                    public void onFailure(Ccode ccode) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                });
            } else {
                DataWifiGetAuthCode.getInstance().setMasterId(mode.getMasterId()).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass6 */

                    public void onSuccess(Object model) {
                        DataWifiConnectMasterWithIdAuthCode.getInstance().setMasterId(mode.getMasterId()).setAuthCode(DataWifiGetAuthCode.getInstance().getAuthCode()).start(new DJIDataCallBack() {
                            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass6.AnonymousClass1 */

                            public void onSuccess(Object model) {
                                callback.onSuccess(null);
                            }

                            public void onFailure(Ccode ccode) {
                                callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                            }
                        });
                    }

                    public void onFailure(Ccode ccode) {
                        callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                    }
                });
            }
        }
    }

    @Getter(RemoteControllerKeys.MASTER_LIST)
    public void getMasterList(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final String[] tmp = new String[0];
        final List<String> list = new ArrayList<>();
        DataWifiScanMasterList.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                for (DataWifiScanMasterList.ScannedMasterInfo info : DataWifiScanMasterList.getInstance().getMasterInfo()) {
                    list.add(info.getMasterId());
                }
                CallbackUtils.onSuccess(callback, list.toArray(tmp));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
            }
        });
    }

    @Action(RemoteControllerKeys.GET_MASTER_AUTH_CODE)
    public void getAuthCodeFromMaster(final DJISDKCacheHWAbstraction.InnerCallback callback, String id) {
        if (((Boolean) CacheHelper.getValue(KeyHelper.getRemoteControllerKey(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED), false)).booleanValue()) {
            callback.onFails(DJINarrowBandModuleError.NARROW_BAND_INSERTED);
        } else {
            DataWifiGetAuthCode.getInstance().setMasterId(id).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass8 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, DataWifiGetAuthCode.getInstance().getAuthCode());
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                }
            });
        }
    }

    @Action(RemoteControllerKeys.RELEASE_GIMBAL_CONTROL)
    public void releaseGimbalControlPermission(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.supportMasterSlaveModeV2) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
        } else {
            DataRcSetAppSpecialControl.getInstance().setCmdType(DataRcSetAppSpecialControl.CmdType.SET_GIMBAL_CONTROL).setValue((byte) 0).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass9 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, DataWifiGetAuthCode.getInstance().getAuthCode());
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIRemoteControllerError.getDJIError(ccode));
                }
            });
        }
    }

    @Action(RemoteControllerKeys.REQUEST_GIMBAL_CONTROL)
    public void requestGimbalControl(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!this.supportMasterSlaveModeV2) {
            CallbackUtils.onFailure(callback, DJIRemoteControllerError.COMMON_UNSUPPORTED);
        } else {
            DataRcSetAppSpecialControl.getInstance().setCmdType(DataRcSetAppSpecialControl.CmdType.SET_GIMBAL_CONTROL).setValue((byte) 1).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass10 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    public void getChargeMobileMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            DataDm368GetGParams.getInstance().setType(true).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass11 */

                public void onSuccess(Object model) {
                    callback.onSuccess(ChargeMobileMode.find(DataDm368GetGParams.getInstance().getChargingMode()));
                }

                public void onFailure(Ccode ccode) {
                    callback.onFails(DJIRemoteControllerError.getDJIError(ccode));
                }
            });
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataWifiGetPushMasterSlaveStatus params) {
        computeMasterSlaveState();
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataRcGetPushParams params) {
        boolean hasControl;
        super.onEvent3BackgroundThread(params);
        if (params.isGetted()) {
            hasControl = params.isGettedGimbalControl();
        } else {
            hasControl = true;
        }
        notifyValueChangeForKeyPath(Boolean.valueOf(hasControl), genKeyPath(RemoteControllerKeys.RC_HAS_GIMBAL_CONTROL));
        computeMasterSlaveState();
    }

    private void computeMasterSlaveState() {
        MasterSlaveState.Builder masterSlaveStateBuilder = new MasterSlaveState.Builder();
        if (DataWifiGetPushMasterSlaveStatus.getInstance().isGetted()) {
            DataWifiGetPushMasterSlaveStatus params = DataWifiGetPushMasterSlaveStatus.getInstance();
            masterSlaveStateBuilder.masterId(params.getMasterId()).slaveId(params.getSlaveId()).authorizationCode(params.getAuthCode()).isConnected("on".equals(params.getConnectState())).freqPoint(params.getFreqPoint()).rcMode(RCMode.find(params.getStatusMode().value())).sendFreq(params.getSendFreq()).receiveFreq(params.getRecvFreq()).rssi(params.getRssi());
        }
        if (DataRcGetPushParams.getInstance().isGetted()) {
            masterSlaveStateBuilder.isGimbalControlled(DataRcGetPushParams.getInstance().isGettedGimbalControl());
        } else {
            masterSlaveStateBuilder.isGimbalControlled(true);
        }
        notifyValueChangeForKeyPath(masterSlaveStateBuilder.build(), genKeyPath(RemoteControllerKeys.MASTER_SLAVE_STATE));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [int, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataOnboardGetPushMixInfo params) {
        if (params.isGetted()) {
            if (params.isSimultaneousControlGimbal()) {
                notifyValueChangeForKeyPath((Object) 0, convertKeyToPath(RemoteControllerKeys.CONTROLLING_GIMBAL_INDEX));
            } else {
                notifyValueChangeForKeyPath(Integer.valueOf(params.getMappedGimbal()), convertKeyToPath(RemoteControllerKeys.CONTROLLING_GIMBAL_INDEX));
            }
        }
    }

    @Setter(RemoteControllerKeys.CONTROLLING_GIMBAL_INDEX)
    public void setControllingGimbalIndex(int index, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (DataOnboardGetPushMixInfo.getInstance().isSimultaneousControlGimbal() && index != 0) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (index == 0 || index == 1) {
            DataOnBoardSetMappedGimbal setter = new DataOnBoardSetMappedGimbal();
            setter.setMappedGimbalID(index);
            setter.start(CallbackUtils.defaultCB(callback));
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return true;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataNarrowBandBaseInfoConfig params) {
        if (params.isForNarrowBand()) {
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isNarrowBandConnected()), genKeyPath(RemoteControllerKeys.IS_NARROW_BAND_MODULE_CONNECTED));
            if (params.isNarrowBandConnected()) {
                notifyValueChangeForKeyPath(NarrowBandSlaveMode.find(params.getNarrowBandWorkMode()), genKeyPath(RemoteControllerKeys.NARROW_BAND_SLAVE_MODE));
                if (params.getNarrowBandWorkMode() == 0) {
                    notifyValueChangeForKeyPath(RCMode.MASTER, genKeyPath("Mode"));
                } else {
                    notifyValueChangeForKeyPath(RCMode.SLAVE, genKeyPath("Mode"));
                }
                notifyValueChangeForKeyPath((Object) true, genKeyPath(RemoteControllerKeys.RC_MASTER_SLAVE_OPEN));
            } else {
                notifyValueChangeForKeyPath(NarrowBandSlaveMode.UNKNOWN, genKeyPath(RemoteControllerKeys.NARROW_BAND_SLAVE_MODE));
            }
            notifyValueChangeForKeyPath(params.getNarrowBandSelfId(), genKeyPath(RemoteControllerKeys.NARROW_BAND_ID));
            notifyValueChangeForKeyPath(params.getNarrowBandMasterId(), genKeyPath(RemoteControllerKeys.NARROW_BAND_MASTER_ID));
            notifyValueChangeForKeyPath(params.getNarrowBandAuthCode(), genKeyPath(RemoteControllerKeys.NARROW_BAND_AUTHCODE));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.getNarrowBandConnectionState()), genKeyPath(RemoteControllerKeys.NARROW_BAND_CONNECTION_STATE));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.getPadAttennnaConnectionState()), genKeyPath(RemoteControllerKeys.NARROW_BAND_PAD_ANTENNA_CONNECT_STATE));
            notifyValueChangeForKeyPath(Boolean.valueOf(params.isNarrowBandShieldUpCmd()), genKeyPath(RemoteControllerKeys.NARROW_BAND_SHIELD_UP_CMD));
        }
    }

    @Setter(RemoteControllerKeys.NARROW_BAND_SHIELD_UP_CMD)
    public void setNarrowBandShieldUpCmd(final boolean shield, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataNarrowBandBaseInfoConfig setter = new DataNarrowBandBaseInfoConfig();
        setter.setCmdType(DataNarrowBandBaseInfoConfig.CmdType.NARROW_BAND_SHIELD_UP_CMD);
        setter.setValue((byte) (shield ? 1 : 0));
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass12 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    CallbackUtils.onSuccess(callback, Boolean.valueOf(shield));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    CallbackUtils.onFailure(callback, DJINarrowBandModuleError.getDJIError(ccode));
                }
            }
        });
    }

    @Getter(RemoteControllerKeys.NARROW_BAND_SLAVE_MODE)
    public void getNarrowBandSlaveMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataNarrowBandBaseInfoConfig getter = new DataNarrowBandBaseInfoConfig();
        getter.setCmdType(DataNarrowBandBaseInfoConfig.CmdType.NARROW_BAND_STATUS).setValue((byte) 0);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass13 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, NarrowBandSlaveMode.find(getter.getNarrowBandWorkMode()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJINarrowBandModuleError.getDJIError(ccode));
            }
        });
    }

    @Setter(RemoteControllerKeys.NARROW_BAND_SLAVE_MODE)
    public void setNarrowBandSlaveMode(final NarrowBandSlaveMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataNarrowBandBaseInfoConfig setter = new DataNarrowBandBaseInfoConfig();
        setter.setNarrowBandWorkMode((byte) mode.getValue());
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass14 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    CallbackUtils.onSuccess(callback, mode);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    CallbackUtils.onFailure(callback, DJINarrowBandModuleError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(RemoteControllerKeys.NARROW_BAND_AUTHCODE)
    public void setNarrowBandAuthCode(String authCode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataNarrowBandBaseInfoConfig().setNarrowBandAuthCode(authCode).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass15 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, model);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJINarrowBandModuleError.getDJIError(ccode));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataNarrowBandGetPushStateInfo info) {
        notifyValueChangeForKeyPath(new SlaveChannelState(info), genKeyPath(RemoteControllerKeys.NARROW_BAND_SLAVE_STATE));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataNarrowBandGetPushDeviceList push) {
        notifyValueChangeForKeyPath(push.getDeviceList(), genKeyPath(RemoteControllerKeys.NARROW_BAND_SCAN_MASTER_LIST));
    }

    @Action(RemoteControllerKeys.NARROW_BAND_EXCHANGE_MODE)
    public void exChangeNBMode(final DJISDKCacheHWAbstraction.InnerCallback callback, NarrowBandSlaveMode fromMode, NarrowBandSlaveMode toMode) {
        if (fromMode == null || toMode == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_INVALID);
        } else {
            new DataNarrowBandExchangeMode().setTransformation(fromMode.getValue(), toMode.getValue()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass16 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, model);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJINarrowBandModuleError.getDJIError(ccode));
                }
            });
        }
    }

    @Action(RemoteControllerKeys.NARROW_BAND_CONNECT)
    public void connectNarrowBand(final DJISDKCacheHWAbstraction.InnerCallback callback, String deviceID, String authCode, NarrowBandSlaveMode desireMode) {
        if (deviceID == null || authCode == null) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_INVALID);
        } else if (authCode.length() != 6) {
            CallbackUtils.onFailure(callback, DJINarrowBandModuleError.NARROW_BAND_AUTHENTICATE_ERROR);
        } else {
            new DataNarrowBandBaseInfoConfig().setNarrowBandConnect(deviceID, authCode, (byte) desireMode.getValue()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass17 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, model);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJINarrowBandModuleError.getDJIError(ccode));
                }
            });
        }
    }

    @Action(RemoteControllerKeys.NARROW_BAND_DISCONNECT)
    public void disconnectNarrowBand(final DJISDKCacheHWAbstraction.InnerCallback callback, String deviceID, String authCode) {
        if (callback != null) {
            if (deviceID == null || authCode == null) {
                callback.onFails(DJIError.COMMON_PARAM_INVALID);
            } else {
                new DataNarrowBandBaseInfoConfig().setNarrowBandDisConnect(deviceID, authCode).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.remotecontroller.DJIRCInspire2Abstraction.AnonymousClass18 */

                    public void onSuccess(Object model) {
                        CallbackUtils.onSuccess(callback, model);
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback, DJINarrowBandModuleError.getDJIError(ccode));
                    }
                });
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataNarrowBandExchangeMode exchangeInfo) {
        if (exchangeInfo.isGetted()) {
            notifyValueChangeForKeyPath(new NarrowBandExchangeEvent(exchangeInfo), convertKeyToPath(RemoteControllerKeys.NARROW_BAND_EXCHANGE_EVENT));
        }
    }
}

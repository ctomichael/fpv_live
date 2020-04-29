package dji.sdksharedlib.hardware.abstractions.gimbal;

import dji.common.error.DJIError;
import dji.common.error.DJIGimbalError;
import dji.common.gimbal.CapabilityKey;
import dji.common.gimbal.MovementSettingsProfile;
import dji.common.handheldcontroller.ControllerMode;
import dji.common.mission.waypoint.Waypoint;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DJIGimbalParamInfoManager;
import dji.midware.data.model.P3.DataGimbalActiveStatus;
import dji.midware.data.model.P3.DataGimbalGetHandleParams;
import dji.midware.data.model.P3.DataGimbalGetUserParams;
import dji.midware.data.model.P3.DataGimbalSetHandleParams;
import dji.midware.data.model.P3.DataSpecialControl;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.model.common.DataCommonActiveGetVer;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.DJISDKCache;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalAbstraction;
import dji.sdksharedlib.keycatalog.DJISDKCacheKey;
import dji.sdksharedlib.keycatalog.DJISDKCacheKeys;
import dji.sdksharedlib.keycatalog.GimbalKeys;
import dji.sdksharedlib.listener.DJIGetCallback;
import dji.sdksharedlib.store.DJISDKCacheParamValue;

@EXClassNullAway
public class DJIGimbalHandheldAbstraction extends DJIGimbalX3Abstraction {
    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
    }

    public void initGimbalCapability() {
        super.initGimbalCapability();
        addMinMaxToCapability(CapabilityKey.ADJUST_PITCH, -120, 30);
        addMinMaxToCapability(CapabilityKey.ADJUST_YAW, Integer.valueOf((int) Waypoint.MIN_HEADING), 180);
        addIsSupportedToCapability(CapabilityKey.ADJUST_ROLL, false);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SPEED_COEFFICIENT, 0, 100);
        addMinMaxToCapability(CapabilityKey.YAW_CONTROLLER_SPEED_COEFFICIENT, 0, 100);
        addMinMaxToCapability(CapabilityKey.PITCH_CONTROLLER_SMOOTHING_FACTOR, 0, 30);
        addMinMaxToCapability(CapabilityKey.YAW_CONTROLLER_SMOOTHING_FACTOR, 0, 30);
        addMinMaxToCapability(CapabilityKey.PITCH_SMOOTH_TRACK_ACCELERATION, 1, 30);
        addMinMaxToCapability(CapabilityKey.YAW_SMOOTH_TRACK_ACCELERATION, 1, 30);
        addMinMaxToCapability(CapabilityKey.PITCH_SMOOTH_TRACK_SPEED, 1, 100);
        addMinMaxToCapability(CapabilityKey.YAW_SMOOTH_TRACK_SPEED, 1, 100);
        addMinMaxToCapability(CapabilityKey.PITCH_SMOOTH_TRACK_DEADBAND, 0, 90);
        addMinMaxToCapability(CapabilityKey.YAW_SMOOTH_TRACK_DEADBAND, 0, 90);
        addIsSupportedToCapability(CapabilityKey.PITCH_SMOOTH_TRACK_ENABLED, true);
        addIsSupportedToCapability(CapabilityKey.YAW_SMOOTH_TRACK_ENABLED, true);
        addIsSupportedToCapability(CapabilityKey.MOVEMENT_SETTINGS, true);
    }

    @Setter(GimbalKeys.MOVEMENT_SETTINGS_PROFILE)
    public void setMovementSettingsProfile(MovementSettingsProfile userConfigType, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (userConfigType == MovementSettingsProfile.UNKNOWN) {
            callback.onFails(DJIError.COMMON_PARAM_ILLEGAL);
        } else {
            setParam(userConfigType.value(), DJIGimbalAbstraction.SettingParamCmd.TABLE_CHOICE, callback);
        }
    }

    @Getter(GimbalKeys.MOVEMENT_SETTINGS_PROFILE)
    public void getMovementSettingsProfile(DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (callback != null) {
            if (this.pushUserParams == null) {
                callback.onFails(DJIGimbalError.COMMON_TIMEOUT);
            } else {
                callback.onSuccess(MovementSettingsProfile.find(this.pushUserParams.getPresetID(getExpectedSenderIdByIndex())));
            }
        }
    }

    @Setter(GimbalKeys.CONTROLLER_MODE)
    public void setGimbalControllerMode(final ControllerMode stickControlMode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataGimbalSetHandleParams().setDualChannelEnable(stickControlMode.value()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass1 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                DJIGimbalHandheldAbstraction.this.getGimbalSettingValue(GimbalKeys.CONTROLLER_MODE, ControllerMode.class, stickControlMode, callback);
            }
        });
    }

    @Getter(GimbalKeys.CONTROLLER_MODE)
    public void getGimbalControllerMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataGimbalGetHandleParams getter = new DataGimbalGetHandleParams();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(ControllerMode.values()[getter.getDualChannelEnable()]);
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(GimbalKeys.PITCH_SMOOTH_TRACK_ENABLED)
    public void setSmoothTrackEnabledOnPitch(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setParam(enabled ? 1 : 0, DJIGimbalAbstraction.SettingParamCmd.PITCH_SMOOTH_TRACK_ENABLED, callback);
    }

    @Setter(GimbalKeys.YAW_SMOOTH_TRACK_ENABLED)
    public void setSmoothTrackEnabledOnYaw(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        setParam(enabled ? 1 : 0, DJIGimbalAbstraction.SettingParamCmd.YAW_SMOOTH_TRACK_ENABLED, callback);
    }

    @Getter(GimbalKeys.PITCH_SMOOTH_TRACK_ENABLED)
    public void getSmoothTrackEnabledOnPitch(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataGimbalGetUserParams) new DataGimbalGetUserParams().setInfos(new String[]{DJIGimbalAbstraction.SettingParamCmd.PITCH_SMOOTH_TRACK_ENABLED.getCmdString()}).setReceiverId(getReceiverIdByIndex(), DataGimbalGetUserParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                boolean z = true;
                DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
                if (DJIGimbalParamInfoManager.read(DJIGimbalAbstraction.SettingParamCmd.PITCH_SMOOTH_TRACK_ENABLED.getCmdString()).value.intValue() != 1) {
                    z = false;
                }
                innerCallback.onSuccess(Boolean.valueOf(z));
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(GimbalKeys.YAW_SMOOTH_TRACK_ENABLED)
    public void getSmoothTrackEnabledOnYaw(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ((DataGimbalGetUserParams) new DataGimbalGetUserParams().setInfos(new String[]{DJIGimbalAbstraction.SettingParamCmd.YAW_SMOOTH_TRACK_ENABLED.getCmdString()}).setReceiverId(getReceiverIdByIndex(), DataGimbalGetUserParams.class)).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                boolean z = true;
                DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
                if (DJIGimbalParamInfoManager.read(DJIGimbalAbstraction.SettingParamCmd.YAW_SMOOTH_TRACK_ENABLED.getCmdString()).value.intValue() != 1) {
                    z = false;
                }
                innerCallback.onSuccess(Boolean.valueOf(z));
            }

            public void onFailure(Ccode ccode) {
                callback.onFails(DJIError.getDJIError(ccode));
            }
        });
    }

    @Action(GimbalKeys.TOGGLE_SELFIE)
    public void toggleGimbalSelfie(DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataSpecialControl.getInstance().selfieGimbal().start(20);
        callback.onSuccess(null);
    }

    @Setter(GimbalKeys.YAW_INVERTED_CONTROL_ENABLED)
    public void setYawInverseControlEnabled(final boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataGimbalSetHandleParams().setPanDirection(enabled ? 1 : 0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                DJIGimbalHandheldAbstraction.this.getGimbalSettingValue(GimbalKeys.YAW_INVERTED_CONTROL_ENABLED, Boolean.class, Boolean.valueOf(enabled), callback);
            }
        });
    }

    @Getter(GimbalKeys.YAW_INVERTED_CONTROL_ENABLED)
    public void getYawInverseControlEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataGimbalGetHandleParams getter = new DataGimbalGetHandleParams();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                boolean z = true;
                int value = getter.getPanDirection();
                if (callback != null) {
                    DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
                    if (value != 1) {
                        z = false;
                    }
                    innerCallback.onSuccess(Boolean.valueOf(z));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIGimbalError.getDJIError(ccode));
                }
            }
        });
    }

    @Setter(GimbalKeys.PITCH_INVERTED_CONTROL_ENABLED)
    public void setPitchOrRollInverseControlEnabled(final boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataGimbalSetHandleParams().setTiltDirection(enabled ? 1 : 0).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
            }

            public void onFailure(Ccode ccode) {
                DJIGimbalHandheldAbstraction.this.getGimbalSettingValue(GimbalKeys.PITCH_INVERTED_CONTROL_ENABLED, Boolean.class, Boolean.valueOf(enabled), callback);
            }
        });
    }

    @Getter(GimbalKeys.PITCH_INVERTED_CONTROL_ENABLED)
    public void getPitchOrRollInverseControlEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataGimbalGetHandleParams getter = new DataGimbalGetHandleParams();
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass8 */

            public void onSuccess(Object model) {
                boolean z = true;
                int value = getter.getTiltDirection();
                if (callback != null) {
                    DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
                    if (value != 1) {
                        z = false;
                    }
                    innerCallback.onSuccess(Boolean.valueOf(z));
                }
            }

            public void onFailure(Ccode ccode) {
                if (callback != null) {
                    callback.onFails(DJIGimbalError.getDJIError(ccode));
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void getGimbalSettingValue(String key, final Class expectedClazz, final Object expectedValue, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJISDKCache.getInstance().getValue(new DJISDKCacheKey.Builder().component(GimbalKeys.COMPONENT_KEY).index(this.index).paramKey(key).build(), new DJIGetCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass9 */

            public void onSuccess(DJISDKCacheParamValue value) {
                if (value == null) {
                    return;
                }
                if (expectedValue == expectedClazz.cast(value.getData())) {
                    if (callback != null) {
                        callback.onSuccess(null);
                    }
                } else if (callback != null) {
                    callback.onFails(DJIGimbalError.RESULT_FAILED);
                }
            }

            public void onFails(DJIError error) {
                if (callback != null) {
                    callback.onFails(error);
                }
            }
        });
    }

    @Getter(DJISDKCacheKeys.SERIAL_NUMBER)
    public void getSerialNum(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataCommonActiveGetVer().setDevice(DeviceType.GIMBAL).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass10 */

            public void onSuccess(Object model) {
                DataGimbalActiveStatus.getInstance().setType(DataAbstractGetPushActiveStatus.TYPE.GET).start(new DJIDataCallBack() {
                    /* class dji.sdksharedlib.hardware.abstractions.gimbal.DJIGimbalHandheldAbstraction.AnonymousClass10.AnonymousClass1 */

                    public void onSuccess(Object model) {
                        CallbackUtils.onSuccess(callback, DJIGimbalHandheldAbstraction.this.getHashSerialNum(DataGimbalActiveStatus.getInstance().getSN(), 0));
                    }

                    public void onFailure(Ccode ccode) {
                        CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                    }
                });
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }
}

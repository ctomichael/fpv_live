package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import android.support.annotation.NonNull;
import com.dji.cmd.v1.protocol.TimeLapseWaypointInfo;
import com.dji.mapkit.core.models.DJILatLng;
import dji.common.camera.StabilizationState;
import dji.common.error.DJIError;
import dji.common.error.DJIFlightControllerError;
import dji.common.flightcontroller.AdvancedGoHomeState;
import dji.common.flightcontroller.DJIVisionTrackHeadingMode;
import dji.common.flightcontroller.DJIVisionTrackMode;
import dji.common.flightcontroller.FixedWingControl;
import dji.common.flightcontroller.VisionDetectionState;
import dji.common.flightcontroller.VisionDrawHeadingMode;
import dji.common.flightcontroller.VisionDrawStatus;
import dji.common.flightcontroller.VisionLandingProtectionState;
import dji.common.flightcontroller.flightassistant.AdvancedPilotAssistantSystemState;
import dji.common.flightcontroller.flightassistant.BottomAuxiliaryLightMode;
import dji.common.flightcontroller.flightassistant.FaceAwareState;
import dji.common.flightcontroller.flightassistant.IntelligentHotpointMissionMode;
import dji.common.flightcontroller.flightassistant.PalmControlState;
import dji.common.flightcontroller.flightassistant.PalmDetectionState;
import dji.common.flightcontroller.flightassistant.PoiException;
import dji.common.flightcontroller.flightassistant.PoiTargetInformation;
import dji.common.flightcontroller.flightassistant.PointOfInterestExecutingState;
import dji.common.flightcontroller.flightassistant.QuickShotException;
import dji.common.flightcontroller.flightassistant.TimeLapseException;
import dji.common.flightcontroller.flightassistant.TimeLapseFramesOption;
import dji.common.flightcontroller.flightassistant.TimeLapseState;
import dji.common.flightcontroller.flightassistant.TimeLapseSubMode;
import dji.common.mission.activetrack.ActiveTrackMode;
import dji.common.product.Model;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.internal.logics.CommonUtil;
import dji.internal.util.VisionDetectionStateHelper;
import dji.midware.component.DJIComponentManager;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.Data2100GetPushCheckStatus;
import dji.midware.data.model.P3.DataEyeFixedWingControl;
import dji.midware.data.model.P3.DataEyeGetHandGestureEnabled;
import dji.midware.data.model.P3.DataEyeGetPerceptionGesture;
import dji.midware.data.model.P3.DataEyeGetPushAdvancedPilotAssistantSystemState;
import dji.midware.data.model.P3.DataEyeGetPushAvoidanceParam;
import dji.midware.data.model.P3.DataEyeGetPushDrawState;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataEyeGetPushFaceDetectionTakeOffState;
import dji.midware.data.model.P3.DataEyeGetPushFixedWingState;
import dji.midware.data.model.P3.DataEyeGetPushFlatCheck;
import dji.midware.data.model.P3.DataEyeGetPushFrontAvoidance;
import dji.midware.data.model.P3.DataEyeGetPushMultiTrackingState;
import dji.midware.data.model.P3.DataEyeGetPushPOIExecutionParams;
import dji.midware.data.model.P3.DataEyeGetPushPOITargetInformation;
import dji.midware.data.model.P3.DataEyeGetPushPalmControlState;
import dji.midware.data.model.P3.DataEyeGetPushTimeLapseKeyFrame;
import dji.midware.data.model.P3.DataEyeGetPushTimeLapseOverallData;
import dji.midware.data.model.P3.DataEyeGetPushTrackStatus;
import dji.midware.data.model.P3.DataEyeGetTimeLapseKeyFrameInfoByIndex;
import dji.midware.data.model.P3.DataEyePushVisionTip;
import dji.midware.data.model.P3.DataEyeSetHandGestureEnabled;
import dji.midware.data.model.P3.DataEyeSetPOIParams;
import dji.midware.data.model.P3.DataEyeSetPerceptionGesture;
import dji.midware.data.model.P3.DataEyeSetTimeLapseKeyFrame;
import dji.midware.data.model.P3.DataEyeSetTimeLapseParams;
import dji.midware.data.model.P3.DataEyeSetTimeLapseSubMode;
import dji.midware.data.model.P3.DataFlycGetPushAvoidParam;
import dji.midware.data.model.P3.DataFlycSetParams;
import dji.midware.data.model.P3.DataSingleDebugCtrlParam;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.data.params.P3.ParamInfo;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.CacheHelper;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.ParamInfoCallBack;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.keycatalog.ProductKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.sdksharedlib.util.configuration.DJISDKCacheProductConfigManager;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class IntelligentFlightAssistant1860Abstraction extends IntelligentFlightAssistantAbstraction {
    private static final String STR_CFG_BRAKE = "g_config.avoid_obstacle_limit_cfg.avoid_obstacle_enable_0";
    private static final String STR_CFG_BRAKE_USER = "g_config.avoid_obstacle_limit_cfg.user_avoid_enable_0";
    private static final String STR_CFG_GH_AVOIDANCE = "g_config.go_home.avoid_enable_0";
    private static final String STR_CFG_VISION = "g_config.mvo_cfg.mvo_func_en_0";
    private final String LANDING_PROTECTION_ENABLED = IntelligentFlightAssistantKeys.LANDING_PROTECTION_ENABLED;
    private final String PRECISE_LANDING_ENABLED = IntelligentFlightAssistantKeys.PRECISION_LANDING_ENABLED;
    /* access modifiers changed from: private */
    public ParamInfo activeAvoidancePI = null;
    /* access modifiers changed from: private */
    public ParamInfo flatCheckPI = null;
    private VisionDetectionState.Callback pushAvoidanceParamCallback;
    private VisionDetectionState.Callback pushFrontAvoidanceParamCallback;
    protected StabilizationState stabilizationState;

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        this.pushAvoidanceParamCallback = new VisionDetectionState.Callback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass1 */

            public void onUpdate(@NonNull VisionDetectionState state) {
                IntelligentFlightAssistant1860Abstraction.this.notifyValueChangeForKeyPath(state.getSystemWarning(), IntelligentFlightAssistant1860Abstraction.this.convertKeyToPath(IntelligentFlightAssistantKeys.VISION_SYSTEM_WARNING));
                IntelligentFlightAssistant1860Abstraction.this.notifyValueChangeForKeyPath(state, IntelligentFlightAssistant1860Abstraction.this.convertKeyToPath(IntelligentFlightAssistantKeys.VISION_DETECTION_STATE));
            }
        };
        this.pushFrontAvoidanceParamCallback = new VisionDetectionState.Callback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass2 */

            public void onUpdate(@NonNull VisionDetectionState state) {
                IntelligentFlightAssistant1860Abstraction.this.notifyValueChangeForKeyPath(state.getDetectionSectors(), IntelligentFlightAssistant1860Abstraction.this.convertKeyToPath(IntelligentFlightAssistantKeys.DETECTION_SECTORS));
                IntelligentFlightAssistant1860Abstraction.this.notifyValueChangeForKeyPath(state, IntelligentFlightAssistant1860Abstraction.this.convertKeyToPath(IntelligentFlightAssistantKeys.VISION_DETECTION_STATE));
            }
        };
    }

    public void setActiveTrackMode(ActiveTrackMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (mode != ActiveTrackMode.TRACE) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_INVALID);
        } else {
            new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_MODE).setTrackMode(convertModeToTrackingMode(mode)).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass3 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        }
    }

    @Setter(IntelligentFlightAssistantKeys.IS_TRACKING_HIGH_SPEED_ENABLED)
    public void setTrackingHighSpeedEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.HIGH_SPEED_ENABLED).setHighSpeedEnabled(enabled).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Setter("HandGestureEnabled")
    public void setHandGestureEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataEyeSetHandGestureEnabled.getInstance().setEnabled(enabled).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass4 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter("HandGestureEnabled")
    public void getHandGestureEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataEyeGetHandGestureEnabled.getInstance().start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass5 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(DataEyeGetHandGestureEnabled.getInstance().getEnabled()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.PALM_CONTROL_AWAY_INDOOR_ENABLED)
    public void setPalmControlAwayIndoorEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.PALM_CONTROL_AWAY).setPalmControlAway(enabled).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass6 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.PALM_CONTROL_AWAY_INDOOR_ENABLED)
    public void getPalmControlAwayIndoorEnable(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam().setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.PALM_CONTROL_AWAY);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass7 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getPalmControlAway()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void setCircularSpeed(Float speed, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        ActiveTrackMode activeTrackMode = getCurrentActiveTrackMode();
        if (activeTrackMode == ActiveTrackMode.TRACE || activeTrackMode == ActiveTrackMode.PROFILE) {
            new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_CIRCLE_Y).setCircleY(speed.floatValue()).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass8 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
        } else {
            CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_UNSUPPORTED);
        }
    }

    public void getCircularSpeed(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (getCurrentActiveTrackMode() != ActiveTrackMode.TRACE) {
            CallbackUtils.onFailure(callback, DJIFlightControllerError.COMMON_UNSUPPORTED);
            return;
        }
        final DataSingleVisualParam getter = new DataSingleVisualParam().setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_CIRCLE_Y);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass9 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getCircleY()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void setActiveTrackGPSAssistantEnabled(Boolean isEnabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
    }

    public void getActiveTrackGPSAssistantEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
        CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
    }

    public void setActiveTrackGestureModeEnabled(Boolean isEnabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_INTELLIGENT).setTrackIntelligent(isEnabled.booleanValue()).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass10 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void getActiveTrackGestureModeEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam().setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_INTELLIGENT);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass11 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getTrackIntelligent()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    private ActiveTrackMode getCurrentActiveTrackMode() {
        if (DataEyeGetPushException.getInstance().isInTracking()) {
            return convertTrackingModeToMode(DataEyeGetPushTrackStatus.getInstance().getTrackingMode(), DataEyeGetPushTrackStatus.getInstance().getTargetType());
        }
        return ActiveTrackMode.UNKNOWN;
    }

    public void setCollisionAdvanceEnabled(boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (enable) {
            DataFlycSetParams setter = new DataFlycSetParams();
            setter.setIndexs("g_config.avoid_obstacle_limit_cfg.avoid_obstacle_enable_0", "g_config.avoid_obstacle_limit_cfg.user_avoid_enable_0", "g_config.go_home.avoid_enable_0");
            setter.setValues(1, 1, 1);
            setter.start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass12 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, ccode);
                }
            });
            return;
        }
        DataFlycSetParams setter2 = new DataFlycSetParams();
        setter2.setIndexs("g_config.avoid_obstacle_limit_cfg.avoid_obstacle_enable_0", "g_config.avoid_obstacle_limit_cfg.user_avoid_enable_0", "g_config.go_home.avoid_enable_0");
        setter2.setValues(0, 0, 0);
        setter2.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass13 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void setUserAvoidEnabled(boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataFlycSetParams setter = new DataFlycSetParams();
        setter.setIndexs("g_config.avoid_obstacle_limit_cfg.user_avoid_enable_0");
        Integer[] numArr = new Integer[1];
        numArr[0] = enable ? 1 : 0;
        setter.setValues(numArr);
        setter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass14 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    public void setVisionPositioningEnabled(boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (enable) {
            new DataFlycSetParams().setInfo("g_config.mvo_cfg.mvo_func_en_0", 1).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass15 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        } else {
            new DataFlycSetParams().setInfo("g_config.mvo_cfg.mvo_func_en_0", 0).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass16 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        }
    }

    public void getVisionPositioningEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.mergeGetFlycParamInfo.getInfo("g_config.mvo_cfg.mvo_func_en_0", new ParamInfoCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass17 */

            public void onSuccess(ParamInfo info) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(info.value.floatValue() > 0.0f));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    public void setRoofAvoidanceEnabled(boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DJISDKCacheProductConfigManager.getInstance().isConfigSupported(IntelligentFlightAssistantKeys.UPWARDS_AVOIDANCE_ENABLED)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
        } else {
            DJISDKCacheProductConfigManager.getInstance().writeConfig(IntelligentFlightAssistantKeys.UPWARDS_AVOIDANCE_ENABLED, Integer.valueOf(enabled ? 1 : 0), new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass18 */

                public void onSuccess(Object o) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    public void getRoofAvoidanceEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DJISDKCacheProductConfigManager.getInstance().isConfigSupported(IntelligentFlightAssistantKeys.UPWARDS_AVOIDANCE_ENABLED)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
        } else {
            this.newMergeGetFlycParamInfo.getInfo(IntelligentFlightAssistantKeys.UPWARDS_AVOIDANCE_ENABLED, new DJISDKCacheCommonMergeCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass19 */

                public void onSuccess(Object object) {
                    boolean z = true;
                    DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
                    if (((Number) object).intValue() != 1) {
                        z = false;
                    }
                    CallbackUtils.onSuccess(innerCallback, Boolean.valueOf(z));
                }

                public void onFailure(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    @Setter(IntelligentFlightAssistantKeys.LANDING_PROTECTION_ENABLED)
    public void setLandingProtectionEnabled(boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DJISDKCacheProductConfigManager.getInstance().isConfigSupported(IntelligentFlightAssistantKeys.LANDING_PROTECTION_ENABLED)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
        } else {
            DJISDKCacheProductConfigManager.getInstance().writeConfig(IntelligentFlightAssistantKeys.LANDING_PROTECTION_ENABLED, Integer.valueOf(enable ? 1 : 0), new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass20 */

                public void onSuccess(Object o) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    @Getter(IntelligentFlightAssistantKeys.LANDING_PROTECTION_ENABLED)
    public void getLandingProtectionEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DJISDKCacheProductConfigManager.getInstance().isConfigSupported(IntelligentFlightAssistantKeys.LANDING_PROTECTION_ENABLED)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
        } else {
            DJISDKCacheProductConfigManager.getInstance().readConfig(IntelligentFlightAssistantKeys.LANDING_PROTECTION_ENABLED, new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass21 */

                public void onSuccess(Object o) {
                    ParamInfo unused = IntelligentFlightAssistant1860Abstraction.this.flatCheckPI = DJISDKCacheProductConfigManager.getInstance().getParamInfo(IntelligentFlightAssistantKeys.LANDING_PROTECTION_ENABLED);
                    CallbackUtils.onSuccess(callback, Boolean.valueOf(IntelligentFlightAssistant1860Abstraction.this.flatCheckPI.value.intValue() != 0));
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    @Setter(IntelligentFlightAssistantKeys.TIME_LAPSE_SPEED_LOCKED)
    public void setTimeLapseSpeedLocked(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataEyeSetTimeLapseParams().setType(DataEyeSetTimeLapseParams.ParamType.SPEED_LOCKED).setValue(Integer.valueOf(enabled ? 1 : 0)).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Setter(IntelligentFlightAssistantKeys.PRECISION_LANDING_ENABLED)
    public void setPreciseLandingEnabled(boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DJISDKCacheProductConfigManager.getInstance().isConfigSupported(IntelligentFlightAssistantKeys.PRECISION_LANDING_ENABLED)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
        } else {
            DJISDKCacheProductConfigManager.getInstance().writeConfig(IntelligentFlightAssistantKeys.PRECISION_LANDING_ENABLED, Integer.valueOf(enable ? 1 : 0), new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass22 */

                public void onSuccess(Object o) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    @Getter(IntelligentFlightAssistantKeys.PRECISION_LANDING_ENABLED)
    public void getPreciseLandingEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DJISDKCacheProductConfigManager.getInstance().isConfigSupported(IntelligentFlightAssistantKeys.PRECISION_LANDING_ENABLED)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
        } else {
            DJISDKCacheProductConfigManager.getInstance().readConfig(IntelligentFlightAssistantKeys.PRECISION_LANDING_ENABLED, new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass23 */

                public void onSuccess(Object o) {
                    ParamInfo unused = IntelligentFlightAssistant1860Abstraction.this.flatCheckPI = DJISDKCacheProductConfigManager.getInstance().getParamInfo(IntelligentFlightAssistantKeys.PRECISION_LANDING_ENABLED);
                    CallbackUtils.onSuccess(callback, Boolean.valueOf(IntelligentFlightAssistant1860Abstraction.this.flatCheckPI.value.intValue() != 0));
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    @Setter(IntelligentFlightAssistantKeys.ACTIVE_OBSTACLE_AVOIDANCE_ENABLED)
    public void setActiveAvoidanceEnabled(Boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DJISDKCacheProductConfigManager.getInstance().isConfigSupported(IntelligentFlightAssistantKeys.ACTIVE_OBSTACLE_AVOIDANCE_ENABLED)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
        } else {
            DJISDKCacheProductConfigManager.getInstance().writeConfig(IntelligentFlightAssistantKeys.ACTIVE_OBSTACLE_AVOIDANCE_ENABLED, Integer.valueOf(enabled.booleanValue() ? 1 : 0), new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass24 */

                public void onSuccess(Object o) {
                    CallbackUtils.onSuccess(callback, (Object) null);
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    @Getter(IntelligentFlightAssistantKeys.ACTIVE_OBSTACLE_AVOIDANCE_ENABLED)
    public void getActiveAvoidanceEnable(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!DJISDKCacheProductConfigManager.getInstance().isConfigSupported(IntelligentFlightAssistantKeys.ACTIVE_OBSTACLE_AVOIDANCE_ENABLED)) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
        } else {
            DJISDKCacheProductConfigManager.getInstance().readConfig(IntelligentFlightAssistantKeys.ACTIVE_OBSTACLE_AVOIDANCE_ENABLED, new DJISDKCacheHWAbstraction.InnerCallback() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass25 */

                public void onSuccess(Object o) {
                    ParamInfo unused = IntelligentFlightAssistant1860Abstraction.this.activeAvoidancePI = DJISDKCacheProductConfigManager.getInstance().getParamInfo(IntelligentFlightAssistantKeys.ACTIVE_OBSTACLE_AVOIDANCE_ENABLED);
                    CallbackUtils.onSuccess(callback, Boolean.valueOf(IntelligentFlightAssistant1860Abstraction.this.activeAvoidancePI.value.intValue() != 0));
                }

                public void onFails(DJIError error) {
                    CallbackUtils.onFailure(callback, error);
                }
            });
        }
    }

    @Setter(IntelligentFlightAssistantKeys.ACTIVE_BACKWARD_FLYING_ENABLED)
    public void setActiveTrackBackwardFlyingEnabled(Boolean enabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_BACKWARD).setBackWardGain(enabled.booleanValue() ? 0.5f : 0.0f).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass26 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.ACTIVE_BACKWARD_FLYING_ENABLED)
    public void getActiveTrackBackwardFlyingEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_BACKWARD).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass27 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getBackWardGain() == 0.5f));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushAvoidanceParam param) {
        notifyValueChangeForKeyPath(Boolean.valueOf(param.isBraking()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_BRAKING));
        notifyValueChangeForKeyPath(Boolean.valueOf(param.isVisualSensorWorking() && param.isAvoidFrontWork()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_SENSOR_WORKING));
        notifyValueChangeForKeyPath(Boolean.valueOf(param.isOpenFrontRadar()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_FRONT_RADAR_OPEN));
        notifyValueChangeForKeyPath(Boolean.valueOf(param.isOpenBackRadar()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_BACK_RADAR_OPEN));
        notifyValueChangeForKeyPath(Boolean.valueOf(param.isOpenLeftRadar()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_LEFT_RADAR_OPEN));
        notifyValueChangeForKeyPath(Boolean.valueOf(param.isOpenRightRadar()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_RIGHT_RADAR_OPEN));
        VisionDetectionStateHelper.parseInformationFromDetectionPushedData(DataEyeGetPushAvoidanceParam.getInstance(), DataEyeGetPushFrontAvoidance.getInstance(), this.pushAvoidanceParamCallback);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushMultiTrackingState state) {
        notifyValueChangeForKeyPath(Boolean.valueOf(state.isHighSpeedEnabled()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_TRACKING_HIGH_SPEED_ENABLED));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushFrontAvoidance front) {
        if (front.isGetted()) {
            VisionDetectionStateHelper.parseInformationFromDetectionPushedData(DataEyeGetPushAvoidanceParam.getInstance(), DataEyeGetPushFrontAvoidance.getInstance(), this.pushFrontAvoidanceParamCallback);
        }
    }

    @Setter(IntelligentFlightAssistantKeys.RTH_OBSTACLE_AVOIDANCE_ENABLED)
    public void setGoHomeAvoid(final boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJISDKCacheProductConfigManager.getInstance().writeConfig(IntelligentFlightAssistantKeys.RTH_OBSTACLE_AVOIDANCE_ENABLED, Integer.valueOf(enable ? 1 : 0), new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass28 */

            public void onSuccess(Object o) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(enable));
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.RTH_OBSTACLE_AVOIDANCE_ENABLED)
    public void getGoHomeAvoid(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        DJISDKCacheProductConfigManager.getInstance().readConfig(IntelligentFlightAssistantKeys.RTH_OBSTACLE_AVOIDANCE_ENABLED, new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass29 */

            public void onSuccess(Object o) {
                boolean enable;
                boolean enable2 = true;
                try {
                    if (((ParamInfo) ((ArrayList) o).get(0)).value.intValue() != 0) {
                        enable = true;
                    } else {
                        enable = false;
                    }
                } catch (Exception e) {
                } finally {
                    CallbackUtils.onSuccess(callback, Boolean.valueOf(enable2));
                }
            }

            public void onFails(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushTrackStatus trackStatus) {
        notifyValueChangeForKeyPath(Boolean.valueOf(trackStatus.getRectMode() == DataEyeGetPushTrackStatus.TrackMode.TRACKING || trackStatus.getRectMode() == DataEyeGetPushTrackStatus.TrackMode.NORMAL), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_TRACKING));
        notifyValueChangeForKeyPath(convertTrackingModeToMode(trackStatus.getTrackingMode(), trackStatus.getTargetType()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.ACTIVE_TRACK_MODE));
        if (!DJIComponentManager.PlatformType.WM230.equals(DJIComponentManager.getInstance().getPlatformType())) {
            notifyValueChangeForKeyPath(Integer.valueOf(trackStatus.getCurrentTrackingMaximumSpeed()), convertKeyToPath("TrackingMaximumSpeed"));
            notifyValueChangeForKeyPath(Integer.valueOf(trackStatus.getTrackingSpeedThreshold()), convertKeyToPath(IntelligentFlightAssistantKeys.TRACKING_SPEED_THRESHOLD));
        }
        notifyValueChangeForKeyPath(trackStatus.getException(), IntelligentFlightAssistantKeys.PALM_CONTROL_EXCEPTION);
    }

    /* access modifiers changed from: protected */
    public ActiveTrackMode convertTrackingModeToMode(DataSingleVisualParam.TrackingMode mode, DataEyeGetPushTrackStatus.TargetObjType targetObjType) {
        if (mode == null) {
            return ActiveTrackMode.UNKNOWN;
        }
        switch (mode) {
            case HEADLESS_FOLLOW:
                return ActiveTrackMode.TRACE;
            case HEAD_LOCK:
            case SPOTLIGHT:
                return ActiveTrackMode.SPOTLIGHT;
            case WATCH_TARGET:
                if (targetObjType == DataEyeGetPushTrackStatus.TargetObjType.HOT_POINT) {
                    return ActiveTrackMode.SPOTLIGHT_HEAT_POINT;
                }
                return ActiveTrackMode.SPOTLIGHT_PRO;
            case FIXED_ANGLE:
                return ActiveTrackMode.PROFILE;
            case QUICK_MOVIE:
                return ActiveTrackMode.QUICK_SHOT;
            default:
                return ActiveTrackMode.UNKNOWN;
        }
    }

    /* access modifiers changed from: protected */
    public DataSingleVisualParam.TrackingMode convertModeToTrackingMode(ActiveTrackMode mode) {
        if (mode == null) {
            return DataSingleVisualParam.TrackingMode.HEADLESS_FOLLOW;
        }
        switch (mode) {
            case TRACE:
                return DataSingleVisualParam.TrackingMode.HEADLESS_FOLLOW;
            case SPOTLIGHT:
                if (((Model) CacheHelper.getProduct(ProductKeys.MODEL_NAME)) == Model.MAVIC_AIR) {
                    return DataSingleVisualParam.TrackingMode.SPOTLIGHT;
                }
                return DataSingleVisualParam.TrackingMode.HEAD_LOCK;
            case SPOTLIGHT_PRO:
                return DataSingleVisualParam.TrackingMode.WATCH_TARGET;
            case SPOTLIGHT_HEAT_POINT:
                return DataSingleVisualParam.TrackingMode.WATCH_TARGET;
            case PROFILE:
                return DataSingleVisualParam.TrackingMode.FIXED_ANGLE;
            case QUICK_SHOT:
                return DataSingleVisualParam.TrackingMode.QUICK_MOVIE;
            default:
                return DataSingleVisualParam.TrackingMode.HEADLESS_FOLLOW;
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushFlatCheck params) {
        notifyValueChangeForKeyPath(VisionLandingProtectionState.find(params.getFlatStatus().value()), convertKeyToPath(IntelligentFlightAssistantKeys.LANDING_PROTECTION_STATE));
        notifyValueChangeForKeyPath(params.getFlatStatus(), convertKeyToPath(IntelligentFlightAssistantKeys.LANDING_PROTECTION_ORIGINAL_STATE));
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void
     arg types: [boolean, dji.sdksharedlib.keycatalog.DJISDKCacheKey]
     candidates:
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, java.lang.String):void
      dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction.notifyValueChangeForKeyPath(java.lang.Object, dji.sdksharedlib.keycatalog.DJISDKCacheKey):void */
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushException params) {
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isInPreciseLanding()), convertKeyToPath("PreciseLandingState"));
        if (!params.isInDraw()) {
            notifyValueChangeForKeyPath(VisionDrawStatus.OTHER, convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DRAW_STATUS));
        }
        if (params.isInAdvanceHoming()) {
            notifyValueChangeForKeyPath((Object) true, convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_IS_IN_ADVANCED_GO_HOME));
            notifyValueChangeForKeyPath(AdvancedGoHomeState.find(params.getAdvanceGoHomeState().value()), convertKeyToPath(IntelligentFlightAssistantKeys.ADVANCED_GO_HOME_STATE));
        } else {
            notifyValueChangeForKeyPath((Object) false, convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_IS_IN_ADVANCED_GO_HOME));
            notifyValueChangeForKeyPath(AdvancedGoHomeState.NONE, convertKeyToPath(IntelligentFlightAssistantKeys.ADVANCED_GO_HOME_STATE));
        }
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isInPreciseLanding()), convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_IS_IN_PRECISE_LANDING));
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isInTapFly()), convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_IS_IN_TAPFLY));
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isInTracking()), convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_IS_IN_TRACKING));
        notifyValueChangeForKeyPath(params.getTrackStatus(), convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TRACK_EXCEPTION));
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isInDraw()), convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_IS_IN_DRAW));
        notifyValueChangeForKeyPath(Long.valueOf(params.getVisionVersion()), convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_VISION_VERSION));
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isMovingObjectDetectEnable()), convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_IS_MOVINGEOBJ_DETECT));
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isMovingObjectDetectEnable()), convertKeyToPath(IntelligentFlightAssistantKeys.ADVANCED_GESTURE_CONTROL_ENABLED));
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isQuickMovieExecuting()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_EXECUTING_QUICK_MOVIE));
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isTimeLapseEnabled()), IntelligentFlightAssistantKeys.TIME_LAPSE_ENABLED);
        notifyValueChangeForKeyPath(Boolean.valueOf(params.isPoi2Enabled()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_POI2_ENABLED));
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataFlycGetPushAvoidParam.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataFlycGetPushAvoidParam.getInstance());
        }
        if (DataEyeGetPushFlatCheck.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushFlatCheck.getInstance());
        }
        if (DataEyeGetPushException.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushException.getInstance());
        }
        if (DataEyeGetPushDrawState.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushDrawState.getInstance());
        }
        if (DataEyeGetPushTrackStatus.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushTrackStatus.getInstance());
        }
        if (DataEyeGetPushFixedWingState.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushFixedWingState.getInstance());
        }
        if (DataEyeGetPushFrontAvoidance.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushFrontAvoidance.getInstance());
        }
        if (DataEyeGetPushAvoidanceParam.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushAvoidanceParam.getInstance());
        }
        if (DataEyeGetPushAdvancedPilotAssistantSystemState.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushAdvancedPilotAssistantSystemState.getInstance());
        }
        if (DataEyeGetPushTimeLapseOverallData.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushTimeLapseOverallData.getInstance());
        }
        if (DataEyeGetPushTimeLapseKeyFrame.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushTimeLapseKeyFrame.getInstance());
        }
    }

    @Setter(IntelligentFlightAssistantKeys.ADVANCED_GO_HOME_ENABLED)
    public void setAdvancedGoHomeEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i;
        DJISDKCacheProductConfigManager instance = DJISDKCacheProductConfigManager.getInstance();
        if (enabled) {
            i = 1;
        } else {
            i = 0;
        }
        instance.writeConfig(IntelligentFlightAssistantKeys.ADVANCED_GO_HOME_ENABLED, Integer.valueOf(i), new DJISDKCacheHWAbstraction.InnerCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass30 */

            public void onSuccess(Object o) {
            }

            public void onFails(DJIError error) {
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.ADVANCED_GO_HOME_ENABLED)
    public void getAdvancedGoHomeEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        this.newMergeGetFlycParamInfo.getInfo(IntelligentFlightAssistantKeys.ADVANCED_GO_HOME_ENABLED, new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass31 */

            public void onSuccess(Object object) {
                boolean z = true;
                DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
                if (((Number) object).intValue() != 1) {
                    z = false;
                }
                CallbackUtils.onSuccess(innerCallback, Boolean.valueOf(z));
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(callback, error);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DRAW_HEADING_MODE)
    public void setVisionDrawHeadingMode(final VisionDrawHeadingMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.DRAW_HEADING).setDrawHeading(DataSingleVisualParam.DrawHeading.find(mode.value())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass32 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, mode);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DRAW_HEADING_MODE)
    public void getVisionDrawHeadingMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.DRAW_HEADING).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass33 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, VisionDrawHeadingMode.find(getter.getDrawHeading().value()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DRAW_SPEED)
    public void setVisionDrawSpeed(final float speed, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.DRAW_SPEED).setDrawSpeed(speed).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass34 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(speed));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DRAW_SPEED)
    public void getVisionDrawSpeed(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.DRAW_SPEED).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass35 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getDrawSpeed()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DRAW_MODE)
    public void setVisionDrawMode(final DataSingleVisualParam.DrawMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.DRAW_MODE).setDrawMode(mode).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass36 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, mode);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DRAW_MODE)
    public void getVisionDrawMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.DRAW_MODE).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass37 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, getter.getDrawMode());
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TRACK_MODE)
    public void setVisionTrackMode(final DJIVisionTrackMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_MODE).setTrackMode(DataSingleVisualParam.TrackingMode.find(mode.value())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass38 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, mode);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TRACK_MODE)
    public void getVisionTrackMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_MODE).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass39 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, DJIVisionTrackMode.find(getter.getTrackMode().value()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TRACK_HEADING_MODE)
    public void setVisionTrackHeadingMode(final DJIVisionTrackHeadingMode mode, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_PROFILE_HEAD).setTrackHeading(DataSingleVisualParam.TrackHeading.find(mode.value())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass40 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, mode);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TRACK_HEADING_MODE)
    public void getVisionTrackHeadingMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_PROFILE_HEAD).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass41 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, DJIVisionTrackHeadingMode.find(getter.getTrackHeading().value()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TAPFLY_RC_GIMBALCTRL)
    public void setVisionTapFlyRcGimbalCtrl(final boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.FLY_RC_GIMBAL_CTRL).setRcGimbalCtrl(enable).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass42 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(enable));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TAPFLY_RC_GIMBALCTRL)
    public void getVisionTapFlyRcGimbalCtrl(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.FLY_RC_GIMBAL_CTRL).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass43 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.isRcGimbalCtrlEnable()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TRACK_AUTO_FOCUS)
    public void setVisionTrackAutoFocus(final boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_AUTO_FOCUS).setTrackAutoFocus(enable).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass44 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(enable));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TRACK_AUTO_FOCUS)
    public void getVisionTrackAutoFocus(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_AUTO_FOCUS).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass45 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getTrackAutoFocus()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.IS_ADVANCED_PILOT_ASSISTANT_SYSTEM_ENABLED)
    public void setAdvancedPilotAssistantSystem(boolean enable, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.OMNI_AVOIDANCE_SWITCH).setOmniAvoidanceEnable(enable).start(CallbackUtils.defaultCB(callback));
    }

    @Getter(IntelligentFlightAssistantKeys.IS_ADVANCED_PILOT_ASSISTANT_SYSTEM_ENABLED)
    public void getAdvancedPilotAssistantSystem(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.OMNI_AVOIDANCE_SWITCH).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass46 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getOmniAvoidanceEnable()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(Data2100GetPushCheckStatus status) {
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isObstacleDetectedFromLeft()), IntelligentFlightAssistantKeys.IS_OBSTACLE_DETECTED_FROM_LEFT);
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isObstacleDetectedFromRight()), IntelligentFlightAssistantKeys.IS_OBSTACLE_DETECTED_FROM_RIGHT);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushTimeLapseKeyFrame keyFrameState) {
        ArrayList<Long> array = keyFrameState.getIds();
        Long[] longArray = new Long[array.size()];
        for (int i = 0; i < longArray.length; i++) {
            longArray[i] = array.get(i);
        }
        notifyValueChangeForKeyPath(longArray, IntelligentFlightAssistantKeys.TIME_LAPSE_KEY_FRAME_ARRAY);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushAdvancedPilotAssistantSystemState advancedPilotAssistantSystemStatePush) {
        AdvancedPilotAssistantSystemState advancedPilotAssistantSystemState = new AdvancedPilotAssistantSystemState(advancedPilotAssistantSystemStatePush.isAPASOn(), advancedPilotAssistantSystemStatePush.isAPASWorking(), advancedPilotAssistantSystemStatePush.isNotOnAir(), advancedPilotAssistantSystemStatePush.isFlightControllerSubModuleUnsatisfied(), advancedPilotAssistantSystemStatePush.otherNavigationModulesWork(), advancedPilotAssistantSystemStatePush.isOnLimitAreaBoundaries(), advancedPilotAssistantSystemStatePush.isPositionSpeedInvalid(), advancedPilotAssistantSystemStatePush.isBinocularDeepImageInvalid());
        notifyValueChangeForKeyPath(advancedPilotAssistantSystemState, convertKeyToPath(IntelligentFlightAssistantKeys.APAS_STATE));
        notifyValueChangeForKeyPath(Boolean.valueOf(advancedPilotAssistantSystemState.isAPASOn()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_APAS_ENABLED));
        notifyValueChangeForKeyPath(Boolean.valueOf(advancedPilotAssistantSystemState.isNotOnAir() || advancedPilotAssistantSystemState.isFlightControllerSubModuleUnsatisfied() || advancedPilotAssistantSystemState.isOtherNavigationModulesWork() || advancedPilotAssistantSystemState.isPositionSpeedInvalid() || advancedPilotAssistantSystemState.isBinocularDeepImageInvalid()), convertKeyToPath(IntelligentFlightAssistantKeys.DOES_APAS_HAVE_TEMP_ERROR));
        notifyValueChangeForKeyPath(Boolean.valueOf(advancedPilotAssistantSystemState.isAPASWorking()), convertKeyToPath(IntelligentFlightAssistantKeys.IS_APAS_FUNCTIONING));
        notifyValueChangeForKeyPath(Boolean.valueOf(advancedPilotAssistantSystemState.isOnLimitAreaBoundaries()), convertKeyToPath(IntelligentFlightAssistantKeys.IN_ON_LIMITE_AREA_BOUNDARIES));
    }

    @Setter(IntelligentFlightAssistantKeys.RTH_REMOTE_OBSTACLE_AVOIDANCE_ENABLED)
    public void setVisionHomingSenseOn(final boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        int i = 0;
        DataSingleVisualParam paramCmdId = new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.HOMING_MONO_SENSE_ON);
        if (enable) {
            i = 1;
        }
        paramCmdId.setHomingMonoSenseOn(i).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass47 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(enable));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.RTH_REMOTE_OBSTACLE_AVOIDANCE_ENABLED)
    public void getVisionHomingSenseOn(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.HOMING_MONO_SENSE_ON).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass48 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getHomingMonoSenseOn() != 0));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TAPFLY_SPEED)
    public void setVisionTapFlySpeed(final float speed, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.FLY_USER_SPEED).setUserSpeed(speed).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass49 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(speed));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TAPFLY_SPEED)
    public void getVisionTapFlySpeed(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.FLY_USER_SPEED).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass50 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getUserSpeed()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.TAP_FLY_ENABLED)
    public void setTapFlyEnabled(boolean isEnabled, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setTapFlyEnterOrExit(isEnabled).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass51 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.COMMON_EXECUTION_FAILED);
            }
        });
    }

    @Setter("TrackingMaximumSpeed")
    public void setTrackingMaximumSpeed(int trackingMaximumSpeed, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setTrackingMaximumSpeed(trackingMaximumSpeed).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_MAXIMUM_SPEED).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass52 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.IS_POI2_ENABLED)
    public void setPoiEnabled(boolean isEnabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.POI_ENABLED).setPOIEnabled(isEnabled).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TRACK_CIRCLEY)
    public void setVisionTrackCircleY(final float speed, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_CIRCLE_Y).setCircleY(speed).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass53 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(speed));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_TRACK_CIRCLEY)
    public void getVisionTrackCircleY(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_CIRCLE_Y).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass54 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getCircleY()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_SELFIE_GPS)
    public void setVisionSelfieGPS(final boolean gpsTracking, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_GPS).setGpsTracking(gpsTracking).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass55 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(gpsTracking));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_SELFIE_GPS)
    public void getVisionSelfieGPS(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_GPS).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass56 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getGpsTracking()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.QUICK_MOVIE_DRONIE_MAXIMUM_DISTANCE)
    public void getQuickMovieDronieMaximumDistance(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.QUICK_MOVIE_DRONIE_MAXIMUM_DISTANCE).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass57 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getQuickMovieDronieDistance()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.QUICK_MOVIE_HELIX_MAXIMUM_DISTANCE)
    public void getQuickMovieHelixMaximumDistance(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.QUICK_MOVIE_HELIX_MAXIMUM_DISTANCE).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass58 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getQuickMovieHelixDistance()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.QUICK_MOVIE_ROCKET_MAXIMUM_DISTANCE)
    public void getQuickMovieRocketMaximumDistance(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.QUICK_MOVIE_ROCKET_MAXIMUM_DISTANCE).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass59 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getQuickMovieRocketDistance()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.REMOVE_TIME_LAPSE_KEY_FRAME)
    public void removeTimeLapseKeyFrame(Long id, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataEyeSetTimeLapseKeyFrame.getInstance().setAction(DataEyeSetTimeLapseKeyFrame.Action.DELETE).setIndex(id.longValue()).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Setter(IntelligentFlightAssistantKeys.ADD_TIME_LAPSE_KEY_FRAME)
    public void addTimeLapseKeyFrame(Long id, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataEyeSetTimeLapseKeyFrame.getInstance().setAction(DataEyeSetTimeLapseKeyFrame.Action.ADD).setIndex(id.longValue()).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Getter(IntelligentFlightAssistantKeys.GET_KEY_FRAME_BY_INDEX)
    public void getKeyFrameByIndex(long index, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataEyeGetTimeLapseKeyFrameInfoByIndex getter = new DataEyeGetTimeLapseKeyFrameInfoByIndex();
        getter.setIndex(index).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass60 */

            public void onSuccess(Object model) {
                callback.onSuccess(new TimeLapseWaypointInfo(getter.getReceiverData()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_FIXWING_GIMBALCTRL)
    public void setFixWingGimbalCtrl(final boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        float f;
        DataSingleDebugCtrlParam setter = new DataSingleDebugCtrlParam();
        DataSingleDebugCtrlParam.CtrlType ctrlType = DataSingleDebugCtrlParam.CtrlType.FIXWING_GIMBALCTRL;
        if (enable) {
            f = 0.0f;
        } else {
            f = 1.0f;
        }
        setter.setCtrlParam(ctrlType, f).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass61 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(enable));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Action(IntelligentFlightAssistantKeys.FIXED_WING_CONTROL)
    public void sendFixedWingCtrl(final DJISDKCacheHWAbstraction.InnerCallback callback, FixedWingControl type) {
        DataEyeFixedWingControl.getInstance().setRequestType(DataEyeFixedWingControl.FixedWingCtrlType.find(type.value())).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass62 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.SINGLE_VISION_SENSOR_ENABLED)
    public void setSingleVisionDetectionEnabled(boolean isEnabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataEyeSetPerceptionGesture().setCommand(DataEyeSetPerceptionGesture.CommandType.SINGLE_VISION_SENSOR).setValue(isEnabled ? 1 : 0).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Getter(IntelligentFlightAssistantKeys.SINGLE_VISION_SENSOR_ENABLED)
    public void getSingleVisionDetectionEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataEyeGetPerceptionGesture getter = new DataEyeGetPerceptionGesture();
        getter.setCommand(DataEyeSetPerceptionGesture.CommandType.SINGLE_VISION_SENSOR).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass63 */

            public void onSuccess(Object model) {
                boolean z = true;
                int value = getter.getValue();
                DJISDKCacheHWAbstraction.InnerCallback innerCallback = callback;
                if (value != 1) {
                    z = false;
                }
                CallbackUtils.onSuccess(innerCallback, Boolean.valueOf(z));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    /* access modifiers changed from: protected */
    public boolean isBottomAuxiliaryLightingModeAutoBeaconSupported() {
        return false;
    }

    @Setter(IntelligentFlightAssistantKeys.BOTTOM_AUXILIARY_LIGHT_MODE)
    public void setFlashLightMode(BottomAuxiliaryLightMode lightMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (lightMode == null || lightMode == BottomAuxiliaryLightMode.UNKNOWN) {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        } else if (lightMode != BottomAuxiliaryLightMode.BEACON || isBottomAuxiliaryLightingModeAutoBeaconSupported()) {
            new DataEyeSetPerceptionGesture().setCommand(DataEyeSetPerceptionGesture.CommandType.FLASH_LIGHT).setValue(lightMode.value()).start(CallbackUtils.getSetterDJIDataCallback(callback));
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter(IntelligentFlightAssistantKeys.BOTTOM_AUXILIARY_LIGHT_MODE)
    public void getFlashLightMode(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataEyeGetPerceptionGesture getter = new DataEyeGetPerceptionGesture();
        getter.setCommand(DataEyeSetPerceptionGesture.CommandType.FLASH_LIGHT).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass64 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, BottomAuxiliaryLightMode.find(getter.getValue()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.TIME_LAPSE_ENABLED)
    public void setTimeLapseEnabled(boolean isEnabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setParamCmdId(DataSingleVisualParam.ParamCmdId.TIMELAPSE).setGet(false).setTimelapseEnabled(isEnabled).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Setter(IntelligentFlightAssistantKeys.POI_CURRENT_SPEED)
    public void setPoiCurrentSpeed(float speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataEyeSetPOIParams().setType(DataEyeSetPOIParams.ParamType.VELOCITY).setValue(Float.valueOf(speed)).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Setter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DYNAMIC_HOME)
    public void setDynamicHome(final boolean enable, final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (!enable || !CommonUtil.isSlaveRc(null)) {
            new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.DYNAMIC_HOME).setDynamicHome(enable).start(new DJIDataCallBack() {
                /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass65 */

                public void onSuccess(Object model) {
                    CallbackUtils.onSuccess(callback, Boolean.valueOf(enable));
                }

                public void onFailure(Ccode ccode) {
                    CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
                }
            });
        } else {
            CallbackUtils.onFailure(callback, DJIError.COMMON_UNSUPPORTED);
        }
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: dji.common.util.CallbackUtils.onSuccess(dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$InnerCallback, java.lang.Object):void
     arg types: [dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$InnerCallback, boolean]
     candidates:
      dji.common.util.CallbackUtils.onSuccess(dji.common.util.CommonCallbacks$CompletionCallbackWith, java.lang.Object):void
      dji.common.util.CallbackUtils.onSuccess(dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction$InnerCallback, java.lang.Object):void */
    @Getter(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DYNAMIC_HOME)
    public void getDynamicHome(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        if (CommonUtil.isSlaveRc(null)) {
            CallbackUtils.onSuccess(callback, (Object) false);
            return;
        }
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.DYNAMIC_HOME).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass66 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getDynamicHome()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, DJIError.getDJIError(ccode));
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.TIME_LAPSE_SPEED)
    public void setTimeLapseSpeed(float speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TIME_LAPSE_SPEED).setTimeLapseSpeed(speed).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Getter(IntelligentFlightAssistantKeys.TIME_LAPSE_SPEED)
    public void getTimeLapseSpeed(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TIME_LAPSE_SPEED).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass67 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getTimeLapseSpeed()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.TIME_LAPSE_MAX_SPEED)
    public void setTimeLapseMaxSpeed(float speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TIME_LAPSE_MAX_SPEED).setTimeLapseSpeed(speed).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Getter(IntelligentFlightAssistantKeys.TIME_LAPSE_MAX_SPEED)
    public void getTimeLapseMaxSpeed(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TIME_LAPSE_MAX_SPEED).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass68 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getTimeLapseMaxSpeed()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.TIME_LAPSE_MIN_SPEED)
    public void setTimeLapseMinSpeed(float speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TIME_LAPSE_MIN_SPEED).setTimeLapseSpeed(speed).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Getter(IntelligentFlightAssistantKeys.TIME_LAPSE_MIN_SPEED)
    public void getTimeLapseMinSpeed(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TIME_LAPSE_MIN_SPEED).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistant1860Abstraction.AnonymousClass69 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, Float.valueOf(getter.getTimeLapseMinSpeed()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.TIME_LAPSE_SUB_MODE)
    public void setTimeLapseSubMode(TimeLapseSubMode subMode, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataEyeSetTimeLapseSubMode.getInstance().setTimeLapseSubMode(subMode.value()).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushTimeLapseOverallData status) {
        notifyValueChangeForKeyPath(TimeLapseSubMode.find(status.getSubModeType()), IntelligentFlightAssistantKeys.TIME_LAPSE_SUB_MODE);
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isTimeLapseExecuting()), IntelligentFlightAssistantKeys.IS_TIME_LAPSE_EXECUTING);
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isTimeLapsePaused()), IntelligentFlightAssistantKeys.IS_TIME_LAPSE_PAUSED);
        notifyValueChangeForKeyPath(TimeLapseState.find(status.getCurrentState()), IntelligentFlightAssistantKeys.TIME_LAPSE_STATE);
        notifyValueChangeForKeyPath(Integer.valueOf(status.getTotalShot()), IntelligentFlightAssistantKeys.TIME_LAPSE_TOTAL_NUMBER_OF_SHOOTING_PHOTO);
        notifyValueChangeForKeyPath(Integer.valueOf(status.getFinishedNumberOfShooting()), IntelligentFlightAssistantKeys.TIME_LAPSE_CURRENT_NNUMBER_OF_SHOOTING_PHOTO);
        notifyValueChangeForKeyPath(Integer.valueOf(status.getProgress()), IntelligentFlightAssistantKeys.TIME_LAPSE_PROGRESS);
        notifyValueChangeForKeyPath(Integer.valueOf(status.getTotalTime()), IntelligentFlightAssistantKeys.TIME_LAPSE_TOTAL_TIME);
        notifyValueChangeForKeyPath(Integer.valueOf(status.getExecutedTime()), IntelligentFlightAssistantKeys.TIME_LAPSE_EXECUTED_TIME);
        notifyValueChangeForKeyPath(TimeLapseException.find(status.getException()), IntelligentFlightAssistantKeys.TIME_LAPSE_EXCEPTION);
        notifyValueChangeForKeyPath(Float.valueOf(status.getCurGimbalAltitude()), IntelligentFlightAssistantKeys.TIME_LAPSE_CUR_GIMBAL_ATTI);
        notifyValueChangeForKeyPath(Float.valueOf(status.getMinGimbalAltitude()), IntelligentFlightAssistantKeys.TIME_LAPSE_MIN_GIMBAL_ATTI);
        notifyValueChangeForKeyPath(Float.valueOf(status.getMaxGimbalAltitude()), IntelligentFlightAssistantKeys.TIME_LAPSE_MAX_GIMBAL_ATTI);
        notifyValueChangeForKeyPath(Integer.valueOf(status.getDuration()), IntelligentFlightAssistantKeys.TIME_LAPSE_DURATION);
        notifyValueChangeForKeyPath(TimeLapseFramesOption.find(status.getFramesNumber()), IntelligentFlightAssistantKeys.TIME_LAPSE_FRAMES_OPTION);
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isInfiniteSupported()), IntelligentFlightAssistantKeys.TIME_LAPSE_IS_INFINITE_SUPPORTED);
        notifyValueChangeForKeyPath(Integer.valueOf(status.getMinSuggestTime()), IntelligentFlightAssistantKeys.MIN_SUGGEST_TIME);
        notifyValueChangeForKeyPath(Integer.valueOf(status.getMaxSuggestTime()), IntelligentFlightAssistantKeys.MAX_SUGGEST_TIME);
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isSpeedLockAvailable()), IntelligentFlightAssistantKeys.IS_SPEED_LOCK_AVAILABLE_IN_TIME_LAPSE);
        notifyValueChangeForKeyPath(Boolean.valueOf(status.isSpeedLocked()), IntelligentFlightAssistantKeys.TIME_LAPSE_SPEED_LOCKED);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushDrawState draw) {
        notifyValueChangeForKeyPath(DataEyeGetPushException.getInstance().isInDraw() ? VisionDrawStatus.find(draw.getState().value()) : VisionDrawStatus.OTHER, convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DRAW_STATUS));
        notifyValueChangeForKeyPath(draw.getDrawMode(), convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_DRAW_MODE));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushFixedWingState state) {
        notifyValueChangeForKeyPath(state.getFixedWingState(), convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_FIXEDWING_STATE));
        notifyValueChangeForKeyPath(Boolean.valueOf(state.isFixWingGimbalCtrled()), convertKeyToPath(IntelligentFlightAssistantKeys.FLIGHT_CONTROLLER_FIXWING_GIMBALCTRL));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushPalmControlState state) {
        notifyValueChangeForKeyPath(PalmControlState.find(state.getPalmControllingState()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.PALM_CONTROL_STATE));
        notifyValueChangeForKeyPath(PalmDetectionState.find(state.getPalmDetectionState()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.PALM_DETECTION_STATE));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushFaceDetectionTakeOffState state) {
        notifyValueChangeForKeyPath(FaceAwareState.find(state.getDetectionTakeOffState()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.FACE_DETECTION_STATE));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushPOIExecutionParams params) {
        IntelligentHotpointMissionMode missionMode = IntelligentHotpointMissionMode.NONE;
        if (params.getWorkMode() == 1) {
            missionMode = IntelligentHotpointMissionMode.VISION_BASED;
        } else if (params.getWorkMode() == 2) {
            missionMode = IntelligentHotpointMissionMode.GPS_BASED;
        }
        notifyValueChangeForKeyPath(PointOfInterestExecutingState.find(params.getState()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.POI_STATE));
        notifyValueChangeForKeyPath(missionMode, KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.POI_MISSION_MODE));
        notifyValueChangeForKeyPath(Float.valueOf(params.getMaxSpeed()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.POI_MAXIMUM_SPEED));
        notifyValueChangeForKeyPath(Float.valueOf(params.getSpeed()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.POI_CURRENT_SPEED));
        notifyValueChangeForKeyPath(Float.valueOf(params.getCircleRadius()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.POI_RADIUS));
        notifyValueChangeForKeyPath(Float.valueOf(params.getHeight()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.POI_HEIGHT));
        notifyValueChangeForKeyPath(PoiException.find(params.getException()), IntelligentFlightAssistantKeys.POI_EXCEPTION);
        notifyValueChangeForKeyPath(new DJILatLng(params.getLatitude(), params.getLongitude()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.POI_LOCATION));
        notifyValueChangeForKeyPath(Float.valueOf(params.getTargetHeight()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.POI_TARGET_HEIGHT));
        notifyValueChangeForKeyPath(Float.valueOf(params.getTargetRadius()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.POI_TARGET_RADIUS));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushPOITargetInformation info) {
        notifyValueChangeForKeyPath(new PoiTargetInformation(info.getStatus(), info.getCenterX(), info.getCenterY(), info.getWidth(), info.getHeight()), convertKeyToPath(IntelligentFlightAssistantKeys.POI_TARGET_INFORMATION));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataFlycGetPushAvoidParam avoid) {
        notifyValueChangeForKeyPath(Boolean.valueOf(avoid.isUserAvoidEnable()), convertKeyToPath(IntelligentFlightAssistantKeys.INTELLIGENT_FLIGHT_ASSISTANT_IS_USERAVOID_ENABLE));
        notifyValueChangeForKeyPath(Boolean.valueOf(avoid.gohomeAvoidEnable()), convertKeyToPath(IntelligentFlightAssistantKeys.RTH_OBSTACLE_AVOIDANCE_ENABLED));
        notifyValueChangeForKeyPath(Boolean.valueOf(avoid.isAvoidObstacleEnable()), convertKeyToPath(IntelligentFlightAssistantKeys.COLLISION_AVOIDANCE_ENABLED));
        notifyValueChangeForKeyPath(Boolean.valueOf(avoid.isAvoidOvershotAct()), convertKeyToPath("IsAvoidingActiveObstacleCollision"));
        notifyValueChangeForKeyPath(Boolean.valueOf(avoid.roofLimitWorkFlag()), convertKeyToPath("IsAscentLimitedByObstacle"));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyePushVisionTip tip) {
        notifyValueChangeForKeyPath(tip.getDynamicHomeGpsStatus(), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.DYNAMIC_HOME_GPS_EXCEPTION));
        notifyValueChangeForKeyPath(Integer.valueOf(tip.getQuickMovieProgress()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.QUICK_SHOT_PROGRESS));
        notifyValueChangeForKeyPath(QuickShotException.find(tip.getQuickShotException()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.QUICK_SHOT_EXCEPTION));
    }

    public void destroy() {
        super.destroy();
    }
}

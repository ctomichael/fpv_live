package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.common.error.DJIError;
import dji.common.flightcontroller.flightassistant.SmartCaptureCameraAction;
import dji.common.flightcontroller.flightassistant.SmartCaptureControlMode;
import dji.common.flightcontroller.flightassistant.SmartCaptureFanProtectionState;
import dji.common.flightcontroller.flightassistant.SmartCaptureFollowingMode;
import dji.common.flightcontroller.flightassistant.SmartCaptureSystemStatus;
import dji.common.flightcontroller.flightassistant.SmartCaptureTargetType;
import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataEyeGetPushMultiTrackingInformation;
import dji.midware.data.model.P3.DataEyeGetPushMultiTrackingState;
import dji.midware.data.model.P3.DataEyeGetPushPalmControlNotification;
import dji.midware.data.model.P3.DataEyeGetPushTrackStatus;
import dji.midware.data.model.P3.DataEyeGetSmartCaptureStatisticsData;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.midware.interfaces.DJIDataCallBack;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.Action;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.MergeGetSmartCaptureStatistics;
import dji.sdksharedlib.hardware.extension.DJISDKCacheCommonMergeCallback;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

@EXClassNullAway
public class IntelligentFlightAssistantWM230Abstraction extends IntelligentFlightAssistantMavicProAbstraction {
    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataEyeGetPushMultiTrackingState.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushMultiTrackingState.getInstance());
        }
        if (DataEyeGetPushMultiTrackingInformation.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushMultiTrackingInformation.getInstance());
        }
        if (DataEyeGetPushPalmControlNotification.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushPalmControlNotification.getInstance());
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushMultiTrackingState state) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = true;
        super.onEvent3BackgroundThread(state);
        if (!state.isTrackingExecuting() || !DataEyeGetPushException.getInstance().isMultiTrackingEnabled()) {
            z = false;
        } else {
            z = true;
        }
        notifyValueChangeForKeyPath(Boolean.valueOf(z), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_TRACKING_EXECUTING));
        if (!state.isTrackingExecuting() || !DataEyeGetPushException.getInstance().isMultiQuickShotEnabled()) {
            z2 = false;
        } else {
            z2 = true;
        }
        notifyValueChangeForKeyPath(Boolean.valueOf(z2), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_EXECUTING));
        if (!state.isInTracking() || !DataEyeGetPushException.getInstance().isMultiTrackingEnabled()) {
            z3 = false;
        } else {
            z3 = true;
        }
        notifyValueChangeForKeyPath(Boolean.valueOf(z3), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_TRACKING_WAITING_CONFIRMATION));
        if (!state.isInTracking() || !DataEyeGetPushException.getInstance().isMultiQuickShotEnabled()) {
            z4 = false;
        }
        notifyValueChangeForKeyPath(Boolean.valueOf(z4), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_WAITING_CONFIRMATION));
        notifyValueChangeForKeyPath(state.getTrackingException(), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.MULTI_TRACKING_EXCEPTION));
        DataSingleVisualParam.TrackingMode trackingMode = DataSingleVisualParam.TrackingMode.find(state.getOverallTrackingMode());
        notifyValueChangeForKeyPath(trackingMode, KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.MULTI_TRACKING_SUB_MODE));
        ArrayList<DataEyeGetPushMultiTrackingInformation.MultiTrackingTargetInformation> infoArray = DataEyeGetPushMultiTrackingInformation.getInstance().getMultiTrackingTargetsInformation();
        DataEyeGetPushTrackStatus.TargetObjType targetObjType = DataEyeGetPushTrackStatus.TargetObjType.UNKNOWN;
        if (infoArray.size() > 0) {
            targetObjType = DataEyeGetPushTrackStatus.TargetObjType.find(infoArray.get(0).type);
        }
        notifyValueChangeForKeyPath(convertTrackingModeToMode(trackingMode, targetObjType), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.ACTIVE_TRACK_MODE));
        notifyValueChangeForKeyPath(Integer.valueOf(state.getMaximumTrackingSpeed()), "TrackingMaximumSpeed");
        notifyValueChangeForKeyPath(Integer.valueOf(state.getTrackingSpeedThreshold()), IntelligentFlightAssistantKeys.TRACKING_SPEED_THRESHOLD);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushException exception) {
        super.onEvent3BackgroundThread(exception);
        notifyValueChangeForKeyPath(Boolean.valueOf(exception.isMultiQuickShotEnabled()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_ENABLED));
        notifyValueChangeForKeyPath(Boolean.valueOf(exception.isMultiTrackingEnabled()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_TRACKING_ENABLED));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushMultiTrackingInformation information) {
        ArrayList<DataEyeGetPushMultiTrackingInformation.MultiTrackingTargetInformation> infoArray = information.getMultiTrackingTargetsInformation();
        if (infoArray.size() > 0) {
            notifyValueChangeForKeyPath(DataEyeGetPushTrackStatus.TrackMode.find(infoArray.get(0).mode), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.MULTI_TRACKING_TRACK_MODE));
        } else {
            notifyValueChangeForKeyPath(DataEyeGetPushTrackStatus.TrackMode.LOST, KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.MULTI_TRACKING_TRACK_MODE));
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushPalmControlNotification notification) {
        notifyValueChangeForKeyPath(SmartCaptureCameraAction.getCameraAction(notification.getCameraAction()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.PALM_CONTROL_CAMERA_ACTION));
        notifyValueChangeForKeyPath(notification.getDetectionLogic() == 0 ? SmartCaptureTargetType.FOLLOWING_HANDS : SmartCaptureTargetType.FOLLOWING_BODY, KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.PALM_CONTROL_DETECTION_LOGIC));
        notifyValueChangeForKeyPath(SmartCaptureSystemStatus.getActionState(notification.getPalmControlActionState()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.PALM_CONTROL_ACTION_STATE));
        notifyValueChangeForKeyPath(SmartCaptureControlMode.getControlMode(notification.getPalmControlControlMode()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.PALM_CONTROL_CONTROL_MODE));
        notifyValueChangeForKeyPath(Integer.valueOf(notification.getLandingCounting()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.PALM_CONTROL_LANDING_PROGRESS));
        notifyValueChangeForKeyPath(Integer.valueOf(notification.getNumberOfPeopleInPic()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.PALM_CONTROL_NUMBER_OF_DETECTED_HUMANS));
        notifyValueChangeForKeyPath(SmartCaptureFanProtectionState.get(notification.getDetectingStateOfFanProtection()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.PALM_CONTROL_FAN_PROTECTION_DETECTING_STATE));
    }

    @Setter(IntelligentFlightAssistantKeys.SMART_CAPTURE_ENABLED)
    public void setSmartCaptureEnabled(Boolean isEnabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_INTELLIGENT).setTrackIntelligent(isEnabled.booleanValue()).start(CallbackUtils.getSetterDJIDataCallback(callback));
    }

    @Getter(IntelligentFlightAssistantKeys.SMART_CAPTURE_ENABLED)
    public void getSmartCaptureEnabled(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam().setGet(true).setParamCmdId(DataSingleVisualParam.ParamCmdId.TRACK_INTELLIGENT);
        getter.start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM230Abstraction.AnonymousClass1 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(callback, Boolean.valueOf(getter.getTrackIntelligent()));
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    @Setter(IntelligentFlightAssistantKeys.IS_MULTI_TRACKING_ENABLED)
    public void setMultiTrackingEnabled(Boolean isEnabled, DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.MULTI_TRACKING).setMultiTrackEnabled(isEnabled.booleanValue()).start(CallbackUtils.getSetterDJIDataCallback(innerCallback));
    }

    @Setter(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_ENABLED)
    public void setMultiQuickShotEnabled(Boolean isEnabled, DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.MULTI_QUICK_SHOT).setMultiQuickShotEnabled(isEnabled.booleanValue()).start(CallbackUtils.getSetterDJIDataCallback(innerCallback));
    }

    @Setter(IntelligentFlightAssistantKeys.SMART_CAPTURE_FOLLOWING_MODE)
    public void setGestureFollowMode(SmartCaptureFollowingMode mode, DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        if (mode == SmartCaptureFollowingMode.PROFILE || mode == SmartCaptureFollowingMode.TRACE) {
            new DataSingleVisualParam().setPalmControlMode(mode == SmartCaptureFollowingMode.TRACE ? 0 : 1).setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.PALM_CONTROL_MODE).start(CallbackUtils.getSetterDJIDataCallback(innerCallback));
        } else {
            CallbackUtils.onFailure(innerCallback, DJIError.COMMON_PARAM_ILLEGAL);
        }
    }

    @Getter(IntelligentFlightAssistantKeys.SMART_CAPTURE_FOLLOWING_MODE)
    public void getGestureFollowMode(final DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        final DataSingleVisualParam getter = new DataSingleVisualParam();
        getter.setParamCmdId(DataSingleVisualParam.ParamCmdId.PALM_CONTROL_MODE).setGet(true).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM230Abstraction.AnonymousClass2 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(innerCallback, getter.getPalmControlMode() == 0 ? SmartCaptureFollowingMode.TRACE : SmartCaptureFollowingMode.PROFILE);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(innerCallback, ccode);
            }
        });
    }

    @Getter(IntelligentFlightAssistantKeys.SMART_CAPTURE_STATISTICS_DATA_COUNT)
    public void getSmartCaptureStaticsDataCount(DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        MergeGetSmartCaptureStatistics.getInstance().getInfo(IntelligentFlightAssistantKeys.SMART_CAPTURE_STATISTICS_DATA_COUNT, getMergeGetCmd(innerCallback));
    }

    @Getter(IntelligentFlightAssistantKeys.SMART_CAPTURE_STATISTICS_DATA)
    public void getSmartCaptureStatisticsData(DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        MergeGetSmartCaptureStatistics.getInstance().getInfo(IntelligentFlightAssistantKeys.SMART_CAPTURE_STATISTICS_DATA, getMergeGetCmd(innerCallback));
    }

    @Getter(IntelligentFlightAssistantKeys.SMART_CAPTURE_STATISTICS_DATA_TIME_STAMP)
    public void getSmartCaptureTimeStamp(DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        MergeGetSmartCaptureStatistics.getInstance().getInfo(IntelligentFlightAssistantKeys.SMART_CAPTURE_STATISTICS_DATA_TIME_STAMP, getMergeGetCmd(innerCallback));
    }

    @Action(IntelligentFlightAssistantKeys.SMART_CAPTURE_CLEAR_CACHE)
    public void clearSmartCaptureClearCache(final DJISDKCacheHWAbstraction.InnerCallback callback) {
        new DataEyeGetSmartCaptureStatisticsData().setCmdTyp(1).start(new DJIDataCallBack() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM230Abstraction.AnonymousClass3 */

            public void onSuccess(Object model) {
                CallbackUtils.onSuccess(callback, (Object) null);
            }

            public void onFailure(Ccode ccode) {
                CallbackUtils.onFailure(callback, ccode);
            }
        });
    }

    private DJISDKCacheCommonMergeCallback getMergeGetCmd(final DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        return new DJISDKCacheCommonMergeCallback() {
            /* class dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantWM230Abstraction.AnonymousClass4 */

            public void onSuccess(Object object) {
                CallbackUtils.onSuccess(innerCallback, object);
            }

            public void onFailure(DJIError error) {
                CallbackUtils.onFailure(innerCallback, error);
            }
        };
    }
}

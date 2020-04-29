package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.common.util.CallbackUtils;
import dji.midware.data.model.P3.DataEyeGetPushException;
import dji.midware.data.model.P3.DataEyeGetPushMultiTrackingInformation;
import dji.midware.data.model.P3.DataEyeGetPushMultiTrackingState;
import dji.midware.data.model.P3.DataEyeGetPushTrackStatus;
import dji.midware.data.model.P3.DataSingleVisualParam;
import dji.sdksharedlib.extension.KeyHelper;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import java.util.ArrayList;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class IntelligentFlightAssistantWM160Abstraction extends IntelligentFlightAssistant1860Abstraction {
    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
    }

    public void syncPushDataFromMidware() {
        super.syncPushDataFromMidware();
        if (DataEyeGetPushException.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushException.getInstance());
        }
        if (DataEyeGetPushMultiTrackingState.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushMultiTrackingState.getInstance());
        }
        if (DataEyeGetPushMultiTrackingInformation.getInstance().isGetted()) {
            onEvent3BackgroundThread(DataEyeGetPushMultiTrackingInformation.getInstance());
        }
    }

    @Setter(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_ENABLED)
    public void setMultiQuickShotEnabled(Boolean isEnabled, DJISDKCacheHWAbstraction.InnerCallback innerCallback) {
        new DataSingleVisualParam().setGet(false).setParamCmdId(DataSingleVisualParam.ParamCmdId.MULTI_QUICK_SHOT).setMultiQuickShotEnabled(isEnabled.booleanValue()).start(CallbackUtils.getSetterDJIDataCallback(innerCallback));
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onEvent3BackgroundThread(DataEyeGetPushException exception) {
        super.onEvent3BackgroundThread(exception);
        notifyValueChangeForKeyPath(Boolean.valueOf(exception.isMultiQuickShotEnabled()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_QUICK_SHOT_ENABLED));
        notifyValueChangeForKeyPath(Boolean.valueOf(exception.isMultiTrackingEnabled()), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.IS_MULTI_TRACKING_ENABLED));
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
    public void onEvent3BackgroundThread(DataEyeGetPushMultiTrackingInformation information) {
        ArrayList<DataEyeGetPushMultiTrackingInformation.MultiTrackingTargetInformation> infoArray = information.getMultiTrackingTargetsInformation();
        if (infoArray.size() > 0) {
            notifyValueChangeForKeyPath(DataEyeGetPushTrackStatus.TrackMode.find(infoArray.get(0).mode), KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.MULTI_TRACKING_TRACK_MODE));
        } else {
            notifyValueChangeForKeyPath(DataEyeGetPushTrackStatus.TrackMode.LOST, KeyHelper.getIntelligentFlightAssistantKey(IntelligentFlightAssistantKeys.MULTI_TRACKING_TRACK_MODE));
        }
    }
}

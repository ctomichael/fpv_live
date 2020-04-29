package dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant;

import dji.common.mission.activetrack.ActiveTrackMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.util.DJIEventBusUtil;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.DJISubComponentHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Getter;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.MergeGetFlycParamInfo;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.merge.MergeGetNewFlyParamInfo;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public abstract class IntelligentFlightAssistantAbstraction extends DJISubComponentHWAbstraction {
    protected MergeGetFlycParamInfo mergeGetFlycParamInfo;
    protected MergeGetNewFlyParamInfo newMergeGetFlycParamInfo;

    @Getter(IntelligentFlightAssistantKeys.ACTIVE_BACKWARD_FLYING_ENABLED)
    public abstract void getActiveTrackBackwardFlyingEnabled(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter(IntelligentFlightAssistantKeys.ACTIVE_TRACK_GPS_ASSISTANT_ENABLED)
    public abstract void getActiveTrackGPSAssistantEnabled(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter(IntelligentFlightAssistantKeys.ADVANCED_GESTURE_CONTROL_ENABLED)
    public abstract void getActiveTrackGestureModeEnabled(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter(IntelligentFlightAssistantKeys.ACTIVE_TRACK_CIRCULAR_SPEED)
    public abstract void getCircularSpeed(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter(IntelligentFlightAssistantKeys.UPWARDS_AVOIDANCE_ENABLED)
    public abstract void getRoofAvoidanceEnabled(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Getter(IntelligentFlightAssistantKeys.VISION_ASSISTED_POSITIONING_ENABLED)
    public abstract void getVisionPositioningEnabled(DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(IntelligentFlightAssistantKeys.ACTIVE_BACKWARD_FLYING_ENABLED)
    public abstract void setActiveTrackBackwardFlyingEnabled(Boolean bool, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(IntelligentFlightAssistantKeys.ACTIVE_TRACK_GPS_ASSISTANT_ENABLED)
    public abstract void setActiveTrackGPSAssistantEnabled(Boolean bool, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(IntelligentFlightAssistantKeys.ADVANCED_GESTURE_CONTROL_ENABLED)
    public abstract void setActiveTrackGestureModeEnabled(Boolean bool, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(IntelligentFlightAssistantKeys.ACTIVE_TRACK_MODE)
    public abstract void setActiveTrackMode(ActiveTrackMode activeTrackMode, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(IntelligentFlightAssistantKeys.ACTIVE_TRACK_CIRCULAR_SPEED)
    public abstract void setCircularSpeed(Float f, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(IntelligentFlightAssistantKeys.COLLISION_AVOIDANCE_ENABLED)
    public abstract void setCollisionAdvanceEnabled(boolean z, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(IntelligentFlightAssistantKeys.UPWARDS_AVOIDANCE_ENABLED)
    public abstract void setRoofAvoidanceEnabled(boolean z, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(IntelligentFlightAssistantKeys.INTELLIGENT_FLIGHT_ASSISTANT_IS_USERAVOID_ENABLE)
    public abstract void setUserAvoidEnabled(boolean z, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    @Setter(IntelligentFlightAssistantKeys.VISION_ASSISTED_POSITIONING_ENABLED)
    public abstract void setVisionPositioningEnabled(boolean z, DJISDKCacheHWAbstraction.InnerCallback innerCallback);

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        addCharacteristics(IntelligentFlightAssistantKeys.class, getClass());
    }

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        DJIEventBusUtil.register(this);
        this.mergeGetFlycParamInfo = MergeGetFlycParamInfo.getInstance();
        this.newMergeGetFlycParamInfo = MergeGetNewFlyParamInfo.getInstance();
    }

    public void destroy() {
        DJIEventBusUtil.unRegister(this);
        super.destroy();
    }
}

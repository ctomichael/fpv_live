package dji.internal.mock.abstractions;

import dji.common.mission.activetrack.ActiveTrackMode;
import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.flightcontroller.flightassistant.IntelligentFlightAssistantAbstraction;
import dji.sdksharedlib.keycatalog.IntelligentFlightAssistantKeys;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;
import dji.thirdparty.rx.Observable;
import dji.thirdparty.rx.functions.Func1;
import dji.thirdparty.rx.schedulers.Schedulers;
import java.util.concurrent.TimeUnit;

@EXClassNullAway
public class MockIntelligentFlightAssistantAbstraction extends IntelligentFlightAssistantAbstraction {
    /* access modifiers changed from: private */
    public boolean isEnabled = false;

    public void init(String component, int index, String subComponent, int subComponentIndex, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, subComponent, subComponentIndex, storeLayer, onValueChangeListener);
        mockData();
    }

    /* access modifiers changed from: protected */
    public void initializeComponentCharacteristics() {
        super.initializeComponentCharacteristics();
        addCharacteristics(IntelligentFlightAssistantKeys.class, getClass());
    }

    public void mockData() {
        Observable.timer(1000, TimeUnit.MILLISECONDS, Schedulers.computation()).flatMap(new Func1<Long, Observable<Boolean>>() {
            /* class dji.internal.mock.abstractions.MockIntelligentFlightAssistantAbstraction.AnonymousClass1 */

            public Observable<Boolean> call(Long aLong) {
                MockIntelligentFlightAssistantAbstraction.this.notifyValueChangeForKeyPath(Boolean.valueOf(MockIntelligentFlightAssistantAbstraction.this.isEnabled = !MockIntelligentFlightAssistantAbstraction.this.isEnabled), IntelligentFlightAssistantKeys.VISION_ASSISTED_POSITIONING_ENABLED);
                return Observable.just(true);
            }
        }).repeat().subscribe();
    }

    public void setActiveTrackMode(ActiveTrackMode mode, DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void setCircularSpeed(Float speed, DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void getCircularSpeed(DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void setActiveTrackGPSAssistantEnabled(Boolean isEnabled2, DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void getActiveTrackGPSAssistantEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void setActiveTrackGestureModeEnabled(Boolean isEnabled2, DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void getActiveTrackGestureModeEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void setCollisionAdvanceEnabled(boolean enable, DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void setUserAvoidEnabled(boolean enable, DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void setVisionPositioningEnabled(boolean enable, DJISDKCacheHWAbstraction.InnerCallback callback) {
        notifyValueChangeForKeyPath(Boolean.valueOf(enable), IntelligentFlightAssistantKeys.VISION_ASSISTED_POSITIONING_ENABLED);
        if (callback != null) {
            callback.onSuccess(null);
        }
    }

    public void getVisionPositioningEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void getRoofAvoidanceEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void setRoofAvoidanceEnabled(boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void setActiveTrackBackwardFlyingEnabled(Boolean enabled, DJISDKCacheHWAbstraction.InnerCallback callback) {
    }

    public void getActiveTrackBackwardFlyingEnabled(DJISDKCacheHWAbstraction.InnerCallback callback) {
    }
}

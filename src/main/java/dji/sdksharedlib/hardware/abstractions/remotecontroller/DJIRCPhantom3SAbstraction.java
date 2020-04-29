package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class DJIRCPhantom3SAbstraction extends DJIRCAbstraction {
    public DJIRCPhantom3SAbstraction() {
        this.hardwareState.getC1Button().setPresent(false);
        this.hardwareState.getC2Button().setPresent(false);
        this.hardwareState.getGoHomeButton().setPresent(false);
        this.hardwareState.getPlaybackButton().setPresent(false);
        this.hardwareState.getRecordButton().setPresent(false);
        this.hardwareState.getRightWheel().setPresent(false);
        this.hardwareState.getShutterButton().setPresent(false);
        this.hardwareState.getTransformationSwitch().setPresent(false);
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNamePhantom3Standard;
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
    }
}

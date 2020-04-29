package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class DJIRCPhantom3Abstraction extends DJIRCAbstraction {
    private boolean isNewProgressOfActivation = false;

    public DJIRCPhantom3Abstraction() {
        this.hardwareState.getC1Button().setPresent(true);
        this.hardwareState.getC2Button().setPresent(true);
        this.hardwareState.getGoHomeButton().setPresent(true);
        if (isPlaybackButtonSupported()) {
            this.hardwareState.getPlaybackButton().setPresent(true);
        } else {
            this.hardwareState.getPauseButton().setPresent(true);
        }
        this.hardwareState.getRecordButton().setPresent(true);
        this.hardwareState.getRightWheel().setPresent(true);
        this.hardwareState.getShutterButton().setPresent(true);
        this.hardwareState.getTransformationSwitch().setPresent(false);
        if (isP4PAndP4ARC()) {
            this.isNewProgressOfActivation = true;
        }
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNamePhantom3Professinal;
    }

    /* access modifiers changed from: protected */
    public boolean isNewProgressOfActivation() {
        return this.isNewProgressOfActivation;
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
    }
}

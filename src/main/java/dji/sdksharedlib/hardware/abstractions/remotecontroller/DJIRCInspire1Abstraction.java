package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.store.DJISDKCacheStoreLayer;

@EXClassNullAway
public class DJIRCInspire1Abstraction extends DJIRCAbstraction {
    public DJIRCInspire1Abstraction() {
        this.supportMasterSlaveMode = true;
        this.remoteFocusCheckingSupported = true;
        this.hardwareState.getC1Button().setPresent(true);
        this.hardwareState.getC2Button().setPresent(true);
        this.hardwareState.getGoHomeButton().setPresent(true);
        this.hardwareState.getPlaybackButton().setPresent(true);
        this.hardwareState.getRecordButton().setPresent(true);
        this.hardwareState.getRightWheel().setPresent(true);
        this.hardwareState.getShutterButton().setPresent(true);
        this.hardwareState.getTransformationSwitch().setPresent(true);
    }

    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNameInspire1;
    }

    public void init(String component, int index, DJISDKCacheStoreLayer storeLayer, DJISDKCacheHWAbstraction.OnValueChangeListener onValueChangeListener) {
        super.init(component, index, storeLayer, onValueChangeListener);
    }
}

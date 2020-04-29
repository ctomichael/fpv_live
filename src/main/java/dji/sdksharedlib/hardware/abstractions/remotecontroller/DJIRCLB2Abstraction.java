package dji.sdksharedlib.hardware.abstractions.remotecontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class DJIRCLB2Abstraction extends DJIRCInspire1Abstraction {
    /* access modifiers changed from: protected */
    public String getComponentDisplayName() {
        return DJIRCAbstraction.DisplayNameLightbridge2;
    }
}

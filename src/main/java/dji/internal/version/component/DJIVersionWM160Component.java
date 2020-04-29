package dji.internal.version.component;

import dji.midware.data.config.P3.DeviceType;

public class DJIVersionWM160Component extends DJIVersionWM230Component {
    /* access modifiers changed from: protected */
    public DeviceType getReceiverType() {
        return DeviceType.CAMERA;
    }

    /* access modifiers changed from: protected */
    public int getReceiverID() {
        return 0;
    }
}

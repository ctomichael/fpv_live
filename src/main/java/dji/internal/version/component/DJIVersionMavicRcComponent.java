package dji.internal.version.component;

import dji.midware.data.config.P3.DeviceType;

public class DJIVersionMavicRcComponent extends DJIVersionInspire2RcComponent {
    /* access modifiers changed from: protected */
    public DeviceType getDeviceType() {
        return DeviceType.DM368_G;
    }

    /* access modifiers changed from: protected */
    public int getDeviceId() {
        return 1;
    }
}

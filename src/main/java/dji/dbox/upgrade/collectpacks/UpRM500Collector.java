package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.midware.data.config.P3.DeviceType;
import java.util.ArrayList;

public class UpRM500Collector extends UpRcParallelCollector {
    public /* bridge */ /* synthetic */ ArrayList getGroupList() {
        return super.getGroupList();
    }

    public /* bridge */ /* synthetic */ void initFirmwareGroup() {
        super.initFirmwareGroup();
    }

    public /* bridge */ /* synthetic */ void startCollect() {
        super.startCollect();
    }

    public /* bridge */ /* synthetic */ void stop(boolean z) {
        super.stop(z);
    }

    UpRM500Collector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
    }

    /* access modifiers changed from: protected */
    public DeviceType getDeviceType() {
        return DeviceType.DM368_G;
    }

    /* access modifiers changed from: protected */
    public int getDeviceId() {
        return 1;
    }
}

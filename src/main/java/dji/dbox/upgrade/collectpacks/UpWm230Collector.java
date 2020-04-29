package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;

@EXClassNullAway
public class UpWm230Collector extends UpWm330Collector {
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

    UpWm230Collector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
    }
}

package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
class UpWm331Collector extends UpWm330Collector {
    UpWm331Collector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
    }
}

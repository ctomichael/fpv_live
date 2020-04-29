package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
class UpPm410Collector extends UpWm330Collector {
    UpPm410Collector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
    }
}

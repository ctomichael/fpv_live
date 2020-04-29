package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
class UpWm221Collector extends UpWm220Collector {
    UpWm221Collector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
    }
}

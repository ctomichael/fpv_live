package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class UpCollectorFactory {
    public static UpBaseCollector build(Context context, DJIUpDeviceType productId) {
        if (productId == null) {
            return null;
        }
        switch (productId) {
            case wm220:
                return new UpWm220Collector(context, productId);
            case wm221:
                return new UpWm221Collector(context, productId);
            case gl811:
                return new UpGl811Collector(context, productId);
            case wm331:
            case wm332:
                return new UpWm331Collector(context, productId);
            case wm330:
            case wm334:
                return new UpWm330Collector(context, productId);
            case wm335:
                return new UpWm335Collector(context, productId);
            case wm620:
                return new UpWm620Collector(context, productId);
            case pm410:
                return new UpPm410Collector(context, productId);
            case rc001:
            case ag410:
                return new UpRc001Collector(context, productId);
            case rc003:
                return new UpRc001Collector(context, productId);
            case rc002:
                return new UpRc002Collector(context, productId);
            case wm100ac:
                return new UpWm100Collector(context, productId);
            case rc230:
                return new UpRc230Collector(context, productId);
            case rm500:
                return new UpRM500Collector(context, productId);
            case wm230:
            case wm240:
                return new UpWm230Collector(context, productId);
            case rc240:
            case rc010:
                return new UpRc240Collector(context, productId);
            default:
                return null;
        }
    }
}

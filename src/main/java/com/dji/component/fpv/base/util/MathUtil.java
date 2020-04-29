package com.dji.component.fpv.base.util;

import dji.publics.LogReport.base.Fields;
import kotlin.Metadata;
import kotlin.jvm.JvmName;

@Metadata(bv = {1, 0, 3}, d1 = {"\u0000\u0012\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0002\u001a\u0016\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0016\u0010\u0004\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0016\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001\u001a\u0016\u0010\u0007\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u0001¨\u0006\b"}, d2 = {"ceil", "", "accurateValue", Fields.Dgo_update.STEP, "floor", "isRounded", "", "round", "DJIFpvBase_debug"}, k = 2, mv = {1, 1, 15})
@JvmName(name = "MathUtil")
/* compiled from: MathUtil.kt */
public final class MathUtil {
    public static final int round(int accurateValue, int step) {
        if (accurateValue % step >= step / 2) {
            return ((accurateValue / step) + 1) * step;
        }
        return (accurateValue / step) * step;
    }

    public static final int ceil(int accurateValue, int step) {
        if (accurateValue % step != 0) {
            return ((accurateValue / step) + 1) * step;
        }
        return accurateValue;
    }

    public static final int floor(int accurateValue, int step) {
        return (accurateValue / step) * step;
    }

    public static final boolean isRounded(int accurateValue, int step) {
        return accurateValue % step == 0;
    }
}

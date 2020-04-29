package dji.dbox.upgrade.strategy;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public abstract class AbstractStrategy {
    protected UpgradeStrategyContext upCollectorListener;

    public abstract void doNext(Context context, DJIUpDeviceType dJIUpDeviceType, DJIUpDeviceType dJIUpDeviceType2);

    public abstract void stop(boolean z);

    public void setUpCollectorListener(UpgradeStrategyContext upCollectorListener2) {
        this.upCollectorListener = upCollectorListener2;
    }
}

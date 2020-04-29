package dji.dbox.upgrade.strategy;

import android.content.Context;
import dji.dbox.upgrade.collectpacks.UpBaseCollector;
import dji.dbox.upgrade.collectpacks.UpCollectorFactory;
import dji.dbox.upgrade.collectpacks.UpCollectorListener;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class McOnly extends AbstractStrategy {
    /* access modifiers changed from: private */
    public UpBaseCollector collector1;

    public void doNext(Context context, DJIUpDeviceType device1, DJIUpDeviceType device2) {
        this.collector1 = UpCollectorFactory.build(context, device1);
        this.collector1.setDJIUpServerManager(new DJIUpServerManager(context, device1.toString()));
        DJIUpStatusHelper.setRollBackStatus(this.collector1.getStatus());
        DJIUpStatusHelper.setMainPageStatus(this.collector1.getStatus());
        this.collector1.setCollectorListener(new UpCollectorListener() {
            /* class dji.dbox.upgrade.strategy.McOnly.AnonymousClass1 */

            public void onStrategyCollectListOver() {
                DJIUpStatusHelper.setIsNeedLockGetted(McOnly.this.collector1.getStatus().isNeedLockGetted());
                DJIUpStatusHelper.setIsNeedLock(McOnly.this.collector1.getStatus().isNeedLock());
                McOnly.this.upCollectorListener.onStrategyCollectListOver();
            }

            public void onStrategyCollectVersionOver() {
                McOnly.this.upCollectorListener.onStrategyCollectVersionOver();
            }
        });
        this.collector1.startCollect();
    }

    public void stop(boolean needQuit) {
        if (this.collector1 != null) {
            this.collector1.stop(needQuit);
        }
    }
}

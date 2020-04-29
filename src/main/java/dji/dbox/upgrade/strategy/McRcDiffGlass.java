package dji.dbox.upgrade.strategy;

import android.content.Context;
import dji.dbox.upgrade.collectpacks.UpBaseCollector;
import dji.dbox.upgrade.collectpacks.UpCollectorFactory;
import dji.dbox.upgrade.collectpacks.UpCollectorListener;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.UpAsyncObjectsMonitor;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class McRcDiffGlass extends AbstractStrategy {
    /* access modifiers changed from: private */
    public UpBaseCollector collector1;
    /* access modifiers changed from: private */
    public UpBaseCollector collector2;

    public void doNext(Context context, final DJIUpDeviceType device1, final DJIUpDeviceType device2) {
        this.collector1 = UpCollectorFactory.build(context, device1);
        this.collector1.setDJIUpServerManager(new DJIUpServerManager(context, device1.toString()));
        this.collector2 = UpCollectorFactory.build(context, device2);
        this.collector2.setDJIUpServerManager(new DJIUpServerManager(context, device2.toString()));
        final UpAsyncObjectsMonitor.UpAsyncObject upAsyncList1 = this.collector1.getUpAsyncObject();
        final UpAsyncObjectsMonitor.UpAsyncObject upAsyncList2 = this.collector2.getUpAsyncObject();
        UpAsyncObjectsMonitor objectsMonitorList = new UpAsyncObjectsMonitor(upAsyncList1, upAsyncList2);
        final UpAsyncObjectsMonitor.UpAsyncObject upAsyncVersion1 = new UpAsyncObjectsMonitor.UpAsyncObject();
        final UpAsyncObjectsMonitor.UpAsyncObject upAsyncVersion2 = new UpAsyncObjectsMonitor.UpAsyncObject();
        UpAsyncObjectsMonitor objectsMonitorVersion = new UpAsyncObjectsMonitor(upAsyncVersion1, upAsyncVersion2);
        DJIUpStatusHelper.setRollBackStatus(this.collector2.getStatus());
        objectsMonitorList.setUpAsyncObjMonitorListener(new UpAsyncObjectsMonitor.UpAsyncObjMonitorListener() {
            /* class dji.dbox.upgrade.strategy.McRcDiffGlass.AnonymousClass1 */

            public void onMonitorStart() {
            }

            public void onMonitorOver() {
                boolean z;
                boolean z2 = false;
                boolean isNeedUpgrade1 = McRcDiffGlass.this.collector1.getStatus().isNeedUpgrade();
                boolean isNeedUpgrade2 = McRcDiffGlass.this.collector2.getStatus().isNeedUpgrade();
                DJIUpStatusHelper.setMainPageStatus(McRcDiffGlass.this.collector1.getStatus(), McRcDiffGlass.this.collector2.getStatus());
                DJIUpConstants.LOGD("", "onStrategyCollectListOver collector1=" + McRcDiffGlass.this.collector1.getStatus().getProductId() + " isNeedUpgrade=" + isNeedUpgrade1);
                DJIUpConstants.LOGD("", "onStrategyCollectListOver collector2=" + McRcDiffGlass.this.collector2.getStatus().getProductId() + " isNeedUpgrade=" + isNeedUpgrade2);
                if (McRcDiffGlass.this.collector1.getStatus().isNeedLockGetted() || McRcDiffGlass.this.collector2.getStatus().isNeedLockGetted()) {
                    z = true;
                } else {
                    z = false;
                }
                DJIUpStatusHelper.setIsNeedLockGetted(z);
                if (McRcDiffGlass.this.collector1.getStatus().isNeedLock() || McRcDiffGlass.this.collector2.getStatus().isNeedLock()) {
                    z2 = true;
                }
                DJIUpStatusHelper.setIsNeedLock(z2);
                McRcDiffGlass.this.upCollectorListener.onStrategyCollectListOver();
            }
        });
        objectsMonitorVersion.setUpAsyncObjMonitorListener(new UpAsyncObjectsMonitor.UpAsyncObjMonitorListener() {
            /* class dji.dbox.upgrade.strategy.McRcDiffGlass.AnonymousClass2 */

            public void onMonitorStart() {
            }

            public void onMonitorOver() {
                McRcDiffGlass.this.upCollectorListener.onStrategyCollectVersionOver();
            }
        });
        this.collector1.setCollectorListener(new UpCollectorListener() {
            /* class dji.dbox.upgrade.strategy.McRcDiffGlass.AnonymousClass3 */

            public void onStrategyCollectListOver() {
                DJIUpConstants.LOGD("", "onStrategyCollectListOver " + device1);
                upAsyncList1.stopMonitor();
            }

            public void onStrategyCollectVersionOver() {
                upAsyncVersion1.stopMonitor();
            }
        });
        this.collector2.setCollectorListener(new UpCollectorListener() {
            /* class dji.dbox.upgrade.strategy.McRcDiffGlass.AnonymousClass4 */

            public void onStrategyCollectListOver() {
                DJIUpConstants.LOGD("", "onStrategyCollectListOver " + device2);
                upAsyncList2.stopMonitor();
            }

            public void onStrategyCollectVersionOver() {
                upAsyncVersion2.stopMonitor();
            }
        });
        this.collector1.startCollect();
        this.collector2.startCollect();
    }

    public void stop(boolean needQuit) {
        if (this.collector1 != null) {
            this.collector1.stop(needQuit);
        }
        if (this.collector2 != null) {
            this.collector2.stop(needQuit);
        }
    }
}

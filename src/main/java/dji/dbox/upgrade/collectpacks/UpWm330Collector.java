package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.utils.DJIRequestDeviceCfg;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;

@EXClassNullAway
class UpWm330Collector extends UpAbstractCollector {
    /* access modifiers changed from: private */
    public DJIUpCfgModel acCfgModel;
    private DJIRequestDeviceCfg requestAcCfg;

    UpWm330Collector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
        this.status.setDeviceType(DeviceType.DM368);
        this.status.setDeviceId(1);
        this.requestAcCfg = new DJIRequestDeviceCfg(context, DeviceType.DM368, 1, productId, new DJIRequestDeviceCfg.DJIRequestCfgListener() {
            /* class dji.dbox.upgrade.collectpacks.UpWm330Collector.AnonymousClass1 */

            public void onSuccess(DJIUpCfgModel cfgModel) {
                DJIUpCfgModel unused = UpWm330Collector.this.acCfgModel = cfgModel;
                DJIUpStatusHelper.setFlycVersion(UpWm330Collector.this.acCfgModel.releaseVersion);
                DJIUpConstants.LOGD(UpWm330Collector.this.TAG, "getcfg flycVersion=" + DJIUpStatusHelper.getFlycVersion());
                UpWm330Collector.this.setCfgModel(UpWm330Collector.this.acCfgModel);
                UpWm330Collector.this.getDeviceVers();
                UpWm330Collector.this.collectorListener.onStrategyCollectVersionOver();
            }

            public void onFailed() {
            }
        });
    }

    /* access modifiers changed from: protected */
    public void startGetDeviceCfg() {
        super.startGetDeviceCfg();
        this.requestAcCfg.startGetDeviceCfg();
    }

    /* access modifiers changed from: protected */
    public void resetStatus() {
        super.resetStatus();
        this.requestAcCfg.resetStatus();
    }

    public void stop(boolean needQuit) {
        this.requestAcCfg.cancel();
        resetStatus();
        super.stop(needQuit);
    }
}

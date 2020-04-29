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
class UpRc001Collector extends UpAbstractParallelCollector {
    /* access modifiers changed from: private */
    public DJIUpCfgModel rcCfgModel;
    /* access modifiers changed from: private */
    public boolean rcCfgSetted;
    private DJIRequestDeviceCfg requestRcCfg;

    UpRc001Collector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
        this.status.setDeviceType(getDeviceType());
        this.status.setDeviceId(0);
        this.requestRcCfg = new DJIRequestDeviceCfg(context, getDeviceType(), 0, productId, new DJIRequestDeviceCfg.DJIRequestCfgListener() {
            /* class dji.dbox.upgrade.collectpacks.UpRc001Collector.AnonymousClass1 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.dbox.upgrade.collectpacks.UpRc001Collector.access$102(dji.dbox.upgrade.collectpacks.UpRc001Collector, boolean):boolean
             arg types: [dji.dbox.upgrade.collectpacks.UpRc001Collector, int]
             candidates:
              dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.access$102(dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector, java.util.List):java.util.List
              dji.dbox.upgrade.collectpacks.UpRc001Collector.access$102(dji.dbox.upgrade.collectpacks.UpRc001Collector, boolean):boolean */
            public void onSuccess(DJIUpCfgModel cfgModel) {
                DJIUpCfgModel unused = UpRc001Collector.this.rcCfgModel = cfgModel;
                DJIUpStatusHelper.setRcVersion(UpRc001Collector.this.rcCfgModel.releaseVersion);
                DJIUpConstants.LOGD(UpRc001Collector.this.TAG, "getcfg rcVersion=" + DJIUpStatusHelper.getRcVersion());
                boolean unused2 = UpRc001Collector.this.rcCfgSetted = true;
                UpRc001Collector.this.monitorCfgCallBack();
            }

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.dbox.upgrade.collectpacks.UpRc001Collector.access$102(dji.dbox.upgrade.collectpacks.UpRc001Collector, boolean):boolean
             arg types: [dji.dbox.upgrade.collectpacks.UpRc001Collector, int]
             candidates:
              dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.access$102(dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector, java.util.List):java.util.List
              dji.dbox.upgrade.collectpacks.UpRc001Collector.access$102(dji.dbox.upgrade.collectpacks.UpRc001Collector, boolean):boolean */
            public void onFailed() {
                boolean unused = UpRc001Collector.this.rcCfgSetted = true;
            }
        });
    }

    /* access modifiers changed from: protected */
    public DeviceType getDeviceType() {
        return DeviceType.DM368_G;
    }

    /* access modifiers changed from: private */
    public synchronized void monitorCfgCallBack() {
        if (this.rcCfgSetted) {
            setCfgModel(this.rcCfgModel);
            this.collectorListener.onStrategyCollectVersionOver();
        }
    }

    /* access modifiers changed from: protected */
    public void startGetDeviceCfg() {
        super.startGetDeviceCfg();
        if (DJIUpStatusHelper.isConnectRC()) {
            this.requestRcCfg.startGetDeviceCfg();
        } else {
            this.rcCfgSetted = true;
        }
    }

    /* access modifiers changed from: protected */
    public void resetStatus() {
        super.resetStatus();
        this.requestRcCfg.resetStatus();
        this.rcCfgSetted = false;
    }

    public void stop(boolean needQuit) {
        this.requestRcCfg.cancel();
        resetStatus();
        super.stop(needQuit);
    }

    public void initFirmwareGroup() {
        this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.AC);
        this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.RC);
        DJIUpConstants.LOGD("", "initFirmwareGroup groupKey=ALL");
    }
}

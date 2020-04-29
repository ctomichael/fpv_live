package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.utils.DJIRequestDeviceCfg;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.midware.data.config.P3.DeviceType;

abstract class UpRcParallelCollector extends UpAbstractParallelCollector {
    /* access modifiers changed from: private */
    public DJIUpCfgModel rcCfgModel;
    /* access modifiers changed from: private */
    public boolean rcCfgSettled;
    private DJIRequestDeviceCfg requestRcCfg;

    /* access modifiers changed from: protected */
    public abstract int getDeviceId();

    /* access modifiers changed from: protected */
    public abstract DeviceType getDeviceType();

    UpRcParallelCollector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
        this.status.setDeviceType(getDeviceType());
        this.status.setDeviceId(getDeviceId());
        this.requestRcCfg = new DJIRequestDeviceCfg(context, getDeviceType(), getDeviceId(), productId, new DJIRequestDeviceCfg.DJIRequestCfgListener() {
            /* class dji.dbox.upgrade.collectpacks.UpRcParallelCollector.AnonymousClass1 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.dbox.upgrade.collectpacks.UpRcParallelCollector.access$102(dji.dbox.upgrade.collectpacks.UpRcParallelCollector, boolean):boolean
             arg types: [dji.dbox.upgrade.collectpacks.UpRcParallelCollector, int]
             candidates:
              dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.access$102(dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector, java.util.List):java.util.List
              dji.dbox.upgrade.collectpacks.UpRcParallelCollector.access$102(dji.dbox.upgrade.collectpacks.UpRcParallelCollector, boolean):boolean */
            public void onSuccess(DJIUpCfgModel cfgModel) {
                DJIUpCfgModel unused = UpRcParallelCollector.this.rcCfgModel = cfgModel;
                DJIUpStatusHelper.setRcVersion(UpRcParallelCollector.this.rcCfgModel.releaseVersion);
                DJIUpConstants.LOGD(UpRcParallelCollector.this.TAG, "getcfg rcVersion=" + DJIUpStatusHelper.getRcVersion());
                boolean unused2 = UpRcParallelCollector.this.rcCfgSettled = true;
                UpRcParallelCollector.this.monitorCfgCallBack();
            }

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.dbox.upgrade.collectpacks.UpRcParallelCollector.access$102(dji.dbox.upgrade.collectpacks.UpRcParallelCollector, boolean):boolean
             arg types: [dji.dbox.upgrade.collectpacks.UpRcParallelCollector, int]
             candidates:
              dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.access$102(dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector, java.util.List):java.util.List
              dji.dbox.upgrade.collectpacks.UpRcParallelCollector.access$102(dji.dbox.upgrade.collectpacks.UpRcParallelCollector, boolean):boolean */
            public void onFailed() {
                boolean unused = UpRcParallelCollector.this.rcCfgSettled = true;
            }
        });
    }

    /* access modifiers changed from: private */
    public synchronized void monitorCfgCallBack() {
        if (this.rcCfgSettled) {
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
            this.rcCfgSettled = true;
        }
    }

    /* access modifiers changed from: protected */
    public void resetStatus() {
        super.resetStatus();
        this.requestRcCfg.resetStatus();
        this.rcCfgSettled = false;
    }

    public void stop(boolean needQuit) {
        this.requestRcCfg.cancel();
        resetStatus();
        super.stop(needQuit);
    }

    public void initFirmwareGroup() {
        this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.RC);
        this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.AC);
        DJIUpConstants.LOGD("", "initFirmwareGroup groupKey=RC");
    }
}

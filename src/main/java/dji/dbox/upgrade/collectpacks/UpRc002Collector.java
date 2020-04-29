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
class UpRc002Collector extends UpAbstractWifiCollector {
    /* access modifiers changed from: private */
    public DJIUpCfgModel rcCfgModel;
    /* access modifiers changed from: private */
    public boolean rcCfgSetted;
    private DJIRequestDeviceCfg requestRcCfg;
    private DJIUpCfgModel smallCfgModel;

    UpRc002Collector(Context context, DJIUpDeviceType productId) {
        super(productId);
        this.status.setDeviceType(DeviceType.WIFI_G);
        this.status.setDeviceId(0);
        this.requestRcCfg = new DJIRequestDeviceCfg(context, DeviceType.WIFI_G, 0, productId, new DJIRequestDeviceCfg.DJIRequestCfgListener() {
            /* class dji.dbox.upgrade.collectpacks.UpRc002Collector.AnonymousClass1 */

            public void onSuccess(DJIUpCfgModel cfgModel) {
                DJIUpCfgModel unused = UpRc002Collector.this.rcCfgModel = cfgModel;
                DJIUpStatusHelper.setRcVersion(UpRc002Collector.this.rcCfgModel.releaseVersion);
                DJIUpConstants.LOGD(UpRc002Collector.this.TAG, "getcfg rcVersion=" + DJIUpStatusHelper.getRcVersion());
                boolean unused2 = UpRc002Collector.this.rcCfgSetted = true;
                UpRc002Collector.this.monitorCfgCallBack();
            }

            public void onFailed() {
                boolean unused = UpRc002Collector.this.rcCfgSetted = true;
            }
        });
    }

    /* access modifiers changed from: private */
    public synchronized void monitorCfgCallBack() {
        if (this.rcCfgSetted) {
            pickSmallVersion();
            if (this.smallCfgModel != null) {
                setCfgModel(this.smallCfgModel);
                this.collectorListener.onStrategyCollectVersionOver();
            }
        }
    }

    private void pickSmallVersion() {
        this.smallCfgModel = this.rcCfgModel;
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
    public boolean isAllSetted() {
        return super.isAllSetted() && this.rcCfgSetted;
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
}

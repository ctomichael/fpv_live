package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.utils.DJIRequestDeviceCfg;
import dji.dbox.upgrade.p4.utils.DJIUpGlassUtil;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.dbox.upgrade.p4.utils.DJIUpgradeBaseUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.ServiceManager;

@EXClassNullAway
class UpWm220Collector extends UpAbstractCollector {
    /* access modifiers changed from: private */
    public DJIUpCfgModel acCfgModel;
    /* access modifiers changed from: private */
    public boolean acCfgSetted;
    /* access modifiers changed from: private */
    public DJIUpCfgModel glassCfgModel;
    /* access modifiers changed from: private */
    public boolean glassCfgSetted;
    /* access modifiers changed from: private */
    public DJIUpCfgModel rcCfgModel;
    /* access modifiers changed from: private */
    public boolean rcCfgSetted;
    private DJIRequestDeviceCfg requestAcCfg;
    private DJIRequestDeviceCfg requestGlassCfg;
    private DJIRequestDeviceCfg requestRcCfg;
    private DJIUpCfgModel smallCfgModel;

    UpWm220Collector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
        this.status.setDeviceType(DeviceType.DM368_G);
        this.status.setDeviceId(1);
        this.requestAcCfg = new DJIRequestDeviceCfg(context, DeviceType.DM368, 1, productId, new DJIRequestDeviceCfg.DJIRequestCfgListener() {
            /* class dji.dbox.upgrade.collectpacks.UpWm220Collector.AnonymousClass1 */

            public void onSuccess(DJIUpCfgModel cfgModel) {
                DJIUpCfgModel unused = UpWm220Collector.this.acCfgModel = cfgModel;
                DJIUpStatusHelper.setFlycVersion(UpWm220Collector.this.acCfgModel.releaseVersion);
                DJIUpConstants.LOGD(UpWm220Collector.this.TAG, "getcfg flycVersion=" + DJIUpStatusHelper.getFlycVersion());
                boolean unused2 = UpWm220Collector.this.acCfgSetted = true;
                UpWm220Collector.this.monitorCfgCallBack();
            }

            public void onFailed() {
                boolean unused = UpWm220Collector.this.acCfgSetted = true;
            }
        });
        this.requestRcCfg = new DJIRequestDeviceCfg(context, DeviceType.DM368_G, 1, productId, new DJIRequestDeviceCfg.DJIRequestCfgListener() {
            /* class dji.dbox.upgrade.collectpacks.UpWm220Collector.AnonymousClass2 */

            public void onSuccess(DJIUpCfgModel cfgModel) {
                DJIUpCfgModel unused = UpWm220Collector.this.rcCfgModel = cfgModel;
                DJIUpStatusHelper.setRcVersion(UpWm220Collector.this.rcCfgModel.releaseVersion);
                DJIUpConstants.LOGD(UpWm220Collector.this.TAG, "getcfg rcVersion=" + DJIUpStatusHelper.getRcVersion());
                boolean unused2 = UpWm220Collector.this.rcCfgSetted = true;
                UpWm220Collector.this.monitorCfgCallBack();
            }

            public void onFailed() {
                boolean unused = UpWm220Collector.this.rcCfgSetted = true;
            }
        });
        this.requestGlassCfg = new DJIRequestDeviceCfg(context, DeviceType.GLASS, 1, productId, new DJIRequestDeviceCfg.DJIRequestCfgListener() {
            /* class dji.dbox.upgrade.collectpacks.UpWm220Collector.AnonymousClass3 */

            public void onSuccess(DJIUpCfgModel cfgModel) {
                DJIUpCfgModel unused = UpWm220Collector.this.glassCfgModel = cfgModel;
                DJIUpStatusHelper.setGlassVersion(UpWm220Collector.this.glassCfgModel.releaseVersion);
                DJIUpConstants.LOGE(UpWm220Collector.this.TAG, "getWM220cfg glassVersion=" + DJIUpStatusHelper.getGlassVersion() + " glassType=" + DJIUpGlassUtil.getGlassType());
                boolean unused2 = UpWm220Collector.this.glassCfgSetted = true;
                UpWm220Collector.this.monitorCfgCallBack();
            }

            public void onFailed() {
                boolean unused = UpWm220Collector.this.glassCfgSetted = true;
            }
        });
        this.requestGlassCfg.setStartLen(200);
    }

    /* access modifiers changed from: private */
    public synchronized void monitorCfgCallBack() {
        if (this.rcCfgSetted && this.acCfgSetted && this.glassCfgSetted) {
            pickSmallVersion();
            if (this.smallCfgModel != null) {
                setCfgModel(this.smallCfgModel);
                getDeviceVers();
                onCollectVersionOver();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCollectVersionOver() {
        super.onCollectVersionOver();
        this.collectorListener.onStrategyCollectVersionOver();
    }

    private void pickSmallVersion() {
        this.smallCfgModel = null;
        getSmallerCfg(this.acCfgModel);
        getSmallerCfg(this.rcCfgModel);
        if (DJIUpGlassUtil.isUpgradeSame()) {
            getSmallerCfg(this.glassCfgModel);
        }
    }

    private void getSmallerCfg(DJIUpCfgModel cfgModel) {
        if (this.smallCfgModel == null) {
            if (cfgModel != null) {
                this.smallCfgModel = cfgModel;
            }
        } else if (cfgModel != null && DJIUpgradeBaseUtils.compareFirVer(this.smallCfgModel.releaseVersion, cfgModel.releaseVersion) >= 0) {
            this.smallCfgModel = cfgModel;
        }
    }

    /* access modifiers changed from: protected */
    public void startGetDeviceCfg() {
        super.startGetDeviceCfg();
        if (DJIUpStatusHelper.isConnectMC() || ServiceManager.getInstance().isRemoteOK()) {
            this.requestAcCfg.startGetDeviceCfg();
        } else {
            this.acCfgSetted = true;
        }
        if (DJIUpGlassUtil.isUpgradeSame()) {
            this.requestGlassCfg.startGetDeviceCfg();
        } else {
            this.glassCfgSetted = true;
        }
        if (DJIUpStatusHelper.isConnectRC()) {
            this.requestRcCfg.startGetDeviceCfg();
        } else {
            this.rcCfgSetted = true;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isAllSetted() {
        return super.isAllSetted() && this.rcCfgSetted && this.glassCfgSetted;
    }

    /* access modifiers changed from: protected */
    public void resetStatus() {
        super.resetStatus();
        this.requestAcCfg.resetStatus();
        this.acCfgSetted = false;
        this.requestRcCfg.resetStatus();
        this.rcCfgSetted = false;
        this.requestGlassCfg.resetStatus();
        this.glassCfgSetted = false;
    }

    public void stop(boolean needQuit) {
        this.requestAcCfg.cancel();
        this.requestRcCfg.cancel();
        this.requestGlassCfg.cancel();
        resetStatus();
        super.stop(needQuit);
    }
}

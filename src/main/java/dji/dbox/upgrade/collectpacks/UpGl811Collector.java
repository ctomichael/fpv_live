package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpConstants;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.utils.DJIRequestDeviceCfg;
import dji.dbox.upgrade.p4.utils.DJIUpGlassUtil;
import dji.dbox.upgrade.p4.utils.DJIUpStatusHelper;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import java.util.ArrayList;

@EXClassNullAway
public class UpGl811Collector extends UpAbstractParallelCollector {
    /* access modifiers changed from: private */
    public DJIUpCfgModel glassCfgModel;
    /* access modifiers changed from: private */
    public boolean glassCfgSetted;
    private DJIRequestDeviceCfg requestGlassCfg;
    private DJIUpCfgModel smallCfgModel;

    public /* bridge */ /* synthetic */ ArrayList getGroupList() {
        return super.getGroupList();
    }

    public /* bridge */ /* synthetic */ void startCollect() {
        super.startCollect();
    }

    UpGl811Collector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
        this.status.setDeviceType(DeviceType.DM368_G);
        this.status.setDeviceId(1);
        this.requestGlassCfg = new DJIRequestDeviceCfg(context, DeviceType.GLASS, 1, productId, new DJIRequestDeviceCfg.DJIRequestCfgListener() {
            /* class dji.dbox.upgrade.collectpacks.UpGl811Collector.AnonymousClass1 */

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.dbox.upgrade.collectpacks.UpGl811Collector.access$102(dji.dbox.upgrade.collectpacks.UpGl811Collector, boolean):boolean
             arg types: [dji.dbox.upgrade.collectpacks.UpGl811Collector, int]
             candidates:
              dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.access$102(dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector, java.util.List):java.util.List
              dji.dbox.upgrade.collectpacks.UpGl811Collector.access$102(dji.dbox.upgrade.collectpacks.UpGl811Collector, boolean):boolean */
            public void onSuccess(DJIUpCfgModel cfgModel) {
                DJIUpCfgModel unused = UpGl811Collector.this.glassCfgModel = cfgModel;
                DJIUpStatusHelper.setGlassVersion(UpGl811Collector.this.glassCfgModel.releaseVersion);
                DJIUpConstants.LOGE(UpGl811Collector.this.TAG, "get811cfg glassVersion=" + DJIUpStatusHelper.getGlassVersion() + " glassType=" + DJIUpGlassUtil.getGlassType());
                boolean unused2 = UpGl811Collector.this.glassCfgSetted = true;
                UpGl811Collector.this.monitorCfgCallBack();
            }

            /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
             method: dji.dbox.upgrade.collectpacks.UpGl811Collector.access$102(dji.dbox.upgrade.collectpacks.UpGl811Collector, boolean):boolean
             arg types: [dji.dbox.upgrade.collectpacks.UpGl811Collector, int]
             candidates:
              dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector.access$102(dji.dbox.upgrade.collectpacks.UpAbstractParallelCollector, java.util.List):java.util.List
              dji.dbox.upgrade.collectpacks.UpGl811Collector.access$102(dji.dbox.upgrade.collectpacks.UpGl811Collector, boolean):boolean */
            public void onFailed() {
                boolean unused = UpGl811Collector.this.glassCfgSetted = true;
            }
        });
        this.requestGlassCfg.setStartLen(200);
    }

    public void initFirmwareGroup() {
        this.groupList.add(DJIUpCfgModel.DJIFirmwareGroup.GL);
        DJIUpConstants.LOGD("", "initFirmwareGroup groupKey=GL glassType = " + DJIUpGlassUtil.getGlassType());
    }

    /* access modifiers changed from: private */
    public synchronized void monitorCfgCallBack() {
        if (this.glassCfgSetted) {
            pickSmallVersion();
            if (this.smallCfgModel != null) {
                setCfgModel(this.smallCfgModel);
                this.collectorListener.onStrategyCollectVersionOver();
            }
        }
    }

    private void pickSmallVersion() {
        this.smallCfgModel = this.glassCfgModel;
    }

    /* access modifiers changed from: protected */
    public void startGetDeviceCfg() {
        super.startGetDeviceCfg();
        if (DJIUpGlassUtil.isUpgradeOwn()) {
            this.requestGlassCfg.startGetDeviceCfg();
        } else {
            this.glassCfgSetted = true;
        }
    }

    /* access modifiers changed from: protected */
    public boolean isAllSetted() {
        return super.isAllSetted() && this.glassCfgSetted;
    }

    /* access modifiers changed from: protected */
    public void resetStatus() {
        super.resetStatus();
        this.requestGlassCfg.resetStatus();
        this.glassCfgSetted = false;
    }

    public void stop(boolean needQuit) {
        this.requestGlassCfg.cancel();
        resetStatus();
        super.stop(needQuit);
    }
}

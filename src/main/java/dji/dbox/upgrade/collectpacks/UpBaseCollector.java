package dji.dbox.upgrade.collectpacks;

import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.DJIUpStatus;
import dji.dbox.upgrade.p4.server.DJIUpServerManager;
import dji.dbox.upgrade.p4.utils.UpAsyncObjectsMonitor;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;

@EXClassNullAway
public abstract class UpBaseCollector {
    protected String TAG = getClass().getSimpleName();
    UpCollectorListener collectorListener;
    protected final DJIUpDeviceType productId;
    DJIUpServerManager serverManager;
    protected DJIUpStatus status;
    private final UpAsyncObjectsMonitor.UpAsyncObject upAsyncObject;

    public abstract ArrayList<DJIUpCfgModel.DJIFirmwareGroup> getGroupList();

    public abstract void initFirmwareGroup();

    public abstract void startCollect();

    public abstract void stop(boolean z);

    UpBaseCollector(DJIUpDeviceType productId2) {
        this.productId = productId2;
        this.status = new DJIUpStatus(productId2);
        this.status.setCollector(this);
        this.upAsyncObject = new UpAsyncObjectsMonitor.UpAsyncObject();
    }

    public DJIUpStatus getStatus() {
        return this.status;
    }

    public UpAsyncObjectsMonitor.UpAsyncObject getUpAsyncObject() {
        return this.upAsyncObject;
    }

    public void setDJIUpServerManager(DJIUpServerManager serverManager2) {
        this.serverManager = serverManager2;
        this.status.setServerManager(serverManager2);
    }

    public void setCollectorListener(UpCollectorListener collectorListener2) {
        this.collectorListener = collectorListener2;
    }
}

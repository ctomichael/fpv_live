package dji.dbox.upgrade.collectpacks;

import android.content.Context;
import dji.dbox.upgrade.p4.constants.DJIUpDeviceType;
import dji.dbox.upgrade.p4.model.DJIUpCfgModel;
import dji.dbox.upgrade.p4.model.DJIUpListElement;
import dji.dbox.upgrade.p4.utils.DJISdBigPackageScanHelper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class UpWm335Collector extends UpWm220Collector {
    private DJISdBigPackageScanHelper mHelper;

    public /* bridge */ /* synthetic */ ArrayList getGroupList() {
        return super.getGroupList();
    }

    public /* bridge */ /* synthetic */ void startCollect() {
        super.startCollect();
    }

    public /* bridge */ /* synthetic */ void stop(boolean z) {
        super.stop(z);
    }

    UpWm335Collector(Context context, DJIUpDeviceType productId) {
        super(context, productId);
        this.mHelper = new DJISdBigPackageScanHelper(productId);
    }

    private void tryScanSdBigPackage() {
        if (this.mHelper.isSupportSdBigPackage()) {
            this.mHelper.startScan(new DJISdBigPackageScanHelper.Callback() {
                /* class dji.dbox.upgrade.collectpacks.UpWm335Collector.AnonymousClass1 */

                public void onScanResult(List<DJISdBigPackageScanHelper.SdTarModel> files) {
                    List<DJIUpListElement> upListElementList = new ArrayList<>();
                    for (DJISdBigPackageScanHelper.SdTarModel sdTarModel : files) {
                        upListElementList.add(UpWm335Collector.this.tarModelToListElement(sdTarModel));
                    }
                    UpWm335Collector.this.status.setVerList(upListElementList);
                    UpWm335Collector.this.status.setLatestElement((DJIUpListElement) upListElementList.get(upListElementList.size() - 1));
                    UpWm335Collector.this.status.setNeedUpgrade(true);
                    UpWm335Collector.this.stop(false);
                    UpWm335Collector.this.collectorListener.onStrategyCollectListOver();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public DJIUpListElement tarModelToListElement(DJISdBigPackageScanHelper.SdTarModel tar) {
        DJIUpListElement element = new DJIUpListElement();
        element.product_version = tar.product_version;
        element.isFromSDCard = true;
        element.sdTarPath = tar.tarPath;
        DJIUpListElement.ReleaseNote note = new DJIUpListElement.ReleaseNote();
        note.zh_cn = tar.releaseNote;
        element.release_note = note;
        DJIUpCfgModel upCfgModel = new DJIUpCfgModel();
        File tarFile = new File(tar.tarPath);
        if (tarFile.exists()) {
            upCfgModel.totalSize = (int) tarFile.length();
        }
        upCfgModel.releaseVersion = tar.product_version;
        upCfgModel.deviceId = tar.deviceId;
        element.cfgModel = upCfgModel;
        return element;
    }

    /* access modifiers changed from: protected */
    public void onCollectVersionOver() {
        super.onCollectVersionOver();
        tryScanSdBigPackage();
    }

    public void initFirmwareGroup() {
        super.initFirmwareGroup();
    }
}

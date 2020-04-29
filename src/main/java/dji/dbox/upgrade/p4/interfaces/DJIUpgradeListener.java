package dji.dbox.upgrade.p4.interfaces;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.model.P3.DataCommonGetPushUpgradeStatus;

public interface DJIUpgradeListener {

    @EXClassNullAway
    public enum UploadFailReason {
        Init,
        Enter,
        Quit,
        Pretrans,
        Transing
    }

    void onCollectDeviceCfgComplete();

    void onCollectFail(String str);

    void onCollectProductTypeComplete();

    void onCollectProductTypeStart();

    void onCollectVersionComplete();

    void onCollectVersionStart();

    void onUpgradeComplete();

    void onUpgradeFail(DataCommonGetPushUpgradeStatus.DJIUpgradeCompleteReason dJIUpgradeCompleteReason);

    void onUpgradeProgress(String str, int i);

    void onUploadComplete();

    void onUploadFail(UploadFailReason uploadFailReason, Ccode ccode);

    void onUploadProgress(int i);

    void onUploadStart();

    void onWaitTimeout();

    void onZipComplete();

    void onZipFail();

    void onZipProgress(int i);

    void onZipStart();
}

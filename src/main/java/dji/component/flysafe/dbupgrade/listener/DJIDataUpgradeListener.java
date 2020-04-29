package dji.component.flysafe.dbupgrade.listener;

import dji.fieldAnnotation.EXClassNullAway;

public interface DJIDataUpgradeListener {

    @EXClassNullAway
    public enum DataFailReason {
        DownloadErr,
        NetWorkErr,
        SignErr,
        Upload,
        LowPower,
        NotSupport,
        UploadOverErr,
        NotConnectUav,
        MOTOR_ON,
        PathNotAvailable,
        ALL_FLOW_TIMEOUT,
        NONE
    }

    void onDownload(int i);

    void onDownloadOver();

    void onFail(DataFailReason dataFailReason);

    void onStart();

    void onSuccess();

    void onUpgrade(int i);

    void onUpgradeStart();
}

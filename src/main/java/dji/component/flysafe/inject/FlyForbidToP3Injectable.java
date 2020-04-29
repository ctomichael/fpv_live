package dji.component.flysafe.inject;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataRcSetMaster;

@EXClassNullAway
public interface FlyForbidToP3Injectable {
    String getAppVersion();

    DataRcSetMaster.MODE getDeviceStatusConfig_CurRcMode();

    String getSnFromSnManager();

    boolean isDevelopPackage();

    boolean isInnerPackage();

    boolean isNewApp();

    void reportCrash(Throwable th);
}

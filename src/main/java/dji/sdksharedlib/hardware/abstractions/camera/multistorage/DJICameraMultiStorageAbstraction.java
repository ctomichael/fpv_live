package dji.sdksharedlib.hardware.abstractions.camera.multistorage;

import dji.common.util.CallbackUtils;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.sdksharedlib.hardware.abstractions.DJISDKCacheHWAbstraction;
import dji.sdksharedlib.hardware.abstractions.Setter;
import dji.sdksharedlib.hardware.abstractions.camera.FoldingDrone.DJICameraFoldingDroneXAbstraction;
import dji.sdksharedlib.keycatalog.CameraKeys;

@EXClassNullAway
public class DJICameraMultiStorageAbstraction extends DJICameraFoldingDroneXAbstraction {
    @Setter(CameraKeys.STORAGE_LOCATION)
    public void setStorageLocation(DataCameraSetStorageInfo.Storage value, DJISDKCacheHWAbstraction.InnerCallback callback) {
        DataCameraSetStorageInfo dataCameraSetStorageInfo = new DataCameraSetStorageInfo();
        dataCameraSetStorageInfo.setReceiverId(getReceiverIdByIndex());
        dataCameraSetStorageInfo.setStorageGet(false);
        dataCameraSetStorageInfo.setStorageLocation(value);
        dataCameraSetStorageInfo.start(CallbackUtils.defaultCB(callback));
    }
}

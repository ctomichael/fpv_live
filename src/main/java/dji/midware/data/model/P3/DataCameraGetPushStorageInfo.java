package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraGetStateInfo;
import dji.midware.data.model.P3.DataCameraSetStorageInfo;
import dji.midware.data.model.base.DJICameraDataBase;

@Keep
@EXClassNullAway
public class DataCameraGetPushStorageInfo extends DJICameraDataBase {
    public static final int STORAGE_BYTE_LENGTH = 18;
    public static final int STORAGE_DOUBLE_BYTE = 40;
    private static DataCameraGetPushStorageInfo instance = null;

    public static synchronized DataCameraGetPushStorageInfo getInstance() {
        DataCameraGetPushStorageInfo dataCameraGetPushStorageInfo;
        synchronized (DataCameraGetPushStorageInfo.class) {
            if (instance == null) {
                instance = new DataCameraGetPushStorageInfo();
                instance.isNeedPushLosed = true;
                instance.isRemoteModel = true;
            }
            dataCameraGetPushStorageInfo = instance;
        }
        return dataCameraGetPushStorageInfo;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public DataCameraSetStorageInfo.Storage getStorageLocation() {
        if (this._recData == null) {
            return DataCameraSetStorageInfo.Storage.SDCARD;
        }
        return DataCameraSetStorageInfo.Storage.find(((Integer) get(0, 1, Integer.class)).intValue() & 3);
    }

    public boolean getSDCardInsertState() {
        return (((Integer) get((getRecDataLen() + -18) + 1, 1, Integer.class)).intValue() & 1) == 1;
    }

    public DataCameraGetStateInfo.SDCardState getSDCardState() {
        if (DataCameraGetStateInfo.getInstance().getUsbState()) {
            return DataCameraGetStateInfo.SDCardState.USBConnected;
        }
        return DataCameraGetStateInfo.SDCardState.find((((Integer) get((getRecDataLen() - 18) + 1, 1, Integer.class)).intValue() >> 1) & 15);
    }

    public DataCameraGetStateInfo.SDCardState getInnerStorageStatus() {
        if (DataCameraGetStateInfo.getInstance().getUsbState()) {
            return DataCameraGetStateInfo.SDCardState.USBConnected;
        }
        if (getRecDataLen() < 40) {
            return DataCameraGetStateInfo.SDCardState.OTHER;
        }
        return DataCameraGetStateInfo.SDCardState.find((((Integer) get((getRecDataLen() - 36) + 1, 1, Integer.class)).intValue() >> 1) & 15);
    }

    public boolean getInnerStorageInsertStatus() {
        if (getRecDataLen() < 40) {
            return false;
        }
        return (((Integer) get((getRecDataLen() + -36) + 1, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getSDCardTotalSize() {
        return ((Integer) get(getRecDataLen() - 16, 4, Integer.class)).intValue();
    }

    public int getSDCardFreeSize() {
        return ((Integer) get(getRecDataLen() - 12, 4, Integer.class)).intValue();
    }

    public long getSDCardRemainedShots() {
        return ((Long) get(getRecDataLen() - 8, 4, Long.class)).longValue();
    }

    public int getSDCardRemainedTime() {
        return ((Integer) get(getRecDataLen() - 4, 4, Integer.class)).intValue();
    }

    public int getInnerStorageTotalSize() {
        return ((Integer) get((getRecDataLen() - 18) - 16, 4, Integer.class)).intValue();
    }

    public int getInnerStorageFreeSize() {
        return ((Integer) get((getRecDataLen() - 18) - 12, 4, Integer.class)).intValue();
    }

    public long getInnerStorageRemainedShots() {
        return ((Long) get((getRecDataLen() - 18) - 8, 4, Long.class)).longValue();
    }

    public int getInnerStorageRemainedTime() {
        return ((Integer) get((getRecDataLen() - 18) - 4, 4, Integer.class)).intValue();
    }
}

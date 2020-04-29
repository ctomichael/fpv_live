package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.SparseArray;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetPushUpgradeStatus;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraGetPushRequestUpgrade extends DataBase {
    private static DataCameraGetPushRequestUpgrade instance = null;
    private SparseArray<UpgradeRequestModel> list = new SparseArray<>();

    @Keep
    public static class UpgradeRequestModel {
        public DeviceType deviceType;
        public long size;
        public DataCameraGetPushUpgradeStatus.FirmwareType type;
        public String version;
    }

    public static synchronized DataCameraGetPushRequestUpgrade getInstance() {
        DataCameraGetPushRequestUpgrade dataCameraGetPushRequestUpgrade;
        synchronized (DataCameraGetPushRequestUpgrade.class) {
            if (instance == null) {
                instance = new DataCameraGetPushRequestUpgrade();
            }
            dataCameraGetPushRequestUpgrade = instance;
        }
        return dataCameraGetPushRequestUpgrade;
    }

    public SparseArray<UpgradeRequestModel> getList() {
        if (this._recData == null) {
            return this.list;
        }
        int size = this._recData.length / 10;
        for (int i = 0; i < size; i++) {
            UpgradeRequestModel model = new UpgradeRequestModel();
            model.deviceType = DeviceType.find(((Integer) get((i * 10) + 0, 1, Integer.class)).intValue());
            model.type = DataCameraGetPushUpgradeStatus.FirmwareType.find(((Integer) get((i * 10) + 1, 1, Integer.class)).intValue());
            model.version = "v " + BytesUtil.byte2hex(this._recData[(i * 10) + 2]) + "." + BytesUtil.byte2hex(this._recData[(i * 10) + 3]) + "." + BytesUtil.byte2hex(this._recData[(i * 10) + 4]) + "." + BytesUtil.byte2hex(this._recData[(i * 10) + 5]);
            model.size = ((Long) get((i * 10) + 6, 4, Long.class)).longValue();
            this.list.append(i, model);
        }
        return this.list;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}

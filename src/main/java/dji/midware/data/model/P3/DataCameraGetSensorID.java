package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraGetSensorID extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetSensorID instance = null;

    public static synchronized DataCameraGetSensorID getInstance() {
        DataCameraGetSensorID dataCameraGetSensorID;
        synchronized (DataCameraGetSensorID.class) {
            if (instance == null) {
                instance = new DataCameraGetSensorID();
            }
            dataCameraGetSensorID = instance;
        }
        return dataCameraGetSensorID;
    }

    public int getSensorType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public String getSensorId() {
        int len = ((Integer) get(1, 1, Integer.class)).intValue();
        if (len > 0) {
            return getRevertSn(2, len);
        }
        return "";
    }

    /* access modifiers changed from: protected */
    public String getRevertSn(int from, int len) {
        StringBuilder sb = new StringBuilder(len);
        if (this._recData != null && from + len <= this._recData.length) {
            int i = from;
            while (i < from + len && 255 != this._recData[i]) {
                if (BytesUtil.isNumberOrLetter(this._recData[i])) {
                    sb.append((char) this._recData[i]);
                } else {
                    sb.append((int) this._recData[i]);
                }
                i++;
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        for (int i = 0; i < this._sendData.length; i++) {
            this._sendData[i] = 0;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetSSensorId.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}

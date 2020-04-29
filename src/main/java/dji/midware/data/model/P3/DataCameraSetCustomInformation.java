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

@Keep
@EXClassNullAway
public class DataCameraSetCustomInformation extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetCustomInformation instance = null;
    private byte[] information = null;

    public static synchronized DataCameraSetCustomInformation getInstance() {
        DataCameraSetCustomInformation dataCameraSetCustomInformation;
        synchronized (DataCameraSetCustomInformation.class) {
            if (instance == null) {
                instance = new DataCameraSetCustomInformation();
            }
            dataCameraSetCustomInformation = instance;
        }
        return dataCameraSetCustomInformation;
    }

    public DataCameraSetCustomInformation setInformation(byte[] information2) {
        this.information = information2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int infoLength = 0;
        if (this.information != null) {
            infoLength = this.information.length;
        }
        if (infoLength > 32) {
            infoLength = 32;
        }
        this._sendData = new byte[(infoLength + 2)];
        this._sendData[0] = 0;
        this._sendData[1] = (byte) infoLength;
        System.arraycopy(this.information, 0, this._sendData, 2, infoLength);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetUserCustomInfo.value();
        start(pack, callBack);
    }
}

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
public class DataCameraGetFileParams extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetFileParams instance = null;
    private ParamsType paramsType;

    public static synchronized DataCameraGetFileParams getInstance() {
        DataCameraGetFileParams dataCameraGetFileParams;
        synchronized (DataCameraGetFileParams.class) {
            if (instance == null) {
                instance = new DataCameraGetFileParams();
            }
            dataCameraGetFileParams = instance;
        }
        return dataCameraGetFileParams;
    }

    public DataCameraGetFileParams setType(ParamsType paramsType2) {
        this.paramsType = paramsType2;
        return this;
    }

    public int getMaxFolderNum() {
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }

    public int getFolderId() {
        return ((Integer) get(2, 2, Integer.class)).intValue();
    }

    public int getMaxFileNum() {
        return ((Integer) get(4, 2, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.paramsType.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetFileParams.value();
        start(pack, callBack);
    }

    @Keep
    public enum ParamsType {
        DCF(0),
        CLIP(1);
        
        private int value;

        private ParamsType(int value2) {
            this.value = value2;
        }

        public int value() {
            return this.value;
        }
    }
}

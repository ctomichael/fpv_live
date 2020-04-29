package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetPushRawNewParam;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraSetRawNewParam extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetRawNewParam instance = null;
    private CMD_TYPE cmdType;
    private int eiValue;
    private int proxyLooks;
    private DataCameraGetPushRawNewParam.DJIShootingMode shootingMode;

    public static synchronized DataCameraSetRawNewParam getInstance() {
        DataCameraSetRawNewParam dataCameraSetRawNewParam;
        synchronized (DataCameraSetRawNewParam.class) {
            if (instance == null) {
                instance = new DataCameraSetRawNewParam();
            }
            dataCameraSetRawNewParam = instance;
        }
        return dataCameraSetRawNewParam;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        switch (this.cmdType) {
            case ShootMode:
                this._sendData = new byte[3];
                this._sendData[0] = (byte) this.cmdType.id;
                this._sendData[1] = (byte) this._sendData.length;
                this._sendData[2] = (byte) this.shootingMode.getCmd();
                return;
            case EIValue:
                this._sendData = new byte[4];
                this._sendData[0] = (byte) this.cmdType.id;
                this._sendData[1] = (byte) this._sendData.length;
                System.arraycopy(BytesUtil.getBytes(this.eiValue), 0, this._sendData, 2, 2);
                return;
            case ProxyLook:
                this._sendData = new byte[3];
                this._sendData[0] = (byte) this.cmdType.id;
                this._sendData[1] = (byte) this._sendData.length;
                this._sendData[2] = (byte) this.proxyLooks;
                return;
            default:
                return;
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
        pack.cmdId = CmdIdCamera.CmdIdType.SetRawNewParam.value();
        pack.doPack();
        start(pack, callBack);
    }

    public DataCameraSetRawNewParam setShootingMode(DataCameraGetPushRawNewParam.DJIShootingMode shootingMode2) {
        this.shootingMode = shootingMode2;
        return this;
    }

    public DataCameraSetRawNewParam setEiValue(int eiValue2) {
        this.eiValue = eiValue2;
        return this;
    }

    public DataCameraSetRawNewParam setProxyLooks(int proxyLooks2) {
        this.proxyLooks = proxyLooks2;
        return this;
    }

    public DataCameraSetRawNewParam setCmdType(CMD_TYPE cmdType2) {
        this.cmdType = cmdType2;
        return this;
    }

    @Keep
    public enum CMD_TYPE {
        ShootMode(1),
        EIValue(2),
        ProxyLook(3);
        
        /* access modifiers changed from: private */
        public int id;

        private CMD_TYPE(int id2) {
            this.id = id2;
        }
    }
}

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
public class DataCameraSetTrackingParms extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetTrackingParms instance = null;
    private boolean isTrackingEnable;
    private int mXCoord;
    private int mYCoord;

    public static synchronized DataCameraSetTrackingParms getInstance() {
        DataCameraSetTrackingParms dataCameraSetTrackingParms;
        synchronized (DataCameraSetTrackingParms.class) {
            if (instance == null) {
                instance = new DataCameraSetTrackingParms();
            }
            dataCameraSetTrackingParms = instance;
        }
        return dataCameraSetTrackingParms;
    }

    public DataCameraSetTrackingParms setIsTrackingEnable(boolean isEnable) {
        this.isTrackingEnable = isEnable;
        return this;
    }

    public DataCameraSetTrackingParms setXCoord(int x) {
        this.mXCoord = x;
        return this;
    }

    public DataCameraSetTrackingParms setYCoord(int y) {
        this.mYCoord = y;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        this._sendData[0] = (byte) (this.isTrackingEnable ? 1 : 0);
        this._sendData[1] = (byte) (this.mXCoord & 255);
        this._sendData[2] = (byte) ((this.mXCoord & 65280) >> 8);
        this._sendData[3] = (byte) (this.mYCoord & 255);
        this._sendData[4] = (byte) ((this.mYCoord & 65280) >> 8);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetTrackingParms.value();
        start(pack, callBack);
    }
}

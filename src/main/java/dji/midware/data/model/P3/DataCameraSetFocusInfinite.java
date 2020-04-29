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
public class DataCameraSetFocusInfinite extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetFocusInfinite instance = null;
    private boolean isFocusInfinite;

    public static synchronized DataCameraSetFocusInfinite getInstance() {
        DataCameraSetFocusInfinite dataCameraSetFocusInfinite;
        synchronized (DataCameraSetFocusInfinite.class) {
            if (instance == null) {
                instance = new DataCameraSetFocusInfinite();
            }
            dataCameraSetFocusInfinite = instance;
        }
        return dataCameraSetFocusInfinite;
    }

    public DataCameraSetFocusInfinite setIsInfinite(boolean isLock) {
        this.isFocusInfinite = isLock;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 1;
        this._sendData = new byte[1];
        byte[] bArr = this._sendData;
        if (!this.isFocusInfinite) {
            i = 0;
        }
        bArr[0] = (byte) i;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetFocusInfinite.value();
        start(pack, callBack);
    }
}

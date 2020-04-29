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
public class DataCameraSetTapZoom extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetTapZoom instance = null;
    private boolean enabled;
    private int zoomScale = 1;

    public static synchronized DataCameraSetTapZoom getInstance() {
        DataCameraSetTapZoom dataCameraSetTapZoom;
        synchronized (DataCameraSetTapZoom.class) {
            if (instance == null) {
                instance = new DataCameraSetTapZoom();
            }
            dataCameraSetTapZoom = instance;
        }
        return dataCameraSetTapZoom;
    }

    public DataCameraSetTapZoom setEnabled(boolean enabled2) {
        this.enabled = enabled2;
        return this;
    }

    public DataCameraSetTapZoom setMultiplier(int zoomScale2) {
        this.zoomScale = zoomScale2;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetTapZoom.value();
        pack.repeatTimes = 4;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i;
        this._sendData = new byte[2];
        byte[] bArr = this._sendData;
        if (this.enabled) {
            i = 1;
        } else {
            i = 0;
        }
        bArr[0] = (byte) i;
        this._sendData[1] = (byte) this.zoomScale;
    }
}

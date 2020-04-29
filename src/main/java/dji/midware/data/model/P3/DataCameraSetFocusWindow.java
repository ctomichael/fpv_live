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
public class DataCameraSetFocusWindow extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetFocusWindow instance = null;
    private int mPrecision = 0;
    private int mXAxisWindowNum = 1;
    private int mYAxisWindowNum = 1;

    public static synchronized DataCameraSetFocusWindow getInstance() {
        DataCameraSetFocusWindow dataCameraSetFocusWindow;
        synchronized (DataCameraSetFocusWindow.class) {
            if (instance == null) {
                instance = new DataCameraSetFocusWindow();
            }
            dataCameraSetFocusWindow = instance;
        }
        return dataCameraSetFocusWindow;
    }

    public DataCameraSetFocusWindow setWindowX(int x) {
        this.mXAxisWindowNum = x;
        return this;
    }

    public DataCameraSetFocusWindow setWindowY(int y) {
        this.mYAxisWindowNum = y;
        return this;
    }

    public DataCameraSetFocusWindow setPrecision(int precision) {
        this.mPrecision = precision;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.mXAxisWindowNum;
        this._sendData[1] = (byte) this.mYAxisWindowNum;
        this._sendData[2] = (byte) this.mPrecision;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetFocusWindow.value();
        start(pack, callBack);
    }
}

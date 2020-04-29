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
public class DataCameraSetNDFilter extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetNDFilter instance = null;
    private NDFilterMode ndFilterMode;

    public static synchronized DataCameraSetNDFilter getInstance() {
        DataCameraSetNDFilter dataCameraSetNDFilter;
        synchronized (DataCameraSetNDFilter.class) {
            if (instance == null) {
                instance = new DataCameraSetNDFilter();
            }
            dataCameraSetNDFilter = instance;
        }
        return dataCameraSetNDFilter;
    }

    public DataCameraSetNDFilter setNDFilter(NDFilterMode mode) {
        this.ndFilterMode = mode;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.ndFilterMode.getCMD();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetNDFilter.value();
        start(pack, callBack);
    }

    @Keep
    public enum NDFilterMode {
        Auto(0),
        On(1),
        Off(2),
        UNKNOWN(255);
        
        private int cmd;

        private NDFilterMode(int cmd2) {
            this.cmd = cmd2;
        }

        public int getCMD() {
            return this.cmd;
        }

        public static NDFilterMode find(int value) {
            NDFilterMode[] values = values();
            for (NDFilterMode mode : values) {
                if (mode.getCMD() == value) {
                    return mode;
                }
            }
            return UNKNOWN;
        }
    }
}

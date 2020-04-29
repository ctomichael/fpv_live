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
public class DataCameraSetOpticsZoomMode extends DataBase implements DJIDataSyncListener {
    private static final String TAG = "DataCameraSetOpticsZoomMode";
    private static DataCameraSetOpticsZoomMode instance = null;
    private int arg1 = -1;
    private int arg2 = -1;
    private OpticsZommMode mMode = OpticsZommMode.OTHER;
    private ZoomSpeed mZoomSpeed = ZoomSpeed.OTHER;

    public static synchronized DataCameraSetOpticsZoomMode getInstance() {
        DataCameraSetOpticsZoomMode dataCameraSetOpticsZoomMode;
        synchronized (DataCameraSetOpticsZoomMode.class) {
            if (instance == null) {
                instance = new DataCameraSetOpticsZoomMode();
            }
            dataCameraSetOpticsZoomMode = instance;
        }
        return dataCameraSetOpticsZoomMode;
    }

    public DataCameraSetOpticsZoomMode setOpticsZoomMode(OpticsZommMode mode, ZoomSpeed speed, int arg12, int arg22) {
        this.mMode = mode;
        this.mZoomSpeed = speed;
        this.arg1 = arg12;
        this.arg2 = arg22;
        return this;
    }

    public int getZoomType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getSetZoomSpeed() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getZoomDirection() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public int getZoomFocusLenthHigh() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetOpticsZoom.value();
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.mMode.getCmd();
        this._sendData[1] = (byte) this.mZoomSpeed.getCmd();
        this._sendData[2] = (byte) this.arg1;
        this._sendData[3] = (byte) this.arg2;
    }

    @Keep
    public enum OpticsZommMode {
        CONTINUOUS(1),
        SETZOOM(2),
        STOPZOOM(255),
        OTHER(100);
        
        private int mCmd;

        private OpticsZommMode(int cmd) {
            this.mCmd = cmd;
        }

        public int getCmd() {
            return this.mCmd;
        }

        public boolean _equals(int cmd) {
            return this.mCmd == cmd;
        }

        public static OpticsZommMode find(int cmd) {
            OpticsZommMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(cmd)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ZoomSpeed {
        SLOWEST(72),
        SLOW(73),
        MIDSLOW(74),
        MID(75),
        MIDFAST(76),
        FAST(77),
        FASTEST(78),
        OTHER(100);
        
        private int mCmd;

        private ZoomSpeed(int cmd) {
            this.mCmd = cmd;
        }

        public int getCmd() {
            return this.mCmd;
        }

        public boolean _equals(int cmd) {
            return this.mCmd == cmd;
        }

        public static ZoomSpeed find(int cmd) {
            ZoomSpeed result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(cmd)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

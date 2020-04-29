package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataCameraGetMode;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCameraGetStateInfo extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetStateInfo instance = null;

    public static synchronized DataCameraGetStateInfo getInstance() {
        DataCameraGetStateInfo dataCameraGetStateInfo;
        synchronized (DataCameraGetStateInfo.class) {
            if (instance == null) {
                instance = new DataCameraGetStateInfo();
            }
            dataCameraGetStateInfo = instance;
        }
        return dataCameraGetStateInfo;
    }

    public boolean getConnectState() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 1) == 1;
    }

    public boolean getUsbState() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 2) == 1;
    }

    public boolean getTimeSyncState() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 4) == 1;
    }

    public PhotoState getPhotoState() {
        return PhotoState.find(((Integer) get(0, 4, Integer.class)).intValue() & 56);
    }

    public boolean getRecordState() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 64) == 1;
    }

    public boolean getSensorState() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 128) == 1;
    }

    public boolean getSDCardInsertState() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 256) == 1;
    }

    public SDCardState getSDCardState() {
        return SDCardState.find(((Integer) get(0, 4, Integer.class)).intValue() & 7680);
    }

    public boolean getFirmUpgradeState() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 8192) == 1;
    }

    public FirmErrorType getFirmUpgradeErrorState() {
        return FirmErrorType.find(((Integer) get(0, 4, Integer.class)).intValue() & 49152);
    }

    public boolean getHotState() {
        return (((Integer) get(0, 4, Integer.class)).intValue() & 65536) == 1;
    }

    public DataCameraGetMode.MODE getMode() {
        return DataCameraGetMode.MODE.find(((Integer) get(4, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetStateInfo.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum FirmErrorType {
        NO(0),
        Nomatch(1),
        UpgradeError(2),
        OTHER(6);
        
        private int data;

        private FirmErrorType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FirmErrorType find(int b) {
            FirmErrorType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum SDCardState {
        Normal(0),
        None(1),
        Invalid(2),
        WriteProtection(3),
        Unformat(4),
        Formating(5),
        Illegal(6),
        Busy(7),
        Full(8),
        Slow(9),
        Unknow(10),
        IndexMax(11),
        Initialzing(12),
        ToFormat(13),
        TryToRecoverFile(14),
        BecomeSlow(15),
        USBConnected(99),
        OTHER(100);
        
        private int data;

        private SDCardState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SDCardState find(int b) {
            SDCardState result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum PhotoState {
        NO(0),
        Single(1),
        Multiple(2),
        Hdr(3),
        FullView(4),
        RawBurst(5),
        HyperLapse(6),
        OTHER(6);
        
        private int data;

        private PhotoState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static PhotoState find(int b) {
            PhotoState result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;

@Keep
@EXClassNullAway
public class DataCameraAckReceiveFiles extends DataBase implements DJIDataAsync2Listener {
    private static DataCameraAckReceiveFiles instance = null;
    private AckCcode ackCcode;

    public static synchronized DataCameraAckReceiveFiles getInstance() {
        DataCameraAckReceiveFiles dataCameraAckReceiveFiles;
        synchronized (DataCameraAckReceiveFiles.class) {
            if (instance == null) {
                instance = new DataCameraAckReceiveFiles();
            }
            dataCameraAckReceiveFiles = instance;
        }
        return dataCameraAckReceiveFiles;
    }

    public int getFileType() {
        return ((Integer) get(8, 1, Integer.class)).intValue();
    }

    public long getFileSize() {
        return ((Long) get(0, 8, Long.class)).longValue();
    }

    public long getCreateTime() {
        return ((Long) get(9, 4, Long.class)).longValue();
    }

    public String getMD5() {
        return get(8, 16);
    }

    public int getIsAllPath() {
        return ((Integer) get(24, 1, Integer.class)).intValue();
    }

    public String getFileName() {
        return get(25, ((this._recData.length - 8) - 16) - 1);
    }

    public DataCameraAckReceiveFiles setAckCcode(AckCcode ackCcode2) {
        this.ackCcode = ackCcode2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.ackCcode.value();
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.ACK.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.AckReceiveFiles.value();
        if (this.pack != null) {
            pack.seq = this.pack.seq;
            start(pack);
        }
    }

    @Keep
    public enum AckCcode {
        Success(0),
        UnableReceive(34),
        NoMemory(35),
        NoSupport(224),
        OTHER(100);
        
        private int data;

        private AckCcode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static AckCcode find(int b) {
            AckCcode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataOsdSetSdrAssitantRead extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetSdrAssitantRead instance = null;
    private int address = 0;
    private SdrCpuType cpuType = SdrCpuType.CP_A7;
    private SdrDataType dataType = SdrDataType.Int_Data;
    private SdrDeviceType deviceType = SdrDeviceType.Sky;

    public static synchronized DataOsdSetSdrAssitantRead getInstance() {
        DataOsdSetSdrAssitantRead dataOsdSetSdrAssitantRead;
        synchronized (DataOsdSetSdrAssitantRead.class) {
            if (instance == null) {
                instance = new DataOsdSetSdrAssitantRead();
            }
            dataOsdSetSdrAssitantRead = instance;
        }
        return dataOsdSetSdrAssitantRead;
    }

    public int getIntValue() {
        return ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public short getShortValue() {
        return ((Short) get(0, 2, Short.class)).shortValue();
    }

    public DataOsdSetSdrAssitantRead setSdrDeviceType(SdrDeviceType deviceType2) {
        this.deviceType = deviceType2;
        return this;
    }

    public DataOsdSetSdrAssitantRead setSdrCpuType(SdrCpuType cpuType2) {
        this.cpuType = cpuType2;
        return this;
    }

    public DataOsdSetSdrAssitantRead setSdrDataType(SdrDataType dataType2) {
        this.dataType = dataType2;
        return this;
    }

    public DataOsdSetSdrAssitantRead setAddress(int address2) {
        this.address = address2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[6];
        this._sendData[0] = (byte) this.cpuType.value();
        this._sendData[1] = (byte) this.dataType.value();
        System.arraycopy(BytesUtil.getBytes(this.address), 0, this._sendData, 2, 4);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        if (this.deviceType == SdrDeviceType.Sky) {
            pack.receiverType = DeviceType.OFDM.value();
        } else {
            pack.receiverType = DeviceType.OSD.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetSdrAssitantRead.value();
        start(pack, callBack);
    }

    @Keep
    public enum SdrDeviceType {
        Sky(0),
        Ground(1),
        OTHER(100);
        
        private int data;

        private SdrDeviceType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SdrDeviceType find(int b) {
            SdrDeviceType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum SdrCpuType {
        CP_A7(0),
        CP_X1643(1),
        CP_XC4210(2),
        AP(3),
        OTHER(100);
        
        private int data;

        private SdrCpuType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SdrCpuType find(int b) {
            SdrCpuType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum SdrDataType {
        Int_Data(0),
        Short_Data(1),
        Byte_Data(2),
        OTHER(100);
        
        private int data;

        private SdrDataType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SdrDataType find(int b) {
            SdrDataType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

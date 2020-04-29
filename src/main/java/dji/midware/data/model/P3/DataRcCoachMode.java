package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcCoachMode extends DataBase implements DJIDataSyncListener {
    private static DataRcCoachMode instance = null;
    private CoachMode mCoachMode = CoachMode.DISABLE;
    private OptMode mOptMode = OptMode.GET;
    private byte[] mSnBytes = null;

    public static synchronized DataRcCoachMode getInstance() {
        DataRcCoachMode dataRcCoachMode;
        synchronized (DataRcCoachMode.class) {
            if (instance == null) {
                instance = new DataRcCoachMode();
            }
            dataRcCoachMode = instance;
        }
        return dataRcCoachMode;
    }

    public DataRcCoachMode setOptMode(OptMode mode) {
        this.mOptMode = mode;
        return this;
    }

    public DataRcCoachMode setCoachMode(CoachMode mode) {
        this.mCoachMode = mode;
        return this;
    }

    public DataRcCoachMode setSnBytes(byte[] bytes) {
        this.mSnBytes = bytes;
        return this;
    }

    public CoachMode getCoachMode() {
        return CoachMode.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 8;
        this._sendData = new byte[10];
        this._sendData[0] = (byte) this.mOptMode.value();
        if (this.mOptMode == OptMode.SET) {
            this._sendData[1] = (byte) this.mCoachMode.value();
        } else if (this.mOptMode == OptMode.ACTIVE && this.mSnBytes != null && this.mSnBytes.length > 0) {
            byte[] bArr = this.mSnBytes;
            byte[] bArr2 = this._sendData;
            if (this.mSnBytes.length <= 8) {
                i = this.mSnBytes.length;
            }
            System.arraycopy(bArr, 0, bArr2, 2, i);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.CoachMode.value();
        start(pack, callBack);
    }

    @Keep
    public enum CoachMode {
        UNACTIVE(0),
        DISABLE(1),
        ENABLE(2);
        
        private int _value = 0;

        private CoachMode(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static CoachMode find(int value) {
            CoachMode[] values = values();
            for (CoachMode arg : values) {
                if (arg._equals(value)) {
                    return arg;
                }
            }
            return DISABLE;
        }
    }

    @Keep
    public enum OptMode {
        GET(0),
        SET(1),
        ACTIVE(2);
        
        private int _value = 0;

        private OptMode(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }

        private boolean _equals(int value) {
            return this._value == value;
        }

        public static OptMode find(int value) {
            OptMode[] values = values();
            for (OptMode arg : values) {
                if (arg._equals(value)) {
                    return arg;
                }
            }
            return GET;
        }
    }
}

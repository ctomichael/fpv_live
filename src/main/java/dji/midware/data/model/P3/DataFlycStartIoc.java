package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataFlycStartIoc extends DataBase implements DJIDataSyncListener {
    private static DataFlycStartIoc instance = null;
    private IOCType mIocType;

    public static synchronized DataFlycStartIoc getInstance() {
        DataFlycStartIoc dataFlycStartIoc;
        synchronized (DataFlycStartIoc.class) {
            if (instance == null) {
                instance = new DataFlycStartIoc();
            }
            dataFlycStartIoc = instance;
        }
        return dataFlycStartIoc;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataFlycStartIoc setMode(IOCType type) {
        this.mIocType = type;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mIocType.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.StartIoc.value();
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    @Keep
    public enum IOCType {
        IOCTypeCourseLock(1),
        IOCTypeHomeLock(2),
        IOCTripod(3),
        IOCTypeHomeLockA2(4),
        Cinematic(4),
        IOCTypeOther(100);
        
        private int data;

        private IOCType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static IOCType find(int b) {
            IOCType result = IOCTypeOther;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

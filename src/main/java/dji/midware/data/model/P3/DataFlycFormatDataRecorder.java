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
public class DataFlycFormatDataRecorder extends DataBase implements DJIDataSyncListener {
    private static DataFlycFormatDataRecorder instance = null;
    private FORMAT_ACTION mFormatAction = FORMAT_ACTION.FORMAT;

    public static synchronized DataFlycFormatDataRecorder getInstance() {
        DataFlycFormatDataRecorder dataFlycFormatDataRecorder;
        synchronized (DataFlycFormatDataRecorder.class) {
            if (instance == null) {
                instance = new DataFlycFormatDataRecorder();
            }
            dataFlycFormatDataRecorder = instance;
        }
        return dataFlycFormatDataRecorder;
    }

    public DataFlycFormatDataRecorder setAction(FORMAT_ACTION action) {
        this.mFormatAction = action;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.FormatDataRecorder.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mFormatAction.value();
    }

    @Keep
    public enum FORMAT_ACTION {
        UNDEFINE(0),
        FORMAT(1),
        DELOLDFILES(2),
        TEST(3),
        OTHER(100);
        
        private int data;

        private FORMAT_ACTION(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FORMAT_ACTION find(int b) {
            FORMAT_ACTION result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

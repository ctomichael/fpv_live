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
public class DataRcSetAppSpecialControl extends DataBase implements DJIDataSyncListener {
    private static DataRcSetAppSpecialControl instance = null;
    private CmdType mCmdType = CmdType.SHIELD_CUSTOMKEY;
    private byte mValue = 0;

    public static synchronized DataRcSetAppSpecialControl getInstance() {
        DataRcSetAppSpecialControl dataRcSetAppSpecialControl;
        synchronized (DataRcSetAppSpecialControl.class) {
            if (instance == null) {
                instance = new DataRcSetAppSpecialControl();
            }
            dataRcSetAppSpecialControl = instance;
        }
        return dataRcSetAppSpecialControl;
    }

    public DataRcSetAppSpecialControl setCmdType(CmdType type) {
        this.mCmdType = type;
        return this;
    }

    public DataRcSetAppSpecialControl setValue(byte value) {
        this.mValue = value;
        return this;
    }

    public CmdType getCmdType() {
        return CmdType.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    public int getValue() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) this.mCmdType.value();
        this._sendData[1] = this.mValue;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.AppSpecailControl.value();
        pack.data = getSendData();
        pack.repeatTimes = 3;
        start(pack, callBack);
    }

    public boolean isCrack() {
        return ((Integer) get(1, 1, Integer.class)).intValue() != 0;
    }

    @Keep
    public enum CmdType {
        SHIELD_CUSTOMKEY(1),
        RC_VIBRATE(2),
        SET_RC_CRACK(5),
        GET_RC_CRACK(6),
        SET_GIMBAL_CONTROL(9),
        SET_AIRCRAFT_TYPE(14),
        GET_AIRCRAFT_TYPE(15),
        SET_RCPRO_CONFIG(16),
        GET_RCPRO_CONFIG(17),
        RC_PRO_SYNC(19),
        RC_PRO_RESET_TO_FACTORY(35),
        OTHER(100);
        
        private final int data;

        private CmdType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static CmdType find(int b) {
            CmdType result = SHIELD_CUSTOMKEY;
            CmdType[] values = values();
            for (CmdType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }

    @Keep
    public enum RcAircraftType {
        P3_ADVANCED(1),
        P3_PROFESSIONAL(2),
        P4(3),
        P4_ADVANCED(4),
        P4_PROFESSIONAL(5),
        IN2(6),
        OTHER(100);
        
        private final int data;

        private RcAircraftType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static RcAircraftType find(int b) {
            RcAircraftType result = P3_ADVANCED;
            RcAircraftType[] values = values();
            for (RcAircraftType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}

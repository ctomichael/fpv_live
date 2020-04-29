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
public class DataFlycFunctionControl extends DataBase implements DJIDataSyncListener {
    private static DataFlycFunctionControl instance = null;
    private FLYC_COMMAND type;

    public static synchronized DataFlycFunctionControl getInstance() {
        DataFlycFunctionControl dataFlycFunctionControl;
        synchronized (DataFlycFunctionControl.class) {
            if (instance == null) {
                instance = new DataFlycFunctionControl();
            }
            dataFlycFunctionControl = instance;
        }
        return dataFlycFunctionControl;
    }

    public DataFlycFunctionControl setCommand(FLYC_COMMAND type2) {
        this.type = type2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.type.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.FunctionControl.value();
        pack.repeatTimes = 3;
        start(pack, callBack);
    }

    @Keep
    public enum FLYC_COMMAND {
        AUTO_FLY(1),
        AUTO_LANDING(2),
        HOMEPOINT_NOW(3),
        HOMEPOINT_HOT(4),
        HOMEPOINT_LOC(5),
        GOHOME(6),
        START_MOTOR(7),
        STOP_MOTOR(8),
        Calibration(9),
        DeformProtecClose(10),
        DeformProtecOpen(11),
        DropGohome(12),
        DropTakeOff(13),
        DropLanding(14),
        DynamicHomePointOpen(15),
        DynamicHomePointClose(16),
        FollowFunctioonOpen(17),
        FollowFunctionClose(18),
        IOCOpen(19),
        IOCClose(20),
        DropCalibration(21),
        PackMode(22),
        UnPackMode(23),
        EnterManaualMode(24),
        StopDeform(25),
        DownDeform(28),
        UpDeform(29),
        ForceLanding(30),
        ForceLanding2(31),
        PRECISION_TAKE_OFF(34),
        MASS_CENTER_CALI(54),
        EXIT_MASS_CENTER_CALI(55),
        OTHER(100);
        
        private int data;

        private FLYC_COMMAND(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FLYC_COMMAND find(int b) {
            FLYC_COMMAND result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

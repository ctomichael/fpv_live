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
public class DataRcGetRcRole extends DataBase implements DJIDataSyncListener {
    private static DataRcGetRcRole instance = null;

    public static synchronized DataRcGetRcRole getInstance() {
        DataRcGetRcRole dataRcGetRcRole;
        synchronized (DataRcGetRcRole.class) {
            if (instance == null) {
                instance = new DataRcGetRcRole();
            }
            dataRcGetRcRole = instance;
        }
        return dataRcGetRcRole;
    }

    public RcRole getRole() {
        return RcRole.find(((Integer) get(0, 1, Integer.class)).intValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetRcRole.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum RcRole {
        MAIN_CONTROL(0),
        SLAVE_CONTROL(1),
        MAIN_CONTROL_SUB(2),
        SLAVE_CONTROL_SUB(3),
        OTHER(10);
        
        private int data;

        private RcRole(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static RcRole find(int b) {
            RcRole result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

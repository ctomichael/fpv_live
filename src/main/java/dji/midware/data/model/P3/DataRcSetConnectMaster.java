package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.Ccode;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataRcGetSlaveList;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataRcSetConnectMaster extends DataBase implements DJIDataSyncListener {
    private static DataRcSetConnectMaster instance = null;
    private DataRcGetSlaveList.RcModel master;

    public static synchronized DataRcSetConnectMaster getInstance() {
        DataRcSetConnectMaster dataRcSetConnectMaster;
        synchronized (DataRcSetConnectMaster.class) {
            if (instance == null) {
                instance = new DataRcSetConnectMaster();
            }
            dataRcSetConnectMaster = instance;
        }
        return dataRcSetConnectMaster;
    }

    public DataRcSetConnectMaster setMaster(DataRcGetSlaveList.RcModel master2) {
        this.master = master2;
        return this;
    }

    public RcConnectError getError(Ccode ccode) {
        return RcConnectError.find(ccode.relValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[12];
        System.arraycopy(BytesUtil.getBytes(this.master.id), 0, this._sendData, 0, 4);
        byte[] namebytes = BytesUtil.getBytes(this.master.name);
        System.arraycopy(namebytes, 0, this._sendData, 4, namebytes.length);
        System.arraycopy(BytesUtil.getUnsignedBytes(this.master.password), 0, this._sendData, 10, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetConnectMaster.value();
        start(pack, callBack);
    }

    @Keep
    public enum RcConnectError {
        WrongPwd(1),
        Refused(2),
        Exceed(3),
        TimeOut(4),
        OTHER(100);
        
        private int data;

        private RcConnectError(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static RcConnectError find(int b) {
            RcConnectError result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

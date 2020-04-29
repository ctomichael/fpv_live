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
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataFlycFaultInject extends DataBase implements DJIDataSyncListener {
    private static DataFlycFaultInject mInstance = null;
    private float break_time = 0.0f;
    private INJECT_CMD cmd = INJECT_CMD.FIT_CMD_STOP;
    private float fault_level = 0.0f;
    private int fault_type = 0;
    private int inject_method = 0;
    private float last_time = 0.0f;
    private int length = 32;
    private int module_index = 0;
    private int module_type = 0;
    private long repeat_num = 0;
    private float start_time = 0.0f;
    private int system_id = 0;
    private long version = 1;

    public static DataFlycFaultInject getInstance() {
        if (mInstance == null) {
            mInstance = new DataFlycFaultInject();
        }
        return mInstance;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public DataFlycFaultInject setCmd(INJECT_CMD cmd2) {
        this.cmd = cmd2;
        return this;
    }

    public DataFlycFaultInject setSystemId(int system_id2) {
        this.system_id = system_id2;
        return this;
    }

    public DataFlycFaultInject setModuleType(int module_type2) {
        this.module_type = module_type2;
        return this;
    }

    public DataFlycFaultInject setModuleIndex(int module_index2) {
        this.module_index = module_index2;
        return this;
    }

    public DataFlycFaultInject setInjectMethod(int inject_method2) {
        this.inject_method = inject_method2;
        return this;
    }

    public DataFlycFaultInject setFaultType(int fault_type2) {
        this.fault_type = fault_type2;
        return this;
    }

    public DataFlycFaultInject setFaultLevel(float fault_level2) {
        this.fault_level = fault_level2;
        return this;
    }

    public DataFlycFaultInject setStartTime(float start_time2) {
        this.start_time = start_time2;
        return this;
    }

    public DataFlycFaultInject setLastTime(float last_time2) {
        this.last_time = last_time2;
        return this;
    }

    public DataFlycFaultInject setBreakTime(float break_time2) {
        this.break_time = break_time2;
        return this;
    }

    public DataFlycFaultInject setRepeatNum(long repeat_num2) {
        this.repeat_num = repeat_num2;
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
        pack.cmdId = CmdIdFlyc.CmdIdType.FaultInject.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[32];
        BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.version), this._sendData, 0);
        BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.length), this._sendData, 4);
        this._sendData[6] = (byte) this.cmd.value();
        this._sendData[7] = (byte) this.system_id;
        this._sendData[8] = (byte) this.module_type;
        this._sendData[9] = (byte) this.module_index;
        this._sendData[10] = (byte) this.inject_method;
        this._sendData[11] = (byte) this.fault_type;
        BytesUtil.arraycopy(BytesUtil.getBytes(this.fault_level), this._sendData, 12);
        BytesUtil.arraycopy(BytesUtil.getBytes(this.start_time), this._sendData, 16);
        BytesUtil.arraycopy(BytesUtil.getBytes(this.last_time), this._sendData, 20);
        BytesUtil.arraycopy(BytesUtil.getBytes(this.break_time), this._sendData, 24);
        BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.repeat_num), this._sendData, 28);
    }

    @Keep
    public enum INJECT_CMD {
        FIT_CMD_STOP(1),
        FIT_CMD_OPEN(2),
        FIT_CMD_SEND(3);
        
        private int data;

        private INJECT_CMD(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static INJECT_CMD find(int b) {
            INJECT_CMD result = FIT_CMD_STOP;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

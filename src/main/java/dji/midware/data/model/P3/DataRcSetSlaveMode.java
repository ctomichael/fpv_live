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
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataRcSetSlaveMode extends DataBase implements DJIDataSyncListener {
    private static DataRcSetSlaveMode instance = null;
    private ArrayList<SlaveCustomModel> arrayList = new ArrayList<>(4);
    private ControlMode controlMode;

    public static synchronized DataRcSetSlaveMode getInstance() {
        DataRcSetSlaveMode dataRcSetSlaveMode;
        synchronized (DataRcSetSlaveMode.class) {
            if (instance == null) {
                instance = new DataRcSetSlaveMode();
            }
            dataRcSetSlaveMode = instance;
        }
        return dataRcSetSlaveMode;
    }

    public DataRcSetSlaveMode setControlType(ControlMode controlMode2) {
        this.controlMode = controlMode2;
        return this;
    }

    public DataRcSetSlaveMode setChannels(ArrayList<SlaveCustomModel> arrayList2) {
        this.arrayList = arrayList2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        this._sendData[0] = (byte) this.controlMode.value();
        if (this.controlMode.equals(ControlMode.Custom)) {
            for (int i = 0; i < this.arrayList.size(); i++) {
                SlaveCustomModel channelCustomModel = this.arrayList.get(i);
                if (channelCustomModel != null) {
                    this._sendData[i + 1] = (byte) ((channelCustomModel.direction << 7) | channelCustomModel.function);
                }
            }
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
        pack.cmdId = CmdIdRc.CmdIdType.SetSlaveMode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum ControlMode {
        Default(0),
        Custom(1),
        OTHER(100);
        
        private int data;

        private ControlMode(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ControlMode find(int b) {
            ControlMode result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum ModeFunction {
        None(0),
        Pitch(1),
        Roll(2),
        Yaw(3),
        OTHER(100);
        
        private int data;

        private ModeFunction(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ModeFunction find(int b) {
            ModeFunction result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public static class SlaveCustomModel {
        public int direction;
        public int function;

        public SlaveCustomModel() {
        }

        public SlaveCustomModel(int direction2, int function2) {
            this.direction = direction2;
            this.function = function2;
        }

        public SlaveCustomModel copy() {
            return new SlaveCustomModel(this.direction, this.function);
        }
    }
}

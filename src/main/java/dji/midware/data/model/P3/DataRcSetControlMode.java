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
public class DataRcSetControlMode extends DataBase implements DJIDataSyncListener {
    private static DataRcSetControlMode instance = null;
    private ArrayList<ChannelCustomModel> arrayList = new ArrayList<>(4);
    private ControlMode controlMode = ControlMode.Japan;
    private DeviceType mReceiverType = DeviceType.OSD;

    public DataRcSetControlMode(boolean isRegist) {
        super(isRegist);
    }

    public static synchronized DataRcSetControlMode getInstance() {
        DataRcSetControlMode dataRcSetControlMode;
        synchronized (DataRcSetControlMode.class) {
            if (instance == null) {
                instance = new DataRcSetControlMode(true);
            }
            dataRcSetControlMode = instance;
        }
        return dataRcSetControlMode;
    }

    public DataRcSetControlMode setControlType(ControlMode controlMode2) {
        this.controlMode = controlMode2;
        return this;
    }

    public ControlMode getControlType() {
        return this.controlMode;
    }

    public DataRcSetControlMode setChannels(ArrayList<ChannelCustomModel> arrayList2) {
        this.arrayList = arrayList2;
        return this;
    }

    public DataRcSetControlMode setReceiverType(DeviceType deviceType) {
        this.mReceiverType = deviceType;
        return this;
    }

    public void setDataToRecDataForRecord() {
        doPack();
        this._recData = this._sendData;
    }

    public int getControlModeForRecord() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[5];
        this._sendData[0] = (byte) this.controlMode.value();
        if (this.arrayList != null || this.controlMode.equals(ControlMode.Custom)) {
            for (int i = 0; i < this.arrayList.size(); i++) {
                ChannelCustomModel channelCustomModel = this.arrayList.get(i);
                if (channelCustomModel != null) {
                    this._sendData[i + 1] = (byte) ((channelCustomModel.direction << 7) | channelCustomModel.function);
                }
            }
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiverType == null ? DeviceType.OSD.value() : this.mReceiverType.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetControlMode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum ControlMode {
        Japan(1),
        America(2),
        China(3),
        Custom(4),
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
    public enum ChannelType {
        None(0),
        A(1),
        E(2),
        T(3),
        R(4),
        OTHER(100);
        
        private int data;

        private ChannelType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ChannelType find(int b) {
            ChannelType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public static class ChannelCustomModel {
        public int direction;
        public int function;

        public ChannelCustomModel() {
        }

        public ChannelCustomModel(int direction2, int function2) {
            this.direction = direction2;
            this.function = function2;
        }

        public ChannelCustomModel copy() {
            return new ChannelCustomModel(this.direction, this.function);
        }
    }
}

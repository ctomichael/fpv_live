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
public class DataFlycSetAndGetRedundancyIMUIndex extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetAndGetRedundancyIMUIndex mInstance = null;
    private CMD_ACTION cmdAction = CMD_ACTION.SET;
    private int deviceIndex = 0;
    private DEVICE_TYPE deviceType = DEVICE_TYPE.IMU;

    public static DataFlycSetAndGetRedundancyIMUIndex getInstance() {
        if (mInstance == null) {
            mInstance = new DataFlycSetAndGetRedundancyIMUIndex();
        }
        return mInstance;
    }

    public int getResult() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getCurIMUIndex() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public DataFlycSetAndGetRedundancyIMUIndex setDeviceIndex(int index) {
        this.deviceIndex = index;
        return this;
    }

    public DataFlycSetAndGetRedundancyIMUIndex setCmdAction(CMD_ACTION action) {
        this.cmdAction = action;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.cmdAction.value();
        this._sendData[1] = (byte) this.deviceType.value();
        this._sendData[2] = (byte) this.deviceIndex;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetAndGetRedundancyIMUIndex.value();
        start(pack, callBack);
    }

    @Keep
    public enum CMD_ACTION {
        GET(1),
        SET(2);
        
        private int mValue = 0;

        private CMD_ACTION(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        public boolean belongs(byte value) {
            return this.mValue == value;
        }

        public static CMD_ACTION ofValue(byte value) {
            CMD_ACTION[] values = values();
            for (CMD_ACTION ht : values) {
                if (ht.belongs(value)) {
                    return ht;
                }
            }
            return SET;
        }
    }

    @Keep
    public enum DEVICE_TYPE {
        IMU(6);
        
        private int mValue = 0;

        private DEVICE_TYPE(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }

        public boolean belongs(byte value) {
            return this.mValue == value;
        }

        public static DEVICE_TYPE ofValue(byte value) {
            DEVICE_TYPE[] values = values();
            for (DEVICE_TYPE ht : values) {
                if (ht.belongs(value)) {
                    return ht;
                }
            }
            return IMU;
        }
    }
}

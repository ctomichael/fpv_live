package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataEyeSetPerceptionGesture extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetPerceptionGesture instance = null;
    private CommandType command;
    private byte[] customData;
    private int value;

    public static synchronized DataEyeSetPerceptionGesture getInstance() {
        DataEyeSetPerceptionGesture dataEyeSetPerceptionGesture;
        synchronized (DataEyeSetPerceptionGesture.class) {
            if (instance == null) {
                instance = new DataEyeSetPerceptionGesture();
            }
            dataEyeSetPerceptionGesture = instance;
        }
        return dataEyeSetPerceptionGesture;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public DataEyeSetPerceptionGesture setCommand(CommandType command2) {
        this.command = command2;
        return this;
    }

    public DataEyeSetPerceptionGesture setValue(int value2) {
        this.value = value2;
        return this;
    }

    @Keep
    public enum CommandType {
        PERCEPTION_GESTURE(0, 1),
        CODEC_ENABLED(1, 1),
        LOG_ENABLED(2, 1),
        FLASH_LIGHT(3, 1),
        SINGLE_VISION_SENSOR(4, 1),
        UNKNOWN(255, 1);
        
        private Class classType;
        private int length;
        private int type;

        private CommandType(int type2, int length2) {
            this.type = type2;
            this.length = length2;
        }

        public int getLength() {
            return this.length;
        }

        public int getType() {
            return this.type;
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.command.getType();
        this._sendData[1] = (byte) this.command.getLength();
        switch (this.command) {
            case PERCEPTION_GESTURE:
            case CODEC_ENABLED:
            case LOG_ENABLED:
            case FLASH_LIGHT:
            case SINGLE_VISION_SENSOR:
                this._sendData[2] = (byte) this.value;
                return;
            default:
                return;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DOUBLE.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.SetPerceptionGesture.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }
}

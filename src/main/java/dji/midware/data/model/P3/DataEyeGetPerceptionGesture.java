package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataEyeSetPerceptionGesture;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataEyeGetPerceptionGesture extends DataBase implements DJIDataSyncListener {
    private DataEyeSetPerceptionGesture.CommandType command;
    private int value;

    public static DataEyeGetPerceptionGesture getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Keep
    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataEyeGetPerceptionGesture INSTANCE = new DataEyeGetPerceptionGesture();

        private SingletonHolder() {
        }
    }

    public DataEyeGetPerceptionGesture setCommand(DataEyeSetPerceptionGesture.CommandType command2) {
        this.command = command2;
        return this;
    }

    public DataEyeGetPerceptionGesture setValue(int value2) {
        this.value = value2;
        return this;
    }

    public int getType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getValue() {
        return ((Integer) get(2, 1, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DOUBLE.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.GetPerceptionGesture.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[3];
        this._sendData[0] = (byte) this.command.getType();
        this._sendData[1] = (byte) this.command.getLength();
        this._sendData[2] = (byte) this.value;
    }
}

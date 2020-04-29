package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdSpecial;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.media.DJIVideoDecoder;

public class DataSpecialLockRcControl extends DataBase implements DJIDataSyncListener {
    private static DataSpecialLockRcControl instance = null;
    private boolean isEnabled = false;
    private boolean isRecordButtonChosen = false;
    private boolean isShutterButtonChosen = false;

    public enum ButtonType {
        SHUTTER,
        RECORD
    }

    public static synchronized DataSpecialLockRcControl getInstance() {
        DataSpecialLockRcControl dataSpecialLockRcControl;
        synchronized (DataSpecialLockRcControl.class) {
            if (instance == null) {
                instance = new DataSpecialLockRcControl();
            }
            dataSpecialLockRcControl = instance;
        }
        return dataSpecialLockRcControl;
    }

    public DataSpecialLockRcControl setButtonToBeLocked(ButtonType buttonType) {
        if (buttonType == ButtonType.RECORD) {
            this.isRecordButtonChosen = true;
        }
        if (buttonType == ButtonType.SHUTTER) {
            this.isShutterButtonChosen = true;
        }
        return this;
    }

    public DataSpecialLockRcControl setEnabled(boolean enabled) {
        this.isEnabled = enabled;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.SPECIAL.value();
        pack.cmdId = CmdIdSpecial.CmdIdType.LockRcControl.value();
        pack.repeatTimes = 2;
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 0;
        this._sendData = new byte[2];
        int choose = 0;
        if (this.isRecordButtonChosen) {
            choose = 0 | 1;
        }
        if (this.isShutterButtonChosen) {
            choose |= 2;
        }
        this._sendData[0] = (byte) choose;
        byte[] bArr = this._sendData;
        if (this.isEnabled) {
            i = 1;
        }
        bArr[1] = (byte) i;
    }
}

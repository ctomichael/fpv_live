package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.diagnostics.model.DJIDiagnosticsError;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOnBoardSDK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.model.P3.DataOnBoardSdkSetIOState;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataOnBoardSdkGetIOState extends DataBase implements DJIDataSyncListener {
    private static DataOnBoardSdkGetIOState instance = null;
    private int iOIndex = 0;

    public static synchronized DataOnBoardSdkGetIOState getInstance() {
        DataOnBoardSdkGetIOState dataOnBoardSdkGetIOState;
        synchronized (DataOnBoardSdkGetIOState.class) {
            if (instance == null) {
                instance = new DataOnBoardSdkGetIOState();
            }
            dataOnBoardSdkGetIOState = instance;
        }
        return dataOnBoardSdkGetIOState;
    }

    public DataOnBoardSdkGetIOState setIOIndex(int iOIndex2) {
        this.iOIndex = iOIndex2;
        return this;
    }

    public boolean hasInitialized() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 4) != 0;
    }

    public DataOnBoardSdkSetIOState.IOProperty getProperty() {
        return DataOnBoardSdkSetIOState.IOProperty.find(((Integer) get(0, 1, Integer.class)).intValue() & 3);
    }

    public DataOnBoardSdkSetIOState.GPIOMode getGPIOMode() {
        return DataOnBoardSdkSetIOState.GPIOMode.find(((Integer) get(0, 1, Integer.class)).intValue() >> 4);
    }

    public boolean isHighElectricLevel() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getDutyRatio() {
        return ((Integer) get(1, 2, Integer.class)).intValue() & DJIDiagnosticsError.Camera.INTERNAL_STORAGE_INVALID;
    }

    public int getFrequency() {
        return (((Integer) get(1, 4, Integer.class)).intValue() & -1024) >> 10;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[2];
        this._sendData[0] = 1;
        this._sendData[1] = (byte) (this.iOIndex & 15);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverId = 7;
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OnboardSDK.value();
        pack.cmdId = CmdIdOnBoardSDK.CmdIdType.setExternalIO.value();
        start(pack, callBack);
    }
}

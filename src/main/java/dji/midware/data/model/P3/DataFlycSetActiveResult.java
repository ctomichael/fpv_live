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
public class DataFlycSetActiveResult extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetActiveResult instance = null;
    private DJIActivationState activationState;
    private String appCommKey;
    private int appId;
    private int appLevel;

    public static synchronized DataFlycSetActiveResult getInstance() {
        DataFlycSetActiveResult dataFlycSetActiveResult;
        synchronized (DataFlycSetActiveResult.class) {
            if (instance == null) {
                instance = new DataFlycSetActiveResult();
            }
            dataFlycSetActiveResult = instance;
        }
        return dataFlycSetActiveResult;
    }

    public DataFlycSetActiveResult setActivationState(DJIActivationState activationState2) {
        this.activationState = activationState2;
        return this;
    }

    public DataFlycSetActiveResult setAppId(int appId2) {
        this.appId = appId2;
        return this;
    }

    public DataFlycSetActiveResult setAppLevel(int appLevel2) {
        this.appLevel = appLevel2;
        return this;
    }

    public DataFlycSetActiveResult setAppCommKey(String appCommKey2) {
        this.appCommKey = appCommKey2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i = 32;
        this._sendData = new byte[44];
        System.arraycopy(BytesUtil.getBytes(this.activationState.value()), 0, this._sendData, 0, 4);
        int index = 0 + 4;
        System.arraycopy(BytesUtil.getBytes(this.appId), 0, this._sendData, index, 4);
        int index2 = index + 4;
        System.arraycopy(BytesUtil.getBytes(this.appLevel), 0, this._sendData, index2, 4);
        int index3 = index2 + 4;
        if (this.appCommKey.length() > 0) {
            byte[] namebytes = BytesUtil.hex2byte(this.appCommKey);
            byte[] bArr = this._sendData;
            if (namebytes.length < 32) {
                i = namebytes.length;
            }
            System.arraycopy(namebytes, 0, bArr, index3, i);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetActiveResult.value();
        start(pack, callBack);
    }

    @Keep
    public enum DJIActivationState {
        Success(0),
        NoNetwork(1),
        InvalidId(2),
        FailedForNet(3),
        OTHER(100);
        
        private int data;

        private DJIActivationState(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIActivationState find(int b) {
            DJIActivationState result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

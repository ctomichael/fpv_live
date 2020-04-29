package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdOnBoardSDK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public abstract class DataOnBoardSDKSetAccessoryCommonParams extends DataBase implements DJIDataSyncListener {
    protected AccessoryType accessoryType = AccessoryType.NAVIGATION_LED;
    private byte[] paramData;
    private int paramLength;
    protected int paramType;
    private DeviceType receiverType = DeviceType.OFDM;

    /* access modifiers changed from: protected */
    public abstract byte[] getParamData();

    /* access modifiers changed from: protected */
    public abstract DataOnBoardSDKSetAccessoryCommonParams setParamType(int i);

    public DataOnBoardSDKSetAccessoryCommonParams(AccessoryType type) {
        this.accessoryType = type;
        this.receiverID = 3;
    }

    public void setReceiverType(DeviceType receiverType2) {
        this.receiverType = receiverType2;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.receiverType == null ? DeviceType.OFDM.value() : this.receiverType.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OnboardSDK.value();
        pack.cmdId = CmdIdOnBoardSDK.CmdIdType.setAccessoryCommonParams.value();
        pack.repeatTimes = 3;
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i;
        this.paramData = getParamData();
        if (this.paramData != null) {
            i = this.paramData.length;
        } else {
            i = 0;
        }
        this.paramLength = i;
        this._sendData = new byte[(this.paramLength + 3)];
        this._sendData[0] = (byte) this.accessoryType.value();
        int index = 0 + 1;
        this._sendData[index] = (byte) this.paramType;
        int index2 = index + 1;
        this._sendData[index2] = (byte) this.paramLength;
        int index3 = index2 + 1;
        if (this.paramLength > 0) {
            System.arraycopy(this.paramData, 0, this._sendData, index3, this.paramLength);
        }
    }

    @Keep
    public enum AccessoryType {
        SEARCHLIGHT_LED(1),
        NAVIGATION_LED(2),
        SPEAKER(3);
        
        private final int mValue;

        private AccessoryType(int value) {
            this.mValue = value;
        }

        public int value() {
            return this.mValue;
        }
    }
}

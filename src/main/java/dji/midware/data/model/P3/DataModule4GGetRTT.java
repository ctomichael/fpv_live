package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdModule4G;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import java.io.UnsupportedEncodingException;

public class DataModule4GGetRTT extends DataBase implements DJIDataSyncListener {
    public static final int A114 = 7;
    public static final int AMAZON = 6;
    public static final int BAIDU = 1;
    public static final int DJI = 0;
    public static final int FACEBOOK = 5;
    public static final int GOOGLE = 4;
    public static final int QQ = 3;
    public static final int TAOBAO = 2;
    private boolean isRttFixed;
    private String mAddress;
    private int mFixedNum;
    private DeviceType mReceiver = DeviceType.OSD;

    @interface RttFixedAddressNum {
    }

    public DataModule4GGetRTT setRTTFixed(boolean fixed) {
        this.isRttFixed = fixed;
        return this;
    }

    public DataModule4GGetRTT setAddress(String address) {
        this.mAddress = address;
        return this;
    }

    public DataModule4GGetRTT setFixAddress(@RttFixedAddressNum int fixNum) {
        this.mFixedNum = fixNum;
        return this;
    }

    public DataModule4GGetRTT setReceiverType(DeviceType receiver) {
        this.mReceiver = receiver;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.isRttFixed) {
            this._sendData = new byte[2];
            this._sendData[0] = 0;
            this._sendData[1] = (byte) this.mFixedNum;
            return;
        }
        try {
            byte[] rttAddress = this.mAddress == null ? new byte[]{0} : this.mAddress.getBytes("UTF-8");
            int len = rttAddress.length + 2;
            this._sendData = new byte[len];
            this._sendData[0] = 1;
            this._sendData[1] = (byte) (len - 1);
            System.arraycopy(rttAddress, 0, this._sendData, 2, rttAddress.length);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiver == null ? DeviceType.OSD.value() : this.mReceiver.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.Module4G.value();
        pack.cmdId = CmdIdModule4G.CmdIdType.Get4GNetRTT.value();
        start(pack, callBack);
    }

    public int getRtt() {
        if (!isGetted()) {
            return -1;
        }
        return ((Integer) get(0, 2, Integer.class)).intValue();
    }
}

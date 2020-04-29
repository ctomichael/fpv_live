package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCommon;
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
public class DataCommonSetAppGpsCyclic extends DataBase implements DJIDataSyncListener {
    private float mHeight = -1.0f;
    private double mLatitude = -1.0d;
    private double mLongitude = -1.0d;
    private int mNum = -1;

    public DataCommonSetAppGpsCyclic setPushData(int num, double latitude, double longitude, float height) {
        this.mNum = num;
        this.mLatitude = latitude;
        this.mLongitude = longitude;
        this.mHeight = height;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.SetAppGpsCyclic.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[21];
        this._sendData[0] = (byte) this.mNum;
        System.arraycopy(BytesUtil.getBytes((this.mLongitude / 180.0d) * 3.141592653589793d), 0, this._sendData, 1, 8);
        System.arraycopy(BytesUtil.getBytes((this.mLatitude / 180.0d) * 3.141592653589793d), 0, this._sendData, 9, 8);
        System.arraycopy(BytesUtil.getBytes(this.mHeight), 0, this._sendData, 17, 4);
    }
}

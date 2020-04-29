package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdADS_B;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DataADSBGetLicenseId extends DataBase implements DJIDataSyncListener {
    private static DataADSBGetLicenseId mInstance = null;
    private List<Integer> mAreaIds = new ArrayList();
    private int mRepeatTime = -1;

    public static DataADSBGetLicenseId getInstance() {
        if (mInstance == null) {
            mInstance = new DataADSBGetLicenseId();
        }
        return mInstance;
    }

    public DataADSBGetLicenseId setAreaIds(List<Integer> areaIds) {
        this.mAreaIds = new ArrayList(areaIds);
        return this;
    }

    public int getLicenseId() {
        return ((Integer) get(1, 4, Integer.class)).intValue();
    }

    public DataADSBGetLicenseId setRepeatTime(int repeatTime) {
        this.mRepeatTime = repeatTime;
        return this;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[((this.mAreaIds.size() * 4) + 1)];
        this._sendData[0] = (byte) this.mAreaIds.size();
        for (int i = 0; i != this.mAreaIds.size(); i++) {
            System.arraycopy(BytesUtil.getBytes(this.mAreaIds.get(i).intValue()), 0, this._sendData, (i * 4) + 1, 4);
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        if (this.mRepeatTime != -1) {
            pack.repeatTimes = this.mRepeatTime;
            this.mRepeatTime = -1;
        }
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.timeOut = 4000;
        pack.cmdSet = CmdSet.ADS_B.value();
        pack.cmdId = CmdIdADS_B.CmdIdType.GetLicenseId.value();
        start(pack, callBack);
    }
}

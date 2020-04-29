package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.google.android.gms.common.ConnectionResult;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCenter;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataCenterSelfDischarge extends DataBase implements DJIDataSyncListener {
    private static DataCenterSelfDischarge instance;
    private int mDay = 0;
    private boolean mGet = true;

    public static synchronized DataCenterSelfDischarge getInstance() {
        DataCenterSelfDischarge dataCenterSelfDischarge;
        synchronized (DataCenterSelfDischarge.class) {
            if (instance == null) {
                instance = new DataCenterSelfDischarge();
            }
            dataCenterSelfDischarge = instance;
        }
        return dataCenterSelfDischarge;
    }

    public DataCenterSelfDischarge setFlag(boolean get) {
        this.mGet = get;
        return this;
    }

    public DataCenterSelfDischarge setDays(int day) {
        this.mDay = day;
        return this;
    }

    public int getDay() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        byte b;
        byte b2 = 0;
        this._sendData = new byte[2];
        byte[] bArr = this._sendData;
        if (this.mGet) {
            b = 0;
        } else {
            b = 1;
        }
        bArr[0] = b;
        byte[] bArr2 = this._sendData;
        if (!this.mGet) {
            b2 = (byte) this.mDay;
        }
        bArr2[1] = b2;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CENTER.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CENTER.value();
        pack.cmdId = CmdIdCenter.CmdIdType.SelfDischarge.value();
        pack.timeOut = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        start(pack, callBack);
    }
}

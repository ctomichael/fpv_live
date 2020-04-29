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
@Deprecated
public class DataFlycGetSetWarningAreaEnable extends DataBase implements DJIDataSyncListener {
    private static DataFlycGetSetWarningAreaEnable instance = null;
    private boolean isEnable = false;
    private boolean isToGetData = false;
    private int mAreaId = 0;

    public static synchronized DataFlycGetSetWarningAreaEnable getInstance() {
        DataFlycGetSetWarningAreaEnable dataFlycGetSetWarningAreaEnable;
        synchronized (DataFlycGetSetWarningAreaEnable.class) {
            if (instance == null) {
                instance = new DataFlycGetSetWarningAreaEnable();
            }
            dataFlycGetSetWarningAreaEnable = instance;
        }
        return dataFlycGetSetWarningAreaEnable;
    }

    public DataFlycGetSetWarningAreaEnable setToGetData(boolean toGetData) {
        this.isToGetData = toGetData;
        return this;
    }

    public DataFlycGetSetWarningAreaEnable setAreaId(int areaId) {
        this.mAreaId = areaId;
        return this;
    }

    public DataFlycGetSetWarningAreaEnable setEnable(boolean enable) {
        this.isEnable = enable;
        return this;
    }

    public long getAckAreaId() {
        return (long) ((Integer) get(0, 4, Integer.class)).intValue();
    }

    public boolean isAckAreaEnbale() {
        return ((Integer) get(4, 1, Integer.class)).intValue() == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[6];
        if (this.isToGetData) {
            this._sendData[0] = 0;
        } else {
            this._sendData[0] = 1;
        }
        System.arraycopy(BytesUtil.getBytes(this.mAreaId), 0, this._sendData, 1, 4);
        if (this.isEnable) {
            this._sendData[5] = 1;
        } else {
            this._sendData[5] = 0;
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
        pack.cmdId = CmdIdFlyc.CmdIdType.GetSetWarningAreaEnable.value();
        start(pack, callBack);
    }
}

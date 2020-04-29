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
public class DataFlycSetHomePoint extends DataBase implements DJIDataSyncListener {
    private static DataFlycSetHomePoint instance = null;
    private HOMETYPE mHomeType = HOMETYPE.RC;
    private byte mInterval = 0;
    private double mLantitue = 0.0d;
    private double mLongtitue = 0.0d;

    public static synchronized DataFlycSetHomePoint getInstance() {
        DataFlycSetHomePoint dataFlycSetHomePoint;
        synchronized (DataFlycSetHomePoint.class) {
            if (instance == null) {
                instance = new DataFlycSetHomePoint();
            }
            dataFlycSetHomePoint = instance;
        }
        return dataFlycSetHomePoint;
    }

    public DataFlycSetHomePoint setHomeType(HOMETYPE homeType) {
        this.mHomeType = homeType;
        return this;
    }

    public DataFlycSetHomePoint setGpsInfo(double latitue, double longtitue) {
        this.mLantitue = latitue;
        this.mLongtitue = longtitue;
        return this;
    }

    public DataFlycSetHomePoint setInerval(byte interval) {
        this.mInterval = interval;
        return this;
    }

    public int getResult() {
        if (((Integer) get(0, 1, Integer.class)).intValue() != 0) {
            return 1;
        }
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[18];
        this._sendData[0] = this.mHomeType.value();
        System.arraycopy(BytesUtil.getBytes(this.mLantitue), 0, this._sendData, 1, 8);
        System.arraycopy(BytesUtil.getBytes(this.mLongtitue), 0, this._sendData, 9, 8);
        this._sendData[17] = this.mInterval;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.SetHomePoint.value();
        start(pack, callBack);
    }

    @Keep
    public enum HOMETYPE {
        AIRCRAFT((byte) 0),
        RC((byte) 1),
        APP((byte) 2),
        FOLLOW((byte) 3);
        
        private byte mValue = 0;

        private HOMETYPE(byte value) {
            this.mValue = value;
        }

        public byte value() {
            return this.mValue;
        }

        public boolean belongs(byte value) {
            return this.mValue == value;
        }

        public static HOMETYPE ofValue(byte value) {
            HOMETYPE[] values = values();
            for (HOMETYPE ht : values) {
                if (ht.belongs(value)) {
                    return ht;
                }
            }
            return RC;
        }
    }
}

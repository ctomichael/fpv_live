package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcGetSimFlyStatus extends DataBase implements DJIDataSyncListener {
    private static DataRcGetSimFlyStatus instance = null;

    public static synchronized DataRcGetSimFlyStatus getInstance() {
        DataRcGetSimFlyStatus dataRcGetSimFlyStatus;
        synchronized (DataRcGetSimFlyStatus.class) {
            if (instance == null) {
                instance = new DataRcGetSimFlyStatus();
            }
            dataRcGetSimFlyStatus = instance;
        }
        return dataRcGetSimFlyStatus;
    }

    public FlySimStatus getFlySimStatus() {
        int data = 0;
        if (this._recData != null && this._recData.length > 0) {
            data = ((Integer) get(0, 1, Integer.class)).intValue();
        }
        return FlySimStatus.ofData(data);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.GetSimFlyStatus.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum FlySimStatus {
        NORMAL(0),
        FLY_SIM(1);
        
        private int mData = 0;

        private FlySimStatus(int data) {
            this.mData = data;
        }

        public int value() {
            return this.mData;
        }

        private boolean belongsTo(int data) {
            return this.mData == data;
        }

        public static FlySimStatus ofData(int data) {
            FlySimStatus[] values = values();
            for (FlySimStatus status : values) {
                if (status.belongsTo(data)) {
                    return status;
                }
            }
            return NORMAL;
        }
    }
}

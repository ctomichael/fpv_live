package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;

@Keep
public class DataAccessoryDeleteFile extends DataCameraDeleteFile {
    public static DataAccessoryDeleteFile getInstance() {
        return SingletonHolder.instance;
    }

    @Keep
    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataAccessoryDeleteFile instance = new DataAccessoryDeleteFile();

        private SingletonHolder() {
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OFDM.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.DeleteFile.value();
        pack.timeOut = 4000;
        start(pack, callBack);
    }
}

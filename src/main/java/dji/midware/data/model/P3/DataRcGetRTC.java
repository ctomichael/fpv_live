package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.Calendar;

public class DataRcGetRTC extends DataBase implements DJIDataSyncListener {
    public static DataRcGetRTC getInstance() {
        return Holder.INSTANCE;
    }

    private static class Holder {
        /* access modifiers changed from: private */
        public static DataRcGetRTC INSTANCE = new DataRcGetRTC();

        private Holder() {
        }
    }

    public long getTime() {
        byte b = this._recData[0];
        byte b2 = this._recData[1];
        byte b3 = this._recData[2];
        byte[] year = {this._recData[3], this._recData[4]};
        byte b4 = this._recData[5];
        byte b5 = this._recData[6];
        Calendar calendar = Calendar.getInstance();
        calendar.set(BytesUtil.convertTwoBytesToSignedInt(year[0], year[1]), b4 - 1, b5, b, b2, b3);
        return calendar.getTimeInMillis();
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
        pack.cmdId = CmdIdRc.CmdIdType.GetRTC.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}

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
import dji.midware.media.DJIVideoDecoder;
import dji.midware.util.BytesUtil;
import java.util.Calendar;

@Keep
@EXClassNullAway
public class DataCommonSetDate extends DataBase implements DJIDataSyncListener {
    private static DataCommonSetDate mInstance = null;
    private int mReceiverId = 0;
    private DeviceType mReceiverType = DeviceType.DM368;

    public static synchronized DataCommonSetDate getInstance() {
        DataCommonSetDate dataCommonSetDate;
        synchronized (DataCommonSetDate.class) {
            if (mInstance == null) {
                mInstance = new DataCommonSetDate();
            }
            dataCommonSetDate = mInstance;
        }
        return dataCommonSetDate;
    }

    public DataCommonSetDate setRecvType(DeviceType type) {
        this.mReceiverType = type;
        return this;
    }

    public DataCommonSetDate setRecevicerId(int id) {
        this.mReceiverId = id;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        Calendar calendar = Calendar.getInstance();
        this._sendData = new byte[7];
        byte[] year = BytesUtil.getUnsignedBytes(calendar.get(1));
        this._sendData[0] = year[0];
        this._sendData[1] = year[1];
        this._sendData[2] = (byte) (calendar.get(2) + 1);
        this._sendData[3] = (byte) calendar.get(5);
        this._sendData[4] = (byte) calendar.get(11);
        this._sendData[5] = (byte) calendar.get(12);
        this._sendData[6] = (byte) calendar.get(13);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiverType.value();
        pack.receiverId = this.mReceiverId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.SetDate.value();
        pack.data = getSendData();
        pack.timeOut = DJIVideoDecoder.connectLosedelay;
        pack.repeatTimes = 5;
        start(pack, callBack);
    }
}

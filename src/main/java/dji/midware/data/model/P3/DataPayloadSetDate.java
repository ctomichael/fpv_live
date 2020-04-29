package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdPayloadSDK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.Calendar;

@Keep
@EXClassNullAway
public class DataPayloadSetDate extends DataBase implements DJIDataSyncListener {
    private static DataPayloadSetDate instance = null;

    public static synchronized DataPayloadSetDate getInstance() {
        DataPayloadSetDate dataPayloadSetDate;
        synchronized (DataPayloadSetDate.class) {
            if (instance == null) {
                instance = new DataPayloadSetDate();
            }
            dataPayloadSetDate = instance;
        }
        return dataPayloadSetDate;
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
        pack.receiverType = DeviceType.GIMBAL.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.PayloadSDK.value();
        pack.cmdId = CmdIdPayloadSDK.CmdIdType.syncDateofAppToPayload.value();
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}

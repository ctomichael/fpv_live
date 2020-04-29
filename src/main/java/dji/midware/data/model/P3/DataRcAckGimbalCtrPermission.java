package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsync2Listener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataRcAckGimbalCtrPermission extends DataBase implements DJIDataAsync2Listener {
    private static DataRcAckGimbalCtrPermission instance = null;
    private boolean isAgree;

    public static synchronized DataRcAckGimbalCtrPermission getInstance() {
        DataRcAckGimbalCtrPermission dataRcAckGimbalCtrPermission;
        synchronized (DataRcAckGimbalCtrPermission.class) {
            if (instance == null) {
                instance = new DataRcAckGimbalCtrPermission();
            }
            dataRcAckGimbalCtrPermission = instance;
        }
        return dataRcAckGimbalCtrPermission;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        return true;
    }

    public DataRcAckGimbalCtrPermission setIsAgree(boolean isAgree2) {
        this.isAgree = isAgree2;
        return this;
    }

    public String getName() {
        return getUTF8(1, 6);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int i;
        this._sendData = new byte[2];
        byte[] bArr = this._sendData;
        if (this.isAgree) {
            i = 0;
        } else {
            i = 1;
        }
        bArr[0] = (byte) i;
        this._sendData[1] = this._recData[0];
        DJILogHelper.getInstance().LOGD("DataRcAckGimbalCtrPermission", "ack =" + BytesUtil.byte2hex(this._sendData));
    }

    public void start() {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.ACK.value();
        pack.isNeedAck = DataConfig.NEEDACK.NO.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.AckGimbalCtrPermission.value();
        pack.seq = this.pack.seq;
        start(pack);
        this._recData = null;
    }
}

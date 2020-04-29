package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.google.android.gms.common.ConnectionResult;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCenter;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.base.DJICommonDataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCenterGetBoardNumber extends DJICommonDataBase implements DJIDataSyncListener {
    private static DataCenterGetBoardNumber instance;

    public static synchronized DataCenterGetBoardNumber getInstance() {
        DataCenterGetBoardNumber dataCenterGetBoardNumber;
        synchronized (DataCenterGetBoardNumber.class) {
            if (instance == null) {
                instance = new DataCenterGetBoardNumber();
            }
            dataCenterGetBoardNumber = instance;
        }
        return dataCenterGetBoardNumber;
    }

    public String getBoardNumber() {
        StringBuilder sb = new StringBuilder(13);
        if (this._recData != null && this._recData.length > 0) {
            boolean beZero = true;
            int length = this._recData.length;
            for (int i = 0; i < length; i++) {
                if (BytesUtil.isNumberOrLetter(this._recData[i])) {
                    sb.append((char) this._recData[i]);
                } else {
                    sb.append((int) this._recData[i]);
                }
                if (this._recData[i] != 0) {
                    beZero = false;
                }
            }
            if (beZero) {
                sb.delete(0, sb.length());
            }
        }
        return sb.toString();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[9];
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.BATTERY.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CENTER.value();
        pack.cmdId = CmdIdCenter.CmdIdType.GetBoardNumber.value();
        pack.timeOut = ConnectionResult.DRIVE_EXTERNAL_STORAGE_REQUIRED;
        start(pack, callBack);
    }
}

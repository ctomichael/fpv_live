package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import android.util.Log;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataRcSetFollowFocusInfo extends DataBase {
    private static DataRcSetFollowFocusInfo instance = null;
    private final String TAG = DataRcSetFollowFocusInfo.class.getSimpleName();
    private int mCtrDirction = -1;
    private int mCtrType = -1;

    public static synchronized DataRcSetFollowFocusInfo getInstance() {
        DataRcSetFollowFocusInfo dataRcSetFollowFocusInfo;
        synchronized (DataRcSetFollowFocusInfo.class) {
            if (instance == null) {
                instance = new DataRcSetFollowFocusInfo();
            }
            dataRcSetFollowFocusInfo = instance;
        }
        return dataRcSetFollowFocusInfo;
    }

    public DataRcSetFollowFocusInfo setCtrType(int type) {
        this.mCtrType = type;
        return this;
    }

    public DataRcSetFollowFocusInfo setCtrDirection(int direction) {
        this.mCtrDirction = direction;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) ((this.mCtrType & 3) | ((this.mCtrDirction << 2) & 4));
        Log.d(this.TAG, "doPack: " + BytesUtil.byte2hex(this._sendData));
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.RC.value();
        pack.receiverId = 1;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetFollowFocusInfo.value();
        start(pack, callBack);
    }
}

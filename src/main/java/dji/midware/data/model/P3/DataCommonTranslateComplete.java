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
import java.util.Arrays;

@Keep
@EXClassNullAway
public class DataCommonTranslateComplete extends DataBase implements DJIDataSyncListener {
    private static DataCommonTranslateComplete instance = null;
    private int mEncrypt = 0;
    private byte[] mMd5 = null;
    private int mReceiveId = 0;
    private DeviceType mReceiveType = DeviceType.RC;
    private int mTimeOut = -1;

    public static synchronized DataCommonTranslateComplete getInstance() {
        DataCommonTranslateComplete dataCommonTranslateComplete;
        synchronized (DataCommonTranslateComplete.class) {
            if (instance == null) {
                instance = new DataCommonTranslateComplete();
            }
            dataCommonTranslateComplete = instance;
        }
        return dataCommonTranslateComplete;
    }

    public DataCommonTranslateComplete setReceiveType(DeviceType type) {
        this.mReceiveType = type;
        return this;
    }

    public DataCommonTranslateComplete setReceiveId(int id) {
        this.mReceiveId = id;
        return this;
    }

    public DataCommonTranslateComplete setMD5(byte[] md5) {
        this.mMd5 = md5;
        return this;
    }

    public DataCommonTranslateComplete setTimeOut(int timeOut) {
        this.mTimeOut = timeOut;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.mMd5 == null) {
            this.mMd5 = new byte[16];
            Arrays.fill(this.mMd5, (byte) 0);
        }
        this._sendData = new byte[(this.mMd5.length + 1)];
        this._sendData[0] = (byte) this.mEncrypt;
        System.arraycopy(this.mMd5, 0, this._sendData, 1, this.mMd5.length);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = this.mReceiveType.value();
        pack.receiverId = this.mReceiveId;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.TranslateComplete.value();
        pack.repeatTimes = 1;
        if (this.mTimeOut > 0) {
            pack.timeOut = this.mTimeOut;
        } else if (this.mReceiveType == DeviceType.DM368_G || this.mReceiveType == DeviceType.TRANSFORM_G) {
            pack.timeOut = 30000;
        } else {
            pack.timeOut = 10000;
        }
        start(pack, callBack);
    }
}

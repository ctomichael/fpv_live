package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.util.BytesUtil;
import org.greenrobot.eventbus.EventBus;

@Keep
@EXClassNullAway
public class DataGlassActiveStatus extends DataAbstractGetPushActiveStatus {
    private static DataGlassActiveStatus instance = null;

    public static synchronized DataGlassActiveStatus getInstance() {
        DataGlassActiveStatus dataGlassActiveStatus;
        synchronized (DataGlassActiveStatus.class) {
            if (instance == null) {
                instance = new DataGlassActiveStatus();
            }
            dataGlassActiveStatus = instance;
        }
        return dataGlassActiveStatus;
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        setRecData(data);
        if (data != null && data.length >= 11 && this.pack.cmdType != 1) {
            if (((Integer) get(0, 1, Integer.class)).intValue() == DataAbstractGetPushActiveStatus.TYPE.PUSH.value(DataAbstractGetPushActiveStatus.DJIActiveVersion.Ver1_1)) {
                this.version = DataAbstractGetPushActiveStatus.DJIActiveVersion.Ver1_1;
            } else {
                this.version = DataAbstractGetPushActiveStatus.DJIActiveVersion.Ver1_2;
            }
            this.snLen = ((Integer) get(1, 1, Integer.class)).intValue();
            if (this.snLen > 16) {
                this.pushSN = "";
            } else {
                this.pushSN = getRevertSn(2, this.snLen);
            }
            DJILogHelper.getInstance().LOGD(this.TAG, getClass().getSimpleName() + " push SN=" + this.pushSN, false, true);
            EventBus.getDefault().post(this);
        }
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        int len = 16;
        DataAbstractGetPushActiveStatus.DJIActiveVersion version = getVersion();
        if (this.type == DataAbstractGetPushActiveStatus.TYPE.GET) {
            this._sendData = new byte[1];
            this._sendData[0] = (byte) this.type.value(version);
        } else if (this.type == DataAbstractGetPushActiveStatus.TYPE.SET) {
            int offset = 0;
            byte[] snbytes = BytesUtil.getBytes(this.sn);
            if (version == DataAbstractGetPushActiveStatus.DJIActiveVersion.Ver1_1) {
                this._sendData = new byte[(snbytes.length + 10)];
            } else {
                this._sendData = new byte[(snbytes.length + 20)];
                offset = 10;
                System.arraycopy(BytesUtil.getBytes(this.ac), 0, this._sendData, 2, 2);
            }
            this._sendData[0] = (byte) this.type.value(version);
            this._sendData[1] = (byte) this.activeStatus;
            BytesUtil.arraycopy(BytesUtil.getUnsignedBytes(this.year), this._sendData, offset + 2);
            this._sendData[offset + 4] = (byte) this.month;
            this._sendData[offset + 5] = (byte) this.day;
            this._sendData[offset + 6] = (byte) this.hour;
            this._sendData[offset + 7] = (byte) this.min;
            this._sendData[offset + 8] = (byte) this.second;
            if (snbytes.length < 16) {
                len = snbytes.length;
            }
            this._sendData[offset + 9] = (byte) len;
            if (snbytes.length > 0) {
                System.arraycopy(snbytes, 0, this._sendData, offset + 10, len);
            }
        } else if (this.type == DataAbstractGetPushActiveStatus.TYPE.FAIL) {
            byte[] snbytes2 = BytesUtil.getBytes(this.sn);
            if (version == DataAbstractGetPushActiveStatus.DJIActiveVersion.Ver1_0) {
                this._sendData = new byte[11];
                this._sendData[0] = (byte) this.type.value(version);
                System.arraycopy(snbytes2, 0, this._sendData, 1, 10);
                return;
            }
            this._sendData = new byte[(snbytes2.length + 2)];
            this._sendData[0] = (byte) this.type.value(version);
            this._sendData[1] = (byte) snbytes2.length;
            System.arraycopy(snbytes2, 0, this._sendData, 2, snbytes2.length);
        }
    }

    public String getSN() {
        if (getVersion() == DataAbstractGetPushActiveStatus.DJIActiveVersion.Ver1_1) {
            int snLen = ((Integer) get(8, 1, Integer.class)).intValue();
            if (snLen > 16) {
                this.getSN = "";
            } else {
                this.getSN = getRevertSn(9, snLen);
            }
        } else {
            int snLen2 = ((Integer) get(18, 1, Integer.class)).intValue();
            if (snLen2 > 16) {
                this.getSN = "";
            } else {
                this.getSN = getRevertSn(19, snLen2);
            }
        }
        return this.getSN;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.GLASS.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.ActiveStatus.value();
        pack.timeOut = 5000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }
}

package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.packages.P3.Pack;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import java.text.SimpleDateFormat;
import java.util.Date;

@Keep
@EXClassNullAway
public class DataFlycActiveStatus extends DataAbstractGetPushActiveStatus {
    private static DataFlycActiveStatus instance = null;

    public static synchronized DataFlycActiveStatus getInstance() {
        DataFlycActiveStatus dataFlycActiveStatus;
        synchronized (DataFlycActiveStatus.class) {
            if (instance == null) {
                instance = new DataFlycActiveStatus();
            }
            dataFlycActiveStatus = instance;
        }
        return dataFlycActiveStatus;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        super.setPushRecPack(pack);
    }

    public boolean isActive() {
        int pushType = ((Integer) get(0, 1, Integer.class)).intValue();
        if (pushType == DataAbstractGetPushActiveStatus.TYPE.PUSH.value(DataAbstractGetPushActiveStatus.DJIActiveVersion.Ver1_0) || pushType == DataAbstractGetPushActiveStatus.TYPE.PUSH.value(DataAbstractGetPushActiveStatus.DJIActiveVersion.Ver1_1)) {
            return true;
        }
        return false;
    }

    public int getFirstByteWhenGet() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        if (this.type == DataAbstractGetPushActiveStatus.TYPE.SET) {
            DJILogHelper.getInstance().LOGE("ACTIVATE_NEW", new SimpleDateFormat("yyyy-MM-dd hh:mm:ss:SSS").format(new Date()) + " @" + "check active", "active");
        }
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.ActiveStatus.value();
        pack.timeOut = 1000;
        pack.repeatTimes = 3;
        start(pack, callBack);
    }
}

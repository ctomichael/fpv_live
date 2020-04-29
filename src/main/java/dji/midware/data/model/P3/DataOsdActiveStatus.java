package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.manager.DJIUSBWifiSwitchManager;
import dji.logic.utils.DJIProductSupportUtil;
import dji.midware.data.config.P3.CmdIdCommon;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.model.common.DataAbstractGetPushActiveStatus;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;

@Keep
@EXClassNullAway
public class DataOsdActiveStatus extends DataAbstractGetPushActiveStatus {
    private static DataOsdActiveStatus instance = null;

    public static synchronized DataOsdActiveStatus getInstance() {
        DataOsdActiveStatus dataOsdActiveStatus;
        synchronized (DataOsdActiveStatus.class) {
            if (instance == null) {
                instance = new DataOsdActiveStatus();
            }
            dataOsdActiveStatus = instance;
        }
        return dataOsdActiveStatus;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        ProductType type = DJIProductManager.getInstance().getType();
        if (DJIProductSupportUtil.isLonganSeries(type) || DJIUSBWifiSwitchManager.getInstance().isProductWifiConnected(type)) {
            pack.receiverType = DeviceType.OFDM.value();
        } else {
            pack.receiverType = DeviceType.OSD.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.COMMON.value();
        pack.cmdId = CmdIdCommon.CmdIdType.ActiveStatus.value();
        start(pack, callBack);
    }
}

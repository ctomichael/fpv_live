package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILogHelper;
import dji.midware.data.config.P3.CmdIdCenter;
import dji.midware.data.config.P3.CmdIdSmartBattery;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.config.P3.ProductType;
import dji.midware.data.manager.P3.DJIProductManager;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import dji.thirdparty.afinal.core.Arrays;

@Keep
@EXClassNullAway
public class DataCenterGetBatteryHistory extends DataBase implements DJIDataSyncListener {
    private static DataCenterGetBatteryHistory mInstance = null;
    private final int[] mHistories = new int[31];

    public static synchronized DataCenterGetBatteryHistory getInstance() {
        DataCenterGetBatteryHistory dataCenterGetBatteryHistory;
        synchronized (DataCenterGetBatteryHistory.class) {
            if (mInstance == null) {
                mInstance = new DataCenterGetBatteryHistory();
            }
            dataCenterGetBatteryHistory = mInstance;
        }
        return dataCenterGetBatteryHistory;
    }

    private DataCenterGetBatteryHistory() {
        Arrays.fill(this.mHistories, 0);
    }

    public int[] getHistory() {
        Arrays.fill(this.mHistories, 0);
        DJILogHelper.getInstance().LOGD("History", "data[" + BytesUtil.byte2hex(this._recData), false, true);
        if (this._recData != null && this._recData.length > 0) {
            int i = 0;
            int offset = 0;
            while (i < this.mHistories.length && offset + 4 < this._recData.length) {
                this.mHistories[i] = ((Integer) get(offset, 4, Integer.class)).intValue();
                i++;
                offset += 4;
            }
        }
        return this.mHistories;
    }

    public long[] getHistoryLong() {
        long[] history = new long[16];
        DJILogHelper.getInstance().LOGD("History", "data[" + BytesUtil.byte2hex(this._recData), false, true);
        for (int i = 0; i < history.length; i++) {
            history[i] = ((Long) get((i * 8) + 2 + 2, 6, Long.class)).longValue();
        }
        return history;
    }

    public void resetHistory() {
        Arrays.fill(this.mHistories, 0);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        ProductType type = DJIProductManager.getInstance().getType();
        if (type == ProductType.litchiC || type == ProductType.litchiS || type == ProductType.litchiX || type == ProductType.P34K || type == ProductType.Tomato || type == ProductType.Pomato || type == ProductType.KumquatX || type == ProductType.KumquatS || type == ProductType.Potato || type == ProductType.Orange2 || type == ProductType.M200 || type == ProductType.PomatoSDR || type == ProductType.PomatoRTK || type == ProductType.M210 || type == ProductType.M210RTK || type == ProductType.PM420 || type == ProductType.PM420PRO || type == ProductType.PM420PRO_RTK) {
            pack.receiverType = DeviceType.BATTERY.value();
        } else {
            pack.receiverType = DeviceType.CENTER.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        if (type == ProductType.KumquatX || type == ProductType.KumquatS) {
            pack.cmdSet = CmdSet.SMARTBATTERY.value();
            pack.cmdId = CmdIdSmartBattery.CmdIdType.GetHistory.value();
        } else {
            pack.cmdSet = CmdSet.CENTER.value();
            pack.cmdId = CmdIdCenter.CmdIdType.GetBatteryHistory.value();
        }
        pack.data = getSendData();
        pack.timeOut = 5000;
        pack.repeatTimes = 2;
        start(pack, callBack);
    }
}

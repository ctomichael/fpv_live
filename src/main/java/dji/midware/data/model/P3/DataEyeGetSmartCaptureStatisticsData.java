package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataEyeGetSmartCaptureStatisticsData extends DataBase implements DJIDataSyncListener {
    private static DataEyeGetSmartCaptureStatisticsData instance = null;
    private int type;

    public static synchronized DataEyeGetSmartCaptureStatisticsData getInstance() {
        DataEyeGetSmartCaptureStatisticsData dataEyeGetSmartCaptureStatisticsData;
        synchronized (DataEyeGetSmartCaptureStatisticsData.class) {
            if (instance == null) {
                instance = new DataEyeGetSmartCaptureStatisticsData();
            }
            dataEyeGetSmartCaptureStatisticsData = instance;
        }
        return dataEyeGetSmartCaptureStatisticsData;
    }

    @Keep
    public static class StatisticsData {
        public int interval;
        public int number;

        public StatisticsData(int interval2, int number2) {
            this.interval = interval2;
            this.number = number2;
        }
    }

    public DataEyeGetSmartCaptureStatisticsData setCmdTyp(int type2) {
        this.type = type2;
        return this;
    }

    public int getNumberOfStatisticsData() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public long getTimeStamp() {
        return ((Long) get(1, 8, Long.class)).longValue();
    }

    public StatisticsData[] getStatisticsData() {
        if (getNumberOfStatisticsData() <= 0) {
            return null;
        }
        StatisticsData[] arrayList = new StatisticsData[getNumberOfStatisticsData()];
        for (int i = 0; i < getNumberOfStatisticsData(); i++) {
            arrayList[i] = new StatisticsData(((Integer) get((i * 3) + 9, 2, Integer.class)).intValue(), ((Integer) get((i * 3) + 9 + 2, 1, Integer.class)).intValue());
        }
        return arrayList;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.GetSmartCaptureStatisticsData.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.type;
    }
}

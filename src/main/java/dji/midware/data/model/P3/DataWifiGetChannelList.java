package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdWifi;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataWifiGetChannelList extends DataBase implements DJIDataSyncListener {
    private static DataWifiGetChannelList mInstance = null;
    private boolean isSupport5G = true;
    private int mCurChannel = -1;

    public static synchronized DataWifiGetChannelList getInstance() {
        DataWifiGetChannelList dataWifiGetChannelList;
        synchronized (DataWifiGetChannelList.class) {
            if (mInstance == null) {
                mInstance = new DataWifiGetChannelList();
            }
            dataWifiGetChannelList = mInstance;
        }
        return dataWifiGetChannelList;
    }

    public DataWifiGetChannelList setSupport5G(boolean isSupport5G2) {
        this.isSupport5G = isSupport5G2;
        return this;
    }

    public int[] get24GChannelList() {
        int modeNums = ((Integer) get(13, 4, Integer.class)).intValue();
        if (((Integer) get(17, 2, Integer.class)).intValue() > 6) {
            modeNums = 0;
        }
        int[] channelList = new int[modeNums];
        for (int i = 0; i != modeNums; i++) {
            channelList[i] = ((Integer) get((i * 2) + 17, 2, Integer.class)).intValue();
        }
        return channelList;
    }

    public int[] get5GChannelList() {
        int mode24GNums = ((Integer) get(13, 4, Integer.class)).intValue();
        if (((Integer) get(17, 2, Integer.class)).intValue() > 6) {
            mode24GNums = 0;
        }
        int start = (mode24GNums * 2) + 17 + 4;
        if (mode24GNums == 0) {
            start -= 8;
        }
        int mode5GNums = ((Integer) get(start, 4, Integer.class)).intValue();
        int[] channelList = new int[mode5GNums];
        for (int i = 0; i != mode5GNums; i++) {
            channelList[i] = ((Integer) get(start + 4 + (i * 2), 2, Integer.class)).intValue();
        }
        return channelList;
    }

    public int getCurChannel() {
        this.mCurChannel = ((Integer) get(4, 1, Integer.class)).intValue();
        return this.mCurChannel;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        if (this.isSupport5G) {
            this._sendData[0] = 1;
        } else {
            this._sendData[0] = 0;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.WIFI.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.cmdId = CmdIdWifi.CmdIdType.GetChannelList.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}

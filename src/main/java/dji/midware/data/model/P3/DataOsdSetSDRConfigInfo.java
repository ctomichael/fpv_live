package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdOsd;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;
import java.util.ArrayList;

@Keep
@EXClassNullAway
public class DataOsdSetSDRConfigInfo extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetSDRConfigInfo instance;
    private ArrayList<SDRConfigInfo> configInfoList;

    public static synchronized DataOsdSetSDRConfigInfo getInstance() {
        DataOsdSetSDRConfigInfo dataOsdSetSDRConfigInfo;
        synchronized (DataOsdSetSDRConfigInfo.class) {
            if (instance == null) {
                instance = new DataOsdSetSDRConfigInfo();
            }
            dataOsdSetSDRConfigInfo = instance;
        }
        return dataOsdSetSDRConfigInfo;
    }

    public DataOsdSetSDRConfigInfo setSDRConfigInfos(ArrayList<SDRConfigInfo> configInfoList2) {
        this.configInfoList = configInfoList2;
        return this;
    }

    public DataOsdSetSDRConfigInfo setSDRConfigInfo(SDRConfigInfo configInfo) {
        ArrayList<SDRConfigInfo> configInfos = new ArrayList<>();
        configInfos.add(configInfo);
        this.configInfoList = configInfos;
        return this;
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetSDRConfigInfo.value();
        start(pack, callBack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        if (this.configInfoList != null) {
            this._sendData = new byte[(this.configInfoList.size() * 3)];
            for (int i = 0; i < this.configInfoList.size(); i++) {
                this._sendData[i * 3] = (byte) this.configInfoList.get(i).type.value();
                System.arraycopy(BytesUtil.getBytes(this.configInfoList.get(i).value), 0, this._sendData, (i * 3) + 1, 2);
            }
        }
    }

    @Keep
    public static class SDRConfigInfo {
        public SDRConfigType type;
        public int value;

        public SDRConfigInfo(SDRConfigType type2, int value2) {
            this.type = type2;
            this.value = value2;
        }
    }

    @Keep
    public enum SDRConfigType {
        NFIndex(1),
        DownlinkFrequencyBand(2),
        SelectionMode(3),
        Bandwidth(4),
        UplinkFrequencyBand(5),
        SelectChannel2pot4G(6),
        SelectChannel5pot8G(6),
        Unknown(255);
        
        private int data;

        private SDRConfigType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SDRConfigType find(int b) {
            SDRConfigType result = Unknown;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

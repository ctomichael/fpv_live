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
import dji.midware.util.BytesUtil;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class DataWifiScanMasterList extends DataBase implements DJIDataSyncListener {
    private static DataWifiScanMasterList mInstance = null;
    private List<ScannedMasterInfo> mMasterInfo = new ArrayList();

    public static synchronized DataWifiScanMasterList getInstance() {
        DataWifiScanMasterList dataWifiScanMasterList;
        synchronized (DataWifiScanMasterList.class) {
            if (mInstance == null) {
                mInstance = new DataWifiScanMasterList();
            }
            dataWifiScanMasterList = mInstance;
        }
        return dataWifiScanMasterList;
    }

    public void setRecData(byte[] data) {
        super.setRecData(data);
        if (data != null) {
            String[] masters = BytesUtil.getStringUTF8(data).split(";");
            this.mMasterInfo.clear();
            for (String master : masters) {
                if (!"".equals(master)) {
                    String[] infos = master.split(",");
                    if (infos.length >= 4) {
                        ScannedMasterInfo info = new ScannedMasterInfo();
                        int i = 0 + 1;
                        info.setMasterId(infos[0]);
                        int i2 = i + 1;
                        info.setFreqPoint(infos[i]);
                        int i3 = i2 + 1;
                        info.setRssi(infos[i2]);
                        int i4 = i3 + 1;
                        info.setFreeOrBusy(infos[i3]);
                        this.mMasterInfo.add(info);
                    }
                }
            }
        }
    }

    public List<ScannedMasterInfo> getMasterInfo() {
        return this.mMasterInfo;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.WIFI_G.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.WIFI.value();
        pack.timeOut = 10000;
        pack.cmdId = CmdIdWifi.CmdIdType.ScanMasterList.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public static class ScannedMasterInfo {
        private String mFreeOrBusy;
        private String mFreqPoint;
        private String mMasterId;
        private String mRssi;

        public String getMasterId() {
            return this.mMasterId;
        }

        public String getFreqPoint() {
            return this.mFreqPoint;
        }

        public String getRssi() {
            return this.mRssi;
        }

        public boolean isFree() {
            return "free".equals(this.mFreeOrBusy);
        }

        /* access modifiers changed from: package-private */
        public void setMasterId(String masterId) {
            this.mMasterId = masterId;
        }

        /* access modifiers changed from: package-private */
        public void setFreqPoint(String freqPoint) {
            this.mFreqPoint = freqPoint;
        }

        /* access modifiers changed from: package-private */
        public void setRssi(String rssi) {
            this.mRssi = rssi;
        }

        /* access modifiers changed from: package-private */
        public void setFreeOrBusy(String freeOrBusy) {
            this.mFreeOrBusy = freeOrBusy;
        }
    }
}

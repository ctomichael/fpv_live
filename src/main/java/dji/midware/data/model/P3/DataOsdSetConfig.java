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
import dji.thirdparty.org.java_websocket.drafts.Draft_75;

@Keep
@EXClassNullAway
public class DataOsdSetConfig extends DataBase implements DJIDataSyncListener {
    private static DataOsdSetConfig instance = null;
    private int bandwidth;
    private int channelId;
    private int downwardFreqType;
    private int isAuto;
    private boolean isDouble;
    private int mcs;
    private int receiverType = 0;
    private int upwardFreqType;

    public static synchronized DataOsdSetConfig getInstance() {
        DataOsdSetConfig dataOsdSetConfig;
        synchronized (DataOsdSetConfig.class) {
            if (instance == null) {
                instance = new DataOsdSetConfig();
            }
            dataOsdSetConfig = instance;
        }
        return dataOsdSetConfig;
    }

    private void reset() {
        this.isAuto = 0;
        this.mcs = 0;
        this.bandwidth = 0;
        this.upwardFreqType = 0;
        this.downwardFreqType = 0;
    }

    public DataOsdSetConfig setSdrMcs(int mcs2) {
        reset();
        this.mcs = mcs2;
        this._sendData = new byte[4];
        this._sendData[0] = 3;
        this._sendData[1] = 0;
        this._sendData[2] = 10;
        this._sendData[3] = (byte) this.mcs;
        return this;
    }

    public DataOsdSetConfig setSdrBandwidth(int bandwidth2) {
        reset();
        this.bandwidth = bandwidth2;
        this._sendData = new byte[4];
        this._sendData[0] = 3;
        this._sendData[1] = 0;
        this._sendData[2] = 14;
        this._sendData[3] = (byte) this.bandwidth;
        return this;
    }

    public DataOsdSetConfig setSdrConfig(int channelId2, int mcs2, int bandwidth2) {
        reset();
        this.channelId = channelId2;
        this.mcs = mcs2;
        this.bandwidth = bandwidth2;
        this._sendData = new byte[8];
        this._sendData[0] = 3;
        this._sendData[1] = 0;
        this._sendData[2] = 1;
        this._sendData[3] = (byte) channelId2;
        this._sendData[4] = 10;
        this._sendData[5] = (byte) mcs2;
        this._sendData[6] = 14;
        this._sendData[7] = (byte) bandwidth2;
        return this;
    }

    public DataOsdSetConfig setFreqType(int type, int value) {
        reset();
        this._sendData = new byte[2];
        if (type == 1) {
            this._sendData[0] = 15;
            this.upwardFreqType = value;
        } else {
            this._sendData[0] = 2;
            this.downwardFreqType = value;
        }
        this._sendData[1] = (byte) value;
        return this;
    }

    public DataOsdSetConfig setReceiverType(int type) {
        this.receiverType = type;
        return this;
    }

    public DataOsdSetConfig setAutoChannel(boolean isAuto2) {
        reset();
        this.isAuto = isAuto2 ? 1 : 0;
        if (isAuto2) {
            this._sendData = new byte[4];
            this._sendData[0] = 3;
            this._sendData[1] = (byte) this.isAuto;
            this._sendData[2] = 1;
            this._sendData[3] = (byte) this.channelId;
        } else {
            this._sendData = new byte[2];
            this._sendData[0] = 3;
            this._sendData[1] = (byte) this.isAuto;
        }
        return this;
    }

    public DataOsdSetConfig setChannel(int channelId2) {
        reset();
        this.channelId = channelId2;
        this._sendData = new byte[4];
        this._sendData[0] = 3;
        this._sendData[1] = 0;
        this._sendData[2] = 1;
        this._sendData[3] = (byte) this.channelId;
        return this;
    }

    public DataOsdSetConfig setMcs(int mcs2) {
        reset();
        this.mcs = mcs2;
        this._sendData = new byte[2];
        this._sendData[0] = 10;
        this._sendData[1] = (byte) this.mcs;
        return this;
    }

    public DataOsdSetConfig setSingleOrDouble(boolean isDouble2) {
        int i = 0;
        reset();
        this.isDouble = isDouble2;
        this._sendData = new byte[2];
        this._sendData[0] = 11;
        byte[] bArr = this._sendData;
        if (this.isDouble) {
            i = 1;
        }
        bArr[1] = (byte) i;
        return this;
    }

    public DataOsdSetConfig setBandWidth(int bandWidth) {
        this._sendData = new byte[2];
        this._sendData[0] = 12;
        this._sendData[1] = (byte) bandWidth;
        return this;
    }

    public DataOsdSetConfig setVideoSource(int source) {
        this._sendData = new byte[2];
        this._sendData[0] = Draft_75.CR;
        this._sendData[1] = (byte) source;
        return this;
    }

    public DataOsdSetConfig setOutputModeChange(int bandWidth, int source) {
        this._sendData = new byte[4];
        this._sendData[0] = 12;
        this._sendData[1] = (byte) bandWidth;
        this._sendData[2] = Draft_75.CR;
        this._sendData[3] = (byte) source;
        return this;
    }

    public DataOsdSetConfig setWorkingFreq(int _freq) {
        this._sendData = new byte[2];
        this._sendData[0] = (byte) KEY.WorkingFreq.value();
        this._sendData[1] = (byte) _freq;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        if (this.receiverType == 1) {
            pack.receiverType = DeviceType.OFDM.value();
        } else {
            pack.receiverType = DeviceType.OSD.value();
        }
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.OSD.value();
        pack.cmdId = CmdIdOsd.CmdIdType.SetConfig.value();
        pack.repeatTimes = 3;
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    @Keep
    public enum KEY {
        Channel(1),
        FreqStep(2),
        Mcs(10),
        SingleOrDouble(11),
        BandWidthPercentage(12),
        VideoSource(13),
        WorkingFreq(16),
        OTHER(6);
        
        private int data;

        private KEY(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static KEY find(int b) {
            KEY result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

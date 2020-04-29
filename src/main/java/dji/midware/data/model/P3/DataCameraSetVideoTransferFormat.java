package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdDm368;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataCameraSetVideoTransferFormat extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetVideoTransferFormat instance = null;
    private VideoTransferFormat mTransferFormat;

    public static synchronized DataCameraSetVideoTransferFormat getInstance() {
        DataCameraSetVideoTransferFormat dataCameraSetVideoTransferFormat;
        synchronized (DataCameraSetVideoTransferFormat.class) {
            if (instance == null) {
                instance = new DataCameraSetVideoTransferFormat();
            }
            dataCameraSetVideoTransferFormat = instance;
        }
        return dataCameraSetVideoTransferFormat;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mTransferFormat.value();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.DM368.value();
        pack.cmdId = CmdIdDm368.CmdIdType.SetVideoTransferFormat.value();
        pack.data = getSendData();
        pack.timeOut = 3000;
        start(pack, callBack);
    }

    public DataCameraSetVideoTransferFormat setVideoFormat(VideoTransferFormat format) {
        this.mTransferFormat = format;
        return this;
    }

    @Keep
    public enum VideoTransferFormat {
        H264(1),
        H265(2);
        
        private final int mData;

        private VideoTransferFormat(int data) {
            this.mData = data;
        }

        public static VideoTransferFormat find(int b) {
            VideoTransferFormat result = H264;
            VideoTransferFormat[] values = values();
            for (VideoTransferFormat tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }

        public int value() {
            return this.mData;
        }

        public boolean _equals(int b) {
            return this.mData == b;
        }
    }
}

package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import com.drew.metadata.exif.makernotes.LeicaMakernoteDirectory;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.media.DJIVideoDecoder;

@Keep
@EXClassNullAway
public class DataEyeEasySelfCalibration extends DataBase implements DJIDataSyncListener {
    private static DataEyeEasySelfCalibration instance = null;
    private SelfRequest mSelfRequest = SelfRequest.ByUser;

    public static synchronized DataEyeEasySelfCalibration getInstance() {
        DataEyeEasySelfCalibration dataEyeEasySelfCalibration;
        synchronized (DataEyeEasySelfCalibration.class) {
            if (instance == null) {
                instance = new DataEyeEasySelfCalibration();
            }
            dataEyeEasySelfCalibration = instance;
        }
        return dataEyeEasySelfCalibration;
    }

    public DataEyeEasySelfCalibration setRequest(SelfRequest request) {
        this.mSelfRequest = request;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.mSelfRequest.value();
    }

    public void start(DJIDataCallBack callBack) {
        int i;
        boolean beCancel = false;
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.DOUBLE.value();
        pack.receiverId = 0;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.EasySelfCal.value();
        if (SelfRequest.Cancel == this.mSelfRequest) {
            beCancel = true;
        }
        pack.timeOut = beCancel ? LeicaMakernoteDirectory.TAG_CAMERA_TEMPERATURE : DJIVideoDecoder.connectLosedelay;
        if (beCancel) {
            i = 3;
        } else {
            i = 1;
        }
        pack.repeatTimes = i;
        start(pack, callBack);
    }

    @Keep
    public enum SelfRequest {
        None(0),
        ByUser(1),
        Cancel(-1),
        OTHER(100);
        
        private final int data;

        private SelfRequest(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SelfRequest find(int b) {
            SelfRequest result = None;
            SelfRequest[] values = values();
            for (SelfRequest tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}

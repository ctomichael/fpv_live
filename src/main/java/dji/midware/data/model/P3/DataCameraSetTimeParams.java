package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCameraSetTimeParams extends DataBase implements DJIDataSyncListener {
    private static DataCameraSetTimeParams instance = null;
    private int num;
    private int period;
    private TYPE type;

    public static synchronized DataCameraSetTimeParams getInstance() {
        DataCameraSetTimeParams dataCameraSetTimeParams;
        synchronized (DataCameraSetTimeParams.class) {
            if (instance == null) {
                instance = new DataCameraSetTimeParams();
            }
            dataCameraSetTimeParams = instance;
        }
        return dataCameraSetTimeParams;
    }

    public DataCameraSetTimeParams setType(TYPE type2) {
        this.type = type2;
        return this;
    }

    public DataCameraSetTimeParams setNum(int num2) {
        this.num = num2;
        return this;
    }

    public DataCameraSetTimeParams setPeriod(int period2) {
        this.period = period2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[4];
        this._sendData[0] = (byte) this.type.value();
        this._sendData[1] = (byte) this.num;
        System.arraycopy(BytesUtil.getUnsignedBytes(this.period), 0, this._sendData, 2, 2);
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetTimeParams.value();
        start(pack, callBack);
    }

    @Keep
    public enum TYPE {
        Single(0),
        Multiple(1),
        Timelapse(2),
        OTHER(100);
        
        private int data;

        private TYPE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static TYPE find(int b) {
            TYPE result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

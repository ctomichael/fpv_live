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

@Keep
@EXClassNullAway
public class DataCameraGetImageSize extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetImageSize instance = null;

    public static synchronized DataCameraGetImageSize getInstance() {
        DataCameraGetImageSize dataCameraGetImageSize;
        synchronized (DataCameraGetImageSize.class) {
            if (instance == null) {
                instance = new DataCameraGetImageSize();
            }
            dataCameraGetImageSize = instance;
        }
        return dataCameraGetImageSize;
    }

    public SizeType getSize() {
        return SizeType.find(this._recData[0]);
    }

    public RatioType getRatio() {
        return RatioType.find(this._recData[1]);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.GetImageSize.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum SizeType {
        DEFAULT(0),
        SMALLEST(1),
        SMALL(2),
        MIDDLE(3),
        LARGE(4),
        LARGEST(5),
        OTHER(6);
        
        private int data;

        private SizeType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SizeType find(int b) {
            SizeType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    @Keep
    public enum RatioType {
        R_4_3(0, 0.75f),
        R_16_9(1, 0.5625f),
        R_3_2(2, 0.6666667f),
        OTHER(6, 0.5625f);
        
        private int data;
        private float mRatio = 0.5625f;

        private RatioType(int _data, float ratio) {
            this.data = _data;
            this.mRatio = ratio;
        }

        public float getRatioNumber() {
            return this.mRatio;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static RatioType find(int b) {
            RatioType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

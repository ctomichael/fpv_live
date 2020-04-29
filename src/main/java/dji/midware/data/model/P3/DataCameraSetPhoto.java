package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdCamera;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataAsyncListener;
import java.util.Timer;

@Keep
@EXClassNullAway
public class DataCameraSetPhoto extends DataBase implements DJIDataAsyncListener {
    private static DataCameraSetPhoto instance = null;
    private Timer timer;
    private TYPE type;

    public static synchronized DataCameraSetPhoto getInstance() {
        DataCameraSetPhoto dataCameraSetPhoto;
        synchronized (DataCameraSetPhoto.class) {
            if (instance == null) {
                instance = new DataCameraSetPhoto();
            }
            dataCameraSetPhoto = instance;
        }
        return dataCameraSetPhoto;
    }

    public DataCameraSetPhoto setType(TYPE type2) {
        this.type = type2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.type.value();
    }

    public void start(long period) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetPhoto.value();
        pack.data = getSendData();
        start(pack);
    }

    public void stop() {
        if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
        }
    }

    @Keep
    public enum TYPE {
        STOP(0),
        SINGLE(1),
        HDR(2),
        FULLVIEW(3),
        BURST(4),
        AEB(5),
        TIME(6),
        APP_FULLVIEW(7),
        TRACKING(8),
        RAWBURST(9),
        HDR_PLUS(10),
        HYPER_NIGHT(11),
        HYPER_LAPSE(12),
        PANORAMA_TRUE(13),
        BOKEH(98),
        PANORAMA(99),
        OTHER(11);
        
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

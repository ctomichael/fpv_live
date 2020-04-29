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
import java.util.TimerTask;

@Keep
@EXClassNullAway
public class DataCameraSetRecord extends DataBase implements DJIDataAsyncListener {
    private static DataCameraSetRecord instance = null;
    private Timer timer;
    private TYPE type;

    public static synchronized DataCameraSetRecord getInstance() {
        DataCameraSetRecord dataCameraSetRecord;
        synchronized (DataCameraSetRecord.class) {
            if (instance == null) {
                instance = new DataCameraSetRecord();
            }
            dataCameraSetRecord = instance;
        }
        return dataCameraSetRecord;
    }

    public DataCameraSetRecord setType(TYPE type2) {
        this.type = type2;
        return this;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
        this._sendData = new byte[1];
        this._sendData[0] = (byte) this.type.value();
    }

    public void start(long period) {
        final SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.CAMERA.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.CAMERA.value();
        pack.cmdId = CmdIdCamera.CmdIdType.SetRecord.value();
        pack.data = getSendData();
        if (this.timer == null) {
            this.timer = new Timer();
        } else {
            stop();
        }
        this.timer.schedule(new TimerTask() {
            /* class dji.midware.data.model.P3.DataCameraSetRecord.AnonymousClass1 */

            public void run() {
                DataCameraSetRecord.this.start(pack);
            }
        }, 0, period);
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
        START(1),
        PAUSE(2),
        RESUME(3),
        OTHER(7);
        
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
    }
}

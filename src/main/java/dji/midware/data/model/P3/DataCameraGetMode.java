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
public class DataCameraGetMode extends DataBase implements DJIDataSyncListener {
    private static DataCameraGetMode instance = null;

    public static synchronized DataCameraGetMode getInstance() {
        DataCameraGetMode dataCameraGetMode;
        synchronized (DataCameraGetMode.class) {
            if (instance == null) {
                instance = new DataCameraGetMode();
            }
            dataCameraGetMode = instance;
        }
        return dataCameraGetMode;
    }

    public MODE getMode() {
        return MODE.find(this._recData[0]);
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
        pack.cmdId = CmdIdCamera.CmdIdType.GetMode.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum MODE {
        TAKEPHOTO(0),
        RECORD(1),
        PLAYBACK(2),
        TRANSCODE(3),
        TUNING(4),
        SAVEPOWER(5),
        DOWNLOAD(6),
        NEW_PLAYBACK(7),
        BROADCAST(8),
        OTHER(100);
        
        private static volatile MODE[] cameraModes;
        private int data;

        private MODE(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static MODE find(int b) {
            MODE result = OTHER;
            if (cameraModes == null) {
                cameraModes = values();
            }
            for (int i = 0; i < cameraModes.length; i++) {
                if (cameraModes[i]._equals(b)) {
                    return cameraModes[i];
                }
            }
            return result;
        }
    }
}

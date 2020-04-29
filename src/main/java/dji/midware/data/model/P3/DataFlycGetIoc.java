package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdFlyc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataFlycGetIoc extends DataBase implements DJIDataSyncListener {
    private static DataFlycGetIoc instance = null;

    public static synchronized DataFlycGetIoc getInstance() {
        DataFlycGetIoc dataFlycGetIoc;
        synchronized (DataFlycGetIoc.class) {
            if (instance == null) {
                instance = new DataFlycGetIoc();
            }
            dataFlycGetIoc = instance;
        }
        return dataFlycGetIoc;
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
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.GetIoc.value();
        pack.data = getSendData();
        start(pack, callBack);
    }

    @Keep
    public enum MODE {
        CourseLock(1),
        HomeLock(2),
        HotspotSurround(3),
        OTHER(100);
        
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
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

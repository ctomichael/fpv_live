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
public class DataFlycGetFsAction extends DataBase implements DJIDataSyncListener {
    private static DataFlycGetFsAction instance = null;

    public static synchronized DataFlycGetFsAction getInstance() {
        DataFlycGetFsAction dataFlycGetFsAction;
        synchronized (DataFlycGetFsAction.class) {
            if (instance == null) {
                instance = new DataFlycGetFsAction();
            }
            dataFlycGetFsAction = instance;
        }
        return dataFlycGetFsAction;
    }

    public FS_ACTION getFsAction() {
        if (this._recData == null || this._recData.length == 0) {
            return FS_ACTION.GoHome;
        }
        return FS_ACTION.find(((Integer) get(0, 1, Integer.class)).intValue());
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
        pack.cmdId = CmdIdFlyc.CmdIdType.GetFsAction.value();
        start(pack, callBack);
    }

    @Keep
    public enum FS_ACTION {
        Hover(0),
        Landing(1),
        GoHome(2),
        OTHER(100);
        
        private int data;

        private FS_ACTION(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FS_ACTION find(int b) {
            FS_ACTION result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

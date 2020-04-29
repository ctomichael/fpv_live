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
public class DataFlycNoeMissionPauseOrResume extends DataBase implements DJIDataSyncListener {
    private static DataFlycNoeMissionPauseOrResume instance;
    private boolean isPause;

    public DataFlycNoeMissionPauseOrResume pauseMission() {
        this.isPause = true;
        return this;
    }

    public DataFlycNoeMissionPauseOrResume resumeMission() {
        this.isPause = false;
        return this;
    }

    public static synchronized DataFlycNoeMissionPauseOrResume getInstance() {
        DataFlycNoeMissionPauseOrResume dataFlycNoeMissionPauseOrResume;
        synchronized (DataFlycNoeMissionPauseOrResume.class) {
            if (instance == null) {
                instance = new DataFlycNoeMissionPauseOrResume();
            }
            dataFlycNoeMissionPauseOrResume = instance;
        }
        return dataFlycNoeMissionPauseOrResume;
    }

    public int getResult() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.FLYC.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.FLYC.value();
        pack.cmdId = CmdIdFlyc.CmdIdType.NoeMissionPauseOrResume.value();
        super.start(pack, callBack);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: InitCodeVariables
        jadx.core.utils.exceptions.JadxRuntimeException: Several immutable types in one variable: [int, byte], vars: [r0v0 ?, r0v1 ?, r0v2 ?]
        	at jadx.core.dex.visitors.InitCodeVariables.setCodeVarType(InitCodeVariables.java:102)
        	at jadx.core.dex.visitors.InitCodeVariables.setCodeVar(InitCodeVariables.java:78)
        	at jadx.core.dex.visitors.InitCodeVariables.initCodeVar(InitCodeVariables.java:69)
        	at jadx.core.dex.visitors.InitCodeVariables.initCodeVars(InitCodeVariables.java:51)
        	at jadx.core.dex.visitors.InitCodeVariables.visit(InitCodeVariables.java:32)
        */
    protected void doPack() {
        /*
            r4 = this;
            r0 = 1
            r1 = 0
            byte[] r2 = new byte[r0]
            r4._sendData = r2
            byte[] r2 = r4._sendData
            boolean r3 = r4.isPause
            if (r3 == 0) goto L_0x000d
            r0 = r1
        L_0x000d:
            r2[r1] = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.model.P3.DataFlycNoeMissionPauseOrResume.doPack():void");
    }
}

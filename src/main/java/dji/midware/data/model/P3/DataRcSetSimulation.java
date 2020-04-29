package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.config.P3.CmdIdRc;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
@EXClassNullAway
public class DataRcSetSimulation extends DataBase implements DJIDataSyncListener {
    private static DataRcSetSimulation instance = null;
    private boolean mEnable = false;

    public static synchronized DataRcSetSimulation getInstance() {
        DataRcSetSimulation dataRcSetSimulation;
        synchronized (DataRcSetSimulation.class) {
            if (instance == null) {
                instance = new DataRcSetSimulation();
            }
            dataRcSetSimulation = instance;
        }
        return dataRcSetSimulation;
    }

    public DataRcSetSimulation startFlySimulation() {
        this.mEnable = true;
        return this;
    }

    public DataRcSetSimulation exitFlySimulation() {
        this.mEnable = false;
        return this;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: InitCodeVariables
        jadx.core.utils.exceptions.JadxRuntimeException: Several immutable types in one variable: [int, byte], vars: [r0v0 ?, r0v2 ?, r0v1 ?]
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
            byte[] r1 = new byte[r0]
            r4._sendData = r1
            byte[] r1 = r4._sendData
            r2 = 0
            boolean r3 = r4.mEnable
            if (r3 == 0) goto L_0x000f
        L_0x000c:
            r1[r2] = r0
            return
        L_0x000f:
            r0 = 2
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.model.P3.DataRcSetSimulation.doPack():void");
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.OSD.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.RC.value();
        pack.cmdId = CmdIdRc.CmdIdType.SetSimulation.value();
        pack.data = getSendData();
        start(pack, callBack);
    }
}

package dji.midware.data.model.P3;

import dji.midware.data.config.P3.CmdIdRTK;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

public class DataRTKSnrSetEnable extends DataBase implements DJIDataSyncListener {
    private boolean mEnable;
    private int mReceiverType;

    public DataRTKSnrSetEnable setEnable(boolean enable) {
        this.mEnable = enable;
        return this;
    }

    public DataRTKSnrSetEnable setReceiverType(int receiverType) {
        this.mReceiverType = receiverType;
        return this;
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
            boolean r3 = r4.mEnable
            if (r3 == 0) goto L_0x000d
            r0 = r1
        L_0x000d:
            r2[r1] = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.model.P3.DataRTKSnrSetEnable.doPack():void");
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.cmdSet = CmdSet.RTK.value();
        pack.cmdId = CmdIdRTK.CmdIdType.RTKSnrSetEnable.value();
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.receiverType = this.mReceiverType;
        pack.receiverId = this.receiverID;
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.senderType = DeviceType.APP.value();
        start(pack, callBack);
    }
}

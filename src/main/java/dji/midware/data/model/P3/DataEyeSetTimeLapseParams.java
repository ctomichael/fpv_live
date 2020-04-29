package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.config.P3.CmdIdEYE;
import dji.midware.data.config.P3.CmdSet;
import dji.midware.data.config.P3.DataConfig;
import dji.midware.data.config.P3.DeviceType;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.SendPack;
import dji.midware.interfaces.DJIDataCallBack;
import dji.midware.interfaces.DJIDataSyncListener;

@Keep
public class DataEyeSetTimeLapseParams extends DataBase implements DJIDataSyncListener {
    private static DataEyeSetTimeLapseParams instance = null;
    private int id = -1;
    private int length;
    private ParamType paramType;
    private Number value;

    public static synchronized DataEyeSetTimeLapseParams getInstance() {
        DataEyeSetTimeLapseParams dataEyeSetTimeLapseParams;
        synchronized (DataEyeSetTimeLapseParams.class) {
            if (instance == null) {
                instance = new DataEyeSetTimeLapseParams();
            }
            dataEyeSetTimeLapseParams = instance;
        }
        return dataEyeSetTimeLapseParams;
    }

    public DataEyeSetTimeLapseParams setType(ParamType paramType2) {
        this.paramType = paramType2;
        return this;
    }

    public DataEyeSetTimeLapseParams setValue(Number value2) {
        this.value = value2;
        return this;
    }

    public DataEyeSetTimeLapseParams setTmpId(int id2) {
        this.id = id2;
        this.length = 4;
        return this;
    }

    @Keep
    public enum ParamType {
        TOTAL_COUNT(1, 2, Integer.class),
        INTERVAL(2, 2, Integer.class),
        CLOCKWISE(3, 2, Integer.class),
        TIME(4, 2, Integer.class),
        FRAME_NUMBER(5, 2, Integer.class),
        SPEED_LOCKED(6, 1, Boolean.class),
        ADD_ONE_PLUS(7, 1, Integer.class);
        
        Class classType;
        int length;
        int type;

        private ParamType(int type2, int length2, Class classType2) {
            this.type = type2;
            this.length = length2;
            this.classType = classType2;
        }
    }

    public void start(DJIDataCallBack callBack) {
        SendPack pack = new SendPack();
        pack.senderType = DeviceType.APP.value();
        pack.receiverType = DeviceType.SINGLE.value();
        pack.receiverId = 7;
        pack.cmdType = DataConfig.CMDTYPE.REQUEST.value();
        pack.isNeedAck = DataConfig.NEEDACK.YES.value();
        pack.encryptType = DataConfig.EncryptType.NO.value();
        pack.cmdSet = CmdSet.EYE.value();
        pack.cmdId = CmdIdEYE.CmdIdType.SetTimeLapseParams.value();
        pack.timeOut = 3000;
        pack.repeatTimes = 1;
        start(pack, callBack);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: InitCodeVariables
        jadx.core.utils.exceptions.JadxRuntimeException: Several immutable types in one variable: [int, byte], vars: [r1v0 ?, r1v5 ?, r1v4 ?]
        	at jadx.core.dex.visitors.InitCodeVariables.setCodeVarType(InitCodeVariables.java:102)
        	at jadx.core.dex.visitors.InitCodeVariables.setCodeVar(InitCodeVariables.java:78)
        	at jadx.core.dex.visitors.InitCodeVariables.initCodeVar(InitCodeVariables.java:69)
        	at jadx.core.dex.visitors.InitCodeVariables.initCodeVars(InitCodeVariables.java:51)
        	at jadx.core.dex.visitors.InitCodeVariables.visit(InitCodeVariables.java:32)
        */
    protected void doPack() {
        /*
            r7 = this;
            r6 = 2
            r1 = 1
            r2 = 0
            int r3 = r7.id
            r4 = -1
            if (r3 == r4) goto L_0x002d
            r3 = 6
            byte[] r3 = new byte[r3]
            r7._sendData = r3
            byte[] r3 = r7._sendData
            int r4 = r7.id
            byte r4 = (byte) r4
            r3[r2] = r4
            byte[] r3 = r7._sendData
            int r4 = r7.length
            byte r4 = (byte) r4
            r3[r1] = r4
            r0 = 0
            java.lang.Number r1 = r7.value
            float r1 = r1.floatValue()
            byte[] r0 = dji.midware.util.BytesUtil.getBytes(r1)
            byte[] r1 = r7._sendData
            r3 = 4
            java.lang.System.arraycopy(r0, r2, r1, r6, r3)
        L_0x002c:
            return
        L_0x002d:
            dji.midware.data.model.P3.DataEyeSetTimeLapseParams$ParamType r3 = r7.paramType
            int r3 = r3.length
            int r3 = r3 + 2
            byte[] r3 = new byte[r3]
            r7._sendData = r3
            byte[] r3 = r7._sendData
            dji.midware.data.model.P3.DataEyeSetTimeLapseParams$ParamType r4 = r7.paramType
            int r4 = r4.type
            byte r4 = (byte) r4
            r3[r2] = r4
            byte[] r3 = r7._sendData
            dji.midware.data.model.P3.DataEyeSetTimeLapseParams$ParamType r4 = r7.paramType
            int r4 = r4.length
            byte r4 = (byte) r4
            r3[r1] = r4
            dji.midware.data.model.P3.DataEyeSetTimeLapseParams$ParamType r3 = r7.paramType
            int r3 = r3.length
            byte[] r0 = new byte[r3]
            dji.midware.data.model.P3.DataEyeSetTimeLapseParams$ParamType r3 = r7.paramType
            java.lang.Class r3 = r3.classType
            java.lang.Class<java.lang.Integer> r4 = java.lang.Integer.class
            if (r3 != r4) goto L_0x006b
            java.lang.Number r1 = r7.value
            int r1 = r1.intValue()
            byte[] r0 = dji.midware.util.BytesUtil.getBytes(r1)
        L_0x0061:
            byte[] r1 = r7._sendData
            dji.midware.data.model.P3.DataEyeSetTimeLapseParams$ParamType r3 = r7.paramType
            int r3 = r3.length
            java.lang.System.arraycopy(r0, r2, r1, r6, r3)
            goto L_0x002c
        L_0x006b:
            dji.midware.data.model.P3.DataEyeSetTimeLapseParams$ParamType r3 = r7.paramType
            java.lang.Class r3 = r3.classType
            java.lang.Class<java.lang.Float> r4 = java.lang.Float.class
            if (r3 != r4) goto L_0x007e
            java.lang.Number r1 = r7.value
            float r1 = r1.floatValue()
            byte[] r0 = dji.midware.util.BytesUtil.getBytes(r1)
            goto L_0x0061
        L_0x007e:
            dji.midware.data.model.P3.DataEyeSetTimeLapseParams$ParamType r3 = r7.paramType
            java.lang.Class r3 = r3.classType
            java.lang.Class<java.lang.Boolean> r4 = java.lang.Boolean.class
            if (r3 != r4) goto L_0x0095
            byte[] r0 = new byte[r1]
            java.lang.Number r3 = r7.value
            int r3 = r3.intValue()
            if (r3 != r1) goto L_0x0093
        L_0x0090:
            r0[r2] = r1
            goto L_0x0061
        L_0x0093:
            r1 = r2
            goto L_0x0090
        L_0x0095:
            dji.midware.data.model.P3.DataEyeSetTimeLapseParams$ParamType r1 = r7.paramType
            java.lang.Class r1 = r1.classType
            java.lang.Class<java.lang.Double> r3 = java.lang.Double.class
            if (r1 != r3) goto L_0x0061
            java.lang.Number r1 = r7.value
            double r4 = r1.doubleValue()
            byte[] r0 = dji.midware.util.BytesUtil.getBytes(r4)
            goto L_0x0061
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.model.P3.DataEyeSetTimeLapseParams.doPack():void");
    }
}

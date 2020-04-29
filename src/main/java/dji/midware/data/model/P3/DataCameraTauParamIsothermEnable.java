package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCameraTauParamer;

@Keep
@EXClassNullAway
public class DataCameraTauParamIsothermEnable extends DataCameraTauParamer {
    public DataCameraTauParamIsothermEnable() {
        this.mParamCmd = DataCameraTauParamer.ParamCmd.ISOTHERM_ENABLE;
    }

    /*  JADX ERROR: JadxRuntimeException in pass: InitCodeVariables
        jadx.core.utils.exceptions.JadxRuntimeException: Several immutable types in one variable: [int, byte], vars: [r0v0 ?, r0v2 ?, r0v1 ?]
        	at jadx.core.dex.visitors.InitCodeVariables.setCodeVarType(InitCodeVariables.java:102)
        	at jadx.core.dex.visitors.InitCodeVariables.setCodeVar(InitCodeVariables.java:78)
        	at jadx.core.dex.visitors.InitCodeVariables.initCodeVar(InitCodeVariables.java:69)
        	at jadx.core.dex.visitors.InitCodeVariables.initCodeVars(InitCodeVariables.java:51)
        	at jadx.core.dex.visitors.InitCodeVariables.visit(InitCodeVariables.java:32)
        */
    public dji.midware.data.model.P3.DataCameraTauParamIsothermEnable setEnable(boolean r4) {
        /*
            r3 = this;
            r0 = 1
            r1 = 0
            byte[] r2 = new byte[r0]
            r3.mParams = r2
            byte[] r2 = r3.mParams
            if (r4 == 0) goto L_0x000d
        L_0x000a:
            r2[r1] = r0
            return r3
        L_0x000d:
            r0 = r1
            goto L_0x000a
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.model.P3.DataCameraTauParamIsothermEnable.setEnable(boolean):dji.midware.data.model.P3.DataCameraTauParamIsothermEnable");
    }

    public boolean isEnable() {
        return (((Integer) get(0, 1, Integer.class)).intValue() & 1) != 0;
    }
}

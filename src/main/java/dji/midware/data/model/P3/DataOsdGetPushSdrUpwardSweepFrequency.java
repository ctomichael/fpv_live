package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataOsdGetPushSdrUpwardSweepFrequency extends DataBase {
    private static DataOsdGetPushSdrUpwardSweepFrequency instance = null;

    public static synchronized DataOsdGetPushSdrUpwardSweepFrequency getInstance() {
        DataOsdGetPushSdrUpwardSweepFrequency dataOsdGetPushSdrUpwardSweepFrequency;
        synchronized (DataOsdGetPushSdrUpwardSweepFrequency.class) {
            if (instance == null) {
                instance = new DataOsdGetPushSdrUpwardSweepFrequency();
            }
            dataOsdGetPushSdrUpwardSweepFrequency = instance;
        }
        return dataOsdGetPushSdrUpwardSweepFrequency;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v0, resolved type: int[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int[] getSignalList() {
        /*
            r4 = this;
            byte[] r3 = r4._recData
            if (r3 != 0) goto L_0x0008
            r3 = 0
            int[] r2 = new int[r3]
        L_0x0007:
            return r2
        L_0x0008:
            byte[] r3 = r4._recData
            int r1 = r3.length
            int[] r2 = new int[r1]
            r0 = 0
        L_0x000e:
            if (r0 >= r1) goto L_0x0007
            byte[] r3 = r4._recData
            byte r3 = r3[r0]
            r2[r0] = r3
            int r0 = r0 + 1
            goto L_0x000e
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.model.P3.DataOsdGetPushSdrUpwardSweepFrequency.getSignalList():int[]");
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}

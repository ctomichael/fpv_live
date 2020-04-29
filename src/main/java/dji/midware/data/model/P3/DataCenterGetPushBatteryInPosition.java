package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.util.BytesUtil;

@Keep
@EXClassNullAway
public class DataCenterGetPushBatteryInPosition extends DataBase {
    private static DataCenterGetPushBatteryInPosition instance = null;

    public static synchronized DataCenterGetPushBatteryInPosition getInstance() {
        DataCenterGetPushBatteryInPosition dataCenterGetPushBatteryInPosition;
        synchronized (DataCenterGetPushBatteryInPosition.class) {
            if (instance == null) {
                instance = new DataCenterGetPushBatteryInPosition();
            }
            dataCenterGetPushBatteryInPosition = instance;
        }
        return dataCenterGetPushBatteryInPosition;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public int getBatteryNum() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v1, resolved type: int[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int[] getBatteryInPosition() {
        /*
            r6 = this;
            int r1 = r6.getBatteryNum()
            byte[] r3 = r6._recData
            if (r1 <= 0) goto L_0x001d
            if (r3 == 0) goto L_0x001d
            int r4 = r3.length
            int r5 = r1 + 1
            if (r4 != r5) goto L_0x001d
            int[] r2 = new int[r1]
            r0 = 0
        L_0x0012:
            if (r0 >= r1) goto L_0x001e
            int r4 = r0 + 1
            byte r4 = r3[r4]
            r2[r0] = r4
            int r0 = r0 + 1
            goto L_0x0012
        L_0x001d:
            r2 = 0
        L_0x001e:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: dji.midware.data.model.P3.DataCenterGetPushBatteryInPosition.getBatteryInPosition():int[]");
    }

    public boolean hasError() {
        DJILog.d("DataCenterGetPushBatteryInPosition", "byte : " + BytesUtil.byte2hex(this._recData), new Object[0]);
        int num = getBatteryNum();
        int[] statusList = getBatteryInPosition();
        if (num > 0 && statusList != null && num == statusList.length) {
            for (int status : statusList) {
                if (status == 1) {
                    return true;
                }
            }
        }
        return false;
    }
}

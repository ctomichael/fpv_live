package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.Pack;

@Keep
@EXClassNullAway
public class DataADSBGetPushOriginal extends DataBase {
    private static DataADSBGetPushOriginal mInstance = null;

    public static DataADSBGetPushOriginal getInstance() {
        if (mInstance == null) {
            mInstance = new DataADSBGetPushOriginal();
        }
        return mInstance;
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (!(pack == null || pack.data == null)) {
            for (int i = 0; i < pack.data.length; i++) {
                pack.data[i] = (byte) (pack.data[i] ^ 165);
            }
        }
        super.setPushRecPack(pack);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}

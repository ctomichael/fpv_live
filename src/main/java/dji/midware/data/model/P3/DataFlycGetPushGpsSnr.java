package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import java.util.Arrays;
import kotlin.jvm.internal.ByteCompanionObject;

@Keep
@EXClassNullAway
public class DataFlycGetPushGpsSnr extends DataBase {
    public static final int MAX_LENGTH = 32;
    private static DataFlycGetPushGpsSnr instance = null;
    private final int[] mGlonassSnr = new int[32];
    private final int[] mSnrValue = new int[32];

    public static synchronized DataFlycGetPushGpsSnr getInstance() {
        DataFlycGetPushGpsSnr dataFlycGetPushGpsSnr;
        synchronized (DataFlycGetPushGpsSnr.class) {
            if (instance == null) {
                instance = new DataFlycGetPushGpsSnr();
            }
            dataFlycGetPushGpsSnr = instance;
        }
        return dataFlycGetPushGpsSnr;
    }

    public int[] getSnrValues() {
        if (this._recData == null) {
            return null;
        }
        Arrays.fill(this.mSnrValue, 0);
        if (this._recData != null && this._recData.length > 0) {
            int i = 0;
            int size = this._recData.length;
            while (i < size && i < 32) {
                this.mSnrValue[i] = this._recData[i] & ByteCompanionObject.MAX_VALUE;
                i++;
            }
        }
        return this.mSnrValue;
    }

    public int[] getGlonassValues() {
        if (this._recData == null) {
            return null;
        }
        Arrays.fill(this.mGlonassSnr, 0);
        if (this._recData != null && this._recData.length > 0) {
            int i = 32;
            int size = this._recData.length;
            while (i < size && i < 64) {
                this.mGlonassSnr[i - 32] = this._recData[i] & ByteCompanionObject.MAX_VALUE;
                i++;
            }
        }
        return this.mGlonassSnr;
    }

    public int getSnrUsed() {
        if (this._recData == null) {
            return 0;
        }
        int number = 0;
        if (this._recData == null || this._recData.length <= 0) {
            return 0;
        }
        int i = 0;
        int size = this._recData.length;
        while (i < size && i < 32) {
            if ((this._recData[i] & 128) != 0) {
                number++;
            }
            i++;
        }
        return number;
    }

    public int getGlonassSnrUsed() {
        if (this._recData == null) {
            return 0;
        }
        int number = 0;
        if (this._recData == null || this._recData.length <= 0) {
            return 0;
        }
        int i = 32;
        int size = this._recData.length;
        while (i < size && i < 64) {
            if ((this._recData[i] & 128) != 0) {
                number++;
            }
            i++;
        }
        return number;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}

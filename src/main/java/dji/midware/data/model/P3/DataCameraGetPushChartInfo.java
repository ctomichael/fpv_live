package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import java.util.Arrays;

@Keep
@EXClassNullAway
public class DataCameraGetPushChartInfo extends DataBase {
    private static final int DEFAULT_INDEX = 1;
    public static final int MAX_LENGTH = 64;
    private static DataCameraGetPushChartInfo instance = null;
    private int mCurrentIndex = 1;
    private final short[] mLightValues_1 = new short[64];
    private final short[] mLightValues_2 = new short[64];

    public static synchronized DataCameraGetPushChartInfo getInstance() {
        DataCameraGetPushChartInfo dataCameraGetPushChartInfo;
        synchronized (DataCameraGetPushChartInfo.class) {
            if (instance == null) {
                instance = new DataCameraGetPushChartInfo();
            }
            dataCameraGetPushChartInfo = instance;
        }
        return dataCameraGetPushChartInfo;
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{java.util.Arrays.fill(short[], short):void}
     arg types: [short[], int]
     candidates:
      ClspMth{java.util.Arrays.fill(double[], double):void}
      ClspMth{java.util.Arrays.fill(byte[], byte):void}
      ClspMth{java.util.Arrays.fill(long[], long):void}
      ClspMth{java.util.Arrays.fill(boolean[], boolean):void}
      ClspMth{java.util.Arrays.fill(char[], char):void}
      ClspMth{java.util.Arrays.fill(java.lang.Object[], java.lang.Object):void}
      ClspMth{java.util.Arrays.fill(int[], int):void}
      ClspMth{java.util.Arrays.fill(float[], float):void}
      ClspMth{java.util.Arrays.fill(short[], short):void} */
    public short[] getParams() {
        if (this.mCurrentIndex == 1) {
            Arrays.fill(this.mLightValues_1, (short) 0);
            if (this._recData != null && this._recData.length > 0) {
                int size = this._recData.length;
                for (int i = 0; i < size; i++) {
                    this.mLightValues_1[i] = (short) (this._recData[i] & 255);
                }
            }
            this.mCurrentIndex = 0;
            return this.mLightValues_1;
        }
        Arrays.fill(this.mLightValues_2, (short) 0);
        if (this._recData != null && this._recData.length > 0) {
            int size2 = this._recData.length;
            for (int i2 = 0; i2 < size2; i2++) {
                this.mLightValues_2[i2] = (short) (this._recData[i2] & 255);
            }
        }
        this.mCurrentIndex = 1;
        return this.mLightValues_2;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }
}

package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.vision.DJIVisionHelper;
import dji.midware.data.manager.P3.DataBase;
import dji.thirdparty.afinal.core.Arrays;

@Keep
@EXClassNullAway
public class DataEyeGetPushFrontAvoidance extends DataBase {
    private static DataEyeGetPushFrontAvoidance instance = null;
    private int[] mCacheData = null;
    private int[] mCacheLevels = null;

    public static synchronized DataEyeGetPushFrontAvoidance getInstance() {
        DataEyeGetPushFrontAvoidance dataEyeGetPushFrontAvoidance;
        synchronized (DataEyeGetPushFrontAvoidance.class) {
            if (instance == null) {
                instance = new DataEyeGetPushFrontAvoidance(true);
            }
            dataEyeGetPushFrontAvoidance = instance;
        }
        return dataEyeGetPushFrontAvoidance;
    }

    public DataEyeGetPushFrontAvoidance(boolean register) {
        super(register);
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        DJIVisionHelper.getInstance().updateAvoidDatas(this);
        super.setPushRecData(data);
    }

    /* access modifiers changed from: protected */
    public boolean isWantPush() {
        return super.isWantPush();
    }

    public SensorType getSensorType() {
        return SensorType.find((((Integer) get(0, 1, Integer.class)).intValue() & 224) >> 5);
    }

    public int getObserveCount() {
        return ((Integer) get(0, 1, Integer.class)).intValue() & 31;
    }

    public int[] getObserveValues() {
        int count = getObserveCount();
        if (count == 0) {
            return null;
        }
        if (this.mCacheData == null || count != this.mCacheData.length) {
            this.mCacheData = new int[count];
        }
        Arrays.fill(this.mCacheData, Integer.MAX_VALUE);
        int i = 0;
        while (i < count && (i * 3) + 3 <= this._recData.length) {
            this.mCacheData[i] = ((Integer) get((i * 3) + 1, 2, Integer.class)).intValue();
            i++;
        }
        return this.mCacheData;
    }

    public int[] getObserveLevels() {
        int count = getObserveCount();
        if (count == 0) {
            return null;
        }
        if (this.mCacheLevels == null || count != this.mCacheLevels.length) {
            this.mCacheLevels = new int[count];
        }
        Arrays.fill(this.mCacheLevels, Integer.MAX_VALUE);
        int i = 0;
        while (i < count && (i * 3) + 4 <= this._recData.length) {
            this.mCacheLevels[i] = ((Integer) get((i * 3) + 3, 1, Integer.class)).intValue();
            i++;
        }
        return this.mCacheLevels;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum SensorType {
        Front(0),
        Back(1),
        Right(2),
        Left(3),
        Top(4),
        Bottom(5),
        OTHER(100);
        
        private final int data;

        private SensorType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static SensorType find(int b) {
            SensorType result = Front;
            SensorType[] values = values();
            for (SensorType tmp : values) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}

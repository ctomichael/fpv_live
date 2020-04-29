package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.logic.vision.DJIVisionHelper;
import dji.midware.data.manager.P3.DataBase;
import dji.thirdparty.afinal.core.Arrays;

@Keep
@EXClassNullAway
public class DataEyeGetPushOmniAvoidance extends DataBase {
    private int[] mCacheDatas = null;
    private int[] mCacheDistances = null;
    private int[] mCacheLevels = null;

    @Keep
    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final DataEyeGetPushOmniAvoidance INSTANCE = new DataEyeGetPushOmniAvoidance(true);

        private SingletonHolder() {
        }
    }

    public static DataEyeGetPushOmniAvoidance getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public DataEyeGetPushOmniAvoidance(boolean register) {
        super(register);
    }

    /* access modifiers changed from: protected */
    public void setPushRecData(byte[] data) {
        DJIVisionHelper.getInstance().updateOmniAvoidanceDatas(this);
        super.setPushRecData(data);
    }

    /* access modifiers changed from: protected */
    public boolean isWantPush() {
        return super.isWantPush();
    }

    public Direction getDirection() {
        return Direction.find((((Integer) get(0, 1, Integer.class)).intValue() & 224) >> 5);
    }

    public short getObserveSize() {
        return ((Short) get(0, 2, Short.class)).shortValue();
    }

    public int getObserveCount() {
        return ((Integer) get(0, 1, Integer.class)).intValue() & 31;
    }

    public int[] getObserveValues() {
        int count = getObserveCount();
        if (count == 0) {
            return null;
        }
        if (this.mCacheDatas == null || count != this.mCacheDatas.length) {
            this.mCacheDatas = new int[count];
        }
        Arrays.fill(this.mCacheDatas, Integer.MAX_VALUE);
        int i = 0;
        while (i < count && (i * 3) + 3 <= this._recData.length) {
            this.mCacheDatas[i] = ((Integer) get((i * 3) + 1, 2, Integer.class)).intValue();
            i++;
        }
        return this.mCacheDatas;
    }

    public int[] getObserveDistances() {
        int observeSize = getObserveSize();
        if (observeSize == 0) {
            return null;
        }
        if (this.mCacheDistances == null || observeSize != this.mCacheDistances.length) {
            this.mCacheDistances = new int[observeSize];
        }
        Arrays.fill(this.mCacheDistances, Integer.MAX_VALUE);
        for (int i = 0; i < observeSize; i++) {
            this.mCacheDistances[i] = ((Integer) get((i + 2) * 2, 2, Integer.class)).intValue();
        }
        return this.mCacheDistances;
    }

    public short getTimeStamp() {
        return ((Short) get(2, 2, Short.class)).shortValue();
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
    public enum Direction {
        FRONT(0),
        BACK(1),
        RIGHT(2),
        LEFT(3),
        TOP(4),
        BOTTOM(5),
        OTHER(-1);
        
        private final int data;

        private Direction(int data2) {
            this.data = data2;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static Direction find(int b) {
            Direction result = FRONT;
            Direction[] values = values();
            for (Direction direction : values) {
                if (direction._equals(b)) {
                    return direction;
                }
            }
            return result;
        }
    }
}

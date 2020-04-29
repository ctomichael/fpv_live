package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.midware.data.manager.P3.DataBase;
import java.util.ArrayList;
import java.util.List;

@Keep
public class DataFlycGetPushNewUnlimitState extends DataBase {
    private static final int LENGTH_HEADER = 3;
    private static DataFlycGetPushNewUnlimitState instance;
    private boolean isCurDataChanged = false;
    private List<Integer> mUnlockAreaIds = new ArrayList();
    private List<Integer> mUnlockCountryCode = new ArrayList();
    private WhiteListPushCircle mWhiteListPushCircle = new WhiteListPushCircle();
    private WhiteListPushPolygon mWhiteListPushPolygon = new WhiteListPushPolygon();

    @Keep
    public static class WhiteListPushCircle {
        public int mHeightLimit;
        public double mLat;
        public double mLng;
        public int mRadius;
    }

    @Keep
    public static class WhiteListPushPolygon {
        public int mHeightLimit;
        public List<Double> mLats = new ArrayList();
        public List<Double> mLngs = new ArrayList();
        public int mPointNum;
    }

    public static synchronized DataFlycGetPushNewUnlimitState getInstance() {
        DataFlycGetPushNewUnlimitState dataFlycGetPushNewUnlimitState;
        synchronized (DataFlycGetPushNewUnlimitState.class) {
            if (instance == null) {
                instance = new DataFlycGetPushNewUnlimitState();
            }
            dataFlycGetPushNewUnlimitState = instance;
        }
        return dataFlycGetPushNewUnlimitState;
    }

    private DataFlycGetPushNewUnlimitState() {
        this.isPushLosed = true;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    public List<Integer> getUnlockAreaIds() {
        return new ArrayList(this.mUnlockAreaIds);
    }

    public List<Integer> getUnlockCountryCode() {
        return new ArrayList(this.mUnlockCountryCode);
    }

    public WhiteListPushCircle getWhiteListPushCircle() {
        return this.mWhiteListPushCircle;
    }

    public int getVersion() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public boolean isUnlockHeightLimit() {
        return (((Integer) get(1, 1, Integer.class)).intValue() & 1) == 1;
    }

    public int getHeightLimit() {
        return ((Integer) get(1, 2, Integer.class)).intValue() >> 1;
    }

    public int getUnlockAreaNum() {
        return ((Integer) get(3, 1, Integer.class)).intValue();
    }

    public int getUnlockCountryNum() {
        return ((Integer) get((getUnlockAreaNum() * 4) + 4, 1, Integer.class)).intValue();
    }

    public ValidWhiteListAreaType getValidWhiteListType() {
        return ValidWhiteListAreaType.find(((Integer) get((getUnlockAreaNum() * 4) + 4 + 1 + (getUnlockCountryNum() * 2), 1, Integer.class)).intValue());
    }

    public WhiteListPushPolygon getWhiteListPushPolygon() {
        return this.mWhiteListPushPolygon;
    }

    /* access modifiers changed from: protected */
    public boolean isChanged(byte[] data) {
        this.isCurDataChanged = super.isChanged(data);
        return true;
    }

    /* access modifiers changed from: protected */
    public void post() {
        extractUnlockAreaIds();
        extractUnlockCountryCodes();
        extractWhiteListPushData();
        super.post();
    }

    private void extractUnlockAreaIds() {
        if (this.isCurDataChanged) {
            this.mUnlockAreaIds.clear();
            int num = getUnlockAreaNum();
            int startIndex = 4;
            for (int i = 0; i < num && startIndex < this._recData.length; i++) {
                this.mUnlockAreaIds.add(Integer.valueOf(((Integer) get(startIndex, 4, Integer.class)).intValue()));
                startIndex += 4;
            }
        }
    }

    private void extractUnlockCountryCodes() {
        if (this.isCurDataChanged) {
            this.mUnlockCountryCode.clear();
            int num = getUnlockCountryNum();
            int startIndex = (getUnlockAreaNum() * 4) + 4 + 1;
            for (int i = 0; i < num && startIndex < this._recData.length; i++) {
                this.mUnlockCountryCode.add(Integer.valueOf(((Integer) get(startIndex, 2, Integer.class)).intValue()));
                startIndex += 2;
            }
        }
    }

    private void extractWhiteListPushData() {
        if (this.isCurDataChanged) {
            int startIndex = (getUnlockAreaNum() * 4) + 4 + 1 + (getUnlockCountryNum() * 2) + 1;
            ValidWhiteListAreaType curType = getValidWhiteListType();
            if (curType == ValidWhiteListAreaType.CIRCLE) {
                this.mWhiteListPushCircle.mHeightLimit = ((Integer) get(startIndex, 4, Integer.class)).intValue();
                int startIndex2 = startIndex + 4;
                this.mWhiteListPushCircle.mLat = ((double) ((Integer) get(startIndex2, 4, Integer.class)).intValue()) / 1000000.0d;
                int startIndex3 = startIndex2 + 4;
                this.mWhiteListPushCircle.mLng = ((double) ((Integer) get(startIndex3, 4, Integer.class)).intValue()) / 1000000.0d;
                this.mWhiteListPushCircle.mRadius = ((Integer) get(startIndex3 + 4, 4, Integer.class)).intValue();
            } else if (curType == ValidWhiteListAreaType.POLYGON) {
                this.mWhiteListPushPolygon.mHeightLimit = ((Integer) get(startIndex, 4, Integer.class)).intValue();
                int startIndex4 = startIndex + 4;
                this.mWhiteListPushPolygon.mPointNum = ((Integer) get(startIndex4, 1, Integer.class)).intValue();
                int startIndex5 = startIndex4 + 1;
                this.mWhiteListPushPolygon.mLats.clear();
                this.mWhiteListPushPolygon.mLngs.clear();
                for (int i = 0; i < this.mWhiteListPushPolygon.mPointNum; i++) {
                    this.mWhiteListPushPolygon.mLats.add(Double.valueOf(((double) ((Integer) get(startIndex5, 4, Integer.class)).intValue()) / 1000000.0d));
                    int startIndex6 = startIndex5 + 4;
                    this.mWhiteListPushPolygon.mLngs.add(Double.valueOf(((double) ((Integer) get(startIndex6, 4, Integer.class)).intValue()) / 1000000.0d));
                    startIndex5 = startIndex6 + 4;
                }
            }
        }
    }

    @Keep
    public enum ValidWhiteListAreaType {
        NON(0),
        CIRCLE(1),
        POLYGON(2),
        OTHER(100);
        
        private int data;

        private ValidWhiteListAreaType(int i) {
            this.data = i;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static ValidWhiteListAreaType find(int b) {
            ValidWhiteListAreaType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

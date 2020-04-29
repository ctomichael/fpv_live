package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataEyeGetPushFlatCheck extends DataBase {
    private static final String TAG = "DataEyeGetPushFlatCheck";
    private static DataEyeGetPushFlatCheck instance = null;

    public static synchronized DataEyeGetPushFlatCheck getInstance() {
        DataEyeGetPushFlatCheck dataEyeGetPushFlatCheck;
        synchronized (DataEyeGetPushFlatCheck.class) {
            if (instance == null) {
                instance = new DataEyeGetPushFlatCheck();
            }
            dataEyeGetPushFlatCheck = instance;
        }
        return dataEyeGetPushFlatCheck;
    }

    private DataEyeGetPushFlatCheck() {
        this.isNeedPushLosed = true;
    }

    public int getTinkCount() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public FlatStatus getFlatStatus() {
        if (this._recData == null || this._recData.length <= 1) {
            return FlatStatus.None;
        }
        return FlatStatus.find(this._recData[1]);
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    /* access modifiers changed from: protected */
    public void setPushLose() {
        this.isPushLosed = true;
        clear();
    }

    @Keep
    public enum FlatStatus {
        None(0),
        Calculating(1),
        SafeForLanding(2),
        UnsafeToHover(3),
        WaterSurfaceToHover(4),
        EnterCheckArea(10),
        UnderExposure(-1),
        DriftMuchWhenLanding(-2),
        MoveStickWhenCalculating(-3),
        TooLow(-4),
        TooHigh(-5),
        BadResult(-10),
        OTHER(100);
        
        private static FlatStatus[] sValues = null;
        private final int data;

        private FlatStatus(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static FlatStatus find(int b) {
            if (sValues == null) {
                sValues = values();
            }
            FlatStatus result = None;
            FlatStatus[] flatStatusArr = sValues;
            for (FlatStatus tmp : flatStatusArr) {
                if (tmp._equals(b)) {
                    return tmp;
                }
            }
            return result;
        }
    }
}

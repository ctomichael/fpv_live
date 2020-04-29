package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataA2PushCommom extends DataBase {
    private static DataA2PushCommom mInstance = null;

    public static DataA2PushCommom getInstance() {
        if (mInstance == null) {
            mInstance = new DataA2PushCommom();
        }
        return mInstance;
    }

    public int getVerType() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getVersion() {
        return ((Integer) get(1, 1, Integer.class)).intValue();
    }

    public int getFirmwareVersion() {
        return ((Integer) get(2, 4, Integer.class)).intValue();
    }

    public int getLoaderVersion() {
        return ((Integer) get(6, 4, Integer.class)).intValue();
    }

    public DJIA2CtrlMode getCtrlMode() {
        return DJIA2CtrlMode.find(((Integer) get(10, 1, Integer.class)).intValue());
    }

    public int getIntelligenceOrientationEnabled() {
        if (getCtrlMode() == DJIA2CtrlMode.Manual || getCtrlMode() == DJIA2CtrlMode.SAFE) {
            return 0;
        }
        return ((Integer) get(11, 1, Integer.class)).intValue();
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum DJIA2CtrlMode {
        Manual(0),
        Atti(1),
        AttiCL(2),
        P_GPS(6),
        P_GPS_CL(7),
        P_GPS_HL(8),
        SAFE(18);
        
        private int _data;

        private DJIA2CtrlMode(int val) {
            this._data = val;
        }

        public int value() {
            return this._data;
        }

        public boolean _equals(int b) {
            return this._data == b;
        }

        public static DJIA2CtrlMode find(int val) {
            DJIA2CtrlMode result = Manual;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(val)) {
                    return values()[i];
                }
            }
            return result;
        }
    }
}

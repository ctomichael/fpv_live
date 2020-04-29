package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.log.DJILog;
import dji.midware.data.manager.P3.DataBase;

@Keep
@EXClassNullAway
public class DataGimbalGetPushType extends DataBase {
    private static final String TAG = "DataGimbalGetPushType";
    private static DataGimbalGetPushType instance = null;

    public static synchronized DataGimbalGetPushType getInstance() {
        DataGimbalGetPushType dataGimbalGetPushType;
        synchronized (DataGimbalGetPushType.class) {
            if (instance == null) {
                instance = new DataGimbalGetPushType();
                instance.isNeedPushLosed = true;
                instance.isRemoteModel = true;
            }
            dataGimbalGetPushType = instance;
        }
        return dataGimbalGetPushType;
    }

    public DataGimbalGetPushType() {
    }

    public DataGimbalGetPushType(boolean isRegist) {
        super(isRegist);
    }

    public DJIGimbalType getType() {
        return getType(-1);
    }

    public DJIGimbalType getType(int index) {
        return DJIGimbalType.find(((Short) get(0, 1, Short.class, index)).shortValue());
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum DJIGimbalType {
        TIMEOUT(0),
        FAULT(1),
        FC550(2),
        FC300SX(3),
        FC260(4),
        FC350(5),
        FC350Z(6),
        Z15(7),
        P4(8),
        XT(9),
        Ronin(10),
        D5(11),
        GH4(12),
        A7(13),
        BMPCC(14),
        WM330(15),
        WM331(16),
        WM620_X5S(17),
        WM620_OneInch(18),
        GD600(19),
        WM220(20),
        WM620_FullFrame(21),
        WM620_X7(22),
        PALYOAD_GIMBAL(38),
        Ronin2(23),
        HG300(36),
        HG301(37),
        OTHER(100);
        
        private int data;

        private DJIGimbalType(int _data) {
            this.data = _data;
        }

        public int value() {
            return this.data;
        }

        public boolean _equals(int b) {
            return this.data == b;
        }

        public static DJIGimbalType find(int b) {
            DJIGimbalType result = OTHER;
            for (int i = 0; i < values().length; i++) {
                if (values()[i]._equals(b)) {
                    return values()[i];
                }
            }
            return result;
        }
    }

    public boolean hasSecondaryRecData() {
        return (this.recDatas == null || this.recDatas.get(2) == null) ? false : true;
    }

    public boolean hasPrimaryRecData() {
        return (this.recDatas == null || this.recDatas.get(0) == null) ? false : true;
    }

    public void clear() {
        super.clear();
        DJILog.d(TAG, "clear data in DataGimbalGetPushType : recDatas" + this.recDatas, new Object[0]);
    }
}

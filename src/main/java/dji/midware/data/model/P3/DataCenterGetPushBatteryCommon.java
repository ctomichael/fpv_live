package dji.midware.data.model.P3;

import android.support.annotation.Keep;
import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.manager.P3.DataBase;
import dji.midware.data.packages.P3.Pack;

@Keep
@EXClassNullAway
public class DataCenterGetPushBatteryCommon extends DataBase {
    private static final int FLAG_DAY = 31;
    private static final int FLAG_MONTH = 480;
    private static final int FLAG_YEAR = 65024;
    private static DataCenterGetPushBatteryCommon instance = null;
    private final int[] mPartVoltages = new int[6];
    private final int[] mProductDate = {2013, 1, 1};

    public static synchronized DataCenterGetPushBatteryCommon getInstance() {
        DataCenterGetPushBatteryCommon dataCenterGetPushBatteryCommon;
        synchronized (DataCenterGetPushBatteryCommon.class) {
            if (instance == null) {
                instance = new DataCenterGetPushBatteryCommon();
            }
            dataCenterGetPushBatteryCommon = instance;
        }
        return dataCenterGetPushBatteryCommon;
    }

    public DataCenterGetPushBatteryCommon() {
    }

    public DataCenterGetPushBatteryCommon(boolean isRegist) {
        super(isRegist);
    }

    /* access modifiers changed from: protected */
    public void setPushRecPack(Pack pack) {
        if (pack == null || pack.data == null || pack.data.length != 21) {
            super.setPushRecPack(pack);
        }
    }

    public int getRelativeCapacity() {
        return ((Integer) get(0, 1, Integer.class)).intValue();
    }

    public int getCurrentPV() {
        return ((Integer) get(1, 2, Integer.class)).intValue();
    }

    public int getCurrentCapacity() {
        return ((Integer) get(3, 2, Integer.class)).intValue();
    }

    public int getFullCapacity() {
        return ((Integer) get(5, 2, Integer.class)).intValue();
    }

    public int getLife() {
        return ((Integer) get(7, 1, Integer.class)).intValue();
    }

    public int getLoopNum() {
        return ((Integer) get(8, 2, Integer.class)).intValue();
    }

    public int getErrorType() {
        return ((Integer) get(10, 4, Integer.class)).intValue();
    }

    public int getCurrent() {
        return ((Integer) get(14, 2, Integer.class)).intValue();
    }

    public int[] getPartVoltages() {
        int i = 0;
        int offset = 16;
        int length = this.mPartVoltages.length;
        while (i < length) {
            this.mPartVoltages[i] = ((Integer) get(offset, 2, Integer.class)).intValue();
            i++;
            offset += 2;
        }
        return this.mPartVoltages;
    }

    public int getSerialNo() {
        return ((Integer) get(28, 2, Integer.class)).intValue();
    }

    public int[] getProductDate() {
        int date = ((Integer) get(30, 2, Integer.class)).intValue();
        this.mProductDate[0] = ((FLAG_YEAR & date) >>> 9) + 1980;
        this.mProductDate[1] = (date & FLAG_MONTH) >>> 5;
        this.mProductDate[2] = date & 31;
        return this.mProductDate;
    }

    public int getTemperature() {
        return ((Integer) get(32, 2, Integer.class)).intValue();
    }

    public ConnStatus getConnStatus() {
        return ConnStatus.ofData(((Integer) get(34, 1, Integer.class)).intValue());
    }

    public int totalStudyCycle() {
        return ((Integer) get(35, 2, Integer.class)).intValue();
    }

    public int lastStudyCycle() {
        if (this._recData.length < 38) {
            return getLoopNum();
        }
        return ((Integer) get(37, 2, Integer.class)).intValue();
    }

    public boolean isNeedStudy() {
        return getLoopNum() - lastStudyCycle() > 15;
    }

    public boolean isBatteryOnCharge() {
        return ((Integer) get(39, 2, Integer.class)).intValue() == 1;
    }

    /* access modifiers changed from: protected */
    public void doPack() {
    }

    @Keep
    public enum ConnStatus {
        NORMAL(0),
        INVALID(1),
        EXCEPTION(2),
        OTHER(100);
        
        private int mData = 0;

        private ConnStatus(int data) {
            this.mData = data;
        }

        public int value() {
            return this.mData;
        }

        private boolean belongsTo(int data) {
            return this.mData == data;
        }

        public static ConnStatus ofData(int data) {
            ConnStatus[] values = values();
            for (ConnStatus cs : values) {
                if (cs.belongsTo(data)) {
                    return cs;
                }
            }
            return OTHER;
        }
    }
}

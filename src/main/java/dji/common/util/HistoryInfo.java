package dji.common.util;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataCenterGetPushBatteryCommon;

@EXClassNullAway
public class HistoryInfo {
    private static final int FLAG_DISCHARGE = 131072;
    private static final long FLAG_DISCHARGE_NEW = 2097152;
    private static final int FLAG_FIRSTLEVEL_CURRENT = 1;
    private static final long FLAG_FIRSTLEVEL_CURRENT_NEW = 1;
    private static final int FLAG_FIRSTLEVEL_LOWTEMP = 16;
    private static final long FLAG_FIRSTLEVEL_LOWTEMP_NEW = 16;
    private static final int FLAG_FIRSTLEVEL_OVERTEMP = 4;
    private static final long FLAG_FIRSTLEVEL_OVERTEMP_NEW = 4;
    private static final int FLAG_INVALID = 7168;
    private static final long FLAG_INVALID_NEW = 126976;
    private static final int FLAG_REPLACE = 57344;
    private static final int FLAG_SECONDLEVEL_CURRENT = 2;
    private static final long FLAG_SECONDLEVEL_CURRENT_NEW = 2;
    private static final int FLAG_SECONDLEVEL_LOWTEMP = 32;
    private static final long FLAG_SECONDLEVEL_LOWTEMP_NEW = 32;
    private static final int FLAG_SECONDLEVEL_OVERTEMP = 8;
    private static final long FLAG_SECONDLEVEL_OVERTEMP_NEW = 8;
    private static final int FLAG_SHORT_CIRCUIT = 64;
    private static final long FLAG_SHORT_CIRCUIT_NEW = 64;
    private static final int FLAG_UNDER_VOLTAGE = 896;
    private static final long FLAG_UNDER_VOLTAGE_NEW = 3968;
    private static final int FLAG_WATCHDOG_RESET = 65536;
    private static final long FLAG_WATCHDOG_RESET_NEW = 1048576;
    public static final byte VALUE_ALREADY_REPLACE = 1;
    public static final byte VALUE_NO_REPLACE = 0;
    private DataCenterGetPushBatteryCommon.ConnStatus connStatus = DataCenterGetPushBatteryCommon.ConnStatus.NORMAL;
    private boolean dischargeFlag = false;
    private boolean firstLevelCurrent = false;
    private boolean firstLevelLowTemp = false;
    private boolean firstLevelOverTemp = false;
    private byte invalidNum = 0;
    private int originalError = 0;
    private long originalNewError = 0;
    private byte replaceFlag = 0;
    private boolean secondLevelCurrent = false;
    private boolean secondLevelLowTemp = false;
    private boolean secondLevelOverTemp = false;
    private boolean shortCircuit = false;
    private byte underVoltageNum = 0;
    private boolean watchDogReset = false;

    public boolean hasError() {
        return hasFirstLevelCurrent() || hasSecondLevelCurrent() || hasFirstLevelOverTemp() || hasSecondLevelOverTemp() || hasFirstLevelLowTemp() || hasSecondLevelLowTemp() || hasShortCircuit() || getUnderVoltageNum() != 0 || getInvalidNum() != 0 || hasDischargeSelf() || this.connStatus != DataCenterGetPushBatteryCommon.ConnStatus.NORMAL;
    }

    public boolean hasConnError() {
        return this.connStatus != DataCenterGetPushBatteryCommon.ConnStatus.NORMAL;
    }

    public DataCenterGetPushBatteryCommon.ConnStatus getConnStatus() {
        return this.connStatus;
    }

    public boolean hasFirstLevelCurrent() {
        return this.firstLevelCurrent;
    }

    public boolean hasSecondLevelCurrent() {
        return this.secondLevelCurrent;
    }

    public boolean hasFirstLevelOverTemp() {
        return this.firstLevelOverTemp;
    }

    public boolean hasSecondLevelOverTemp() {
        return this.secondLevelOverTemp;
    }

    public boolean hasFirstLevelLowTemp() {
        return this.firstLevelLowTemp;
    }

    public boolean hasSecondLevelLowTemp() {
        return this.secondLevelLowTemp;
    }

    public boolean hasShortCircuit() {
        return this.shortCircuit;
    }

    public byte getUnderVoltageNum() {
        return this.underVoltageNum;
    }

    public byte getInvalidNum() {
        return this.invalidNum;
    }

    public byte getReplaceFlag() {
        return this.replaceFlag;
    }

    public boolean hasWatchDogReset() {
        return this.watchDogReset;
    }

    public boolean hasDischargeSelf() {
        return this.dischargeFlag;
    }

    public void parseSimple(int error) {
        resetSimple();
        this.originalError = error;
        this.underVoltageNum = (byte) ((error & FLAG_UNDER_VOLTAGE) >>> 7);
        this.invalidNum = (byte) ((error & FLAG_INVALID) >>> 10);
    }

    public void parse(int error) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8 = true;
        reset();
        this.originalError = error;
        this.firstLevelCurrent = (error & 1) != 0;
        if (((error & 2) >>> 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.secondLevelCurrent = z;
        if (((error & 4) >>> 2) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.firstLevelOverTemp = z2;
        if (((error & 8) >>> 3) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.secondLevelOverTemp = z3;
        if (((error & 16) >>> 4) != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.firstLevelLowTemp = z4;
        if (((error & 32) >>> 5) != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.secondLevelLowTemp = z5;
        if (((error & 64) >>> 6) != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.shortCircuit = z6;
        this.underVoltageNum = (byte) ((error & FLAG_UNDER_VOLTAGE) >>> 7);
        this.invalidNum = (byte) ((error & FLAG_INVALID) >>> 10);
        this.replaceFlag = (byte) ((FLAG_REPLACE & error) >>> 13);
        if (((65536 & error) >>> 16) != 0) {
            z7 = true;
        } else {
            z7 = false;
        }
        this.watchDogReset = z7;
        if (((131072 & error) >>> 17) == 0) {
            z8 = false;
        }
        this.dischargeFlag = z8;
    }

    public void updateConnStatus(DataCenterGetPushBatteryCommon.ConnStatus cs) {
        this.connStatus = cs;
    }

    public void reset() {
        this.firstLevelCurrent = false;
        this.secondLevelCurrent = false;
        this.firstLevelOverTemp = false;
        this.secondLevelOverTemp = false;
        this.firstLevelLowTemp = false;
        this.secondLevelLowTemp = false;
        this.shortCircuit = false;
        this.underVoltageNum = 0;
        this.invalidNum = 0;
        this.replaceFlag = 0;
        this.watchDogReset = false;
        this.dischargeFlag = false;
    }

    public void resetSimple() {
        this.underVoltageNum = 0;
        this.invalidNum = 0;
    }

    public boolean equals(Object o) {
        boolean ret = super.equals(o);
        if (ret || !(o instanceof HistoryInfo) || this.originalError != ((HistoryInfo) o).originalError) {
            return ret;
        }
        return true;
    }

    public int hashCode() {
        return this.originalError;
    }

    public String toString() {
        return Integer.toBinaryString(this.originalError);
    }

    public void parse(long error) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8 = true;
        reset();
        this.originalNewError = error;
        this.firstLevelCurrent = (1 & error) != 0;
        if ((2 & error) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.secondLevelCurrent = z;
        if ((4 & error) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.firstLevelOverTemp = z2;
        if ((8 & error) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.secondLevelOverTemp = z3;
        if ((16 & error) != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.firstLevelLowTemp = z4;
        if ((32 & error) != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.secondLevelLowTemp = z5;
        if ((64 & error) != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.shortCircuit = z6;
        this.underVoltageNum = (byte) ((int) ((FLAG_UNDER_VOLTAGE_NEW & error) >> 7));
        this.invalidNum = (byte) ((int) ((FLAG_INVALID_NEW & error) >> 12));
        this.replaceFlag = 0;
        if ((1048576 & error) != 0) {
            z7 = true;
        } else {
            z7 = false;
        }
        this.watchDogReset = z7;
        if ((2097152 & error) == 0) {
            z8 = false;
        }
        this.dischargeFlag = z8;
    }
}

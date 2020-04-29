package dji.pilot.battery.control;

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
    private DataCenterGetPushBatteryCommon.ConnStatus mConnStatus = DataCenterGetPushBatteryCommon.ConnStatus.NORMAL;
    private boolean mDischargeFlag = false;
    private boolean mFirstLevelCurrent = false;
    private boolean mFirstLevelLowTemp = false;
    private boolean mFirstLevelOverTemp = false;
    private byte mInvalidNum = 0;
    private int mOriginalError = 0;
    private long mOriginalNewError = 0;
    private byte mReplaceFlag = 0;
    private boolean mSecondLevelCurrent = false;
    private boolean mSecondLevelLowTemp = false;
    private boolean mSecondLevelOverTemp = false;
    private boolean mShortCircuit = false;
    private byte mUnderVoltageNum = 0;
    private boolean mWatchDogReset = false;

    public boolean hasError() {
        return hasFirstLevelCurrent() || hasSecondLevelCurrent() || hasFirstLevelOverTemp() || hasSecondLevelOverTemp() || hasFirstLevelLowTemp() || hasSecondLevelLowTemp() || hasShortCircuit() || getUnderVoltageNum() != 0 || getInvalidNum() != 0 || hasDischargeSelf() || this.mConnStatus != DataCenterGetPushBatteryCommon.ConnStatus.NORMAL;
    }

    public boolean hasConnError() {
        return this.mConnStatus != DataCenterGetPushBatteryCommon.ConnStatus.NORMAL;
    }

    public DataCenterGetPushBatteryCommon.ConnStatus getConnStatus() {
        return this.mConnStatus;
    }

    public boolean hasFirstLevelCurrent() {
        return this.mFirstLevelCurrent;
    }

    public boolean hasSecondLevelCurrent() {
        return this.mSecondLevelCurrent;
    }

    public boolean hasFirstLevelOverTemp() {
        return this.mFirstLevelOverTemp;
    }

    public boolean hasSecondLevelOverTemp() {
        return this.mSecondLevelOverTemp;
    }

    public boolean hasFirstLevelLowTemp() {
        return this.mFirstLevelLowTemp;
    }

    public boolean hasSecondLevelLowTemp() {
        return this.mSecondLevelLowTemp;
    }

    public boolean hasShortCircuit() {
        return this.mShortCircuit;
    }

    public byte getUnderVoltageNum() {
        return this.mUnderVoltageNum;
    }

    public byte getInvalidNum() {
        return this.mInvalidNum;
    }

    public byte getReplaceFlag() {
        return this.mReplaceFlag;
    }

    public boolean hasWatchDogReset() {
        return this.mWatchDogReset;
    }

    public boolean hasDischargeSelf() {
        return this.mDischargeFlag;
    }

    public void parseSimple(int error) {
        resetSimple();
        this.mOriginalError = error;
        this.mUnderVoltageNum = (byte) ((error & FLAG_UNDER_VOLTAGE) >>> 7);
        this.mInvalidNum = (byte) ((error & FLAG_INVALID) >>> 10);
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
        this.mOriginalError = error;
        this.mFirstLevelCurrent = (error & 1) != 0;
        if (((error & 2) >>> 1) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mSecondLevelCurrent = z;
        if (((error & 4) >>> 2) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mFirstLevelOverTemp = z2;
        if (((error & 8) >>> 3) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.mSecondLevelOverTemp = z3;
        if (((error & 16) >>> 4) != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.mFirstLevelLowTemp = z4;
        if (((error & 32) >>> 5) != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.mSecondLevelLowTemp = z5;
        if (((error & 64) >>> 6) != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.mShortCircuit = z6;
        this.mUnderVoltageNum = (byte) ((error & FLAG_UNDER_VOLTAGE) >>> 7);
        this.mInvalidNum = (byte) ((error & FLAG_INVALID) >>> 10);
        this.mReplaceFlag = (byte) ((FLAG_REPLACE & error) >>> 13);
        if (((65536 & error) >>> 16) != 0) {
            z7 = true;
        } else {
            z7 = false;
        }
        this.mWatchDogReset = z7;
        if (((131072 & error) >>> 17) == 0) {
            z8 = false;
        }
        this.mDischargeFlag = z8;
    }

    public void updateConnStatus(DataCenterGetPushBatteryCommon.ConnStatus cs) {
        this.mConnStatus = cs;
    }

    public void reset() {
        this.mFirstLevelCurrent = false;
        this.mSecondLevelCurrent = false;
        this.mFirstLevelOverTemp = false;
        this.mSecondLevelOverTemp = false;
        this.mFirstLevelLowTemp = false;
        this.mSecondLevelLowTemp = false;
        this.mShortCircuit = false;
        this.mUnderVoltageNum = 0;
        this.mInvalidNum = 0;
        this.mReplaceFlag = 0;
        this.mWatchDogReset = false;
        this.mDischargeFlag = false;
    }

    public void resetSimple() {
        this.mUnderVoltageNum = 0;
        this.mInvalidNum = 0;
    }

    public boolean equals(Object o) {
        boolean ret = super.equals(o);
        if (ret || !(o instanceof HistoryInfo) || this.mOriginalError != ((HistoryInfo) o).mOriginalError) {
            return ret;
        }
        return true;
    }

    public int hashCode() {
        return this.mOriginalError;
    }

    public String toString() {
        return Integer.toBinaryString(this.mOriginalError);
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
        this.mOriginalNewError = error;
        this.mFirstLevelCurrent = (1 & error) != 0;
        if ((2 & error) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mSecondLevelCurrent = z;
        if ((4 & error) != 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mFirstLevelOverTemp = z2;
        if ((8 & error) != 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.mSecondLevelOverTemp = z3;
        if ((16 & error) != 0) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.mFirstLevelLowTemp = z4;
        if ((32 & error) != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.mSecondLevelLowTemp = z5;
        if ((64 & error) != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.mShortCircuit = z6;
        this.mUnderVoltageNum = (byte) ((int) ((FLAG_UNDER_VOLTAGE_NEW & error) >> 7));
        this.mInvalidNum = (byte) ((int) ((FLAG_INVALID_NEW & error) >> 12));
        this.mReplaceFlag = 0;
        if ((1048576 & error) != 0) {
            z7 = true;
        } else {
            z7 = false;
        }
        this.mWatchDogReset = z7;
        if ((2097152 & error) == 0) {
            z8 = false;
        }
        this.mDischargeFlag = z8;
    }
}

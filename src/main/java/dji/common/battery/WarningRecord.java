package dji.common.battery;

import android.support.v4.media.session.PlaybackStateCompat;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class WarningRecord {
    private boolean batteryAbnormalConnection = false;
    private boolean batteryLessTenPerc = false;
    private boolean batteryUnderVoltage = false;
    private boolean bigDiffPressureWithMove = false;
    private boolean bigDiffPressureWithoutMove = false;
    private boolean currentOverloaded;
    private boolean customDischargeEnabled;
    private short damagedCellIndex;
    private boolean exception = false;
    private boolean lowTemperature;
    private short lowVoltageCellIndex;
    private boolean overHeated;
    private boolean reRangement = false;
    private boolean shortCircuited;
    private boolean tooLittleBattery = false;

    public WarningRecord(boolean currentOverloaded2, boolean overHeated2, boolean lowTemperature2, boolean shortCircuited2, boolean customDischargeEnabled2, short lowVoltageCellIndex2, short damagedCellIndex2) {
        this.currentOverloaded = currentOverloaded2;
        this.overHeated = overHeated2;
        this.lowTemperature = lowTemperature2;
        this.shortCircuited = shortCircuited2;
        this.customDischargeEnabled = customDischargeEnabled2;
        this.lowVoltageCellIndex = (short) (lowVoltageCellIndex2 - 1);
        this.damagedCellIndex = (short) (damagedCellIndex2 - 1);
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int i9;
        int i10;
        int i11;
        int i12 = 1;
        if (this.currentOverloaded) {
            result = 1;
        } else {
            result = 0;
        }
        int i13 = result * 31;
        if (this.overHeated) {
            i = 1;
        } else {
            i = 0;
        }
        int i14 = (i13 + i) * 31;
        if (this.lowTemperature) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i15 = (i14 + i2) * 31;
        if (this.shortCircuited) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i16 = (i15 + i3) * 31;
        if (this.customDischargeEnabled) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        int i17 = (i16 + i4) * 31;
        if (this.tooLittleBattery) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        int i18 = (i17 + i5) * 31;
        if (this.batteryAbnormalConnection) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        int i19 = (i18 + i6) * 31;
        if (this.bigDiffPressureWithoutMove) {
            i7 = 1;
        } else {
            i7 = 0;
        }
        int i20 = (i19 + i7) * 31;
        if (this.batteryUnderVoltage) {
            i8 = 1;
        } else {
            i8 = 0;
        }
        int i21 = (i20 + i8) * 31;
        if (this.batteryLessTenPerc) {
            i9 = 1;
        } else {
            i9 = 0;
        }
        int i22 = (i21 + i9) * 31;
        if (this.bigDiffPressureWithMove) {
            i10 = 1;
        } else {
            i10 = 0;
        }
        int i23 = (i22 + i10) * 31;
        if (this.exception) {
            i11 = 1;
        } else {
            i11 = 0;
        }
        int i24 = (i23 + i11) * 31;
        if (!this.reRangement) {
            i12 = 0;
        }
        return ((((i24 + i12) * 31) + this.lowVoltageCellIndex) * 31) + this.damagedCellIndex;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof WarningRecord)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.currentOverloaded == ((WarningRecord) o).currentOverloaded && this.overHeated == ((WarningRecord) o).overHeated && this.lowTemperature == ((WarningRecord) o).lowTemperature && this.shortCircuited == ((WarningRecord) o).shortCircuited && this.customDischargeEnabled == ((WarningRecord) o).customDischargeEnabled && this.tooLittleBattery == ((WarningRecord) o).tooLittleBattery && this.batteryAbnormalConnection == ((WarningRecord) o).batteryAbnormalConnection && this.bigDiffPressureWithoutMove == ((WarningRecord) o).bigDiffPressureWithoutMove && this.batteryUnderVoltage == ((WarningRecord) o).batteryUnderVoltage && this.batteryLessTenPerc == ((WarningRecord) o).batteryLessTenPerc && this.bigDiffPressureWithMove == ((WarningRecord) o).bigDiffPressureWithMove && this.exception == ((WarningRecord) o).exception && this.reRangement == ((WarningRecord) o).reRangement && this.lowVoltageCellIndex == ((WarningRecord) o).lowVoltageCellIndex && this.damagedCellIndex == ((WarningRecord) o).damagedCellIndex;
    }

    public boolean getException() {
        return this.exception;
    }

    public boolean getReRangement() {
        return this.reRangement;
    }

    public boolean isTooLittleBattery() {
        return this.tooLittleBattery;
    }

    public boolean isBatteryAbnormalConnection() {
        return this.batteryAbnormalConnection;
    }

    public boolean isBigDiffPressureWithoutMove() {
        return this.bigDiffPressureWithoutMove;
    }

    public boolean isBatteryUnderVoltage() {
        return this.batteryUnderVoltage;
    }

    public boolean isBatteryLessTenPerc() {
        return this.batteryLessTenPerc;
    }

    public boolean isBigDiffPressureWithMove() {
        return this.bigDiffPressureWithMove;
    }

    public WarningRecord(long data) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        boolean z6;
        boolean z7;
        boolean z8;
        boolean z9;
        boolean z10;
        boolean z11 = false;
        this.currentOverloaded = (data & 1) == 1 || (data & 2) == 2;
        if ((4 & data) == 4 || (8 & data) == 8) {
            z = true;
        } else {
            z = false;
        }
        this.overHeated = z;
        if ((16 & data) == 16 || (32 & data) == 32) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.lowTemperature = z2;
        if ((64 & data) == 64) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.shortCircuited = z3;
        if ((PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE & data) == PlaybackStateCompat.ACTION_SET_SHUFFLE_MODE) {
            z4 = true;
        } else {
            z4 = false;
        }
        this.customDischargeEnabled = z4;
        this.lowVoltageCellIndex = (short) ((int) ((3968 & data) - 1));
        this.damagedCellIndex = (short) ((int) ((126976 & data) - 1));
        if ((281474976710656L & data) != 0) {
            z5 = true;
        } else {
            z5 = false;
        }
        this.tooLittleBattery = z5;
        if ((562949953421312L & data) != 0) {
            z6 = true;
        } else {
            z6 = false;
        }
        this.batteryAbnormalConnection = z6;
        if ((2251799813685248L & data) != 0) {
            z7 = true;
        } else {
            z7 = false;
        }
        this.bigDiffPressureWithoutMove = z7;
        if ((9007199254740992L & data) != 0) {
            z8 = true;
        } else {
            z8 = false;
        }
        this.batteryUnderVoltage = z8;
        if ((18014398509481984L & data) != 0) {
            z9 = true;
        } else {
            z9 = false;
        }
        this.batteryLessTenPerc = z9;
        if ((1125899906842624L & data) != 0) {
            z10 = true;
        } else {
            z10 = false;
        }
        this.bigDiffPressureWithMove = z10;
        this.exception = (this.customDischargeEnabled || this.currentOverloaded || this.overHeated || this.lowTemperature || this.customDischargeEnabled || this.tooLittleBattery || this.batteryAbnormalConnection || this.bigDiffPressureWithoutMove || this.batteryUnderVoltage || this.batteryLessTenPerc) ? true : z11;
        this.reRangement = this.bigDiffPressureWithMove;
    }

    public WarningRecord(int data) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = true;
        this.currentOverloaded = (data & 1) == 1 || (data & 2) == 2;
        if ((data & 4) == 4 || (data & 8) == 8) {
            z = true;
        } else {
            z = false;
        }
        this.overHeated = z;
        if ((data & 16) == 16 || (data & 32) == 32) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.lowTemperature = z2;
        if ((data & 64) == 64) {
            z3 = true;
        } else {
            z3 = false;
        }
        this.shortCircuited = z3;
        this.customDischargeEnabled = (data & 131072) != 131072 ? false : z4;
        this.lowVoltageCellIndex = (short) (((data >> 7) & 8) - 1);
        this.damagedCellIndex = (short) (((data >> 10) & 8) - 1);
    }

    public boolean isCurrentOverloaded() {
        return this.currentOverloaded;
    }

    public boolean isOverHeated() {
        return this.overHeated;
    }

    public boolean isLowTemperature() {
        return this.lowTemperature;
    }

    public boolean isShortCircuited() {
        return this.shortCircuited;
    }

    public boolean isCustomDischargeEnabled() {
        return this.customDischargeEnabled;
    }

    public int getLowVoltageCellIndex() {
        return this.lowVoltageCellIndex;
    }

    public int getDamagedCellIndex() {
        return this.damagedCellIndex;
    }

    public boolean hasError() {
        return this.currentOverloaded || this.overHeated || this.lowTemperature || this.shortCircuited || this.lowVoltageCellIndex >= 0 || this.damagedCellIndex >= 0 || this.customDischargeEnabled;
    }
}

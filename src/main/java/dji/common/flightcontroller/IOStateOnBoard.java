package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.data.model.P3.DataOnBoardSdkSetIOState;

@EXClassNullAway
public class IOStateOnBoard {
    /* access modifiers changed from: private */
    public int dutyRatioOfPWM;
    /* access modifiers changed from: private */
    public int frequencyOfPWM;
    /* access modifiers changed from: private */
    public GPIOWorkModeOnBoard gpioWorkModeOnBoard;
    /* access modifiers changed from: private */
    public boolean initiated;
    /* access modifiers changed from: private */
    public IOMode ioMode;
    /* access modifiers changed from: private */
    public boolean isInHighElectricLevel;
    /* access modifiers changed from: private */
    public boolean isOutputMode;

    private IOStateOnBoard() {
        this.initiated = false;
        this.ioMode = IOMode.OTHER;
        this.gpioWorkModeOnBoard = GPIOWorkModeOnBoard.OTHER;
        this.dutyRatioOfPWM = -1;
        this.frequencyOfPWM = 0;
        this.isInHighElectricLevel = false;
        this.isOutputMode = false;
    }

    public boolean isInitiated() {
        return this.initiated;
    }

    public IOMode getIoMode() {
        return this.ioMode;
    }

    public GPIOWorkModeOnBoard getGpioWorkModeOnBoard() {
        return this.gpioWorkModeOnBoard;
    }

    public int getDutyRatioOfPWM() {
        return this.dutyRatioOfPWM;
    }

    public int getFrequencyOfPWM() {
        return this.frequencyOfPWM;
    }

    public boolean isInHighElectricLevel() {
        return this.isInHighElectricLevel;
    }

    public boolean isOutputMode() {
        return this.isOutputMode;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof IOStateOnBoard)) {
            return false;
        }
        IOStateOnBoard that = (IOStateOnBoard) o;
        if (this.initiated != that.initiated || this.dutyRatioOfPWM != that.dutyRatioOfPWM || this.frequencyOfPWM != that.frequencyOfPWM || this.isInHighElectricLevel != that.isInHighElectricLevel || this.isOutputMode != that.isOutputMode || this.ioMode != that.ioMode) {
            return false;
        }
        if (this.gpioWorkModeOnBoard != that.gpioWorkModeOnBoard) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4 = 1;
        if (this.initiated) {
            result = 1;
        } else {
            result = 0;
        }
        int i5 = result * 31;
        if (this.ioMode != null) {
            i = this.ioMode.hashCode();
        } else {
            i = 0;
        }
        int i6 = (i5 + i) * 31;
        if (this.gpioWorkModeOnBoard != null) {
            i2 = this.gpioWorkModeOnBoard.hashCode();
        } else {
            i2 = 0;
        }
        int i7 = (((((i6 + i2) * 31) + this.dutyRatioOfPWM) * 31) + this.frequencyOfPWM) * 31;
        if (this.isInHighElectricLevel) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i8 = (i7 + i3) * 31;
        if (!this.isOutputMode) {
            i4 = 0;
        }
        return i8 + i4;
    }

    public static class Builder {
        public static IOStateOnBoard createReturnValue(boolean isInHighElectricLevel, DataOnBoardSdkSetIOState.GPIOMode mode, boolean initiated) {
            IOStateOnBoard ioStateOnBoard = new IOStateOnBoard();
            IOMode unused = ioStateOnBoard.ioMode = IOMode.GPIO;
            GPIOWorkModeOnBoard unused2 = ioStateOnBoard.gpioWorkModeOnBoard = GPIOWorkModeOnBoard.find(mode.value());
            boolean unused3 = ioStateOnBoard.initiated = initiated;
            boolean unused4 = ioStateOnBoard.isInHighElectricLevel = isInHighElectricLevel;
            return ioStateOnBoard;
        }

        public static IOStateOnBoard createSetParams(boolean isInHighElectricLevel) {
            IOStateOnBoard ioStateOnBoard = new IOStateOnBoard();
            IOMode unused = ioStateOnBoard.ioMode = IOMode.GPIO;
            boolean unused2 = ioStateOnBoard.isInHighElectricLevel = isInHighElectricLevel;
            return ioStateOnBoard;
        }

        public static IOStateOnBoard createInitialParams(GPIOWorkModeOnBoard gpioWorkModeOnBoard) {
            IOStateOnBoard ioStateOnBoard = new IOStateOnBoard();
            IOMode unused = ioStateOnBoard.ioMode = IOMode.GPIO;
            GPIOWorkModeOnBoard unused2 = ioStateOnBoard.gpioWorkModeOnBoard = gpioWorkModeOnBoard;
            return ioStateOnBoard;
        }

        public static IOStateOnBoard createReturnValue(int dutyRatio, int frequency, boolean initiated) {
            IOStateOnBoard ioStateOnBoard = new IOStateOnBoard();
            IOMode unused = ioStateOnBoard.ioMode = IOMode.PWM;
            int unused2 = ioStateOnBoard.frequencyOfPWM = frequency;
            boolean unused3 = ioStateOnBoard.initiated = initiated;
            int unused4 = ioStateOnBoard.dutyRatioOfPWM = dutyRatio;
            return ioStateOnBoard;
        }

        public static IOStateOnBoard createFChannelReturnValue(int dutyRatio, boolean isInHighElectricLevel) {
            IOStateOnBoard ioStateOnBoard = new IOStateOnBoard();
            boolean unused = ioStateOnBoard.isInHighElectricLevel = isInHighElectricLevel;
            int unused2 = ioStateOnBoard.dutyRatioOfPWM = dutyRatio;
            return ioStateOnBoard;
        }

        public static IOStateOnBoard createSetParams(int dutyRatio) {
            IOStateOnBoard ioStateOnBoard = new IOStateOnBoard();
            IOMode unused = ioStateOnBoard.ioMode = IOMode.PWM;
            int unused2 = ioStateOnBoard.dutyRatioOfPWM = dutyRatio;
            return ioStateOnBoard;
        }

        public static IOStateOnBoard createInitialParams(int dutyRatio, int frequency) {
            IOStateOnBoard ioStateOnBoard = new IOStateOnBoard();
            IOMode unused = ioStateOnBoard.ioMode = IOMode.PWM;
            int unused2 = ioStateOnBoard.dutyRatioOfPWM = dutyRatio;
            int unused3 = ioStateOnBoard.frequencyOfPWM = frequency;
            return ioStateOnBoard;
        }

        public static IOStateOnBoard createFChannelInitialParams(int dutyRatio, int frequency, boolean isOutputMode) {
            IOStateOnBoard ioStateOnBoard = new IOStateOnBoard();
            IOMode unused = ioStateOnBoard.ioMode = IOMode.PWM;
            int unused2 = ioStateOnBoard.dutyRatioOfPWM = dutyRatio;
            int unused3 = ioStateOnBoard.frequencyOfPWM = frequency;
            boolean unused4 = ioStateOnBoard.isOutputMode = isOutputMode;
            return ioStateOnBoard;
        }

        public static IOStateOnBoard createFChannelInitialParams(boolean isInHighElectricLevel, boolean isOutputMode) {
            IOStateOnBoard ioStateOnBoard = new IOStateOnBoard();
            IOMode unused = ioStateOnBoard.ioMode = IOMode.GPIO;
            boolean unused2 = ioStateOnBoard.isInHighElectricLevel = isInHighElectricLevel;
            boolean unused3 = ioStateOnBoard.isOutputMode = isOutputMode;
            return ioStateOnBoard;
        }
    }
}

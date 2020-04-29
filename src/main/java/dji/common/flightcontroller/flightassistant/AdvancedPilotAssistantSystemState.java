package dji.common.flightcontroller.flightassistant;

public class AdvancedPilotAssistantSystemState {
    private final boolean isAPASOn;
    private final boolean isAPASWorking;
    private final boolean isBinocularDeepImageInvalid;
    private final boolean isFlightControllerSubModuleUnsatisfied;
    private final boolean isNotOnAir;
    private final boolean isOnLimitAreaBoundaries;
    private final boolean isPositionSpeedInvalid;
    private final boolean otherNavigationModulesWork;

    public AdvancedPilotAssistantSystemState(boolean isAPASOn2, boolean isAPASWorking2, boolean isNotOnAir2, boolean isFlightControllerSubModuleUnsatisfied2, boolean otherNavigationModulesWork2, boolean isOnLimitAreaBoundaries2, boolean isPositionSpeedInvalid2, boolean isBinocularDeepImageInvalid2) {
        this.isAPASOn = isAPASOn2;
        this.isAPASWorking = isAPASWorking2;
        this.isNotOnAir = isNotOnAir2;
        this.isFlightControllerSubModuleUnsatisfied = isFlightControllerSubModuleUnsatisfied2;
        this.otherNavigationModulesWork = otherNavigationModulesWork2;
        this.isOnLimitAreaBoundaries = isOnLimitAreaBoundaries2;
        this.isPositionSpeedInvalid = isPositionSpeedInvalid2;
        this.isBinocularDeepImageInvalid = isBinocularDeepImageInvalid2;
    }

    public boolean isAPASOn() {
        return this.isAPASOn;
    }

    public boolean isAPASWorking() {
        return this.isAPASWorking;
    }

    public boolean isNotOnAir() {
        return this.isNotOnAir;
    }

    public boolean isFlightControllerSubModuleUnsatisfied() {
        return this.isFlightControllerSubModuleUnsatisfied;
    }

    public boolean isOtherNavigationModulesWork() {
        return this.otherNavigationModulesWork;
    }

    public boolean isOnLimitAreaBoundaries() {
        return this.isOnLimitAreaBoundaries;
    }

    public boolean isPositionSpeedInvalid() {
        return this.isPositionSpeedInvalid;
    }

    public boolean isBinocularDeepImageInvalid() {
        return this.isBinocularDeepImageInvalid;
    }

    public boolean doesAPASHasTempError() {
        return this.isNotOnAir || this.isFlightControllerSubModuleUnsatisfied || this.otherNavigationModulesWork || this.isPositionSpeedInvalid || this.isBinocularDeepImageInvalid;
    }

    public boolean equals(Object o) {
        boolean z = true;
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdvancedPilotAssistantSystemState)) {
            return false;
        }
        AdvancedPilotAssistantSystemState that = (AdvancedPilotAssistantSystemState) o;
        if (isAPASOn() != that.isAPASOn() || isAPASWorking() != that.isAPASWorking() || isNotOnAir() != that.isNotOnAir() || isFlightControllerSubModuleUnsatisfied() != that.isFlightControllerSubModuleUnsatisfied() || isOtherNavigationModulesWork() != that.isOtherNavigationModulesWork() || isOnLimitAreaBoundaries() != that.isOnLimitAreaBoundaries() || isPositionSpeedInvalid() != that.isPositionSpeedInvalid()) {
            return false;
        }
        if (isBinocularDeepImageInvalid() != that.isBinocularDeepImageInvalid()) {
            z = false;
        }
        return z;
    }

    public int hashCode() {
        int result;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7 = 1;
        if (isAPASOn()) {
            result = 1;
        } else {
            result = 0;
        }
        int i8 = result * 31;
        if (isAPASWorking()) {
            i = 1;
        } else {
            i = 0;
        }
        int i9 = (i8 + i) * 31;
        if (isNotOnAir()) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        int i10 = (i9 + i2) * 31;
        if (isFlightControllerSubModuleUnsatisfied()) {
            i3 = 1;
        } else {
            i3 = 0;
        }
        int i11 = (i10 + i3) * 31;
        if (isOtherNavigationModulesWork()) {
            i4 = 1;
        } else {
            i4 = 0;
        }
        int i12 = (i11 + i4) * 31;
        if (isOnLimitAreaBoundaries()) {
            i5 = 1;
        } else {
            i5 = 0;
        }
        int i13 = (i12 + i5) * 31;
        if (isPositionSpeedInvalid()) {
            i6 = 1;
        } else {
            i6 = 0;
        }
        int i14 = (i13 + i6) * 31;
        if (!isBinocularDeepImageInvalid()) {
            i7 = 0;
        }
        return i14 + i7;
    }

    public String toString() {
        return "AdvancedPilotAssistantSystemState{isAPASOn=" + this.isAPASOn + ", isAPASWorking=" + this.isAPASWorking + ", isNotOnAir=" + this.isNotOnAir + ", isFlightControllerSubModuleUnsatisfied=" + this.isFlightControllerSubModuleUnsatisfied + ", otherNavigationModulesWork=" + this.otherNavigationModulesWork + ", isOnLimitAreaBoundaries=" + this.isOnLimitAreaBoundaries + ", isPositionSpeedInvalid=" + this.isPositionSpeedInvalid + ", isBinocularDeepImageInvalid=" + this.isBinocularDeepImageInvalid + '}';
    }
}

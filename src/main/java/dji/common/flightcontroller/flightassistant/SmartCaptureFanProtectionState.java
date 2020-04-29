package dji.common.flightcontroller.flightassistant;

public enum SmartCaptureFanProtectionState {
    NONE,
    DETECTING,
    OFF,
    ON;

    public static SmartCaptureFanProtectionState get(int value) {
        switch (value) {
            case 1:
                return DETECTING;
            case 2:
                return OFF;
            case 3:
                return ON;
            default:
                return NONE;
        }
    }
}

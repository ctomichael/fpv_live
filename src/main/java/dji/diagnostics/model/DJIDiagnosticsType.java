package dji.diagnostics.model;

public enum DJIDiagnosticsType {
    AIR1860(0),
    BATTERY(1),
    CAMERA(2),
    CENTER_BOARD(3),
    OSD(4),
    FLIGHT_CONTROLLER(5),
    GIMBAL(6),
    LIGHT_BRIDGE(7),
    REMOTE_CONTROLLER(8),
    VISION(9),
    RTK(10),
    PRODUCT(11),
    NAVIGATION(12),
    OTHER(100);
    
    private int value;

    private DJIDiagnosticsType(int value2) {
        this.value = value2;
    }

    public int getValue() {
        return this.value;
    }

    public static DJIDiagnosticsType find(int value2) {
        DJIDiagnosticsType[] values = values();
        for (DJIDiagnosticsType item : values) {
            if (item.getValue() == value2) {
                return item;
            }
        }
        return OTHER;
    }
}

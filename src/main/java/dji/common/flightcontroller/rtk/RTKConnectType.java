package dji.common.flightcontroller.rtk;

public enum RTKConnectType {
    None(0),
    SDR(1),
    LTE(2);
    
    int value;

    private RTKConnectType(int value2) {
        this.value = value2;
    }

    public static RTKConnectType find(int value2) {
        RTKConnectType[] values = values();
        for (RTKConnectType item : values) {
            if (item.value == value2) {
                return item;
            }
        }
        return None;
    }
}

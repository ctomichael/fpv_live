package dji.common.battery;

public enum BatterySOPTemperatureLevel {
    LEVEL_0(0),
    LEVEL_1(1),
    LEVEL_2(2),
    UNKNOWN(255);
    
    private int data;

    private BatterySOPTemperatureLevel(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }
}

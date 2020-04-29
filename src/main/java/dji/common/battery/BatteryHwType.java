package dji.common.battery;

public enum BatteryHwType {
    WM160_BA04_JP(99),
    GET_FAIL(254),
    OTHER(255);
    
    private int data;

    public boolean _equals(int b) {
        return this.data == b;
    }

    private BatteryHwType(int _data) {
        this.data = _data;
    }

    public static BatteryHwType find(int value) {
        BatteryHwType result = OTHER;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value)) {
                return values()[i];
            }
        }
        return result;
    }
}

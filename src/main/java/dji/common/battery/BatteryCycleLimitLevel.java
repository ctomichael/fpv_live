package dji.common.battery;

public enum BatteryCycleLimitLevel {
    GOOD(0),
    YELLOW_WARNING(1),
    RED_ALERT(2),
    UNKNOWN(255);
    
    private int data;

    private BatteryCycleLimitLevel(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }
}

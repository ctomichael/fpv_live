package dji.common.flightcontroller.virtualfence;

public enum AreaShape {
    CIRCLE(12, 0),
    POLYGON(8, 1),
    UNKNOWN(0, 255);
    
    private final int length;
    private final int value;

    private AreaShape(int length2, int value2) {
        this.length = length2;
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static AreaShape find(int value2) {
        AreaShape result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}

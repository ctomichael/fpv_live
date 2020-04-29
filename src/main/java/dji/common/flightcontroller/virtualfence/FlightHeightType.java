package dji.common.flightcontroller.virtualfence;

public enum FlightHeightType {
    NOT_SET(0),
    RELATIVE(1),
    ABSOLUTE(2);
    
    private final int data;

    public int value() {
        return this.data;
    }

    private FlightHeightType(int _data) {
        this.data = _data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static FlightHeightType find(int value) {
        FlightHeightType result = NOT_SET;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value)) {
                return values()[i];
            }
        }
        return result;
    }
}

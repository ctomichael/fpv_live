package dji.common.flightcontroller.virtualstick;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum FlightCoordinateSystem {
    GROUND(0),
    BODY(1);
    
    private int data;

    private FlightCoordinateSystem(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static FlightCoordinateSystem find(int b) {
        FlightCoordinateSystem result = GROUND;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}

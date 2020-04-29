package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum FlightOrientationMode {
    AIRCRAFT_HEADING(255),
    COURSE_LOCK(1),
    HOME_LOCK(2);
    
    private int data;

    private FlightOrientationMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static FlightOrientationMode find(int b) {
        FlightOrientationMode result = AIRCRAFT_HEADING;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(b)) {
                return values()[i];
            }
        }
        return result;
    }
}

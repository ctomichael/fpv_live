package dji.common.realname;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum AircraftBindingState {
    NOT_SUPPORTED(3),
    INITIAL(2),
    BOUND(1),
    NOT_REQUIRED(0),
    UNBOUND(-2),
    UNBOUND_BUT_CANNOT_SYNC(-3),
    UNKNOWN(255);
    
    int data = 0;

    public interface AircraftBindingStateListener {
        void onUpdate(AircraftBindingState aircraftBindingState);
    }

    private AircraftBindingState(int i) {
        this.data = i;
    }

    public static AircraftBindingState find(int b) {
        AircraftBindingState result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i].data == b) {
                return values()[i];
            }
        }
        return result;
    }
}

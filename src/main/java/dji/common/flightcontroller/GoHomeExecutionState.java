package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum GoHomeExecutionState {
    NOT_EXECUTING(0),
    TURN_DIRECTION_TO_HOME_POINT(2),
    GO_UP_TO_HEIGHT(1),
    AUTO_FLY_TO_HOME_POINT(4),
    GO_DOWN_TO_GROUND(7),
    COMPLETED(8),
    BRAKING(5),
    BYPASSING(6),
    UNKNOWN(255);
    
    private static volatile GoHomeExecutionState[] goHomeExecutionStates;
    private int data;

    private GoHomeExecutionState(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    public boolean _equals(int b) {
        return this.data == b;
    }

    public static GoHomeExecutionState find(int b) {
        GoHomeExecutionState result = UNKNOWN;
        if (goHomeExecutionStates == null) {
            goHomeExecutionStates = values();
        }
        for (int i = 0; i < goHomeExecutionStates.length; i++) {
            if (goHomeExecutionStates[i]._equals(b)) {
                return goHomeExecutionStates[i];
            }
        }
        return result;
    }
}

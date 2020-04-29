package dji.common.mission.followme;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum FollowMeMissionExecuteState {
    INITIALIZING(0),
    MOVING(1),
    WAITING(2);
    
    private int value;

    private FollowMeMissionExecuteState(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static FollowMeMissionExecuteState find(int value2) {
        FollowMeMissionExecuteState result = WAITING;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}

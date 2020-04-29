package dji.common.mission.followme;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum FollowMeHeading {
    TOWARD_FOLLOW_POSITION(0),
    CONTROLLED_BY_REMOTE_CONTROLLER(1);
    
    private int value;

    private FollowMeHeading(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static FollowMeHeading find(int value2) {
        FollowMeHeading result = TOWARD_FOLLOW_POSITION;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}

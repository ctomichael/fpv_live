package dji.common.mission.hotpoint;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum HotpointHeading {
    ALONG_CIRCLE_LOOKING_FORWARDS(0),
    ALONG_CIRCLE_LOOKING_BACKWARDS(5),
    TOWARDS_HOT_POINT(1),
    AWAY_FROM_HOT_POINT(2),
    CONTROLLED_BY_REMOTE_CONTROLLER(3),
    USING_INITIAL_HEADING(4);
    
    private int value;

    private HotpointHeading(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static HotpointHeading find(int value2) {
        HotpointHeading result = ALONG_CIRCLE_LOOKING_FORWARDS;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}

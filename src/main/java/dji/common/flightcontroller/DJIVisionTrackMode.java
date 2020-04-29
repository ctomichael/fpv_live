package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum DJIVisionTrackMode {
    HEADLESS_FOLLOW(0),
    PARALLEL_FOLLOW(1),
    FIXED_ANGLE(2),
    WATCH_TARGET(3),
    HEAD_LOCK(4),
    WAYPOINT(5),
    QUICK_MOVIE(6),
    SPOTLIGHT(10),
    OTHER(255);
    
    private final int data;

    private DJIVisionTrackMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static DJIVisionTrackMode find(int b) {
        DJIVisionTrackMode result = HEADLESS_FOLLOW;
        DJIVisionTrackMode[] values = values();
        for (DJIVisionTrackMode tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}

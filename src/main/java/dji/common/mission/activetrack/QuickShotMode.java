package dji.common.mission.activetrack;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum QuickShotMode {
    CIRCLE(1),
    DRONIE(2),
    HELIX(3),
    ROCKET(4),
    BOOMERANG(6),
    ASTEROID(8),
    DOLLY_ZOOM(10),
    UNKNOWN(255);
    
    private final int data;

    private QuickShotMode(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static QuickShotMode find(int b) {
        QuickShotMode result = UNKNOWN;
        QuickShotMode[] values = values();
        for (QuickShotMode tmp : values) {
            if (tmp._equals(b)) {
                return tmp;
            }
        }
        return result;
    }
}

package dji.common.flightcontroller;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum VisionDrawStatus {
    INIT(0),
    PREPARE(1),
    READY_TO_GO(2),
    START_AUTO(3),
    START_MANUAL(4),
    PAUSE(5),
    OTHER(100);
    
    private final int data;

    private VisionDrawStatus(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }

    private boolean _equals(int b) {
        return this.data == b;
    }

    public static VisionDrawStatus find(int value) {
        VisionDrawStatus result = INIT;
        VisionDrawStatus[] values = values();
        for (VisionDrawStatus tmp : values) {
            if (tmp._equals(value)) {
                return tmp;
            }
        }
        return result;
    }
}

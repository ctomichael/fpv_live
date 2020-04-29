package dji.internal.narrowband;

public enum NarrowBandSlaveMode {
    MASTER(0),
    GIMBAL_CONTROLLER(1),
    ASSISTANT_1(2),
    ASSISTANT_2(3),
    UNKNOWN(255);
    
    private int data;

    private NarrowBandSlaveMode(int _data) {
        this.data = _data;
    }

    public int getValue() {
        return this.data;
    }

    public static NarrowBandSlaveMode find(int b) {
        NarrowBandSlaveMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i].getValue() == b) {
                return values()[i];
            }
        }
        return result;
    }
}

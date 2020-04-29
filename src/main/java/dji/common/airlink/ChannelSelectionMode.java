package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum ChannelSelectionMode {
    AUTO(0),
    MANUAL(1),
    UNKNOWN(255);
    
    private int value;

    private ChannelSelectionMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    private boolean _equals(int b) {
        return this.value == b;
    }

    public static ChannelSelectionMode find(int value2) {
        ChannelSelectionMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}

package dji.common.handheld;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum PowerMode {
    ON(0),
    SLEEPING(1),
    OFF(2),
    UNKNOWN(255);
    
    private int value;

    public interface Callback {
        void onUpdate(PowerMode powerMode);
    }

    private PowerMode(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static PowerMode find(int value2) {
        PowerMode result = UNKNOWN;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}

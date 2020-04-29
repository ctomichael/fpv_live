package dji.common.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum SDRInterferedTerminal {
    Ground(1),
    UAV(2),
    Non(255);
    
    private int value;

    private SDRInterferedTerminal(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean _equals(int b) {
        return this.value == b;
    }

    public static SDRInterferedTerminal find(int value2) {
        SDRInterferedTerminal result = Non;
        for (int i = 0; i < values().length; i++) {
            if (values()[i]._equals(value2)) {
                return values()[i];
            }
        }
        return result;
    }
}

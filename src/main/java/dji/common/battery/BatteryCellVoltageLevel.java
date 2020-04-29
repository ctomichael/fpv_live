package dji.common.battery;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum BatteryCellVoltageLevel {
    LEVEL_0(0),
    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    UNKNOWN(255);
    
    private int data;

    private BatteryCellVoltageLevel(int _data) {
        this.data = _data;
    }

    public int value() {
        return this.data;
    }
}

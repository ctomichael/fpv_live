package dji.internal.airlink;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum WorkingFrequency {
    FREQUENCY_2_4G(0),
    FREQUENCY_5_1G(1),
    FREQUENCY_5_8G(2);
    
    private int value = 0;

    public int value() {
        return this.value;
    }

    private WorkingFrequency(int value2) {
        this.value = value2;
    }

    public static WorkingFrequency find(int value2) {
        WorkingFrequency[] enums = values();
        for (WorkingFrequency item : enums) {
            if (item.value == value2) {
                return item;
            }
        }
        return null;
    }
}

package com.dji.cmd.v1.protocol;

public enum MlTkModeE {
    ML_TK_MODE_UNFUSED(-1),
    ML_TK_MODE_LOST(0),
    ML_TK_MODE_TRACKED(1),
    ML_TK_MODE_NOT_CONFIDENT(2),
    ML_TK_MODE_REDETECTED(3);
    
    private int value;

    private MlTkModeE(int value2) {
        this.value = value2;
    }

    public int value() {
        return this.value;
    }

    public boolean equals(int b) {
        return this.value == b;
    }

    public static MlTkModeE find(int value2) {
        for (int i = 0; i < values().length; i++) {
            if (values()[i].equals(value2)) {
                return values()[i];
            }
        }
        return null;
    }
}

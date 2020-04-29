package dji.common.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class FocusAssistantSettings {
    private boolean enabledAF;
    private boolean enabledMF;

    public FocusAssistantSettings(boolean enabledAF2, boolean enabledMF2) {
        this.enabledAF = enabledAF2;
        this.enabledMF = enabledMF2;
    }

    public boolean isEnabledAF() {
        return this.enabledAF;
    }

    public boolean isEnabledMF() {
        return this.enabledMF;
    }

    public int hashCode() {
        int result;
        int i = 1;
        if (this.enabledMF) {
            result = 1;
        } else {
            result = 0;
        }
        int i2 = result * 31;
        if (!this.enabledAF) {
            i = 0;
        }
        return i2 + i;
    }

    public boolean equals(Object o) {
        if (o == null || !(o instanceof FocusAssistantSettings)) {
            return false;
        }
        if (this == o) {
            return true;
        }
        return this.enabledMF == ((FocusAssistantSettings) o).enabledMF && this.enabledAF == ((FocusAssistantSettings) o).enabledAF;
    }
}

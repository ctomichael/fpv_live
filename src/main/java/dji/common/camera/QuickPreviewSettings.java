package dji.common.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class QuickPreviewSettings {
    private boolean mEnable = false;
    private int mTime = 0;

    public QuickPreviewSettings(boolean mEnable2, int mTime2) {
        this.mEnable = mEnable2;
        this.mTime = mTime2;
    }

    public boolean isEnable() {
        return this.mEnable;
    }

    public int getTime() {
        return this.mTime;
    }
}

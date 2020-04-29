package dji.common.camera;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class TauVideoFormat {
    private int fps = 0;
    private int ratio = 0;

    public TauVideoFormat(int ratio2, int fps2) {
        this.ratio = ratio2;
        this.fps = fps2;
    }

    public int getRatio() {
        return this.ratio;
    }

    public int getVideoFps() {
        return this.fps;
    }
}

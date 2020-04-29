package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum RenderType {
    POINTS(0),
    LINES(1),
    LINE_LOOP(2),
    LINE_STRIP(3),
    TRIANGLES(4),
    TRIANGLE_STRIP(5),
    TRIANGLE_FAN(6);
    
    private final int _glValue;

    private RenderType(int $glValue) {
        this._glValue = $glValue;
    }

    public int glValue() {
        return this._glValue;
    }
}

package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum FogType {
    LINEAR(9729),
    EXP(2048),
    EXP2(2049);
    
    private final int _glValue;

    private FogType(int $glValue) {
        this._glValue = $glValue;
    }

    public int glValue() {
        return this._glValue;
    }
}

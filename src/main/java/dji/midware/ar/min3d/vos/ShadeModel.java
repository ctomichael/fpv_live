package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum ShadeModel {
    SMOOTH(7425),
    FLAT(7424);
    
    private final int _glConstant;

    private ShadeModel(int $glConstant) {
        this._glConstant = $glConstant;
    }

    public int glConstant() {
        return this._glConstant;
    }
}

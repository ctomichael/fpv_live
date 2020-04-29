package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public enum LightType {
    DIRECTIONAL(0.0f),
    POSITIONAL(1.0f);
    
    private final float _glValue;

    private LightType(float $glValue) {
        this._glValue = $glValue;
    }

    public float glValue() {
        return this._glValue;
    }
}

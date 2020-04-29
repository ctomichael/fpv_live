package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.interfaces.IDirtyParent;

@EXClassNullAway
public class FloatManaged extends AbstractDirtyManaged {
    private float _f;

    public FloatManaged(float $value, IDirtyParent $parent) {
        super($parent);
        set($value);
    }

    public float get() {
        return this._f;
    }

    public void set(float $f) {
        this._f = $f;
        setDirtyFlag();
    }
}

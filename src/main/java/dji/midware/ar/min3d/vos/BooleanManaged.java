package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.interfaces.IDirtyParent;

@EXClassNullAway
public class BooleanManaged extends AbstractDirtyManaged {
    private boolean _b;

    public BooleanManaged(boolean $value, IDirtyParent $parent) {
        super($parent);
        set($value);
    }

    public boolean get() {
        return this._b;
    }

    public void set(boolean $b) {
        this._b = $b;
        setDirtyFlag();
    }
}

package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.interfaces.IDirtyManaged;
import dji.midware.ar.min3d.interfaces.IDirtyParent;

@EXClassNullAway
public abstract class AbstractDirtyManaged implements IDirtyManaged {
    protected boolean _dirty;
    protected IDirtyParent _parent;

    public AbstractDirtyManaged(IDirtyParent $parent) {
        this._parent = $parent;
    }

    public boolean isDirty() {
        return this._dirty;
    }

    public void setDirtyFlag() {
        this._dirty = true;
        if (this._parent != null) {
            this._parent.onDirty();
        }
    }

    public void clearDirtyFlag() {
        this._dirty = false;
    }
}

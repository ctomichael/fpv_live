package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.interfaces.IDirtyParent;

@EXClassNullAway
public class ShadeModelManaged extends AbstractDirtyManaged {
    private ShadeModel _shadeModel;

    public ShadeModelManaged(IDirtyParent $parent) {
        super($parent);
        set(ShadeModel.SMOOTH);
    }

    public ShadeModel get() {
        return this._shadeModel;
    }

    public void set(ShadeModel $shadeModel) {
        this._shadeModel = $shadeModel;
        this._dirty = true;
    }
}

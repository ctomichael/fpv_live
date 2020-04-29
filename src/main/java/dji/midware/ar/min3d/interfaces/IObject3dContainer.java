package dji.midware.ar.min3d.interfaces;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.core.Object3d;

@EXClassNullAway
public interface IObject3dContainer {
    void addChild(Object3d object3d);

    void addChildAt(Object3d object3d, int i);

    Object3d getChildAt(int i);

    Object3d getChildByName(String str);

    int getChildIndexOf(Object3d object3d);

    int numChildren();

    boolean removeChild(Object3d object3d);

    Object3d removeChildAt(int i);
}

package dji.midware.ar.min3d.core;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.interfaces.IObject3dContainer;
import java.util.ArrayList;

@EXClassNullAway
public class Object3dContainer extends Object3d implements IObject3dContainer {
    protected ArrayList<Object3d> _children = new ArrayList<>();

    public Object3dContainer() {
        super(0, 0, false, false, false);
    }

    public Object3dContainer(int $maxVerts, int $maxFaces) {
        super($maxVerts, $maxFaces, true, true, true);
    }

    public Object3dContainer(int $maxVerts, int $maxFaces, Boolean $useUvs, Boolean $useNormals, Boolean $useVertexColors) {
        super($maxVerts, $maxFaces, $useUvs, $useNormals, $useVertexColors);
    }

    public Object3dContainer(Vertices $vertices, FacesBufferedList $faces, TextureList $textures) {
        super($vertices, $faces, $textures);
    }

    public void addChild(Object3d $o) {
        this._children.add($o);
        $o.parent(this);
        $o.scene(scene());
    }

    public void addChildAt(Object3d $o, int $index) {
        this._children.add($index, $o);
        $o.parent(this);
        $o.scene(scene());
    }

    public boolean removeChild(Object3d $o) {
        boolean b = this._children.remove($o);
        if (b) {
            $o.parent(null);
            $o.scene(null);
        }
        return b;
    }

    public Object3d removeChildAt(int $index) {
        Object3d o = this._children.remove($index);
        if (o != null) {
            o.parent(null);
            o.scene(null);
        }
        return o;
    }

    public Object3d getChildAt(int $index) {
        return this._children.get($index);
    }

    public Object3d getChildByName(String $name) {
        for (int i = 0; i < this._children.size(); i++) {
            if (this._children.get(i).name().equals($name)) {
                return this._children.get(i);
            }
        }
        return null;
    }

    public int getChildIndexOf(Object3d $o) {
        return this._children.indexOf($o);
    }

    public int numChildren() {
        return this._children.size();
    }

    /* access modifiers changed from: package-private */
    public ArrayList<Object3d> children() {
        return this._children;
    }

    public Object3dContainer clone() {
        Object3dContainer clone = new Object3dContainer(this._vertices.clone(), this._faces.clone(), this._textures);
        clone.position().x = position().x;
        clone.position().y = position().y;
        clone.position().z = position().z;
        clone.rotation().x = rotation().x;
        clone.rotation().y = rotation().y;
        clone.rotation().z = rotation().z;
        clone.scale().x = scale().x;
        clone.scale().y = scale().y;
        clone.scale().z = scale().z;
        for (int i = 0; i < numChildren(); i++) {
            clone.addChild(getChildAt(i));
        }
        return clone;
    }
}

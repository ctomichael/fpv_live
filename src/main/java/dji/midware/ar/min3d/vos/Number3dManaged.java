package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Utils;
import dji.midware.ar.min3d.interfaces.IDirtyParent;
import java.nio.FloatBuffer;

@EXClassNullAway
public class Number3dManaged extends AbstractDirtyManaged {
    private FloatBuffer _fb;
    private float _x;
    private float _y;
    private float _z;

    public Number3dManaged(IDirtyParent $parent) {
        super($parent);
        this._x = 0.0f;
        this._y = 0.0f;
        this._z = 0.0f;
        this._fb = toFloatBuffer();
        setDirtyFlag();
    }

    public Number3dManaged(float $x, float $y, float $z, IDirtyParent $parent) {
        super($parent);
        this._x = $x;
        this._y = $y;
        this._z = $z;
        this._fb = toFloatBuffer();
        setDirtyFlag();
    }

    public float getX() {
        return this._x;
    }

    public void setX(float x) {
        this._x = x;
        setDirtyFlag();
    }

    public float getY() {
        return this._y;
    }

    public void setY(float y) {
        this._y = y;
        setDirtyFlag();
    }

    public float getZ() {
        return this._z;
    }

    public void setZ(float z) {
        this._z = z;
        setDirtyFlag();
    }

    public void setAll(float $x, float $y, float $z) {
        this._x = $x;
        this._y = $y;
        this._z = $z;
        setDirtyFlag();
    }

    public void setAllFrom(Number3d $n) {
        this._x = $n.x;
        this._y = $n.y;
        this._z = $n.z;
        setDirtyFlag();
    }

    public void setAllFrom(Number3dManaged $n) {
        this._x = $n.getX();
        this._y = $n.getY();
        this._z = $n.getZ();
        setDirtyFlag();
    }

    public Number3d toNumber3d() {
        return new Number3d(this._x, this._y, this._z);
    }

    public String toString() {
        return this._x + "," + this._y + "," + this._z;
    }

    public FloatBuffer toFloatBuffer() {
        return Utils.makeFloatBuffer3(this._x, this._y, this._z);
    }

    public void toFloatBuffer(FloatBuffer $floatBuffer) {
        $floatBuffer.position(0);
        $floatBuffer.put(this._x);
        $floatBuffer.put(this._y);
        $floatBuffer.put(this._z);
        $floatBuffer.position(0);
    }

    public FloatBuffer floatBuffer() {
        return this._fb;
    }

    public void commitToFloatBuffer() {
        toFloatBuffer(this._fb);
    }
}

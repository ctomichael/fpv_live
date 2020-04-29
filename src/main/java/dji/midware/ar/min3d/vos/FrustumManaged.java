package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.interfaces.IDirtyParent;

@EXClassNullAway
public class FrustumManaged extends AbstractDirtyManaged {
    private float _horizontalCenter;
    private float _shortSideLength;
    private float _verticalCenter;
    private float _zFar;
    private float _zNear;

    public FrustumManaged(IDirtyParent $parent) {
        super($parent);
        this._horizontalCenter = 0.0f;
        this._verticalCenter = 0.0f;
        this._shortSideLength = 1.0f;
        this._zNear = 1.0f;
        this._zFar = 100.0f;
    }

    public FrustumManaged(float $horizontalCenter, float $verticalCenter, float $shortSideLength, float $zNear, float $zFar, IDirtyParent $parent) {
        super($parent);
        this._horizontalCenter = $horizontalCenter;
        this._verticalCenter = $verticalCenter;
        this._shortSideLength = $shortSideLength;
        this._zNear = $zNear;
        this._zFar = $zFar;
    }

    public float shortSideLength() {
        return this._shortSideLength;
    }

    public void shortSideLength(float shortSideLength) {
        this._shortSideLength = shortSideLength;
        setDirtyFlag();
    }

    public float horizontalCenter() {
        return this._horizontalCenter;
    }

    public void horizontalCenter(float horizontalCenter) {
        this._horizontalCenter = horizontalCenter;
        setDirtyFlag();
    }

    public float verticalCenter() {
        return this._verticalCenter;
    }

    public void verticalCenter(float verticalCenter) {
        this._verticalCenter = verticalCenter;
        setDirtyFlag();
    }

    public float zNear() {
        return this._zNear;
    }

    public void zNear(float zNear) {
        this._zNear = zNear;
        setDirtyFlag();
    }

    public float zFar() {
        return this._zFar;
    }

    public void zFar(float zFar) {
        this._zFar = zFar;
        setDirtyFlag();
    }
}

package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Number3d {
    private static Number3d _temp = new Number3d();
    public float x;
    public float y;
    public float z;

    public Number3d() {
        this.x = 0.0f;
        this.y = 0.0f;
        this.z = 0.0f;
    }

    public Number3d(float $x, float $y, float $z) {
        this.x = $x;
        this.y = $y;
        this.z = $z;
    }

    public void setAll(float $x, float $y, float $z) {
        this.x = $x;
        this.y = $y;
        this.z = $z;
    }

    public void setAllFrom(Number3d $n) {
        this.x = $n.x;
        this.y = $n.y;
        this.z = $n.z;
    }

    public void normalize() {
        float mod = (float) Math.sqrt((double) ((this.x * this.x) + (this.y * this.y) + (this.z * this.z)));
        if (mod != 0.0f && mod != 1.0f) {
            float mod2 = 1.0f / mod;
            this.x *= mod2;
            this.y *= mod2;
            this.z *= mod2;
        }
    }

    public void add(Number3d n) {
        this.x += n.x;
        this.y += n.y;
        this.z += n.z;
    }

    public void subtract(Number3d n) {
        this.x -= n.x;
        this.y -= n.y;
        this.z -= n.z;
    }

    public void multiply(Float f) {
        this.x *= f.floatValue();
        this.y *= f.floatValue();
        this.z *= f.floatValue();
    }

    public float length() {
        return (float) Math.sqrt((double) ((this.x * this.x) + (this.y * this.y) + (this.z * this.z)));
    }

    public Number3d clone() {
        return new Number3d(this.x, this.y, this.z);
    }

    public void rotateX(float angle) {
        float cosRY = (float) Math.cos((double) angle);
        float sinRY = (float) Math.sin((double) angle);
        _temp.setAll(this.x, this.y, this.z);
        this.y = (_temp.y * cosRY) - (_temp.z * sinRY);
        this.z = (_temp.y * sinRY) + (_temp.z * cosRY);
    }

    public void rotateY(float angle) {
        float cosRY = (float) Math.cos((double) angle);
        float sinRY = (float) Math.sin((double) angle);
        _temp.setAll(this.x, this.y, this.z);
        this.x = (_temp.x * cosRY) + (_temp.z * sinRY);
        this.z = (_temp.x * (-sinRY)) + (_temp.z * cosRY);
    }

    public void rotateZ(float angle) {
        float cosRY = (float) Math.cos((double) angle);
        float sinRY = (float) Math.sin((double) angle);
        _temp.setAll(this.x, this.y, this.z);
        this.x = (_temp.x * cosRY) - (_temp.y * sinRY);
        this.y = (_temp.x * sinRY) + (_temp.y * cosRY);
    }

    public String toString() {
        return this.x + "," + this.y + "," + this.z;
    }

    public static Number3d add(Number3d a, Number3d b) {
        return new Number3d(a.x + b.x, a.y + b.y, a.z + b.z);
    }

    public static Number3d subtract(Number3d a, Number3d b) {
        return new Number3d(a.x - b.x, a.y - b.y, a.z - b.z);
    }

    public static Number3d multiply(Number3d a, Number3d b) {
        return new Number3d(a.x * b.x, a.y * b.y, a.z * b.z);
    }

    public static Number3d cross(Number3d v, Number3d w) {
        return new Number3d((w.y * v.z) - (w.z * v.y), (w.z * v.x) - (w.x * v.z), (w.x * v.y) - (w.y * v.x));
    }

    public static float dot(Number3d v, Number3d w) {
        return (v.x * w.x) + (v.y * w.y) + (w.z * v.z);
    }
}

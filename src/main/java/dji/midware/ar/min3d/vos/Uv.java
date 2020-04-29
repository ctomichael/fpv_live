package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Uv {
    public float u;
    public float v;

    public Uv() {
        this.u = 0.0f;
        this.v = 0.0f;
    }

    public Uv(float $u, float $v) {
        this.u = $u;
        this.v = $v;
    }

    public Uv clone() {
        return new Uv(this.u, this.v);
    }
}

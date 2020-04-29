package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Utils;
import dji.midware.ar.min3d.interfaces.IDirtyParent;
import java.nio.FloatBuffer;

@EXClassNullAway
public class Color4Managed extends AbstractDirtyManaged {
    private short _a;
    private short _b;
    private FloatBuffer _fb;
    private short _g;
    private short _r;

    public Color4Managed(IDirtyParent $parent) {
        super($parent);
        this._r = 255;
        this._g = 255;
        this._b = 255;
        this._a = 255;
        this._fb = toFloatBuffer();
        setDirtyFlag();
    }

    public Color4Managed(short $r, short $g, short $b, short $a, IDirtyParent $parent) {
        super($parent);
        this._r = $r;
        this._g = $g;
        this._b = $b;
        this._a = $a;
        this._fb = toFloatBuffer();
        setDirtyFlag();
    }

    public Color4Managed(int $r, int $g, int $b, int $a, IDirtyParent $parent) {
        super($parent);
        this._r = (short) $r;
        this._g = (short) $g;
        this._b = (short) $b;
        this._a = (short) $a;
        this._fb = toFloatBuffer();
        setDirtyFlag();
    }

    public void setAll(short $r, short $g, short $b, short $a) {
        this._r = $r;
        this._g = $g;
        this._b = $b;
        this._a = $a;
        setDirtyFlag();
    }

    public void setAll(int $r, int $g, int $b, int $a) {
        setAll((short) $r, (short) $g, (short) $b, (short) $a);
    }

    public Color4 toColor4() {
        return new Color4(this._r, this._g, this._b, this._a);
    }

    public void setAll(long $argb32) {
        this._a = (short) ((int) (($argb32 >> 24) & 255));
        this._r = (short) ((int) (($argb32 >> 16) & 255));
        this._g = (short) ((int) (($argb32 >> 8) & 255));
        this._b = (short) ((int) ($argb32 & 255));
        setDirtyFlag();
    }

    public void setAll(Color4 $color) {
        setAll($color.r, $color.g, $color.b, $color.a);
    }

    public short r() {
        return this._r;
    }

    public void r(short $r) {
        this._r = $r;
        setDirtyFlag();
    }

    public short g() {
        return this._g;
    }

    public void g(short $g) {
        this._g = $g;
        setDirtyFlag();
    }

    public short b() {
        return this._b;
    }

    public void b(short $b) {
        this._b = $b;
        setDirtyFlag();
    }

    public short a() {
        return this._a;
    }

    public void a(short $a) {
        this._a = $a;
        setDirtyFlag();
    }

    public FloatBuffer toFloatBuffer() {
        return Utils.makeFloatBuffer4(((float) r()) / 255.0f, ((float) g()) / 255.0f, ((float) b()) / 255.0f, ((float) a()) / 255.0f);
    }

    public void toFloatBuffer(FloatBuffer $floatBuffer) {
        $floatBuffer.position(0);
        $floatBuffer.put(((float) r()) / 255.0f);
        $floatBuffer.put(((float) g()) / 255.0f);
        $floatBuffer.put(((float) b()) / 255.0f);
        $floatBuffer.put(((float) a()) / 255.0f);
        $floatBuffer.position(0);
    }

    public FloatBuffer floatBuffer() {
        return this._fb;
    }

    public void commitToFloatBuffer() {
        toFloatBuffer(this._fb);
    }

    public String toString() {
        return "r:" + ((int) this._r) + ", g:" + ((int) this._g) + ", b:" + ((int) this._b) + ", a:" + ((int) this._a);
    }
}

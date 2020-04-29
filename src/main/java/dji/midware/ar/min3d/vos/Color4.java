package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Utils;
import java.nio.FloatBuffer;

@EXClassNullAway
public class Color4 {
    public short a;
    public short b;
    public short g;
    public short r;

    public Color4() {
        this.r = 255;
        this.g = 255;
        this.b = 255;
        this.a = 255;
    }

    public Color4(short $r, short $g, short $b, short $a) {
        this.r = $r;
        this.g = $g;
        this.b = $b;
        this.a = $a;
    }

    public Color4(int $r, int $g, int $b, int $a) {
        this.r = (short) $r;
        this.g = (short) $g;
        this.b = (short) $b;
        this.a = (short) $a;
    }

    public Color4(float $r, float $g, float $b, float $a) {
        this.r = (short) ((int) $r);
        this.g = (short) ((int) $g);
        this.b = (short) ((int) $b);
        this.a = (short) ((int) $a);
    }

    public void setAll(short $r, short $g, short $b, short $a) {
        this.r = $r;
        this.g = $g;
        this.b = $b;
        this.a = $a;
    }

    public void setAll(long $argb32) {
        this.a = (short) ((int) (($argb32 >> 24) & 255));
        this.r = (short) ((int) (($argb32 >> 16) & 255));
        this.g = (short) ((int) (($argb32 >> 8) & 255));
        this.b = (short) ((int) ($argb32 & 255));
    }

    public String toString() {
        return "r:" + ((int) this.r) + ", g:" + ((int) this.g) + ", b:" + ((int) this.b) + ", a:" + ((int) this.a);
    }

    public FloatBuffer toFloatBuffer() {
        return Utils.makeFloatBuffer4((float) this.r, (float) this.g, (float) this.b, (float) this.a);
    }

    public void toFloatBuffer(FloatBuffer $floatBuffer) {
        $floatBuffer.position(0);
        $floatBuffer.put(((float) this.r) / 255.0f);
        $floatBuffer.put(((float) this.g) / 255.0f);
        $floatBuffer.put(((float) this.b) / 255.0f);
        $floatBuffer.put(((float) this.a) / 255.0f);
    }
}

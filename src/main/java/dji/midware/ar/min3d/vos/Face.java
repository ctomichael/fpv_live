package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Face {
    public short a;
    public short b;
    public short c;

    public Face(short $a, short $b, short $c) {
        this.a = $a;
        this.b = $b;
        this.c = $c;
    }

    public Face(int $a, int $b, int $c) {
        this.a = (short) $a;
        this.b = (short) $b;
        this.c = (short) $c;
    }
}

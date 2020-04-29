package dji.midware.ar.min3d.vos;

import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class Vertex3d {
    public Color4 color;
    public Number3d normal;
    public Number3d position = new Number3d();
    public Uv uv;
}

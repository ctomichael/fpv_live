package dji.midware.ar.min3d.core;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Number3d;
import dji.midware.ar.min3d.vos.Uv;

@EXClassNullAway
public class Vertices {
    private Color4BufferList _colors;
    private boolean _hasColors;
    private boolean _hasNormals;
    private boolean _hasUvs;
    private Number3dBufferList _normals;
    private Number3dBufferList _points;
    private UvBufferList _uvs;

    public Vertices(int $maxElements) {
        this._points = new Number3dBufferList($maxElements);
        this._hasUvs = true;
        this._hasNormals = true;
        this._hasColors = true;
        if (this._hasUvs) {
            this._uvs = new UvBufferList($maxElements);
        }
        if (this._hasNormals) {
            this._normals = new Number3dBufferList($maxElements);
        }
        if (this._hasColors) {
            this._colors = new Color4BufferList($maxElements);
        }
    }

    public Vertices(int $maxElements, Boolean $useUvs, Boolean $useNormals, Boolean $useColors) {
        this._points = new Number3dBufferList($maxElements);
        this._hasUvs = $useUvs.booleanValue();
        this._hasNormals = $useNormals.booleanValue();
        this._hasColors = $useColors.booleanValue();
        if (this._hasUvs) {
            this._uvs = new UvBufferList($maxElements);
        }
        if (this._hasNormals) {
            this._normals = new Number3dBufferList($maxElements);
        }
        if (this._hasColors) {
            this._colors = new Color4BufferList($maxElements);
        }
    }

    public Vertices(Number3dBufferList $points, UvBufferList $uvs, Number3dBufferList $normals, Color4BufferList $colors) {
        boolean z;
        boolean z2;
        boolean z3 = true;
        this._points = $points;
        this._uvs = $uvs;
        this._normals = $normals;
        this._colors = $colors;
        if (this._uvs == null || this._uvs.size() <= 0) {
            z = false;
        } else {
            z = true;
        }
        this._hasUvs = z;
        if (this._normals == null || this._normals.size() <= 0) {
            z2 = false;
        } else {
            z2 = true;
        }
        this._hasNormals = z2;
        this._hasColors = (this._colors == null || this._colors.size() <= 0) ? false : z3;
    }

    public int size() {
        return this._points.size();
    }

    public int capacity() {
        return this._points.capacity();
    }

    public boolean hasUvs() {
        return this._hasUvs;
    }

    public boolean hasNormals() {
        return this._hasNormals;
    }

    public boolean hasColors() {
        return this._hasColors;
    }

    public short addVertex(float $pointX, float $pointY, float $pointZ, float $textureU, float $textureV, float $normalX, float $normalY, float $normalZ, short $colorR, short $colorG, short $colorB, short $colorA) {
        this._points.add($pointX, $pointY, $pointZ);
        if (this._hasUvs) {
            this._uvs.add($textureU, $textureV);
        }
        if (this._hasNormals) {
            this._normals.add($normalX, $normalY, $normalZ);
        }
        if (this._hasColors) {
            this._colors.add($colorR, $colorG, $colorB, $colorA);
        }
        return (short) (this._points.size() - 1);
    }

    public short addVertex(Number3d $point, Uv $textureUv, Number3d $normal, Color4 $color) {
        this._points.add($point);
        if (this._hasUvs) {
            this._uvs.add($textureUv);
        }
        if (this._hasNormals) {
            this._normals.add($normal);
        }
        if (this._hasColors) {
            this._colors.add($color);
        }
        return (short) (this._points.size() - 1);
    }

    public void overwriteVerts(float[] $newVerts) {
        this._points.overwrite($newVerts);
    }

    public void overwriteNormals(float[] $newNormals) {
        this._normals.overwrite($newNormals);
    }

    /* access modifiers changed from: package-private */
    public Number3dBufferList points() {
        return this._points;
    }

    /* access modifiers changed from: package-private */
    public UvBufferList uvs() {
        return this._uvs;
    }

    /* access modifiers changed from: package-private */
    public Number3dBufferList normals() {
        return this._normals;
    }

    /* access modifiers changed from: package-private */
    public Color4BufferList colors() {
        return this._colors;
    }

    public Vertices clone() {
        return new Vertices(this._points.clone(), this._uvs.clone(), this._normals.clone(), this._colors.clone());
    }
}

package dji.midware.ar.min3d.objectPrimitives;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Utils;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.vos.Color4;

@EXClassNullAway
public class Box extends Object3dContainer {
    private Color4[] _cols;
    private float _depth;
    private float _height;
    private float _width;

    public Box(float $width, float $height, float $depth, Color4[] $sixColor4s, Boolean $useUvs, Boolean $useNormals, Boolean $useVertexColors) {
        super(24, 12, $useUvs, $useNormals, $useVertexColors);
        this._width = $width;
        this._height = $height;
        this._depth = $depth;
        if ($sixColor4s != null) {
            this._cols = $sixColor4s;
        } else {
            this._cols = new Color4[6];
            this._cols[0] = new Color4(255, 0, 0, 255);
            this._cols[1] = new Color4(0, 255, 0, 255);
            this._cols[2] = new Color4(0, 0, 255, 255);
            this._cols[3] = new Color4(255, 255, 0, 255);
            this._cols[4] = new Color4(0, 255, 255, 255);
            this._cols[5] = new Color4(255, 0, 255, 255);
        }
        make();
    }

    public Box(float $width, float $height, float $depth, Color4[] $sixColor4s) {
        this($width, $height, $depth, $sixColor4s, true, true, true);
    }

    public Box(float $width, float $height, float $depth, Color4 color) {
        this($width, $height, $depth, new Color4[]{color, color, color, color, color, color}, true, true, true);
    }

    public Box(float $width, float $height, float $depth) {
        this($width, $height, $depth, null, true, true, true);
    }

    private void make() {
        float w = this._width / 2.0f;
        float h = this._height / 2.0f;
        float d = this._depth / 2.0f;
        Utils.addQuad(this, vertices().addVertex(-w, h, d, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, this._cols[0].r, this._cols[0].g, this._cols[0].b, this._cols[0].a), vertices().addVertex(w, h, d, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f, this._cols[0].r, this._cols[0].g, this._cols[0].b, this._cols[0].a), vertices().addVertex(w, -h, d, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, this._cols[0].r, this._cols[0].g, this._cols[0].b, this._cols[0].a), vertices().addVertex(-w, -h, d, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, this._cols[0].r, this._cols[0].g, this._cols[0].b, this._cols[0].a));
        Utils.addQuad(this, vertices().addVertex(w, h, d, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, this._cols[1].r, this._cols[1].g, this._cols[1].b, this._cols[1].a), vertices().addVertex(w, h, -d, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, this._cols[1].r, this._cols[1].g, this._cols[1].b, this._cols[1].a), vertices().addVertex(w, -h, -d, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, this._cols[1].r, this._cols[1].g, this._cols[1].b, this._cols[1].a), vertices().addVertex(w, -h, d, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, this._cols[1].r, this._cols[1].g, this._cols[1].b, this._cols[1].a));
        Utils.addQuad(this, vertices().addVertex(w, h, -d, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, this._cols[2].r, this._cols[2].g, this._cols[2].b, this._cols[2].a), vertices().addVertex(-w, h, -d, 1.0f, 0.0f, 0.0f, 0.0f, -1.0f, this._cols[2].r, this._cols[2].g, this._cols[2].b, this._cols[2].a), vertices().addVertex(-w, -h, -d, 1.0f, 1.0f, 0.0f, 0.0f, -1.0f, this._cols[2].r, this._cols[2].g, this._cols[2].b, this._cols[2].a), vertices().addVertex(w, -h, -d, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, this._cols[2].r, this._cols[2].g, this._cols[2].b, this._cols[2].a));
        Utils.addQuad(this, vertices().addVertex(-w, h, -d, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, this._cols[3].r, this._cols[3].g, this._cols[3].b, this._cols[3].a), vertices().addVertex(-w, h, d, 1.0f, 0.0f, -1.0f, 0.0f, 0.0f, this._cols[3].r, this._cols[3].g, this._cols[3].b, this._cols[3].a), vertices().addVertex(-w, -h, d, 1.0f, 1.0f, -1.0f, 0.0f, 0.0f, this._cols[3].r, this._cols[3].g, this._cols[3].b, this._cols[3].a), vertices().addVertex(-w, -h, -d, 0.0f, 1.0f, -1.0f, 0.0f, 0.0f, this._cols[3].r, this._cols[3].g, this._cols[3].b, this._cols[3].a));
        Utils.addQuad(this, vertices().addVertex(-w, h, -d, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, this._cols[4].r, this._cols[4].g, this._cols[4].b, this._cols[4].a), vertices().addVertex(w, h, -d, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, this._cols[4].r, this._cols[4].g, this._cols[4].b, this._cols[4].a), vertices().addVertex(w, h, d, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, this._cols[4].r, this._cols[4].g, this._cols[4].b, this._cols[4].a), vertices().addVertex(-w, h, d, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, this._cols[4].r, this._cols[4].g, this._cols[4].b, this._cols[4].a));
        Utils.addQuad(this, vertices().addVertex(-w, -h, d, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, this._cols[5].r, this._cols[5].g, this._cols[5].b, this._cols[5].a), vertices().addVertex(w, -h, d, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, this._cols[5].r, this._cols[5].g, this._cols[5].b, this._cols[5].a), vertices().addVertex(w, -h, -d, 1.0f, 1.0f, 0.0f, -1.0f, 0.0f, this._cols[5].r, this._cols[5].g, this._cols[5].b, this._cols[5].a), vertices().addVertex(-w, -h, -d, 0.0f, 1.0f, 0.0f, -1.0f, 0.0f, this._cols[5].r, this._cols[5].g, this._cols[5].b, this._cols[5].a));
    }
}

package dji.midware.ar.min3d.objectPrimitives;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Number3d;
import dji.midware.ar.min3d.vos.Uv;

@EXClassNullAway
public class HollowCylinder extends Object3dContainer {
    private double DEG = 0.017453292519943295d;
    private float _height;
    private float _radiusInner;
    private float _radiusOuter;
    private int _segs;

    public HollowCylinder(float $radiusOuter, float $radiusInner, float $height, int $segs) {
        super($segs * 4, $segs * 8);
        this._segs = $segs;
        this._height = $height;
        this._radiusOuter = $radiusOuter;
        this._radiusInner = $radiusInner;
        addHorizontalSurface(false, this._height / 2.0f);
        addHorizontalSurface(true, this._height / -2.0f);
        addVerticalSurface(true);
        addVerticalSurface(false);
    }

    private void addHorizontalSurface(boolean $isTopSide, float $zOffset) {
        Color4 col;
        int indexOffset = this._vertices.size();
        float step = (float) ((360.0d / ((double) this._segs)) * this.DEG);
        if ($isTopSide) {
            col = new Color4(255, 0, 0, 255);
        } else {
            col = new Color4(0, 255, 0, 255);
        }
        for (int i = 0; i < this._segs; i++) {
            float angle = ((float) i) * step;
            float x1 = ((float) Math.sin((double) angle)) * this._radiusOuter;
            float y1 = ((float) Math.cos((double) angle)) * this._radiusOuter;
            vertices().addVertex(new Number3d(x1, y1, $zOffset), new Uv(x1, y1), new Number3d(0.0f, 0.0f, $isTopSide ? -1.0f : 1.0f), col);
            float x2 = ((float) Math.sin((double) angle)) * this._radiusInner;
            float y2 = ((float) Math.cos((double) angle)) * this._radiusInner;
            vertices().addVertex(new Number3d(x2, y2, $zOffset), new Uv(x2, y2), new Number3d(0.0f, 0.0f, $isTopSide ? -1.0f : 1.0f), col);
        }
        for (int i2 = 2; i2 <= this._segs; i2++) {
            addQuad((((i2 * 2) + indexOffset) - 3) - 1, (((i2 * 2) + indexOffset) - 2) - 1, (((i2 * 2) + indexOffset) - 1) - 1, (((i2 * 2) + indexOffset) + 0) - 1, $isTopSide);
        }
        addQuad((((this._segs * 2) + indexOffset) - 1) - 1, (((this._segs * 2) + indexOffset) + 0) - 1, indexOffset + 0, indexOffset + 1, $isTopSide);
    }

    private void addVerticalSurface(boolean $isOuter) {
        int off = this._vertices.size() / 2;
        for (int i = 0; i < this._segs - 1; i++) {
            int ul = i * 2;
            int bl = ul + off;
            int ur = (i * 2) + 2;
            int br = ur + off;
            if (!$isOuter) {
                ul++;
                bl++;
                ur++;
                br++;
            }
            addQuad(ul, bl, ur, br, $isOuter);
        }
        int ul2 = (this._segs - 1) * 2;
        int bl2 = ul2 + off;
        int ur2 = 0;
        int br2 = 0 + off;
        if (!$isOuter) {
            ul2++;
            bl2++;
            ur2 = 0 + 1;
            br2++;
        }
        addQuad(ul2, bl2, ur2, br2, $isOuter);
    }

    private void addQuad(int ul, int bl, int ur, int br, boolean $flipped) {
        if (!$flipped) {
            this._faces.add((short) ul, (short) bl, (short) ur);
            this._faces.add((short) bl, (short) br, (short) ur);
            return;
        }
        this._faces.add((short) ur, (short) br, (short) ul);
        this._faces.add((short) br, (short) bl, (short) ul);
    }
}

package dji.midware.ar.min3d.objectPrimitives;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Utils;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.vos.Color4;
import dji.midware.ar.min3d.vos.Number3d;

@EXClassNullAway
public class Sphere extends Object3dContainer {
    private int _cols;
    private float _radius;
    private int _rows;

    public Sphere(float $radius, int $columns, int $rows, Boolean $useUvs, Boolean $useNormals, Boolean $useVertexColors) {
        super(($rows + 1) * ($columns + 1), $columns * $rows * 2, $useUvs, $useNormals, $useVertexColors);
        this._cols = $columns;
        this._rows = $rows;
        this._radius = $radius;
        build();
    }

    public Sphere(float $radius, int $columns, int $rows) {
        super(($rows + 1) * ($columns + 1), $columns * $rows * 2, true, true, true);
        this._cols = $columns;
        this._rows = $rows;
        this._radius = $radius;
        build();
    }

    public Sphere(float $radius, int $columns, int $rows, Color4 color) {
        super(($rows + 1) * ($columns + 1), $columns * $rows * 2, true, true, true);
        defaultColor(color);
        this._cols = $columns;
        this._rows = $rows;
        this._radius = $radius;
        build();
    }

    private void build() {
        Number3d n = new Number3d();
        Number3d pos = new Number3d();
        Number3d posFull = new Number3d();
        if (defaultColor() == null) {
            defaultColor(new Color4());
        }
        for (int r = 0; r <= this._rows; r++) {
            float v = ((float) r) / ((float) this._rows);
            n.setAll(0.0f, 1.0f, 0.0f);
            n.rotateZ(v * 3.1415927f);
            for (int c = 0; c <= this._cols; c++) {
                float u = ((float) c) / ((float) this._cols);
                pos.setAllFrom(n);
                pos.rotateY(u * 6.2831855f);
                posFull.setAllFrom(pos);
                posFull.multiply(Float.valueOf(this._radius));
                vertices().addVertex(posFull.x, posFull.y, posFull.z, u, v, pos.x, pos.y, pos.z, defaultColor().r, defaultColor().g, defaultColor().b, defaultColor().a);
            }
        }
        int colLength = this._cols + 1;
        for (int r2 = 0; r2 < this._rows; r2++) {
            int offset = r2 * colLength;
            for (int c2 = 0; c2 < this._cols; c2++) {
                Utils.addQuad(this, offset + c2, offset + c2 + 1, offset + c2 + 1 + colLength, offset + c2 + 0 + colLength);
            }
        }
    }
}

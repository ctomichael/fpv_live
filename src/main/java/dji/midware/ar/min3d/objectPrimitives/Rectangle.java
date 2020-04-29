package dji.midware.ar.min3d.objectPrimitives;

import dji.fieldAnnotation.EXClassNullAway;
import dji.midware.ar.min3d.Utils;
import dji.midware.ar.min3d.core.Object3dContainer;
import dji.midware.ar.min3d.vos.Color4;

@EXClassNullAway
public class Rectangle extends Object3dContainer {
    public Rectangle(float $width, float $height, int $segsW, int $segsH) {
        this($width, $height, $segsW, $segsH, new Color4(255, 0, 0, 255));
    }

    public Rectangle(float $width, float $height, int $segsW, int $segsH, Color4 color) {
        super($segsW * 4 * $segsH, $segsW * 2 * $segsH);
        float w = $width / ((float) $segsW);
        float h = $height / ((float) $segsH);
        float width5 = $width / 2.0f;
        float height5 = $height / 2.0f;
        for (int row = 0; row <= $segsH; row++) {
            for (int col = 0; col <= $segsW; col++) {
                vertices().addVertex((((float) col) * w) - width5, (((float) row) * h) - height5, 0.0f, ((float) col) / ((float) $segsW), 1.0f - (((float) row) / ((float) $segsH)), 0.0f, 0.0f, 1.0f, color.r, color.g, color.b, color.a);
            }
        }
        int colspan = $segsW + 1;
        for (int row2 = 1; row2 <= $segsH; row2++) {
            for (int col2 = 1; col2 <= $segsW; col2++) {
                int lr = (row2 * colspan) + col2;
                int ur = lr - colspan;
                Utils.addQuad(this, ur - 1, ur, lr, lr - 1);
            }
        }
    }
}

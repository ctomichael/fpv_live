package com.dji.scan.qr.camera;

import android.graphics.Rect;
import android.util.Log;
import com.dji.scan.qr.Size;
import dji.fieldAnnotation.EXClassNullAway;

@EXClassNullAway
public class CenterCropStrategy extends PreviewScalingStrategy {
    private static final String TAG = CenterCropStrategy.class.getSimpleName();

    /* access modifiers changed from: protected */
    public float getScore(Size size, Size desired) {
        float scaleScore;
        if (size.width <= 0 || size.height <= 0) {
            return 0.0f;
        }
        Size scaled = size.scaleCrop(desired);
        float scaleRatio = (((float) scaled.width) * 1.0f) / ((float) size.width);
        if (scaleRatio > 1.0f) {
            scaleScore = (float) Math.pow((double) (1.0f / scaleRatio), 1.1d);
        } else {
            scaleScore = scaleRatio;
        }
        float cropRatio = ((((float) scaled.width) * 1.0f) / ((float) desired.width)) + ((((float) scaled.height) * 1.0f) / ((float) desired.height));
        return scaleScore * ((1.0f / cropRatio) / cropRatio);
    }

    public Rect scalePreview(Size previewSize, Size viewfinderSize) {
        Size scaledPreview = previewSize.scaleCrop(viewfinderSize);
        Log.i(TAG, "Preview: " + previewSize + "; Scaled: " + scaledPreview + "; Want: " + viewfinderSize);
        int dx = (scaledPreview.width - viewfinderSize.width) / 2;
        int dy = (scaledPreview.height - viewfinderSize.height) / 2;
        return new Rect(-dx, -dy, scaledPreview.width - dx, scaledPreview.height - dy);
    }
}

package com.dji.scan.qr.camera;

import android.graphics.Rect;
import android.util.Log;
import com.dji.scan.qr.Size;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@EXClassNullAway
public class LegacyPreviewScalingStrategy extends PreviewScalingStrategy {
    private static final String TAG = LegacyPreviewScalingStrategy.class.getSimpleName();

    public Size getBestPreviewSize(List<Size> sizes, final Size desired) {
        if (desired == null) {
            return sizes.get(0);
        }
        Collections.sort(sizes, new Comparator<Size>() {
            /* class com.dji.scan.qr.camera.LegacyPreviewScalingStrategy.AnonymousClass1 */

            public int compare(Size a, Size b) {
                int aScale = LegacyPreviewScalingStrategy.scale(a, desired).width - a.width;
                int bScale = LegacyPreviewScalingStrategy.scale(b, desired).width - b.width;
                if (aScale == 0 && bScale == 0) {
                    return a.compareTo(b);
                }
                if (aScale == 0) {
                    return -1;
                }
                if (bScale == 0) {
                    return 1;
                }
                if (aScale < 0 && bScale < 0) {
                    return a.compareTo(b);
                }
                if (aScale > 0 && bScale > 0) {
                    return -a.compareTo(b);
                }
                if (aScale >= 0) {
                    return 1;
                }
                return -1;
            }
        });
        Log.i(TAG, "Viewfinder size: " + desired);
        Log.i(TAG, "Preview in order of preference: " + sizes);
        return sizes.get(0);
    }

    public static Size scale(Size from, Size to) {
        Size scaled66;
        Size current = from;
        if (!to.fitsIn(current)) {
            while (true) {
                Size scaled150 = current.scale(3, 2);
                Size scaled200 = current.scale(2, 1);
                if (to.fitsIn(scaled150)) {
                    return scaled150;
                }
                if (to.fitsIn(scaled200)) {
                    return scaled200;
                }
                current = scaled200;
            }
        } else {
            while (true) {
                scaled66 = current.scale(2, 3);
                Size scaled50 = current.scale(1, 2);
                if (!to.fitsIn(scaled50)) {
                    break;
                }
                current = scaled50;
            }
            if (to.fitsIn(scaled66)) {
                return scaled66;
            }
            return current;
        }
    }

    public Rect scalePreview(Size previewSize, Size viewfinderSize) {
        Size scaledPreview = scale(previewSize, viewfinderSize);
        Log.i(TAG, "Preview: " + previewSize + "; Scaled: " + scaledPreview + "; Want: " + viewfinderSize);
        int dx = (scaledPreview.width - viewfinderSize.width) / 2;
        int dy = (scaledPreview.height - viewfinderSize.height) / 2;
        return new Rect(-dx, -dy, scaledPreview.width - dx, scaledPreview.height - dy);
    }
}

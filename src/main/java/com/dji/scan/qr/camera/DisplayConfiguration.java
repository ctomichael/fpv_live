package com.dji.scan.qr.camera;

import android.graphics.Rect;
import com.dji.scan.qr.Size;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.List;

@EXClassNullAway
public class DisplayConfiguration {
    private static final String TAG = DisplayConfiguration.class.getSimpleName();
    private boolean center = false;
    private PreviewScalingStrategy previewScalingStrategy = new FitCenterStrategy();
    private int rotation;
    private Size viewfinderSize;

    public DisplayConfiguration(int rotation2) {
        this.rotation = rotation2;
    }

    public DisplayConfiguration(int rotation2, Size viewfinderSize2) {
        this.rotation = rotation2;
        this.viewfinderSize = viewfinderSize2;
    }

    public int getRotation() {
        return this.rotation;
    }

    public Size getViewfinderSize() {
        return this.viewfinderSize;
    }

    public PreviewScalingStrategy getPreviewScalingStrategy() {
        return this.previewScalingStrategy;
    }

    public void setPreviewScalingStrategy(PreviewScalingStrategy previewScalingStrategy2) {
        this.previewScalingStrategy = previewScalingStrategy2;
    }

    public Size getDesiredPreviewSize(boolean rotate) {
        if (this.viewfinderSize == null) {
            return null;
        }
        if (rotate) {
            return this.viewfinderSize.rotate();
        }
        return this.viewfinderSize;
    }

    public Size getBestPreviewSize(List<Size> sizes, boolean isRotated) {
        return this.previewScalingStrategy.getBestPreviewSize(sizes, getDesiredPreviewSize(isRotated));
    }

    public Rect scalePreview(Size previewSize) {
        return this.previewScalingStrategy.scalePreview(previewSize, this.viewfinderSize);
    }
}

package com.dji.widget2.videoview;

import android.graphics.Matrix;
import android.util.Size;

public class ScaleManager {
    private Size mVideoSize;
    private Size mViewSize;

    public ScaleManager(Size viewSize, Size videoSize) {
        this.mViewSize = viewSize;
        this.mVideoSize = videoSize;
    }

    public Matrix getScaleMatrix(ScalableType scalableType) {
        switch (scalableType) {
            case NONE:
                return getNoScale();
            case FIT_XY:
                return fitXY();
            case FIT_CENTER:
                return fitCenter();
            case FIT_START:
                return fitStart();
            case FIT_END:
                return fitEnd();
            case LEFT_TOP:
                return getOriginalScale(PivotPoint.LEFT_TOP);
            case LEFT_CENTER:
                return getOriginalScale(PivotPoint.LEFT_CENTER);
            case LEFT_BOTTOM:
                return getOriginalScale(PivotPoint.LEFT_BOTTOM);
            case CENTER_TOP:
                return getOriginalScale(PivotPoint.CENTER_TOP);
            case CENTER:
                return getOriginalScale(PivotPoint.CENTER);
            case CENTER_BOTTOM:
                return getOriginalScale(PivotPoint.CENTER_BOTTOM);
            case RIGHT_TOP:
                return getOriginalScale(PivotPoint.RIGHT_TOP);
            case RIGHT_CENTER:
                return getOriginalScale(PivotPoint.RIGHT_CENTER);
            case RIGHT_BOTTOM:
                return getOriginalScale(PivotPoint.RIGHT_BOTTOM);
            case LEFT_TOP_CROP:
                return getCropScale(PivotPoint.LEFT_TOP);
            case LEFT_CENTER_CROP:
                return getCropScale(PivotPoint.LEFT_CENTER);
            case LEFT_BOTTOM_CROP:
                return getCropScale(PivotPoint.LEFT_BOTTOM);
            case CENTER_TOP_CROP:
                return getCropScale(PivotPoint.CENTER_TOP);
            case CENTER_CROP:
                return getCropScale(PivotPoint.CENTER);
            case CENTER_BOTTOM_CROP:
                return getCropScale(PivotPoint.CENTER_BOTTOM);
            case RIGHT_TOP_CROP:
                return getCropScale(PivotPoint.RIGHT_TOP);
            case RIGHT_CENTER_CROP:
                return getCropScale(PivotPoint.RIGHT_CENTER);
            case RIGHT_BOTTOM_CROP:
                return getCropScale(PivotPoint.RIGHT_BOTTOM);
            case START_INSIDE:
                return startInside();
            case CENTER_INSIDE:
                return centerInside();
            case END_INSIDE:
                return endInside();
            default:
                return null;
        }
    }

    private Matrix getNoScale() {
        return getMatrix(((float) this.mVideoSize.getWidth()) / ((float) this.mViewSize.getWidth()), ((float) this.mVideoSize.getWidth()) / ((float) this.mViewSize.getHeight()), PivotPoint.LEFT_TOP);
    }

    private Matrix getOriginalScale(PivotPoint pivotPoint) {
        return getMatrix(((float) this.mVideoSize.getWidth()) / ((float) this.mViewSize.getWidth()), ((float) this.mVideoSize.getHeight()) / ((float) this.mViewSize.getHeight()), pivotPoint);
    }

    private Matrix getMatrix(float sx, float sy, PivotPoint pivotPoint) {
        switch (pivotPoint) {
            case LEFT_TOP:
                return getMatrix(sx, sy, 0.0f, 0.0f);
            case LEFT_CENTER:
                return getMatrix(sx, sy, 0.0f, ((float) this.mViewSize.getHeight()) / 2.0f);
            case LEFT_BOTTOM:
                return getMatrix(sx, sy, 0.0f, (float) this.mViewSize.getHeight());
            case CENTER_TOP:
                return getMatrix(sx, sy, ((float) this.mViewSize.getWidth()) / 2.0f, 0.0f);
            case CENTER:
                return getMatrix(sx, sy, ((float) this.mViewSize.getWidth()) / 2.0f, ((float) this.mViewSize.getHeight()) / 2.0f);
            case CENTER_BOTTOM:
                return getMatrix(sx, sy, ((float) this.mViewSize.getWidth()) / 2.0f, (float) this.mViewSize.getHeight());
            case RIGHT_TOP:
                return getMatrix(sx, sy, (float) this.mViewSize.getWidth(), 0.0f);
            case RIGHT_CENTER:
                return getMatrix(sx, sy, (float) this.mViewSize.getWidth(), ((float) this.mViewSize.getHeight()) / 2.0f);
            case RIGHT_BOTTOM:
                return getMatrix(sx, sy, (float) this.mViewSize.getWidth(), (float) this.mViewSize.getHeight());
            default:
                throw new IllegalArgumentException("Illegal PivotPoint");
        }
    }

    private Matrix getMatrix(float sx, float sy, float px, float py) {
        Matrix matrix = new Matrix();
        matrix.setScale(sx, sy, px, py);
        return matrix;
    }

    private Matrix fitXY() {
        return getMatrix(1.0f, 1.0f, PivotPoint.LEFT_TOP);
    }

    private Matrix fitCenter() {
        return getFitScale(PivotPoint.CENTER);
    }

    private Matrix fitStart() {
        return getFitScale(PivotPoint.LEFT_TOP);
    }

    private Matrix fitEnd() {
        return getFitScale(PivotPoint.RIGHT_BOTTOM);
    }

    private Matrix getFitScale(PivotPoint pivotPoint) {
        float sx = ((float) this.mViewSize.getWidth()) / ((float) this.mVideoSize.getWidth());
        float sy = ((float) this.mViewSize.getHeight()) / ((float) this.mVideoSize.getHeight());
        float minScale = Math.min(sx, sy);
        return getMatrix(minScale / sx, minScale / sy, pivotPoint);
    }

    private Matrix getCropScale(PivotPoint pivotPoint) {
        float sx = ((float) this.mViewSize.getWidth()) / ((float) this.mVideoSize.getWidth());
        float sy = ((float) this.mViewSize.getHeight()) / ((float) this.mVideoSize.getHeight());
        float maxScale = Math.max(sx, sy);
        return getMatrix(maxScale / sx, maxScale / sy, pivotPoint);
    }

    private Matrix startInside() {
        if (this.mVideoSize.getHeight() > this.mViewSize.getWidth() || this.mVideoSize.getHeight() > this.mViewSize.getHeight()) {
            return fitStart();
        }
        return getOriginalScale(PivotPoint.LEFT_TOP);
    }

    private Matrix centerInside() {
        if (this.mVideoSize.getHeight() > this.mViewSize.getWidth() || this.mVideoSize.getHeight() > this.mViewSize.getHeight()) {
            return fitCenter();
        }
        return getOriginalScale(PivotPoint.CENTER);
    }

    private Matrix endInside() {
        if (this.mVideoSize.getHeight() > this.mViewSize.getWidth() || this.mVideoSize.getHeight() > this.mViewSize.getHeight()) {
            return fitEnd();
        }
        return getOriginalScale(PivotPoint.RIGHT_BOTTOM);
    }
}

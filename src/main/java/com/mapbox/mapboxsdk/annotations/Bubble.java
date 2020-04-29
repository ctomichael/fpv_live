package com.mapbox.mapboxsdk.annotations;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

@Deprecated
class Bubble extends Drawable {
    private float arrowHeight;
    private float arrowPosition;
    private float arrowWidth;
    private float cornersRadius;
    @NonNull
    private Paint paint = new Paint(1);
    @NonNull
    private Path path = new Path();
    private RectF rect;
    private Paint strokePaint;
    private Path strokePath;
    private float strokeWidth;

    Bubble(@NonNull RectF rect2, @NonNull ArrowDirection arrowDirection, float arrowWidth2, float arrowHeight2, float arrowPosition2, float cornersRadius2, int bubbleColor, float strokeWidth2, int strokeColor) {
        this.rect = rect2;
        this.arrowWidth = arrowWidth2;
        this.arrowHeight = arrowHeight2;
        this.arrowPosition = arrowPosition2;
        this.cornersRadius = cornersRadius2;
        this.paint.setColor(bubbleColor);
        this.strokeWidth = strokeWidth2;
        if (strokeWidth2 > 0.0f) {
            this.strokePaint = new Paint(1);
            this.strokePaint.setColor(strokeColor);
            this.strokePath = new Path();
            initPath(arrowDirection, this.path, strokeWidth2);
            initPath(arrowDirection, this.strokePath, 0.0f);
            return;
        }
        initPath(arrowDirection, this.path, 0.0f);
    }

    /* access modifiers changed from: protected */
    public void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
    }

    public void draw(@NonNull Canvas canvas) {
        if (this.strokeWidth > 0.0f) {
            canvas.drawPath(this.strokePath, this.strokePaint);
        }
        canvas.drawPath(this.path, this.paint);
    }

    public int getOpacity() {
        return -3;
    }

    public void setAlpha(int alpha) {
        this.paint.setAlpha(alpha);
    }

    public void setColorFilter(ColorFilter cf) {
        this.paint.setColorFilter(cf);
    }

    public int getIntrinsicWidth() {
        return (int) this.rect.width();
    }

    public int getIntrinsicHeight() {
        return (int) this.rect.height();
    }

    private void initPath(@NonNull ArrowDirection arrowDirection, @NonNull Path path2, float strokeWidth2) {
        switch (arrowDirection.getValue()) {
            case 0:
                if (this.cornersRadius <= 0.0f) {
                    initLeftSquarePath(this.rect, path2, strokeWidth2);
                    return;
                } else if (strokeWidth2 <= 0.0f || strokeWidth2 <= this.cornersRadius) {
                    initLeftRoundedPath(this.rect, path2, strokeWidth2);
                    return;
                } else {
                    initLeftSquarePath(this.rect, path2, strokeWidth2);
                    return;
                }
            case 1:
                if (this.cornersRadius <= 0.0f) {
                    initRightSquarePath(this.rect, path2, strokeWidth2);
                    return;
                } else if (strokeWidth2 <= 0.0f || strokeWidth2 <= this.cornersRadius) {
                    initRightRoundedPath(this.rect, path2, strokeWidth2);
                    return;
                } else {
                    initRightSquarePath(this.rect, path2, strokeWidth2);
                    return;
                }
            case 2:
                if (this.cornersRadius <= 0.0f) {
                    initTopSquarePath(this.rect, path2, strokeWidth2);
                    return;
                } else if (strokeWidth2 <= 0.0f || strokeWidth2 <= this.cornersRadius) {
                    initTopRoundedPath(this.rect, path2, strokeWidth2);
                    return;
                } else {
                    initTopSquarePath(this.rect, path2, strokeWidth2);
                    return;
                }
            case 3:
                if (this.cornersRadius <= 0.0f) {
                    initBottomSquarePath(this.rect, path2, strokeWidth2);
                    return;
                } else if (strokeWidth2 <= 0.0f || strokeWidth2 <= this.cornersRadius) {
                    initBottomRoundedPath(this.rect, path2, strokeWidth2);
                    return;
                } else {
                    initBottomSquarePath(this.rect, path2, strokeWidth2);
                    return;
                }
            default:
                return;
        }
    }

    private void initLeftSquarePath(@NonNull RectF rect2, @NonNull Path path2, float strokeWidth2) {
        path2.moveTo(this.arrowWidth + rect2.left + strokeWidth2, rect2.top + strokeWidth2);
        path2.lineTo(rect2.width() - strokeWidth2, rect2.top + strokeWidth2);
        path2.lineTo(rect2.right - strokeWidth2, rect2.bottom - strokeWidth2);
        path2.lineTo(rect2.left + this.arrowWidth + strokeWidth2, rect2.bottom - strokeWidth2);
        path2.lineTo(rect2.left + this.arrowWidth + strokeWidth2, (this.arrowHeight + this.arrowPosition) - (strokeWidth2 / 2.0f));
        path2.lineTo(rect2.left + strokeWidth2 + strokeWidth2, this.arrowPosition + (this.arrowHeight / 2.0f));
        path2.lineTo(rect2.left + this.arrowWidth + strokeWidth2, this.arrowPosition + (strokeWidth2 / 2.0f));
        path2.lineTo(rect2.left + this.arrowWidth + strokeWidth2, rect2.top + strokeWidth2);
        path2.close();
    }

    private void initLeftRoundedPath(@NonNull RectF rect2, @NonNull Path path2, float strokeWidth2) {
        path2.moveTo(this.arrowWidth + rect2.left + this.cornersRadius + strokeWidth2, rect2.top + strokeWidth2);
        path2.lineTo((rect2.width() - this.cornersRadius) - strokeWidth2, rect2.top + strokeWidth2);
        path2.arcTo(new RectF(rect2.right - this.cornersRadius, rect2.top + strokeWidth2, rect2.right - strokeWidth2, this.cornersRadius + rect2.top), 270.0f, 90.0f);
        path2.lineTo(rect2.right - strokeWidth2, (rect2.bottom - this.cornersRadius) - strokeWidth2);
        path2.arcTo(new RectF(rect2.right - this.cornersRadius, rect2.bottom - this.cornersRadius, rect2.right - strokeWidth2, rect2.bottom - strokeWidth2), 0.0f, 90.0f);
        path2.lineTo(rect2.left + this.arrowWidth + this.cornersRadius + strokeWidth2, rect2.bottom - strokeWidth2);
        path2.arcTo(new RectF(rect2.left + this.arrowWidth + strokeWidth2, rect2.bottom - this.cornersRadius, this.cornersRadius + rect2.left + this.arrowWidth, rect2.bottom - strokeWidth2), 90.0f, 90.0f);
        path2.lineTo(rect2.left + this.arrowWidth + strokeWidth2, (this.arrowHeight + this.arrowPosition) - (strokeWidth2 / 2.0f));
        path2.lineTo(rect2.left + strokeWidth2 + strokeWidth2, this.arrowPosition + (this.arrowHeight / 2.0f));
        path2.lineTo(rect2.left + this.arrowWidth + strokeWidth2, this.arrowPosition + (strokeWidth2 / 2.0f));
        path2.lineTo(rect2.left + this.arrowWidth + strokeWidth2, rect2.top + this.cornersRadius + strokeWidth2);
        path2.arcTo(new RectF(rect2.left + this.arrowWidth + strokeWidth2, rect2.top + strokeWidth2, this.cornersRadius + rect2.left + this.arrowWidth, this.cornersRadius + rect2.top), 180.0f, 90.0f);
        path2.close();
    }

    private void initTopSquarePath(@NonNull RectF rect2, @NonNull Path path2, float strokeWidth2) {
        path2.moveTo(rect2.left + this.arrowPosition + strokeWidth2, rect2.top + this.arrowHeight + strokeWidth2);
        path2.lineTo(rect2.left + this.arrowPosition + (strokeWidth2 / 2.0f), rect2.top + this.arrowHeight + strokeWidth2);
        path2.lineTo(rect2.left + (this.arrowWidth / 2.0f) + this.arrowPosition, rect2.top + strokeWidth2 + strokeWidth2);
        path2.lineTo(((rect2.left + this.arrowWidth) + this.arrowPosition) - (strokeWidth2 / 2.0f), rect2.top + this.arrowHeight + strokeWidth2);
        path2.lineTo(rect2.right - strokeWidth2, rect2.top + this.arrowHeight + strokeWidth2);
        path2.lineTo(rect2.right - strokeWidth2, rect2.bottom - strokeWidth2);
        path2.lineTo(rect2.left + strokeWidth2, rect2.bottom - strokeWidth2);
        path2.lineTo(rect2.left + strokeWidth2, rect2.top + this.arrowHeight + strokeWidth2);
        path2.lineTo(rect2.left + this.arrowPosition + strokeWidth2, rect2.top + this.arrowHeight + strokeWidth2);
        path2.close();
    }

    private void initTopRoundedPath(@NonNull RectF rect2, @NonNull Path path2, float strokeWidth2) {
        path2.moveTo(rect2.left + Math.min(this.arrowPosition, this.cornersRadius) + strokeWidth2, rect2.top + this.arrowHeight + strokeWidth2);
        path2.lineTo(rect2.left + this.arrowPosition + (strokeWidth2 / 2.0f), rect2.top + this.arrowHeight + strokeWidth2);
        path2.lineTo(rect2.left + (this.arrowWidth / 2.0f) + this.arrowPosition, rect2.top + strokeWidth2 + strokeWidth2);
        path2.lineTo(((rect2.left + this.arrowWidth) + this.arrowPosition) - (strokeWidth2 / 2.0f), rect2.top + this.arrowHeight + strokeWidth2);
        path2.lineTo((rect2.right - this.cornersRadius) - strokeWidth2, rect2.top + this.arrowHeight + strokeWidth2);
        path2.arcTo(new RectF(rect2.right - this.cornersRadius, rect2.top + this.arrowHeight + strokeWidth2, rect2.right - strokeWidth2, this.cornersRadius + rect2.top + this.arrowHeight), 270.0f, 90.0f);
        path2.lineTo(rect2.right - strokeWidth2, (rect2.bottom - this.cornersRadius) - strokeWidth2);
        path2.arcTo(new RectF(rect2.right - this.cornersRadius, rect2.bottom - this.cornersRadius, rect2.right - strokeWidth2, rect2.bottom - strokeWidth2), 0.0f, 90.0f);
        path2.lineTo(rect2.left + this.cornersRadius + strokeWidth2, rect2.bottom - strokeWidth2);
        path2.arcTo(new RectF(rect2.left + strokeWidth2, rect2.bottom - this.cornersRadius, this.cornersRadius + rect2.left, rect2.bottom - strokeWidth2), 90.0f, 90.0f);
        path2.lineTo(rect2.left + strokeWidth2, rect2.top + this.arrowHeight + this.cornersRadius + strokeWidth2);
        path2.arcTo(new RectF(rect2.left + strokeWidth2, rect2.top + this.arrowHeight + strokeWidth2, this.cornersRadius + rect2.left, this.cornersRadius + rect2.top + this.arrowHeight), 180.0f, 90.0f);
        path2.close();
    }

    private void initRightSquarePath(@NonNull RectF rect2, @NonNull Path path2, float strokeWidth2) {
        path2.moveTo(rect2.left + strokeWidth2, rect2.top + strokeWidth2);
        path2.lineTo((rect2.width() - this.arrowWidth) - strokeWidth2, rect2.top + strokeWidth2);
        path2.lineTo((rect2.right - this.arrowWidth) - strokeWidth2, this.arrowPosition + (strokeWidth2 / 2.0f));
        path2.lineTo((rect2.right - strokeWidth2) - strokeWidth2, this.arrowPosition + (this.arrowHeight / 2.0f));
        path2.lineTo((rect2.right - this.arrowWidth) - strokeWidth2, (this.arrowPosition + this.arrowHeight) - (strokeWidth2 / 2.0f));
        path2.lineTo((rect2.right - this.arrowWidth) - strokeWidth2, rect2.bottom - strokeWidth2);
        path2.lineTo(rect2.left + strokeWidth2, rect2.bottom - strokeWidth2);
        path2.lineTo(rect2.left + strokeWidth2, rect2.top + strokeWidth2);
        path2.close();
    }

    private void initRightRoundedPath(@NonNull RectF rect2, @NonNull Path path2, float strokeWidth2) {
        path2.moveTo(rect2.left + this.cornersRadius + strokeWidth2, rect2.top + strokeWidth2);
        path2.lineTo(((rect2.width() - this.cornersRadius) - this.arrowWidth) - strokeWidth2, rect2.top + strokeWidth2);
        path2.arcTo(new RectF((rect2.right - this.cornersRadius) - this.arrowWidth, rect2.top + strokeWidth2, (rect2.right - this.arrowWidth) - strokeWidth2, this.cornersRadius + rect2.top), 270.0f, 90.0f);
        path2.lineTo((rect2.right - this.arrowWidth) - strokeWidth2, this.arrowPosition + (strokeWidth2 / 2.0f));
        path2.lineTo((rect2.right - strokeWidth2) - strokeWidth2, this.arrowPosition + (this.arrowHeight / 2.0f));
        path2.lineTo((rect2.right - this.arrowWidth) - strokeWidth2, (this.arrowPosition + this.arrowHeight) - (strokeWidth2 / 2.0f));
        path2.lineTo((rect2.right - this.arrowWidth) - strokeWidth2, (rect2.bottom - this.cornersRadius) - strokeWidth2);
        path2.arcTo(new RectF((rect2.right - this.cornersRadius) - this.arrowWidth, rect2.bottom - this.cornersRadius, (rect2.right - this.arrowWidth) - strokeWidth2, rect2.bottom - strokeWidth2), 0.0f, 90.0f);
        path2.lineTo(rect2.left + this.arrowWidth + strokeWidth2, rect2.bottom - strokeWidth2);
        path2.arcTo(new RectF(rect2.left + strokeWidth2, rect2.bottom - this.cornersRadius, this.cornersRadius + rect2.left, rect2.bottom - strokeWidth2), 90.0f, 90.0f);
        path2.arcTo(new RectF(rect2.left + strokeWidth2, rect2.top + strokeWidth2, this.cornersRadius + rect2.left, this.cornersRadius + rect2.top), 180.0f, 90.0f);
        path2.close();
    }

    private void initBottomSquarePath(@NonNull RectF rect2, @NonNull Path path2, float strokeWidth2) {
        path2.moveTo(rect2.left + strokeWidth2, rect2.top + strokeWidth2);
        path2.lineTo(rect2.right - strokeWidth2, rect2.top + strokeWidth2);
        path2.lineTo(rect2.right - strokeWidth2, (rect2.bottom - this.arrowHeight) - strokeWidth2);
        path2.lineTo(((rect2.left + this.arrowWidth) + this.arrowPosition) - (strokeWidth2 / 2.0f), (rect2.bottom - this.arrowHeight) - strokeWidth2);
        path2.lineTo(rect2.left + this.arrowPosition + (this.arrowWidth / 2.0f), (rect2.bottom - strokeWidth2) - strokeWidth2);
        path2.lineTo(rect2.left + this.arrowPosition + (strokeWidth2 / 2.0f), (rect2.bottom - this.arrowHeight) - strokeWidth2);
        path2.lineTo(rect2.left + this.arrowPosition + strokeWidth2, (rect2.bottom - this.arrowHeight) - strokeWidth2);
        path2.lineTo(rect2.left + strokeWidth2, (rect2.bottom - this.arrowHeight) - strokeWidth2);
        path2.lineTo(rect2.left + strokeWidth2, rect2.top + strokeWidth2);
        path2.close();
    }

    private void initBottomRoundedPath(@NonNull RectF rect2, @NonNull Path path2, float strokeWidth2) {
        path2.moveTo(rect2.left + this.cornersRadius + strokeWidth2, rect2.top + strokeWidth2);
        path2.lineTo((rect2.width() - this.cornersRadius) - strokeWidth2, rect2.top + strokeWidth2);
        path2.arcTo(new RectF(rect2.right - this.cornersRadius, rect2.top + strokeWidth2, rect2.right - strokeWidth2, this.cornersRadius + rect2.top), 270.0f, 90.0f);
        path2.lineTo(rect2.right - strokeWidth2, ((rect2.bottom - this.arrowHeight) - this.cornersRadius) - strokeWidth2);
        path2.arcTo(new RectF(rect2.right - this.cornersRadius, (rect2.bottom - this.cornersRadius) - this.arrowHeight, rect2.right - strokeWidth2, (rect2.bottom - this.arrowHeight) - strokeWidth2), 0.0f, 90.0f);
        path2.lineTo(((rect2.left + this.arrowWidth) + this.arrowPosition) - (strokeWidth2 / 2.0f), (rect2.bottom - this.arrowHeight) - strokeWidth2);
        path2.lineTo(rect2.left + this.arrowPosition + (this.arrowWidth / 2.0f), (rect2.bottom - strokeWidth2) - strokeWidth2);
        path2.lineTo(rect2.left + this.arrowPosition + (strokeWidth2 / 2.0f), (rect2.bottom - this.arrowHeight) - strokeWidth2);
        path2.lineTo(rect2.left + Math.min(this.cornersRadius, this.arrowPosition) + strokeWidth2, (rect2.bottom - this.arrowHeight) - strokeWidth2);
        path2.arcTo(new RectF(rect2.left + strokeWidth2, (rect2.bottom - this.cornersRadius) - this.arrowHeight, this.cornersRadius + rect2.left, (rect2.bottom - this.arrowHeight) - strokeWidth2), 90.0f, 90.0f);
        path2.lineTo(rect2.left + strokeWidth2, rect2.top + this.cornersRadius + strokeWidth2);
        path2.arcTo(new RectF(rect2.left + strokeWidth2, rect2.top + strokeWidth2, this.cornersRadius + rect2.left, this.cornersRadius + rect2.top), 180.0f, 90.0f);
        path2.close();
    }
}

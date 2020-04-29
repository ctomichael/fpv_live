package com.dji.scan.qr;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Keep;
import android.util.AttributeSet;
import android.view.View;
import com.dji.pubmodule.R;
import com.dji.scan.qr.CameraPreview;
import com.google.zxing.ResultPoint;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.ArrayList;
import java.util.List;

@Keep
@EXClassNullAway
public class ViewfinderView extends View {
    protected static final long ANIMATION_DELAY = 40;
    protected static final int CURRENT_POINT_OPACITY = 160;
    protected static final int MAX_RESULT_POINTS = 20;
    private static final int MIDDLE_LINE_WIDTH = 6;
    protected static final int POINT_SIZE = 6;
    protected static final int[] SCANNER_ALPHA = {0, 64, 128, 192, 255, 192, 128, 64};
    protected static final String TAG = ViewfinderView.class.getSimpleName();
    protected CameraPreview cameraPreview;
    protected Rect framingRect;
    private boolean isFirst;
    protected final int laserColor;
    protected List<ResultPoint> lastPossibleResultPoints;
    protected final int maskColor;
    protected final Paint paint = new Paint(1);
    protected List<ResultPoint> possibleResultPoints;
    protected Rect previewFramingRect;
    protected Bitmap resultBitmap;
    protected final int resultColor;
    protected final int resultPointColor;
    private Bitmap scan_lineBitmap;
    protected int scannerAlpha;
    private int slideBottom;
    private int slideTop;
    private float speed_index = 10.0f;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Resources resources = getResources();
        TypedArray attributes = getContext().obtainStyledAttributes(attrs, R.styleable.zxing_finder);
        this.maskColor = attributes.getColor(R.styleable.zxing_finder_zxing_viewfinder_mask, resources.getColor(R.color.zxing_viewfinder_mask));
        this.resultColor = attributes.getColor(R.styleable.zxing_finder_zxing_result_view, resources.getColor(R.color.zxing_result_view));
        this.laserColor = attributes.getColor(R.styleable.zxing_finder_zxing_viewfinder_laser, resources.getColor(R.color.zxing_custom_viewfinder_laser));
        this.resultPointColor = attributes.getColor(R.styleable.zxing_finder_zxing_possible_result_points, resources.getColor(R.color.zxing_custom_viewfinder_laser));
        attributes.recycle();
        this.scannerAlpha = 0;
        this.possibleResultPoints = new ArrayList(20);
        this.lastPossibleResultPoints = new ArrayList(20);
    }

    public void setCameraPreview(CameraPreview view) {
        this.cameraPreview = view;
        view.addStateListener(new CameraPreview.StateListener() {
            /* class com.dji.scan.qr.ViewfinderView.AnonymousClass1 */

            public void previewSized() {
                ViewfinderView.this.refreshSizes();
                ViewfinderView.this.invalidate();
            }

            public void previewStarted() {
            }

            public void previewStopped() {
            }

            public void cameraError(Exception error) {
            }

            public void cameraClosed() {
            }
        });
    }

    /* access modifiers changed from: protected */
    public void refreshSizes() {
        if (this.cameraPreview != null) {
            Rect framingRect2 = this.cameraPreview.getFramingRect();
            Rect previewFramingRect2 = this.cameraPreview.getPreviewFramingRect();
            if (framingRect2 != null && previewFramingRect2 != null) {
                this.framingRect = framingRect2;
                this.previewFramingRect = previewFramingRect2;
            }
        }
    }

    public void onDraw(Canvas canvas) {
        int i;
        refreshSizes();
        if (this.framingRect != null && this.previewFramingRect != null) {
            Rect frame = this.framingRect;
            Rect previewFrame = this.previewFramingRect;
            int width = canvas.getWidth();
            int height = canvas.getHeight();
            Paint paint2 = this.paint;
            if (this.resultBitmap != null) {
                i = this.resultColor;
            } else {
                i = this.maskColor;
            }
            paint2.setColor(i);
            canvas.drawRect(0.0f, 0.0f, (float) width, (float) frame.top, this.paint);
            canvas.drawRect(0.0f, (float) frame.top, (float) frame.left, (float) (frame.bottom + 1), this.paint);
            canvas.drawRect((float) (frame.right + 1), (float) frame.top, (float) width, (float) (frame.bottom + 1), this.paint);
            canvas.drawRect(0.0f, (float) (frame.bottom + 1), (float) width, (float) height, this.paint);
            drawFrameBounds(canvas, frame);
            dradCenterLine(canvas, frame);
            if (this.resultBitmap != null) {
                this.paint.setAlpha(160);
                canvas.drawBitmap(this.resultBitmap, (Rect) null, frame, this.paint);
                return;
            }
            this.paint.setColor(this.laserColor);
            this.paint.setAlpha(SCANNER_ALPHA[this.scannerAlpha]);
            this.scannerAlpha = (this.scannerAlpha + 1) % SCANNER_ALPHA.length;
            int height2 = (frame.height() / 2) + frame.top;
            float scaleX = ((float) frame.width()) / ((float) previewFrame.width());
            float scaleY = ((float) frame.height()) / ((float) previewFrame.height());
            int frameLeft = frame.left;
            int frameTop = frame.top;
            if (!this.lastPossibleResultPoints.isEmpty()) {
                this.paint.setAlpha(80);
                this.paint.setColor(this.resultPointColor);
                for (ResultPoint point : this.lastPossibleResultPoints) {
                    Canvas canvas2 = canvas;
                    canvas2.drawCircle((float) (((int) (point.getX() * scaleX)) + frameLeft), (float) (((int) (point.getY() * scaleY)) + frameTop), 3.0f, this.paint);
                }
                this.lastPossibleResultPoints.clear();
            }
            if (!this.possibleResultPoints.isEmpty()) {
                this.paint.setAlpha(160);
                this.paint.setColor(this.resultPointColor);
                for (ResultPoint point2 : this.possibleResultPoints) {
                    Canvas canvas3 = canvas;
                    canvas3.drawCircle((float) (((int) (point2.getX() * scaleX)) + frameLeft), (float) (((int) (point2.getY() * scaleY)) + frameTop), 6.0f, this.paint);
                }
                List<ResultPoint> temp = this.possibleResultPoints;
                this.possibleResultPoints = this.lastPossibleResultPoints;
                this.lastPossibleResultPoints = temp;
                this.possibleResultPoints.clear();
            }
            postInvalidateDelayed(ANIMATION_DELAY, frame.left - 6, frame.top - 6, frame.right + 6, frame.bottom + 6);
        }
    }

    private void dradCenterLine(Canvas canvas, Rect frame) {
        if (this.scan_lineBitmap == null) {
            this.scan_lineBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scan_qr_center_line);
            this.scan_lineBitmap = zoomImg(this.scan_lineBitmap, frame.right - frame.left);
        }
        if (!this.isFirst) {
            this.isFirst = true;
            this.slideTop = frame.top;
            this.slideBottom = frame.bottom;
        }
        this.slideTop = (int) (((float) this.slideTop) + this.speed_index);
        if (this.slideTop >= frame.bottom - this.scan_lineBitmap.getHeight()) {
            this.slideTop = frame.top;
            this.speed_index = 20.0f;
        }
        canvas.drawBitmap(this.scan_lineBitmap, (float) frame.left, (float) this.slideTop, (Paint) null);
    }

    /* JADX DEBUG: Failed to find minimal casts for resolve overloaded methods, cast all args instead
     method: ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap}
     arg types: [android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, int]
     candidates:
      ClspMth{android.graphics.Bitmap.createBitmap(android.util.DisplayMetrics, int[], int, int, int, int, android.graphics.Bitmap$Config):android.graphics.Bitmap}
      ClspMth{android.graphics.Bitmap.createBitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean):android.graphics.Bitmap} */
    public static Bitmap zoomImg(Bitmap bm, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        Matrix matrix = new Matrix();
        matrix.postScale(((float) newWidth) / ((float) width), 1.0f);
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
    }

    private void drawFrameBounds(Canvas canvas, Rect frame) {
        this.paint.setColor(-1);
        this.paint.setStrokeWidth(2.0f);
        this.paint.setStyle(Paint.Style.STROKE);
        canvas.drawRect(frame, this.paint);
        this.paint.setColor(getResources().getColor(R.color.zxing_custom_viewfinder_laser));
        this.paint.setStyle(Paint.Style.FILL);
        canvas.drawRect((float) frame.left, (float) frame.top, (float) (frame.left + 15), (float) (frame.top + 65), this.paint);
        canvas.drawRect((float) frame.left, (float) frame.top, (float) (frame.left + 65), (float) (frame.top + 15), this.paint);
        canvas.drawRect((float) (frame.right - 15), (float) frame.top, (float) frame.right, (float) (frame.top + 65), this.paint);
        canvas.drawRect((float) (frame.right - 65), (float) frame.top, (float) frame.right, (float) (frame.top + 15), this.paint);
        canvas.drawRect((float) frame.left, (float) (frame.bottom - 65), (float) (frame.left + 15), (float) frame.bottom, this.paint);
        canvas.drawRect((float) frame.left, (float) (frame.bottom - 15), (float) (frame.left + 65), (float) frame.bottom, this.paint);
        canvas.drawRect((float) (frame.right - 15), (float) (frame.bottom - 65), (float) frame.right, (float) frame.bottom, this.paint);
        canvas.drawRect((float) (frame.right - 65), (float) (frame.bottom - 15), (float) frame.right, (float) frame.bottom, this.paint);
    }

    public void drawViewfinder() {
        Bitmap resultBitmap2 = this.resultBitmap;
        this.resultBitmap = null;
        if (resultBitmap2 != null) {
            resultBitmap2.recycle();
        }
        invalidate();
    }

    public void drawResultBitmap(Bitmap result) {
        this.resultBitmap = result;
        invalidate();
    }

    public void addPossibleResultPoint(ResultPoint point) {
        if (this.possibleResultPoints.size() < 20) {
            this.possibleResultPoints.add(point);
        }
    }
}

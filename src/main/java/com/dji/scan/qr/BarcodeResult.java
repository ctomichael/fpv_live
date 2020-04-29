package com.dji.scan.qr;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.ResultPoint;
import dji.fieldAnnotation.EXClassNullAway;
import java.util.Map;

@EXClassNullAway
public class BarcodeResult {
    private static final float PREVIEW_DOT_WIDTH = 10.0f;
    private static final float PREVIEW_LINE_WIDTH = 4.0f;
    protected Result mResult;
    private final int mScaleFactor = 2;
    protected SourceData sourceData;

    public BarcodeResult(Result result, SourceData sourceData2) {
        this.mResult = result;
        this.sourceData = sourceData2;
    }

    private static void drawLine(Canvas canvas, Paint paint, ResultPoint a, ResultPoint b, int scaleFactor) {
        if (a != null && b != null) {
            canvas.drawLine(a.getX() / ((float) scaleFactor), a.getY() / ((float) scaleFactor), b.getX() / ((float) scaleFactor), b.getY() / ((float) scaleFactor), paint);
        }
    }

    public Result getResult() {
        return this.mResult;
    }

    public Bitmap getBitmap() {
        return this.sourceData.getBitmap(2);
    }

    public Bitmap getBitmapWithResultPoints(int color) {
        Bitmap bitmap = getBitmap();
        Bitmap barcode = bitmap;
        ResultPoint[] points = this.mResult.getResultPoints();
        if (!(points == null || points.length <= 0 || bitmap == null)) {
            barcode = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(barcode);
            canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
            Paint paint = new Paint();
            paint.setColor(color);
            if (points.length == 2) {
                paint.setStrokeWidth(4.0f);
                drawLine(canvas, paint, points[0], points[1], 2);
            } else if (points.length == 4 && (this.mResult.getBarcodeFormat() == BarcodeFormat.UPC_A || this.mResult.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
                drawLine(canvas, paint, points[0], points[1], 2);
                drawLine(canvas, paint, points[2], points[3], 2);
            } else {
                paint.setStrokeWidth(PREVIEW_DOT_WIDTH);
                for (ResultPoint point : points) {
                    if (point != null) {
                        canvas.drawPoint(point.getX() / 2.0f, point.getY() / 2.0f, paint);
                    }
                }
            }
        }
        return barcode;
    }

    public int getBitmapScaleFactor() {
        return 2;
    }

    public String getText() {
        return this.mResult.getText();
    }

    public byte[] getRawBytes() {
        return this.mResult.getRawBytes();
    }

    public ResultPoint[] getResultPoints() {
        return this.mResult.getResultPoints();
    }

    public BarcodeFormat getBarcodeFormat() {
        return this.mResult.getBarcodeFormat();
    }

    public Map<ResultMetadataType, Object> getResultMetadata() {
        return this.mResult.getResultMetadata();
    }

    public long getTimestamp() {
        return this.mResult.getTimestamp();
    }

    public String toString() {
        return this.mResult.getText();
    }
}

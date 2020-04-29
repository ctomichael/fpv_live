package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import lecho.lib.hellocharts.formatter.PieChartValueFormatter;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.PieChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

public class PieChartRenderer extends AbstractChartRenderer {
    private static final float DEFAULT_LABEL_INSIDE_RADIUS_FACTOR = 0.7f;
    private static final float DEFAULT_LABEL_OUTSIDE_RADIUS_FACTOR = 1.0f;
    private static final int DEFAULT_START_ROTATION = 45;
    private static final int DEFAULT_TOUCH_ADDITIONAL_DP = 8;
    private static final float MAX_WIDTH_HEIGHT = 100.0f;
    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 1;
    private Paint centerCirclePaint = new Paint();
    private float centerCircleScale;
    private Paint.FontMetricsInt centerCircleText1FontMetrics = new Paint.FontMetricsInt();
    private Paint centerCircleText1Paint = new Paint();
    private Paint.FontMetricsInt centerCircleText2FontMetrics = new Paint.FontMetricsInt();
    private Paint centerCircleText2Paint = new Paint();
    private float circleFillRatio = 1.0f;
    private PieChartDataProvider dataProvider;
    private RectF drawCircleOval = new RectF();
    private boolean hasCenterCircle;
    private boolean hasLabels;
    private boolean hasLabelsOnlyForSelected;
    private boolean hasLabelsOutside;
    private float maxSum;
    private RectF originCircleOval = new RectF();
    private int rotation = 45;
    private Paint separationLinesPaint = new Paint();
    private Paint slicePaint = new Paint();
    private PointF sliceVector = new PointF();
    private Bitmap softwareBitmap;
    private Canvas softwareCanvas = new Canvas();
    private Viewport tempMaximumViewport = new Viewport();
    private int touchAdditional;
    private PieChartValueFormatter valueFormatter;

    public PieChartRenderer(Context context, Chart chart, PieChartDataProvider dataProvider2) {
        super(context, chart);
        this.dataProvider = dataProvider2;
        this.touchAdditional = ChartUtils.dp2px(this.density, 8);
        this.slicePaint.setAntiAlias(true);
        this.slicePaint.setStyle(Paint.Style.FILL);
        this.centerCirclePaint.setAntiAlias(true);
        this.centerCirclePaint.setStyle(Paint.Style.FILL);
        this.centerCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        this.centerCircleText1Paint.setAntiAlias(true);
        this.centerCircleText1Paint.setTextAlign(Paint.Align.CENTER);
        this.centerCircleText2Paint.setAntiAlias(true);
        this.centerCircleText2Paint.setTextAlign(Paint.Align.CENTER);
        this.separationLinesPaint.setAntiAlias(true);
        this.separationLinesPaint.setStyle(Paint.Style.STROKE);
        this.separationLinesPaint.setStrokeCap(Paint.Cap.ROUND);
        this.separationLinesPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        this.separationLinesPaint.setColor(0);
    }

    public void onChartSizeChanged() {
        calculateCircleOval();
        if (this.computator.getChartWidth() > 0 && this.computator.getChartHeight() > 0) {
            this.softwareBitmap = Bitmap.createBitmap(this.computator.getChartWidth(), this.computator.getChartHeight(), Bitmap.Config.ARGB_8888);
            this.softwareCanvas.setBitmap(this.softwareBitmap);
        }
    }

    public void onChartDataChanged() {
        super.onChartDataChanged();
        PieChartData data = this.dataProvider.getPieChartData();
        this.hasLabelsOutside = data.hasLabelsOutside();
        this.hasLabels = data.hasLabels();
        this.hasLabelsOnlyForSelected = data.hasLabelsOnlyForSelected();
        this.valueFormatter = data.getFormatter();
        this.hasCenterCircle = data.hasCenterCircle();
        this.centerCircleScale = data.getCenterCircleScale();
        this.centerCirclePaint.setColor(data.getCenterCircleColor());
        if (data.getCenterText1Typeface() != null) {
            this.centerCircleText1Paint.setTypeface(data.getCenterText1Typeface());
        }
        this.centerCircleText1Paint.setTextSize((float) ChartUtils.sp2px(this.scaledDensity, data.getCenterText1FontSize()));
        this.centerCircleText1Paint.setColor(data.getCenterText1Color());
        this.centerCircleText1Paint.getFontMetricsInt(this.centerCircleText1FontMetrics);
        if (data.getCenterText2Typeface() != null) {
            this.centerCircleText2Paint.setTypeface(data.getCenterText2Typeface());
        }
        this.centerCircleText2Paint.setTextSize((float) ChartUtils.sp2px(this.scaledDensity, data.getCenterText2FontSize()));
        this.centerCircleText2Paint.setColor(data.getCenterText2Color());
        this.centerCircleText2Paint.getFontMetricsInt(this.centerCircleText2FontMetrics);
        onChartViewportChanged();
    }

    public void onChartViewportChanged() {
        if (this.isViewportCalculationEnabled) {
            calculateMaxViewport();
            this.computator.setMaxViewport(this.tempMaximumViewport);
            this.computator.setCurrentViewport(this.computator.getMaximumViewport());
        }
    }

    public void draw(Canvas canvas) {
        Canvas drawCanvas;
        if (this.softwareBitmap != null) {
            drawCanvas = this.softwareCanvas;
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        } else {
            drawCanvas = canvas;
        }
        drawSlices(drawCanvas);
        drawSeparationLines(drawCanvas);
        if (this.hasCenterCircle) {
            drawCenterCircle(drawCanvas);
        }
        drawLabels(drawCanvas);
        if (this.softwareBitmap != null) {
            canvas.drawBitmap(this.softwareBitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    public void drawUnclipped(Canvas canvas) {
    }

    public boolean checkTouch(float touchX, float touchY) {
        this.selectedValue.clear();
        PieChartData data = this.dataProvider.getPieChartData();
        float centerX = this.originCircleOval.centerX();
        float centerY = this.originCircleOval.centerY();
        float circleRadius = this.originCircleOval.width() / 2.0f;
        this.sliceVector.set(touchX - centerX, touchY - centerY);
        if (this.sliceVector.length() > ((float) this.touchAdditional) + circleRadius) {
            return false;
        }
        if (data.hasCenterCircle() && this.sliceVector.length() < data.getCenterCircleScale() * circleRadius) {
            return false;
        }
        float touchAngle = ((pointToAngle(touchX, touchY, centerX, centerY) - ((float) this.rotation)) + 360.0f) % 360.0f;
        float sliceScale = 360.0f / this.maxSum;
        float lastAngle = 0.0f;
        int sliceIndex = 0;
        for (SliceValue sliceValue : data.getValues()) {
            float angle = Math.abs(sliceValue.getValue()) * sliceScale;
            if (touchAngle >= lastAngle) {
                this.selectedValue.set(sliceIndex, sliceIndex, SelectedValue.SelectedValueType.NONE);
            }
            lastAngle += angle;
            sliceIndex++;
        }
        return isTouched();
    }

    private void drawCenterCircle(Canvas canvas) {
        PieChartData data = this.dataProvider.getPieChartData();
        float centerRadius = (this.originCircleOval.width() / 2.0f) * data.getCenterCircleScale();
        float centerX = this.originCircleOval.centerX();
        float centerY = this.originCircleOval.centerY();
        canvas.drawCircle(centerX, centerY, centerRadius, this.centerCirclePaint);
        if (!TextUtils.isEmpty(data.getCenterText1())) {
            int text1Height = Math.abs(this.centerCircleText1FontMetrics.ascent);
            if (!TextUtils.isEmpty(data.getCenterText2())) {
                int text2Height = Math.abs(this.centerCircleText2FontMetrics.ascent);
                canvas.drawText(data.getCenterText1(), centerX, centerY - (((float) text1Height) * 0.2f), this.centerCircleText1Paint);
                canvas.drawText(data.getCenterText2(), centerX, ((float) text2Height) + centerY, this.centerCircleText2Paint);
                return;
            }
            canvas.drawText(data.getCenterText1(), centerX, ((float) (text1Height / 4)) + centerY, this.centerCircleText1Paint);
        }
    }

    private void drawSlices(Canvas canvas) {
        PieChartData data = this.dataProvider.getPieChartData();
        float sliceScale = 360.0f / this.maxSum;
        float lastAngle = (float) this.rotation;
        int sliceIndex = 0;
        for (SliceValue sliceValue : data.getValues()) {
            float angle = Math.abs(sliceValue.getValue()) * sliceScale;
            if (!isTouched() || this.selectedValue.getFirstIndex() != sliceIndex) {
                drawSlice(canvas, sliceValue, lastAngle, angle, 0);
            } else {
                drawSlice(canvas, sliceValue, lastAngle, angle, 1);
            }
            lastAngle += angle;
            sliceIndex++;
        }
    }

    private void drawSeparationLines(Canvas canvas) {
        int sliceSpacing;
        PieChartData data = this.dataProvider.getPieChartData();
        if (data.getValues().size() >= 2 && (sliceSpacing = ChartUtils.dp2px(this.density, data.getSlicesSpacing())) >= 1) {
            float sliceScale = 360.0f / this.maxSum;
            float lastAngle = (float) this.rotation;
            float circleRadius = this.originCircleOval.width() / 2.0f;
            this.separationLinesPaint.setStrokeWidth((float) sliceSpacing);
            for (SliceValue sliceValue : data.getValues()) {
                float angle = Math.abs(sliceValue.getValue()) * sliceScale;
                this.sliceVector.set((float) Math.cos(Math.toRadians((double) lastAngle)), (float) Math.sin(Math.toRadians((double) lastAngle)));
                normalizeVector(this.sliceVector);
                canvas.drawLine(this.originCircleOval.centerX(), this.originCircleOval.centerY(), (this.sliceVector.x * (((float) this.touchAdditional) + circleRadius)) + this.originCircleOval.centerX(), (this.sliceVector.y * (((float) this.touchAdditional) + circleRadius)) + this.originCircleOval.centerY(), this.separationLinesPaint);
                lastAngle += angle;
            }
        }
    }

    public void drawLabels(Canvas canvas) {
        PieChartData data = this.dataProvider.getPieChartData();
        float sliceScale = 360.0f / this.maxSum;
        float lastAngle = (float) this.rotation;
        int sliceIndex = 0;
        for (SliceValue sliceValue : data.getValues()) {
            float angle = Math.abs(sliceValue.getValue()) * sliceScale;
            if (isTouched()) {
                if (this.hasLabels) {
                    drawLabel(canvas, sliceValue, lastAngle, angle);
                } else if (this.hasLabelsOnlyForSelected && this.selectedValue.getFirstIndex() == sliceIndex) {
                    drawLabel(canvas, sliceValue, lastAngle, angle);
                }
            } else if (this.hasLabels) {
                drawLabel(canvas, sliceValue, lastAngle, angle);
            }
            lastAngle += angle;
            sliceIndex++;
        }
    }

    private void drawSlice(Canvas canvas, SliceValue sliceValue, float lastAngle, float angle, int mode) {
        this.sliceVector.set((float) Math.cos(Math.toRadians((double) ((angle / 2.0f) + lastAngle))), (float) Math.sin(Math.toRadians((double) ((angle / 2.0f) + lastAngle))));
        normalizeVector(this.sliceVector);
        this.drawCircleOval.set(this.originCircleOval);
        if (1 == mode) {
            this.drawCircleOval.inset((float) (-this.touchAdditional), (float) (-this.touchAdditional));
            this.slicePaint.setColor(sliceValue.getDarkenColor());
            canvas.drawArc(this.drawCircleOval, lastAngle, angle, true, this.slicePaint);
            return;
        }
        this.slicePaint.setColor(sliceValue.getColor());
        canvas.drawArc(this.drawCircleOval, lastAngle, angle, true, this.slicePaint);
    }

    private void drawLabel(Canvas canvas, SliceValue sliceValue, float lastAngle, float angle) {
        float labelRadius;
        float left;
        float right;
        float top;
        float bottom;
        this.sliceVector.set((float) Math.cos(Math.toRadians((double) ((angle / 2.0f) + lastAngle))), (float) Math.sin(Math.toRadians((double) ((angle / 2.0f) + lastAngle))));
        normalizeVector(this.sliceVector);
        int numChars = this.valueFormatter.formatChartValue(this.labelBuffer, sliceValue);
        if (numChars != 0) {
            float labelWidth = this.labelPaint.measureText(this.labelBuffer, this.labelBuffer.length - numChars, numChars);
            int labelHeight = Math.abs(this.fontMetrics.ascent);
            float centerX = this.originCircleOval.centerX();
            float centerY = this.originCircleOval.centerY();
            float circleRadius = this.originCircleOval.width() / 2.0f;
            if (this.hasLabelsOutside) {
                labelRadius = circleRadius * 1.0f;
            } else if (this.hasCenterCircle) {
                labelRadius = circleRadius - ((circleRadius - (this.centerCircleScale * circleRadius)) / 2.0f);
            } else {
                labelRadius = circleRadius * DEFAULT_LABEL_INSIDE_RADIUS_FACTOR;
            }
            float rawX = (this.sliceVector.x * labelRadius) + centerX;
            float rawY = (this.sliceVector.y * labelRadius) + centerY;
            if (this.hasLabelsOutside) {
                if (rawX > centerX) {
                    left = rawX + ((float) this.labelMargin);
                    right = rawX + labelWidth + ((float) (this.labelMargin * 3));
                } else {
                    left = (rawX - labelWidth) - ((float) (this.labelMargin * 3));
                    right = rawX - ((float) this.labelMargin);
                }
                if (rawY > centerY) {
                    top = rawY + ((float) this.labelMargin);
                    bottom = ((float) labelHeight) + rawY + ((float) (this.labelMargin * 3));
                } else {
                    top = (rawY - ((float) labelHeight)) - ((float) (this.labelMargin * 3));
                    bottom = rawY - ((float) this.labelMargin);
                }
            } else {
                left = (rawX - (labelWidth / 2.0f)) - ((float) this.labelMargin);
                right = (labelWidth / 2.0f) + rawX + ((float) this.labelMargin);
                top = (rawY - ((float) (labelHeight / 2))) - ((float) this.labelMargin);
                bottom = ((float) (labelHeight / 2)) + rawY + ((float) this.labelMargin);
            }
            this.labelBackgroundRect.set(left, top, right, bottom);
            drawLabelTextAndBackground(canvas, this.labelBuffer, this.labelBuffer.length - numChars, numChars, sliceValue.getDarkenColor());
        }
    }

    private void normalizeVector(PointF point) {
        float abs = point.length();
        point.set(point.x / abs, point.y / abs);
    }

    private float pointToAngle(float x, float y, float centerX, float centerY) {
        return ((((float) Math.toDegrees(Math.atan2(-((double) (x - centerX)), (double) (y - centerY)))) + 360.0f) % 360.0f) + 90.0f;
    }

    private void calculateCircleOval() {
        Rect contentRect = this.computator.getContentRectMinusAllMargins();
        float circleRadius = Math.min(((float) contentRect.width()) / 2.0f, ((float) contentRect.height()) / 2.0f);
        float centerX = (float) contentRect.centerX();
        float centerY = (float) contentRect.centerY();
        this.originCircleOval.set((centerX - circleRadius) + ((float) this.touchAdditional), (centerY - circleRadius) + ((float) this.touchAdditional), (centerX + circleRadius) - ((float) this.touchAdditional), (centerY + circleRadius) - ((float) this.touchAdditional));
        float inest = 0.5f * this.originCircleOval.width() * (1.0f - this.circleFillRatio);
        this.originCircleOval.inset(inest, inest);
    }

    private void calculateMaxViewport() {
        this.tempMaximumViewport.set(0.0f, 100.0f, 100.0f, 0.0f);
        this.maxSum = 0.0f;
        for (SliceValue sliceValue : this.dataProvider.getPieChartData().getValues()) {
            this.maxSum += Math.abs(sliceValue.getValue());
        }
    }

    public RectF getCircleOval() {
        return this.originCircleOval;
    }

    public void setCircleOval(RectF orginCircleOval) {
        this.originCircleOval = orginCircleOval;
    }

    public int getChartRotation() {
        return this.rotation;
    }

    public void setChartRotation(int rotation2) {
        this.rotation = ((rotation2 % 360) + 360) % 360;
    }

    public SliceValue getValueForAngle(int angle, SelectedValue selectedValue) {
        PieChartData data = this.dataProvider.getPieChartData();
        float touchAngle = (((float) (angle - this.rotation)) + 360.0f) % 360.0f;
        float sliceScale = 360.0f / this.maxSum;
        float lastAngle = 0.0f;
        int sliceIndex = 0;
        for (SliceValue sliceValue : data.getValues()) {
            float tempAngle = Math.abs(sliceValue.getValue()) * sliceScale;
            if (touchAngle < lastAngle) {
                lastAngle += tempAngle;
                sliceIndex++;
            } else if (selectedValue == null) {
                return sliceValue;
            } else {
                selectedValue.set(sliceIndex, sliceIndex, SelectedValue.SelectedValueType.NONE);
                return sliceValue;
            }
        }
        return null;
    }

    public float getCircleFillRatio() {
        return this.circleFillRatio;
    }

    public void setCircleFillRatio(float fillRatio) {
        if (fillRatio < 0.0f) {
            fillRatio = 0.0f;
        } else if (fillRatio > 1.0f) {
            fillRatio = 1.0f;
        }
        this.circleFillRatio = fillRatio;
        calculateCircleOval();
    }
}

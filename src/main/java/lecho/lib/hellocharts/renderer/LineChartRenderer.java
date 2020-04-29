package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.LineChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

public class LineChartRenderer extends AbstractChartRenderer {
    private static final int DEFAULT_LINE_STROKE_WIDTH_DP = 3;
    private static final int DEFAULT_TOUCH_TOLERANCE_MARGIN_DP = 4;
    private static final float LINE_SMOOTHNESS = 0.16f;
    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 1;
    private float baseValue;
    private int checkPrecision;
    private LineChartDataProvider dataProvider;
    private Paint linePaint = new Paint();
    private Path path = new Path();
    private Paint pointPaint = new Paint();
    private Bitmap softwareBitmap;
    private Canvas softwareCanvas = new Canvas();
    private Viewport tempMaximumViewport = new Viewport();
    private int touchToleranceMargin;

    public LineChartRenderer(Context context, Chart chart, LineChartDataProvider dataProvider2) {
        super(context, chart);
        this.dataProvider = dataProvider2;
        this.touchToleranceMargin = ChartUtils.dp2px(this.density, 4);
        this.linePaint.setAntiAlias(true);
        this.linePaint.setStyle(Paint.Style.STROKE);
        this.linePaint.setStrokeCap(Paint.Cap.ROUND);
        this.linePaint.setStrokeWidth((float) ChartUtils.dp2px(this.density, 3));
        this.pointPaint.setAntiAlias(true);
        this.pointPaint.setStyle(Paint.Style.FILL);
        this.checkPrecision = ChartUtils.dp2px(this.density, 2);
    }

    public void setShader(Shader shader) {
        this.linePaint.setShader(shader);
    }

    public void onChartSizeChanged() {
        int internalMargin = calculateContentRectInternalMargin();
        this.computator.insetContentRectByInternalMargins(internalMargin, internalMargin, internalMargin, internalMargin);
        if (this.computator.getChartWidth() > 0 && this.computator.getChartHeight() > 0) {
            this.softwareBitmap = Bitmap.createBitmap(this.computator.getChartWidth(), this.computator.getChartHeight(), Bitmap.Config.ARGB_8888);
            this.softwareCanvas.setBitmap(this.softwareBitmap);
        }
    }

    public void onChartDataChanged() {
        super.onChartDataChanged();
        int internalMargin = calculateContentRectInternalMargin();
        this.computator.insetContentRectByInternalMargins(internalMargin, internalMargin, internalMargin, internalMargin);
        this.baseValue = this.dataProvider.getLineChartData().getBaseValue();
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
        LineChartData data = this.dataProvider.getLineChartData();
        if (this.softwareBitmap != null) {
            drawCanvas = this.softwareCanvas;
            drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        } else {
            drawCanvas = canvas;
        }
        for (Line line : data.getLines()) {
            if (line.hasLines()) {
                if (line.isCubic()) {
                    drawSmoothPath(drawCanvas, line);
                } else if (line.isSquare()) {
                    drawSquarePath(drawCanvas, line);
                } else {
                    drawPath(drawCanvas, line);
                }
            }
        }
        if (this.softwareBitmap != null) {
            canvas.drawBitmap(this.softwareBitmap, 0.0f, 0.0f, (Paint) null);
        }
    }

    public void drawUnclipped(Canvas canvas) {
        int lineIndex = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            if (checkIfShouldDrawPoints(line)) {
                drawPoints(canvas, line, lineIndex, 0);
            }
            lineIndex++;
        }
        if (isTouched()) {
            highlightPoints(canvas);
        }
    }

    private boolean checkIfShouldDrawPoints(Line line) {
        return line.hasPoints() || line.getValues().size() == 1;
    }

    public boolean checkTouch(float touchX, float touchY) {
        this.selectedValue.clear();
        int lineIndex = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            if (checkIfShouldDrawPoints(line)) {
                int pointRadius = ChartUtils.dp2px(this.density, line.getPointRadius());
                int valueIndex = 0;
                for (PointValue pointValue : line.getValues()) {
                    if (isInArea(this.computator.computeRawX(pointValue.getX()), this.computator.computeRawY(pointValue.getY()), touchX, touchY, (float) (this.touchToleranceMargin + pointRadius))) {
                        this.selectedValue.set(lineIndex, valueIndex, SelectedValue.SelectedValueType.LINE);
                    }
                    valueIndex++;
                }
            }
            lineIndex++;
        }
        return isTouched();
    }

    private void calculateMaxViewport() {
        this.tempMaximumViewport.set(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE);
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            for (PointValue pointValue : line.getValues()) {
                if (pointValue.getX() < this.tempMaximumViewport.left) {
                    this.tempMaximumViewport.left = pointValue.getX();
                }
                if (pointValue.getX() > this.tempMaximumViewport.right) {
                    this.tempMaximumViewport.right = pointValue.getX();
                }
                if (pointValue.getY() < this.tempMaximumViewport.bottom) {
                    this.tempMaximumViewport.bottom = pointValue.getY();
                }
                if (pointValue.getY() > this.tempMaximumViewport.top) {
                    this.tempMaximumViewport.top = pointValue.getY();
                }
            }
        }
    }

    private int calculateContentRectInternalMargin() {
        int margin;
        int contentAreaMargin = 0;
        for (Line line : this.dataProvider.getLineChartData().getLines()) {
            if (checkIfShouldDrawPoints(line) && (margin = line.getPointRadius() + 4) > contentAreaMargin) {
                contentAreaMargin = margin;
            }
        }
        return ChartUtils.dp2px(this.density, contentAreaMargin);
    }

    private void drawPath(Canvas canvas, Line line) {
        prepareLinePaint(line);
        int valueIndex = 0;
        for (PointValue pointValue : line.getValues()) {
            float rawX = this.computator.computeRawX(pointValue.getX());
            float rawY = this.computator.computeRawY(pointValue.getY());
            if (valueIndex == 0) {
                this.path.moveTo(rawX, rawY);
            } else {
                this.path.lineTo(rawX, rawY);
            }
            valueIndex++;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void drawSquarePath(Canvas canvas, Line line) {
        prepareLinePaint(line);
        int valueIndex = 0;
        float previousRawY = 0.0f;
        for (PointValue pointValue : line.getValues()) {
            float rawX = this.computator.computeRawX(pointValue.getX());
            float rawY = this.computator.computeRawY(pointValue.getY());
            if (valueIndex == 0) {
                this.path.moveTo(rawX, rawY);
            } else {
                this.path.lineTo(rawX, previousRawY);
                this.path.lineTo(rawX, rawY);
            }
            previousRawY = rawY;
            valueIndex++;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void drawSmoothPath(Canvas canvas, Line line) {
        float nextPointX;
        float nextPointY;
        prepareLinePaint(line);
        int lineSize = line.getValues().size();
        float prePreviousPointX = Float.NaN;
        float prePreviousPointY = Float.NaN;
        float previousPointX = Float.NaN;
        float previousPointY = Float.NaN;
        float currentPointX = Float.NaN;
        float currentPointY = Float.NaN;
        for (int valueIndex = 0; valueIndex < lineSize; valueIndex++) {
            if (Float.isNaN(currentPointX)) {
                PointValue linePoint = line.getValues().get(valueIndex);
                currentPointX = this.computator.computeRawX(linePoint.getX());
                currentPointY = this.computator.computeRawY(linePoint.getY());
            }
            if (Float.isNaN(previousPointX)) {
                if (valueIndex > 0) {
                    PointValue linePoint2 = line.getValues().get(valueIndex - 1);
                    previousPointX = this.computator.computeRawX(linePoint2.getX());
                    previousPointY = this.computator.computeRawY(linePoint2.getY());
                } else {
                    previousPointX = currentPointX;
                    previousPointY = currentPointY;
                }
            }
            if (Float.isNaN(prePreviousPointX)) {
                if (valueIndex > 1) {
                    PointValue linePoint3 = line.getValues().get(valueIndex - 2);
                    prePreviousPointX = this.computator.computeRawX(linePoint3.getX());
                    prePreviousPointY = this.computator.computeRawY(linePoint3.getY());
                } else {
                    prePreviousPointX = previousPointX;
                    prePreviousPointY = previousPointY;
                }
            }
            if (valueIndex < lineSize - 1) {
                PointValue linePoint4 = line.getValues().get(valueIndex + 1);
                nextPointX = this.computator.computeRawX(linePoint4.getX());
                nextPointY = this.computator.computeRawY(linePoint4.getY());
            } else {
                nextPointX = currentPointX;
                nextPointY = currentPointY;
            }
            if (valueIndex == 0) {
                this.path.moveTo(currentPointX, currentPointY);
            } else {
                this.path.cubicTo(previousPointX + (LINE_SMOOTHNESS * (currentPointX - prePreviousPointX)), previousPointY + (LINE_SMOOTHNESS * (currentPointY - prePreviousPointY)), currentPointX - (LINE_SMOOTHNESS * (nextPointX - previousPointX)), currentPointY - (LINE_SMOOTHNESS * (nextPointY - previousPointY)), currentPointX, currentPointY);
            }
            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
        canvas.drawPath(this.path, this.linePaint);
        if (line.isFilled()) {
            drawArea(canvas, line);
        }
        this.path.reset();
    }

    private void prepareLinePaint(Line line) {
        this.linePaint.setStrokeWidth((float) ChartUtils.dp2px(this.density, line.getStrokeWidth()));
        this.linePaint.setColor(line.getColor());
        this.linePaint.setPathEffect(line.getPathEffect());
    }

    private void drawPoints(Canvas canvas, Line line, int lineIndex, int mode) {
        this.pointPaint.setColor(line.getPointColor());
        int valueIndex = 0;
        for (PointValue pointValue : line.getValues()) {
            int pointRadius = ChartUtils.dp2px(this.density, line.getPointRadius());
            float rawX = this.computator.computeRawX(pointValue.getX());
            float rawY = this.computator.computeRawY(pointValue.getY());
            if (this.computator.isWithinContentRect(rawX, rawY, (float) this.checkPrecision)) {
                if (mode == 0) {
                    drawPoint(canvas, line, pointValue, rawX, rawY, (float) pointRadius);
                    if (line.hasLabels()) {
                        drawLabel(canvas, line, pointValue, rawX, rawY, (float) (this.labelOffset + pointRadius));
                    }
                } else if (1 == mode) {
                    highlightPoint(canvas, line, pointValue, rawX, rawY, lineIndex, valueIndex);
                } else {
                    throw new IllegalStateException("Cannot process points in mode: " + mode);
                }
            }
            valueIndex++;
        }
    }

    private void drawPoint(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, float pointRadius) {
        if (ValueShape.SQUARE.equals(line.getShape())) {
            canvas.drawRect(rawX - pointRadius, rawY - pointRadius, rawX + pointRadius, rawY + pointRadius, this.pointPaint);
        } else if (ValueShape.CIRCLE.equals(line.getShape())) {
            canvas.drawCircle(rawX, rawY, pointRadius, this.pointPaint);
        } else if (ValueShape.DIAMOND.equals(line.getShape())) {
            canvas.save();
            canvas.rotate(45.0f, rawX, rawY);
            canvas.drawRect(rawX - pointRadius, rawY - pointRadius, rawX + pointRadius, rawY + pointRadius, this.pointPaint);
            canvas.restore();
        } else {
            throw new IllegalArgumentException("Invalid point shape: " + line.getShape());
        }
    }

    private void highlightPoints(Canvas canvas) {
        int lineIndex = this.selectedValue.getFirstIndex();
        drawPoints(canvas, this.dataProvider.getLineChartData().getLines().get(lineIndex), lineIndex, 1);
    }

    private void highlightPoint(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, int lineIndex, int valueIndex) {
        if (this.selectedValue.getFirstIndex() == lineIndex && this.selectedValue.getSecondIndex() == valueIndex) {
            int pointRadius = ChartUtils.dp2px(this.density, line.getPointRadius());
            this.pointPaint.setColor(line.getDarkenColor());
            drawPoint(canvas, line, pointValue, rawX, rawY, (float) (this.touchToleranceMargin + pointRadius));
            if (line.hasLabels() || line.hasLabelsOnlyForSelected()) {
                drawLabel(canvas, line, pointValue, rawX, rawY, (float) (this.labelOffset + pointRadius));
            }
        }
    }

    private void drawLabel(Canvas canvas, Line line, PointValue pointValue, float rawX, float rawY, float offset) {
        float top;
        float bottom;
        Rect contentRect = this.computator.getContentRectMinusAllMargins();
        int numChars = line.getFormatter().formatChartValue(this.labelBuffer, pointValue);
        if (numChars != 0) {
            float labelWidth = this.labelPaint.measureText(this.labelBuffer, this.labelBuffer.length - numChars, numChars);
            int labelHeight = Math.abs(this.fontMetrics.ascent);
            float left = (rawX - (labelWidth / 2.0f)) - ((float) this.labelMargin);
            float right = (labelWidth / 2.0f) + rawX + ((float) this.labelMargin);
            if (pointValue.getY() >= this.baseValue) {
                top = ((rawY - offset) - ((float) labelHeight)) - ((float) (this.labelMargin * 2));
                bottom = rawY - offset;
            } else {
                top = rawY + offset;
                bottom = rawY + offset + ((float) labelHeight) + ((float) (this.labelMargin * 2));
            }
            if (top < ((float) contentRect.top)) {
                top = rawY + offset;
                bottom = rawY + offset + ((float) labelHeight) + ((float) (this.labelMargin * 2));
            }
            if (bottom > ((float) contentRect.bottom)) {
                top = ((rawY - offset) - ((float) labelHeight)) - ((float) (this.labelMargin * 2));
                bottom = rawY - offset;
            }
            if (left < ((float) contentRect.left)) {
                left = rawX;
                right = rawX + labelWidth + ((float) (this.labelMargin * 2));
            }
            if (right > ((float) contentRect.right)) {
                left = (rawX - labelWidth) - ((float) (this.labelMargin * 2));
                right = rawX;
            }
            this.labelBackgroundRect.set(left, top, right, bottom);
            drawLabelTextAndBackground(canvas, this.labelBuffer, this.labelBuffer.length - numChars, numChars, line.getDarkenColor());
        }
    }

    private void drawArea(Canvas canvas, Line line) {
        int lineSize = line.getValues().size();
        if (lineSize >= 2) {
            Rect contentRect = this.computator.getContentRectMinusAllMargins();
            float baseRawValue = Math.min((float) contentRect.bottom, Math.max(this.computator.computeRawY(this.baseValue), (float) contentRect.top));
            float left = Math.max(this.computator.computeRawX(line.getValues().get(0).getX()), (float) contentRect.left);
            this.path.lineTo(Math.min(this.computator.computeRawX(line.getValues().get(lineSize - 1).getX()), (float) contentRect.right), baseRawValue);
            this.path.lineTo(left, baseRawValue);
            this.path.close();
            this.linePaint.setStyle(Paint.Style.FILL);
            this.linePaint.setAlpha(line.getAreaTransparency());
            canvas.drawPath(this.path, this.linePaint);
            this.linePaint.setStyle(Paint.Style.STROKE);
        }
    }

    private boolean isInArea(float x, float y, float touchX, float touchY, float radius) {
        return Math.pow((double) (touchX - x), 2.0d) + Math.pow((double) (touchY - y), 2.0d) <= Math.pow((double) radius, 2.0d) * 2.0d;
    }
}

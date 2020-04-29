package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import lecho.lib.hellocharts.formatter.BubbleChartValueFormatter;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.provider.BubbleChartDataProvider;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;

public class BubbleChartRenderer extends AbstractChartRenderer {
    private static final int DEFAULT_TOUCH_ADDITIONAL_DP = 4;
    private static final int MODE_DRAW = 0;
    private static final int MODE_HIGHLIGHT = 1;
    private PointF bubbleCenter = new PointF();
    private Paint bubblePaint = new Paint();
    private RectF bubbleRect = new RectF();
    private float bubbleScaleX;
    private float bubbleScaleY;
    private BubbleChartDataProvider dataProvider;
    private boolean hasLabels;
    private boolean hasLabelsOnlyForSelected;
    private boolean isBubbleScaledByX = true;
    private float maxRadius;
    private float minRawRadius;
    private Viewport tempMaximumViewport = new Viewport();
    private int touchAdditional;
    private BubbleChartValueFormatter valueFormatter;

    public BubbleChartRenderer(Context context, Chart chart, BubbleChartDataProvider dataProvider2) {
        super(context, chart);
        this.dataProvider = dataProvider2;
        this.touchAdditional = ChartUtils.dp2px(this.density, 4);
        this.bubblePaint.setAntiAlias(true);
        this.bubblePaint.setStyle(Paint.Style.FILL);
    }

    public void onChartSizeChanged() {
        Rect contentRect = this.chart.getChartComputator().getContentRectMinusAllMargins();
        if (contentRect.width() < contentRect.height()) {
            this.isBubbleScaledByX = true;
        } else {
            this.isBubbleScaledByX = false;
        }
    }

    public void onChartDataChanged() {
        super.onChartDataChanged();
        BubbleChartData data = this.dataProvider.getBubbleChartData();
        this.hasLabels = data.hasLabels();
        this.hasLabelsOnlyForSelected = data.hasLabelsOnlyForSelected();
        this.valueFormatter = data.getFormatter();
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
        drawBubbles(canvas);
        if (isTouched()) {
            highlightBubbles(canvas);
        }
    }

    public void drawUnclipped(Canvas canvas) {
    }

    public boolean checkTouch(float touchX, float touchY) {
        this.selectedValue.clear();
        int valueIndex = 0;
        for (BubbleValue bubbleValue : this.dataProvider.getBubbleChartData().getValues()) {
            float rawRadius = processBubble(bubbleValue, this.bubbleCenter);
            if (ValueShape.SQUARE.equals(bubbleValue.getShape())) {
                if (this.bubbleRect.contains(touchX, touchY)) {
                    this.selectedValue.set(valueIndex, valueIndex, SelectedValue.SelectedValueType.NONE);
                }
            } else if (ValueShape.CIRCLE.equals(bubbleValue.getShape())) {
                float diffX = touchX - this.bubbleCenter.x;
                float diffY = touchY - this.bubbleCenter.y;
                if (((float) Math.sqrt((double) ((diffX * diffX) + (diffY * diffY)))) <= rawRadius) {
                    this.selectedValue.set(valueIndex, valueIndex, SelectedValue.SelectedValueType.NONE);
                }
            } else {
                throw new IllegalArgumentException("Invalid bubble shape: " + bubbleValue.getShape());
            }
            valueIndex++;
        }
        return isTouched();
    }

    public void removeMargins() {
        Rect contentRect = this.computator.getContentRectMinusAllMargins();
        if (contentRect.height() != 0 && contentRect.width() != 0) {
            float pxX = this.computator.computeRawDistanceX(this.maxRadius * this.bubbleScaleX);
            float pxY = this.computator.computeRawDistanceY(this.maxRadius * this.bubbleScaleY);
            float scaleX = this.computator.getMaximumViewport().width() / ((float) contentRect.width());
            float scaleY = this.computator.getMaximumViewport().height() / ((float) contentRect.height());
            float dx = 0.0f;
            float dy = 0.0f;
            if (this.isBubbleScaledByX) {
                dy = (pxY - pxX) * scaleY * 0.75f;
            } else {
                dx = (pxX - pxY) * scaleX * 0.75f;
            }
            Viewport maxViewport = this.computator.getMaximumViewport();
            maxViewport.inset(dx, dy);
            Viewport currentViewport = this.computator.getCurrentViewport();
            currentViewport.inset(dx, dy);
            this.computator.setMaxViewport(maxViewport);
            this.computator.setCurrentViewport(currentViewport);
        }
    }

    private void drawBubbles(Canvas canvas) {
        for (BubbleValue bubbleValue : this.dataProvider.getBubbleChartData().getValues()) {
            drawBubble(canvas, bubbleValue);
        }
    }

    private void drawBubble(Canvas canvas, BubbleValue bubbleValue) {
        float rawRadius = processBubble(bubbleValue, this.bubbleCenter) - ((float) this.touchAdditional);
        this.bubbleRect.inset((float) this.touchAdditional, (float) this.touchAdditional);
        this.bubblePaint.setColor(bubbleValue.getColor());
        drawBubbleShapeAndLabel(canvas, bubbleValue, rawRadius, 0);
    }

    private void drawBubbleShapeAndLabel(Canvas canvas, BubbleValue bubbleValue, float rawRadius, int mode) {
        if (ValueShape.SQUARE.equals(bubbleValue.getShape())) {
            canvas.drawRect(this.bubbleRect, this.bubblePaint);
        } else if (ValueShape.CIRCLE.equals(bubbleValue.getShape())) {
            canvas.drawCircle(this.bubbleCenter.x, this.bubbleCenter.y, rawRadius, this.bubblePaint);
        } else {
            throw new IllegalArgumentException("Invalid bubble shape: " + bubbleValue.getShape());
        }
        if (1 == mode) {
            if (this.hasLabels || this.hasLabelsOnlyForSelected) {
                drawLabel(canvas, bubbleValue, this.bubbleCenter.x, this.bubbleCenter.y);
            }
        } else if (mode != 0) {
            throw new IllegalStateException("Cannot process bubble in mode: " + mode);
        } else if (this.hasLabels) {
            drawLabel(canvas, bubbleValue, this.bubbleCenter.x, this.bubbleCenter.y);
        }
    }

    private void highlightBubbles(Canvas canvas) {
        highlightBubble(canvas, this.dataProvider.getBubbleChartData().getValues().get(this.selectedValue.getFirstIndex()));
    }

    private void highlightBubble(Canvas canvas, BubbleValue bubbleValue) {
        float rawRadius = processBubble(bubbleValue, this.bubbleCenter);
        this.bubblePaint.setColor(bubbleValue.getDarkenColor());
        drawBubbleShapeAndLabel(canvas, bubbleValue, rawRadius, 1);
    }

    private float processBubble(BubbleValue bubbleValue, PointF point) {
        float rawRadius;
        float rawX = this.computator.computeRawX(bubbleValue.getX());
        float rawY = this.computator.computeRawY(bubbleValue.getY());
        float radius = (float) Math.sqrt(((double) Math.abs(bubbleValue.getZ())) / 3.141592653589793d);
        if (this.isBubbleScaledByX) {
            rawRadius = this.computator.computeRawDistanceX(radius * this.bubbleScaleX);
        } else {
            rawRadius = this.computator.computeRawDistanceY(radius * this.bubbleScaleY);
        }
        if (rawRadius < this.minRawRadius + ((float) this.touchAdditional)) {
            rawRadius = this.minRawRadius + ((float) this.touchAdditional);
        }
        this.bubbleCenter.set(rawX, rawY);
        if (ValueShape.SQUARE.equals(bubbleValue.getShape())) {
            this.bubbleRect.set(rawX - rawRadius, rawY - rawRadius, rawX + rawRadius, rawY + rawRadius);
        }
        return rawRadius;
    }

    private void drawLabel(Canvas canvas, BubbleValue bubbleValue, float rawX, float rawY) {
        Rect contentRect = this.computator.getContentRectMinusAllMargins();
        int numChars = this.valueFormatter.formatChartValue(this.labelBuffer, bubbleValue);
        if (numChars != 0) {
            float labelWidth = this.labelPaint.measureText(this.labelBuffer, this.labelBuffer.length - numChars, numChars);
            int labelHeight = Math.abs(this.fontMetrics.ascent);
            float left = (rawX - (labelWidth / 2.0f)) - ((float) this.labelMargin);
            float right = (labelWidth / 2.0f) + rawX + ((float) this.labelMargin);
            float top = (rawY - ((float) (labelHeight / 2))) - ((float) this.labelMargin);
            float bottom = ((float) (labelHeight / 2)) + rawY + ((float) this.labelMargin);
            if (top < ((float) contentRect.top)) {
                top = rawY;
                bottom = ((float) labelHeight) + rawY + ((float) (this.labelMargin * 2));
            }
            if (bottom > ((float) contentRect.bottom)) {
                top = (rawY - ((float) labelHeight)) - ((float) (this.labelMargin * 2));
                bottom = rawY;
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
            drawLabelTextAndBackground(canvas, this.labelBuffer, this.labelBuffer.length - numChars, numChars, bubbleValue.getDarkenColor());
        }
    }

    private void calculateMaxViewport() {
        float maxZ = Float.MIN_VALUE;
        this.tempMaximumViewport.set(Float.MAX_VALUE, Float.MIN_VALUE, Float.MIN_VALUE, Float.MAX_VALUE);
        BubbleChartData data = this.dataProvider.getBubbleChartData();
        for (BubbleValue bubbleValue : data.getValues()) {
            if (Math.abs(bubbleValue.getZ()) > maxZ) {
                maxZ = Math.abs(bubbleValue.getZ());
            }
            if (bubbleValue.getX() < this.tempMaximumViewport.left) {
                this.tempMaximumViewport.left = bubbleValue.getX();
            }
            if (bubbleValue.getX() > this.tempMaximumViewport.right) {
                this.tempMaximumViewport.right = bubbleValue.getX();
            }
            if (bubbleValue.getY() < this.tempMaximumViewport.bottom) {
                this.tempMaximumViewport.bottom = bubbleValue.getY();
            }
            if (bubbleValue.getY() > this.tempMaximumViewport.top) {
                this.tempMaximumViewport.top = bubbleValue.getY();
            }
        }
        this.maxRadius = (float) Math.sqrt(((double) maxZ) / 3.141592653589793d);
        this.bubbleScaleX = this.tempMaximumViewport.width() / (this.maxRadius * 4.0f);
        if (this.bubbleScaleX == 0.0f) {
            this.bubbleScaleX = 1.0f;
        }
        this.bubbleScaleY = this.tempMaximumViewport.height() / (this.maxRadius * 4.0f);
        if (this.bubbleScaleY == 0.0f) {
            this.bubbleScaleY = 1.0f;
        }
        this.bubbleScaleX *= data.getBubbleScale();
        this.bubbleScaleY *= data.getBubbleScale();
        this.tempMaximumViewport.inset((-this.maxRadius) * this.bubbleScaleX, (-this.maxRadius) * this.bubbleScaleY);
        this.minRawRadius = (float) ChartUtils.dp2px(this.density, this.dataProvider.getBubbleChartData().getMinBubbleRadius());
    }
}

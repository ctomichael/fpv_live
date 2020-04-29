package lecho.lib.hellocharts.renderer;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import java.lang.reflect.Array;
import lecho.lib.hellocharts.computator.ChartComputator;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.AxisAutoValues;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.util.FloatUtils;
import lecho.lib.hellocharts.view.Chart;

public class AxesRenderer {
    private static final int BOTTOM = 3;
    private static final int DEFAULT_AXIS_MARGIN_DP = 2;
    private static final int LEFT = 1;
    private static final int RIGHT = 2;
    private static final int TOP = 0;
    private static final char[] labelWidthChars = {'0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0'};
    private AxisAutoValues[] autoValuesBufferTab = {new AxisAutoValues(), new AxisAutoValues(), new AxisAutoValues(), new AxisAutoValues()};
    private float[][] autoValuesToDrawTab = ((float[][]) Array.newInstance(Float.TYPE, 4, 0));
    private int axisMargin;
    private Chart chart;
    private ChartComputator computator;
    private float density;
    private Paint.FontMetricsInt[] fontMetricsTab = {new Paint.FontMetricsInt(), new Paint.FontMetricsInt(), new Paint.FontMetricsInt(), new Paint.FontMetricsInt()};
    private float[] labelBaselineTab = new float[4];
    private char[] labelBuffer = new char[64];
    private int[] labelDimensionForMarginsTab = new int[4];
    private int[] labelDimensionForStepsTab = new int[4];
    private Paint[] labelPaintTab = {new Paint(), new Paint(), new Paint(), new Paint()};
    private int[] labelTextAscentTab = new int[4];
    private int[] labelTextDescentTab = new int[4];
    private int[] labelWidthTab = new int[4];
    private Paint[] linePaintTab = {new Paint(), new Paint(), new Paint(), new Paint()};
    private float[][] linesDrawBufferTab = ((float[][]) Array.newInstance(Float.TYPE, 4, 0));
    private float[] nameBaselineTab = new float[4];
    private Paint[] namePaintTab = {new Paint(), new Paint(), new Paint(), new Paint()};
    private float[][] rawValuesTab = ((float[][]) Array.newInstance(Float.TYPE, 4, 0));
    private float scaledDensity;
    private float[] separationLineTab = new float[4];
    private int[] tiltedLabelXTranslation = new int[4];
    private int[] tiltedLabelYTranslation = new int[4];
    private int[] valuesToDrawNumTab = new int[4];
    private AxisValue[][] valuesToDrawTab = ((AxisValue[][]) Array.newInstance(AxisValue.class, 4, 0));

    public AxesRenderer(Context context, Chart chart2) {
        this.chart = chart2;
        this.computator = chart2.getChartComputator();
        this.density = context.getResources().getDisplayMetrics().density;
        this.scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        this.axisMargin = ChartUtils.dp2px(this.density, 2);
        for (int position = 0; position < 4; position++) {
            this.labelPaintTab[position].setStyle(Paint.Style.FILL);
            this.labelPaintTab[position].setAntiAlias(true);
            this.namePaintTab[position].setStyle(Paint.Style.FILL);
            this.namePaintTab[position].setAntiAlias(true);
            this.linePaintTab[position].setStyle(Paint.Style.STROKE);
            this.linePaintTab[position].setAntiAlias(true);
        }
    }

    public void onChartSizeChanged() {
        onChartDataOrSizeChanged();
    }

    public void onChartDataChanged() {
        onChartDataOrSizeChanged();
    }

    private void onChartDataOrSizeChanged() {
        initAxis(this.chart.getChartData().getAxisXTop(), 0);
        initAxis(this.chart.getChartData().getAxisXBottom(), 3);
        initAxis(this.chart.getChartData().getAxisYLeft(), 1);
        initAxis(this.chart.getChartData().getAxisYRight(), 2);
    }

    public void resetRenderer() {
        this.computator = this.chart.getChartComputator();
    }

    private void initAxis(Axis axis, int position) {
        if (axis != null) {
            initAxisAttributes(axis, position);
            initAxisMargin(axis, position);
            initAxisMeasurements(axis, position);
        }
    }

    private void initAxisAttributes(Axis axis, int position) {
        initAxisPaints(axis, position);
        initAxisTextAlignment(axis, position);
        if (axis.hasTiltedLabels()) {
            initAxisDimensionForTiltedLabels(position);
            intiTiltedLabelsTranslation(axis, position);
            return;
        }
        initAxisDimension(position);
    }

    private void initAxisPaints(Axis axis, int position) {
        Typeface typeface = axis.getTypeface();
        if (typeface != null) {
            this.labelPaintTab[position].setTypeface(typeface);
            this.namePaintTab[position].setTypeface(typeface);
        }
        this.labelPaintTab[position].setColor(axis.getTextColor());
        this.labelPaintTab[position].setTextSize((float) ChartUtils.sp2px(this.scaledDensity, axis.getTextSize()));
        this.labelPaintTab[position].getFontMetricsInt(this.fontMetricsTab[position]);
        this.namePaintTab[position].setColor(axis.getTextColor());
        this.namePaintTab[position].setTextSize((float) ChartUtils.sp2px(this.scaledDensity, axis.getTextSize()));
        this.linePaintTab[position].setColor(axis.getLineColor());
        this.labelTextAscentTab[position] = Math.abs(this.fontMetricsTab[position].ascent);
        this.labelTextDescentTab[position] = Math.abs(this.fontMetricsTab[position].descent);
        this.labelWidthTab[position] = (int) this.labelPaintTab[position].measureText(labelWidthChars, 0, axis.getMaxLabelChars());
    }

    private void initAxisTextAlignment(Axis axis, int position) {
        this.namePaintTab[position].setTextAlign(Paint.Align.CENTER);
        if (position == 0 || 3 == position) {
            this.labelPaintTab[position].setTextAlign(Paint.Align.CENTER);
        } else if (1 == position) {
            if (axis.isInside()) {
                this.labelPaintTab[position].setTextAlign(Paint.Align.LEFT);
            } else {
                this.labelPaintTab[position].setTextAlign(Paint.Align.RIGHT);
            }
        } else if (2 != position) {
        } else {
            if (axis.isInside()) {
                this.labelPaintTab[position].setTextAlign(Paint.Align.RIGHT);
            } else {
                this.labelPaintTab[position].setTextAlign(Paint.Align.LEFT);
            }
        }
    }

    private void initAxisDimensionForTiltedLabels(int position) {
        int pythagoreanFromLabelWidth = (int) Math.sqrt(Math.pow((double) this.labelWidthTab[position], 2.0d) / 2.0d);
        this.labelDimensionForMarginsTab[position] = ((int) Math.sqrt(Math.pow((double) this.labelTextAscentTab[position], 2.0d) / 2.0d)) + pythagoreanFromLabelWidth;
        this.labelDimensionForStepsTab[position] = Math.round(((float) this.labelDimensionForMarginsTab[position]) * 0.75f);
    }

    private void initAxisDimension(int position) {
        if (1 == position || 2 == position) {
            this.labelDimensionForMarginsTab[position] = this.labelWidthTab[position];
            this.labelDimensionForStepsTab[position] = this.labelTextAscentTab[position];
        } else if (position == 0 || 3 == position) {
            this.labelDimensionForMarginsTab[position] = this.labelTextAscentTab[position] + this.labelTextDescentTab[position];
            this.labelDimensionForStepsTab[position] = this.labelWidthTab[position];
        }
    }

    private void intiTiltedLabelsTranslation(Axis axis, int position) {
        int pythagoreanFromLabelWidth = (int) Math.sqrt(Math.pow((double) this.labelWidthTab[position], 2.0d) / 2.0d);
        int pythagoreanFromAscent = (int) Math.sqrt(Math.pow((double) this.labelTextAscentTab[position], 2.0d) / 2.0d);
        int dx = 0;
        int dy = 0;
        if (axis.isInside()) {
            if (1 == position) {
                dx = pythagoreanFromAscent;
            } else if (2 == position) {
                dy = (-pythagoreanFromLabelWidth) / 2;
            } else if (position == 0) {
                dy = ((pythagoreanFromLabelWidth / 2) + pythagoreanFromAscent) - this.labelTextAscentTab[position];
            } else if (3 == position) {
                dy = (-pythagoreanFromLabelWidth) / 2;
            }
        } else if (1 == position) {
            dy = (-pythagoreanFromLabelWidth) / 2;
        } else if (2 == position) {
            dx = pythagoreanFromAscent;
        } else if (position == 0) {
            dy = (-pythagoreanFromLabelWidth) / 2;
        } else if (3 == position) {
            dy = ((pythagoreanFromLabelWidth / 2) + pythagoreanFromAscent) - this.labelTextAscentTab[position];
        }
        this.tiltedLabelXTranslation[position] = dx;
        this.tiltedLabelYTranslation[position] = dy;
    }

    private void initAxisMargin(Axis axis, int position) {
        int margin = 0;
        if (!axis.isInside() && (axis.isAutoGenerated() || !axis.getValues().isEmpty())) {
            margin = 0 + this.axisMargin + this.labelDimensionForMarginsTab[position];
        }
        insetContentRectWithAxesMargins(margin + getAxisNameMargin(axis, position), position);
    }

    private int getAxisNameMargin(Axis axis, int position) {
        if (!TextUtils.isEmpty(axis.getName())) {
            return 0 + this.labelTextAscentTab[position] + this.labelTextDescentTab[position] + this.axisMargin;
        }
        return 0;
    }

    private void insetContentRectWithAxesMargins(int axisMargin2, int position) {
        if (1 == position) {
            this.chart.getChartComputator().insetContentRect(axisMargin2, 0, 0, 0);
        } else if (2 == position) {
            this.chart.getChartComputator().insetContentRect(0, 0, axisMargin2, 0);
        } else if (position == 0) {
            this.chart.getChartComputator().insetContentRect(0, axisMargin2, 0, 0);
        } else if (3 == position) {
            this.chart.getChartComputator().insetContentRect(0, 0, 0, axisMargin2);
        }
    }

    private void initAxisMeasurements(Axis axis, int position) {
        if (1 == position) {
            if (axis.isInside()) {
                this.labelBaselineTab[position] = (float) (this.computator.getContentRectMinusAllMargins().left + this.axisMargin);
                this.nameBaselineTab[position] = (float) ((this.computator.getContentRectMinusAxesMargins().left - this.axisMargin) - this.labelTextDescentTab[position]);
            } else {
                this.labelBaselineTab[position] = (float) (this.computator.getContentRectMinusAxesMargins().left - this.axisMargin);
                this.nameBaselineTab[position] = ((this.labelBaselineTab[position] - ((float) this.axisMargin)) - ((float) this.labelTextDescentTab[position])) - ((float) this.labelDimensionForMarginsTab[position]);
            }
            this.separationLineTab[position] = (float) this.computator.getContentRectMinusAllMargins().left;
        } else if (2 == position) {
            if (axis.isInside()) {
                this.labelBaselineTab[position] = (float) (this.computator.getContentRectMinusAllMargins().right - this.axisMargin);
                this.nameBaselineTab[position] = (float) (this.computator.getContentRectMinusAxesMargins().right + this.axisMargin + this.labelTextAscentTab[position]);
            } else {
                this.labelBaselineTab[position] = (float) (this.computator.getContentRectMinusAxesMargins().right + this.axisMargin);
                this.nameBaselineTab[position] = this.labelBaselineTab[position] + ((float) this.axisMargin) + ((float) this.labelTextAscentTab[position]) + ((float) this.labelDimensionForMarginsTab[position]);
            }
            this.separationLineTab[position] = (float) this.computator.getContentRectMinusAllMargins().right;
        } else if (3 == position) {
            if (axis.isInside()) {
                this.labelBaselineTab[position] = (float) ((this.computator.getContentRectMinusAllMargins().bottom - this.axisMargin) - this.labelTextDescentTab[position]);
                this.nameBaselineTab[position] = (float) (this.computator.getContentRectMinusAxesMargins().bottom + this.axisMargin + this.labelTextAscentTab[position]);
            } else {
                this.labelBaselineTab[position] = (float) (this.computator.getContentRectMinusAxesMargins().bottom + this.axisMargin + this.labelTextAscentTab[position]);
                this.nameBaselineTab[position] = this.labelBaselineTab[position] + ((float) this.axisMargin) + ((float) this.labelDimensionForMarginsTab[position]);
            }
            this.separationLineTab[position] = (float) this.computator.getContentRectMinusAllMargins().bottom;
        } else if (position == 0) {
            if (axis.isInside()) {
                this.labelBaselineTab[position] = (float) (this.computator.getContentRectMinusAllMargins().top + this.axisMargin + this.labelTextAscentTab[position]);
                this.nameBaselineTab[position] = (float) ((this.computator.getContentRectMinusAxesMargins().top - this.axisMargin) - this.labelTextDescentTab[position]);
            } else {
                this.labelBaselineTab[position] = (float) ((this.computator.getContentRectMinusAxesMargins().top - this.axisMargin) - this.labelTextDescentTab[position]);
                this.nameBaselineTab[position] = (this.labelBaselineTab[position] - ((float) this.axisMargin)) - ((float) this.labelDimensionForMarginsTab[position]);
            }
            this.separationLineTab[position] = (float) this.computator.getContentRectMinusAllMargins().top;
        } else {
            throw new IllegalArgumentException("Invalid axis position: " + position);
        }
    }

    public void drawInBackground(Canvas canvas) {
        Axis axis = this.chart.getChartData().getAxisYLeft();
        if (axis != null) {
            prepareAxisToDraw(axis, 1);
            drawAxisLines(canvas, axis, 1);
        }
        Axis axis2 = this.chart.getChartData().getAxisYRight();
        if (axis2 != null) {
            prepareAxisToDraw(axis2, 2);
            drawAxisLines(canvas, axis2, 2);
        }
        Axis axis3 = this.chart.getChartData().getAxisXBottom();
        if (axis3 != null) {
            prepareAxisToDraw(axis3, 3);
            drawAxisLines(canvas, axis3, 3);
        }
        Axis axis4 = this.chart.getChartData().getAxisXTop();
        if (axis4 != null) {
            prepareAxisToDraw(axis4, 0);
            drawAxisLines(canvas, axis4, 0);
        }
    }

    private void prepareAxisToDraw(Axis axis, int position) {
        if (axis.isAutoGenerated()) {
            prepareAutoGeneratedAxis(axis, position);
        } else {
            prepareCustomAxis(axis, position);
        }
    }

    public void drawInForeground(Canvas canvas) {
        Axis axis = this.chart.getChartData().getAxisYLeft();
        if (axis != null) {
            drawAxisLabelsAndName(canvas, axis, 1);
        }
        Axis axis2 = this.chart.getChartData().getAxisYRight();
        if (axis2 != null) {
            drawAxisLabelsAndName(canvas, axis2, 2);
        }
        Axis axis3 = this.chart.getChartData().getAxisXBottom();
        if (axis3 != null) {
            drawAxisLabelsAndName(canvas, axis3, 3);
        }
        Axis axis4 = this.chart.getChartData().getAxisXTop();
        if (axis4 != null) {
            drawAxisLabelsAndName(canvas, axis4, 0);
        }
    }

    private void prepareCustomAxis(Axis axis, int position) {
        float viewportMin;
        float viewportMax;
        float rawValue;
        Viewport maxViewport = this.computator.getMaximumViewport();
        Viewport visibleViewport = this.computator.getVisibleViewport();
        Rect contentRect = this.computator.getContentRectMinusAllMargins();
        boolean isAxisVertical = isAxisVertical(position);
        float scale = 1.0f;
        if (isAxisVertical) {
            if (maxViewport.height() > 0.0f && visibleViewport.height() > 0.0f) {
                scale = ((float) contentRect.height()) * (maxViewport.height() / visibleViewport.height());
            }
            viewportMin = visibleViewport.bottom;
            viewportMax = visibleViewport.top;
        } else {
            if (maxViewport.width() > 0.0f && visibleViewport.width() > 0.0f) {
                scale = ((float) contentRect.width()) * (maxViewport.width() / visibleViewport.width());
            }
            viewportMin = visibleViewport.left;
            viewportMax = visibleViewport.right;
        }
        if (scale == 0.0f) {
            scale = 1.0f;
        }
        int module = (int) Math.max(1.0d, Math.ceil((((double) (axis.getValues().size() * this.labelDimensionForStepsTab[position])) * 1.5d) / ((double) scale)));
        if (axis.hasLines() && this.linesDrawBufferTab[position].length < axis.getValues().size() * 4) {
            this.linesDrawBufferTab[position] = new float[(axis.getValues().size() * 4)];
        }
        if (this.rawValuesTab[position].length < axis.getValues().size()) {
            this.rawValuesTab[position] = new float[axis.getValues().size()];
        }
        if (this.valuesToDrawTab[position].length < axis.getValues().size()) {
            this.valuesToDrawTab[position] = new AxisValue[axis.getValues().size()];
        }
        int valueIndex = 0;
        int valueToDrawIndex = 0;
        for (AxisValue axisValue : axis.getValues()) {
            float value = axisValue.getValue();
            if (value >= viewportMin && value <= viewportMax) {
                if (valueIndex % module == 0) {
                    if (isAxisVertical) {
                        rawValue = this.computator.computeRawY(value);
                    } else {
                        rawValue = this.computator.computeRawX(value);
                    }
                    if (checkRawValue(contentRect, rawValue, axis.isInside(), position, isAxisVertical)) {
                        this.rawValuesTab[position][valueToDrawIndex] = rawValue;
                        this.valuesToDrawTab[position][valueToDrawIndex] = axisValue;
                        valueToDrawIndex++;
                    }
                }
                valueIndex++;
            }
        }
        this.valuesToDrawNumTab[position] = valueToDrawIndex;
    }

    private void prepareAutoGeneratedAxis(Axis axis, int position) {
        float start;
        float stop;
        int contentRectDimension;
        float rawValue;
        Viewport visibleViewport = this.computator.getVisibleViewport();
        Rect contentRect = this.computator.getContentRectMinusAllMargins();
        boolean isAxisVertical = isAxisVertical(position);
        if (isAxisVertical) {
            start = visibleViewport.bottom;
            stop = visibleViewport.top;
            contentRectDimension = contentRect.height();
        } else {
            start = visibleViewport.left;
            stop = visibleViewport.right;
            contentRectDimension = contentRect.width();
        }
        FloatUtils.computeAutoGeneratedAxisValues(start, stop, (Math.abs(contentRectDimension) / this.labelDimensionForStepsTab[position]) / 2, this.autoValuesBufferTab[position]);
        if (axis.hasLines() && this.linesDrawBufferTab[position].length < this.autoValuesBufferTab[position].valuesNumber * 4) {
            this.linesDrawBufferTab[position] = new float[(this.autoValuesBufferTab[position].valuesNumber * 4)];
        }
        if (this.rawValuesTab[position].length < this.autoValuesBufferTab[position].valuesNumber) {
            this.rawValuesTab[position] = new float[this.autoValuesBufferTab[position].valuesNumber];
        }
        if (this.autoValuesToDrawTab[position].length < this.autoValuesBufferTab[position].valuesNumber) {
            this.autoValuesToDrawTab[position] = new float[this.autoValuesBufferTab[position].valuesNumber];
        }
        int valueToDrawIndex = 0;
        for (int i = 0; i < this.autoValuesBufferTab[position].valuesNumber; i++) {
            if (isAxisVertical) {
                rawValue = this.computator.computeRawY(this.autoValuesBufferTab[position].values[i]);
            } else {
                rawValue = this.computator.computeRawX(this.autoValuesBufferTab[position].values[i]);
            }
            if (checkRawValue(contentRect, rawValue, axis.isInside(), position, isAxisVertical)) {
                this.rawValuesTab[position][valueToDrawIndex] = rawValue;
                this.autoValuesToDrawTab[position][valueToDrawIndex] = this.autoValuesBufferTab[position].values[i];
                valueToDrawIndex++;
            }
        }
        this.valuesToDrawNumTab[position] = valueToDrawIndex;
    }

    private boolean checkRawValue(Rect rect, float rawValue, boolean axisInside, int position, boolean isVertical) {
        if (!axisInside) {
            return true;
        }
        if (isVertical) {
            float marginBottom = (float) (this.labelTextAscentTab[3] + this.axisMargin);
            float marginTop = (float) (this.labelTextAscentTab[0] + this.axisMargin);
            if (rawValue > ((float) rect.bottom) - marginBottom || rawValue < ((float) rect.top) + marginTop) {
                return false;
            }
            return true;
        }
        float margin = (float) (this.labelWidthTab[position] / 2);
        if (rawValue < ((float) rect.left) + margin || rawValue > ((float) rect.right) - margin) {
            return false;
        }
        return true;
    }

    private void drawAxisLines(Canvas canvas, Axis axis, int position) {
        Rect contentRectMargins = this.computator.getContentRectMinusAxesMargins();
        float separationY2 = 0.0f;
        float separationX2 = 0.0f;
        float separationY1 = 0.0f;
        float separationX1 = 0.0f;
        float lineY2 = 0.0f;
        float lineX2 = 0.0f;
        float lineY1 = 0.0f;
        float lineX1 = 0.0f;
        boolean isAxisVertical = isAxisVertical(position);
        if (1 == position || 2 == position) {
            separationX2 = this.separationLineTab[position];
            separationX1 = separationX2;
            separationY1 = (float) contentRectMargins.bottom;
            separationY2 = (float) contentRectMargins.top;
            lineX1 = (float) contentRectMargins.left;
            lineX2 = (float) contentRectMargins.right;
        } else if (position == 0 || 3 == position) {
            separationX1 = (float) contentRectMargins.left;
            separationX2 = (float) contentRectMargins.right;
            separationY2 = this.separationLineTab[position];
            separationY1 = separationY2;
            lineY1 = (float) contentRectMargins.top;
            lineY2 = (float) contentRectMargins.bottom;
        }
        if (axis.hasSeparationLine()) {
            canvas.drawLine(separationX1, separationY1, separationX2, separationY2, this.labelPaintTab[position]);
        }
        if (axis.hasLines()) {
            int valueToDrawIndex = 0;
            while (valueToDrawIndex < this.valuesToDrawNumTab[position]) {
                if (isAxisVertical) {
                    lineY2 = this.rawValuesTab[position][valueToDrawIndex];
                    lineY1 = lineY2;
                } else {
                    lineX2 = this.rawValuesTab[position][valueToDrawIndex];
                    lineX1 = lineX2;
                }
                this.linesDrawBufferTab[position][(valueToDrawIndex * 4) + 0] = lineX1;
                this.linesDrawBufferTab[position][(valueToDrawIndex * 4) + 1] = lineY1;
                this.linesDrawBufferTab[position][(valueToDrawIndex * 4) + 2] = lineX2;
                this.linesDrawBufferTab[position][(valueToDrawIndex * 4) + 3] = lineY2;
                valueToDrawIndex++;
            }
            canvas.drawLines(this.linesDrawBufferTab[position], 0, valueToDrawIndex * 4, this.linePaintTab[position]);
        }
    }

    private void drawAxisLabelsAndName(Canvas canvas, Axis axis, int position) {
        int charsNumber;
        float labelY = 0.0f;
        float labelX = 0.0f;
        boolean isAxisVertical = isAxisVertical(position);
        if (1 == position || 2 == position) {
            labelX = this.labelBaselineTab[position];
        } else if (position == 0 || 3 == position) {
            labelY = this.labelBaselineTab[position];
        }
        for (int valueToDrawIndex = 0; valueToDrawIndex < this.valuesToDrawNumTab[position]; valueToDrawIndex++) {
            if (axis.isAutoGenerated()) {
                charsNumber = axis.getFormatter().formatValueForAutoGeneratedAxis(this.labelBuffer, this.autoValuesToDrawTab[position][valueToDrawIndex], this.autoValuesBufferTab[position].decimals);
            } else {
                charsNumber = axis.getFormatter().formatValueForManualAxis(this.labelBuffer, this.valuesToDrawTab[position][valueToDrawIndex]);
            }
            if (isAxisVertical) {
                labelY = this.rawValuesTab[position][valueToDrawIndex];
            } else {
                labelX = this.rawValuesTab[position][valueToDrawIndex];
            }
            if (axis.hasTiltedLabels()) {
                canvas.save();
                canvas.translate((float) this.tiltedLabelXTranslation[position], (float) this.tiltedLabelYTranslation[position]);
                canvas.rotate(-45.0f, labelX, labelY);
                canvas.drawText(this.labelBuffer, this.labelBuffer.length - charsNumber, charsNumber, labelX, labelY, this.labelPaintTab[position]);
                canvas.restore();
            } else {
                canvas.drawText(this.labelBuffer, this.labelBuffer.length - charsNumber, charsNumber, labelX, labelY, this.labelPaintTab[position]);
            }
        }
        Rect contentRectMargins = this.computator.getContentRectMinusAxesMargins();
        if (TextUtils.isEmpty(axis.getName())) {
            return;
        }
        if (isAxisVertical) {
            canvas.save();
            canvas.rotate(-90.0f, (float) contentRectMargins.centerY(), (float) contentRectMargins.centerY());
            canvas.drawText(axis.getName(), (float) contentRectMargins.centerY(), this.nameBaselineTab[position], this.namePaintTab[position]);
            canvas.restore();
            return;
        }
        canvas.drawText(axis.getName(), (float) contentRectMargins.centerX(), this.nameBaselineTab[position], this.namePaintTab[position]);
    }

    private boolean isAxisVertical(int position) {
        if (1 == position || 2 == position) {
            return true;
        }
        if (position == 0 || 3 == position) {
            return false;
        }
        throw new IllegalArgumentException("Invalid axis position " + position);
    }
}

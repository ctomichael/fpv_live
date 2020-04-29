package lecho.lib.hellocharts.model;

import android.graphics.PathEffect;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.util.ChartUtils;

public class Line {
    private static final int DEFAULT_AREA_TRANSPARENCY = 64;
    private static final int DEFAULT_LINE_STROKE_WIDTH_DP = 3;
    private static final int DEFAULT_POINT_RADIUS_DP = 6;
    public static final int UNINITIALIZED = 0;
    private int areaTransparency = 64;
    private int color = ChartUtils.DEFAULT_COLOR;
    private int darkenColor = ChartUtils.DEFAULT_DARKEN_COLOR;
    private LineChartValueFormatter formatter = new SimpleLineChartValueFormatter();
    private boolean hasLabels = false;
    private boolean hasLabelsOnlyForSelected = false;
    private boolean hasLines = true;
    private boolean hasPoints = true;
    private boolean isCubic = false;
    private boolean isFilled = false;
    private boolean isSquare = false;
    private PathEffect pathEffect;
    private int pointColor = 0;
    private int pointRadius = 6;
    private ValueShape shape = ValueShape.CIRCLE;
    private int strokeWidth = 3;
    private List<PointValue> values = new ArrayList();

    public Line() {
    }

    public Line(List<PointValue> values2) {
        setValues(values2);
    }

    public Line(Line line) {
        this.color = line.color;
        this.pointColor = line.pointColor;
        this.darkenColor = line.darkenColor;
        this.areaTransparency = line.areaTransparency;
        this.strokeWidth = line.strokeWidth;
        this.pointRadius = line.pointRadius;
        this.hasPoints = line.hasPoints;
        this.hasLines = line.hasLines;
        this.hasLabels = line.hasLabels;
        this.hasLabelsOnlyForSelected = line.hasLabelsOnlyForSelected;
        this.isSquare = line.isSquare;
        this.isCubic = line.isCubic;
        this.isFilled = line.isFilled;
        this.shape = line.shape;
        this.pathEffect = line.pathEffect;
        this.formatter = line.formatter;
        for (PointValue pointValue : line.values) {
            this.values.add(new PointValue(pointValue));
        }
    }

    public void update(float scale) {
        for (PointValue value : this.values) {
            value.update(scale);
        }
    }

    public void finish() {
        for (PointValue value : this.values) {
            value.finish();
        }
    }

    public List<PointValue> getValues() {
        return this.values;
    }

    public void setValues(List<PointValue> values2) {
        if (values2 == null) {
            this.values = new ArrayList();
        } else {
            this.values = values2;
        }
    }

    public int getColor() {
        return this.color;
    }

    public Line setColor(int color2) {
        this.color = color2;
        if (this.pointColor == 0) {
            this.darkenColor = ChartUtils.darkenColor(color2);
        }
        return this;
    }

    public int getPointColor() {
        if (this.pointColor == 0) {
            return this.color;
        }
        return this.pointColor;
    }

    public Line setPointColor(int pointColor2) {
        this.pointColor = pointColor2;
        if (pointColor2 == 0) {
            this.darkenColor = ChartUtils.darkenColor(this.color);
        } else {
            this.darkenColor = ChartUtils.darkenColor(pointColor2);
        }
        return this;
    }

    public int getDarkenColor() {
        return this.darkenColor;
    }

    public int getAreaTransparency() {
        return this.areaTransparency;
    }

    public Line setAreaTransparency(int areaTransparency2) {
        this.areaTransparency = areaTransparency2;
        return this;
    }

    public int getStrokeWidth() {
        return this.strokeWidth;
    }

    public Line setStrokeWidth(int strokeWidth2) {
        this.strokeWidth = strokeWidth2;
        return this;
    }

    public boolean hasPoints() {
        return this.hasPoints;
    }

    public Line setHasPoints(boolean hasPoints2) {
        this.hasPoints = hasPoints2;
        return this;
    }

    public boolean hasLines() {
        return this.hasLines;
    }

    public Line setHasLines(boolean hasLines2) {
        this.hasLines = hasLines2;
        return this;
    }

    public boolean hasLabels() {
        return this.hasLabels;
    }

    public Line setHasLabels(boolean hasLabels2) {
        this.hasLabels = hasLabels2;
        if (hasLabels2) {
            this.hasLabelsOnlyForSelected = false;
        }
        return this;
    }

    public boolean hasLabelsOnlyForSelected() {
        return this.hasLabelsOnlyForSelected;
    }

    public Line setHasLabelsOnlyForSelected(boolean hasLabelsOnlyForSelected2) {
        this.hasLabelsOnlyForSelected = hasLabelsOnlyForSelected2;
        if (hasLabelsOnlyForSelected2) {
            this.hasLabels = false;
        }
        return this;
    }

    public int getPointRadius() {
        return this.pointRadius;
    }

    public Line setPointRadius(int pointRadius2) {
        this.pointRadius = pointRadius2;
        return this;
    }

    public boolean isCubic() {
        return this.isCubic;
    }

    public Line setCubic(boolean isCubic2) {
        this.isCubic = isCubic2;
        if (this.isSquare) {
            setSquare(false);
        }
        return this;
    }

    public boolean isSquare() {
        return this.isSquare;
    }

    public Line setSquare(boolean isSquare2) {
        this.isSquare = isSquare2;
        if (this.isCubic) {
            setCubic(false);
        }
        return this;
    }

    public boolean isFilled() {
        return this.isFilled;
    }

    public Line setFilled(boolean isFilled2) {
        this.isFilled = isFilled2;
        return this;
    }

    public ValueShape getShape() {
        return this.shape;
    }

    public Line setShape(ValueShape shape2) {
        this.shape = shape2;
        return this;
    }

    public PathEffect getPathEffect() {
        return this.pathEffect;
    }

    public void setPathEffect(PathEffect pathEffect2) {
        this.pathEffect = pathEffect2;
    }

    public LineChartValueFormatter getFormatter() {
        return this.formatter;
    }

    public Line setFormatter(LineChartValueFormatter formatter2) {
        if (formatter2 != null) {
            this.formatter = formatter2;
        }
        return this;
    }
}

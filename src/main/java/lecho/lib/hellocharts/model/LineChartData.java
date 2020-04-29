package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.List;

public class LineChartData extends AbstractChartData {
    public static final float DEFAULT_BASE_VALUE = 0.0f;
    private float baseValue = 0.0f;
    private List<Line> lines = new ArrayList();

    public LineChartData() {
    }

    public LineChartData(List<Line> lines2) {
        setLines(lines2);
    }

    public LineChartData(LineChartData data) {
        super(data);
        this.baseValue = data.baseValue;
        for (Line line : data.lines) {
            this.lines.add(new Line(line));
        }
    }

    public static LineChartData generateDummyData() {
        LineChartData data = new LineChartData();
        List<PointValue> values = new ArrayList<>(4);
        values.add(new PointValue(0.0f, 2.0f));
        values.add(new PointValue(1.0f, 4.0f));
        values.add(new PointValue(2.0f, 3.0f));
        values.add(new PointValue(3.0f, 4.0f));
        Line line = new Line(values);
        List<Line> lines2 = new ArrayList<>(1);
        lines2.add(line);
        data.setLines(lines2);
        return data;
    }

    public void update(float scale) {
        for (Line line : this.lines) {
            line.update(scale);
        }
    }

    public void finish() {
        for (Line line : this.lines) {
            line.finish();
        }
    }

    public List<Line> getLines() {
        return this.lines;
    }

    public LineChartData setLines(List<Line> lines2) {
        if (lines2 == null) {
            this.lines = new ArrayList();
        } else {
            this.lines = lines2;
        }
        return this;
    }

    public float getBaseValue() {
        return this.baseValue;
    }

    public LineChartData setBaseValue(float baseValue2) {
        this.baseValue = baseValue2;
        return this;
    }
}

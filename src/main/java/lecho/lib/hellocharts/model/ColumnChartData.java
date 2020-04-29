package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.List;

public class ColumnChartData extends AbstractChartData {
    public static final float DEFAULT_BASE_VALUE = 0.0f;
    public static final float DEFAULT_FILL_RATIO = 0.75f;
    private float baseValue = 0.0f;
    private List<Column> columns = new ArrayList();
    private float fillRatio = 0.75f;
    private boolean isStacked = false;

    public ColumnChartData() {
    }

    public ColumnChartData(List<Column> columns2) {
        setColumns(columns2);
    }

    public ColumnChartData(ColumnChartData data) {
        super(data);
        this.isStacked = data.isStacked;
        this.fillRatio = data.fillRatio;
        for (Column column : data.columns) {
            this.columns.add(new Column(column));
        }
    }

    public static ColumnChartData generateDummyData() {
        ColumnChartData data = new ColumnChartData();
        List<Column> columns2 = new ArrayList<>(4);
        for (int i = 1; i <= 4; i++) {
            List<SubcolumnValue> values = new ArrayList<>(4);
            values.add(new SubcolumnValue((float) i));
            columns2.add(new Column(values));
        }
        data.setColumns(columns2);
        return data;
    }

    public void update(float scale) {
        for (Column column : this.columns) {
            column.update(scale);
        }
    }

    public void finish() {
        for (Column column : this.columns) {
            column.finish();
        }
    }

    public List<Column> getColumns() {
        return this.columns;
    }

    public ColumnChartData setColumns(List<Column> columns2) {
        if (columns2 == null) {
            this.columns = new ArrayList();
        } else {
            this.columns = columns2;
        }
        return this;
    }

    public boolean isStacked() {
        return this.isStacked;
    }

    public ColumnChartData setStacked(boolean isStacked2) {
        this.isStacked = isStacked2;
        return this;
    }

    public float getFillRatio() {
        return this.fillRatio;
    }

    public ColumnChartData setFillRatio(float fillRatio2) {
        if (fillRatio2 < 0.0f) {
            fillRatio2 = 0.0f;
        }
        if (fillRatio2 > 1.0f) {
            fillRatio2 = 1.0f;
        }
        this.fillRatio = fillRatio2;
        return this;
    }

    public float getBaseValue() {
        return this.baseValue;
    }

    public ColumnChartData setBaseValue(float baseValue2) {
        this.baseValue = baseValue2;
        return this;
    }
}

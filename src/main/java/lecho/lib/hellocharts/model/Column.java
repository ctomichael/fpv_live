package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.formatter.ColumnChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleColumnChartValueFormatter;

public class Column {
    private ColumnChartValueFormatter formatter = new SimpleColumnChartValueFormatter();
    private boolean hasLabels = false;
    private boolean hasLabelsOnlyForSelected = false;
    private List<SubcolumnValue> values = new ArrayList();

    public Column() {
    }

    public Column(List<SubcolumnValue> values2) {
        setValues(values2);
    }

    public Column(Column column) {
        this.hasLabels = column.hasLabels;
        this.hasLabelsOnlyForSelected = column.hasLabelsOnlyForSelected;
        this.formatter = column.formatter;
        for (SubcolumnValue columnValue : column.values) {
            this.values.add(new SubcolumnValue(columnValue));
        }
    }

    public void update(float scale) {
        for (SubcolumnValue value : this.values) {
            value.update(scale);
        }
    }

    public void finish() {
        for (SubcolumnValue value : this.values) {
            value.finish();
        }
    }

    public List<SubcolumnValue> getValues() {
        return this.values;
    }

    public Column setValues(List<SubcolumnValue> values2) {
        if (values2 == null) {
            this.values = new ArrayList();
        } else {
            this.values = values2;
        }
        return this;
    }

    public boolean hasLabels() {
        return this.hasLabels;
    }

    public Column setHasLabels(boolean hasLabels2) {
        this.hasLabels = hasLabels2;
        if (hasLabels2) {
            this.hasLabelsOnlyForSelected = false;
        }
        return this;
    }

    public boolean hasLabelsOnlyForSelected() {
        return this.hasLabelsOnlyForSelected;
    }

    public Column setHasLabelsOnlyForSelected(boolean hasLabelsOnlyForSelected2) {
        this.hasLabelsOnlyForSelected = hasLabelsOnlyForSelected2;
        if (hasLabelsOnlyForSelected2) {
            this.hasLabels = false;
        }
        return this;
    }

    public ColumnChartValueFormatter getFormatter() {
        return this.formatter;
    }

    public Column setFormatter(ColumnChartValueFormatter formatter2) {
        if (formatter2 != null) {
            this.formatter = formatter2;
        }
        return this;
    }
}

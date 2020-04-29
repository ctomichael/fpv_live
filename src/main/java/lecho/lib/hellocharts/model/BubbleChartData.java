package lecho.lib.hellocharts.model;

import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.formatter.BubbleChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleBubbleChartValueFormatter;

public class BubbleChartData extends AbstractChartData {
    public static final float DEFAULT_BUBBLE_SCALE = 1.0f;
    public static final int DEFAULT_MIN_BUBBLE_RADIUS_DP = 6;
    private float bubbleScale = 1.0f;
    private BubbleChartValueFormatter formatter = new SimpleBubbleChartValueFormatter();
    private boolean hasLabels = false;
    private boolean hasLabelsOnlyForSelected = false;
    private int minBubbleRadius = 6;
    private List<BubbleValue> values = new ArrayList();

    public BubbleChartData() {
    }

    public BubbleChartData(List<BubbleValue> values2) {
        setValues(values2);
    }

    public BubbleChartData(BubbleChartData data) {
        super(data);
        this.formatter = data.formatter;
        this.hasLabels = data.hasLabels;
        this.hasLabelsOnlyForSelected = data.hasLabelsOnlyForSelected;
        this.minBubbleRadius = data.minBubbleRadius;
        this.bubbleScale = data.bubbleScale;
        for (BubbleValue bubbleValue : data.getValues()) {
            this.values.add(new BubbleValue(bubbleValue));
        }
    }

    public static BubbleChartData generateDummyData() {
        BubbleChartData data = new BubbleChartData();
        List<BubbleValue> values2 = new ArrayList<>(4);
        values2.add(new BubbleValue(0.0f, 20.0f, 15000.0f));
        values2.add(new BubbleValue(3.0f, 22.0f, 20000.0f));
        values2.add(new BubbleValue(5.0f, 25.0f, 5000.0f));
        values2.add(new BubbleValue(7.0f, 30.0f, 30000.0f));
        values2.add(new BubbleValue(11.0f, 22.0f, 10.0f));
        data.setValues(values2);
        return data;
    }

    public void update(float scale) {
        for (BubbleValue value : this.values) {
            value.update(scale);
        }
    }

    public void finish() {
        for (BubbleValue value : this.values) {
            value.finish();
        }
    }

    public List<BubbleValue> getValues() {
        return this.values;
    }

    public BubbleChartData setValues(List<BubbleValue> values2) {
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

    public BubbleChartData setHasLabels(boolean hasLabels2) {
        this.hasLabels = hasLabels2;
        if (hasLabels2) {
            this.hasLabelsOnlyForSelected = false;
        }
        return this;
    }

    public boolean hasLabelsOnlyForSelected() {
        return this.hasLabelsOnlyForSelected;
    }

    public BubbleChartData setHasLabelsOnlyForSelected(boolean hasLabelsOnlyForSelected2) {
        this.hasLabelsOnlyForSelected = hasLabelsOnlyForSelected2;
        if (hasLabelsOnlyForSelected2) {
            this.hasLabels = false;
        }
        return this;
    }

    public int getMinBubbleRadius() {
        return this.minBubbleRadius;
    }

    public void setMinBubbleRadius(int minBubbleRadius2) {
        this.minBubbleRadius = minBubbleRadius2;
    }

    public float getBubbleScale() {
        return this.bubbleScale;
    }

    public void setBubbleScale(float bubbleScale2) {
        this.bubbleScale = bubbleScale2;
    }

    public BubbleChartValueFormatter getFormatter() {
        return this.formatter;
    }

    public BubbleChartData setFormatter(BubbleChartValueFormatter formatter2) {
        if (formatter2 != null) {
            this.formatter = formatter2;
        }
        return this;
    }
}

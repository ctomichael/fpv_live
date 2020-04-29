package lecho.lib.hellocharts.listener;

import lecho.lib.hellocharts.model.PointValue;

public interface LineChartOnValueSelectListener extends OnValueDeselectListener {
    void onValueSelected(int i, int i2, PointValue pointValue);
}

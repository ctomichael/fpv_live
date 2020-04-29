package lecho.lib.hellocharts.listener;

import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SubcolumnValue;

public interface ComboLineColumnChartOnValueSelectListener extends OnValueDeselectListener {
    void onColumnValueSelected(int i, int i2, SubcolumnValue subcolumnValue);

    void onPointValueSelected(int i, int i2, PointValue pointValue);
}

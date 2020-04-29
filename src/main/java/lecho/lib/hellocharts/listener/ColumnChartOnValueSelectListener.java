package lecho.lib.hellocharts.listener;

import lecho.lib.hellocharts.model.SubcolumnValue;

public interface ColumnChartOnValueSelectListener extends OnValueDeselectListener {
    void onValueSelected(int i, int i2, SubcolumnValue subcolumnValue);
}

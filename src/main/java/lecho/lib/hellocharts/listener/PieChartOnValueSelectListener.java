package lecho.lib.hellocharts.listener;

import lecho.lib.hellocharts.model.SliceValue;

public interface PieChartOnValueSelectListener extends OnValueDeselectListener {
    void onValueSelected(int i, SliceValue sliceValue);
}
